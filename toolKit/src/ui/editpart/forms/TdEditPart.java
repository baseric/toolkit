package ui.editpart.forms;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.editpart.abstractPart.ContainerPart;
import ui.figure.TdFigure;
import util.GloableParam;
import util.Log;

public class TdEditPart extends ContainerPart {

	@Override
	protected IFigure createFigure() {
		try{
			TrEditPart pEditPart = (TrEditPart)this.getParent();
			Rectangle pRect = pEditPart.getFigure().getBounds();
			int index = pEditPart.getModelIndex(this);
			int width = pRect.width/GloableParam.columnNum;
			Rectangle rect = new Rectangle(pRect.x+index*width,pRect.y,width,pRect.height);
			return new TdFigure(rect);
		}catch(Exception e){
			Log.write("出现异常", e);
		}
		return null;
	}
}
