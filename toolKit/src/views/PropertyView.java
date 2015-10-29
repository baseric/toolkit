package views;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.part.ViewPart;

import ui.UIAbstractModel;
import ui.dialog.formdialog.MyEditingSupport;
import util.ViewerUtil;

public class PropertyView extends ViewPart {

	public static final String ID = "views.PropertyView"; //$NON-NLS-1$
	private Table table_1;
	private Table table;
	private TableViewer tableViewer_1 = null;//事件
	private TableViewer tableViewer = null;//属性
	private UIAbstractModel model = null;
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());

		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		initAttrTab(tabFolder);
		initEventTab(tabFolder);
		createActions();
		initializeToolBar();
		initializeMenu();
	}
	public void setDataByModel(UIAbstractModel model){
		this.model = model;
		List<Map<String,String>> data = new ArrayList<Map<String,String>>();
		List<Map<String,String>> dataEvent = new ArrayList<Map<String,String>>();
		for(int i = 0;i<model.getAttributes().size();i++){
			Map<String,String> map = model.getAttributes().get(i);
			if("Y".equals(map.get("disp_yn"))&&("1".equals(map.get("attr_type"))||"3".equals(map.get("attr_type"))||"5".equals(map.get("attr_type")))){//过滤掉不显示的属性和事件属性
				data.add(map);
			} else if("2".equals(map.get("attr_type"))||"4".equals(map.get("attr_type"))){//过滤掉不显示的属性和事件属性
				dataEvent.add(map);
			}
		}
		try {
			tableViewer.setData("model", model);
			tableViewer.setInput(data); 
			tableViewer_1.setData("model", model);
			tableViewer_1.setInput(dataEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void setFocus() {
		// Set the focus
	}
	private void initAttrTab(TabFolder tabFolder){
		
		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("属性值");
		tableViewer = new TableViewer(tabFolder,  SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		tabItem.setControl(table);
		List<String[]> columns = new ArrayList<String[]>();
		try {
			columns.add(new String[]{"100","属性名","attr_name"});
			columns.add(new String[]{"100","属性描述","attr_desc"});
			columns.add(new String[]{"200","属性值","value"});
			columns.add(new String[]{"250","备注","brf"});
			ViewerUtil.initTable(tableViewer, columns,new int[]{2},new MyEditingSupport(tableViewer));
		
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
	private void initEventTab(TabFolder tabFolder){
		final TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("事件");
		tableViewer_1 = new TableViewer(tabFolder,  SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table_1 = tableViewer_1.getTable();
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		tabItem_1.setControl(table_1);
		List<String[]> columns = new ArrayList<String[]>();
		try {
			columns.add(new String[]{"100","事件名","attr_name"});
			columns.add(new String[]{"100","事件描述","attr_desc"});
			columns.add(new String[]{"200","方法名","value"});
			columns.add(new String[]{"250","备注","brf"});
			ViewerUtil.initTable(tableViewer_1, columns,new int[]{2},new MyEditingSupport(tableViewer_1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the actions
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar
	 */
	private void initializeToolBar() {
		@SuppressWarnings("unused")
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu
	 */
	private void initializeMenu() {
		@SuppressWarnings("unused")
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}
}
