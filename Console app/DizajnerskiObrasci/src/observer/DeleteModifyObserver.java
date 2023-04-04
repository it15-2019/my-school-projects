package observer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DeleteModifyObserver {

	private boolean tglDeleteEnabled;
	private boolean tglModifyEnabled;
	
	private PropertyChangeSupport propertyChangeSupport;

	public DeleteModifyObserver() {
		propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener)
	{
		propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener)
	{
		propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
	}
	
	public void setDeleteEnabled(boolean tglDeleteEnabled)
	{
		propertyChangeSupport.firePropertyChange("tglDelete",this.tglDeleteEnabled, tglDeleteEnabled);
		this.tglDeleteEnabled = tglDeleteEnabled;
	}
	
	public void setModifyEnabled(boolean tglModifyEnabled)
	{
		propertyChangeSupport.firePropertyChange("tglModify",this.tglModifyEnabled, tglModifyEnabled);
		this.tglModifyEnabled = tglModifyEnabled;
	}
	
	public boolean isTglDeleteEnabled() {
		return tglDeleteEnabled;
	}

	public boolean isTglModifyEnabled() {
		return tglModifyEnabled;
	}
}
