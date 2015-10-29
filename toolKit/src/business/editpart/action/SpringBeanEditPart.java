package business.editpart.action;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.swt.widgets.Display;

import business.dialog.MethodDialog;
import business.editpart.DefNodeEditPart;
import business.editpart.NodeFigure;
import business.model.action.MethodModel;

public class SpringBeanEditPart extends DefNodeEditPart {
	/**
	 * 双击打开窗口
	 */
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN){
			try{
				MethodModel method = (MethodModel)this.getModel();
				MethodDialog dialog = new MethodDialog(Display.getCurrent().getActiveShell(),method);
				dialog.setHelpAvailable(false);
				dialog.open();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void refreshVisuals() {
		MethodModel method = (MethodModel)this.getModel();
		NodeFigure figure = (NodeFigure)this.getFigure();
		figure.label.setText(method.getDisplay());
		super.refreshVisuals();
	}

	
	
}
