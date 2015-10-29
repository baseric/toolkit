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

public class TopEditPart extends ContainerPart{
	private Figure figure = null;
	private Label name = null;
	private ImageFigure labelContainer = null;
	protected IFigure createFigure() {
		try {
			UIAbstractModel model = (UIAbstractModel)this.getModel();
			Rectangle rect = new Rectangle( 20, 20,0,Integer.parseInt(model.val("height")));
			figure = new Figure();
			figure.setBounds(rect);
			XYLayout layout = new XYLayout();
			figure.setBorder(new LineBorder(ColorValue.getColor()));
			figure.setLayoutManager(layout);
			
			//设置标题
			Rectangle title = new Rectangle(rect.x,rect.y-2,rect.width+10,27);
			name = new Label(model.val("title"));
			name.setTextAlignment(PositionConstants.LEFT);
			name.setLabelAlignment(PositionConstants.LEFT);
			name.setBounds(title);
			labelContainer = new ImageFigure();
			labelContainer.setImage(IconFactory.getImageDescriptor("ui/layout_title.png").createImage());
			labelContainer.add(name);
			labelContainer.setBounds(title);
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
			String titleTxt = ((UIAbstractModel)this.getModel()).val("title");
			name.setText("    "+titleTxt);
			Rectangle title = labelContainer.getBounds();
			title.x = panel.x;
			title.y = panel.y;
			title.width = panel.width;
			title.height = 25;
			
			name.setBounds(title);
			int offsetY = title.height;
			if(titleTxt==null||titleTxt.length()==0){
				labelContainer.setVisible(false);
				offsetY = 0;
			}else{
				labelContainer.setVisible(true);
			}
			
			
			for(int i = 0;i<children.size();i++){
				Rectangle rect = children.get(i).getFigure().getBounds();
				rect.x = panel.x ;
				rect.y = panel.y + offsetY +this.getLocationY(children.get(i));
				rect.width = panel.width ;
				rect.height = panel.height - offsetY;
				UIAbstractModel model =(UIAbstractModel)children.get(i).getModel();
				model.setHeight(String.valueOf(rect.height));
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
	}
}
