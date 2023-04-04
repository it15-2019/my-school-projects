package command;

import geometry.Donut;

public class UpdateDonutCmd implements Command {

	private Donut oldState; 
	private Donut newState;
	Donut original = new Donut(); 
	
	public UpdateDonutCmd(Donut oldState, Donut newState)
	{
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public void execute() {
		//original = oldState.clone(original);
		//oldState = newState.clone(oldState);
		
		original = oldState.clone();
		oldState.getCenter().setX(newState.getCenter().getX());
		oldState.getCenter().setY(newState.getCenter().getY());
		try {
			oldState.setRadius(newState.getRadius());
		} catch (Exception e) {
			e.printStackTrace();
		}
		oldState.setInnerRadius(newState.getInnerRadius());
		oldState.setColor(newState.getColor());
		oldState.setInnerColor(newState.getInnerColor());
	}

	@Override
	public void unexecute() {
		//oldState = original.clone(oldState);
		
		oldState.getCenter().setX(original.getCenter().getX());
		oldState.getCenter().setY(original.getCenter().getY());
		try {
			oldState.setRadius(original.getRadius());
		} catch (Exception e) {
			e.printStackTrace();
		}
		oldState.setInnerRadius(original.getInnerRadius());
		oldState.setColor(original.getColor());
		oldState.setInnerColor(original.getInnerColor());
	}

	public Donut getOriginal() {
		return original;
	}

}
