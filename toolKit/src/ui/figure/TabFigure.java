package ui.figure;
import images.IconFactory;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import util.ColorValue;

/**
 * 
 * @author dhcc
 *
 */
public class TabFigure extends Figure {

	public ImageFigure labelContainer = new ImageFigure();
	public Figure itemContainer = new Figure();
	public int title_height = 30;//标题高度
	public TabFigure(Rectangle rect) {
		this.setBounds(rect);
		XYLayout layout = new XYLayout();
		setBorder(new LineBorder(ColorValue.getColor()));
		setLayoutManager(layout);
		Rectangle title = new Rectangle(rect.x,rect.y+2,rect.width+10,title_height);
		
		labelContainer.setImage(IconFactory.getImageDescriptor("ui/tab_title.png").createImage());
		labelContainer.setBounds(title); 
		add(labelContainer);
		
		itemContainer.setBounds(new Rectangle(rect.x+1,rect.y+2+title_height,rect.width+10,rect.height));
		//itemContainer.setBorder(new LineBorder(ColorConstants.red,1,2));
		add(itemContainer);
		
		
		this.setOpaque(false);
	}
 
}
