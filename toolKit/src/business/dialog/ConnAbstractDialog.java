package business.dialog;

import java.sql.Connection;
import java.sql.SQLException;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;

public class ConnAbstractDialog extends TitleAreaDialog {
	public Connection con = null;
	public ConnAbstractDialog(Shell parentShell) {
		super(parentShell);
	}
		@Override
	protected void cancelPressed() {
		// TODO Auto-generated method stub
		super.cancelPressed();
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
