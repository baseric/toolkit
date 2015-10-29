package ui.dialog.tableMenu;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class DeleteMenu extends MenuDef {
	public DeleteMenu(){
		super("删除");
	}
	@Override
	public void handleEvent(Event arg0) {
		// TODO Auto-generated method stub
		try{
			Table tab = this.tableViewer.getTable();
			TableItem[] item = tab.getSelection();
			for(int i = 0;i<item.length;i++){
				TableItem[] all = tab.getItems();
				for(int j = 0;j<all.length;j++){
					if(all[j]==item[i]){
						tab.remove(j);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
