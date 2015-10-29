package action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import action.dialog.DataSourceConfigDialog;

/**
 * 数据源配置
 * @author Administrator
 *
 */
public class DataSourceConfigAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub
		this.window = arg0;
	}

	public void run(IAction action) {
		DataSourceConfigDialog config = new DataSourceConfigDialog(window.getShell());
		config.open();
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
	}

}
