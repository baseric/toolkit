package ui.dialog.formdialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import ui.UIAbstractModel;
import ui.model.forms.TableModel;
import ui.model.liger.FormModel;
import util.BaseInfoUtil;
public class MyCellModifier implements ICellModifier {
	private TableViewer tv = null;
	private List<HashMap<String,String>> toolList = null;
	private List<String> list = null;
	public MyCellModifier(TableViewer tv){
		this.tv = tv;
		toolList = new ArrayList<HashMap<String,String>>();
		BaseInfoUtil util = new BaseInfoUtil();
		List<Map<String,String>> tools = util.readXML("dev_comp_menu");

		for(int i = 0;i<tools.size();i++){
			Map<String,String> item = tools.get(i);
			if("tool".equals(item.get("com_type"))&&("D".equals(item.get("up_comp_name")))){
				String text = item.get("comp_desc");
				String className = item.get("class");
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("text", text);
				map.put("class", className);
				map.put("comp_name", item.get("comp_name"));
				map.put("up_comp_name", item.get("up_comp_name"));
				toolList.add(map);
			}
		}
	}
	@Override
	public boolean canModify(Object arg0, String arg1) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getValue(Object element, String propty) {
		try {
			HashMap<String,String> p = (HashMap<String,String>)element;
			String value = p.get(propty);
			for(int i = 0;i<toolList.size();i++){
				String comp_name = toolList.get(i).get("comp_name");
				if(value.equals(comp_name)){
					return i;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Integer("0");
	}
	public String render(String col_name,String value){
		if("comp_name".equals(col_name)){
			try {
				for(int i = 0;i<toolList.size();i++){
					String comp_name = toolList.get(i).get("comp_name");
					if(value.equals(comp_name)){
						return toolList.get(i).get("text");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("el_express".equals(col_name)){
			if("0".equals(value)){
				return "是";
			}else {
				return "否";
			}
		}
		return value;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void modify(Object element, String property, Object value) {
		 try {
			TableItem item = (TableItem)element;   
			 HashMap<String,String> p = (HashMap<String,String>)item.getData();
			 if("comp_name".equals(property)){
				 String comp_name = toolList.get(Integer.parseInt(String.valueOf(value))).get("comp_name");
				 String clazz = toolList.get(Integer.parseInt(String.valueOf(value))).get("class");
				 p.put(property, comp_name);
				 p.put("class", clazz);
			 }else{
				 p.put(property, String.valueOf(value));
			 }
			 this.tv.update(p, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String[] getTypes(UIAbstractModel model){
		list = new ArrayList<String>();
		for(int i = 0;i<toolList.size();i++){
			if(model instanceof TableModel){
				if("C".equals(toolList.get(i).get("up_comp_name"))){
					list.add(toolList.get(i).get("text"));
				}
			}else if(model instanceof FormModel){
				if("D".equals(toolList.get(i).get("up_comp_name"))){
					list.add(toolList.get(i).get("text"));
				}
			}
		}
		String[] arr = new String[list.size()];
		list.toArray(arr);
		return arr;
	}
	public String  getClassByType(String type){
		for(int i = 0;i<toolList.size();i++){
				if(type.equals(toolList.get(i).get("comp_name"))){
					return toolList.get(i).get("class");
				}
		}
		return null;
	}
}
