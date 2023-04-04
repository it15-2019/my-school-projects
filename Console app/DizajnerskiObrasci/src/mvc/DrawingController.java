package mvc;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import adapter.HexagonAdapter;
import command.AddShapeCmd;
import command.BringToBackCmd;
import command.BringToFrontCmd;
import command.CmdList;
import command.ToBackCmd;
import command.ToFrontCmd;
import command.Command;
import command.DeselectShapeCmd;
import command.DeleteShapeCmd;
import command.SelectShapeCmd;
import command.UpdateCircleCmd;
import command.UpdateDonutCmd;
import command.UpdateHexagonCmd;
import command.UpdateLineCmd;
import command.UpdatePointCmd;
import command.UpdateRectangleCmd;
import drawing.DlgCircle;
import drawing.DlgDelete;
import drawing.DlgDonut;
import drawing.DlgHexagon;
import drawing.DlgLine;
import drawing.DlgPoint;
import drawing.DlgRectangle;
import geometry.Circle;
import geometry.Donut;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;
import observer.ObserverUpdate;
import strategy.ManagerStrategy;
import strategy.RadiusException;
import strategy.SaveLogStrategy;
import strategy.SaveShapesStrategy;
import observer.DeleteModifyObserver;

public class DrawingController {
	
	public DrawingModel model;
	public DrawingFrame frame;
	
	private List<Shape> selectedShapes = new ArrayList<Shape>();
	private CmdList cmdList = new CmdList();
	
	private DeleteModifyObserver tglObserver = new DeleteModifyObserver();
	private ObserverUpdate observerUpdate;
	
	private Shape selected = null;
	private Shape isSelected = null;
	
	private ArrayList<String> logList = new ArrayList<String>();
	private boolean logEnd = false;
	private int logCounter = 0;
	private String logReaderLine;
	
	private int tempCmd;
	
	private DlgPoint dlgPoint = new DlgPoint();
	private DlgLine dlgLine = new DlgLine();
	private DlgRectangle dlgRectangle = new DlgRectangle();
	private DlgCircle dlgCircle = new DlgCircle();
	private DlgDonut dlgDonut = new DlgDonut();
	private DlgHexagon dlgHexagon = new DlgHexagon();
	private DlgDelete dlgDelete = new DlgDelete();
	
	public DrawingController(DrawingModel model, DrawingFrame frame)
	{
		this.model = model;
		this.frame = frame;
		observerUpdate = new ObserverUpdate(frame);
		tglObserver.addPropertyChangeListener(observerUpdate);
	}
	
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	Shape temp;
	Point startPoint;
	Point upperLeftPoint;
	
//____________________________ Selektovanje - funkcionalnost __________________________________

	public void mouseClicked(MouseEvent e) 
	{
		if(frame.getTglSelect().isSelected())
		{
			if (model.getShapes().size() == 0)
			{
				JOptionPane.showMessageDialog(null, "You have to draw shape first!");	
			}
			
			isSelected = null;
			selected = null;
			
			ListIterator<Shape> it = model.getShapes().listIterator();
			while(it.hasNext())
			{
				isSelected = it.next();
				if(isSelected.contains(e.getX(), e.getY()))
				{
					selected = isSelected;
				}
			}
			if(selected != null)
			{
				if(selected.isSelected())
				{
					DeselectShapeCmd deselectCmd = new DeselectShapeCmd(this, selected);
					deselectCmd.execute();
					
					frame.getTextArea().append("Deselect - " + selected + "\n");
					undoRedo(deselectCmd, selected, "Deselect", "Deselect");
					
					frame.getTglUndo().setEnabled(true);
					frame.getTglRedo().setEnabled(false);
				}
				else 
				{
					SelectShapeCmd selectCmd = new SelectShapeCmd(this, selected); 
					selectCmd.execute();
					
					frame.getTextArea().append("Select - " + selected + "\n");

					undoRedo(selectCmd, selected, "Select", "Select");
					
					frame.getTglUndo().setEnabled(true);
					frame.getTglRedo().setEnabled(false);
				}
			}
			else
			{
				ListIterator<Shape> it2 = selectedShapes.listIterator();
				while(it2.hasNext())
				{
					isSelected = it2.next();
					isSelected.setSelected(false);
				}
				selectedShapes.clear();
			}
			frame.getView().repaint();
		}
		else if (frame.getState() == 1) 
		{
			pointDraw(e);
		} 
		else if (frame.getState() == 2) 
		{
			lineDraw(e);
		} 
		else if (frame.getState() == 3) 
		{
			rectangleDraw(e);
		}
		else if (frame.getState() == 4) 
		{
			circleDraw(e);
		} 
		else if (frame.getState() == 5) 
		{
			donutDraw(e);
		} 
		else if (frame.getState() == 6) 
		{
			hegaxonDraw(e);
		}
		else 
		{
			JOptionPane.showMessageDialog(null, "You have to pick shape first!");	
		}
		deleteModify();
		frame.getView().repaint();
	}
	
//______________________________________________________________________	
	
	public void selectShape(MouseEvent me) 
	{
		Shape selected = null;
		Iterator<Shape> it = shapes.iterator();
		while (it.hasNext()) 
		{
			Shape shape = it.next();
			shape.setSelected(false);
			if (shape.contains(me.getX(), me.getY())) 
			{
				selected = shape;
			}
		}
		if (selected != null) 
		{
			selected.setSelected(true);
		}
		frame.getView().repaint();
	}
	
//____________________________ Crtanje - funkcionalnost __________________________________

	public void pointDraw(MouseEvent me) 
	{
		
		Point p = new Point(me.getX(), me.getY(), frame.getBtnBorderColor().getBackground());
		AddShapeCmd addCmd = new AddShapeCmd(model, p);
		addCmd.execute();
		
		frame.getTextArea().append("Draw - " + p + "\n");
		undoRedo(addCmd, p, "Draw", "Draw");
		
		frame.getTglUndo().setEnabled(true);
		frame.getTglRedo().setEnabled(false);
	}

//______________________________________________________________________
	
	public void lineDraw(MouseEvent me) 
	{
		if(startPoint == null) 
		{
			startPoint = new Point(me.getX(), me.getY());
		}
		else
		{
			
			Line l = new Line(startPoint, new Point(me.getX(), me.getY()), frame.getBtnBorderColor().getBackground());
			AddShapeCmd addCmd = new AddShapeCmd(model, l);
			addCmd.execute();
			
			frame.getTextArea().append("Draw - " + l + "\n");
			undoRedo(addCmd, l, "Draw","Draw");
			
			frame.getTglUndo().setEnabled(true);
			frame.getTglRedo().setEnabled(false);
				
			startPoint = null;
		}
	}

//______________________________________________________________________

