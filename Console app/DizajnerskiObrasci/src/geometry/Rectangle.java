package geometry;

import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class Rectangle extends SurfaceShape {

//________________________ Deklarisanje promenljivih ________________________

	private Point upperLeftPoint = new Point();
	private int width;
	private int height;
	
//________________________ Konstruktori ________________________
	
	public Rectangle() {
		
	}
	
	public Rectangle(Point upperLeftPoint, int width, int height) {
		this.upperLeftPoint = upperLeftPoint;
		setWidth(width);
		setHeight(height);
	}
	
	public Rectangle(Point upperLeftPoint, int width, int height, boolean selected) {
		this.upperLeftPoint = upperLeftPoint;
		setWidth(width);
		setHeight(height);
		setSelected(selected); //pre je bilo this.setSelected(selected);
	}
	
	public Rectangle(Point upperLeftPoint, int width, int height, boolean selected, Color color) {
		this(upperLeftPoint, width, height);
		setColor(color);
	}
	
	public Rectangle(Point upperLeftPoint, int width, int height, boolean selected, Color color, Color innerColor) {
		this(upperLeftPoint, width, height, selected, color);
		setInnerColor(innerColor);
	}
	
	public Rectangle(Point upperLeftPoint, int width, int height, Color color) {
		this.upperLeftPoint = upperLeftPoint;
		setWidth(width);
		setHeight(height);
		setColor(color);
	}
	
	public Rectangle(Point upperLeftPoint, int width, int height, Color color, Color innerColor) {
		this(upperLeftPoint, width, height, color);
		setInnerColor(innerColor);
	}
	
//________________________ Override ________________________
	
	@Override
	public int compareTo(Object o) {
		if (o instanceof Rectangle) {
			return this.area() - ((Rectangle) o).area();
		}
		return 0;
	}
	
	@Override
	public void moveBy(int byX, int byY) {
		this.upperLeftPoint.moveBy(byX, byY);
	}
	
	@Override
	public void fill(Graphics g) {
		g.setColor(getInnerColor());
		g.fillRect(this.upperLeftPoint.getX() + 1 , this.upperLeftPoint.getY() + 1, this.width - 1, this.height - 1);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(getColor());
		g.drawRect(this.upperLeftPoint.getX(), this.upperLeftPoint.getY(), this.width, this.height);
		this.fill(g);
		
		if (isSelected()) {
			g.setColor(Color.BLUE);
			g.drawRect(this.upperLeftPoint.getX() - 3 , this.upperLeftPoint.getY() - 3, 6, 6);
			g.drawRect(this.upperLeftPoint.getX() - 3 , this.upperLeftPoint.getY() + this.height - 3, 6, 6);
			g.drawRect(this.upperLeftPoint.getX() + this.width - 3 , this.upperLeftPoint.getY() - 3, 6, 6);
			g.drawRect(this.upperLeftPoint.getX() + this.width - 3 , this.upperLeftPoint.getY() + this.height - 3, 6, 6);
		}
	}
	
//________________________ Metode ________________________
	
	public boolean contains(int x, int y) {
		if (this.upperLeftPoint.getX() <= x && this.upperLeftPoint.getY() <= y && this.upperLeftPoint.getX() + this.width >= x && this.upperLeftPoint.getY() + this.height >= y) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean contains(Point p) {
		if (this.upperLeftPoint.getX() <= p.getX() && this.upperLeftPoint.getY() <= p.getY() && this.upperLeftPoint.getX() + this.width >= p.getX() && this.upperLeftPoint.getY() + this.height >= p.getY()) {
			return true;
		} else {
			return false;
		}
	}
	
	public int area() {
		return width * height;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Rectangle) {
			Rectangle pomocna = (Rectangle) obj;
			if (this.upperLeftPoint.equals(pomocna.upperLeftPoint) && this.width == pomocna.width && this.height == pomocna.height) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public String toString() {
		//Rectangle: (x,y), dimensions=widthXheight, colors: (boja,boja)
		return "Rectangle: (" + upperLeftPoint.getX() + "," + upperLeftPoint.getY() + "), dimensions=" + width + "x" + height + ", colors: (" + Integer.toString(getColor().getRGB()) + "," + Integer.toString(getInnerColor().getRGB()) + ")";
 	}
	
	public Rectangle clone(Rectangle r) {

		r.getUpperLeftPoint().setX(this.getUpperLeftPoint().getX());
		r.getUpperLeftPoint().setY(this.getUpperLeftPoint().getY());
		r.setHeight(this.getHeight());
		r.setWidth(this.getWidth());
		r.setColor(this.getColor());
		r.setInnerColor(this.getInnerColor());
		
		return r;
	}
//________________________ Geteri i seteri ________________________
	
	public Point getUpperLeftPoint() {
		return upperLeftPoint;
	}
	
	public void setUpperLeftPoint(Point upperLeftPoint) {
		this.upperLeftPoint = upperLeftPoint;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
}
