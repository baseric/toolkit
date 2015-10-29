package business.model.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import util.CacheInit;

import business.AbstractModel;

@SuppressWarnings("serial")
public class ValidateModel extends AbstractModel {
	private List<HashMap<String,String>> parameter =  new ArrayList<HashMap<String,String>>();
	public ValidateModel(){
		this.setDisplay("数据校验");
		this.setIcon("validation.png");
	}

	@Override
	public String toCode(Map<String,Object> root,int offset){
		HashMap<String,Object> obj = new HashMap<String,Object>();
		List<Map<String,String>> options = CacheInit.getValue("validateType");
		List<HashMap<String,String>> temp = new ArrayList<HashMap<String,String>>();
		for(int i = 0;i<parameter.size();i++){
			HashMap<String,String> row = new HashMap<String,String>();
			HashMap<String,String> row_ = parameter.get(i);
			row.put("paramName",row_.get("paramName"));
			String value = "";
			for(int j = 0;j<options.size();j++){
				Map<String,String> opt = options.get(j);
				if(row_.get("validateType").equals(opt.get("optiontext"))){
					value = opt.get("optionvalue");
					break;
				}
			}
			row.put("validateType",value);
			row.put("validateParam",row_.get("validateParam"));
			temp.add(row);
		}
		computeNextStep(obj);
		obj.put("parameter", temp);
		return getCodeByTemplate(obj);
	}
	public void writeInXml (Element element){
		element.addAttribute("display", display);
		writeList(element,this.getParameter(),"parameter");
	}
	public void readFromXml(Element element){
		this.display = element.attributeValue("display");
		this.parameter = readList(element, "parameter");
	}
	@Override
	public List<HashMap<String, String>> getParamList() {	
		List<HashMap<String,String>> paramList = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> row = new HashMap<String,String>();
		row.put("paramName", "validateResult");
		row.put("paramType", "boolean");
		paramList.add(row);
		row = new HashMap<String,String>();
		row.put("paramName", "validateError");
		row.put("paramType", "String");
		paramList.add(row);
		row = new HashMap<String,String>();
		row.put("paramName", "validateErrorMsg");
		row.put("paramType", "String");
		paramList.add(row);
		return paramList;
	}

	public List<HashMap<String, String>> getParameter() {
		return parameter;
	}

	public void setParameter(List<HashMap<String, String>> parameter) {
		this.parameter = parameter;
	}
	
}
