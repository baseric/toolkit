package business.dialog.buttonAdapter;

import java.util.HashMap;
import java.util.Set;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
public class AddAdapter extends MouseAdapter {
	TableViewer tabview = null;
	HashMap<String,String> init = null;
	public AddAdapter(TableViewer tabview,HashMap<String,String> init){
		this.tabview = tabview;
		this.init = init;
	}
	public void mouseDown(MouseEvent e) {
		try {
			String[] columns = (String[])tabview.getColumnProperties();
			HashMap<String,String> row = new HashMap<String,String>();
			for(int i = 0;i<columns.length;i++){
				row.put(columns[i], "");
			}
			if(init!=null){
			Set<String> keys = init.keySet();
				for(String key:keys){
					row.put(key,String.valueOf(init.get(key)));
				}
			}
			row.put("seqn", System.currentTimeMillis()+"");
			tabview.add(row);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
