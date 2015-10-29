package business.dialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import util.CacheInit;
import util.ConnFactory;
import util.EditorUtil;
import util.PublicTableSet;
import util.thread.InitConnectionThread;
import util.thread.ThreadTask;
import business.dialog.buttonAdapter.AddAdapter;
import business.dialog.buttonAdapter.DeleteAdapter;
import business.model.logic.SingleTabOptModel;

public class SingleTabOptNew extends ConnAbstractDialog {

	private Text paramName;
	private Combo entityName2;
	private Combo entityName;
	private Text display;
	private Combo datasource;
	private Table table_2;
	private Table table;
	private Text tab_name;
	private Text sql;
	private Combo optType;
	private SingleTabOptModel model ;
	private Map<String,Object> allDataSource;
	private TableViewer tableViewer = null;//预编译参数
	private TableViewer tableViewer_1 = null;//列绑定值
	private List<String> tableBindParam = null;
	private List<String> entityList = new ArrayList<String>();
	private AutoCompleteField au = null;
	private Button button_3_2_1 = null;
	
	private TabItem updateParam = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public SingleTabOptNew(Shell parentShell,SingleTabOptModel model) {
		super(parentShell);
		this.model = model;
	}
	protected void okPressed() {
		try {
			String param_name = paramName.getText();
			int indx1 =entityName.getSelectionIndex();
			int indx2 = entityName2.getSelectionIndex();
			if(param_name==null||"".equals(param_name)){
				this.setErrorMessage("变量名不能唯空");
				return;
			}
			String comText = optType.getItem(optType.getSelectionIndex());
			if("update".equals(comText)){
				if(indx1==indx2){
					this.setErrorMessage("更新实体和被更新实体不能是同一个变量");
					return;
				}
			}
			
			if(!"insert".equals(comText)){
				if(sql.getText()==null||sql.getText().length()==0){
					this.setErrorMessage("条件语句不能为空");
					return;
				}
			}
			
			model.setOptType(optType.getSelectionIndex());
			model.setDisplay(display.getText());
			model.setTab_name(tab_name.getText());
			model.setSql(sql.getText());
			
			model.setParamName(param_name);

			if(indx1!=-1){
				model.setEntityName(entityList.get(indx1));
			}
			if(indx2!=-1){
				model.setEntityName2(entityList.get(indx2));
			}
			List<HashMap<String,String>> parameter = PublicTableSet.getTableData(tableViewer);
			model.setYbyParam(parameter);
			List<HashMap<String,String>> parameter2 = PublicTableSet.getTableData(tableViewer_1);
			model.setParamList2(parameter2);
			EditorUtil.fireEditorDirty();
			super.okPressed();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		tableBindParam = new ArrayList<String>();
		HashMap<String,String> paramAllMap = new HashMap<String,String>();
		model.getParamInfo(paramAllMap,model);
		Set<String> paramNames = paramAllMap.keySet();
		for(String name : paramNames){
			String type = paramAllMap.get(name);
			if(type!=null&&type.indexOf("com.dhcc.financemanage")>-1){
				entityList.add(name);
			}else if("Integer".equals(type)||"float".equals(type)||"int".equals(type)||"String".equals(type)||"Double".equalsIgnoreCase(type)||"Float".equalsIgnoreCase(type)){
				tableBindParam.add(name);
			}
		}
		//----------------------表信息区---------------------------
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, true));
		final Label label_4 = new Label(container, SWT.NONE);
		label_4.setText("操作类型：");
		label_4.setBounds(10, 47, 60, 17);

