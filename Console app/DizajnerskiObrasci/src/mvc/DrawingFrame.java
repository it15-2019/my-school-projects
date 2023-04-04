package mvc;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;
import strategy.RadiusException;
import javax.swing.border.LineBorder;
import javax.swing.UIManager;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import java.awt.Toolkit;

@SuppressWarnings("serial")
public class DrawingFrame extends JFrame {
	
	private DrawingView view = new DrawingView();
	private DrawingController controller;
	
	private JToggleButton tglModify;
	private JToggleButton tglSelect;
	private JToggleButton tglDelete;
	private JToggleButton tglUndo;
	private JToggleButton tglRedo;
	
	private JButton btnInnerColor;
	private JButton btnBorderColor;
	private JButton btnNext;
	
	private JMenu mnFile;
	private JMenuItem mntOpenLog;
	private JMenuItem mntSaveLog;
	private JMenuItem mntOpenDrawing;
	private JMenuItem mntSaveDrawing;
	
	public Color innerFill;
	public Color borderFill;
	
	private JTextArea textArea;
	private JPanel contentPane;
	private int state = 0;
	

//_____________________________ Frame ___________________________________
	
	public DrawingFrame() 
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Sofija\\Desktop\\FAKS\\PETI SEMESTAR\\DO\\drawing1.png"));
		view.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.mouseClicked(e);
			}
		});
		
		setTitle("Drawing app");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);

		
//________________________________________________________________
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.BLACK);

		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		mnFile.setBackground(new Color(0, 255, 102));
		mnFile.setForeground(new Color(0, 255, 102));
		menuBar.add(mnFile);
		
//________________________________________________________________
		
		mntSaveLog = new JMenuItem("Save log");
		mntSaveLog.setForeground(Color.WHITE);
		mntSaveLog.setBackground(Color.BLACK);
		mntSaveLog.setIcon(new ImageIcon("C:\\Users\\Sofija\\Desktop\\FAKS\\PETI SEMESTAR\\DO\\saveDrawing2.png"));
		mntSaveLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					controller.saveLog();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		
//________________________________________________________________
		
		mntSaveDrawing = new JMenuItem("Save drawing");
		mntSaveDrawing.setBackground(Color.WHITE);
		mntSaveDrawing.setIcon(new ImageIcon("C:\\Users\\Sofija\\Desktop\\FAKS\\PETI SEMESTAR\\DO\\saveDrawing1.png"));
		mntSaveDrawing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					controller.saveDrawing();
				} 
				catch (IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
		});
		mnFile.add(mntSaveDrawing);
		
//________________________________________________________________
		
		mntOpenDrawing = new JMenuItem("Open drawing");
		mntOpenDrawing.setBackground(Color.WHITE);
		mntOpenDrawing.setIcon(new ImageIcon("C:\\Users\\Sofija\\Desktop\\FAKS\\PETI SEMESTAR\\DO\\openDrawing1.png"));
		mntOpenDrawing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					controller.openDrawing(); 
				}  
				catch (ClassNotFoundException | IOException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		mnFile.add(mntOpenDrawing);
		mnFile.add(mntSaveLog);
		
		
//________________________________________________________________
		
		mntOpenLog = new JMenuItem("Open log");
		mntOpenLog.setForeground(Color.WHITE);
		mntOpenLog.setBackground(Color.BLACK);
		mntOpenLog.setIcon(new ImageIcon("C:\\Users\\Sofija\\Desktop\\FAKS\\PETI SEMESTAR\\DO\\openDrawing2.png"));
		mntOpenLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					controller.openLog();
				} 
				catch (IOException ioe)
				{
					ioe.printStackTrace();
				} 
			}
		});
		mnFile.add(mntOpenLog);
		
//________________________________________________________________
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBorder(null);
		setContentPane(contentPane);
		ButtonGroup group = new ButtonGroup();
		
//____________________________ North ___________________________________
		
		JPanel pnlNorth = new JPanel();
		pnlNorth.setForeground(new Color(0, 0, 0));
		pnlNorth.setBackground(Color.BLACK);
		contentPane.add(pnlNorth, BorderLayout.NORTH);
		
		JLabel lblDrawing = new JLabel("Sofija Dangubic IT15/2019");
		lblDrawing.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblDrawing.setBackground(new Color(0, 255, 102));
		lblDrawing.setHorizontalAlignment(SwingConstants.LEFT);
		lblDrawing.setForeground(new Color(0, 255, 102));
		pnlNorth.add(lblDrawing);
		
