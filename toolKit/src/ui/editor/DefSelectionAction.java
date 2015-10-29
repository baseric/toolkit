package ui.editor;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import ui.UIAbstractModel;
import ui.UIEditPartWithListener;

/**
 * 编辑器右键菜单抽象类
 * @author Administrator
 *
 */
public abstract class DefSelectionAction extends SelectionAction {

	public DefSelectionAction(IWorkbenchPart part) {
		super(part);
	}
	public UIAbstractModel getModel(){
		try {
			List<?> objects = getSelectedObjects();
			    if (objects.isEmpty())
			        return null;
			    
			    for (Iterator<?> iter = objects.iterator(); iter.hasNext();) {
			        Object obj = iter.next();
			        UIAbstractModel model = (UIAbstractModel)((UIEditPartWithListener)obj).getModel();
			        return model;
			    }
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}
}
