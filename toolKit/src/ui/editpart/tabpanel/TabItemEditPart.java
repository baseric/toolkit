package ui.editpart.tabpanel;

import images.IconFactory;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;
import ui.editpart.forms.IframeEditPart;
import ui.figure.TabFigure;
import ui.model.tabpanel.TabItemModel;

public class TabItemEditPart extends ContainerPart {
	private boolean hasSelect = true;
	
	private ImageFigure tabItem = null;
	public Label name = null;
	@Override
	protected IFigure createFigure() {
		TabEditPart tab = (TabEditPart)this.getParent();
		tabItem = new ImageFigure();
		Rectangle tabRect = tab.getFigure().getBounds();
		
		TabItemModel model = (TabItemModel)this.getModel();
		Rectangle title = new Rectangle(tabRect.x+1,tabRect.y+1,Integer.parseInt(model.getWidth()),Integer.parseInt(model.getHeight()));
		tabItem.setImage(IconFactory.getImageDescriptor("ui/tab_unselect.png").createImage());
		tabItem.setBounds(title);
		
		//设置标题
		name = new Label(model.val("title"));
		name.setTextAlignment(PositionConstants.LEFT);
		name.setLabelAlignment(PositionConstants.LEFT);
		name.setBounds(title);
		tabItem.add(name);
		
		return tabItem;
	}
	@Override
	public void deactivate() {
		try {
			List temps = this.getChildren();
			TabFigure parent = (TabFigure)((TabEditPart)this.getParent()).getFigure();
			for(int j = 0;j<temps.size();j++){
				UIEditPartWithListener p = (UIEditPartWithListener)temps.get(j);
				parent.itemContainer.remove(p.getFigure());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.deactivate();
	}
	@Override
	public IFigure getContentPane() {
		TabEditPart parent = (TabEditPart)this.getParent();
		TabFigure figure = parent.figure;
		return figure.itemContainer;
	}

	/**
	 * 标签页的状态切换
	 * @param evt
	 * 2015-1-30
	 * @tianming
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if("select".equals(evt.getPropertyName())){
			setUnselect();
			tabItem.setImage(IconFactory.getImageDescriptor("ui/tab.png").createImage());
			this.setHasSelect(true);
			List temps = this.getChildren();
			for(int j = 0;j<temps.size();j++){
				UIEditPartWithListener p = (UIEditPartWithListener)temps.get(j);
				p.getFigure().setVisible(true);
			}
		}
		TabItemModel model = (TabItemModel)this.getModel();
		name.setText(model.val("title"));
		super.propertyChange(evt);
	}
	/**
	 * 设置标签页为非选中状态 
	 * 2015-1-30
	 * @tianming
	 */
	public void setUnselect(){
		TabEditPart part = (TabEditPart)this.getParent();
		((TabFigure)part.getFigure()).itemContainer.setVisible(true);
		List children = part.getChildren();
		for(int i = 0;i<children.size();i++){
			EditPart item = (EditPart)children.get(i);
			if(item instanceof TabItemEditPart){
				TabItemEditPart tabItem = (TabItemEditPart)item;
				tabItem.getTabItem().setImage(IconFactory.getImageDescriptor("ui/tab_unselect.png").createImage());
				tabItem.setHasSelect(false);
				List temps = tabItem.getChildren();
				for(int j = 0;j<temps.size();j++){
					UIEditPartWithListener p = (UIEditPartWithListener)temps.get(j);
					p.getFigure().setVisible(false);
				}
			}
		}
	}
	@Override
	public void performRequest(Request req) {
		if(req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)){
			performDirectEdit();
			return;
		}
		super.performRequest(req);
	}
	@Override
	public void reLocationChildren() {
		try {
			TabFigure figure = ((TabEditPart)this.getParent()).figure;
			Figure itemContainer = figure.itemContainer;
			Rectangle container = itemContainer.getBounds();
			List children = this.getChildren();
			for(int i = 0;i<children.size();i++){
				UIEditPartWithListener childPart = (UIEditPartWithListener)children.get(i);
				Rectangle rect = childPart.getFigure().getBounds();
				rect.x = container.x;
				rect.y = container.y;
				rect.width = container.width;
				rect.height = container.height;
				childPart.getFigure().setBounds(rect);
				if(!(childPart instanceof IframeEditPart)){
					childPart.getFigure().setBorder(null);
				}
				childPart.getFigure().repaint();
				childPart.reSetInner();
				if(childPart instanceof ContainerPart){
					((ContainerPart)childPart).reLocationChildren();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isHasSelect() {
		return hasSelect;
	}
	public void setHasSelect(boolean hasSelect) {
		this.hasSelect = hasSelect;
	}
	public ImageFigure getTabItem() {
		return tabItem;
	}
	public void setTabItem(ImageFigure tabItem) {
		this.tabItem = tabItem;
	}
}
