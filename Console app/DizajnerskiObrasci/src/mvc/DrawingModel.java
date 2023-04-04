package mvc;

import java.util.ArrayList;
import geometry.Shape;

public class DrawingModel {
	
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	
	public void add(Shape shape) 
	{
		shapes.add(shape);
	}
	
	public void remove(Shape shape) 
	{
		shapes.remove(shape);
	}
	
	public Shape get(int index) 
	{
		return shapes.get(index);
	}

	//_____________________ Geteri i seteri _____________________
	public ArrayList<Shape> getShapes() {
		return shapes;
	}
}
