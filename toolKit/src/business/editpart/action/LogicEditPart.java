package business.editpart.action;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.swt.widgets.Display;

import business.dialog.LogicDialog;
import business.editpart.DefNodeEditPart;
import business.model.action.LogicModel;

public class LogicEditPart extends DefNodeEditPart {
	/**
	 * 双击打开窗口
	 */
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN){
			try{
				LogicModel logic = (LogicModel)this.getModel();
				LogicDialog dialog = new LogicDialog(Display.getCurrent().getActiveShell(),logic);
				dialog.setHelpAvailable(false);
				dialog.open();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
