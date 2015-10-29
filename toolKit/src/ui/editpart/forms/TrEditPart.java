package ui.editpart.forms;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.UIAbstractModel;
import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;
import ui.figure.TrFigure;
import util.GloableParam;

public class TrEditPart extends ContainerPart {

	@Override
	protected IFigure createFigure() {
		try {
			UIAbstractModel model = (UIAbstractModel)this.getModel();
			TableEditPart pEditPart = (TableEditPart)this.getParent();
			Rectangle parent = pEditPart.getFigure().getBounds();
			Rectangle rect = new Rectangle(parent.x,parent.y,parent.width,Integer.parseInt(model.getHeight()));
			return new TrFigure(rect);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 *  重新定位子元素的位置大小等信息 
	 * 2014-12-13
	 * @tianming
	 */
	@SuppressWarnings("unchecked")
	public void reLocationChildren() {
		try {
			List<UIEditPartWithListener> children = this.getChildren();
			Rectangle container = this.getContentPane().getBounds();
			int width = container.width/GloableParam.columnNum;
			for(int i = 0;i<children.size();i++){
				UIEditPartWithListener childPart = (UIEditPartWithListener)children.get(i);
				Rectangle rect = childPart.getFigure().getBounds();
				rect.x = container.x + i * width;
				rect.width = width;
				rect.y = container.y;
				rect.height = container.height;
				childPart.getFigure().setBounds(rect);
				childPart.getFigure().repaint();
				if(childPart instanceof ContainerPart){
					((ContainerPart)childPart).reLocationChildren();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
