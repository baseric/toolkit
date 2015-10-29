package ui.dialog.tablemodify.cellEditor.dialog;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import ui.UIAbstractModel;
import ui.dialog.tablemodify.cellEditor.MyComboBoxCellEditor;
import ui.dialog.tablemodify.cellEditor.SelectUICompCellEditor;
import ui.dialog.tablemodify.cellEditor.UrlDialogCellEditor;
import util.CacheInit;
/**
 *  属性视图使用
 * @author Administrator
 *
 */
public class MehtodEditingSupport extends EditingSupport {
	private CellEditor[] editors;
	private TableViewer viewer;
	private UIAbstractModel model;
	public MehtodEditingSupport(TableViewer viewer,UIAbstractModel model) {
		super(viewer);
		this.viewer = viewer;
		this.model = model;
		editors = new CellEditor[2];
		editors[0] = new MyComboBoxCellEditor(viewer.getTable(), new String[0]);
		editors[1] = new TextCellEditor(viewer.getTable());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected CellEditor getCellEditor(Object element) {
		try{
			HashMap map = (HashMap)element;
			String modify_type = CacheInit.getValueByText("modify_type",(String)map.get("modify_type"));
			if("comp_id".equals(modify_type)){
				return new SelectUICompCellEditor(viewer.getTable(),model, map);
			}else if("url".equals(modify_type)){
				return new UrlDialogCellEditor(viewer.getTable(),model, map);
			}else{
				return editors[1];
			}
		}catch(Exception e){ e.printStackTrace();}
		return null;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		HashMap<String,String> map = (HashMap<String,String>)element;
		Object value = map.get("value");
		if(value==null){
			value = "";
		}else{
		}
		if("select".equals(map.get("modify_type"))){
			value = CacheInit.getTextByValue(map.get("attr_name"),String.valueOf(value));
		}
		
		return value;
	}

	@SuppressWarnings("unchecked")
	protected void setValue(Object element, Object value) {
		  try {
			Map<String,String> map = (Map<String,String>)element;
			  if("select".equals(map.get("modify_type"))){
				  map.put("value", CacheInit.getValueByText(map.get("attr_name"),String.valueOf(value)));
			  }else{
				  map.put("value",String.valueOf(value));
			  }
			  UIAbstractModel model = (UIAbstractModel)viewer.getData("model");
			  if(model!=null){
				  model.firePropertyChange("refresh", null, null);
			  }
			  viewer.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


