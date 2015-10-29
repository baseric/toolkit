package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import ui.UIAbstractModel;
import ui.editor.WebUIMulitEditor;
import ui.model.UIContentsModel;
import ui.model.abstractModel.ContainerModel;
import business.editor.ActionEditor;
import business.editor.LogicEditor;

public class EditorUtil {
	public static void fireEditorDirty(){
		IEditorPart part=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(part instanceof WebUIMulitEditor){
			WebUIMulitEditor editor = (WebUIMulitEditor)part;
			editor.ui_editor.fireDirty();
		}
		if(part instanceof ActionEditor){
			ActionEditor editor = (ActionEditor)part;
			editor.firDirty();
		}
		if(part instanceof LogicEditor){
			LogicEditor editor = (LogicEditor)part;
			editor.firDirty();
		}
	}
	@SuppressWarnings("deprecation")
	public static List<UIContentsModel> getAllContents(IWorkbenchWindow workbenchWindow){
		IEditorPart[] part=workbenchWindow.getActivePage().getEditors();
		List<UIContentsModel> list = new ArrayList<UIContentsModel>();
		for(int i = 0;i<part.length;i++){
			if(part[i] instanceof WebUIMulitEditor){
				WebUIMulitEditor editor = (WebUIMulitEditor)part[i];
				editor.ui_editor.fireDirty();
				list.add(editor.ui_editor.getContentsModel());
			}
		}
		return list;
	}
	public static void refreshAllContents(IWorkbenchWindow workbenchWindow){
		List<UIContentsModel> list = getAllContents(workbenchWindow);
		for(int i = 0;i<list.size();i++){
			refreshAbstractModel(list.get(i));
		}
	}
	@SuppressWarnings("unchecked")
	public static void refreshAbstractModel(UIAbstractModel model){
		String className = model.getModelName();
		List<Map<String,String>> attributes = new ArrayList<Map<String,String>>();//
		List<Map<String,String>> attributes_old = model.getAttributes();//刷新前的数据
		Map<String,String> values = new HashMap<String,String>();
		for(int i = 0;i<attributes_old.size();i++){
			Map<String,String> attr = attributes_old.get(i);
			values.put(attr.get("attr_name").trim(), attr.get("value"));
		}
		List<Map<String,String>> list = CacheInit.getAttrValue(className);
		if(list!=null){
			for(int i = 0;i<list.size();i++){
				HashMap<String,String> temp = (HashMap<String,String>)list.get(i);
				HashMap<String,String> clone = (HashMap<String,String>)temp.clone();
				if(values.containsKey(clone.get("attr_name").trim())&&"Y".equals(temp.get("disp_yn"))){//如果原来的数据中有这个属性且该属性是显示可编辑的，则将默认值覆盖掉
					clone.put("value", values.get(clone.get("attr_name").trim()));
				}
				attributes.add(clone);
			}
		}
		model.setAttributes(attributes);//更新属性信息
		model.setModelInfo(CacheInit.getModelMap(model.getModelName()));
		if(model instanceof ContainerModel){
			ContainerModel container = (ContainerModel)model;
			for(int i = 0;i<container.getChildren().size();i++){
				refreshAbstractModel(container.getChildren().get(i));
			}
		}
		
	}
}
