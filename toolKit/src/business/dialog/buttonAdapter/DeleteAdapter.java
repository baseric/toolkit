package business.dialog.buttonAdapter;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class DeleteAdapter extends MouseAdapter {
	TableViewer tabview = null;
	public DeleteAdapter(TableViewer tabview){
		this.tabview = tabview;
	}
	public void mouseDown(MouseEvent e) {
		try {
			Table tab = tabview.getTable();
			TableItem[] item = tab.getSelection();
			for(int i = 0;i<item.length;i++){
				TableItem[] all = tab.getItems();
				for(int j = 0;j<all.length;j++){
					if(all[j]==item[i]){
						tab.remove(j);
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
