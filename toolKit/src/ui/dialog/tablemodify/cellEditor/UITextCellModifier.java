package ui.dialog.tablemodify.cellEditor;
import java.util.HashMap;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import ui.UIAbstractModel;
public class UITextCellModifier implements ICellModifier {
	private TableViewer tv = null;
	@SuppressWarnings("unused")
	private UIAbstractModel model = null;
	public UITextCellModifier(TableViewer tv,UIAbstractModel model){
		this.tv = tv;
		this.model = model;
	}
	@Override
	public boolean canModify(Object arg0, String arg1) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getValue(Object element, String propty) {
		String value = "";
		try {
			HashMap<String,Object> p = (HashMap<String,Object>)element;
			value = String.valueOf(p.get(propty));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void modify(Object element, String property, Object value) {
		 try {
			 TableItem item = (TableItem)element;
			 HashMap<String,String> p = (HashMap<String,String>)item.getData();
			 p.put(property, String.valueOf(value));
			 tv.update(p, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
