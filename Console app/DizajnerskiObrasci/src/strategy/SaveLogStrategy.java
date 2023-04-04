package strategy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import mvc.DrawingFrame;

public class SaveLogStrategy implements SaveStrategy {
	
	@Override
	public void saveFile(Object o, File f) {
		
		DrawingFrame frame = (DrawingFrame)o;
		BufferedWriter bufferedWriter = null;
		
		try 
		{
			bufferedWriter = new BufferedWriter((new FileWriter(f.getAbsolutePath())));
			frame.getTextArea().write(bufferedWriter);
			bufferedWriter.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

}
