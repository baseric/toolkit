package ui.editpart.grid;
 
import images.IconFactory;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.editpart.abstractPart.ContainerPart;

public class ColumnBarEditPart extends ContainerPart{
	public ImageFigure figure = null;
	protected IFigure createFigure() {
		Rectangle rect = new Rectangle(20,30,0,0);
		figure = new ImageFigure();
		figure.setBounds(rect);
		figure.setImage(IconFactory.getImageDescriptor("ui/grid/columnbar.png").createImage());
		XYLayout layout = new XYLayout();
		figure.setLayoutManager(layout);
		return figure;
	}
}
