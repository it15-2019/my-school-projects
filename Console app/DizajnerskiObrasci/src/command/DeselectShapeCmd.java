package command;

import geometry.Shape;
import mvc.DrawingController;

public class DeselectShapeCmd implements Command {
	
	private DrawingController controller;
	private Shape shape;
	
	public DeselectShapeCmd(DrawingController controller, Shape shape) 
	{
		this.controller = controller;
		this.shape = shape;
	}

	@Override
	public void execute() {
		shape.setSelected(false);
		controller.getSelectedShapes().remove(shape);
	}

	@Override
	public void unexecute() {
		shape.setSelected(true);
		controller.getSelectedShapes().add(shape);
	}
}
