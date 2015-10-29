package business;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import business.editpart.ContentsEditPart;
import business.model.ContentsModel;
import business.policy.CustomXYLayoutPolicy;
import business.policy.DeleteEditPolicy;

public abstract class EditPartWithListener extends AbstractGraphicalEditPart implements
		PropertyChangeListener {
	public void activate() {
		super.activate();
		((AbstractModel)this.getModel()).addListener(this);
	}
	 
	public void deactivate() {
		super.deactivate();
		((AbstractModel)this.getModel()).deleteListener(this);
	}
	public void installPolicy(){
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CustomXYLayoutPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteEditPolicy());
	}
	
	public ContentsModel getContentModel(EditPartWithListener parent){
		if(parent instanceof ContentsEditPart){
			return (ContentsModel)parent.getModel();
		}else{
			return getContentModel((EditPartWithListener)parent.getParent());
		}
	}
}
