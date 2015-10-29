package ui.editpart;
import java.util.List;

import images.IconFactory;

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
import util.Log;

public class PanelEditPart extends ContainerPart{
	private Figure figure = null;
	private Figure panelContent = null;
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
			
			labelContainer = new ImageFigure();
			Rectangle title = new Rectangle(rect.x,rect.y-2,rect.width+10,29);
			//设置标题
			name = new Label(model.val("title"));
			name.setTextAlignment(PositionConstants.LEFT);
			name.setLabelAlignment(PositionConstants.LEFT);
			name.setBounds(title);
			labelContainer.setImage(IconFactory.getImageDescriptor("ui/layout_title.png").createImage());
			labelContainer.setBounds(title); 
			labelContainer.add(name);
			figure.add(labelContainer);
			
			panelContent = new Figure();
			XYLayout layout2 = new XYLayout();
			panelContent.setLayoutManager(layout2);
			Rectangle panel = new Rectangle(rect.x,rect.y+27,rect.width,rect.height-27);
			panelContent.setBounds(panel);
			figure.add(panelContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return figure;
	}
	
	@Override
	public IFigure getContentPane() {
		// TODO Auto-generated method stub
		return panelContent;
	}

	/**
	 * 
	 * 2014-12-13
	 * @tianming
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void reLocationChildren() {
		reSetInner();
		List<UIEditPartWithListener> children = this.getChildren();
		Rectangle panel = this.getFigure().getBounds();
		for(int i = 0;i<children.size();i++){
			Rectangle rect = children.get(i).getFigure().getBounds();
			rect.x = panel.x ;
			rect.y = panel.y + 25;
			rect.width = panel.width;
			rect.height = panel.height-25;
			children.get(i).getFigure().setBounds(rect);
			if(children.get(i) instanceof ContainerPart){
				((ContainerPart)children.get(i)).reLocationChildren();
			}
		}
	}
	@Override
	protected void refreshVisuals() {
		try{
			UIAbstractModel model = (UIAbstractModel)this.getModel();
			name.setText("  "+model.val("title"));
		}catch(Exception e){
			Log.write("", e);
		}
		super.refreshVisuals();
	}
	public void reSetInner(){
		Rectangle container = figure.getBounds().getCopy();
		Rectangle labelRect = labelContainer.getBounds();
		labelRect.x = container.x;
		labelRect.y = container.y-2;
		labelRect.width = container.width;
		name.setBounds(labelRect);
		labelContainer.setBounds(labelRect);
		Rectangle panel = panelContent.getBounds();
		panel.x = container.x;
		panel.y = container.y +27;
		panel.width = container.width;
		panel.height = container.height -27;
		panelContent.setBounds(panel);
	}
}
