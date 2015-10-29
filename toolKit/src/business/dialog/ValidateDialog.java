package business.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import util.EditorUtil;
import util.PublicTableSet;

import business.dialog.buttonAdapter.AddAdapter;
import business.dialog.buttonAdapter.DeleteAdapter;
import business.model.action.ValidateModel;

public class ValidateDialog extends TitleAreaDialog {

	private TableViewer tableViewer = null;
	private Text text;//显示
	
	private ValidateModel model = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public ValidateDialog(Shell parentShell,ValidateModel model) {
		super(parentShell);
		this.model = model;
	}

	@Override
	protected void okPressed() {
		model.setDisplay(text.getText());
		List<HashMap<String,String>> parameter = PublicTableSet.getTableData(tableViewer);
		model.setParameter(parameter);
		EditorUtil.fireEditorDirty();
		super.okPressed();
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setBounds(10, 10, 607, 318);

		final TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("参数设置");

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite);

		final Group group = new Group(composite, SWT.NONE);
		group.setText("输入参数");
		group.setBounds(0, 52, 585, 226);

		tableViewer = new TableViewer(group,  SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(10, 20, 509, 196);
		List<String> paramAll = new ArrayList<String>();
		model.getAllParamName(paramAll);
		PublicTableSet.validateTable(model,tableViewer,paramAll);
		tableViewer.setInput(model.getParameter());
		
		final Button button_1 = new Button(group, SWT.NONE);
		button_1.setText("新增");
		button_1.setBounds(525, 21, 50, 27);
		HashMap<String,String> init = new HashMap<String,String>(); 
		init.put("paramName", "var");
		init.put("validateType", "");
		init.put("validateParam", "");
		button_1.addMouseListener(new AddAdapter(tableViewer,init));//
		
		final Button button_2 = new Button(group, SWT.NONE);
		button_2.setText("删除");
		button_2.setBounds(525, 57, 50, 27);
		button_2.addMouseListener(new DeleteAdapter(tableViewer));
		
		final Button button = new Button(group, SWT.NONE);
		button.setText("上移");
		button.setBounds(525, 95, 50, 27);

		final Button button_3 = new Button(group, SWT.NONE);
		button_3.setBounds(525, 138, 50, 27);
		button_3.setText("下移");

		final Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 24,59, 17);
		label.setText("显示名称：");

		text = new Text(composite, SWT.BORDER);
		text.setBounds(75, 21,500, 25);
		text.setText(model.getDisplay());
 
		setMessage("设置校验属性");
		//
		return area;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(643, 493);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("信息");
	}
	@Override
	protected Point getInitialLocation(Point initialSize) {
		// TODO Auto-generated method stub
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - initialSize.x / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - initialSize.y / 2;
		return new Point(nLocationX,nLocationY);
	}
}
