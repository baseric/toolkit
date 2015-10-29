package ui.editpart.toolbar;

import images.IconFactory;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import ui.editpart.abstractPart.ContainerPart;
import ui.model.toolbar.ToolBarItemModel;
import util.Log;

public class ToolBarItemEditPart extends ContainerPart{
	private Label button = null;
	private String icon = "";
	public void performRequest(Request req) {
		super.performRequest(req);
		if(req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)){
			performDirectEdit();
			return;
		}
	}
	@Override
	protected IFigure createFigure() {
		try{
			ToolBarItemModel model = (ToolBarItemModel)this.getModel();
			button = new Label();
			icon = model.val("iconCls");
			button.setIcon(IconFactory.getImageDescriptor("ui/button/"+(icon.replace("icon-", ""))+".png").createImage());
			button.setIconAlignment(Label.LEFT);
			button.setBounds(new Rectangle(0,0,Integer.parseInt(model.getWidth()),Integer.parseInt(model.getHeight())));
			button.setText(model.val("text"));
			button.setIconTextGap(10);
			button.setLabelAlignment(PositionConstants.LEFT);
			return button;
		}catch(Exception e){
			Log.write("出现异常", e);
		}
		return null;
	}
	@Override
	public void reLocationChildren() {
		ToolBarItemModel model = (ToolBarItemModel)this.getModel();
		if(button!=null){
			button.setText(model.val("text"));
			String temp = model.val("iconCls");
			if(!icon.equals(temp)){
				icon = temp;
				button.setIcon(IconFactory.getImageDescriptor("ui/button/"+(icon.replace("icon-", ""))+".png").createImage());
			}
		}
	}
}
