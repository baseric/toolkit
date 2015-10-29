package business.dialog.tableEdit;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import business.AbstractModel;

public class MyComboBoxCellEditor extends ComboBoxCellEditor {
	private String[] arr = null;
	public MyComboBoxCellEditor(final TableViewer parent,String[] arr,final ComboSelectionAdapter listener,final AbstractModel model){
		super(parent.getTable(),arr);
		this.arr = arr;
		final CCombo combo = (CCombo)this.getControl();
		final MyComboBoxCellEditor editor = this;
		combo.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String item = combo.getItem(combo.getSelectionIndex());
				String columnName = null;
				CellEditor[] cellArr = parent.getCellEditors();
				Object[] columns = parent.getColumnProperties();
				int i = 0;
				for(;i<cellArr.length;i++){
					if(editor==cellArr[i]){
						break;
					}
				}
				columnName = (String)columns[i];
				if(listener!=null){
					listener.selectAfter(model,item,parent,columnName);
				}
			}
			
		});
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
		int index = (Integer)super.doGetValue();
		Object selectValue = arr[index];
		return selectValue;
	}
    
    
}
