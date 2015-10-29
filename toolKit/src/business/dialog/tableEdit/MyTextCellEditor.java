package business.dialog.tableEdit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import business.AbstractModel;
import business.dialog.ConfigArrayDialog;
import business.model.action.VariableModel;


public class MyTextCellEditor extends TextCellEditor {
	private String columnName = null;
	private AbstractModel model = null;
	private TableViewer tv;
	public MyTextCellEditor(AbstractModel model,TableViewer parent,String columnName){
		super(parent.getTable());
		this.columnName = columnName;
		this.model = model;
		this.tv = parent;
    }
	@SuppressWarnings("unchecked")
	@Override
    protected void doSetValue(Object value) {
		if(model instanceof VariableModel&&"paramValue".equals(columnName)){
			Table tab = tv.getTable();
		 	TableItem[] selectItem = tab.getSelection();
		 	if(selectItem.length>0){
		 		 TableItem data = (TableItem)selectItem[0];   
				 HashMap<String,String> p = (HashMap<String,String>)data.getData();
				 String type = p.get("paramType");
				 String isArray = p.get("isArray");
				 if("String".equals(type)&&"Y".equals(isArray)){
					 List<String> list = new ArrayList<String>();
					 String temp = (String)value;
					 if(temp.length()>0){
						String[] tempArr = temp.substring(1,temp.length()-1).split(",");
						for(int i = 0;i<tempArr.length;i++){
							if(tempArr[i]!=null&&!"".equals(tempArr[i].trim())){
								list.add(tempArr[i]);
							}
						}
					 }
					 ConfigArrayDialog dialog = new ConfigArrayDialog(Display.getCurrent().getActiveShell(),list);
					 dialog.open();
					 value = list.toString();
				 }
		 	}
		}
		super.doSetValue(value);
    }
	@Override
	protected Object doGetValue() {
		return super.doGetValue();
	}
    
}
