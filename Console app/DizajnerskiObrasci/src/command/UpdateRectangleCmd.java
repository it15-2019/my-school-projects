package command;

import geometry.Rectangle;

public class UpdateRectangleCmd implements Command {
	
	private Rectangle oldState; 
	private Rectangle newState;
	Rectangle original = new Rectangle(); 
	
	public UpdateRectangleCmd(Rectangle oldState, Rectangle newState)
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

	public Rectangle getOriginal() {
		return original;
	}
}
