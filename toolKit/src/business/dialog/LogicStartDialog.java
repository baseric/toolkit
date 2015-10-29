package business.dialog;

import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import business.model.logic.LogicStartModel;

public class LogicStartDialog extends TitleAreaDialog {

	private Table table;
	private Text text;
	private LogicStartModel start = null;
	private TableViewer tableViewer_1 = null;
	//private IProject project = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public LogicStartDialog(Shell parentShell,LogicStartModel start,IProject project) {
		super(parentShell);
		this.start = start;
		//this.project = project;
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

		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setBounds(10, 49, 567, 267);

		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("参数定义");
		final Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		tableViewer_1 = initTab(composite,start.getParameter());
 
		setMessage("设置业务逻辑的入口参数");
		//
		return area;
	}
	public TableViewer initTab(Composite composite,List<HashMap<String,String>> data){
		Group group = new Group(composite, SWT.NONE);
		group.setText("输入参数");
		group.setBounds(0, 0, 559, 237);
		TableViewer tableViewer = new TableViewer(group, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		final List<String[]> columns = PublicTableSet.logicStartTable(start,tableViewer);
		tableViewer.setInput(data);
		table.setBounds(10, 21, 472, 206);
		Button button = new Button(group, SWT.NONE);
		button.setData(tableViewer);
		button.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				try {
					HashMap<String,String> row = new HashMap<String,String>();
					Button btn = (Button)e.getSource();
					for(int i = 0;i<columns.size();i++){
						row.put(columns.get(i)[2], "");
					}
					row.put("paramName","name");
					row.put("paramType","String");
					TableViewer tab = (TableViewer)btn.getData();
					tab.add(row);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		button.setText("新增");
		button.setBounds(487, 30, 62, 27);

		Button button_1 = new Button(group, SWT.NONE);
		button_1.setBounds(487, 66, 62, 27);
		button_1.setText("删除");
		button_1.setData(tableViewer);
		button_1.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				try {
					Button btn = (Button)e.getSource();
					TableViewer table = (TableViewer)btn.getData();
					Table tab = table.getTable();
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
		Button button_2 = new Button(group, SWT.NONE);
		button_2.setBounds(487, 99, 62, 27);
		button_2.setText("上移");

		Button button_3 = new Button(group, SWT.NONE);
		button_3.setBounds(487, 134, 62, 27);
		button_3.setText("下移");
		return tableViewer;
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
		return new Point(603, 489);
	}
	@Override
	protected Point getInitialLocation(Point initialSize) {
		// TODO Auto-generated method stub
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - initialSize.x / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - initialSize.y / 2;
		return new Point(nLocationX,nLocationY);
	}
	@Override
	protected void okPressed() {
		List<HashMap<String,String>> parameter = PublicTableSet.getTableData(tableViewer_1);
		start.setParameter(parameter);
		EditorUtil.fireEditorDirty();
		super.okPressed();
	}
	
}