//_____________________________ West ___________________________________
		
		JPanel pnlWest = new JPanel();
		pnlWest.setBackground(new Color(0, 0, 0));
		contentPane.add(pnlWest, BorderLayout.WEST);

//________________________________________________________________
		
		JRadioButton rdbPoint = new JRadioButton("Point");
		rdbPoint.setForeground(new Color(0, 255, 102));
		rdbPoint.setFont(new Font("Times New Roman", Font.BOLD, 12));
		rdbPoint.setBackground(new Color(0, 0, 0));
		group.add(rdbPoint);
		rdbPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				state = 1;
				
			}
		});
		pnlWest.setLayout(new MigLayout("", "[126px,grow]", "[22px][23px][23px][23px][23px][23px,grow][23px][23px][][][][][][][]"));
		pnlWest.add(rdbPoint, "cell 0 0,alignx left,aligny center");
		
//________________________________________________________________
		
		JRadioButton rdbLine = new JRadioButton("Line");
		rdbLine.setForeground(new Color(0, 255, 102));
		rdbLine.setFont(new Font("Times New Roman", Font.BOLD, 12));
		rdbLine.setBackground(new Color(0, 0, 0));
		group.add(rdbLine);
		rdbLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				state = 2;
				
			}
		});
		pnlWest.add(rdbLine, "cell 0 1,alignx left,aligny center");
		
//________________________________________________________________
		
		JRadioButton rdbRectangle = new JRadioButton("Rectangle");
		rdbRectangle.setForeground(new Color(0, 255, 102));
		rdbRectangle.setFont(new Font("Times New Roman", Font.BOLD, 12));
		rdbRectangle.setBackground(new Color(0, 0, 0));
		group.add(rdbRectangle);
		rdbRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				state = 3;
			}
		});
		pnlWest.add(rdbRectangle, "cell 0 2,alignx left,aligny center");
		
//________________________________________________________________
		
		JRadioButton rdbCircle = new JRadioButton("Circle");
		rdbCircle.setForeground(new Color(0, 255, 102));
		rdbCircle.setFont(new Font("Times New Roman", Font.BOLD, 12));
		rdbCircle.setBackground(new Color(0, 0, 0));
		group.add(rdbCircle);
		rdbCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				state = 4;
			}
		});
		pnlWest.add(rdbCircle, "cell 0 3,alignx left,aligny center");
		
//________________________________________________________________
		
		JRadioButton rdbDonut = new JRadioButton("Donut");
		rdbDonut.setForeground(new Color(0, 255, 102));
		rdbDonut.setFont(new Font("Times New Roman", Font.BOLD, 12));
		rdbDonut.setBackground(new Color(0, 0, 0));
		group.add(rdbDonut);
		rdbDonut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				state = 5;
			}
		});
		pnlWest.add(rdbDonut, "cell 0 4,alignx left,aligny center");
		
//________________________________________________________________
		
		JRadioButton rdbHexagon = new JRadioButton("Hexagon");
		rdbHexagon.setForeground(new Color(0, 255, 102));
		rdbHexagon.setFont(new Font("Times New Roman", Font.BOLD, 12));
		rdbHexagon.setBackground(new Color(0, 0, 0));
		group.add(rdbHexagon);
		rdbHexagon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				state = 6;
			}
		});
		pnlWest.add(rdbHexagon, "cell 0 5,alignx left,aligny center");
		
//________________________________________________________________
		
		tglSelect = new JToggleButton("Select");
		tglSelect.setFont(new Font("Times New Roman", Font.BOLD, 12));
		tglSelect.setBackground(new Color(0, 255, 102));
		tglSelect.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 0, 0)));
		group.add(tglSelect);
		tglSelect.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
			}
		});
		pnlWest.add(tglSelect, "cell 0 8,growx,aligny center");
		
