package command;

import java.util.Collections;

import geometry.Shape;
import mvc.DrawingModel;

public class BringToBackCmd implements Command {
	
	private DrawingModel model;
	private Shape shape;
	private int index;
	
	public BringToBackCmd(DrawingModel model,Shape shape)
	{
		this.model = model;
		this.shape = shape;
		index = model.getShapes().indexOf(shape);
	}


	@Override
	public void execute() {
		for (int i = index; i > 0; i--) 
		{
			Collections.swap(model.getShapes(), i, i-1);
		}
	}

	@Override
	public void unexecute() {
		for (int i = 0; i < index; i++) 
		{
			Collections.swap(model.getShapes(), i, i+1);
		}
	}
}
