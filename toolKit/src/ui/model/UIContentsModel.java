package ui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ui.UIAbstractModel;
import ui.model.abstractModel.ContainerModel;
import ui.model.forms.FormElementModel;
import ui.model.liger.FormFieldModel;

@SuppressWarnings("serial")
public class UIContentsModel extends ContainerModel {
	private List<String> js = new ArrayList<String>();
	public StringBuffer javascriptCode = new StringBuffer();
	public UIContentsModel(String modelName) {
		super(modelName);
	}
	public List<String> getJs() {
		return js;
	}

	public void setJs(List<String> js) {
		this.js = js;
	}
	/**
	 * 获取页面所有的表单类型
	 * @param list
	 * @param container
	 * 2015-4-26
	 * @tianming
	 */
	public void getFormUIComponentID(List<HashMap<String,String>> list,ContainerModel container){
		List<UIAbstractModel> children = container.getChildren();
		for(int i = 0 ;i<children.size();i++){
			UIAbstractModel model = children.get(i);
			if(model instanceof FormElementModel){
				if("LabelModel".equals(model.getModelName())||"FormButtonModel".equals(model.getModelName())||"GridButtonModel".equals(model.getModelName())){
					continue;
				}
				String name = model.val("name");
				if("TblModel".equals(model.getModelName())){
					name = model.val("property");
				}
				HashMap<String,String> formElement = new HashMap<String,String>();
				if(model.val("id")==null||model.val("id").length()==0) continue;
				formElement.put("id",model.val("id"));
				formElement.put("name",name);
				formElement.put("type",model.getModelName());
				list.add(formElement);
			}else if(model instanceof FormFieldModel){
				HashMap<String,String> formElement = new HashMap<String,String>();
				formElement.put("id",model.val("name"));
				formElement.put("name",model.val("name"));
				formElement.put("type","String");
				list.add(formElement);
			}else if(model instanceof ContainerModel){
				getFormUIComponentID(list,(ContainerModel) model);
			}
		}
	}
	/**
	 * 获取页面所有的组件信息
	 * @param list
	 * @param container
	 * 2015-4-26
	 * @tianming
	 */
	public void getAllUIComponentID(List<HashMap<String,String>> list,ContainerModel container){
		List<UIAbstractModel> children = container.getChildren();
		for(int i = 0 ;i<children.size();i++){
			UIAbstractModel model = children.get(i);
			String name = model.val("name");
			if("TblModel".equals(model.getModelName())){
				name = model.val("property");
			}
			HashMap<String,String> formElement = new HashMap<String,String>();
			if(model.val("id")!=null&&model.val("id").length()>0){
				formElement.put("id",model.val("id"));
				formElement.put("name",name);
				formElement.put("type",model.getModelName());
				list.add(formElement);
			}
			if(model instanceof ContainerModel){
				getFormUIComponentID(list,(ContainerModel) model);
			}
		}
	}
	
	public UIAbstractModel getModelByName(ContainerModel container , String modeName){
		for(int i = 0;i<container.getChildren().size();i++){
			if(container.getChildren().get(i).getModelName().equals(modeName)){
				return container.getChildren().get(i);
			}else if(container.getChildren().get(i) instanceof ContainerModel){
				ContainerModel container2 = (ContainerModel)container.getChildren().get(i);
				if(container2.getChildren().size()>0){
					UIAbstractModel model = getModelByName(container2,modeName);
					if(model!=null){
						return model;
					}
				}
			}
		}
		return null;
	}
}
