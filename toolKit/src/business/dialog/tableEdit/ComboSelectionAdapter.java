package business.dialog.tableEdit;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import business.AbstractModel;
import business.dialog.openResource.OpenUtil;

public class  ComboSelectionAdapter {
	public ComboSelectionAdapter(){
	}
	/**
	 * @param model 当前编辑的数据模型
	 * @param item 下拉选项选择的内容
	 * @param table
	 * 2015-1-15
	 * @tianming
	 */
	@SuppressWarnings("unchecked")
	public void selectAfter(AbstractModel model,String item,TableViewer table,String columnName){
		 if("POJO".equals(item)){
			 	IFile selectFile = OpenUtil.openSearch(model.getFile().getProject(), "src/com/dhcc/financemanage/entity/", "java");
			 	String src = selectFile.getName().replace(".java","");
			 	Table tab = table.getTable();
			 	TableItem[] selectItem = tab.getSelection();
			 	if(selectItem.length>0){
			 		 TableItem data = (TableItem)selectItem[0];   
					 HashMap<String,String> p = (HashMap<String,String>)data.getData();
					 p.put(columnName, "POJO-"+src);
					 table.update(p, null);
			 	}
		 }
	}
}
