package util;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ui.dialog.formdialog.MyCellModifier;
import ui.dialog.tableMenu.MenuDef;

public class ViewerUtil {
	/**
	 * 初始化表格工具类
	 * @param tableViewer
	 * @param tableConfig 
	 * 2014-12-26
	 * @tianming
	 */
	public static void initTable(final TableViewer tableViewer,final List<String[]> tableConfig){
		final Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		String[] columns = new String[tableConfig.size()];
		for(int i = 0;i<tableConfig.size();i++){
			String[] row = tableConfig.get(i);
			final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
			newColumnTableColumn.setWidth(Integer.parseInt(row[0]));
			newColumnTableColumn.setText(row[1]);
			columns[i] = row[2];
		}
	    class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
	        @SuppressWarnings("unchecked")
			public String getColumnText(Object element, int columnIndex) {
	        	try{
	            	if(element instanceof HashMap){
	            		HashMap<String,String> temp = (HashMap<String,String>)element;
	            		String[] row = tableConfig.get(columnIndex);
	            		String text = temp.get(row[2]);
	            		if(text==null){
	            			text = "";
	            		}
	            		return text;
	            	}
	        	}catch(Exception e){ 
	        		e.printStackTrace();
	        	}
	            return null;
	        }
	        public Image getColumnImage(Object element, int columnIndex) {
	            return null;
	        }
	    }
	    tableViewer.setContentProvider(new ContentProvider());
	    tableViewer.setLabelProvider(new TableLabelProvider());
	    tableViewer.setColumnProperties(columns);
	}
	/**
	 * 初始化表格工具类
	 * @param tableViewer
	 * @param tableConfig 
	 * 2014-12-26
	 * @tianming
	 */
	public static void initTable(final TableViewer tableViewer,final List<String[]> tableConfig,int[] idex,EditingSupport supert){
		final Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		String[] columns = new String[tableConfig.size()];
		for(int i = 0;i<tableConfig.size();i++){
			String[] row = tableConfig.get(i);
			final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
			newColumnTableColumn.setWidth(Integer.parseInt(row[0]));
			newColumnTableColumn.setText(row[1]);
			columns[i] = row[2];
		}
	    class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
	        @SuppressWarnings("unchecked")
			public String getColumnText(Object element, int columnIndex) {
	        	try{
	            	if(element instanceof HashMap){
	            		HashMap<String,String> temp = (HashMap<String,String>)element;
	            		String[] row = tableConfig.get(columnIndex);
	            		String text = temp.get(row[2]);
	            		if(text==null){
	            			text = "";
	            		}
	            		if(temp.containsKey("modify_type")&&temp.get("modify_type").startsWith("select")&&columnIndex==2){
	            			String type = temp.get("modify_type");
	            			String attrName = temp.get("attr_name");
	            			if(type.indexOf("#")>-1){
	            				String[] arr = type.split("#");
	            				attrName = arr[1];
	            			}
	            			text = CacheInit.getTextByValue(attrName, text);
	            		}
	            		return text;
	            	}
	        	}catch(Exception e){ 
	        		e.printStackTrace();
	        	}
	            return null;
	        }
	        public Image getColumnImage(Object element, int columnIndex) {
	            return null;
	        }
	    }
	    for(int i = 0;i<idex.length;i++){
		    TableColumn valColumn = table.getColumn(idex[i]);
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer,valColumn);
		    tableViewerColumn.setEditingSupport(supert);
	    }
	    tableViewer.setContentProvider(new ContentProvider());
	    tableViewer.setLabelProvider(new TableLabelProvider());
	    tableViewer.setColumnProperties(columns);
	}
	/**
	 * 初始化表格工具类
	 * @param tableViewer
	 * @param tableConfig 
	 * 2014-12-26
	 * @tianming
	 */
	public static void initTable(final TableViewer tableViewer,final List<String[]> tableConfig,final MenuDef[] menus,final MyCellModifier cellModify){
		final Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		String[] columns = new String[tableConfig.size()];
		for(int i = 0;i<tableConfig.size();i++){
			String[] row = tableConfig.get(i);
			final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
			newColumnTableColumn.setWidth(Integer.parseInt(row[0]));
			newColumnTableColumn.setText(row[1]);
			columns[i] = row[2];
		}
	    class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
	        @SuppressWarnings("unchecked")
			public String getColumnText(Object element, int columnIndex) {
	        	try{
	            	if(element instanceof HashMap){
	            		HashMap<String,String> temp = (HashMap<String,String>)element;
	            		String[] row = tableConfig.get(columnIndex);
	            		if(cellModify!=null){
	            			return cellModify.render(row[2], temp.get(row[2]));
	            		}
	            		return String.valueOf(temp.get(row[2]));
	            	}
	        	}catch(Exception e){ e.printStackTrace();}
	            return null;
	        }
	        public Image getColumnImage(Object element, int columnIndex) {
	            return null;
	        }
	    }
	    tableViewer.setContentProvider(new ContentProvider());
	    tableViewer.setLabelProvider(new TableLabelProvider());
	    tableViewer.setColumnProperties(columns);
	    //右键菜单
		table.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (e.button == 3) {
					try{
						Menu menu = new Menu(tableViewer.getTable());
						tableViewer.getTable().setMenu(menu);
						if(menus!=null){
							for(int i = 0;i<menus.length;i++){
								if(menus[i]!=null){
									MenuItem item3 = new MenuItem(menu, SWT.PUSH);
									item3.setText(menus[i].text);
									menus[i].tableViewer = tableViewer;
									item3.addListener(SWT.Selection, menus[i]);
								}
							}
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		});
	}
}
class ContentProvider implements IStructuredContentProvider {
    @SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
    	try{
            if(inputElement instanceof List){
                return ((List)inputElement).toArray();
            }else{
                return new Object[0];
            }
    	}catch(Exception e){ e.printStackTrace();}
    	return new Object[0];
    }
    public void dispose() {
    }
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}