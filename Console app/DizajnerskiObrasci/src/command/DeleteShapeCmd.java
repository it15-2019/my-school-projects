package command;

import geometry.Shape;
import mvc.DrawingModel;

public class DeleteShapeCmd implements Command {

	private Shape shape;
	private DrawingModel model;
	
	public DeleteShapeCmd(DrawingModel model, Shape shape) {
		this.model = model;
		this.shape = shape;
	}
	
	@Override
	public void execute() {
		model.remove(shape);

	}

	@Override
	public void unexecute() {
		model.add(shape);

	}

}
