package ui.dialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import swtdesigner.ResourceManager;
import toolkit.Activator;
import ui.UIAbstractModel;
import ui.dialog.tableMenu.DeleteMenu;
import ui.dialog.tableMenu.MenuDef;
import ui.model.grid.ColumnBarModel;
import ui.model.grid.ColumnModel;
import ui.model.grid.GridModel;
import util.CacheInit;
import util.ConnFactory;
import util.EditorUtil;
import util.ViewerUtil;
import util.thread.InitConnectionThread;
import business.dialog.ConnAbstractDialog;

/**
 * 通过数据字典初始化ligerGrid的列
 * @author Administrator
 *
 */
public class EditColumnsNew extends ConnAbstractDialog {

	private Combo combo;
	private Text text_1;
	private TableViewer table_1;
	private TableViewer tableViewer;
	private TreeViewer tree;
	protected Object result;
	protected Shell shell;
	private GridModel grid = null;
	private Map<String,Object> allDataSource;
	private String owner = "";
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public EditColumnsNew(Shell parentShell) {
		super(parentShell);
	}
	/**
	 * Create the dialog
	 * @param parent
	 */
	public EditColumnsNew(Shell parent,GridModel model) {
		super(parent);
		this.grid = model;
	}
	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		try{
			final EditColumnsNew colEdit = this;
			final SashForm sashForm = new SashForm(area, SWT.HORIZONTAL);
			
			Composite left = new Composite(sashForm,SWT.None);
			text_1 = new Text(left, SWT.BORDER);
			text_1.setBounds(120, 6, 198, 25);
			text_1.addKeyListener(new KeyAdapter(){
				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					super.keyPressed(e);
					if(e.keyCode==13){
						String text_1_val = text_1.getText();
						colEdit.initTree(text_1_val);
					}
				}
				
			});
			tree = new TreeViewer(left, SWT.BORDER);
			Tree treeLeft = tree.getTree();
			tree.setLabelProvider(new TreeLabelProvider()); 
			tree.setContentProvider(new TreeContent()); 
			treeLeft.setBounds(0, 33, 356,557);

