package business.editpart.action;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Display;

import business.dialog.ExcelDialog;
import business.editpart.DefNodeEditPart;
import business.editpart.NodeFigure;
import business.model.action.ExcelModel;

public class ExcelEditPart extends DefNodeEditPart {
	/**
	 * 双击打开窗口
	 */
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN){
			try{
				ExcelModel excel = (ExcelModel)this.getModel();
				TitleAreaDialog dialog = new ExcelDialog(Display.getCurrent().getActiveShell(),excel);
				dialog.setHelpAvailable(false);
				dialog.open();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void refreshVisuals() {
		ExcelModel method = (ExcelModel)this.getModel();
		NodeFigure figure = (NodeFigure)this.getFigure();
		figure.label.setText(method.getDisplay());
		super.refreshVisuals();
	}
}
