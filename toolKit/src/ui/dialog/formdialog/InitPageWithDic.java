package ui.dialog.formdialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import ui.UIAbstractModel;
import ui.dialog.TreeContent;
import ui.dialog.TreeLabelProvider;
import ui.dialog.tableMenu.DeleteMenu;
import ui.dialog.tableMenu.MenuDef;
import ui.dialog.tableMenu.SelectAll;
import ui.dialog.tableMenu.UnSelect;
import ui.model.abstractModel.ContainerModel;
import ui.model.liger.FormModel;
import util.CacheInit;
import util.ConnFactory;
import util.DicUtil;
import util.EditorUtil;
import util.Log;
import util.ViewerUtil;
import util.thread.InitConnectionThread;
import business.dialog.ConnAbstractDialog;

/**
 * 根据数据字典生成表单项
 * @author Administrator
 *
 */
public class InitPageWithDic extends ConnAbstractDialog {

	private Combo combo;
	private Table table_1;
	private Table table;
	private Text text;
	private Tree tree;
	private ContainerModel form = null;
	private TreeViewer treeViewer = null;
	private TableViewer tableViewer = null;
	private TableViewer tableViewer_1 = null;
	
	private Map<String,Object> allDataSource;
	private String owner = "";
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public InitPageWithDic(Shell parentShell,ContainerModel form) {
		super(parentShell);
		this.form = form;
	}
	/**
	 * 查询表信息
	 * @param str
	 * 2014-12-26
	 * @tianming
	 */
	public void initTree(String str){
		String sql = "select * from dic_table where owner_usr='"+owner+"'";
		if(str!=null&&!"".equals(str)){
			sql += " and (tab_name like '%"+str.toUpperCase()+"%' or tab_dsc like '%"+str+"%')";
		}
		this.setMessage(sql);
		List<Map<String,String>> tables = ConnFactory.queryList(sql, con);
		if(tables.size()==0){
			this.setMessage("查询的表不存在");
		}else{
			this.setMessage("");
		}
		treeViewer.setInput(tables);
	}
	/**
	 * 查询上表的数据
	 * @param tab_name
	 * @param colName
	 * @param colDesc
	 * 2014-12-26
	 * @tianming
	 */
	public void initTableData(String tab_name,String colName,String  colDesc){
		String sql = "select * from data_table where tab_name='"+tab_name+"' and owner_usr='"+owner+"'";
		if(colName!=null&&!"".equals(colName)){
			sql +=" and col_name like '%"+colName+"%'";
		}
		if(colDesc!=null&&!"".equals(colDesc)){
			sql +=" and col_dsc like '%"+colDesc+"%'";
		}
		this.setMessage(sql);
		List<Map<String,String>> tables = ConnFactory.queryList(sql, con);
		tableViewer.setInput(tables);
	}
	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FillLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		List<String[]> columns = new ArrayList<String[]>();
		columns.add(new String[]{"111","列名","col_name"});
		columns.add(new String[]{"134","列描述","col_dsc"});
		columns.add(new String[]{"104","表名","tab_name"});
		columns.add(new String[]{"100","数据来源类型","input_type"});
		columns.add(new String[]{"100","功能类型","func_type"});
		columns.add(new String[]{"100","备注","col_brf"});
		MenuDef[] menus = new MenuDef[2];
		menus[0] = new SelectAll();
		menus[1] = new UnSelect();
		final SashForm sashForm = new SashForm(container, SWT.NONE);

