package ui.editpart.forms;
 
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.UIAbstractModel;
import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;
import ui.figure.HiddenPanelFigure;

public class HiddenPanelEditPart extends ContainerPart{
	private HiddenPanelFigure figure = null;
	protected IFigure createFigure() {
		try {
			UIAbstractModel model = (UIAbstractModel)this.getModel();
			Rectangle rect = new Rectangle( 20, 20,Integer.parseInt(model.getWidth()),Integer.parseInt(model.getHeight()));
			figure = new HiddenPanelFigure(rect);
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
			int row = 0;
			int count = 1;
			for(int i = 0;i<children.size();i++){
				Rectangle rect = children.get(i).getFigure().getBounds();
				rect.x = panel.x +10 +row*rect.width+4*row;
				rect.y = panel.y +30*count;
				if((rect.x + rect.width*2)>(panel.x + panel.width)){
					row = 0;
					count++;
				}else{
					row ++;
				}
				children.get(i).getFigure().setBounds(rect);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void reSetInner(){
		ImageFigure label = figure.labelContainer;
		Rectangle container = figure.getBounds().getCopy();
		Rectangle labelRect = label.getBounds();
		labelRect.x = container.x;
		labelRect.y = container.y-2;
		labelRect.width = container.width;
		figure.name.setBounds(labelRect);
		label.setBounds(labelRect);
	}
}
