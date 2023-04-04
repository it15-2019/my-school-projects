package geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

@SuppressWarnings("serial")
public class Donut extends Circle { //Circle je parent klasa, a Donut child klasa
						
//________________________ Deklarisanje promenljivih ________________________

	private int innerRadius;
	
//________________________ Konstruktori ________________________
	
	public Donut() {
		
	}
	
	public Donut(Point center, int radius, int innerRadius) {
		super(center, radius);
		this.innerRadius = innerRadius;
	}
	
	public Donut(Point center, int radius, int innerRadius, boolean selected) {
		this(center, radius, innerRadius);
		setSelected(selected); // pre je bilo this.setSelected(selected);
	}
	
	public Donut(Point center, int radius, int innerRadius, boolean selected, Color color) {
		this(center, radius, innerRadius, selected);
		setColor(color);
	}
	
	public Donut(Point center, int radius, int innerRadius, boolean selected, Color color, Color innerColor) {
		this(center, radius, innerRadius, selected, color);
		setInnerColor(innerColor);
	}
	
	public Donut(Point center, int radius, int innerRadius, Color color) {
		this(center, radius, innerRadius);
		setColor(color);
	}
	
	public Donut(Point center, int radius, int innerRadius, Color color, Color innerColor) {
		this(center, radius, innerRadius, color);
		setInnerColor(innerColor);
	}

//________________________ Override ________________________
	
	@Override
	public int compareTo(Object o) {
		if (o instanceof Donut) {
			return (int)(this.area() - ((Donut) o).area());
		}
		return 0;
	}
	
	@Override
	public void draw(Graphics g) {
		Ellipse2D bigCircle = new Ellipse2D.Double(this.getCenter().getX() - this.getRadius(), this.getCenter().getY() - this.getRadius(), this.getRadius()*2, this.getRadius()*2);
		Ellipse2D smallCircle = new Ellipse2D.Double(this.getCenter().getX() - this.getInnerRadius(), this.getCenter().getY() - this.getInnerRadius(), this.getInnerRadius()*2, this.getInnerRadius()*2);
		Area bigArea = new Area(bigCircle);
		bigArea.subtract(new Area(smallCircle));
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(getInnerColor());
		g2d.fill(bigArea);
		g2d.setColor(getColor());
		g2d.draw(bigArea);
		
		if (isSelected()) {
			g.setColor(Color.BLUE);
			g.drawRect(this.getCenter().getX() + getInnerRadius() - 3, this.getCenter().getY()-3, 6, 6);
			g.drawRect(this.getCenter().getX() - getInnerRadius() - 3, this.getCenter().getY()-3, 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() + getInnerRadius() -3, 6, 6);
			g.drawRect(this.getCenter().getX()  - 3, this.getCenter().getY() - getInnerRadius() -3, 6, 6);
			
			g.drawRect(this.getCenter().getX() + getRadius() - 3, this.getCenter().getY()-3, 6, 6);
			g.drawRect(this.getCenter().getX() - getRadius() - 3, this.getCenter().getY()-3, 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() + getRadius() -3, 6, 6);
			g.drawRect(this.getCenter().getX()  - 3, this.getCenter().getY() - getRadius() -3, 6, 6);
		}
	}

//________________________ Metode ________________________

	public boolean contains(int x, int y) {
		double dFromCenter = this.getCenter().distance(x, y);
		return super.contains(x, y) && dFromCenter > innerRadius;
	}
	
	public boolean contains(Point p) {
		double dFromCenter = this.getCenter().distance(p.getX(), p.getY());
		return super.contains(p.getX(), p.getY()) && dFromCenter > innerRadius;
	}
	
	public double area() {
		return super.area() - innerRadius * innerRadius * Math.PI;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Donut) {
			Donut pomocna = (Donut) obj;
			if (this.innerRadius == pomocna.innerRadius && this.getRadius() == pomocna.getRadius() && this.getCenter().equals(pomocna.getCenter())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public String toString() {
		//Donut: (x,y), R=radius, r=inner radius, colors: (boja,boja)
		return "Donut: (" + super.getCenter().getX() + "," + super.getCenter().getY() + "), R=" + super.getRadius() + ", r=" + innerRadius + ", colors: (" + Integer.toString(super.getColor().getRGB()) + "," + Integer.toString(super.getInnerColor().getRGB()) + ")";
	}
	
	public Donut clone() {
		Donut donut = new Donut();
		
		donut.getCenter().setX(this.getCenter().getX());
		donut.getCenter().setY(this.getCenter().getY());
		try 
		{
			donut.setRadius(this.getRadius());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		donut.setInnerRadius(this.getInnerRadius());
		donut.setColor(this.getColor());
		donut.setInnerColor(this.getInnerColor());
		
		return donut;
	}

	
//________________________ Geteri i seteri ________________________
	
	public int getInnerRadius() {
		return innerRadius;
	}
	
	public void setInnerRadius(int innerRadius) {
		this.innerRadius = innerRadius;
	}
}
