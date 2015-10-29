package business.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.swt.widgets.Display;

import business.dialog.LinkDialog;
import business.model.AbstractConnectionModel;
import business.model.LineConnectionModel;
import business.policy.CommConnectionEndpointEditPolicy;
import business.policy.CustomConnectionEditPolicy;

public class LineConnectionEditPart extends AbstractConnectionEditPart implements PropertyChangeListener {
	private Label label = null;
	public void activate() {
		super.activate();
		((AbstractConnectionModel)this.getModel()).addListener(this);
	}
	 
	public void deactivate() {
		super.deactivate();
		((AbstractConnectionModel)this.getModel()).deleteListener(this);
	}
	@Override
	protected IFigure createFigure() {
		LineConnectionModel model = (LineConnectionModel)this.getModel();
		PolylineConnection line = new PolylineConnection();
		line.setTargetDecoration(new PolygonDecoration());
		line.setLineWidth(2);
		
		// add a label
		label = new Label(model.getDisplay());
	    label.setOpaque(true);
	    line.add(label, new MidpointLocator(line, 0));
	    
		return line;
	}

	@Override
	protected void refreshVisuals() {
		LineConnectionModel model = (LineConnectionModel)this.getModel();
		label.setText(model.getDisplay());
		label.repaint();
		super.refreshVisuals();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new CustomConnectionEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new CommConnectionEndpointEditPolicy());
	}
	/**
	 * 双击打开窗口
	 */
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN){
			try{
				LineConnectionModel model = (LineConnectionModel)this.getModel();
				LinkDialog dialog = new LinkDialog(Display.getCurrent().getActiveShell(),model);
				dialog.setHelpAvailable(false);
				dialog.open();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		refreshVisuals();
	}
}
