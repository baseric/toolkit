package business.editpart.logic;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Display;

import business.dialog.EndDialog;
import business.editpart.DefNodeEditPart;
import business.editpart.NodeFigure;
import business.model.logic.EndModel;

public class EndEditPart extends DefNodeEditPart {
	/**
	 * 双击打开窗口
	 */
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN){
			try{
				EndModel end = (EndModel)this.getModel();
				TitleAreaDialog dialog = new EndDialog(Display.getCurrent().getActiveShell(),end);
				dialog.setHelpAvailable(false);
				dialog.open();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void refreshVisuals() {
		EndModel end = (EndModel)this.getModel();
		NodeFigure figure = (NodeFigure)this.getFigure();
		figure.label.setText(end.getDisplay());
		super.refreshVisuals();
	}
}
