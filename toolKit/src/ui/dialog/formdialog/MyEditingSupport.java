package ui.dialog.formdialog;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import ui.UIAbstractModel;
import ui.dialog.tablemodify.cellEditor.MyComboBoxCellEditor;
import util.CacheInit;
import util.EditorUtil;
/**
 *  属性视图使用
 * @author Administrator
 *
 */
public class MyEditingSupport extends EditingSupport {
	private CellEditor[] editors;
	private TableViewer viewer;
	public MyEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
		editors = new CellEditor[2];
		editors[0] = new MyComboBoxCellEditor(viewer.getTable(), new String[0]);
		editors[1] = new TextCellEditor(viewer.getTable());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected CellEditor getCellEditor(Object element) {
		try{
			HashMap<String,String> map = (HashMap<String,String>)element;
			if(map.get("modify_type")!=null&&map.get("modify_type").startsWith("select")){
				String type = map.get("modify_type");
				String attrName = map.get("attr_name");
				if(type.indexOf("#")>-1){
					String[] arr = type.split("#");
					attrName = arr[1];
				}
				String[] arr = CacheInit.getTextArray(attrName);
				return new MyComboBoxCellEditor(viewer.getTable(), arr);
			}else if("dialog".equals(map.get("modify_type"))){
				UIAbstractModel temp = (UIAbstractModel)viewer.getData("model");
				String className = String.valueOf(map.get("dialog_class"));
				if(className!=null&&!"".equals(className)){
					Class clazz = Class.forName(className);
					Constructor  constructor = clazz.getConstructor(Composite.class,UIAbstractModel.class,HashMap.class);
					return (CellEditor)constructor.newInstance(viewer.getTable(),temp,map);
				}
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

	@SuppressWarnings("unchecked")
	@Override
	protected Object getValue(Object element) {
		HashMap<String,String> map = (HashMap<String,String>)element;
		Object value = map.get("value");
		if(value==null){
			value = "";
		}else{
		}
		if(map.get("modify_type")!=null&&map.get("modify_type").startsWith("select")){
			String type = map.get("modify_type");
			String attrName = map.get("attr_name");
			if(type.indexOf("#")>-1){
				String[] arr = type.split("#");
				attrName = arr[1];
			}
			value = CacheInit.getTextByValue(attrName,String.valueOf(value));
		}
		
		return value;
	}

	@SuppressWarnings("unchecked")
	protected void setValue(Object element, Object value) {
		  try {
			   Map<String,String> map = (Map<String,String>)element;
			   if(map.get("modify_type")!=null&&map.get("modify_type").startsWith("select")){
				  String type = map.get("modify_type");
				  String attrName = map.get("attr_name");
				  if(type.indexOf("#")>-1){
					String[] arr = type.split("#");
					attrName = arr[1];
				  }
				  map.put("value", CacheInit.getValueByText(attrName,String.valueOf(value)));
			  }else{
				  map.put("value",String.valueOf(value));
			  }
			  UIAbstractModel model = (UIAbstractModel)viewer.getData("model");
			  if(model!=null){
				  model.firePropertyChange("refresh", map.get("attr_name"), value);
				  EditorUtil.fireEditorDirty();
			  }
			  viewer.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


