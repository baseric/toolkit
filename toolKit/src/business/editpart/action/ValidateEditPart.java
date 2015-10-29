package business.editpart.action;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Display;

import business.dialog.ValidateDialog;
import business.editpart.DefNodeEditPart;
import business.model.action.ValidateModel;

public class ValidateEditPart extends DefNodeEditPart {
	/**
	 * 双击打开窗口
	 */
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN){
			try{
				ValidateModel model = (ValidateModel)this.getModel();
				TitleAreaDialog dialog = new ValidateDialog(Display.getCurrent().getActiveShell(),model);
				dialog.setHelpAvailable(false);
				dialog.open();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
