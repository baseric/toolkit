package ui.model.abstractModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

import ui.UIAbstractModel;
@SuppressWarnings("serial")
public abstract class ContainerModel extends UIAbstractModel {
	private List<UIAbstractModel> children = new ArrayList<UIAbstractModel>();
	private Rectangle rect = null;
	public ContainerModel(String modelName){
		super(modelName);
	}
	public void addChild(UIAbstractModel child){
		this.children.add(child);
		this.firePropertyChange("children", null, child);
	}
	public void addChild(UIAbstractModel child,int idex){
		this.children.add(idex,child);
		this.firePropertyChange("children", null, child);
	}
	public void addChildArray(UIAbstractModel[] child){
		for(int i = 0;i<child.length;i++){
			this.children.add(child[i]);
		}
		this.firePropertyChange("children", null, child);
	}
	public void addChildArray(UIAbstractModel[] child,int offset){
		for(int i = 0;i<child.length;i++){
			this.children.add(children.size()-offset,child[i]);
		}
		this.firePropertyChange("children", null, child);
	}
	public void addChildList(List<UIAbstractModel> child){
		for(int i = 0;i<child.size();i++){
			this.children.add(child.get(i));
		}
		this.firePropertyChange("children", null, child);
	}
	public List<UIAbstractModel> getChildren(){
		return this.children;
	}
	public void removeChild(Object child){
		children.remove(child);
		this.firePropertyChange("children", null, child);
	}
	public void reLocationIndex(Object child,int i){
		children.remove(child);
		children.add(i,(UIAbstractModel)child);
		this.firePropertyChange("relocationChild", null, child);
	}

	public void setChildren(List<UIAbstractModel> children) {
		this.children = children;
	}
	public Rectangle getRect() {
		return rect;
	}
	
	public void setRect(Rectangle rect) {
		this.rect = rect;
		this.setX(String.valueOf(rect.x));
		this.setY(String.valueOf(rect.y));
		this.setWidth(String.valueOf(rect.width));
		this.setHeight(String.valueOf(rect.height));
		this.setAsyncWidth(1);
		firePropertyChange("resizeForm", null, rect);
	}
}
