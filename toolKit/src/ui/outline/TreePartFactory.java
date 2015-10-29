package ui.outline;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import ui.model.UIContentsModel;

public class TreePartFactory implements EditPartFactory{
	public List<Map<String,String>> toolsList = null;
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof UIContentsModel) {
			return new ContentsTreeEditPart(model);
		} 
		return new FormTreeEditPart(model,toolsList);
		 
	}
}
