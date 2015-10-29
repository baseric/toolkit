package ui.dialog.tablemodify.cellEditor;

import java.io.File;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import ui.UIAbstractModel;
import ui.dialog.tablemodify.DefDialogCellEditor;
import business.dialog.openResource.OpenUtil;

public class UrlDialogCellEditor extends DefDialogCellEditor {
	   private UIAbstractModel model = null;
	   public UrlDialogCellEditor(Composite parent,UIAbstractModel model,HashMap<String,String> map){
	       super(parent);
	       this.model = model;
	   }
	   protected Object openDialogBox(Control cellEditorWindow) {
		   IProject project = model.getFile().getProject();
		   String rootPath = project.getLocation().toFile().getAbsolutePath();
		   IFile file = OpenUtil.openSearch(project, "", "ui,ctl");
		   File f = file.getLocation().toFile();
		   String filePath = f.getAbsolutePath();
		   return filePath.replace(rootPath, "").replace("\\", "/").replace(".ui", ".jsp").replace(".ctl", ".do");
	   }
}
