package business.editor.menuaction;

import images.IconFactory;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import util.PropertyReader;

import business.editpart.action.JspEditPart;
import business.editpart.action.LogicEditPart;
import business.editpart.action.MethodEditPart;
import business.model.ContentsModel;
import business.model.action.JspModel;
import business.model.action.LogicModel;
import business.model.action.MethodModel;
 

public class OpenFileAction extends SelectionAction {
	public IFile file = null;
	public ContentsModel contents = null;
	public OpenFileAction(IWorkbenchPart part,IFile file,ContentsModel contents) {
		super(part);
		this.file = file;
		this.contents = contents;
		this.setId("openFile");
	    this.setText("打开关联文件");
	    this.setImageDescriptor(IconFactory.getImageDescriptor("business/node.gif"));
	}
	public void run() {
	    IWorkbenchPage page =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	    IProject project = file.getProject();
	    PropertyReader reader = new PropertyReader();
	    String basePath =  reader.getPropertyValue("resourcePath");
	    String basePath2 =  reader.getPropertyValue("javaPath");
	    try {
	    	if(getSelectEditPart() instanceof LogicEditPart){
			    LogicEditPart part = (LogicEditPart)getSelectEditPart();
			    LogicModel logicModel = (LogicModel)part.getModel();
			    String logicPath = logicModel.getLogicPath();
		    	IFile source = project.getFile(logicPath);
		    	IDE.openEditor(page,source,true);
	    	}else if(getSelectEditPart() instanceof JspEditPart){
	    		JspEditPart jsp = (JspEditPart)this.getSelectEditPart();
	    		JspModel jspModel = (JspModel)jsp.getModel();
	    		String jspPath = jspModel.getPath();
	    		IFile file = project.getFile(basePath+jspPath.replace(".jsp", ".ui"));
	    		if(file==null){
	    			file = project.getFile(jspPath);
	    		}
	    		IDE.openEditor(page,file,true);
	    	}else if(getSelectEditPart() instanceof MethodEditPart){
	    		MethodEditPart method = (MethodEditPart)this.getSelectEditPart();
	    		MethodModel methodModel = (MethodModel)method.getModel();
	    		String jspPath = methodModel.getClassPath();
	    		jspPath =basePath2+ jspPath.replace(".","\\")+".java";
	    		IFile file = project.getFile(jspPath);
	    		IDE.openEditor(page,file,true);
	    	}
	    } catch (PartInitException e) {
		}

	}
	protected boolean calculateEnabled() {
		EditPart part = getSelectEditPart();
		if(part!=null&&(part instanceof MethodEditPart||part instanceof LogicEditPart||part instanceof JspEditPart)) return true;
	    return false;
	}
	public EditPart getSelectEditPart(){
		List<?> objects = getSelectedObjects();
	    for (Iterator<?> iter = objects.iterator(); iter.hasNext();) {
	        Object obj = iter.next();
	        if(obj instanceof EditPart){
	        	return (EditPart)obj;
	        }
	    }
	    return null;
	}
}
