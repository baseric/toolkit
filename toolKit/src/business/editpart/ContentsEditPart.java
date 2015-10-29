package business.editpart;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPolicy;

import business.EditPartWithListener;
import business.model.ContentsModel;
import business.policy.CustomXYLayoutPolicy;

public class ContentsEditPart extends EditPartWithListener{
	private Layer figure = null;
	protected IFigure createFigure() {
		figure = new Layer();
		figure.setLayoutManager(new XYLayout());
		return figure;
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CustomXYLayoutPolicy());
	}
	protected List<?> getModelChildren() {
		// TODO Auto-generated method stub
		return ((ContentsModel)this.getModel()).getChildren();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		try {
			if(evt.getPropertyName().equals("children")){
				this.refreshChildren();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
