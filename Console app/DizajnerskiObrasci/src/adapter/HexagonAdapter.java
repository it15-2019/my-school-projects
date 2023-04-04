package adapter;

import java.awt.Color;
import java.awt.Graphics;
import geometry.Point;
import geometry.SurfaceShape;
import hexagon.Hexagon;

@SuppressWarnings("serial")
public class HexagonAdapter extends SurfaceShape {
	
//________________________ Deklarisanje promenljivih ________________________

	private Hexagon hexagon;
	
//________________________ Konstruktori ________________________
	
	public HexagonAdapter() {
		
	}
	
	public HexagonAdapter(Point center, int radius, Color color, Color innerColor) {
		this.hexagon = new Hexagon(center.getX(), center.getY(), radius);
		this.hexagon.setBorderColor(color);
		this.hexagon.setAreaColor(innerColor);
	}

//________________________ Override ________________________

	@Override
	public int compareTo(Object o) {
		if(o instanceof Hexagon) {
			Hexagon h = (Hexagon) o;
			return (int) (hexagon.getR() - h.getR());
		}
		else
			return 0;
	}
	
	@Override
	public void moveBy(int byX, int byY) {
		hexagon.setX(hexagon.getX() + byX); 	
	  	hexagon.setY(hexagon.getY() + byY);
	}
	
	@Override
	public void draw(Graphics g) {
		hexagon.paint(g);
	}
	
	@Override
	public void fill(Graphics g) {
	}
	
	@Override
	public boolean contains(int x, int y) {
		return hexagon.doesContain(x, y);
	}
	
	@Override
	public boolean contains(Point p) {
		return hexagon.doesContain(p.getX(), p.getY());
	}
	
	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		hexagon.setSelected(selected);
	}
	
	@Override
	public boolean isSelected() {
		return hexagon.isSelected();
	}
	
//________________________ Metode ________________________
	
	
	public double area() {
		return hexagon.getR() * hexagon.getR() * Math.PI;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof HexagonAdapter){
			HexagonAdapter hexAdapter = (HexagonAdapter) obj;
			Point p1 = new Point(hexagon.getX(),hexagon.getY());
			Point p2 = new Point(hexAdapter.hexagon.getX(),hexAdapter.hexagon.getY());
			if(p1.equals(p2) && hexagon.getR() == hexAdapter.getHexagon().getR())
				return true;
			else
				return false;

		}
		else
			return false;
	}
	
	public String toString() {
		//Hexagon: (x,y), r=radius, colors: (boja,boja)
		return "Hexagon: (" + hexagon.getX() + "," + hexagon.getY() + "), r=" + hexagon.getR() + ", colors: (" + Integer.toString(hexagon.getBorderColor().getRGB()) + "," + Integer.toString(hexagon.getAreaColor().getRGB()) + ")";
	}
	
	public HexagonAdapter clone() {
		HexagonAdapter ha = new HexagonAdapter(new Point(-1,-1),-1,Color.black, Color.black);
		
		ha.getHexagon().setX(this.getHexagon().getX());
		ha.getHexagon().setY(this.getHexagon().getY());
		ha.getHexagon().setR(this.getHexagon().getR());
		ha.getHexagon().setBorderColor(this.getHexagon().getBorderColor());
		ha.getHexagon().setAreaColor(this.getHexagon().getAreaColor());
		
		return ha;
	}
	
//________________________ Geteri i seteri ________________________
	
	public Hexagon getHexagon() {
		return hexagon;
	}
	
	public void setHexagon(Point center, int r, Color color, Color innerColor) {
		this.hexagon = new Hexagon(center.getX(), center.getY(), r);
		this.hexagon.setBorderColor(color);
		this.hexagon.setAreaColor(innerColor);
		setSelected(true);
	}
	
	public int getRadius() {
		return hexagon.getR();
	}
	
	public void setRadius(int radius) {
		hexagon.setR(radius);
	}
	
	public Point getCenter() {
		return new Point(hexagon.getX(),hexagon.getY());
	}
	
	public void setCenter(Point center) {
		hexagon.setX(center.getX());
		hexagon.setY(center.getY());
	}
}