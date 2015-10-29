package business.editpart;

import images.IconFactory;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public class NodeFigure extends Figure implements HandleBounds{
	public ImageFigure icon = null;
	public ImageFigure error = null;
	public Label label = null;
	public NodeFigure(String text,String iconName,Rectangle rect){
		icon = new ImageFigure();
		error = new ImageFigure();
		label = new Label();
		label.setText(text);
		error.setImage(IconFactory.getImageDescriptor("business/error.gif").createImage());
		icon.setImage(IconFactory.getImageDescriptor("business/"+iconName).createImage());
		int width = 0;     
		GC gc = new GC(Display.getCurrent()); 
		if(text!=null){
			for(int i = 0; i < text.length(); i++) {     
				char c = text.charAt(i);     
				width += gc.getAdvanceWidth(c);     
			} 
		}
		if(width<32){
			width = 32;
		}
		//width +=13;
		width = 52;
		//设置节点的坐标
		rect.width = width;
		rect.height = 52;
		rect.x = rect.x - 16;
		rect.y = rect.y - 16;
		this.setBounds(rect);
		//设置文本的坐标
		Rectangle labelRect = new Rectangle();
		labelRect.x = rect.x;
		labelRect.y = rect.y +34;
		labelRect.width = width;
		labelRect.height = 20;
		label.setBounds(labelRect);
		this.add(label);
		//设置图标的位置
		Rectangle iconRect = new Rectangle();
		if(width>32){
			iconRect.x = rect.x + (width-32)/2;
		}else{
			iconRect.x = rect.x;
		}
		iconRect.y = rect.y;
		iconRect.width = 32;
		iconRect.height = 32;
		icon.setBounds(iconRect);
		Rectangle errorRect = iconRect.getCopy();
		errorRect.y = errorRect.y+10;
		error.setBounds(errorRect);
		this.add(icon);
		icon.add(error);
		error.setVisible(false);
		
		//this.setBorder(new LineBorder(ColorConstants.gray));
	}
	@Override
	public Rectangle getHandleBounds() {
		// TODO Auto-generated method stub
		return icon.getBounds();
	}
	 
}
