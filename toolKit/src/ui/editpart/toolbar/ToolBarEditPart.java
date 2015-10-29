package ui.editpart.toolbar;

import images.IconFactory;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.editpart.abstractPart.ContainerPart;
import ui.model.toolbar.ToolBarModel;

public class ToolBarEditPart extends ContainerPart{
	public ImageFigure figure = null;
	public ToolBarEditPart(){
		super();
		offset = 2;
	}
	protected IFigure createFigure() {
		ToolBarModel item = (ToolBarModel)this.getModel();
		Rectangle rect = new Rectangle(20,30,Integer.parseInt(item.getWidth()),Integer.parseInt(item.getHeight()));
		figure = new ImageFigure();
		figure.setBounds(rect);
		figure.setImage(IconFactory.getImageDescriptor("ui/grid/toolbar_new.png").createImage());
		figure.setBorder(null);
	 
		XYLayout layout = new XYLayout();
		figure.setLayoutManager(layout);
		return figure;
	}
}
