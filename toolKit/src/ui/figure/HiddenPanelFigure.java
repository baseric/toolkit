package ui.figure;
import images.IconFactory;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import util.ColorValue;

/**
 * 
 * @author dhcc
 *
 */
public class HiddenPanelFigure extends Figure {

	public Label name = new Label("隐藏域设置区[双击隐藏内容区]");
	public ImageFigure labelContainer = new ImageFigure();
	public int title_height = 29;//标题高度
	public HiddenPanelFigure(Rectangle rect) {
		this.setBounds(rect);
		XYLayout layout = new XYLayout();
		setBorder(new LineBorder(ColorValue.getColor()));
		setLayoutManager(layout);
		Rectangle title = new Rectangle(rect.x,rect.y-2,rect.width+10,title_height);
		//设置标题
		name.setTextAlignment(PositionConstants.LEFT);
		name.setLabelAlignment(PositionConstants.LEFT);
		name.setBounds(title);
		labelContainer.setImage(IconFactory.getImageDescriptor("ui/title.png").createImage());
		labelContainer.setBounds(title); 
		labelContainer.add(name);
		add(labelContainer);
		this.setOpaque(false);
	}
 

	public void setName(String name) {
		this.name.setText(name);
	}
}
