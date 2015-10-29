package action.dialog;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import swtdesigner.ResourceManager;
import toolkit.Activator;
import util.PropertyReader;
import util.PublicTableSet;
import util.ViewerUtil;
import business.dialog.tableEdit.MyComboBoxCellEditor;
import business.dialog.tableEdit.TextCellModifier;

public class DataSourceConfigDialog extends TitleAreaDialog {

	private Table table;
	private TableViewer tableViewer;
	private List<HashMap<String,String>> dataSource = new ArrayList<HashMap<String,String>>();
	private Properties prop = null;
	private PropertyReader reader = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	@SuppressWarnings("unchecked")
	public DataSourceConfigDialog(Shell parentShell) {
		super(parentShell);
		reader = new PropertyReader("dataSource");
		prop = reader.getProperty();
		HashMap<String,String> allDataSource = new HashMap<String,String>();
		Enumeration<String> enu = (Enumeration<String>) prop.propertyNames();  
	    while (enu.hasMoreElements()) {  
			String key = enu.nextElement();
			allDataSource.put(key.split("\\.")[0],"");
		}  
	    Set<String> dataSourceNames = allDataSource.keySet();
	    for(String key:dataSourceNames){
	    	HashMap<String,String> row = new HashMap<String,String>();
	    	row.put("dataSourceName",key);
			row.put("driver",prop.getProperty(key+".driver"));
			row.put("url",prop.getProperty(key+".url"));
			row.put("username",prop.getProperty(key+".username"));
			row.put("password",prop.getProperty(key+".password"));
			row.put("isDatadic",prop.getProperty(key+".isDatadic"));
			row.put("owner",prop.getProperty(key+".owner"));
			row.put("isDefault",prop.getProperty(key+".isDefault"));
			dataSource.add(row);
	    }
		
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

		final ToolBar toolBar = new ToolBar(container, SWT.NONE);
		toolBar.setBounds(0, 0, 651, 25);

		final ToolItem newItemToolItem = new ToolItem(toolBar, SWT.PUSH);
		newItemToolItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				HashMap<String,String> row = new HashMap<String,String>();
				row.put("dataSourceName","name");
				row.put("driver","");
				row.put("url","");
				row.put("username","");
				row.put("password","");
				row.put("isDatadic", "N");
				row.put("owner", "");
				row.put("isDefault", "N");
				tableViewer.add(row);
			}
		});
		newItemToolItem.setImage(ResourceManager.getPluginImage(Activator.getDefault(), "icons/add.gif"));

		final ToolItem newItemToolItem_1 = new ToolItem(toolBar, SWT.PUSH);
		newItemToolItem_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
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
		newItemToolItem_1.setImage(ResourceManager.getPluginImage(Activator.getDefault(), "icons/delete.gif"));

		tableViewer = new TableViewer(container, SWT.BORDER|SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		List<String[]> columns = new ArrayList<String[]>();
		columns.add(new String[]{"105","数据源名","dataSourceName"});
		columns.add(new String[]{"64","默认使用","isDefault"});
		columns.add(new String[]{"165","数据库驱动","driver"});
		columns.add(new String[]{"254","数据库URL","url"});
		columns.add(new String[]{"70","用户名","username"});
		columns.add(new String[]{"70","密码","password"});
		columns.add(new String[]{"64","数据字典","isDatadic"});
		columns.add(new String[]{"84","数据库用户","owner"});
		ViewerUtil.initTable(tableViewer, columns);
		
		TextCellModifier  modifier = new TextCellModifier(tableViewer,null);   
		CellEditor[] cellEditor = new CellEditor[8];      
		cellEditor[0] = new TextCellEditor(tableViewer.getTable());      
		cellEditor[1] =  new MyComboBoxCellEditor(tableViewer,new String[]{"Y","N"},null,null);
		cellEditor[2] = new TextCellEditor(tableViewer.getTable());
		cellEditor[3] = new TextCellEditor(tableViewer.getTable());
		cellEditor[4] = new TextCellEditor(tableViewer.getTable());
		cellEditor[5] = new TextCellEditor(tableViewer.getTable());
		cellEditor[6] = new MyComboBoxCellEditor(tableViewer,new String[]{"Y","N"},null,null);
		cellEditor[7] =new TextCellEditor(tableViewer.getTable());
		tableViewer.setCellEditors(cellEditor);
		//设置修改器  
		tableViewer.setCellModifier(modifier);
		tableViewer.setInput(dataSource);
		table.setBounds(0, 25, 890, 318);
		//
		return area;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(916, 506);
	}

	@Override
	protected void okPressed() {
		List<HashMap<String,String>> data = PublicTableSet.getTableData(tableViewer);
		prop.clear();
		for(int i = 0;i<data.size();i++){
			HashMap<String,String> row = data.get(i);
			prop.setProperty(row.get("dataSourceName")+".driver", row.get("driver"));
			prop.setProperty(row.get("dataSourceName")+".url", row.get("url"));
			prop.setProperty(row.get("dataSourceName")+".username", row.get("username"));
			prop.setProperty(row.get("dataSourceName")+".password", row.get("password"));
			prop.setProperty(row.get("dataSourceName")+".isDatadic", row.get("isDatadic"));
			prop.setProperty(row.get("dataSourceName")+".owner", row.get("owner"));
			prop.setProperty(row.get("dataSourceName")+".isDefault", row.get("isDefault"));
		}
		reader.writerProperties(prop);
 		super.okPressed();
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("配置数据源");
	}

}
