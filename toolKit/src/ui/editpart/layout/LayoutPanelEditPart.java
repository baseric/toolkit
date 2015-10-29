package ui.editpart.layout;
 
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.UIAbstractModel;
import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;

public class LayoutPanelEditPart extends ContainerPart{
	private Figure figure = null;
	protected IFigure createFigure() {
		try {
			UIAbstractModel model = (UIAbstractModel)this.getModel();
			Rectangle rect = new Rectangle( 20, 20,Integer.parseInt(model.getWidth()),Integer.parseInt(model.getHeight()));
			figure = new Figure();
			figure.setBounds(rect);
			XYLayout layout = new XYLayout();
			figure.setBorder(null);
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
			LeftEditPart left = null;
			CenterEditPart center = null;
			TopEditPart top = null;
			for(int i = 0;i<children.size();i++){
				if(children.get(i) instanceof LeftEditPart){
					left = (LeftEditPart)children.get(i);
				}else if(children.get(i) instanceof CenterEditPart){
					center = (CenterEditPart)children.get(i);
				}else{
					top = (TopEditPart)children.get(i);
				}
			}
			Rectangle centerRect = center.getFigure().getBounds();
			//top
			int topOffset = 1;
			if(top!=null){
				Rectangle topRect = top.getFigure().getBounds();
				topRect.x = panel.x ;
				topRect.y = panel.y ;
				topRect.width = panel.width  ;
				topOffset = topRect.height;
			}
			//left
			int leftOffset = 0;
			if(left!=null){
				Rectangle leftRect = left.getFigure().getBounds();
				leftRect.x = panel.x+1;
				leftRect.y = panel.y+1 + topOffset;
				leftRect.height = panel.height-2 -topOffset;
				leftOffset = leftRect.width + 3 ;
			}
			//center
			centerRect.x = panel.x + leftOffset;
			centerRect.y = panel.y+1 + topOffset;
			centerRect.width = panel.width - leftOffset;
			centerRect.height = panel.height-2 - topOffset;
			
			if(left instanceof ContainerPart){
				((ContainerPart)left).reLocationChildren();
			}
			if(center instanceof ContainerPart){
				((ContainerPart)center).reLocationChildren();
			}
			if(top instanceof ContainerPart){
				((ContainerPart)top).reLocationChildren();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
