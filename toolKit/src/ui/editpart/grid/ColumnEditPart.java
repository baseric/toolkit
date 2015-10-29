package ui.editpart.grid;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import ui.editpart.abstractPart.ContainerPart;
import ui.figure.ColumnFigure;
import ui.model.grid.ColumnModel;
import util.Log;

public class ColumnEditPart extends ContainerPart {
	@Override
	public void performRequest(Request req) {
		if(req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)){
			performDirectEdit();
			return;
		}
		super.performRequest(req);
	}
	protected void createEditPolicies() {
		this.installPolicy();
	}
	@Override
	protected IFigure createFigure() {
		try{
			ColumnModel model = (ColumnModel)this.getModel();
			Label column = new ColumnFigure(model);
			column.setText(model.val("title"));
			return column;
		}catch(Exception e){
			Log.write("出现异常", e);
		}
		return null;
	}
	/**
	 * 刷新视图 
	 * 2014-12-14
	 * @tianming
	 */
 	protected void refreshVisuals() {
		try{
			ColumnModel model = (ColumnModel)this.getModel();
			ColumnFigure figure = (ColumnFigure)this.getFigure();
			String width = model.getWidth();
			if(width.indexOf("%")==-1){
				figure.setSize(Integer.parseInt(width), 23);
			}else{
				int pWidth = figure.getParent().getBounds().width;
				int w = Integer.parseInt(width.replace("%",""))*pWidth/100;
				figure.setSize(w, 23);
			}
			figure.setText(model.val("title"));
		}catch(Exception e){
			e.printStackTrace();
		}
		super.refreshVisuals();
	}
	@Override
	public void reLocationChildren() {}
}
