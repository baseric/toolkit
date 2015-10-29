package ui.editpart.grid;
 
import images.IconFactory;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.editpart.abstractPart.ContainerPart;

public class BottomBarEditPart extends ContainerPart{
	public ImageFigure figure = null;
	public Label bottom_left = null;
	protected IFigure createFigure() {
		Rectangle rect = new Rectangle(23,30,0,0);
		figure = new ImageFigure();
		figure.setBounds(rect);
		figure.setImage(IconFactory.getImageDescriptor("ui/grid/bottom_new.png").createImage());
		XYLayout layout = new XYLayout();
		figure.setLayoutManager(layout);
		bottom_left = new Label();
		bottom_left.setBounds(new Rectangle(rect.x,rect.y+rect.height-27,318,23));
		bottom_left.setIcon(IconFactory.getImageDescriptor("ui/grid/bottom_left.png").createImage());
		figure.add(bottom_left);
		return figure;
	}
	/**
	 *功能描述： 重新设置子元素的位置坐标
	 * @author  tianming
	 * @2014-12-8
	 */
	public void reLocationChildren(){
		try {
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void reSetInner() {
		Rectangle figRect = figure.getBounds();
		Rectangle left = bottom_left.getBounds();
		left.x = figRect.x+2;
		left.y = figRect.y+figRect.height-27;
		bottom_left.setBounds(left);
	}
	
}
