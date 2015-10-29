package business.model.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import business.AbstractModel;

@SuppressWarnings("serial")
public class PackageVariableModel extends AbstractModel {
	private int paramType = 0;
	private String paramName = "";
	private List<HashMap<String,String>> values = null;
	private String tableName = "";
	public PackageVariableModel(){
		this.setDisplay("封装变量");
		this.setIcon("variableSet.png");
	}
	@Override
	public String toCode(Map<String, Object> root, int offset) throws Exception {
		HashMap<String,Object> obj = new HashMap<String,Object>();
		computeNextStep(obj);
		obj.put("paramType", paramType);
		obj.put("paramName", paramName);
		for(int i = 0;i<values.size();i++){
			if(!values.get(i).containsKey("value")){
				values.get(i).put("value","");
			}
		}
		obj.put("values", values);
		obj.put("tableName", tableName.toLowerCase());
		return this.getCodeByTemplate(obj);
	}
	/**
	 * 功能：首字母大写
	 * @param needHead
	 * @return
	 */
	private String firstUpper(String needHead) {
		return needHead.substring(0,1).toUpperCase()+needHead.substring(1).toLowerCase();
	}
	public int getParamType() {
		return paramType;
	}
	public void setParamType(int paramType) {
		this.paramType = paramType;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public List<HashMap<String,String>> getValues() {
		return values;
	}
	public void setValues(List<HashMap<String,String>> values) {
		this.values = values;
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public void writeInXml (Element element){
		element.addAttribute("tableName", tableName);
		element.addAttribute("paramName", paramName);
		element.addAttribute("paramType", String.valueOf(paramType));
		writeList(element,this.getValues(),"values");
	}
	public void readFromXml(Element element){
		this.values = readList(element, "values");
		String paramTypeStr = element.attributeValue("paramType");
		if(paramTypeStr!=null&&!"".equals(paramTypeStr)){
			this.paramType = Integer.parseInt(paramTypeStr);
		}
		this.paramName = element.attributeValue("paramName");
		this.tableName = element.attributeValue("tableName");
		if(this.tableName==null){
			this.tableName = "";
		}
	}
	@Override
	public List<HashMap<String, String>> getParamList() {
		List<HashMap<String,String>> paramList = new ArrayList<HashMap<String,String>>();
		if(this.getParamName()!=null&&this.getParamName().length()>0){
			HashMap<String,String> row = new HashMap<String,String>();
			row.put("paramName", this.getParamName());
			row.put("paramType", "com.dhcc.financemanage.entity."+firstUpper(this.getTableName())+"Entity");
			paramList.add(row);
		}
		return paramList;
	}
}
