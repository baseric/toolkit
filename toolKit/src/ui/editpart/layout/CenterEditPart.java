package ui.editpart.layout;
 
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.UIAbstractModel;
import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;
import util.ColorValue;

public class CenterEditPart extends ContainerPart{
	private Figure figure = null;
	protected IFigure createFigure() {
		
		try {
			UIAbstractModel model = (UIAbstractModel)this.getModel();
			Rectangle rect = new Rectangle( 20, 20,Integer.parseInt(model.getWidth()),Integer.parseInt(model.getHeight()));
			figure = new Figure();
			figure.setBounds(rect);
			XYLayout layout = new XYLayout();
			figure.setBorder(new LineBorder(ColorValue.getColor()));
			figure.setLayoutManager(layout);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return figure;
	}
	/**
	 * 
	 * 2014-12-13
	 * @tianming
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void reLocationChildren() {
		try {
			this.reSetInner(); 
			List<UIEditPartWithListener> children = this.getChildren();
			Rectangle panel = this.getFigure().getBounds();
			for(int i = 0;i<children.size();i++){
				Rectangle rect = children.get(i).getFigure().getBounds();
				rect.x = panel.x ;
				rect.y = panel.y ;
				rect.width = panel.width;
				rect.height = panel.height;
				children.get(i).getFigure().setBounds(rect);
				if(children.get(i) instanceof ContainerPart){
					((ContainerPart)children.get(i)).reLocationChildren();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
