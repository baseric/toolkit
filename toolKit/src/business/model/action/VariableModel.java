package business.model.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import business.AbstractModel;

@SuppressWarnings("serial")
public class VariableModel extends AbstractModel {
	private List<HashMap<String,String>> varList = new ArrayList<HashMap<String,String>>();
	
 	public List<HashMap<String, String>> getVarList() {
		return varList;
	}

	public void setVarList(List<HashMap<String, String>> varList) {
		this.varList = varList;
	}

	public VariableModel(){
		this.setDisplay("变量设置");
		this.setIcon("variableSet.png");
	}
	public void writeInXml (Element element){
		element.addAttribute("display", this.getDisplay());
		writeList(element, varList, "varList");
	}
	public void readFromXml(Element element){
		this.setDisplay(element.attributeValue("display"));
		this.varList = readList(element, "varList");
	}
	@SuppressWarnings("unchecked")
	@Override
	public String toCode(Map<String, Object> root,int offset) {
		HashMap<String,Object> obj = new HashMap<String,Object>();
		List<HashMap<String,String>> returnList = new ArrayList<HashMap<String,String>>();
		for(int i = 0;i<varList.size();i++){
			HashMap<String,String> param = varList.get(i);
			HashMap<String,String> code = (HashMap<String,String>)param.clone();
			if("String".equals(param.get("paramType"))&&"Y".equals(param.get("isArray"))){
				String paramValue = param.get("paramValue");
				paramValue = paramValue.replace("[", "\"").replace("]", "\"").replace(",", "\",\"").replace(" ", "");
				code.put("paramValue",paramValue);
			}
			returnList.add(code);
		}
		
		obj.put("varList", returnList);
		computeNextStep(obj);
		return getCodeByTemplate(obj);
	}

	@Override
	public List<HashMap<String, String>> getParamList() {
		// TODO Auto-generated method stub
		return this.varList;
	}
}