			final Button button_3 = new Button(left, SWT.FLAT | SWT.LEFT);
			button_3.setImage(ResourceManager.getPluginImage(Activator.getDefault(), "icons/LookUp.png"));
			button_3.setBounds(324, 6, 28, 25);
			button_3.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseDown(MouseEvent e) {
					String text_1_val = text_1.getText();
					colEdit.initTree(text_1_val);
				}
			});

			combo = new Combo(left, SWT.NONE);
			combo.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					dataSourceChange();
				}
			});
			combo.setBounds(0, 6, 114, 25);
			sashForm.setBounds(0, 0, 1252, 590);
	
			final SashForm sashForm_1 = new SashForm(sashForm, SWT.VERTICAL);
			Composite up = new Composite(sashForm_1,SWT.BORDER | SWT.EMBEDDED);
			
			tableViewer =  new TableViewer(up, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL); //数据字典列信息
			table_1 = new TableViewer(sashForm_1, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL); 
			initTable1();
			initTable2();
			
			initDrag();
			sashForm_1.setWeights(new int[] {1, 1 });
			sashForm.setWeights(new int[] {2, 5 });
			
			tableViewer.getTable().setBounds(0, 7,up.getBounds().width, up.getBounds().height-7);
			
			allDataSource = CacheInit.getDataSource(combo,"Y");
			dataSourceChange();
			Tree temp = tree.getTree();
			temp.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e) {
				}
				@SuppressWarnings("unchecked")
				public void widgetSelected(SelectionEvent e) {
			         TreeItem item = (TreeItem)e.item;
			         HashMap<String,String> tab_name = (HashMap<String,String>)item.getData();
			         colEdit.initTableData(tab_name.get("tab_name"),null,null);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		return area;
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
		sql +=" order by tab_name";
		this.setMessage(sql);
		List<Map<String,String>> tables = ConnFactory.queryList(sql, con);
	    tree.setInput(tables);
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
	 * 初始化拖拽功能 
	 * 2014-12-26
	 * @tianming
	 */
	public void initDrag(){
		// 创建拖放源
		DragSource source = new DragSource(table_1.getTable(), DND.DROP_MOVE);
		source.setTransfer(new Transfer[] { LocalSelectionTransfer.getTransfer()});
		source.addDragListener(new DragSourceAdapter() {
			public void dragStart(DragSourceEvent event) {
				event.detail = DND.DROP_MOVE;
			}
			public void dragSetData(DragSourceEvent event) {
				LocalSelectionTransfer.getTransfer().setSelection(table_1.getSelection());
			}
		});
		// 定义拖放的目标控件
		DropTarget target = new DropTarget(table_1.getTable(), DND.DROP_MOVE);
		// 设置目标控件所接受的传输数据
		target.setTransfer(new Transfer[] { LocalSelectionTransfer.getTransfer() });
		target.addDropListener(new DropTargetAdapter() {
			// 当光标进入到目标控件的区域时
			public void dragEnter(DropTargetEvent event) {
			    event.detail = DND.DROP_MOVE;
			}
			@SuppressWarnings("unchecked")
			public void drop(DropTargetEvent event) {
				try{
					final Iterator it = ((IStructuredSelection)event.data).iterator();
					ColumnModel drop_row =(ColumnModel) it.next();
					List<ColumnModel> list = new ArrayList<ColumnModel>();
					TableItem[] items = table_1.getTable().getItems();
					TableItem overItem = (TableItem)event.item;
					 
					for(int i = 0;i<items.length;i++){
						TableItem item = items[i];
						ColumnModel row = (ColumnModel)item.getData();
						if(row.val("tab_name").equals(drop_row.val("tab_name"))&&row.val("name").equals(drop_row.val("name"))){
						}else{
							if(item==overItem){
								list.add(drop_row);
							}
							list.add(row);
						}
					}
					table_1.setInput(list);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 初始化上表 
	 * 2014-12-26
	 * @tianming
	 */
	public void initTable1(){
		List<String[]> tableConfig = new ArrayList<String[]>();
		tableConfig.add(new String[]{"111","列名","col_name"});
		tableConfig.add(new String[]{"134","列描述","col_dsc"});
		tableConfig.add(new String[]{"104","表名","tab_name"});
		tableConfig.add(new String[]{"100","功能类型","func_type"});
		tableConfig.add(new String[]{"100","数据来源类型","input_type"});
		tableConfig.add(new String[]{"100","备注","col_brf"});
		MenuDef[] menus = new MenuDef[2];
		menus[0] = new MenuDef("选择当前行"){
			@SuppressWarnings("unchecked")
			public void handleEvent(Event arg0) {
				try {
					Table tab = this.tableViewer.getTable();
					TableItem item = tab.getSelection()[0];
					Map<String,String> data = (Map<String,String>)item.getData();
					table_1.add(data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}};
		menus[1] = new MenuDef("选择全部"){
			@SuppressWarnings("unchecked")
			public void handleEvent(Event arg0) {
				Table tab = this.tableViewer.getTable();
				TableItem[] item = tab.getItems();
				for(int i = 0;i<item.length;i++){
					Map<String,String> data = (Map<String,String>)item[i].getData();
					table_1.add(data);
				}
			}};
		ViewerUtil.initTable(tableViewer, tableConfig,menus,null);
	}
	/**
	 * 初始化下表
	 * 2014-12-26
	 * @tianming
	 */
	public void initTable2(){
		List<String[]> tableConfig = new ArrayList<String[]>();
		tableConfig.add(new String[]{"111","列名","col_name"});
		tableConfig.add(new String[]{"134","列描述","col_dsc"});
		tableConfig.add(new String[]{"104","表名","tab_name"});
		MenuDef[] menus = new MenuDef[1];
		menus[0] = new DeleteMenu();
		ViewerUtil.initTable(table_1, tableConfig,menus,null);
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
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void okPressed() {
		try{
			List<UIAbstractModel> list = new ArrayList<UIAbstractModel>();
			Table tab = table_1.getTable();
			TableItem[] items = tab.getItems();
			ColumnBarModel bar = null;
			for(int i = 0;i<grid.getChildren().size();i++){
				if(grid.getChildren().get(i) instanceof ColumnBarModel){
					bar = (ColumnBarModel)grid.getChildren().get(i);
					break;
				}
			}
			for(int i = 0;i<items.length;i++){
				HashMap<String,String> rows = (HashMap<String,String>)items[i].getData();
				ColumnModel model = new ColumnModel("ColumnModel");
				model.setVal("title",rows.get("col_dsc"));
				model.setVal("tab_name",rows.get("tab_name").toLowerCase());
				model.setVal("field",rows.get("col_name").toLowerCase());
				list.add((UIAbstractModel)model);
				model.parentModel = bar;
			}
			bar.addChildList(list);
			EditorUtil.fireEditorDirty();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		super.okPressed();
	}
	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(1262, 743);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("初始化Grid列信息");
	}

}
