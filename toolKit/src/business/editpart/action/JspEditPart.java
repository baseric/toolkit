package business.editpart.action;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Display;

import business.dialog.JspDialog;
import business.editpart.DefNodeEditPart;
import business.editpart.NodeFigure;
import business.model.action.JspModel;

public class JspEditPart extends DefNodeEditPart {
	/**
	 * 双击打开窗口
	 */
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN){
			try{
				JspModel jsp = (JspModel)this.getModel();
				TitleAreaDialog dialog = new JspDialog(Display.getCurrent().getActiveShell(),jsp);
				dialog.setHelpAvailable(false);
				dialog.open();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void refreshVisuals() {
		JspModel method = (JspModel)this.getModel();
		NodeFigure figure = (NodeFigure)this.getFigure();
		figure.label.setText(method.getDisplay());
		super.refreshVisuals();
	}
}
