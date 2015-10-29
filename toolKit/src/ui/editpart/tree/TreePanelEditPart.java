package ui.editpart.tree;
 
import images.IconFactory;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.UIAbstractModel;
import ui.editpart.abstractPart.ContainerPart;

public class TreePanelEditPart extends ContainerPart{
	private ImageFigure figure = null;
	protected IFigure createFigure() {
		try {
			UIAbstractModel model = (UIAbstractModel)this.getModel();
			Rectangle rect = new Rectangle( 20, 20,Integer.parseInt(model.getWidth()),Integer.parseInt(model.getHeight()));
			figure = new ImageFigure();
			figure.setBounds(rect);
			XYLayout layout = new XYLayout();
			figure.setLayoutManager(layout);
			figure.setImage(IconFactory.getImageDescriptor("ui/treePic.png").createImage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return figure;
	}
	public void refreshVisuals(){}
	/**
	 * 
	 * 2014-12-13
	 * @tianming
	 */
	@Override
	public void reLocationChildren() {
		try {
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
