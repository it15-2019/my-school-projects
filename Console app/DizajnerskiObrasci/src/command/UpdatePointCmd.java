package command;

import geometry.Point;

public class UpdatePointCmd implements Command {
	
	private Point oldState; //privremeno cuva
	private Point newState;
	Point original = new Point(); //trajno cuva 
	
	public UpdatePointCmd(Point oldState, Point newState)
	{
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public void execute() {
		original = oldState.clone(original);
		oldState = newState.clone(oldState);
	}

	@Override
	public void unexecute() {
		oldState = original.clone(oldState);
	}

	public Point getOriginal() {
		return original;
	}
}
