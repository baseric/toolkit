package ui.editpart.forms;

import images.IconFactory;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;
import ui.model.forms.TitleModel;

public class TitleEditPart extends ContainerPart {
	public Label name = null;
	@Override
	protected IFigure createFigure() {
		try {
			TitleModel model = (TitleModel)this.getModel();
			name = new Label(model.val("title"));
			Rectangle parent = ((UIEditPartWithListener)this.getParent()).getFigure().getBounds();
			ImageFigure toolbar = new ImageFigure();
			Rectangle title = new Rectangle(0,0,parent.width+10,Integer.parseInt(model.getHeight()));
			//设置标题
			name.setTextAlignment(PositionConstants.CENTER);
			name.setLabelAlignment(PositionConstants.CENTER);
			name.setBounds(title);
			toolbar.setImage(IconFactory.getImageDescriptor("ui/title.png").createImage());
			toolbar.setBounds(title); 
			toolbar.add(name);
			return toolbar;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void reLocationChildren() {
		TitleModel model = (TitleModel)this.getModel();
		Rectangle rect = this.getFigure().getBounds();
		name.setBounds(rect);
		name.setText(model.val("title"));
	}
}
