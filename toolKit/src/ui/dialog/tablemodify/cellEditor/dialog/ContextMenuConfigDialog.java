package ui.dialog.tablemodify.cellEditor.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import ui.UIAbstractModel;
import ui.dialog.tablemodify.cellEditor.MethodCellEditor;
import ui.dialog.tablemodify.cellEditor.UITextCellModifier;
import util.ViewerUtil;

public class ContextMenuConfigDialog extends Dialog {

	private Table table;
	protected String result;
	protected Shell shell;
	private TableViewer tableViewer = null;
	private List<HashMap<String,String>> list = null;
	private UIAbstractModel model = null;
	/**
	 * Create the dialog
	 * @param parent
	 * @param style
	 */
	public ContextMenuConfigDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Create the dialog
	 * @param parent
	 */
	public ContextMenuConfigDialog(Shell parent,UIAbstractModel model) {
		this(parent, SWT.NONE);
		this.model = model;
		list = new ArrayList<HashMap<String,String>>();
		String menus = model.val("contextMenu");
		if(menus!=null&&menus.length()>0){
			String[] temp = menus.split("@");
			for(int i = 0;i<temp.length;i++){
				if(temp[i]!=null&&temp[i].length()>0){
					String[] info = temp[i].split("#");
					HashMap<String,String> row = new HashMap<String,String>();
					row.put("attr_name", info[0]);
					row.put("attr_desc", info[1]);
					if(info.length==3){
						row.put("value", info[2]);
					}else{
						row.put("value", "");
					}
					row.put("modify_type", "dialog");
					row.put("dialog_class", "ui.dialog.tablemodify.cellEditor.MethodCellEditor");
					list.add(row);
				}
			}
		}
		//this.menus = menus;
	}

	/**
	 * Open the dialog
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - 210 / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - 342 / 2;
	    shell.setLocation(nLocationX, nLocationY);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}

	/**
	 * Create contents of the dialog
	 */
	protected void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(603, 309);
		shell.setText("右键菜单配置");
		
		final Button button = new Button(shell, SWT.NONE);
		button.setBounds(522, 245, 65, 27);
		button.setText("确定");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				result = "";
				for(int i = 0;i<list.size();i++){
					HashMap<String,String> row = list.get(i);
					result += (row.get("attr_name")+"#"+row.get("attr_desc")+"#"+row.get("value"))+"@";
				}
				shell.close();
			}
		}); 

		tableViewer = new TableViewer(shell,  SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(10, 10, 577, 225);
		List<String[]> columns = new ArrayList<String[]>();
		try {
			columns.add(new String[]{"180","菜单名称","attr_name"});
			columns.add(new String[]{"100","图标","attr_desc"});
			columns.add(new String[]{"250","调用函数","value"});
			ViewerUtil.initTable(tableViewer, columns);
			 
			UITextCellModifier  modifier = new UITextCellModifier(tableViewer,model);   
			CellEditor[] cellEditor = new CellEditor[3];      
			cellEditor[0] = new TextCellEditor(tableViewer.getTable()) ;      
			cellEditor[1] = new TextCellEditor(tableViewer.getTable()) ;
			cellEditor[2] = new MethodCellEditor(tableViewer.getTable(),model,null) ;
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
			tableViewer.setInput(list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final Button button_1 = new Button(shell, SWT.NONE);
		button_1.setText("删除一行");
		button_1.setBounds(440, 245, 60, 27);

		final Button button_2 = new Button(shell, SWT.NONE);
		button_2.setText("增加一行");
		button_2.setBounds(355, 245, 60, 27);
		button_1.addMouseListener(new MouseAdapter(){
			public void mouseDown(MouseEvent e) {
				try {
					Table tab = tableViewer.getTable();
					TableItem[] item = tab.getSelection();
					for(int i = 0;i<item.length;i++){
						list.remove(item[i].getData());
					}
					tableViewer.refresh();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}); 
		button_2.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				try {
					HashMap<String,String> row = new HashMap<String,String>();
					row.put("attr_name", "新增");
					row.put("attr_desc", "add");
					row.put("value", "");
					row.put("modify_type", "dialog");
					row.put("dialog_class", "ui.dialog.tablemodify.cellEditor.MethodCellEditor");
					list.add(row);
					tableViewer.refresh();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}); 
	}
	
}
 