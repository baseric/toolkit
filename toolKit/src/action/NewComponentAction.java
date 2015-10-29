package action;


import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import action.dialog.NewComponentDialog;

/**
 * 基础信息配置 
 * @author Administrator
 *
 */
public class NewComponentAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow arg0) {
		this.window = arg0;
	}

	public void run(IAction action) {
		try {
			NewComponentDialog config = new NewComponentDialog(window.getShell());
			config.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
	}

}
