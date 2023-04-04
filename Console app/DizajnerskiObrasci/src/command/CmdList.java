package command;

import java.util.ArrayList;

import geometry.Shape;

public class CmdList {

	private ArrayList<Command> list = new ArrayList<Command>();
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	private ArrayList<String> logUndo = new ArrayList<String>();
	private ArrayList<String> logRedo = new ArrayList<String>();
	
	private int current = 0;
	
	public void deleteElementsAfterPointer(int current)
	{
		if(list.size() < 1)
		{
			return;
		}
		for(int i = list.size() - 1; i > current; i--)
		{
			list.remove(i);
			shapes.remove(i);
			logUndo.remove(i);
			logRedo.remove(i);
		}
	}
	
	public void undo() {
		
		list.get(current-1).unexecute();
		current--;
		
	}
	
	public void redo() {
		current++; 
		list.get(current-1).execute();
	}
	
	
//________________________ Geteri i seteri _________________________________

	public ArrayList<Command> getList() {
		return list;
	}

	public void setList(ArrayList<Command> list) {
		this.list = list;
	}

	public ArrayList<Shape> getShapes() {
		return shapes;
	}

	public void setShapes(ArrayList<Shape> shapes) {
		this.shapes = shapes;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public ArrayList<String> getLogUndo() {
		return logUndo;
	}

	public void setLogUndo(ArrayList<String> logUndo) {
		this.logUndo = logUndo;
	}

	public ArrayList<String> getLogRedo() {
		return logRedo;
	}

	public void setLogRedo(ArrayList<String> logRedo) {
		this.logRedo = logRedo;
	}
}
