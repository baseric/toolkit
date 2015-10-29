package ui.editor.menuaction;

import images.IconFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IWorkbenchPart;

import ui.editor.DefSelectionAction;
import ui.model.UIContentsModel;
import ui.util.UIGenerateCode;
 

public class CreateCodeAction extends DefSelectionAction {
	public IFile file = null;
	public UIContentsModel contents = null;
	public CreateCodeAction(IWorkbenchPart part,IFile file,UIContentsModel contents) {
		super(part);
		this.file = file;
		this.contents = contents;
		this.setId("createCode");
	    this.setText("生成代码");
	    this.setImageDescriptor(IconFactory.getImageDescriptor("ui/node.gif"));
	}
	public void run() {
		UIGenerateCode code = new UIGenerateCode();
		try {
			code.createJSP(file.getLocation().toFile(), contents, file.getProject());
			file.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected boolean calculateEnabled() {
	   return true;
	}

}
