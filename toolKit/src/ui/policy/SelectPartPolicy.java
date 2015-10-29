package ui.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

import toolkit.Activator;
import ui.UIAbstractModel;
import ui.model.UIContentsModel;
import views.PropertyView;

/**
 * 选择节点时触发
 * @author Administrator
 *
 */
public class SelectPartPolicy extends SelectionEditPolicy {

	@Override
	protected void hideSelection() {
		UIAbstractModel model = (UIAbstractModel)this.getHost().getModel();
		(model).firePropertyChange("unselect", null, null);
		try {
			UIContentsModel contents = null;
			UIAbstractModel temp = model;
			while(true){
				if(temp.parentModel instanceof UIContentsModel){
					contents = (UIContentsModel)temp.parentModel;
					break;
				}
				temp = temp.parentModel;
			}
			this.setModelProperty(contents);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	@SuppressWarnings("restriction")
	@Override
	protected void showSelection() {
		if(CustomXYLayoutPolicy.currentIn!=null){
			CustomXYLayoutPolicy.currentIn.firePropertyChange("move_out", null, null);
			CustomXYLayoutPolicy.currentIn = null;
		}
		UIAbstractModel model = (UIAbstractModel)this.getHost().getModel();
		try {
			Rectangle rect = this.getHostFigure().getBounds();
			WorkbenchWindow workbenchWindow = (WorkbenchWindow)PlatformUI.getWorkbench().getActiveWorkbenchWindow();   
			IStatusLineManager lineManager = workbenchWindow.getStatusLineManager();
			lineManager.setMessage("[ type:"+model.getClass().getSimpleName()+", x:"+rect.x +", y="+rect.y+", width="+rect.width+", height="+rect.height+"]");
		} catch (Exception e) {
			e.printStackTrace();
		}
		UIAbstractModel temp = model.getDefComponentByParent();
		if(temp!=null){
			model = temp;
		}
		
		this.setModelProperty(model);
		(model).firePropertyChange("select", null, null);
	}
	private void setModelProperty(UIAbstractModel model){
		try {
			IViewPart viewPart = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("views.PropertyView");    
			if(viewPart instanceof PropertyView){
				PropertyView view = (PropertyView)viewPart;
				view.setDataByModel(model);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
}