		optType = new Combo(container, SWT.NONE);//操作类型
		String[] opttype = CacheInit.getTextArray("singleTabOpt_type");
		optType.setItems(opttype);
		optType.setBounds(84, 44, 80, 25);
		optType.select(model.getOptType());
		optType.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				optTypeChange();
			}
		});
		final Label label_1_1 = new Label(container, SWT.NONE);
		label_1_1.setBounds(170, 47, 32, 17);
		label_1_1.setText("表名：");

		tab_name = new Text(container, SWT.BORDER);
		tab_name.setBounds(208, 44, 102, 25);
		tab_name.setText(model.getTab_name());
    	
		final Label label = new Label(container, SWT.NONE);
		label.setText("条件sql：");
		label.setBounds(10, 78, 68, 17);
		sql = new Text(container, SWT.BORDER);
		sql.setBounds(84, 75, 556, 25);
		sql.setText(model.getSql());
		
		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("数据源：");
		label_1.setBounds(10, 9, 43, 17);
		
		datasource = new Combo(container, SWT.NONE);
		datasource.setBounds(85, 6, 80, 25);
		allDataSource = CacheInit.getDataSource(datasource,"N");
		datasource.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				dataSourceChange();
			}
		});
		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setBounds(10, 114, 726, 327);
		//--------------------------------------------------------------------------------------
		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("预编译参数");
		final Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		
		tableViewer = new TableViewer(composite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(0, 0, 654, 297);
		PublicTableSet.tabColumnPrepareTable(model,tableViewer,tableBindParam);
		tableViewer.setInput(model.getYbyParam());
		
		final Button button = new Button(composite, SWT.NONE);
		button.setBounds(660, 0,50, 27);
		button.setText("新增");
		final Button button_1 = new Button(composite, SWT.NONE);
		button_1.setBounds(660, 33,50, 27);
		button_1.setText("删除");
		final Button button_3_1_1 = new Button(composite, SWT.NONE);
		button_3_1_1.setBounds(660, 66,50, 27);
		button_3_1_1.setText("上移");
		button_3_2_1 = new Button(composite, SWT.NONE);
		button_3_2_1.setBounds(660, 99,50, 27);
		button_3_2_1.setText("下移");
		HashMap<String,String> init = new HashMap<String,String>();
		init.put("paramType", "String");
		init.put("paramName", "name");
		button.addMouseListener(new AddAdapter(tableViewer,init));//
		button_1.addMouseListener(new DeleteAdapter(tableViewer));
		//--------------------------------------------------------------------------------------
		//--------------------------------------------------------------------------------------------

		

		updateParam = new TabItem(tabFolder, SWT.NONE);
		updateParam.setText("指定更新的列");
		final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		updateParam.setControl(composite_1);

		tableViewer_1 = new TableViewer(composite_1,SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table_2 = tableViewer_1.getTable();
		table_2.setLinesVisible(true);
		table_2.setHeaderVisible(true);
		table_2.setBounds(0, 0, 654, 297);
		
		HashMap<String,String> init2 = new HashMap<String,String>();
		init2.put("paramName", "var");
		PublicTableSet.optTabPrepareTable(model,tableViewer_1,tableBindParam);
		tableViewer_1.setInput(model.getParamList2());
		
		final Button delete = new Button(composite_1, SWT.NONE);
		delete.setBounds(660, 0,50, 27);
		delete.setText("删除");
		delete.addMouseListener(new DeleteAdapter(tableViewer_1));
		
		final Button button_2 = new Button(container, SWT.NONE);
		button_2.setText("获取表结构");
		button_2.setBounds(646, 73, 90, 27);
		button_2.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				String name = tab_name.getText();
				if(name!=null&&name.length()>0){
					List<Map<String,String>> list = null;
					list = ConnFactory.queryList("select * from col  where tname='"+name.toUpperCase()+"'", con);
					for(int i = 0;i<list.size();i++){
						Map<String,String> row = list.get(i);
						row.put("cname", row.get("cname").toLowerCase());
						for(int j = 0;j<tableBindParam.size();j++){
							String id = row.get("tname")+"_"+row.get("cname");
							if(id.equalsIgnoreCase(tableBindParam.get(j))){
								row.put("paramName", tableBindParam.get(j));
								break;
							}
						}
					}
					tableViewer_1.setInput(list);
				}
			}
		});

		final Label label_2 = new Label(container, SWT.NONE);
		label_2.setText("名称：");
		label_2.setBounds(171, 9, 31, 17);

		display = new Text(container, SWT.BORDER);
		display.setBounds(208, 6, 102, 25);
		display.setText(model.getDisplay());
		//--------------------------------------------------------------------------------------------
		datasource.forceFocus();

		final Label label_3 = new Label(container, SWT.NONE);
		label_3.setText("新记录：");
		label_3.setBounds(316, 47, 66, 17);

		int selectIndex = 0;
		int selectIndex2 = 0;
		
		for(int i = 0;i<entityList.size();i++){
			if(entityList.get(i).equals(model.getEntityName())){
				selectIndex = i;
			}
			if(entityList.get(i).equals(model.getEntityName2())){
				selectIndex2 = i;
			}
		}
		entityName = new Combo(container, SWT.NONE);
		entityName.setBounds(416, 44, 111, 25);
		entityList.remove(model.getParamName());
		String[] arr = new String[entityList.size()];
		entityList.toArray(arr);
		entityName.setItems(arr);
		entityName.select(selectIndex);
		final Label label_5 = new Label(container, SWT.NONE);
		label_5.setText("原记录：");
		label_5.setBounds(538, 47, 68, 17);

		entityName2 = new Combo(container, SWT.NONE);
		entityName2.setBounds(612, 44, 124, 25);
		entityName2.setItems(arr);
		entityName2.select(selectIndex2);
		final Label label_6 = new Label(container, SWT.NONE);
		label_6.setText("执行结果变量名：");
		label_6.setBounds(316, 9, 96, 17);

		paramName = new Text(container, SWT.BORDER);
		paramName.setBounds(416, 6, 320, 25);
		if(model.getParamName()!=null)
			paramName.setText(model.getParamName());
		optTypeChange();
		dataSourceChange();
		setTitle("查询");
		//
		return area;
	}
	public void optTypeChange(){
		String comText = optType.getItem(optType.getSelectionIndex());
		if("insert".equals(comText)){
			entityName.setEnabled(true);
			entityName2.setEnabled(false);
			sql.setEnabled(false);
			button_3_2_1.setEnabled(false);
		}else if("delete".equals(comText)){
			entityName.setEnabled(false);
			entityName2.setEnabled(false);
			sql.setEnabled(true);
			button_3_2_1.setEnabled(false);
		}else if("update".equals(comText)){
			entityName.setEnabled(true);
			entityName2.setEnabled(true);
			sql.setEnabled(true);
			button_3_2_1.setEnabled(true);
		}else if("selectEntity".equals(comText)||"selectEntityForUpdate".equals(comText)){
			entityName.setEnabled(false);
			entityName2.setEnabled(false);
			sql.setEnabled(true);
			button_3_2_1.setEnabled(true);
		}
		
	}
	String owner = "";
	@SuppressWarnings("unchecked")
	public void dataSourceChange(){
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		String comText = datasource.getItem(datasource.getSelectionIndex());
		HashMap<String,String> info = (HashMap<String,String>)allDataSource.get(comText);
		final String username = info.get("username");
		owner = info.get("owner");
		InitConnectionThread initCon = new InitConnectionThread(this, comText, info,new ThreadTask() {
			public void doTask() {
				List<Map<String,String>> list = ConnFactory.queryList("select * from dba_tables where owner='"+username.toUpperCase()+"' ", con);
				final String[] arr = new String[list.size()];
				for(int i = 0;i<list.size();i++){
					Map<String,String> map = (Map<String,String>)list.get(i);
					arr[i] = map.get("table_name");
				}
				if(au==null){
					Display.getDefault().syncExec(new Runnable() {
					    public void run() {
							TextContentAdapter ad = new TextContentAdapter(){};
							au = new AutoCompleteField(tab_name,ad , arr);
					    }
					});
				}else{
					Display.getDefault().syncExec(new Runnable() {
					    public void run() {
					    	au.setProposals(arr);
					    }
					});
				}				
			}
		});
		initCon.start();
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
		return new Point(755, 611);
	}
	@Override
	protected Point getInitialLocation(Point initialSize) {
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - initialSize.x / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - initialSize.y / 2;
		return new Point(nLocationX,nLocationY);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("单表操作组件");
	}
}
