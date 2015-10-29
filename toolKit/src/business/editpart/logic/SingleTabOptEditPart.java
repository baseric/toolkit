package business.editpart.logic;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Display;

import business.dialog.SingleTabOptNew;
import business.editpart.DefNodeEditPart;
import business.editpart.NodeFigure;
import business.model.logic.SingleTabOptModel;

public class SingleTabOptEditPart extends DefNodeEditPart {
	/**
	 * 双击打开窗口
	 */
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN){
			try{
				SingleTabOptModel end = (SingleTabOptModel)this.getModel();
				TitleAreaDialog dialog = new SingleTabOptNew(Display.getCurrent().getActiveShell(),end);
				dialog.setHelpAvailable(false);
				dialog.open();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void refreshVisuals() {
		SingleTabOptModel end = (SingleTabOptModel)this.getModel();
		NodeFigure figure = (NodeFigure)this.getFigure();
		figure.label.setText(end.getDisplay());
		super.refreshVisuals();
	}
}
