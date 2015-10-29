package ui.editpart.forms;
 
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.editpart.abstractPart.ContainerPart;
import ui.model.forms.IframeModel;
import util.Log;
public class IframeEditPart extends ContainerPart{
 
	protected IFigure createFigure() {
		Label figure = null;
		try {
			IframeModel model = (IframeModel)this.getModel();
			Rectangle rect = new Rectangle( 20, 20,Integer.parseInt(model.getWidth()),Integer.parseInt(model.getHeight()));
			figure = new Label();
			figure.setBorder(new LineBorder(ColorConstants.red));
			figure.setBounds(rect);
			figure.setText("iframe = src://"+model.val("src"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return figure;
	}
	@Override
	protected void refreshVisuals() {
		try{
			IframeModel model = (IframeModel)this.getModel();
			Label figure = (Label)this.getFigure();
			figure.setText("iframe = src://"+model.val("src"));
		}catch(Exception e){
			Log.write("", e);
		}
		super.refreshVisuals();
	}
	/**
	 * 重新定位子元素的位置信息
	 * 
	 * 2014-12-13
	 * @tianming
	 */
	@Override
	public void reLocationChildren() {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
