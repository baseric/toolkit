package ui.dialog.tableMenu;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Listener;

public abstract class MenuDef implements Listener{
	public String text = "";
	public TableViewer tableViewer = null;
	public MenuDef(String menuName){this.text = menuName;};
	public MenuDef() { }
}
