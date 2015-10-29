package business.editor.menuaction;

import images.IconFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import util.GenerateCode;
import business.model.ContentsModel;
import exception.ComplierException;
 

public class CreateCodeAction extends SelectionAction {
	public IFile file = null;
	public ContentsModel contents = null;
	public String name = "";
	public CreateCodeAction(IWorkbenchPart part,IFile file,ContentsModel contents,String name) {
		super(part);
		this.file = file;
		this.name = name;
		this.contents = contents;
		this.setId("createCode");
	    this.setText("生成代码");
	    this.setImageDescriptor(IconFactory.getImageDescriptor("business/node.gif"));
	}
	public void run() {
		try {
			GenerateCode code = new GenerateCode();
			code.createAction(contents, file.getLocation().toFile(),file.getProject(),name);
		} catch (Exception e) {
			if(!(e instanceof ComplierException)){
				e.printStackTrace();
			}
		}
	}
	protected boolean calculateEnabled() {
	   return true;
	}

}
