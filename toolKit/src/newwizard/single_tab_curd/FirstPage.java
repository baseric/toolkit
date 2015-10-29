package newwizard.single_tab_curd;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class FirstPage extends WizardPage {

	/**
	 * Create the wizard
	 */
	public FirstPage() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
	}

	/**
	 * Create contents of the wizard
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		//
		setControl(container);

		final Label label = new Label(container, SWT.NONE);
		label.setText("Label");
		label.setBounds(10, 26, 122, 17);
	}

}