	public void rectangleDraw(MouseEvent me) 
	{
		DlgRectangle dlgRectangle = new DlgRectangle();
		dlgRectangle.getTxtX().setText(Integer.toString(me.getX()));
		dlgRectangle.getTxtY().setText(Integer.toString(me.getY()));
		dlgRectangle.getBtnBorderColor().setBackground(frame.getBtnBorderColor().getBackground());
		dlgRectangle.getBtnInnerColor().setBackground(frame.getBtnInnerColor().getBackground());
		
		if(dlgRectangle.getBtnBorderColor().getBackground().equals(Color.BLACK)) {
			dlgRectangle.getBtnBorderColor().setForeground(Color.WHITE);
		}
		else
			dlgRectangle.getBtnBorderColor().setForeground(Color.BLACK);
		
		if(dlgRectangle.getBtnInnerColor().getBackground().equals(Color.BLACK)) {
			dlgRectangle.getBtnInnerColor().setForeground(Color.WHITE);
		}
		else
			dlgRectangle.getBtnInnerColor().setForeground(Color.BLACK);
		
		dlgRectangle.setVisible(true);
		if(dlgRectangle.isConfirmation()) 
		{
			int width = Integer.parseInt(dlgRectangle.getTxtWidth().getText());
			int height = Integer.parseInt(dlgRectangle.getTxtHeight().getText());
			Rectangle r = new Rectangle(new Point(me.getX(), me.getY()), width, height, dlgRectangle.getBtnBorderColor().getBackground(), dlgRectangle.getBtnInnerColor().getBackground());
			
			r.setSelected(false);
			
			try 
			{
				AddShapeCmd addCmd = new AddShapeCmd(model, r);
				addCmd.execute();
				
				frame.getTextArea().append("Draw - " + r + "\n");
				undoRedo(addCmd, r, "Draw", "Draw");
				
				frame.getTglUndo().setEnabled(true);
				frame.getTglRedo().setEnabled(false);
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(frame, "Wrong value!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		dlgRectangle.getTxtWidth().setText("");
		dlgRectangle.getTxtHeight().setText("");
	}

//______________________________________________________________________

	public void circleDraw(MouseEvent me) 
	{
		DlgCircle dlgCircle = new DlgCircle();
		dlgCircle.getTxtX().setText(Integer.toString(me.getX()));
		dlgCircle.getTxtY().setText(Integer.toString(me.getY()));
		dlgCircle.getBtnBorderColor().setBackground(frame.getBtnBorderColor().getBackground());
		dlgCircle.getBtnInnerColor().setBackground(frame.getBtnInnerColor().getBackground());
		
		if(dlgCircle.getBtnBorderColor().getBackground().equals(Color.BLACK)) {
			dlgCircle.getBtnBorderColor().setForeground(Color.WHITE);
		}
		else
			dlgCircle.getBtnBorderColor().setForeground(Color.BLACK);
		
		if(dlgCircle.getBtnInnerColor().getBackground().equals(Color.BLACK)) {
			dlgCircle.getBtnInnerColor().setForeground(Color.WHITE);
		}
		else
			dlgCircle.getBtnInnerColor().setForeground(Color.BLACK);
		
		dlgCircle.setVisible(true);
		if(dlgCircle.isConfirmation())
		{
			int radius = Integer.parseInt(dlgCircle.getTxtRadius().getText());
			Circle c = new Circle(new Point(me.getX(), me.getY()), radius, dlgCircle.getBtnBorderColor().getBackground(), dlgCircle.getBtnInnerColor().getBackground());
			
			c.setSelected(false);
			
			try 
			{
				AddShapeCmd addCmd = new AddShapeCmd(model, c);
				addCmd.execute();
				
				frame.getTextArea().append("Draw - " + c + "\n");
				undoRedo(addCmd, c, "Draw", "Draw");
				
				frame.getTglUndo().setEnabled(true);
				frame.getTglRedo().setEnabled(false);
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(frame, "Wrong value!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		dlgCircle.getTxtRadius().setText("");
	}
	
//______________________________________________________________________

	public void donutDraw(MouseEvent me) 
	{
		DlgDonut dlgDonut = new DlgDonut();
		dlgDonut.getTxtX().setText(Integer.toString(me.getX()));
		dlgDonut.getTxtY().setText(Integer.toString(me.getY()));
		dlgDonut.getBtnBorderColor().setBackground(frame.getBtnBorderColor().getBackground());
		dlgDonut.getBtnInnerColor().setBackground(frame.getBtnInnerColor().getBackground());
		
		if(dlgDonut.getBtnBorderColor().getBackground().equals(Color.BLACK)) {
			dlgDonut.getBtnBorderColor().setForeground(Color.WHITE);
		}
		else
			dlgDonut.getBtnBorderColor().setForeground(Color.BLACK);
		
		if(dlgDonut.getBtnInnerColor().getBackground().equals(Color.BLACK)) {
			dlgDonut.getBtnInnerColor().setForeground(Color.WHITE);
		}
		else
			dlgDonut.getBtnInnerColor().setForeground(Color.BLACK);
		
		dlgDonut.setVisible(true);
		if(dlgDonut.isConfirmation())
		{
			int radius = Integer.parseInt(dlgDonut.getTxtRadius().getText());
			int innerRadius = Integer.parseInt(dlgDonut.getTxtInnerRadius().getText());
			Donut d = new Donut(new Point(me.getX(), me.getY()), radius, innerRadius, false, dlgDonut.getBtnBorderColor().getBackground(), dlgDonut.getBtnInnerColor().getBackground());
			
			try 
			{
				AddShapeCmd addCmd = new AddShapeCmd(model, d);
				addCmd.execute();
				
				frame.getTextArea().append("Draw - " + d + "\n");
				undoRedo(addCmd, d, "Draw", "Draw");
				
				frame.getTglUndo().setEnabled(true);
				frame.getTglRedo().setEnabled(false);
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(frame, "Wrong value!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		dlgDonut.getTxtRadius().setText("");
		dlgDonut.getTxtInnerRadius().setText("");
	}
	
//______________________________________________________________________

	public void hegaxonDraw(MouseEvent me) 
	{
		DlgHexagon dlgHexagon = new DlgHexagon();
		dlgHexagon.getTxtX().setText(Integer.toString(me.getX()));
		dlgHexagon.getTxtY().setText(Integer.toString(me.getY()));
		dlgHexagon.getBtnBorderColor().setBackground(frame.getBtnBorderColor().getBackground());
		dlgHexagon.getBtnInnerColor().setBackground(frame.getBtnInnerColor().getBackground());
		
		if(dlgHexagon.getBtnBorderColor().getBackground().equals(Color.BLACK)) {
			dlgHexagon.getBtnBorderColor().setForeground(Color.WHITE);
		}
		else
			dlgHexagon.getBtnBorderColor().setForeground(Color.BLACK);
		
		if(dlgHexagon.getBtnInnerColor().getBackground().equals(Color.BLACK)) {
			dlgHexagon.getBtnInnerColor().setForeground(Color.WHITE);
		}
		else
			dlgHexagon.getBtnInnerColor().setForeground(Color.BLACK);
		
		dlgHexagon.setVisible(true);
		if(dlgHexagon.isConfirmation())
		{
			int radius = Integer.parseInt(dlgHexagon.getTxtRadius().getText());
			HexagonAdapter h = new HexagonAdapter(new Point(me.getX(), me.getY()), radius, dlgHexagon.getBtnBorderColor().getBackground(), dlgHexagon.getBtnInnerColor().getBackground());
			
			h.setSelected(false);
			
			try 
			{
				AddShapeCmd addCmd = new AddShapeCmd(model, h);
				addCmd.execute();
				
				frame.getTextArea().append("Draw - " + h + "\n");
				undoRedo(addCmd, h, "","Draw");
				
				frame.getTglUndo().setEnabled(true);
				frame.getTglRedo().setEnabled(false);
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(frame, "Wrong value!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		dlgHexagon.getTxtRadius().setText("");
	}

//___________________________ Modifikacija - funkcionalnost __________________________________
	
	public void modify() {
		
		if (selectedShapes.size() == 0) 
		{
			JOptionPane.showMessageDialog(frame, "Have to selected shape first!", "Error", JOptionPane.ERROR_MESSAGE);

		}
		else 
		{ 
			if (selectedShapes.get(0) instanceof Point) 
			{
				Point temp = (Point)selectedShapes.get(0);
				dlgPoint.getTxtX().setText(Integer.toString(temp.getX()));
				dlgPoint.getTxtY().setText(Integer.toString(temp.getY()));
				dlgPoint.getBtnColor().setBackground(temp.getColor());
				
				if (dlgPoint.getBtnColor().getBackground() == Color.BLACK)
					dlgPoint.getBtnColor().setForeground(Color.WHITE);
				else
					dlgPoint.getBtnColor().setForeground(Color.BLACK);
	
				dlgPoint.setVisible(true);
				
				if (dlgPoint.isConfirmation()) {
					Point p = new Point(Integer.parseInt(dlgPoint.getTxtX().getText()),
							  		    Integer.parseInt(dlgPoint.getTxtY().getText()), dlgPoint.getBtnColor().getBackground());
					UpdatePointCmd pointCmd = new UpdatePointCmd(temp, p);
					pointCmd.execute();
					
					frame.getTextArea().append("Modify - " + p + "\n");
					undoRedo(pointCmd, p, "Modify", "Modify");
					frame.getView().repaint();
				}
			}

			//______________________________________________________________________
			
			else if (selectedShapes.get(0) instanceof Line) 
			{
				Line temp = (Line)selectedShapes.get(0);
				dlgLine.getTxtStartPointX().setText(Integer.toString(temp.getStartPoint().getX()));
				dlgLine.getTxtStartPointY().setText(Integer.toString(temp.getStartPoint().getY()));
				dlgLine.getTxtEndPointX().setText(Integer.toString(temp.getEndPoint().getX()));
				dlgLine.getTxtEndPointY().setText(Integer.toString(temp.getEndPoint().getY()));
				dlgLine.getBtnColor().setBackground(temp.getColor());
				if (dlgLine.getBtnColor().getBackground() == Color.BLACK)
					dlgLine.getBtnColor().setForeground(Color.WHITE);
				else
					dlgLine.getBtnColor().setForeground(Color.BLACK);
	
				dlgLine.setVisible(true);
				if (dlgLine.isConfirmation()) {
					Line l = new Line(new Point(Integer.parseInt(dlgLine.getTxtStartPointX().getText()), Integer.parseInt(dlgLine.getTxtStartPointY().getText())), 
									  new Point(Integer.parseInt(dlgLine.getTxtEndPointX().getText()), Integer.parseInt(dlgLine.getTxtEndPointY().getText())), 
									  dlgLine.getBtnColor().getBackground());
					UpdateLineCmd lineCmd = new UpdateLineCmd(temp, l);
					lineCmd.execute();
					
					frame.getTextArea().append("Modify - " + l + "\n");
					undoRedo(lineCmd, l, "Modify", "Modify");
					frame.getView().repaint();
				}
			}

			//______________________________________________________________________
			
			else if (selectedShapes.get(0) instanceof Rectangle) 
			{
				Rectangle temp = (Rectangle)selectedShapes.get(0);
				dlgRectangle.getTxtX().setText(Integer.toString(temp.getUpperLeftPoint().getX()));
				dlgRectangle.getTxtY().setText(Integer.toString(temp.getUpperLeftPoint().getY()));
				dlgRectangle.getTxtHeight().setText(Integer.toString(temp.getHeight()));
				dlgRectangle.getTxtWidth().setText(Integer.toString(temp.getWidth()));
				dlgRectangle.getBtnInnerColor().setBackground(temp.getInnerColor());
				dlgRectangle.getBtnBorderColor().setBackground(temp.getColor());
	
				if(dlgRectangle.getBtnBorderColor().getBackground().equals(Color.BLACK)) {
					dlgRectangle.getBtnBorderColor().setForeground(Color.WHITE);
				}
				else
					dlgRectangle.getBtnBorderColor().setForeground(Color.BLACK);
				
				if(dlgRectangle.getBtnInnerColor().getBackground().equals(Color.BLACK)) {
					dlgRectangle.getBtnInnerColor().setForeground(Color.WHITE);
				}
				else
					dlgRectangle.getBtnInnerColor().setForeground(Color.BLACK);
				
				dlgRectangle.setVisible(true);
				if (dlgRectangle.isConfirmation()) {
					Rectangle r = new Rectangle(new Point(Integer.parseInt(dlgRectangle.getTxtX().getText()), Integer.parseInt(dlgRectangle.getTxtY().getText())),
												Integer.parseInt(dlgRectangle.getTxtWidth().getText()),
												Integer.parseInt(dlgRectangle.getTxtHeight().getText()),
												dlgRectangle.getBtnBorderColor().getBackground(),
												dlgRectangle.getBtnInnerColor().getBackground());
					UpdateRectangleCmd rectangleCmd = new UpdateRectangleCmd(temp, r);
					rectangleCmd.execute();
					
					frame.getTextArea().append("Modify - " + r + "\n");
					undoRedo(rectangleCmd, r, "Modify", "Modify");
					frame.getView().repaint();
				}
			}

			//______________________________________________________________________
					
			else if (selectedShapes.get(0) instanceof Donut) 
			{
				Donut temp = (Donut)selectedShapes.get(0);
				dlgDonut.getTxtX().setText(Integer.toString(temp.getCenter().getX()));
				dlgDonut.getTxtY().setText(Integer.toString(temp.getCenter().getY()));
				dlgDonut.getTxtInnerRadius().setText(Integer.toString(temp.getInnerRadius()));
				dlgDonut.getTxtRadius().setText(Integer.toString(temp.getRadius()));
				dlgDonut.getBtnInnerColor().setBackground(temp.getInnerColor());
				dlgDonut.getBtnBorderColor().setBackground(temp.getColor());
				
				if(dlgDonut.getBtnBorderColor().getBackground().equals(Color.BLACK)) {
					dlgDonut.getBtnBorderColor().setForeground(Color.WHITE);
				}
				else
					dlgDonut.getBtnBorderColor().setForeground(Color.BLACK);
				
				if(dlgDonut.getBtnInnerColor().getBackground().equals(Color.BLACK)) {
					dlgDonut.getBtnInnerColor().setForeground(Color.WHITE);
				}
				else
					dlgDonut.getBtnInnerColor().setForeground(Color.BLACK);
				
				dlgDonut.setVisible(true);
				if (dlgDonut.isConfirmation()) {
					Donut d = new Donut(new Point (Integer.parseInt(dlgDonut.getTxtX().getText()), Integer.parseInt(dlgDonut.getTxtY().getText())),
										Integer.parseInt(dlgDonut.getTxtRadius().getText()),
										Integer.parseInt(dlgDonut.getTxtInnerRadius().getText()),
										dlgDonut.getBtnBorderColor().getBackground(), 
										dlgDonut.getBtnInnerColor().getBackground());
					UpdateDonutCmd donutCmd = new UpdateDonutCmd(temp, d);
					donutCmd.execute();
					
					frame.getTextArea().append("Modify - " + d + "\n");
					undoRedo(donutCmd, d, "Modify", "Modify");
					frame.getView().repaint();
				}
			}
			
			//______________________________________________________________________
			
			else if (selectedShapes.get(0) instanceof Circle) 
			{
				Circle temp = (Circle)selectedShapes.get(0);
				dlgCircle.getTxtX().setText(Integer.toString(temp.getCenter().getX()));
				dlgCircle.getTxtY().setText(Integer.toString(temp.getCenter().getY()));
				dlgCircle.getTxtRadius().setText(Integer.toString(temp.getRadius()));
				dlgCircle.getBtnInnerColor().setBackground(temp.getInnerColor());
				dlgCircle.getBtnBorderColor().setBackground(temp.getColor());
				
				if(dlgCircle.getBtnBorderColor().getBackground().equals(Color.BLACK)) {
					dlgCircle.getBtnBorderColor().setForeground(Color.WHITE);
				}
				else
					dlgCircle.getBtnBorderColor().setForeground(Color.BLACK);
				
				if(dlgCircle.getBtnInnerColor().getBackground().equals(Color.BLACK)) {
					dlgCircle.getBtnInnerColor().setForeground(Color.WHITE);
				}
				else
					dlgCircle.getBtnInnerColor().setForeground(Color.BLACK);
				
				dlgCircle.setVisible(true);
				if (dlgCircle.isConfirmation()) {
					Circle c = new Circle(new Point (Integer.parseInt(dlgCircle.getTxtX().getText()), Integer.parseInt(dlgCircle.getTxtY().getText())),
										  Integer.parseInt(dlgCircle.getTxtRadius().getText()),
										  dlgCircle.getBtnBorderColor().getBackground(), 
										  dlgCircle.getBtnInnerColor().getBackground());
					UpdateCircleCmd circleCmd = new UpdateCircleCmd(temp, c);
					circleCmd.execute();
					
					frame.getTextArea().append("Modify - " + c + "\n");
					undoRedo(circleCmd, c, "Modify", "Modify");
					frame.getView().repaint();
				}
			}

			//______________________________________________________________________
			
			else if (selectedShapes.get(0) instanceof HexagonAdapter) 
			{
				HexagonAdapter temp = (HexagonAdapter)selectedShapes.get(0);
				dlgHexagon.getTxtX().setText(Integer.toString(temp.getCenter().getX()));
				dlgHexagon.getTxtY().setText(Integer.toString(temp.getCenter().getY()));
				dlgHexagon.getTxtRadius().setText(Integer.toString(temp.getRadius()));
				dlgHexagon.getBtnInnerColor().setBackground(temp.getHexagon().getAreaColor());
				dlgHexagon.getBtnBorderColor().setBackground(temp.getHexagon().getBorderColor());
				
				if(dlgHexagon.getBtnBorderColor().getBackground().equals(Color.BLACK)) {
					dlgHexagon.getBtnBorderColor().setForeground(Color.WHITE);
				}
				else
					dlgHexagon.getBtnBorderColor().setForeground(Color.BLACK);
				
				if(dlgHexagon.getBtnInnerColor().getBackground().equals(Color.BLACK)) {
					dlgHexagon.getBtnInnerColor().setForeground(Color.WHITE);
				}
				else
					dlgHexagon.getBtnInnerColor().setForeground(Color.BLACK);
				
				dlgHexagon.setVisible(true);
				if (dlgHexagon.isConfirmation()) {
					HexagonAdapter h = new HexagonAdapter(new Point (Integer.parseInt(dlgHexagon.getTxtX().getText()), Integer.parseInt(dlgHexagon.getTxtY().getText())),
											   			  Integer.parseInt(dlgHexagon.getTxtRadius().getText()),
											   			  dlgHexagon.getBtnBorderColor().getBackground(), 
											   			  dlgHexagon.getBtnInnerColor().getBackground());
					UpdateHexagonCmd hexagonCmd = new UpdateHexagonCmd(temp, h);
					hexagonCmd.execute();
					
					frame.getTextArea().append("Modify - " + h + "\n");
					undoRedo(hexagonCmd, h, "Modify", "Modify");
					frame.getView().repaint();
				}
			}
		}
	}
	
//___________________________ Brisanje - funkcionalnost __________________________________

	public void delete() 
	{
		for (int i = selectedShapes.size() - 1; i >= 0; i--) 
		{
			dlgDelete.setMessage(new JLabel("Are you sure you want to delete selected shape?"));
			
			dlgDelete.setVisible(true);
			
			if (dlgDelete.isConfirmation())
			{	
				if (selectedShapes.get(i) instanceof Point) 
				{
					Point p = (Point)selectedShapes.get(i);
					DeleteShapeCmd deleteCmd = new DeleteShapeCmd(model, p);
					deleteCmd.execute();
					
					frame.getTextArea().append("Delete - " + p + "\n");
					undoRedo(deleteCmd, p, "Delete", "Delete");
					frame.getView().repaint();
					selectedShapes.remove(i);
				}
				else if (selectedShapes.get(i) instanceof Line) 
				{
					Line l = (Line)selectedShapes.get(i);
					DeleteShapeCmd deleteCmd = new DeleteShapeCmd(model, l);
					deleteCmd.execute();
					
					frame.getTextArea().append("Delete - " + l + "\n");
					undoRedo(deleteCmd, l, "Delete", "Delete");
					frame.getView().repaint();
					selectedShapes.remove(i);
				}
				else if (selectedShapes.get(i) instanceof Rectangle) 
				{
					Rectangle r = (Rectangle)selectedShapes.get(i);
					DeleteShapeCmd deleteCmd = new DeleteShapeCmd(model, r);
					deleteCmd.execute();
					
					frame.getTextArea().append("Delete - " + r + "\n");
					undoRedo(deleteCmd, r, "Delete", "Delete");
					frame.getView().repaint();
					selectedShapes.remove(i);
				}
				else if (selectedShapes.get(i) instanceof Circle) 
				{
					Circle c = (Circle)selectedShapes.get(i);
					DeleteShapeCmd deleteCmd = new DeleteShapeCmd(model, c);
					deleteCmd.execute();
					
					frame.getTextArea().append("Delete - " + c + "\n");
					undoRedo(deleteCmd, c, "Delete", "Delete");
					frame.getView().repaint();
					selectedShapes.remove(i);
				}
				else if (selectedShapes.get(i) instanceof Donut) 
				{
					Donut d = (Donut)selectedShapes.get(i);
					DeleteShapeCmd deleteCmd = new DeleteShapeCmd(model, d);
					deleteCmd.execute();
					
					frame.getTextArea().append("Delete - " + d + "\n");
					undoRedo(deleteCmd, d, "Delete", "Delete");
					frame.getView().repaint();
					selectedShapes.remove(i);
				}
				else if (selectedShapes.get(i) instanceof HexagonAdapter)
				{
					HexagonAdapter h = (HexagonAdapter)selectedShapes.get(i);
					DeleteShapeCmd deleteCmd = new DeleteShapeCmd(model, h);
					deleteCmd.execute();
					
					frame.getTextArea().append("Delete - " + h + "\n");
					undoRedo(deleteCmd, h, "Delete", "Delete");
					frame.getView().repaint();
					selectedShapes.remove(i);
				}
			}
			else
				continue;
		}
		if(selectedShapes.size() == 0)
		{
			frame.getTglUndo().setEnabled(true);
			frame.getTglRedo().setEnabled(false);
		}
	}
	
//___________________________ Delete/modify - dostupnost btn-a _________________________________

	public void deleteModify() {
		if(selectedShapes.size() !=0)
		{
			if(selectedShapes.size()==1)
			{
				tglObserver.setModifyEnabled(true);
			} 
			else 
			{
				tglObserver.setModifyEnabled(false);
			}
			tglObserver.setDeleteEnabled(true);
		} 
		else 
		{
			tglObserver.setModifyEnabled(false);
			tglObserver.setDeleteEnabled(false);
		}
	}
	
	
	
//___________________________ Undo/redo - funkcionalnost _________________________________
	
	
	public void undoRedo(Command comand, Shape shape, String undo, String redo)
	{
		cmdList.deleteElementsAfterPointer(cmdList.getCurrent() - 1);
		cmdList.getList().add(comand);
		cmdList.getShapes().add(shape);
		cmdList.getLogUndo().add(undo);
		cmdList.getLogRedo().add(redo);
		cmdList.setCurrent(cmdList.getCurrent()+1);
	}
	
	public void undoShape() {
		
		frame.getTextArea().append("Undo " + cmdList.getLogUndo().get(cmdList.getCurrent()-1) + " - " + cmdList.getShapes().get(cmdList.getCurrent()-1) + "\n");
		if(cmdList.getLogUndo().get(cmdList.getCurrent()-1).equals("Draw")) {
			SelectShapeCmd selectCmd = new SelectShapeCmd(this,cmdList.getShapes().get(cmdList.getCurrent()-1));
			selectCmd.execute();	
		}
		
		cmdList.undo();
		deleteModify();
		frame.getView().repaint();
	}
	
	public void redoShape() {
		
		frame.getTextArea().append("Redo " + cmdList.getLogRedo().get(cmdList.getCurrent()) + " - "+cmdList.getShapes().get(cmdList.getCurrent())+"\n");
		if(cmdList.getLogRedo().get(cmdList.getCurrent()).equals("Draw"))
		{
			DeselectShapeCmd cmdShapeDeselect = new DeselectShapeCmd(this,cmdList.getShapes().get(cmdList.getCurrent()));
			cmdShapeDeselect.execute();
		}
		cmdList.redo();
		deleteModify();
		frame.getView().repaint();
	}
	
//___________________________ To front/back - funkcionalnost _________________________________

	public void ToFront() {
		if (selectedShapes.size() < 1 || selectedShapes.size() > 1) 
		{
			JOptionPane.showMessageDialog(frame, "You have to select just one shape!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else if (model.getShapes().size() <= 1)
		{
			JOptionPane.showMessageDialog(frame, "You have to draw at least 2 shapes!", "Error", JOptionPane.ERROR_MESSAGE);

		}
		else 
		{
			frame.getTglRedo().setEnabled(false);
			ToFrontCmd toFrontCmd = new ToFrontCmd(model,selectedShapes.get(0));
			toFrontCmd.execute();
			frame.getTextArea().append("To front - " + selectedShapes.get(0)+"\n");
			undoRedo(toFrontCmd, selectedShapes.get(0), "To front", "To front");	
			frame.getView().repaint();
		}
	}
	
	public void ToBack() {
		if (selectedShapes.size() < 1 || selectedShapes.size() > 1) 
		{
			JOptionPane.showMessageDialog(frame, "You have to select just one shape!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else if (model.getShapes().size() <= 1)
		{
			JOptionPane.showMessageDialog(frame, "You have to draw at least 2 shapes!", "Error", JOptionPane.ERROR_MESSAGE);

		}
		else 
		{
			frame.getTglRedo().setEnabled(false);
			ToBackCmd toBackCmd = new ToBackCmd(model,selectedShapes.get(0));
			toBackCmd.execute();
			frame.getTextArea().append("To back - " + selectedShapes.get(0)+"\n");
			undoRedo(toBackCmd, selectedShapes.get(0), "To back", "To back");
			frame.getView().repaint();
		}
	}

	public void BringToFront() {
		if (selectedShapes.size() < 1 || selectedShapes.size() > 1) 
		{
			JOptionPane.showMessageDialog(frame, "You have to select one shape!", "Error", JOptionPane.ERROR_MESSAGE);
		}	
		else if (model.getShapes().size() <= 1)
		{
			JOptionPane.showMessageDialog(frame, "You have to draw at least 2 shapes!", "Error", JOptionPane.ERROR_MESSAGE);

		}
		else 
		{
			frame.getTglRedo().setEnabled(false);
			BringToFrontCmd bringToFrontCmd = new BringToFrontCmd(model,selectedShapes.get(0));
			bringToFrontCmd.execute();
			frame.getTextArea().append("Bring to front - "+ selectedShapes.get(0)+"\n");
			undoRedo(bringToFrontCmd, selectedShapes.get(0), "Bring to front", "Bring to front");
			frame.getView().repaint();
		}
	}
	
	public void BringToBack() {
		if (selectedShapes.size() < 1 || selectedShapes.size() > 1) 
		{
			JOptionPane.showMessageDialog(frame, "You have to select just one shape!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else if (model.getShapes().size() <= 1)
		{
			JOptionPane.showMessageDialog(frame, "You have to draw at least 2 shapes!", "Error", JOptionPane.ERROR_MESSAGE);

		}
		else 
		{
			frame.getTglRedo().setEnabled(false);
			BringToBackCmd bringToBackCmd = new BringToBackCmd(model,selectedShapes.get(0));
			bringToBackCmd.execute();
			frame.getTextArea().append("Bring to back - "+ selectedShapes.get(0)+"\n");
			undoRedo(bringToBackCmd, selectedShapes.get(0), "Bring to back", "Bring to back");
			frame.getView().repaint();
		}
	}
	
//___________________________ Crtez funkcionalnost _________________________________
	
//_________________ CUVANJE CRTEZA _________________
		
	public void saveDrawing() throws IOException 
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save drawing!");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".bin","bin");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(filter);
		

		int userSelection = fileChooser.showSaveDialog(null);
		if(userSelection == JFileChooser.APPROVE_OPTION) {
			File drawingToSave = fileChooser.getSelectedFile();
			File logToSave;
			String filePath = drawingToSave.getAbsolutePath();
			if(!filePath.endsWith(".bin") && !filePath.contains(".")) {
				drawingToSave = new File(filePath + ".bin");
				logToSave = new File(filePath + ".txt");
			}
			
			String filename = drawingToSave.getPath();
			System.out.println("Successfully saved drawing -> " + drawingToSave.getName() + ".bin");
			
			if(filename.substring(filename.lastIndexOf("."), filename.length()).contains(".bin")) 
			{
				filename = drawingToSave.getAbsolutePath().substring(0,filename.lastIndexOf("."))+".txt";
				logToSave = new File(filename);
				
				ManagerStrategy saveDrawing = new ManagerStrategy(new SaveShapesStrategy());
				ManagerStrategy saveLog = new ManagerStrategy(new SaveLogStrategy());
				
				saveDrawing.saveFile(model, drawingToSave);
				saveLog.saveFile(frame, logToSave);
			} else {
				JOptionPane.showMessageDialog(null, "Wrong file extension!");
			}
		}
	}
	
//_________________ OTVARANJE CRTEZA _________________
	
	public void openDrawing() throws IOException,ClassNotFoundException 
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		fileChooser.setDialogTitle("Open drawing");
		int userSelection = fileChooser.showOpenDialog(null);
		
		if(userSelection == JFileChooser.APPROVE_OPTION) 
		{
			File drawingToLoad = fileChooser.getSelectedFile();
			String drawingName = drawingToLoad.getPath();
			try {
				if (drawingName.contains(".bin"))
				{
					loadPainting(drawingToLoad);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Wrong file extension!");
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Invalid file!");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RadiusException e) {
				e.printStackTrace();
			}
		}
	}
		
	
//_________________ UCITAVANJE CRTEZA _________________
	
	@SuppressWarnings("unchecked")
	public void loadPainting(File drawingToLoad) throws FileNotFoundException,IOException,ClassNotFoundException, RadiusException {
		File file = new File(drawingToLoad.getAbsolutePath().replace("bin", "txt"));
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String logLine;
		
		logList.clear();
		
		while((logLine = bufferedReader.readLine()) != null) 
		{
			logList.add(logLine);
		}
		bufferedReader.close();
		
		next();
	
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(drawingToLoad));
		try 
		{
			cmdList.setCurrent(-1);
			model.getShapes().clear();
			cmdList.getList().clear();
			cmdList.getShapes().clear();
			cmdList.getLogRedo().clear();
			cmdList.getLogUndo().clear();
			selectedShapes.clear();
			
			frame.getTglRedo().setEnabled(false);
			frame.getTglUndo().setEnabled(false);
			frame.getTglSelect().setEnabled(false);
			
			tglObserver.setDeleteEnabled(false);
			tglObserver.setModifyEnabled(false);
			
			model.getShapes().addAll((ArrayList<Shape>)objectInputStream.readObject());
			objectInputStream.close();
		
		} catch (InvalidClassException ice) {
			
		} catch (SocketTimeoutException ste) {
			
		} catch (EOFException eofe) {
			
		} catch (IOException exc) {
			exc.printStackTrace();
		}
		
		for (int i = 0; i < model.getShapes().size(); i++) 
		{
			if(model.getShapes().get(i).isSelected()) 
			{
				selectedShapes.add(model.getShapes().get(i));
			}
		}
		
		frame.getTextArea().setText("");
		
		@SuppressWarnings("resource")
		BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file));

		while((logLine = bufferedReader2.readLine()) != null) 
		{
			frame.getTextArea().append(logLine + "\n");
		}
		bufferedReader.close();
	}
	
//___________________________ Log - funkcionalnost _________________________________

//_________________ CUVANJE LOG-A _________________	
	
	public void saveLog() throws IOException 
	{
		JFileChooser fileChooser  = new JFileChooser(); 
		fileChooser.setDialogTitle("Save log");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt","txt");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(filter);
		
		if(fileChooser.showSaveDialog(frame.getParent()) == JFileChooser.APPROVE_OPTION) 
		{
			System.out.println("Successfully saved file -> " + fileChooser.getSelectedFile().getName() + ".txt");
			File file = fileChooser.getSelectedFile();
			String filePath = file.getAbsolutePath();
			File logToSave = new File(filePath + ".txt");
			
			ManagerStrategy manager = new ManagerStrategy(new SaveLogStrategy());
			manager.saveFile(frame, logToSave);
			
			frame.getView().repaint();
		}
	}
	
//_________________ OTVARANJE LOG-A _________________	

	public void openLog() throws IOException 
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open log");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(filter);

		int userSelection = fileChooser.showOpenDialog(null);
		if(userSelection == JFileChooser.APPROVE_OPTION) 
		{
			File logTxt = fileChooser.getSelectedFile();
			
			logEnd = false;
			cmdList.setCurrent(-1);
			model.getShapes().clear();
			cmdList.getShapes().clear();
			cmdList.getList().clear();
			cmdList.getLogUndo().clear();
			cmdList.getLogRedo().clear();
			
			selectedShapes.clear();
			logList.clear();
			
			frame.getBtnNext().setEnabled(true);
			frame.getTglSelect().setEnabled(false);
			frame.getTglRedo().setEnabled(false);
			frame.getTglUndo().setEnabled(false);
			frame.getTglSelect().setEnabled(false);
			
			tglObserver.setDeleteEnabled(false);
			tglObserver.setModifyEnabled(false);
			
			logCounter = 0;

			frame.getTextArea().setText("LOG OPENED\n");
			frame.getBtnInnerColor().setBackground(Color.WHITE);
			frame.getBtnBorderColor().setBackground(Color.BLACK);
			frame.getBtnBorderColor().setForeground(Color.WHITE);
			frame.getView().repaint();
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader(logTxt));
			
			while((logReaderLine = bufferedReader.readLine()) != null) 
			{
				logList.add(logReaderLine);
			}
			bufferedReader.close();
		}
	}
	
//___________________________ Next - funkcionalnost _________________________________
	
	public void next() throws RadiusException 
	{
		Shape shape = null;
		
		if(logEnd == false) 
		{
			String line = logList.get(logCounter);
		
			//______________________________________________________________________	
			
			if(line.contains("Point")) 
			{
				int x = Integer.parseInt(line.substring(line.indexOf("(")+1, line.indexOf(",")));
				int y = Integer.parseInt(line.substring(line.indexOf(",")+1, line.indexOf(")")));
				String colorString = line.substring(line.lastIndexOf("(")+1, line.lastIndexOf(")"));
				Color color = new Color(Integer.parseInt(colorString));
				shape = new Point(x, y, color);
				
				if(line.contains("Modify"))
				{
					UpdatePointCmd pointCmd = null;
					Point p = (Point) shape;
					Point temp = (Point) selectedShapes.get(0);
					
					if(line.contains("Undo"))
					{
						for (int i = cmdList.getShapes().size()-1; i >= 0; i--) 
						{
							if(cmdList.getShapes().get(i).equals(temp) && cmdList.getLogUndo().get(i)=="Modify")
							{
								tempCmd = i;
							}
						}
						pointCmd = (UpdatePointCmd)cmdList.getList().get(tempCmd);
						pointCmd.unexecute();
						frame.getTextArea().append(line + "\n");
						undoRedo(pointCmd, pointCmd.getOriginal(), "Modify", "Modify");
					} 
					else 
					{
						pointCmd = new UpdatePointCmd(temp, p);
						pointCmd.execute();
						frame.getTextArea().append(line + "\n");
						undoRedo(pointCmd, p, "Modify", "Modify");
					}
				}
			}
			
			//______________________________________________________________________	
			
			else if (line.contains("Line"))
			{
				String[] lineSplit = line.split("-->");
				String line1 = lineSplit[0];
				String line2 = lineSplit[1];
				int startX = Integer.parseInt(line1.substring(line1.indexOf("(")+1, line1.indexOf(",")));
				int startY = Integer.parseInt(line1.substring(line1.indexOf(",")+1, line1.indexOf(")")));
				int endX = Integer.parseInt(line2.substring(line2.indexOf("(")+1, line2.indexOf(",")));
				int endY = Integer.parseInt(line2.substring(line2.indexOf(",")+1, line2.indexOf(")")));
				String colorString = line2.substring(line2.lastIndexOf("(")+1, line2.lastIndexOf(")"));
				Color color = new Color(Integer.parseInt(colorString));
				shape = new Line(new Point(startX, startY),
								 new Point(endX, endY), color);
				
				if(line.contains("Modify"))
				{
					UpdateLineCmd lineCmd = null;
					Line l = (Line) shape;
					Line temp = (Line) selectedShapes.get(0);
					
					if(line.contains("Undo"))
					{
						for (int i = cmdList.getShapes().size()-1; i >= 0; i--) 
						{
							if(cmdList.getShapes().get(i).equals(temp) && cmdList.getLogUndo().get(i)=="Modify")
							{
								tempCmd = i;
							}
						}
						lineCmd = (UpdateLineCmd)cmdList.getList().get(tempCmd);
						lineCmd.unexecute();
						frame.getTextArea().append(line + "\n");
						undoRedo(lineCmd, lineCmd.getOriginal(), "Modify", "Modify");
					} 
					else 
					{
						lineCmd = new UpdateLineCmd(temp, l);
						lineCmd.execute();
						frame.getTextArea().append(line + "\n");
						undoRedo(lineCmd, l, "Modify", "Modify");
					}
				}
			}	
			
			//______________________________________________________________________	
			
			else if (line.contains("Rectangle"))
			{
				String[] lineSplit = line.split("colors:");
				String line1 = lineSplit[0];
				String line2 = lineSplit[1];
				int x = Integer.parseInt(line1.substring(line1.indexOf("(")+1, line1.indexOf(",")));
				int y = Integer.parseInt(line1.substring(line1.indexOf(",")+1, line1.indexOf(")")));
				int width = Integer.parseInt(line1.substring(line1.indexOf("=")+1, line1.indexOf("x")));
				int height = Integer.parseInt(line1.substring(line1.indexOf("x")+1, line1.lastIndexOf(",")));
				String colorString1 = line2.substring(line2.indexOf("(")+1, line2.indexOf(","));
				String colorString2 = line2.substring(line2.indexOf(",")+1, line2.indexOf(")"));
				Color color1 = new Color(Integer.parseInt(colorString1));
				Color color2 = new Color(Integer.parseInt(colorString2));
				shape = new Rectangle(new Point(x, y), width, height, color1, color2);
				
				if(line.contains("Modify"))
				{
					UpdateRectangleCmd rectangleCmd = null;
					Rectangle r = (Rectangle) shape;
					Rectangle temp = (Rectangle) selectedShapes.get(0);
					
					if(line.contains("Undo"))
					{
						for (int i = cmdList.getShapes().size()-1; i >= 0; i--) 
						{
							if(cmdList.getShapes().get(i).equals(temp) && cmdList.getLogUndo().get(i)=="Modify")
							{
								tempCmd = i;
							}
						}
						rectangleCmd = (UpdateRectangleCmd)cmdList.getList().get(tempCmd);
						rectangleCmd.unexecute();
						frame.getTextArea().append(line + "\n");
						undoRedo(rectangleCmd, rectangleCmd.getOriginal(), "Modify", "Modify");
					} 
					else 
					{
						rectangleCmd = new UpdateRectangleCmd(temp, r);
						rectangleCmd.execute();
						frame.getTextArea().append(line + "\n");
						undoRedo(rectangleCmd, r, "Modify", "Modify");
					}
				}
			}	
			
			//______________________________________________________________________	
			
			else if (line.contains("Circle"))
			{
				String[] lineSplit = line.split("colors:");
				String line1 = lineSplit[0];
				String line2 = lineSplit[1];
				int x = Integer.parseInt(line1.substring(line1.indexOf("(")+1, line1.indexOf(",")));
				int y = Integer.parseInt(line1.substring(line1.indexOf(",")+1, line1.indexOf(")")));
				int radius = Integer.parseInt(line1.substring(line1.indexOf("=")+1, line1.lastIndexOf(",")));
				String colorString1 = line2.substring(line2.indexOf("(")+1, line2.indexOf(","));
				String colorString2 = line2.substring(line2.indexOf(",")+1, line2.indexOf(")"));
				Color color1 = new Color(Integer.parseInt(colorString1));
				Color color2 = new Color(Integer.parseInt(colorString2));
				shape = new Circle(new Point(x, y), radius, color1, color2);
				
				if(line.contains("Modify"))
				{
					UpdateCircleCmd circleCmd = null;
					Circle c = (Circle) shape;
					Circle temp = (Circle) selectedShapes.get(0);
					
					if(line.contains("Undo"))
					{
						for (int i = cmdList.getShapes().size()-1; i >= 0; i--) 
						{
							if(cmdList.getShapes().get(i).equals(temp) && cmdList.getLogUndo().get(i)=="Modify")
							{
								tempCmd = i;
							}
						}
						circleCmd = (UpdateCircleCmd)cmdList.getList().get(tempCmd);
						circleCmd.unexecute();
						frame.getTextArea().append(line + "\n");
						undoRedo(circleCmd, circleCmd.getOriginal(), "Modify", "Modify");
					} 
					else 
					{
						circleCmd = new UpdateCircleCmd(temp, c);
						circleCmd.execute();
						frame.getTextArea().append(line + "\n");
						undoRedo(circleCmd, c, "Modify", "Modify");
					}
				}
			}
			
			//______________________________________________________________________	
			
			else if (line.contains("Donut"))
			{
				String[] lineSplit = line.split("r=");
				String line1 = lineSplit[0];
				String line2 = lineSplit[1];
				int x = Integer.parseInt(line1.substring(line1.indexOf("(")+1, line1.indexOf(",")));
				int y = Integer.parseInt(line1.substring(line1.indexOf(",")+1, line1.indexOf(")")));
				int radius = Integer.parseInt(line1.substring(line1.indexOf("=")+1, line1.lastIndexOf(",")));
				int innerRadius = Integer.parseInt(line2.substring(0, line2.indexOf(",")));
				String colorString1 = line2.substring(line2.indexOf("(")+1, line2.lastIndexOf(","));
				String colorString2 = line2.substring(line2.lastIndexOf(",")+1, line2.indexOf(")"));				
				Color color1 = new Color(Integer.parseInt(colorString1));
				Color color2 = new Color(Integer.parseInt(colorString2));
				shape = new Donut(new Point(x, y), radius, innerRadius, color1, color2);
				
				if(line.contains("Modify"))
				{
					UpdateDonutCmd donutCmd = null;
					Donut d = (Donut) shape;
					Donut temp = (Donut) selectedShapes.get(0);
					
					if(line.contains("Undo"))
					{
						for (int i = cmdList.getShapes().size()-1; i >= 0; i--) 
						{
							if(cmdList.getShapes().get(i).equals(temp) && cmdList.getLogUndo().get(i)=="Modify")
							{
								tempCmd = i;
							}
						}
						donutCmd = (UpdateDonutCmd)cmdList.getList().get(tempCmd);
						donutCmd.unexecute();
						frame.getTextArea().append(line + "\n");
						undoRedo(donutCmd, donutCmd.getOriginal(), "Modify", "Modify");
					} 
					else 
					{
						donutCmd = new UpdateDonutCmd(temp, d);
						donutCmd.execute();
						frame.getTextArea().append(line + "\n");
						undoRedo(donutCmd, d, "Modify", "Modify");
					}
				}
			}	
			
			//______________________________________________________________________	
			
			else if (line.contains("Hexagon"))
			{
				String[] lineSplit = line.split("colors:");
				String line1 = lineSplit[0];
				String line2 = lineSplit[1];
				int x = Integer.parseInt(line1.substring(line1.indexOf("(")+1, line1.indexOf(",")));
				int y = Integer.parseInt(line1.substring(line1.indexOf(",")+1, line1.indexOf(")")));
				int radius = Integer.parseInt(line1.substring(line1.indexOf("=")+1, line1.lastIndexOf(",")));
				String colorString1 = line2.substring(line2.indexOf("(")+1, line2.indexOf(","));
				String colorString2 = line2.substring(line2.indexOf(",")+1, line2.indexOf(")"));
				Color color1 = new Color(Integer.parseInt(colorString1));
				Color color2 = new Color(Integer.parseInt(colorString2));
				shape = new HexagonAdapter(new Point(x, y), radius, color1, color2);
				
				if(line.contains("Modify"))
				{
					UpdateHexagonCmd hexagonCmd = null;
					HexagonAdapter h = (HexagonAdapter) shape;
					HexagonAdapter temp = (HexagonAdapter) selectedShapes.get(0);
					
					if(line.contains("Undo"))
					{
						for (int i = cmdList.getShapes().size()-1; i >= 0; i--) 
						{
							if(cmdList.getShapes().get(i).equals(temp) && cmdList.getLogUndo().get(i)=="Modify")
							{
								tempCmd = i;
							}
						}
						hexagonCmd = (UpdateHexagonCmd)cmdList.getList().get(tempCmd);
						hexagonCmd.unexecute();
						frame.getTextArea().append(line + "\n");
						undoRedo(hexagonCmd, hexagonCmd.getOriginal(), "Modify", "Modify");
					} 
					else 
					{
						hexagonCmd = new UpdateHexagonCmd(temp, h);
						hexagonCmd.execute();
						frame.getTextArea().append(line + "\n");
						undoRedo(hexagonCmd, h, "Modify", "Modify");
					}
				}
			}
			
			//______________________________________________________________________
			
			if(line.contains("Draw"))
			{
				AddShapeCmd addCmd = new AddShapeCmd(model,shape);
				SelectShapeCmd selectCmd = new SelectShapeCmd(this, shape);

				if(line.contains("Undo"))
				{
					addCmd.unexecute();
					selectCmd.unexecute();
					frame.getTextArea().append(line + "\n");
					undoRedo(addCmd, shape, "Draw", "Draw");
				}
				else if (line.contains("Redo"))
				{
					addCmd.execute();
					frame.getTextArea().append(line + "\n");
					undoRedo(addCmd, shape, "Draw", "Draw");
				}
				else
				{
					addCmd.execute();
					frame.getTextArea().append(line + "\n");
					undoRedo(addCmd, shape, "Draw", "Draw");
				}
			}
			
			//______________________________________________________________________
			
			else if (line.contains("Select")) {
				shape = model.getShapes().get(model.getShapes().indexOf(shape));
				
				SelectShapeCmd selectCmd = new SelectShapeCmd(this,shape);
				
				if(line.contains("Undo"))
				{
					selectCmd.unexecute();
					frame.getTextArea().append(line + "\n");
					undoRedo(selectCmd, shape, "Select", "Select");
				}
				else
				{
					selectCmd.execute();
					frame.getTextArea().append(line + "\n");
					undoRedo(selectCmd, shape, "Select", "Select");
				}
			}
			else if (line.contains("Deselect")) {
				shape = model.getShapes().get(model.getShapes().indexOf(shape));
				
				DeselectShapeCmd deselectCmd = new DeselectShapeCmd(this,shape);
				
				if(line.contains("Undo"))
				{
					deselectCmd.unexecute();
					frame.getTextArea().append(line + "\n");
					undoRedo(deselectCmd, shape, "Deselect", "Deselect");
				}
				else
				{
					deselectCmd.execute();
					frame.getTextArea().append(line + "\n");
					undoRedo(deselectCmd, shape, "Deselect", "Deselect");
				}
			}
			
			//______________________________________________________________________
			
			else if(line.contains("Delete"))
			{
				DeleteShapeCmd deleteCmd = new DeleteShapeCmd(model,shape);
				SelectShapeCmd selectCmd = new SelectShapeCmd(this, shape);

				if(line.contains("Undo"))
				{
					deleteCmd.unexecute();
					selectCmd.execute();
					frame.getTextArea().append(line + "\n");
					undoRedo(deleteCmd, shape, "Delete", "Delete");
				}
				else
				{
					deleteCmd.execute();
					frame.getTextArea().append(line + "\n");
					undoRedo(deleteCmd, shape, "Delete", "Delete");
				}
			}
			
			//______________________________________________________________________
			
			else if(line.contains("To front")) 
			{
				ToFrontCmd toFrontCmd = new ToFrontCmd(model,selectedShapes.get(0));

				if(line.contains("Undo"))
				{
					toFrontCmd.unexecute();
					frame.getTextArea().append(line + "\n");
					undoRedo(toFrontCmd, selectedShapes.get(0), "To front", "To front");
				}
				else
				{
					toFrontCmd.execute();
					frame.getTextArea().append(line + "\n");
					undoRedo(toFrontCmd, selectedShapes.get(0), "To front", "To front");
				}
			} 
			else if(line.contains("To back")) 
			{
				ToBackCmd toBackCmd = new ToBackCmd(model,selectedShapes.get(0));
				
				if(line.contains("Undo"))
				{
					toBackCmd.unexecute();
					frame.getTextArea().append(line + "\n");
					undoRedo(toBackCmd, selectedShapes.get(0), "To back", "To back");
				}
				else
				{
					toBackCmd.execute();
					frame.getTextArea().append(line + "\n");
					undoRedo(toBackCmd, selectedShapes.get(0), "To back", "To back");
				}
			} 
			else if(line.contains("Bring to front")) 
			{
				BringToFrontCmd bringFrontCmd = new BringToFrontCmd(model,selectedShapes.get(0));
				BringToBackCmd bringBackCmd = new BringToBackCmd(model,selectedShapes.get(0));

				if(line.contains("Undo"))
				{
					bringBackCmd.execute();
					frame.getTextArea().append(line + "\n");
					undoRedo(bringFrontCmd, selectedShapes.get(0), "Bring to front", "Bring to front");					}
				else
				{
					bringFrontCmd.execute();
					frame.getTextArea().append(line + "\n");
					undoRedo(bringFrontCmd, selectedShapes.get(0), "Bring to front", "Bring to front");					}
			}
			else if(line.contains("Bring to back")) 
			{
				BringToBackCmd bringBackCmd = new BringToBackCmd(model,selectedShapes.get(0));
				BringToFrontCmd bringFrontCmd = new BringToFrontCmd(model,selectedShapes.get(0));
				
				if(line.contains("Undo"))
				{
					bringFrontCmd.execute();
					frame.getTextArea().append(line + "\n");
					undoRedo(bringBackCmd, selectedShapes.get(0), "Bring to back", "Bring to back");					}
				else
				{
					bringBackCmd.execute();
					frame.getTextArea().append(line + "\n");
					undoRedo(bringBackCmd, selectedShapes.get(0), "Bring to back", "Bring to back");
				}
			} 

			//______________________________________________________________________
			
			if(logCounter == logList.size()-1) 
			{
				frame.getTextArea().append("LOG CLOSED\n");
				frame.getBtnNext().setEnabled(false);
				logEnd = true;
			}
			logCounter++;
			frame.getView().repaint();
		}
	}
	
//________________________ Geteri i seteri _________________________________
	
	public List<Shape> getSelectedShapes() {
		return selectedShapes;
	}

	public CmdList getCmdList() {
		return cmdList;
	}

	public void setCmdList(CmdList cmdList) {
		this.cmdList = cmdList;
	}

	public boolean isLogEnd() {
		return logEnd;
	}

	public void setLogEnd(boolean logEnd) {
		this.logEnd = logEnd;
	}
}




