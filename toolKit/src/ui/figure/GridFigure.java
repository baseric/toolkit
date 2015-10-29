package ui.figure;

import images.IconFactory;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import util.ColorValue;

/**
 * 
 * @author dhcc
 *
 */
public class GridFigure extends Figure {

	public ImageFigure head = new ImageFigure();
	public ImageFigure bottom = new ImageFigure();
	public Label bottom_left = new Label();
	public Figure containerFigure = new Figure();
	public int title_height = 58;//标题高度
	public GridFigure(Rectangle rect) {
		try {
			this.setBounds(rect);
			XYLayout layout = new XYLayout();
			setBorder(new LineBorder(ColorValue.getColor()));
			setLayoutManager(layout);
			
			int width = rect.width;
			head.setBounds(new Rectangle(rect.x,rect.y-4,width+10,title_height));
			head.setImage(IconFactory.getImageDescriptor("ui/top.png").createImage());
			
			bottom.setBounds(new Rectangle(rect.x-1,rect.y+rect.height-28,width+10,29));
			bottom.setImage(IconFactory.getImageDescriptor("ui/grid/bottom.png").createImage());
			//bottom.setAlignment(PositionConstants.LEFT);
			bottom.setLayoutManager(new XYLayout());
			bottom_left.setBounds(new Rectangle(rect.x,rect.y+rect.height-27,310,29));
			bottom_left.setIcon(IconFactory.getImageDescriptor("ui/grid/bottom_left.png").createImage());
			bottom.add(bottom_left);
			add(head);
			add(bottom);

			this.setOpaque(false);
			this.bindEventListener();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Figure getContainerFigure() {
		return head;
	}
	public void bindEventListener(){
		
		final ImageFigure head = this.head;
		final ImageFigure bottom = this.bottom;
		final Figure figure = this;
		this.addFigureListener(new FigureListener() {
			@Override
			public void figureMoved(IFigure arg0) {
				try{
					 Rectangle bounds = figure.getBounds();
					 
					 Rectangle oldName = head.getBounds();
					 oldName.width = bounds.width+7;
					 figure.getLayoutManager().setConstraint(head,new Rectangle(0,-6,bounds.width+7,oldName.height));
					 
					 Rectangle oldBottom = bottom.getBounds();
					 oldBottom.width = bounds.width+7;
					 figure.getLayoutManager().setConstraint(bottom,new Rectangle(0,bounds.height-29,bounds.width+7,oldBottom.height));
				}catch(Exception e){
					 e.printStackTrace();
				}
			}
		});
	}
}
