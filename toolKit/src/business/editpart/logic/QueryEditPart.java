package business.editpart.logic;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.swt.widgets.Display;

import business.dialog.QueryDialog;
import business.editpart.DefNodeEditPart;
import business.editpart.NodeFigure;
import business.model.logic.QueryModel;

public class QueryEditPart extends DefNodeEditPart {
	/**
	 * 双击打开窗口
	 */
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN){
			try{
				QueryModel method = (QueryModel)this.getModel();
				QueryDialog dialog = new QueryDialog(Display.getCurrent().getActiveShell(),method);
				dialog.setHelpAvailable(false);
				dialog.open();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void refreshVisuals() {
		QueryModel method = (QueryModel)this.getModel();
		NodeFigure figure = (NodeFigure)this.getFigure();
		figure.label.setText(method.getDisplay());
		super.refreshVisuals();
	}

	
	
}
