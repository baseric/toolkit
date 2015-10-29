package ui.dialog.tableMenu;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;

public class SelectAll extends MenuDef {
	public SelectAll(){
		super("全选");
	}
	@Override
	public void handleEvent(Event arg0) {
		// TODO Auto-generated method stub
		TableItem[] items = this.tableViewer.getTable().getItems(); 
		if (items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				items[i].setChecked(true); 
			} 
		} 
	}

}
