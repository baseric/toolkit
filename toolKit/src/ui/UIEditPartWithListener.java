package ui;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import ui.editpart.UIContentsEditPart;
import ui.model.UIContentsModel;
import ui.policy.CustomXYLayoutPolicy;
import ui.policy.DeleteEditPolicy;
import ui.policy.DirectEditPolicyImpl;
import ui.policy.EditParentNodePolicy;
import ui.policy.SelectPartPolicy;

public abstract class UIEditPartWithListener extends AbstractGraphicalEditPart implements
		PropertyChangeListener {
	@Override
	public void activate() {
		super.activate();
		((UIAbstractModel)this.getModel()).addListener(this);
	}
	@Override
	public void deactivate() {
		try {
			super.deactivate();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		((UIAbstractModel)this.getModel()).deleteListener(this);
	}
	public void installPolicy(){
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CustomXYLayoutPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new SelectPartPolicy());   

		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteEditPolicy());
		installEditPolicy("1", new EditParentNodePolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DirectEditPolicyImpl());
	}
	
	public UIContentsModel getContentModel(UIEditPartWithListener parent){
		if(parent instanceof UIContentsEditPart){
			return (UIContentsModel)parent.getModel();
		}else{
			return getContentModel((UIEditPartWithListener)parent.getParent());
		}
	}
	/**
	 * 重新设置内部的组件位置
	 * 2014-12-25
	 * @tianming
	 */
	public void reSetInner(){}
	
	
}
