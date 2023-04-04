package command;

import java.util.Collections;

import geometry.Shape;
import mvc.DrawingModel;

public class BringToFrontCmd implements Command {
	
	private DrawingModel model;
	private Shape shape;
	private int index;
	private int poslednji;
	
	public BringToFrontCmd(DrawingModel model,Shape shape)
	{
		this.model = model;
		this.shape = shape;
		index = model.getShapes().indexOf(shape);
		poslednji = model.getShapes().size()-1;
	}
	
	@Override
	public void execute() {
		for(int i = index; i < poslednji; i++)
		{
			Collections.swap(model.getShapes(), i, i+1);
		}
	}

	@Override
	public void unexecute() {
		for(int i = poslednji; i > index; i--)
		{
			Collections.swap(model.getShapes(), i, i-1);
		}
		
	}

}
