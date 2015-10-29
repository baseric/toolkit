package ui.editor.menuaction;

import images.IconFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import ui.UIAbstractModel;
import ui.dialog.EditColumnsNew;
import ui.editor.DefSelectionAction;
import ui.model.UIContentsModel;
import ui.model.grid.GridModel;
 

public class InitGridColumnAction extends DefSelectionAction {
	public IFile file = null;
	public UIContentsModel contents = null;
	public InitGridColumnAction(IWorkbenchPart part,IFile file) {
		super(part);
		this.file = file;
		this.setId("initFormElement");
	    this.setText("初始化表格列");
	    this.setImageDescriptor(IconFactory.getImageDescriptor("ui/settings.gif"));
	}
	public void run() {
		try {
			UIAbstractModel model = this.getModel();
			if(model instanceof GridModel){
				EditColumnsNew dialog = new EditColumnsNew(Display.getCurrent().getActiveShell(),(GridModel)model);
				dialog.setHelpAvailable(false);
				dialog.open();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
	protected boolean calculateEnabled() {
	   return getModel()!=null&&getModel() instanceof GridModel; 
	}
}
