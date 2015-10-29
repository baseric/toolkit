package business.editpart.action;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Display;

import business.dialog.VariableDialog;
import business.editpart.DefNodeEditPart;
import business.model.action.VariableModel;

public class VariableEditPart extends DefNodeEditPart {
	/**
	 * 双击打开窗口
	 */
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN){
			try{
				VariableModel varModel = (VariableModel)this.getModel();
				TitleAreaDialog dialog = new VariableDialog(Display.getCurrent().getActiveShell(),varModel);
				dialog.setHelpAvailable(false);
				dialog.open();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
