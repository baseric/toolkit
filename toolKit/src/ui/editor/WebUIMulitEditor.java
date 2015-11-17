package ui.editor;

import listener.ResourceUpdater;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import ui.util.UIGenerateCode;
import util.Log;
import util.PropertyReader;

public class WebUIMulitEditor extends MultiPageEditorPart {
	private Javascript editor;
	public ViewEditor ui_editor;
	public static ResourceUpdater theListener = null;
	@Override
	protected void createPages() {
		//String fileName = null;
		IFile file = null;
		try {
			if(theListener==null){
				theListener = new ResourceUpdater();
				ResourcesPlugin.getWorkspace().addResourceChangeListener(theListener);
			}
			file = ((IFileEditorInput) getEditorInput()).getFile();
			////-----------------------页面设计-------------------------------------------------------------------------
			ui_editor = new ViewEditor();
			int index1 = addPage(ui_editor, getEditorInput());
			setPageText(index1, "页面设计");
			////-----------------------JAVASCRIPT----------------------------------------------------------------------
			editor = new Javascript(ui_editor.getContentsModel().javascriptCode.toString());
			 
			int index = addPage(editor, null);
			setPageText(index, "javascript代码");
			
			setPartName(file.getName());
		} catch (Exception e) {
			Log.write("", e);
		}
	}
 
	@Override
	public void doSave(IProgressMonitor monitor) {
		IProject project = null;
		try {
			UIGenerateCode code = new UIGenerateCode();
			getEditor(1).doSave(monitor);
			ui_editor.doSave(monitor);
			//保存文件信息
			ui_editor.getContentsModel().javascriptCode = new StringBuffer(editor.getJsCode());
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			firePropertyChange(IEditorPart.PROP_DIRTY);
			code.saveToFile(file.getLocation().toFile(), ui_editor.getContentsModel());
			//生成代码
			if(file.getName().endsWith("ui")){
				project = file.getProject();
				PropertyReader reader = new PropertyReader();
				if(!"N".equals(reader.getPropertyValue("isComplierOnSave"))){
					code.createJSP(file.getLocation().toFile(), ui_editor.getContentsModel(),project);
				}
			}
			file.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Exception ioe) {
			Log.write("", ioe);
		}
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
}
