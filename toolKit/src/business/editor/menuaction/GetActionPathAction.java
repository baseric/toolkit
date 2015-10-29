package business.editor.menuaction;

import images.IconFactory;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import util.PropertyReader;
 

public class GetActionPathAction extends SelectionAction {
	public IFile file = null;
	public GetActionPathAction(IWorkbenchPart part,IFile file) {
		super(part);
		this.file = file;
		this.setId("getActionPath");
		String fileName = file.getName();
		if(fileName.endsWith("ctl")){
			this.setText("获取Controller映射路径");
		}else if(fileName.endsWith("ui")||fileName.endsWith("cmp")){
			this.setText("获取JSP路径");
		}
	    this.setImageDescriptor(IconFactory.getImageDescriptor("business/node.gif"));
	}
	public void run() {
		 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();  //得到系统剪贴板 
		 /**查找配置文件**/ 
		 String fileName = file.getName();
		 String filePath = file.getLocation().toFile().getAbsolutePath();
		 String projectPath = file.getProject().getLocation().toFile().getAbsolutePath();
		 PropertyReader reader = new PropertyReader();
		 String resourcePath = reader.getPropertyValue("resourcePath");
		 filePath = filePath.replace(fileName, "");
		 filePath = filePath.replace((projectPath+resourcePath).replace("/","\\"), "").replace("\\", "/");
		 if(fileName.endsWith("ctl")){
			 fileName = fileName.replace(".ctl", "");
			 filePath = "/"+filePath + fileName+".do";
		 }else if(fileName.endsWith("ui")){
			 fileName = fileName.replace(".ui", "");
			 filePath = "/"+filePath + fileName+".jsp";
		 }
		 StringSelection selection = new StringSelection(filePath);  
		 clipboard.setContents(selection, null);  

	}
	protected boolean calculateEnabled() {
	   return true;
	}

}
