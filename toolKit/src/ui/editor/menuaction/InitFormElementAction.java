package ui.editor.menuaction;

import images.IconFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import ui.UIAbstractModel;
import ui.dialog.formdialog.InitPageWithDic;
import ui.editor.DefSelectionAction;
import ui.model.UIContentsModel;
import ui.model.abstractModel.ContainerModel;
import ui.model.forms.TableModel;
import ui.model.liger.FormModel;
 

public class InitFormElementAction extends DefSelectionAction {
	public IFile file = null;
	public UIContentsModel contents = null;
	public InitFormElementAction(IWorkbenchPart part,IFile file) {
		super(part);
		this.file = file;
		this.setId("initGridColumn");
	    this.setText("初始化表单项");
	    this.setImageDescriptor(IconFactory.getImageDescriptor("ui/settings.gif"));
	}
	public void run() {
		try {
			UIAbstractModel model = this.getModel();
			if(model instanceof TableModel||model instanceof FormModel){
				InitPageWithDic dialog = new InitPageWithDic(Display.getCurrent().getActiveShell(),(ContainerModel)model);
				dialog.setHelpAvailable(false);
				dialog.open();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
	protected boolean calculateEnabled() {
	   return getModel()!=null&&(getModel() instanceof TableModel||getModel() instanceof FormModel); 
	}
}