//________________________________________________________________
		
		tglModify = new JToggleButton("Modify");
		tglModify.setFont(new Font("Times New Roman", Font.BOLD, 12));
		tglModify.setBackground(new Color(0, 255, 102));
		tglModify.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 0, 0)));
		tglModify.setEnabled(false);
		group.add(tglModify);
		tglModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.modify();
			}
		});
		pnlWest.add(tglModify, "flowy,cell 0 9,growx,aligny center");
		
//________________________________________________________________
		
		tglDelete = new JToggleButton("Delete");
		tglDelete.setFont(new Font("Times New Roman", Font.BOLD, 12));
		tglDelete.setBackground(new Color(0, 255, 102));
		tglDelete.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 0, 0)));
		tglDelete.setEnabled(false);
		group.add(tglDelete);
		tglDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.delete();
			}
		});
		pnlWest.add(tglDelete, "cell 0 10,growx");
		
//________________________________________________________________
		
		tglUndo = new JToggleButton("Undo");
		tglUndo.setFont(new Font("Times New Roman", Font.BOLD, 12));
		tglUndo.setBackground(new Color(0, 255, 102));
		tglUndo.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 0, 0)));
		tglUndo.setEnabled(false);
		group.add(tglUndo);
		tglUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.undoShape();
				tglRedo.setEnabled(true);
				if((controller.getCmdList().getCurrent()) == 0 )
				{
					tglUndo.setEnabled(false);
				}
			}
		});
		pnlWest.add(tglUndo, "cell 0 15,growx");
		
//________________________________________________________________
		
		tglRedo = new JToggleButton("Redo");
		tglRedo.setFont(new Font("Times New Roman", Font.BOLD, 12));
		tglRedo.setBackground(new Color(0, 255, 102));
		tglRedo.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 0, 0)));
		tglRedo.setEnabled(false);
		group.add(tglRedo);
		tglRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.redoShape();
				tglUndo.setEnabled(true);
				if(controller.getCmdList().getCurrent()-1 == controller.getCmdList().getList().size()-1)
				{
					tglRedo.setEnabled(false);
				}
			}
		});
		pnlWest.add(tglRedo, "cell 0 15,growx");
		
		
//____________________________ Center ___________________________________
		
		JPanel pnlCenter = new JPanel();
		pnlCenter.setLayout(new BorderLayout(0,0));
		pnlCenter.setBorder(new LineBorder(Color.BLACK));
		contentPane.add(pnlCenter, BorderLayout.CENTER);
		
		view.setPreferredSize(new Dimension(200,400));
		view.setBackground(Color.WHITE);
		contentPane.add(view);
		
//____________________________ South ___________________________________
		
		JPanel pnlSouth = new JPanel();
		pnlSouth.setPreferredSize(new Dimension(300,100));
		pnlSouth.setLayout(new BorderLayout(0,0));
		pnlSouth.setBorder(new LineBorder(Color.BLACK));
		contentPane.add(pnlSouth, BorderLayout.SOUTH);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.BLACK));
		pnlSouth.add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setBackground(new Color(0, 255, 102));
		textArea.setForeground(Color.BLACK);
		textArea.setBorder(new LineBorder(Color.BLACK));
		textArea.setEditable(false);
		textArea.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		scrollPane.setViewportView(textArea);
		
//____________________________ East ___________________________________
		
		JPanel pnlEast = new JPanel();
		pnlEast.setBackground(Color.BLACK);
		pnlEast.setForeground(Color.BLACK);
		contentPane.add(pnlEast, BorderLayout.EAST);
		pnlEast.setLayout(new MigLayout("", "[126px,grow]", "[22px][23px][][23px][][23px][][23px][][23px,grow][][][][][][][23px][23px][][][][][][][][][][][][][][][]"));
		
//________________________________________________________________
		
		btnInnerColor = new JButton("Inner Color");
		btnInnerColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				innerFill = JColorChooser.showDialog(null, "Choose a inner color", innerFill);
				btnInnerColor.setBackground(innerFill);
				if(btnInnerColor.getBackground().equals(Color.BLACK)) {
					btnInnerColor.setForeground(Color.WHITE);
				}
				else
					btnInnerColor.setForeground(Color.BLACK);
			}
		});
		btnInnerColor.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnInnerColor.setBackground(Color.WHITE);
		btnInnerColor.setBorder(UIManager.getBorder("Button.border"));
		pnlEast.add(btnInnerColor, "cell 0 0,grow");
		
