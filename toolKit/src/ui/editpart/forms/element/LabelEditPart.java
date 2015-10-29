package ui.editpart.forms.element;
 

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.editpart.forms.FormElementEditPart;
import ui.model.forms.element.LabelModel;
import util.Log;

public class LabelEditPart extends FormElementEditPart {
	private Label label = null;
	@Override
	protected IFigure createFigure() {
		try{
			LabelModel model = (LabelModel)this.getModel();
			label = new Label();
			label.setText(model.val("label"));
			label.setLabelAlignment(PositionConstants.LEFT);
			label.setBounds(new Rectangle(0,0,Integer.parseInt(model.getWidth()),Integer.parseInt(model.getHeight())));
			return label;
		}catch(Exception e){
			Log.write("出现异常", e);
		}
		return null;
	}
	protected void refreshVisuals() {
		try{
			LabelModel model = (LabelModel)this.getModel();
			label.setText("  "+model.val("label"));
		}catch(Exception e){
			Log.write("", e);
		}
		super.refreshVisuals();
	}
}
