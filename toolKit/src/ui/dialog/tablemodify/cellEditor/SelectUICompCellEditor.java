package ui.dialog.tablemodify.cellEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

import ui.UIAbstractModel;

/**
 * 选择页面组件ID下拉选项
 * @author Administrator
 *
 */
public class SelectUICompCellEditor extends ComboBoxCellEditor {
	private List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	@SuppressWarnings("unchecked")
	public SelectUICompCellEditor(Composite parent,UIAbstractModel model ,HashMap editRow){
		super(parent,model.getAllCompId());
		model.getAllCompId(list);
	}
    protected void doSetValue(Object value) {
        for(int i = 0;i<list.size();i++){
        	if(list.get(i).get("id").equals(value)){
        		value = i;
        	}
        }
        if("".equals(value)){
        	value = 0;
        }
        super.doSetValue(value);
    }
	@Override
	protected Object doGetValue() {
		// TODO Auto-generated method stub
		int index = (Integer)super.doGetValue();
		return list.get(index).get("id");
	}
    
    
}