		final Composite composite = new Composite(sashForm, SWT.NONE);
		treeViewer = new TreeViewer(composite, SWT.BORDER);
		text = new Text(composite, SWT.BORDER);
		text.setBounds(80, 0, 185, 25);
		text.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
				if(e.keyCode==13){
					String val = text.getText();
					if(val!=null&&val.length()>0){
						initTree(val);
					}
				}
			}
			public void keyPressed(KeyEvent arg0) {}
		});
		text.forceFocus();

		tree = treeViewer.getTree();
		tree.setBounds(0, 26, 265, 442);
		treeViewer.setLabelProvider(new TreeLabelProvider()); 
		treeViewer.setContentProvider(new TreeContent()); 
		//initTree(null);
		tree.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent e) {
		         TreeItem item = (TreeItem)e.item;
		         HashMap<String,String> tab_name = (HashMap<String,String>)item.getData();
		         initTableData(tab_name.get("tab_name"),null,null);
			}
		});

		combo = new Combo(composite, SWT.NONE);
		combo.setBounds(0, 0, 79, 25);
		tableViewer = CheckboxTableViewer.newCheckList(sashForm, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		
		table = tableViewer.getTable();
		ViewerUtil.initTable(tableViewer, columns,menus,null);
		table.setBounds(255, 199, 490, 463);

		final Composite composite_1 = new Composite(sashForm, SWT.NONE);

		final Button button = new Button(composite_1, SWT.NONE);
		button.setText(">>");
		button.setBounds(5, 205, 30, 27);
		//final ContainerModel ff = this.form;
		tableViewer_1= new TableViewer(sashForm, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION|SWT.BORDER);
		final MyCellModifier  modifier=new MyCellModifier(tableViewer_1);   
		button.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					Object[] objects = ((CheckboxTableViewer)tableViewer).getCheckedElements();
					for(int i = 0;i<objects.length;i++){
						if(objects[i] instanceof HashMap){
							HashMap<String,String> map = (HashMap<String,String>)objects[i];
							String input_type = map.get("input_type");
							map.put("comp_name", DicUtil.getModelTypeByInputType(input_type));
							map.put("class", modifier.getClassByType(map.get("comp_name")));
							map.put("el_express", "否");
						}
					}
					tableViewer_1.add(objects);
					UnSelect unselect = new UnSelect();
					unselect.tableViewer = tableViewer;
					unselect.handleEvent(null);
				} catch (Exception ex) {
					Log.write("", ex);
				}
			}
		});

		
		columns = new ArrayList<String[]>();
		columns.add(new String[]{"134","列名","col_name"});
		columns.add(new String[]{"124","表单类型","comp_name"});
		columns.add(new String[]{"100","启用EL表达式","el_express"});
		menus = new MenuDef[4];
		menus[0] = new DeleteMenu();
		table_1 = tableViewer_1.getTable();
		
		ViewerUtil.initTable(tableViewer_1, columns,menus,modifier);
		CellEditor[] cellEditor = new CellEditor[3];      
		cellEditor[0] = null;      
		cellEditor[1] = new ComboBoxCellEditor(tableViewer_1.getTable(),modifier.getTypes(this.form));
		cellEditor[2] = new ComboBoxCellEditor(tableViewer_1.getTable(),new String[]{"是","否"});
		tableViewer_1.setCellEditors(cellEditor);
		//设置修改器   
		tableViewer_1.setCellModifier(modifier);   
		
		table_1.setBounds(725, 129, 381, 463);
		sashForm.setWeights(new int[] {265, 428, 39, 365 });
		this.setTitle("数据字典生成");
		allDataSource = CacheInit.getDataSource(combo,"Y");
		dataSourceChange();
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				dataSourceChange();
			}
		});
		return area;
	}
	@SuppressWarnings("unchecked")
	private void dataSourceChange(){
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		int index = combo.getSelectionIndex();
		if(index>-1){
			String comText = combo.getItem(index);
			HashMap<String,String> info = (HashMap<String,String>)allDataSource.get(comText);
			owner = info.get("owner");
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
		createButton(parent, 12, "批量设置表单类型",
				false);
		Button button = getButton(12);
		button.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unchecked")
			public void mouseDown(MouseEvent e) {
				
				Table tab = tableViewer_1.getTable();
				TableItem[] items = tab.getItems();
				List<HashMap> data = new ArrayList<HashMap>();
				if(items.length>0){
					HashMap row = (HashMap)items[0].getData();
					data.add(row);
					String input_type = String.valueOf(row.get("comp_name"));
					for(int i = 1;i<items.length;i++){
						HashMap p = (HashMap)items[i].getData();
						p.put("comp_name", input_type);
						data.add(p);
					}
					tableViewer_1.setInput(data);
				}
			}
		});
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
		return new Point(1122, 631);
	}
	@Override
	protected Point getInitialLocation(Point initialSize) {
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - initialSize.x / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - initialSize.y / 2;
		return new Point(nLocationX,nLocationY);
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void okPressed() {
		try {
			Object[] objects = tableViewer_1.getTable().getItems();
			if(this.form instanceof FormModel){
				List<UIAbstractModel> children = new ArrayList<UIAbstractModel>();
				for(int i = 0;i<objects.length;i++){
					TableItem item = (TableItem)objects[i];
					HashMap<String,String> map = (HashMap<String,String>)(item.getData());
					UIAbstractModel child = null;
					String comp_name = map.get("comp_name");
					String tab_name = map.get("tab_name").toLowerCase();
					String col_name = map.get("col_name").toLowerCase();
					Class<?> clazz = Class.forName(map.get("class"));
					child = (UIAbstractModel)clazz.getConstructor(String.class).newInstance(comp_name);
					child.setVal("label",map.get("col_dsc"));
					child.setVal("name",col_name);
					child.setVal("tab_name",tab_name);
					if("0".equals(map.get("el_express"))){
						child.setVal("value", "${"+tab_name+"."+col_name+"}");
					}
					child.parentModel = this.form;
					children.add(child);
				}
				form.addChildList(children);
			}
			EditorUtil.fireEditorDirty();

		} catch (Exception e) {
			e.printStackTrace();
		}
		super.okPressed();
	}
	
}
