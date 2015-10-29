package business.dialog.openResource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.SelectionDialog;

import business.dialog.openResource.dialogs.OpenJavaTypeByAuthorDialog;

public class OpenUtil {
	public static IFile openSearch(IProject project, String basePath , String extendsName){
		SelectionDialog dialog = new OpenJavaTypeByAuthorDialog(Display.getCurrent().getActiveShell(),project,basePath,extendsName);
		dialog.setTitle("Open Java File by Author");
		dialog.setMessage("");

		int result = dialog.open();
		if (result == IDialogConstants.OK_ID){
			Object[] files = dialog.getResult();
			if (files != null && files.length > 0) {
				IFile file = null;
				for (int i = 0; i < files.length; i++) {
					file = (IFile) files[i];
					 return file;
				}
			}
		}
		return null;
	}
}
