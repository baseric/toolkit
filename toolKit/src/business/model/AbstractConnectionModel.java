package business.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import business.AbstractModel;

public abstract class AbstractConnectionModel {
	public AbstractModel source,target;
	private PropertyChangeSupport listener = new PropertyChangeSupport(this);
	public void attachSource(){
		if(!source.getSourceConnection().contains(this)){
			source.addSourceConnection(this);
		}
	}
	public void detachSource(){
		source.removeSourceConnection(this);
	}
	public void attachTarget(){
		if(!target.getTargetConnection().contains(this)){
			target.addTargetConnection(this);
		}
	}
	public void detachTarget(){
		target.removeTargetConnection(this);
	}
	public AbstractModel getSource() {
		return source;
	}
	public void setSource(AbstractModel source) {
		this.source = source;
	}
	public AbstractModel getTarget() {
		return target;
	}
	public void setTarget(AbstractModel target) {
		this.target = target;
	}
	
	public void addListener(PropertyChangeListener l){
		listener.addPropertyChangeListener(l);
	}
	public void deleteListener(PropertyChangeListener l){
		listener.removePropertyChangeListener(l);
	}
	public void firePropertyChange(String propName,Object oldValue,Object newValue){
		listener.firePropertyChange(propName, oldValue, newValue);
	}
}
