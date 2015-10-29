package business.dialog;

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

import util.PublicTableSet;
import util.ViewerUtil;
import business.dialog.tableEdit.TextCellModifier;

public class ConfigArrayDialog extends Dialog {

	private Table table;
	protected Object result;
	protected Shell shell;
	private TableViewer tableViewer ;
	private List<String> list;
	private int count = 0;
	/**
	 * Create the dialog
	 * @param parent
	 * @param style
	 */
	public ConfigArrayDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Create the dialog
	 * @param parent
	 */
	public ConfigArrayDialog(Shell parent,List<String> list) {
		this(parent, SWT.NONE);
		this.list = list;
	}

	/**
	 * Open the dialog
	 * @return the result
	 */
	public Object open() {
		createContents();
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - shell.getShell().getSize().x / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - shell.getSize().y / 2;
	    shell.setLocation(nLocationX, nLocationY);
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
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
		shell.setSize(319, 476);
		shell.setText("设置数组值");

		final Button button = new Button(shell, SWT.NONE);
		button.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				HashMap<String,String> row = new HashMap<String,String>();
				row.put("paramName", "name"+(++count));
				tableViewer.add(row);
			}
		});
		button.setText("增加");
		button.setBounds(10, 411, 71, 27);

		final Button button_1 = new Button(shell, SWT.NONE);
		button_1.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				Table tab = tableViewer.getTable();
				TableItem[] item = tab.getSelection();
				for(int i = 0;i<item.length;i++){
					TableItem[] all = tab.getItems();
					for(int j = 0;j<all.length;j++){
						if(all[j]==item[i]){
							tab.remove(j);
						}
					}
				}
			}
		});
		button_1.setText("删除");
		button_1.setBounds(87, 411, 71, 27);

		final Button button_2 = new Button(shell, SWT.NONE);
		button_2.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				list.clear();
				List<HashMap<String,String>> allRows = PublicTableSet.getTableData(tableViewer);
				for(HashMap<String,String> row:allRows){
					list.add(row.get("paramName"));
				}
				shell.close();
			}
		});
		button_2.setText("确定");
		button_2.setBounds(164, 411, 71, 27);

		final Button button_3 = new Button(shell, SWT.NONE);
		button_3.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				shell.close();
			}
		});
		button_3.setText("关闭");
		button_3.setBounds(241, 411, 62, 27);

		tableViewer = new TableViewer(shell, SWT.BORDER);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		List<String[]> columns = new ArrayList<String[]>();
		columns.add(new String[]{"287","变量名","paramName"});
		ViewerUtil.initTable(tableViewer, columns);
		table.setHeaderVisible(false);
		table.setBounds(10, 10, 293, 395);
		TextCellModifier  modifier = new TextCellModifier(tableViewer,null);   
		CellEditor[] cellEditor = new CellEditor[1];      
		cellEditor[0] = new TextCellEditor(tableViewer.getTable());
		tableViewer.setCellEditors(cellEditor);
		//设置修改器   
		tableViewer.setCellModifier(modifier);
		
		
		List<HashMap<String,String>> tableData = new ArrayList<HashMap<String,String>>();
		for(int i = 0;i<list.size();i++){
			HashMap<String,String> row = new HashMap<String,String>();
			row.put("paramName",list.get(i).trim());
			tableData.add(row);
		}
		tableViewer.setInput(tableData);
		//
	}

}
