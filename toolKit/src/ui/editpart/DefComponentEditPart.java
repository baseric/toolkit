package ui.editpart;
 
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;
import util.Log;
public class DefComponentEditPart extends ContainerPart{
	private Figure panel = null;
	protected IFigure createFigure() {
		try {
			Rectangle rect = new Rectangle( 20, 20,500,200);
			panel = new Figure();
			//figure.setBorder(new LineBorder(ColorConstants.red));
			panel.setBounds(rect);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return panel;
	}
	@Override
	protected void refreshVisuals() {
		try{
		}catch(Exception e){
			Log.write("", e);
		}
		super.refreshVisuals();
	}
	/**
	 * 重新定位子元素的位置信息
	 * @tianming
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void reLocationChildren() {
		try {
			List children = this.getChildren();
			Rectangle panelRect = panel.getBounds();
			for(int i = 0;i<children.size();i++){
				UIEditPartWithListener childPart = (UIEditPartWithListener)children.get(i);
				Rectangle rect = childPart.getFigure().getBounds();
				rect.x = panelRect.x ;
				rect.y = panelRect.y ;
				rect.width = panelRect.width;
				rect.height = panelRect.height;
				childPart.getFigure().setBounds(rect);
				if(childPart instanceof ContainerPart){
					((ContainerPart) childPart).reLocationChildren();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
