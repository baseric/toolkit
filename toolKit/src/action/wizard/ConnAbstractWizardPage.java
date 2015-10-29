package action.wizard;

import java.sql.Connection;

import org.eclipse.jface.wizard.WizardPage;

public abstract class ConnAbstractWizardPage extends WizardPage {
	protected ConnAbstractWizardPage(String pageName) {
		super(pageName);
	}

	public Connection con = null;
}
