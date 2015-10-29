package business.editpart.logic;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Display;

import business.dialog.LogicStartDialog;
import business.editpart.DefNodeEditPart;
import business.model.ContentsModel;
import business.model.logic.LogicStartModel;

public class LogicStartEditPart extends DefNodeEditPart {
	/**
	 * 双击打开窗口
	 */
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN){
			try{
				ContentsModel contents = this.getContentModel(this);
				LogicStartModel model = (LogicStartModel)this.getModel();
				TitleAreaDialog dialog = new LogicStartDialog(Display.getCurrent().getActiveShell(),model,contents.getFile().getProject());
				dialog.setHelpAvailable(false);
				dialog.open();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
