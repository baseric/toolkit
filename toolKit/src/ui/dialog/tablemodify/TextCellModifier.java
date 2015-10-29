package ui.dialog.tablemodify;
import java.util.HashMap;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import ui.UIAbstractModel;
public class TextCellModifier implements ICellModifier {
	private TableViewer tv = null;
	//private AbstractModel model = null;
	private ComboSelectionListener listener = null;
	public TextCellModifier(TableViewer tv,UIAbstractModel model,ComboSelectionListener listener){
		this.tv = tv;
		//this.model = model;
		this.listener = listener;
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
			HashMap<String,String> p = (HashMap<String,String>)element;
			value = p.get(propty);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	@SuppressWarnings("unchecked")
	public void modify(Object element, String property, Object value) {
		 try {
			 TableItem item = (TableItem)element;   
			 HashMap<String,String> p = (HashMap<String,String>)item.getData();
			 p.put(property, String.valueOf(value));
			 if(listener!=null)
				 listener.selectAfter(p,property, value);
			 tv.update(p, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