//________________________________________________________________
		
		btnBorderColor = new JButton("Border Color");
		btnBorderColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				borderFill = JColorChooser.showDialog(null, "Choose a border color", borderFill);
				btnBorderColor.setBackground(borderFill);
				if(btnBorderColor.getBackground().equals(Color.BLACK)) {
					btnBorderColor.setForeground(Color.WHITE);
				}
				else
					btnBorderColor.setForeground(Color.BLACK);
			}
		});
		btnBorderColor.setForeground(Color.WHITE);
		btnBorderColor.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnBorderColor.setBackground(Color.BLACK);
		btnBorderColor.setBorder(UIManager.getBorder("Button.border"));
		pnlEast.add(btnBorderColor, "cell 0 2,grow");
		
//________________________________________________________________
		
		JButton btnToFront = new JButton("To Front");
		btnToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.ToFront();
			}
		});
		btnToFront.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnToFront.setForeground(new Color(0, 255, 102));
		btnToFront.setBackground(Color.BLACK);
		btnToFront.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 255, 102)));
		pnlEast.add(btnToFront, "cell 0 4,grow");
		
//________________________________________________________________
		
		JButton btnToBack = new JButton("To Back");
		btnToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.ToBack();
			}
		});
		btnToBack.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnToBack.setForeground(new Color(0, 255, 102));
		btnToBack.setBackground(Color.BLACK);
		btnToBack.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 255, 102)));
		pnlEast.add(btnToBack, "cell 0 6,grow");
		
//________________________________________________________________
		
		JButton btnBringToFront = new JButton("Bring To Front");
		btnBringToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.BringToFront();
			}
		});
		btnBringToFront.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnBringToFront.setForeground(new Color(0, 255, 102));
		btnBringToFront.setBackground(Color.BLACK);
		btnBringToFront.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 255, 102)));
		pnlEast.add(btnBringToFront, "cell 0 8,grow");
		
//________________________________________________________________
		
		JButton btnBringToBack = new JButton("Bring To Back");
		btnBringToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.BringToBack();
			}
		});
		btnBringToBack.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnBringToBack.setForeground(new Color(0, 255, 102));
		btnBringToBack.setBackground(Color.BLACK);
		btnBringToBack.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 255, 102)));
		pnlEast.add(btnBringToBack, "cell 0 10,grow");
		
//________________________________________________________________
		
		btnNext = new JButton("");
		btnNext.setIcon(new ImageIcon("C:\\Users\\Sofija\\Desktop\\FAKS\\PETI SEMESTAR\\DO\\next.png"));
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try 
				{
					controller.next();
				} 
				catch (RadiusException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNext.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnNext.setBackground(new Color(0, 255, 102));
		btnNext.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, new Color(0, 0, 0)));
		btnNext.setEnabled(false);
		pnlEast.add(btnNext, "cell 0 31,grow");
	}

//______________________ Getters and Setters ______________________________
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public DrawingView getView() {
		return view;
	}
	
	public void setController(DrawingController controller) {
		this.controller = controller;
	}

	public JToggleButton getTglModify() {
		return tglModify;
	}

	public JToggleButton getTglSelect() {
		return tglSelect;
	}

	public JToggleButton getTglDelete() {
		return tglDelete;
	}

	public JToggleButton getTglUndo() {
		return tglUndo;
	}

	public void setTglUndo(JToggleButton tglUndo) {
		this.tglUndo = tglUndo;
	}

	public JToggleButton getTglRedo() {
		return tglRedo;
	}

	public void setTglRedo(JToggleButton tglRedo) {
		this.tglRedo = tglRedo;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public JButton getBtnInnerColor() {
		return btnInnerColor;
	}

	public void setBtnInnerColor(JButton btnInnerColor) {
		this.btnInnerColor = btnInnerColor;
	}

	public JButton getBtnBorderColor() {
		return btnBorderColor;
	}

	public void setBtnBorderColor(JButton btnBorderColor) {
		this.btnBorderColor = btnBorderColor;
	}

	public JButton getBtnNext() {
		return btnNext;
	}

	public void setBtnNext(JButton btnNext) {
		this.btnNext = btnNext;
	}
}



