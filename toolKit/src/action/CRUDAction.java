package action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import action.wizard.CRUDWizard;

public class CRUDAction implements IWorkbenchWindowActionDelegate {
	private IStructuredSelection selection = null;
	@Override
	public void run(IAction arg0) {
		CRUDWizard wizard = new CRUDWizard();
		wizard.init(null, selection);
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.open();
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection selection) {
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub
		
	}
}
