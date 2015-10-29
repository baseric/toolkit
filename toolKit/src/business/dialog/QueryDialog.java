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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import util.EditorUtil;
import util.PublicTableSet;
import business.dialog.buttonAdapter.AddAdapter;
import business.dialog.buttonAdapter.DeleteAdapter;
import business.model.logic.QueryModel;

public class QueryDialog extends TitleAreaDialog {

	private Combo combo;
	private Text order;
	private Table table_2;
	private Table table;
	private Text translation;
	private Text sql;
	private Text display;
	private Combo isFenye;
	private QueryModel model ;
	
	private TableViewer tableViewer = null;
	private TableViewer tableViewer_1 = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public QueryDialog(Shell parentShell,QueryModel model) {
		super(parentShell);
		this.model = model;
	}
	protected void okPressed() {
		model.setDisplay(display.getText());
		model.setQueryType(0);
		model.setIsFenye(isFenye.getSelectionIndex());
		
		model.setTab_name(translation.getText());
		model.setOrder(order.getText());
		model.setSql(sql.getText());
		model.setTransFlag(combo.getSelectionIndex());
		
		List<HashMap<String,String>> parameter = PublicTableSet.getTableData(tableViewer);
		model.setQueryConditon(parameter);
		List<HashMap<String,String>> parameter2 = PublicTableSet.getTableData(tableViewer_1);
		model.setParamList2(parameter2);
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
		
		display = new Text(container, SWT.BORDER);
		display.setBounds(74, 6, 110, 25);
		display.setText(model.getDisplay());
		final Label label_3 = new Label(container, SWT.NONE);
		label_3.setText("显示名称：");
		label_3.setBounds(10, 9, 60, 17);

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setBounds(190, 9,60, 17);
		label_1.setText("是否分页：");

		isFenye = new Combo(container, SWT.NONE);
		isFenye.setBounds(250, 6,60, 25);
		isFenye.add("是");
		isFenye.add("否");
		isFenye.select(model.getIsFenye());

		
		final Label label_1_1 = new Label(container, SWT.NONE);
		label_1_1.setBounds(316, 9, 60, 17);
		label_1_1.setText("翻译表名：");

		translation = new Text(container, SWT.BORDER);
		translation.setBounds(382, 6, 105, 25);
		translation.setText(model.getTab_name());
		
		final Label label = new Label(container, SWT.NONE);
		label.setText("SQL语句：");
		label.setBounds(10, 45, 60, 17);
		sql = new Text(container, SWT.BORDER);
		sql.setBounds(74, 42, 413, 25);
		sql.setText(model.getSql());
		
		final Label label_2 = new Label(container, SWT.NONE);
		label_2.setText("order by：");
		label_2.setBounds(496, 45, 69, 17);

		order = new Text(container, SWT.BORDER);
		order.setBounds(571, 42, 165, 25);
		order.setText(model.getOrder());
		//--------------------------------------------------------------------------------------------
		final Group group = new Group(container, SWT.NONE);
		group.setText("动态查询参数");
		group.setBounds(10, 70, 726, 189);

		tableViewer = new TableViewer(group, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(10, 25, 653, 154);
		List<String> paramAll = new ArrayList<String>();
		model.getAllParamName(paramAll);
		PublicTableSet.queryTable(model,tableViewer,paramAll);
		tableViewer.setInput(model.getQueryConditon());
		final Button button = new Button(group, SWT.NONE);
		button.setText("新增");
		button.setBounds(670, 25, 50, 27);
		HashMap<String,String> init = new HashMap<String,String>(); 
		init.put("link", "and");
		init.put("colName", "colname");
		init.put("comp", "=");
		init.put("paramName", "var");
		button.addMouseListener(new AddAdapter(tableViewer,init));//
		final Button button_1 = new Button(group, SWT.NONE);
		button_1.setText("删除");
		button_1.setBounds(670, 55, 50, 27);
		button_1.addMouseListener(new DeleteAdapter(tableViewer));
		final Button button_3_1_1 = new Button(group, SWT.NONE);
		button_3_1_1.setBounds(670, 85, 50, 27);
		button_3_1_1.setText("上移");

		final Button button_3_2_1 = new Button(group, SWT.NONE);
		button_3_2_1.setBounds(670, 115, 50, 27);
		button_3_2_1.setText("下移");
		//--------------------------------------------------------------------------------------------
		final Group group_1 = new Group(container, SWT.NONE);
		group_1.setText("预编译参数");
		group_1.setBounds(10, 265, 726, 174);

		tableViewer_1 = new TableViewer(group_1,SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table_2 = tableViewer_1.getTable();
		table_2.setLinesVisible(true);
		table_2.setHeaderVisible(true);
		table_2.setBounds(10, 20, 654, 144);
		PublicTableSet.queryPrepareTable(model,tableViewer_1,paramAll);
		tableViewer_1.setInput(model.getParamList2());
		final Button button_2 = new Button(group_1, SWT.NONE);
		button_2.setText("新增");
		button_2.setBounds(670, 20, 50, 27);
		HashMap<String,String> init2 = new HashMap<String,String>();
		init2.put("paramName", "var");
		button_2.addMouseListener(new AddAdapter(tableViewer_1,init2));
		final Button button_3 = new Button(group_1, SWT.NONE);
		button_3.setText("删除");
		button_3.setBounds(670, 50, 50, 27);
		button_3.addMouseListener(new DeleteAdapter(tableViewer_1));
		final Button button_3_1 = new Button(group_1, SWT.NONE);
		button_3_1.setBounds(670, 80, 50, 27);
		button_3_1.setText("上移");

		final Button button_3_2 = new Button(group_1, SWT.NONE);
		button_3_2.setBounds(670, 110, 50, 27);
		button_3_2.setText("下移");

		final Label label_4 = new Label(container, SWT.NONE);
		label_4.setText("翻译标志位：");
		label_4.setBounds(493, 9, 72, 17);

		combo = new Combo(container, SWT.NONE);
		combo.setBounds(571, 6, 165, 25);
		combo.add("0");
		combo.add("1");
		combo.select(model.getTransFlag());
		setTitle("查询");
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
		return new Point(762, 614);
	}
	@Override
	protected Point getInitialLocation(Point initialSize) {
		// TODO Auto-generated method stub
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - initialSize.x / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - initialSize.y / 2;
		return new Point(nLocationX,nLocationY);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("公共查询组件");
	}
}
