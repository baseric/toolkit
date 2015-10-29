package business.editpart;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;

import util.ToolTipUtil;

import business.AbstractModel;
import business.EditPartWithListener;
import business.policy.CustomNodeEditPolicy;

public abstract class DefNodeEditPart extends EditPartWithListener implements NodeEditPart{
	public Label label = null;
	public ImageFigure icon = null;
	@Override
	protected IFigure createFigure() {
		AbstractModel start = (AbstractModel)this.getModel();
		Figure figure = new NodeFigure(start.getDisplay(),start.getIcon(),start.getRect());
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		installPolicy();
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new CustomNodeEditPolicy());
	}
	protected void refreshVisuals() {
		try{
			AbstractModel form = (AbstractModel)this.getModel();
			Rectangle rect = form.getRect();
			Rectangle old = figure.getBounds();
			if(rect!=null){
				rect.width = old.width;
				rect.height = old.height;
				((GraphicalEditPart)getParent()).setLayoutConstraint(this,this.getFigure(),rect);
			}
			NodeFigure node = (NodeFigure)this.getFigure();
			node.setToolTip(ToolTipUtil.setToolTips(form));
			node.label.setText(form.getDisplay());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		try {
			refreshVisuals();
			if("error".equals(evt.getPropertyName())){
				NodeFigure figure = (NodeFigure)this.getFigure();
				figure.error.setVisible(true);
			}else if("success".equals(evt.getPropertyName())){
				NodeFigure figure = (NodeFigure)this.getFigure();
				figure.error.setVisible(false);
			}
			
			refreshSourceConnections();
			refreshTargetConnections();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List getModelSourceConnections() {
		// TODO Auto-generated method stub
		return ((AbstractModel)this.getModel()).getSourceConnection();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List getModelTargetConnections() {
		// TODO Auto-generated method stub
		return ((AbstractModel)this.getModel()).getTargetConnection();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart arg0) {
		return new ChopboxAnchor(this.getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request arg0) {
		return new ChopboxAnchor(this.getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart arg0) {
		return new ChopboxAnchor(this.getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request arg0) {
		return new ChopboxAnchor(this.getFigure());
	}

	
}
