package ui.dialog.tablemodify.cellEditor.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import ui.UIAbstractModel;
import util.BaseInfoUtil;
import util.CacheInit;
import util.PublicTableSet;
import util.ViewerUtil;
import business.dialog.tableEdit.MyComboBoxCellEditor;
import business.dialog.tableEdit.TextCellModifier;

/**
 * JAVASCRIPT 调用方法及方法参数配置
 * @author Administrator
 *
 */
public class MehtodConfigDialog extends Dialog {
	
	private Table table;
	private Combo combo;
	protected Object result;
	protected Shell shell;
	private String methodName = "";
	private UIAbstractModel model;

	/**
	 * Create the dialog
	 * @param parent
	 * @param style
	 */
	public MehtodConfigDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Create the dialog
	 * @param parent
	 */
	public MehtodConfigDialog(Shell parent,String methodName,UIAbstractModel model) {
		this(parent, SWT.NONE);
		this.methodName = methodName;
		this.model = model;
	}

	/**
	 * Open the dialog
	 * @return the result
	 */
	public Object open() {
		try {
			createContents();
			shell.open();
			shell.layout();
			Display display = getParent().getDisplay();
			int nLocationX = Display.getCurrent().getClientArea().width / 2 - 634 / 2;
			int nLocationY = Display.getCurrent().getClientArea().height / 2 - 356 / 2;
			shell.setLocation(nLocationX, nLocationY);
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	private List<HashMap<String,String>> data  = new ArrayList<HashMap<String,String>>();
	private TableViewer tableViewer = null;
	private List<Map<String,String>> list = null;
	private List<Map<String,String>> methodParams = null;
	/**
	 * Create contents of the dialog
	 */
	protected  void createContents() {//
		//--------------处理需要回显的方法信息
		String method_name = null;
		if(methodName!=null&&methodName.length()>0){
			try {
				method_name = methodName.split("\\(")[0];
				String parameters = methodName.split("\\(")[1].split("\\)")[0];
				String[] temp = parameters.split(",");
				if(temp!=null){
					for(int i = 0;i<temp.length;i++){
						HashMap<String,String> map = new HashMap<String,String>();
						map.put("modify_type", "字符串常量");
						map.put("value", temp[i].trim().replaceAll("'",""));
						data.add(map);
						
					}
				}
			} catch (Exception e1) {
				//e1.printStackTrace();
			}
		}
		//
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(634, 356);
		shell.setText("SWT Dialog");
		BaseInfoUtil util = new BaseInfoUtil();
		list = util.readXML("dev_common_method");
		methodParams = util.readXML("dev_method_param");
		final Label label = new Label(shell, SWT.NONE);
		label.setText("公共方法名：");
		label.setBounds(21, 10, 72, 17);

		combo = new Combo(shell, SWT.NONE);
		List<String> arr = new ArrayList<String>();
		int defaultSelect = 0;
		for(int i = 0;i<list.size();i++){
			if("Y".equals(list.get(i).get("disp_yn"))){
				arr.add("["+list.get(i).get("method_name")+"]"+list.get(i).get("method_desc"));
				if(list.get(i).get("method_name").equals(method_name)){
					defaultSelect = i;
				}
			}
		}
		String[] temp = new String[arr.size()];
		arr.toArray(temp);
		combo.setBounds(109, 7, 491, 25);
		combo.setItems(temp);
		combo.select(defaultSelect);
		final Group group = new Group(shell, SWT.NONE);
		group.setText("方法参数");
		group.setBounds(10, 40, 608, 226);

		tableViewer = new TableViewer(group,  SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(10, 29, 584, 187);
		List<String[]> columns = new ArrayList<String[]>();
		try {
			columns.add(new String[]{"200","参数类型","modify_type"});
			columns.add(new String[]{"300","参数值","value"});
			ViewerUtil.initTable(tableViewer, columns,new int[]{1},new MehtodEditingSupport(tableViewer,model));
			TextCellModifier  modifier = new TextCellModifier(tableViewer,null);   
			CellEditor[] cellEditor = new CellEditor[1];      
			cellEditor[0] = new MyComboBoxCellEditor(tableViewer,CacheInit.getTextArray("modify_type"),null,null);      
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier); 
			tableViewer.setInput(data);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		final Button button = new Button(shell, SWT.NONE);
		button.setText("确定");
		button.setBounds(544, 284, 61, 27);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(tableViewer!=null){
					try {
						List<HashMap<String,String>> validate = PublicTableSet.getTableData(tableViewer);
						if(validate!=null){
							String val = "";
							for(int i = 0;i<validate.size();i++){
								HashMap<String,String> temp = validate.get(i);
								String value = temp.get("value");
								val+="'"+value+"'";
								if(i!=validate.size()-1){
									val +=",";
								}
							}
							String method_name = list.get(combo.getSelectionIndex()).get("method_name");
							result =method_name+"("+val+")";
						}
					} catch (Exception ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
				}
				shell.close();
			}
		
		});
		final Button button_1 = new Button(shell, SWT.NONE);
		button_1.setText("删除一行");
		button_1.setBounds(478, 284, 60, 27);
		button_1.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				try {
					Table tab = tableViewer.getTable();
					TableItem[] item = tab.getSelection();
					for(int i = 0;i<item.length;i++){
						TableItem[] all = tab.getItems();
						for(int j = 0;j<all.length;j++){
							if(all[j]==item[i]){
								tab.remove(j);
								data.remove(j);
							}
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		final Button button_1_1 = new Button(shell, SWT.NONE);
		button_1_1.setBounds(412, 284, 60, 27);
		button_1_1.setText("新增一行");
		HashMap<String,String> init = new HashMap<String,String>();
		init.put("modify_type", "str");
		button_1_1.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				HashMap<String,String> row = new HashMap<String,String>();
				row.put("modify_type", "字符串常量");
				row.put("value", "");
				data.add(row);
				tableViewer.refresh();
			}
			
		});
		//
	}

}
