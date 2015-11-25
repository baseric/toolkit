package business.dialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import util.CacheInit;
import util.ConnFactory;
import util.EditorUtil;
import util.PublicTableSet;
import util.thread.InitConnectionThread;
import business.dialog.buttonAdapter.AddAdapter;
import business.dialog.buttonAdapter.DeleteAdapter;
import business.model.action.PackageVariableModel;

public class PackageVariableDialog extends ConnAbstractDialog {

	private Table table;
	private Combo combo_1;
	private Text text_2;
	private Combo combo;
	private Text text_1;
	private Text text;
	private PackageVariableModel model = null;
	private List<String> paramNames = null;
	private TableViewer tableViewer = null;
	private Button importVar = null;
	private Button delete = null;
	private Button add = null;
	
	private Map<String,Object> allDataSource;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public PackageVariableDialog(Shell parentShell,PackageVariableModel model) {
		super(parentShell);
		this.model = model;
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
		label.setBounds(10, 10, 58, 17);

		text = new Text(container, SWT.BORDER);
		text.setBounds(74, 7, 73, 25);
		text.setText(model.getDisplay());
		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("变量名：");
		label_1.setBounds(153, 10, 48, 17);

		text_1 = new Text(container, SWT.BORDER);
		text_1.setBounds(202, 7, 81, 25);
		text_1.setText(model.getParamName());
		final Label label_2 = new Label(container, SWT.NONE);
		label_2.setText("变量类型：");
		label_2.setBounds(289, 10, 58, 17);

		combo = new Combo(container, SWT.NONE);
		combo.setBounds(353, 7, 106, 25);
		combo.setItems(new String[]{"HashMap","实体类"});
		combo.select(model.getParamType());
		
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				paramTypeChanage();
			}
		});
		final Label label_3 = new Label(container, SWT.NONE);
		label_3.setText("表名：");
		label_3.setBounds(621, 10, 36, 17);

		text_2 = new Text(container, SWT.BORDER);
		text_2.setBounds(662, 7, 130, 25);
		text_2.setEnabled(false);
		text_2.setText(model.getTableName());
		final Label label_4 = new Label(container, SWT.NONE);
		label_4.setText("数据源：");
		label_4.setBounds(465, 10, 48, 17);

		combo_1 = new Combo(container, SWT.NONE);
		combo_1.setBounds(519, 7, 96, 25);
		combo_1.setEnabled(false);
		allDataSource = CacheInit.getDataSource(combo_1,"N");
		
		combo_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				dataSourceChange();
			}
		});
		dataSourceChange();
		tableViewer = new TableViewer(container, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(10, 38, 782, 402);
		paramNames = new ArrayList<String>();
		model.getAllParamName(paramNames);
		PublicTableSet.packageVariable(model, tableViewer, paramNames);
		tableViewer.setInput(model.getValues());
		setTitle("将变量封装到实体中");
		paramTypeChanage();
		//
		return area;
	}
	private void paramTypeChanage(){
		int key = combo.getSelectionIndex();
       	if(key==0){
       		text_2.setEnabled(false);
       		combo_1.setEnabled(false);
       	}else{
       		text_2.setEnabled(true);
       		combo_1.setEnabled(true);
       	}
	}
	@SuppressWarnings("unchecked")
	private void dataSourceChange(){
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		int index = combo_1.getSelectionIndex();
		if(index>-1){
			String comText = combo_1.getItem(index);
			HashMap<String,String> info = (HashMap<String,String>)allDataSource.get(comText);
			InitConnectionThread initCon = new InitConnectionThread(this, comText, info);
			initCon.start();
		}
	}
	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent,11, "导入变量",
				false);
		importVar = getButton(11);
		final TitleAreaDialog dialog = this;
		importVar.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				int index = combo.getSelectionIndex();
				if(index==0){
					if(paramNames!=null){
						List<Map<String,String>> data = new ArrayList<Map<String,String>>();
						for(int i = 0;i<paramNames.size();i++){
							Map<String,String> row = new HashMap<String,String>();
							row.put("key", paramNames.get(i));
							row.put("value", paramNames.get(i));
							data.add(row);
						}
						tableViewer.setInput(data);
					}
				}else{
					String tableName = text_2.getText();
					if(tableName==null||"".equals(tableName)){
						dialog.setErrorMessage("输入表名");
						return ;
					}else{
						List<Map<String,String>> columns = ConnFactory.queryList("SELECT CNAME AS KEY,COLTYPE,SCALE,TNAME FROM COL WHERE TNAME='"+tableName.toUpperCase()+"' ", con);
						for(int i = 0;i<columns.size();i++){
							Map<String,String> row = columns.get(i);
							String id =  row.get("key");
							row.put("key",id.toLowerCase());
							for(int j = 0;j<paramNames.size();j++){
								if(id.equalsIgnoreCase(paramNames.get(j))){
									row.put("value", paramNames.get(j));
									break;
								}
							}
						}
						tableViewer.setInput(columns);
					}
				}
			}
			
		});
		
		createButton(parent,12, "新增一行",
				false);
		add = getButton(12);
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("key","col_name");
		map.put("value","value");
		add.addMouseListener(new AddAdapter(tableViewer,map));
		
		createButton(parent,13, "删除一行",
				false);
		delete = getButton(13);
		delete.addMouseListener(new DeleteAdapter(tableViewer));
		
		
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
		return new Point(817, 611);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("实体封装");
	}

	@Override
	protected void okPressed() {
		String paramName = text_1.getText();
		String tableName = text_2.getText();
		if(paramName==null||paramName.length()==0){
			this.setErrorMessage("变量名不能为空");
			return;
		}
		int paramType = combo.getSelectionIndex();
		if(paramType==1){
			if(tableName==null||tableName.length()==0){
				this.setErrorMessage("表名不能为空");
				return;
			}
		}
		model.setDisplay(text.getText());
		model.setParamType(paramType);
		model.setParamName(paramName);
		model.setTableName(tableName);
		List<HashMap<String,String>> parameter = PublicTableSet.getTableData(tableViewer);
		model.setValues(parameter);
		
		EditorUtil.fireEditorDirty();
		super.okPressed();
	}

}
