package strategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import mvc.DrawingModel;

public class SaveShapesStrategy implements SaveStrategy {

	@Override
	public void saveFile(Object o, File f) {
		
		DrawingModel model = (DrawingModel)o;
		ObjectOutputStream objectOutputStream = null;
		try 
		{
			objectOutputStream = new ObjectOutputStream(new FileOutputStream(f));
			objectOutputStream.writeObject(model.getShapes());
			objectOutputStream.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

}
