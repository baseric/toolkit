package action;

import newwizard.action.NewActionWizard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class NewActionAction implements IObjectActionDelegate {
	private IStructuredSelection selection = null;
	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void run(IAction arg0) {
		NewActionWizard wizard = new NewActionWizard();
		wizard.init(null, selection);
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.open(); 
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection selection) {
		if (selection != null && selection instanceof IStructuredSelection) {
			   this.selection = (IStructuredSelection) selection;
		}
	}
}
