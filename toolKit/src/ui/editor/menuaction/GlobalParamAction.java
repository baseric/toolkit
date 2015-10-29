package ui.editor.menuaction;

import images.IconFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import ui.UIAbstractModel;
import ui.editor.DefSelectionAction;
import ui.model.UIContentsModel;
import ui.model.grid.GridModel;
import ui.model.liger.FormModel;
 

public class GlobalParamAction extends DefSelectionAction {
	public IFile file = null;
	public UIContentsModel contents = null;
	public GlobalParamAction(IWorkbenchPart part,IFile file,UIContentsModel contents) {
		super(part);
		this.file = file;
		this.setId("globalParam");
	    this.setText("配置全局变量");
	    this.setImageDescriptor(IconFactory.getImageDescriptor("ui/node.gif"));
	    this.contents = contents;
	}
	public void run() {
		UIAbstractModel model = null;
		IWorkbenchPage page =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			model = getModel();
			if(model!=null){
				if(model instanceof GridModel){
					GridModel grid = (GridModel)model;
					String url = grid.val("url");
					IFile source = file.getProject().getFile(url.replace(".do", ".action"));
					IDE.openEditor(page, source);
				}else if(model instanceof FormModel){
					FormModel form = (FormModel)model;
					String url = form.val("action");
					IFile source = file.getProject().getFile(url.replace(".do", ".action"));
					IDE.openEditor(page, source);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected boolean calculateEnabled() {
		return true;
	}
 
}
