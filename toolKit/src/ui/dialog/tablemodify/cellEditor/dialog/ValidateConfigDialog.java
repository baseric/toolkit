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

import ui.dialog.tablemodify.TextCellModifier;
import ui.dialog.tablemodify.cellEditor.MyComboBoxCellEditor;
import util.CacheInit;
import util.PublicTableSet;
import util.ViewerUtil;
import business.dialog.buttonAdapter.AddAdapter;
import business.dialog.buttonAdapter.DeleteAdapter;

public class ValidateConfigDialog extends Dialog {

	private Table table;
	protected Object result;
	protected Shell shell;
	private String validate = null;
	private TableViewer tableViewer = null;
	/**
	 * Create the dialog
	 * @param parent
	 * @param style
	 */
	public ValidateConfigDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Create the dialog
	 * @param parent
	 */
	public ValidateConfigDialog(Shell parent,String validate) {
		this(parent, SWT.NONE);
		this.validate = validate;
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
		shell.setSize(600, 465);
		shell.setText("SWT Dialog");

		tableViewer = new TableViewer(shell,  SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		Table table_2 = tableViewer.getTable();
		table_2.setLinesVisible(true);
		table_2.setHeaderVisible(true);
		List<String[]> columns = new ArrayList<String[]>();
		try {
			columns.add(new String[]{"200","验证名称","validateName"});
			columns.add(new String[]{"300","校验参数","validateParam"});
			ViewerUtil.initTable(tableViewer, columns);
			TextCellModifier  modifier = new TextCellModifier(tableViewer,null, null);   
			CellEditor[] cellEditor = new CellEditor[2];
			cellEditor[0] = new MyComboBoxCellEditor(table_2, CacheInit.getTextArray("validate"));
			cellEditor[1] = new TextCellEditor(tableViewer.getTable());
			tableViewer.setCellEditors(cellEditor);
			tableViewer.setCellModifier(modifier);
			List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
			if(validate!=null&&validate.length()>0){
				validate = validate.trim();	
				String[] valArr = validate.split(" ");
				for(int j = 0;j<valArr.length;j++){
					String[] temp = valArr[j].split(";");
					HashMap<String,String> validateMap = new HashMap<String,String>();
					validateMap.put("validateName", temp[0].replace(":", ""));
					validateMap.put("validateParam", "");
					if(temp.length>1){
						validateMap.put("validateParam", temp[1]);
					}
					list.add(validateMap);
				}
			}
		 
			tableViewer.setInput(list);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(0, 0, 594, 377);

		final Button button = new Button(shell, SWT.NONE);
		button.setText("增加一行");
		button.setBounds(357, 400, 67, 27);
		HashMap<String,String> init = new HashMap<String,String>();
		init.put("validateName", "required");
		button.addMouseListener(new AddAdapter(tableViewer, init));
		final Button button_1 = new Button(shell, SWT.NONE);
		button_1.setText("删除一行");
		button_1.setBounds(430, 400, 67, 27);
		button_1.addMouseListener(new DeleteAdapter(tableViewer));

		final Button button_2 = new Button(shell, SWT.NONE);
		button_2.setText("确定");
		button_2.setBounds(503, 400, 67, 27);
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(tableViewer!=null){
					try {
						List<HashMap<String,String>> validate = PublicTableSet.getTableData(tableViewer);
						if(validate!=null){
							String val = "";
							for(int i = 0;i<validate.size();i++){
								HashMap<String,String> temp = validate.get(i);
								String validateName = temp.get("validateName");
								String validateParam = temp.get("validateParam");
								val +=validateName;
								if(validateParam!=null&&validateParam.length()>0){
									val+=";"+validateParam;
								}
								if(i!=validate.size()-1){
									val +=" ";
								}
							}
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
