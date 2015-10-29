package business.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import business.AbstractModel;

@SuppressWarnings("serial")
public class ContentsModel extends ContainerModel {
	public int count = 0;
	public String projectPath = "";
	public AbstractModel getModelById(String id){
		for(int i = 0;i<this.getChildren().size();i++){
			AbstractModel model = this.getChildren().get(i);
			if(id.equals(model.getId())){
				return model;
			}
		}
		return null;
	}

	@Override
	public String toCode(Map<String, Object> root,int offset) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public List<HashMap<String, String>> getParamList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public AbstractModel getModelByType(Class clazz){
		for(int i = 0;i<this.getChildren().size();i++){
			if(this.getChildren().get(i).getClass()==clazz){
				return this.getChildren().get(i);
			}
		}
		return null;
	}
}
