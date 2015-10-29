package ui.editor.menuaction;

import images.IconFactory;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import ui.UIAbstractModel;
import ui.editor.DefSelectionAction;
import util.PropertyReader;
 

public class OpenUrlAction extends DefSelectionAction {
	public IFile file = null;
	public OpenUrlAction(IWorkbenchPart part,IFile file) {
		super(part);
		this.file = file;
		this.setId("openAction");
	    this.setText("打开关联文件");
	    this.setImageDescriptor(IconFactory.getImageDescriptor("ui/node.gif"));
	}
	public void run() {
		UIAbstractModel model = null;
		IWorkbenchPage page =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		PropertyReader reader = new PropertyReader();
		String resourcePath = reader.getPropertyValue("resourcePath");
		try {
			model = getModel();
			if(model!=null){
				String url = model.val("url");
				if(url!=null&&!"".equals(url)){
					if(url.endsWith(".do")){
						url = url.replace(".do", ".ctl");	
					}else if(url.endsWith(".jsp")){
						url = url.replace(".jsp", ".ui");	
					}
					url  = resourcePath+url;
					IFile source = file.getProject().getFile(url);
					IDE.openEditor(page, source);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected boolean calculateEnabled() {
		UIAbstractModel model = null;
		PropertyReader reader = new PropertyReader();
		String resourcePath = reader.getPropertyValue("resourcePath");
		try {
			model = getModel();
			if(model!=null){
				String url = model.val("url");
				if(url!=null&&!"".equals(url)){
					if(url.endsWith(".do")){
						url = url.replace(".do", ".ctl");	
					}else if(url.endsWith(".jsp")){
						url = url.replace(".jsp", ".ui");	
					}
					File actionFile = new File(file.getProject().getLocation().toFile().getAbsoluteFile()+resourcePath+url);
					return actionFile.exists();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
 
}
