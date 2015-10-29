package action.wizard.crudpage;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import util.PublicTableSet;

public class ModifyConfigPage extends WizardPage {
	private Table table;
	private ISelection selection;
	public TableViewer tableViewer = null;
	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public ModifyConfigPage() {
		super("wizardPage");
		setTitle("修改配置-显示项，更新列");
		setDescription("");
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		initialize();
		setControl(container);
		tableViewer = new TableViewer(container, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table = tableViewer.getTable();
		PublicTableSet.crudModifyTable(tableViewer);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(2, 2, 664, 296);
	}

	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				/*IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();*/
			}
		}
	}
}
