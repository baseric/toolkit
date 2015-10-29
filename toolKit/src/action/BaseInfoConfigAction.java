package action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import action.dialog.BaseInfoConfig;

/**
 * 基础信息配置 
 * @author Administrator
 *
 */
public class BaseInfoConfigAction implements IWorkbenchWindowActionDelegate {
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
			BaseInfoConfig config = new BaseInfoConfig(window.getShell());
			config.open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
	}

}
