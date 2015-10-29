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

import ui.UIAbstractModel;
import ui.dialog.tablemodify.TextCellModifier;
import util.PublicTableSet;
import util.ViewerUtil;
import business.dialog.buttonAdapter.AddAdapter;
import business.dialog.buttonAdapter.DeleteAdapter;

/**
 * 下拉选择表 列信息配置
 * @author Administrator
 *
 */
public class LookupConfigDialog extends Dialog {

	private Table table;
	protected Object result;
	protected Shell shell;
	private String columns = null;
	private TableViewer tableViewer = null;
	private UIAbstractModel model = null;
	/**
	 * Create the dialog
	 * @param parent
	 * @param style
	 */
	public LookupConfigDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Create the dialog
	 * @param parent
	 */
	public LookupConfigDialog(Shell parent,String columns,UIAbstractModel model) {
		this(parent, SWT.NONE);
		this.columns = columns;
		this.model = model;
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
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - 600 / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - 464 / 2;
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
		shell.setSize(824, 465);
		shell.setText("SWT Dialog");

		tableViewer = new TableViewer(shell,  SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		Table table_2 = tableViewer.getTable();
		table_2.setLinesVisible(true);
		table_2.setHeaderVisible(true);
		List<String[]> columns = new ArrayList<String[]>();
		try {
			columns.add(new String[]{"100","字段名","field"});
			columns.add(new String[]{"100","列中文名","title"});
			columns.add(new String[]{"100","宽度","width"});
			columns.add(new String[]{"100","对齐方式","align"});
			columns.add(new String[]{"200","绑定表单项Name","form_name"});
			columns.add(new String[]{"100","查询条件","query"});
			columns.add(new String[]{"100","查询条件类型","query_type"});
			ViewerUtil.initTable(tableViewer, columns);
			TextCellModifier  modifier = new TextCellModifier(tableViewer,null, null);   
			CellEditor[] cellEditor = new CellEditor[7];
			cellEditor[0] = new TextCellEditor(tableViewer.getTable());
			cellEditor[1] = new TextCellEditor(tableViewer.getTable());
			cellEditor[2] = new TextCellEditor(tableViewer.getTable());
			cellEditor[3] = new TextCellEditor(tableViewer.getTable());
			cellEditor[4] = new TextCellEditor(tableViewer.getTable());
			cellEditor[5] = new TextCellEditor(tableViewer.getTable());
			cellEditor[6] = new TextCellEditor(tableViewer.getTable());
			tableViewer.setCellEditors(cellEditor);
			tableViewer.setCellModifier(modifier);
			List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
			if(this.columns!=null&&this.columns.length()>0){
				String temp = this.columns.replaceAll("\\[\\{", "").replaceAll("\\}\\]", "");
				String[] rows = temp.split("\\},\\{");
				for(int i = 0;i<rows.length;i++){
					 HashMap<String,String> map = new HashMap<String,String>();
					 String[] cfg = rows[i].split(",");
					 for(int j = 0;j<cfg.length;j++){
						 String[] attrValue = cfg[j].split(":");
						 map.put(attrValue[0].trim(), attrValue[1].replaceAll("\"","").trim());
					 }
					 list.add(map);
				}
			
			}
			tableViewer.setInput(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(0, 0, 814, 377);

		final Button button = new Button(shell, SWT.NONE);
		button.setText("增加一行");
		button.setBounds(556, 400, 67, 27);
		HashMap<String,String> init = new HashMap<String,String>();
		init.put("field", "col_name");
		init.put("title", "显示名");
		init.put("width", "100");
		init.put("align", "center");
		init.put("form_name", "");
		init.put("query", "N");
		init.put("query_type", "Text");
		button.addMouseListener(new AddAdapter(tableViewer, init));
		final Button button_1 = new Button(shell, SWT.NONE);
		button_1.setText("删除一行");
		button_1.setBounds(629, 400, 67, 27);
		button_1.addMouseListener(new DeleteAdapter(tableViewer));

		final Button button_2 = new Button(shell, SWT.NONE);
		button_2.setText("确定");
		button_2.setBounds(702, 400, 67, 27);
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(tableViewer!=null){
					try {
						List<HashMap<String,String>> columnsData = PublicTableSet.getTableData(tableViewer);
						if(columnsData!=null){
							String val = "[";
							for(int i = 0;i<columnsData.size();i++){
								val +="{ title:\""+columnsData.get(i).get("title")+
									  "\",field:\""+columnsData.get(i).get("field")+
									  "\",width:"+columnsData.get(i).get("width")+
									  ",align:\""+columnsData.get(i).get("align")+
									  "\",form_name:\""+columnsData.get(i).get("form_name")+
									  "\",query:\""+columnsData.get(i).get("query")+
									  "\",query_type:\""+columnsData.get(i).get("query_type")+"\"}";
								if(i!=columnsData.size()-1){
									val+=",";
								}
							}
							val+="]";
							result = val;
						}
					} catch (Exception ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
				}
				shell.close();
			}
		});
		//
	}

}
