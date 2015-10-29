package business.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

import business.AbstractModel;
@SuppressWarnings("serial")
public abstract class ContainerModel extends AbstractModel {
	private List<AbstractModel> children = new ArrayList<AbstractModel>();
	private Rectangle rect = null;
 
	public void addChild(AbstractModel child){
		this.children.add(child);
		this.firePropertyChange("children", null, child);
	}
	public void addChildArray(AbstractModel[] child){
		for(int i = 0;i<child.length;i++){
			this.children.add(child[i]);
		}
		this.firePropertyChange("children", null, child);
	}
	public void addChildList(List<AbstractModel> child){
		for(int i = 0;i<child.size();i++){
			this.children.add(child.get(i));
		}
		this.firePropertyChange("children", null, child);
	}
	public List<AbstractModel> getChildren(){
		return this.children;
	}
	public void removeChild(Object child){
		children.remove(child);
		this.firePropertyChange("children", null, child);
	}
	public void reLocationIndex(Object child,int i){
		children.remove(child);
		children.add(i,(AbstractModel)child);
		this.firePropertyChange("relocationChild", null, child);
	}

	public Rectangle getRect() {
		return rect;
	}
	
	public void setRect(Rectangle rect) {
		this.rect = rect;
		firePropertyChange("resizeForm", null, rect);
	}
}
