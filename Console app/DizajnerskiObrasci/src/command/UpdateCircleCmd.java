package command;

import geometry.Circle;

public class UpdateCircleCmd implements Command {

	private Circle oldState;
	private Circle newState;
	private Circle original = new Circle();
	
	public  UpdateCircleCmd(Circle oldState,Circle newState)
	{
		this.oldState=oldState;
		this.newState=newState;
	}
	
	@Override
	public void execute() {
		original = oldState.clone(original);
		oldState = newState.clone(oldState);
	}
	
	@Override
	public void unexecute() {
		oldState  = original.clone(oldState);
	}
	
	public Circle getOriginal() {
		return original;
	}
}
