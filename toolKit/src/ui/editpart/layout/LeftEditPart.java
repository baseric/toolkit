package ui.editpart.layout;
 
import images.IconFactory;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.UIAbstractModel;
import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;
import util.ColorValue;

public class LeftEditPart extends ContainerPart{
	private Figure figure = null;
	private Label name = null;
	private ImageFigure labelContainer = null;
	protected IFigure createFigure() {
		try {
			UIAbstractModel model = (UIAbstractModel)this.getModel();
			Rectangle rect = new Rectangle( 20, 20,Integer.parseInt(model.getWidth()),Integer.parseInt(model.getHeight()));
			figure = new Figure();
			figure.setBounds(rect);
			XYLayout layout = new XYLayout();
			figure.setBorder(new LineBorder(ColorValue.getColor()));
			figure.setLayoutManager(layout);
			
			name = new Label("   "+model.val("title"));
			labelContainer = new ImageFigure();
			Rectangle title = new Rectangle(rect.x,rect.y-2,rect.width+10,29);
			//设置标题
			name.setTextAlignment(PositionConstants.LEFT);
			name.setLabelAlignment(PositionConstants.LEFT);
			name.setBounds(title);
			labelContainer.setImage(IconFactory.getImageDescriptor("ui/layout_title.png").createImage());
			labelContainer.setBounds(title); 
			labelContainer.add(name);
			figure.add(labelContainer);
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
			for(int i = 0;i<children.size();i++){
				Rectangle rect = children.get(i).getFigure().getBounds();
				rect.x = panel.x +1;
				rect.y = panel.y +this.getLocationY(children.get(i));
				rect.width = panel.width-2;
				rect.height = panel.height-2;
				UIAbstractModel model =(UIAbstractModel)children.get(i).getModel();
				model.setWidth(String.valueOf(rect.width));
				children.get(i).getFigure().setBounds(rect);
				if(children.get(i) instanceof ContainerPart){
					((ContainerPart)children.get(i)).reLocationChildren();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void reSetInner(){
		UIAbstractModel model = (UIAbstractModel)this.getModel();
		Rectangle container = figure.getBounds().getCopy();
		Rectangle labelRect = labelContainer.getBounds();
		labelRect.x = container.x;
		labelRect.y = container.y-2;
		labelRect.width = container.width;
		name.setBounds(labelRect);
		String title = model.val("title");
		if(title==null||title.length()==0){
			labelContainer.setVisible(false);
		}else{
			labelContainer.setVisible(true);
			name.setText("   "+model.val("title"));
			labelContainer.setBounds(labelRect);
		}
	}
}
