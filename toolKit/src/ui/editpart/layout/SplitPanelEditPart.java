package ui.editpart.layout;
 
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.UIAbstractModel;
import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;

public class SplitPanelEditPart extends ContainerPart{
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
			TopEditPart top = null;
			CenterEditPart center = null;
			for(int i = 0;i<children.size();i++){
				if(children.get(i) instanceof TopEditPart){
					top = (TopEditPart)children.get(i);
				}else if(children.get(i) instanceof CenterEditPart){
					center = (CenterEditPart)children.get(i);
				}
			}
			Rectangle topRect = top.getFigure().getBounds();
			Rectangle centerRect = center.getFigure().getBounds();
			
			topRect.x = panel.x ;
			topRect.y = panel.y ;
			topRect.width = panel.width ;
			
			centerRect.x = panel.x ;
			centerRect.y = panel.y +topRect.height+3;
			centerRect.width = panel.width ;
			centerRect.height = panel.height-topRect.height - 3;
			
			if(top instanceof ContainerPart){
				((ContainerPart)top).reLocationChildren();
			}
			if(center instanceof ContainerPart){
				((ContainerPart)center).reLocationChildren();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
