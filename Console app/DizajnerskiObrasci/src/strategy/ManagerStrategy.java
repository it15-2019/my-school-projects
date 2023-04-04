package strategy;

import java.io.File;

public class ManagerStrategy implements SaveStrategy {

	private SaveStrategy save;
	
	public ManagerStrategy(SaveStrategy save)
	{
		this.save = save;
	}
	
	@Override
	public void saveFile(Object o, File f) {
		save.saveFile(o,f);
	}
	
	public SaveStrategy getSave() {
		return save;
	}

	public void setSave(SaveStrategy save) {
		this.save = save;
	}
}
