package observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import mvc.DrawingFrame;

public class ObserverUpdate implements PropertyChangeListener {

	private DrawingFrame frame;
	
	public ObserverUpdate(DrawingFrame frame)
	{
		this.frame = frame;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("tglDelete")) {
			frame.getTglDelete().setEnabled((boolean)evt.getNewValue());
		}
		if(evt.getPropertyName().equals("tglModify")) {
			frame.getTglModify().setEnabled((boolean)evt.getNewValue());
		}
	}
}
