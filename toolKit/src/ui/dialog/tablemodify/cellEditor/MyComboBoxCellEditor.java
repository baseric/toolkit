package ui.dialog.tablemodify.cellEditor;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * 下拉选项
 * @author Administrator
 *
 */
public class MyComboBoxCellEditor extends ComboBoxCellEditor {
	private String[] arr = null;
	public MyComboBoxCellEditor(Composite parent,String[] arr){
		super(parent,arr);
		this.arr = arr;
	}
    protected void doSetValue(Object value) {
        for(int i = 0;i<arr.length;i++){
        	if(arr[i].equals(value)){
        		value = i;
        	}
        }
        super.doSetValue(value);
    }
	@Override
	protected Object doGetValue() {
		// TODO Auto-generated method stub
		int index = (Integer)super.doGetValue();
		if(arr.length==0){
			return null;
		}
		return arr[index];
	}
    
    
}
