package business.editor.menuaction;

import images.IconFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import util.PropertyReader;

public class OpenSourceAction extends SelectionAction {
	public IFile file = null;
	//public ContentsModel contents = null;
	public OpenSourceAction(IWorkbenchPart part,IFile file) {
		super(part);
		this.file = file;
		//this.contents = contents;
		this.setId("openSource");
	    this.setText("查看源码");
	    this.setImageDescriptor(IconFactory.getImageDescriptor("business/node.gif"));
	}
	public void run() {
	    IWorkbenchPage page =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	    IProject project = file.getProject();
	    String fileName = file.getName();
	    PropertyReader reader = new PropertyReader();
	    String basePath =  reader.getPropertyValue("resourcePath");
	    String basePath2 =  reader.getPropertyValue("javaPath");
	    String basePath3 =  reader.getPropertyValue("jspPath");
	    try {
			String filePath = file.getLocation().toFile().getAbsolutePath();//配置文件路径
			String projectPath = project.getLocation().toFile().getAbsolutePath();//工程路径
			filePath = filePath.replace((projectPath+basePath).replace("/", "\\"), "").replace("\\"+fileName,"");
			//获取类名
			if(fileName.endsWith(".ctl")||fileName.endsWith(".logic")){
				fileName = fileName.replace(".ctl", ".java").replace(".logic",".java").replace(".ui",".jsp");
		    	IFile source = project.getFile(basePath2+filePath+"/"+fileName);
		    	IDE.openEditor(page,source,true);
			}else{
				fileName = fileName.replace(".ui",".jsp");
		    	IFile source = project.getFile(basePath3+filePath+"/"+fileName);
		    	IDE.openEditor(page,source,true);
			}
	    } catch (PartInitException e) {
		}

	}
	protected boolean calculateEnabled() {
	   return true;
	}

}
