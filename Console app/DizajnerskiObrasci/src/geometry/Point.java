package geometry;

import java.awt.Color;
import java.awt.Graphics;


@SuppressWarnings("serial")
public class Point extends Shape {
	
	private int x;
	private int y;
	
	//overload - pojava kada se 2 ili vise metoda mogu zvati isto
	//1. uslov: mora da se razlikuju po broju parametra
	//2. uslov: u slucaju da imaju isti br parametra, tipovi se moraju razlikovati
	
	public Point() {
	
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(int x, int y, boolean selected) {
		this(x, y); //kada pozivamo konstruktor to mora biti u 1. liniji koda
		setSelected(selected); 
	}
	
	public Point(int x, int y, boolean selected, Color color) {
		this(x, y, selected);
		setColor(color);
	}
	
	public Point(int x, int y, Color color) {
		this(x,y);
		setColor(color);
	}
	
	public int compareTo(Object o) {
		if (o instanceof Point) {
			Point pocetak = new Point(0, 0);
			return (int)(this.distance(pocetak.getX(), pocetak.getY()) - ((Point) o).distance(pocetak.getX(), pocetak.getY()));
		}
		return 0;
	}
	
	public void moveBy(int byX, int byY) {
		this.x = this.x + byX; //this.x += byX;
		this.y = this.y + byY; //this.y += byY;
	}
	
	public void draw(Graphics g) {
		g.setColor(getColor());
		g.drawLine(this.x - 2, this.y, this.x + 2, this.y);
		g.drawLine(this.x, this.y - 2, this.x, this.y + 2);
	
		if (isSelected()) {
			g.setColor(Color.BLUE);
			g.drawRect(this.x - 3, this.y - 3, 6, 6);
		}
	}
	
	public boolean contains(int x, int y) {
		return this.distance(x, y) <= 3;
		//if (this.distance(x, y) <= 3) {
		//	return true;
		//} else {
		//	return false;
		//}
	}
	
	public double distance(int x2, int y2) {
		double dx = this.x - x2;
		double dy = this.y - y2;
		double d = Math.sqrt(dx * dx + dy * dy);
		return d;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Point) {
			Point pomocna = (Point) obj;
			if (this.x == pomocna.x && this.y == pomocna.y) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public int getX() {
		return this.x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	//Enkapsulacija u Javi - Varijable i instance su privatne, a da se za njih 
	//prave metode pristupa koje su public. One podrazumevaju get metode koje 
	//pristupaju vrednosti i set metode koje ih postavljaju na neke nove vrednosti
	
	public int getY() {
		return this.y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	//override - preklapanje metoda nad klase 
	
	//Point: (x,y), color: (boja)
	public String toString() {
		return "Point: (" + x + "," + y + "), color: (" + Integer.toString(getColor().getRGB()) + ")"; 
	}
	
	public Point clone(Point p) {

		p.setX(this.getX());
		p.setY(this.getY());
		p.setColor(this.getColor());
		
		return p;
	}
	
}
