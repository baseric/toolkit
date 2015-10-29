package action;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import ui.model.UIContentsModel;
import ui.util.UIGenerateCode;
import util.GenerateCode;
import business.model.ContentsModel;

public class ComplierResourceMenu implements IObjectActionDelegate {
	private IStructuredSelection selection = null;
	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void run(IAction arg0) {
			final Object obj = selection.getFirstElement();
			IRunnableWithProgress process = new IRunnableWithProgress() {
				public void run(IProgressMonitor m)  throws InvocationTargetException, InterruptedException {
					try {
						m.beginTask("开始检索资源文件", 2);
						File file = null;
						IProject project = null;
				    	if (obj instanceof IFolder) {
				    		final IFolder floder = (IFolder) obj;
				    		project = floder.getProject();
				    		file = floder.getLocation().toFile();
				    	}else if(obj instanceof IFile){
				    		final IFile ff = (IFile)obj;
				    		project = ff.getProject();
				    		file = ff.getLocation().toFile();
				    	}
				    	List<File> list = new ArrayList<File>();
			    		getAllIFileByFolder(list,file);
				    	m.setTaskName("开始编译资源文件.......");
				    	GenerateCode java = new GenerateCode();
				    	UIGenerateCode jsp = new UIGenerateCode();
				    	jsp.setProject(project);
				    	for(int i = 0;i<list.size();i++){
				    		File resource = list.get(i);
				    		if(resource.getName().endsWith("ctl")){
				    			ContentsModel contents = (ContentsModel)java.readFile(resource);
								java.createAction(contents, resource,project, "action.ftl");
							}else if(resource.getName().endsWith("ui")){
								UIContentsModel contents = (UIContentsModel)jsp.readFile(resource);
								jsp.createJSP(resource, contents,project);
							}
				    	}
				    	m.worked(1);
				    } catch (Exception e) {
				    	e.printStackTrace();
				    	m.setTaskName(e.getMessage());
				    } finally {
				    	m.done();
				    }
				}
			};
			ProgressMonitorDialog d = new ProgressMonitorDialog(null);
			try {
				d.run(true, false, process);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection selection) {
		if (selection != null && selection instanceof IStructuredSelection) {
			   this.selection = (IStructuredSelection) selection;
		}
	}
	private void getAllIFileByFolder(List<File> list, File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(int i = 0;i<files.length;i++){
				if(files[i].isDirectory()){
					getAllIFileByFolder(list, files[i]);
				}else if(files[i].getName().endsWith("ctl")||files[i].getName().endsWith("ui")){
					list.add(files[i]);
				}
			}
		}else {
			list.add(file);
		}
	}
}
