package business.editpart.action;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Display;

import business.dialog.StartDialog;
import business.editpart.DefNodeEditPart;
import business.model.action.StartModel;

public class StartEditPart extends DefNodeEditPart {
	/**
	 * 双击打开窗口
	 */
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN){
			try{
				StartModel model = (StartModel)this.getModel();
				TitleAreaDialog dialog = new StartDialog(Display.getCurrent().getActiveShell(),model);
				dialog.setHelpAvailable(false);
				dialog.open();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
