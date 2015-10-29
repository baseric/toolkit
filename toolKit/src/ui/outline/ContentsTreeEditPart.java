package ui.outline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import ui.model.UIContentsModel;

public class ContentsTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener{
	public ContentsTreeEditPart(Object model) {
		super(model);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		try {
			refreshChildren();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	public void activate() {
		super.activate();
		((UIContentsModel) getModel()).addListener(this);
	}
	public void deactivate() {
		super.deactivate();
		((UIContentsModel) getModel()).deleteListener(this);
	}
	protected List<?> getModelChildren() {
		return ((UIContentsModel) getModel()).getChildren();
	}
}
