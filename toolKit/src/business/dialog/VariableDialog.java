package business.dialog;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import util.EditorUtil;
import util.PublicTableSet;
import business.model.action.VariableModel;

public class VariableDialog extends TitleAreaDialog {

	private Table table;
	private Text text;
	private VariableModel varModel = null;
	private TableViewer tableViewer_1 = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public VariableDialog(Shell parentShell,VariableModel varModel) {
		super(parentShell);
		this.varModel = varModel;
	}

	@Override
	protected void okPressed() {
		List<HashMap<String,String>> parameter = PublicTableSet.getTableData(tableViewer_1);
		varModel.setVarList(parameter);
		varModel.setDisplay(text.getText());
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

		final Label label = new Label(container, SWT.NONE);
		label.setText("显示名称：");
		label.setBounds(10, 21, 67, 17);

		text = new Text(container, SWT.BORDER);
		text.setBounds(83, 18, 276, 25);
		text.setText(varModel.getDisplay());

		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setBounds(10, 49, 753, 267);

		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("设置常量");
		final Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		initTab(composite);
		setMessage("取值范围为request");
		//
		return area;
	}
	public void initTab(Composite composite){
		final Group group = new Group(composite, SWT.NONE);
		group.setText("输入参数");
		group.setBounds(0, 0, 745, 237);

		tableViewer_1 = new TableViewer(group, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table = tableViewer_1.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		final List<String[]> columns = PublicTableSet.variableTable(varModel,tableViewer_1);
		table.setBounds(10, 21, 657, 206);
		tableViewer_1.setInput(varModel.getVarList());
		
		final Button button = new Button(group, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		button.setText("新增");
		button.setBounds(673, 24, 62, 27);

		final Button button_1 = new Button(group, SWT.NONE);
		button_1.setBounds(675, 65, 62, 27);
		button_1.setText("删除");
		button_1.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				try {
					Table tab = tableViewer_1.getTable();
					TableItem[] item = tab.getSelection();
					for(int i = 0;i<item.length;i++){
						TableItem[] all = tab.getItems();
						for(int j = 0;j<all.length;j++){
							if(all[j]==item[i]){
								tab.remove(j);
							}
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		button.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				try {
					HashMap<String,String> row = new HashMap<String,String>();
					for(int i = 0;i<columns.size();i++){
						row.put(columns.get(i)[2], "");
					}
					row.put("paramName","name");
					row.put("paramType","String");
					row.put("isArray", "N");
					tableViewer_1.add(row);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(789, 509);
	}
	@Override
	protected Point getInitialLocation(Point initialSize) {
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - initialSize.x / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - initialSize.y / 2;
		return new Point(nLocationX,nLocationY);
	}
}
