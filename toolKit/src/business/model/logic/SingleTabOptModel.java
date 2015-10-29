package business.model.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import util.CacheInit;
import business.AbstractModel;

@SuppressWarnings("serial")
public class SingleTabOptModel extends AbstractModel {
	private String paramName = "";
	private int optType = 0;
	private String tab_name = "";
	private String entityName = "";
	private String entityName2 = "";
	private String sql = "";
	private List<HashMap<String,String>> ybyParam =  new ArrayList<HashMap<String,String>>();//预编译参数
	private List<HashMap<String,String>> paramList2 =  new ArrayList<HashMap<String,String>>();//列参数
	
	public SingleTabOptModel() {
		this.setDisplay("单表操作");
		this.setIcon("database_opt.png");
	}
	/**
	 * 功能：首字母大写
	 * @param needHead
	 * @return
	 */
	private String firstUpper(String needHead) {
		return needHead.substring(0,1).toUpperCase()+needHead.substring(1).toLowerCase();
	}

	@Override
	public String toCode(Map<String, Object> root, int offset) throws Exception {
		HashMap<String,Object> obj = new HashMap<String,Object>();
		computeNextStep(obj);
		String optype = CacheInit.getValueByIndex("singleTabOpt_type", optType);
		obj.put("paramName", paramName);
		obj.put("optype", optype);
		obj.put("tab_name", firstUpper(tab_name));
		obj.put("entityName", entityName);
		obj.put("entityName2", entityName2);
		obj.put("sql", sql);
		obj.put("ybyParam", ybyParam);
		obj.put("paramList2", paramList2);
		
		return getCodeByTemplate(obj);
	}
	public void writeInXml (Element element){
		element.addAttribute("display", display);
		element.addAttribute("queryType", String.valueOf(optType));
		element.addAttribute("tab_name", tab_name);
		element.addAttribute("sql", sql);
		
		element.addAttribute("paramName", paramName);
		element.addAttribute("entityName", entityName);
		element.addAttribute("entityName2", entityName2);
		
		this.writeList(element, ybyParam, "paramList");
		this.writeList(element, paramList2, "paramList2");
	}
	public void readFromXml(Element element){
		this.display = element.attributeValue("display");
		this.optType =Integer.parseInt(element.attributeValue("queryType"));
		this.tab_name = element.attributeValue("tab_name");
		this.sql = element.attributeValue("sql");
		
		this.paramName = element.attributeValue("paramName");
		this.entityName = element.attributeValue("entityName");
		this.entityName2 = element.attributeValue("entityName2");
		
		ybyParam = this.readList(element, "paramList");
		paramList2 = this.readList(element, "paramList2");
	}
 
	public int getOptType() {
		return optType;
	}
	public void setOptType(int optType) {
		this.optType = optType;
	}
	public String getTab_name() {
		return tab_name;
	}
	public void setTab_name(String tabName) {
		tab_name = tabName;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<HashMap<String, String>> getYbyParam() {
		return ybyParam;
	}
	public void setYbyParam(List<HashMap<String, String>> ybyParam) {
		this.ybyParam = ybyParam;
	}
	public List<HashMap<String, String>> getParamList2() {
		return paramList2;
	}
	public void setParamList2(List<HashMap<String, String>> paramList2) {
		this.paramList2 = paramList2;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getEntityName2() {
		return entityName2;
	}
	public void setEntityName2(String entityName2) {
		this.entityName2 = entityName2;
	}
	@Override
	public List<HashMap<String,String>> getParamList() {
		List<HashMap<String,String>> paramList = new ArrayList<HashMap<String,String>>();
		String optype = CacheInit.getValueByIndex("singleTabOpt_type", optType);
		if(this.getParamName()!=null&&this.getParamName().length()>0){
			HashMap<String,String> row = new HashMap<String,String>();
			row.put("paramName", this.getParamName());
			if("insert".equals(optype)||"update".equals(optype)||"delete".equals(optype)){
				row.put("paramType", "String");
			}else{
				row.put("paramType", "com.dhcc.financemanage.entity."+firstUpper(this.getTab_name())+"Entity");
			}
			paramList.add(row);
		}
		HashMap<String,String> row = new HashMap<String,String>();
		row.put("paramName", "return_code");
		row.put("paramType", "String");
		paramList.add(row);
		row = new HashMap<String,String>();
		row.put("paramName", "err_message");
		row.put("paramType", "String");
		paramList.add(row);
		return paramList;
	}
	@Override
	public List<HashMap<String, String>> getInParamList() {
		List<HashMap<String,String>> params = new ArrayList<HashMap<String,String>>();
		return params;
	}
}
