package command;

import geometry.Line;

public class UpdateLineCmd implements Command {
	
	private Line oldState;
	private Line newState;
	private Line original = new Line();

	public UpdateLineCmd(Line oldState, Line newState)
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

	public Line getOriginal() {
		return original;
	}

	public void setOriginal(Line original) {
		this.original = original;
	}
}
