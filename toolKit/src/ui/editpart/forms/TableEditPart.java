package ui.editpart.forms;
 
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;
import ui.model.forms.TableModel;
import util.ColorValue;

public class TableEditPart extends ContainerPart{
	private Figure figure = null;
	protected IFigure createFigure() {
		
		try {
			TableModel model = (TableModel)this.getModel();
			Rectangle rect = new Rectangle( 20, 20,
											Integer.parseInt(model.getWidth()),
											Integer.parseInt(model.getHeight())
										   );
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
			List<UIEditPartWithListener> children = this.getChildren();
			Rectangle container = this.getContentPane().getBounds();
			Rectangle form = this.getFigure().getBounds();
			int rowcount = 0;
			int buttoncount = 0;
			for(int i = 0;i<children.size();i++){
				UIEditPartWithListener childPart = (UIEditPartWithListener)children.get(i);
				if(childPart instanceof TrEditPart||childPart instanceof TitleEditPart){
					Rectangle rect = childPart.getFigure().getBounds();
					rect.y = container.y+rowcount*28 -1;
					rect.width = form.width;
					rect.height = 27;
					rect.x = container.x;
					childPart.getFigure().setBounds(rect);
					childPart.getFigure().repaint();
					if(childPart instanceof ContainerPart){
						((ContainerPart)childPart).reLocationChildren();
					}
					rowcount ++;
				} 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
