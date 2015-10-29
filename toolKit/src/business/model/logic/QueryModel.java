package business.model.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import business.AbstractModel;

@SuppressWarnings("serial")
public class QueryModel extends AbstractModel {
	private String display = null;
	private int queryType = 0;
	private String tab_name = "";
	private int isFenye = 0;
	private String sql = "";
	private String order = "";
	private int transFlag = 0;
	private List<HashMap<String,String>> queryConditon =  new ArrayList<HashMap<String,String>>();//动态查询参数
	private List<HashMap<String,String>> paramList2 =  new ArrayList<HashMap<String,String>>();//预编译参数
	
	public QueryModel() {
		this.setDisplay("列表查询");
		this.setIcon("database_search.png");
	}
	@Override
	public List<HashMap<String, String>> getInParamList() {
		List<HashMap<String,String>> params = new ArrayList<HashMap<String,String>>();
		return params;
	}
	@Override
	public String toCode(Map<String, Object> root, int offset) throws Exception {
		HashMap<String,Object> obj = new HashMap<String,Object>();
		computeNextStep(obj);
		obj.put("queryType", queryType);
		obj.put("transFlag", transFlag);
		obj.put("tab_name", tab_name);
		obj.put("isFenye", isFenye);
		obj.put("sql", sql);
		if(order==null||"".equals(order)){
			obj.put("order", "");
		}else{
			obj.put("order", " order by "+order);
		}
		
		obj.put("queryConditon", queryConditon);
		obj.put("paramList2", paramList2);
		return getCodeByTemplate(obj);
	}
	public void writeInXml (Element element){
		element.addAttribute("display", display);
		element.addAttribute("queryType", String.valueOf(queryType));
		element.addAttribute("transFlag", String.valueOf(transFlag));
		element.addAttribute("isFenye", String.valueOf(isFenye));
		element.addAttribute("tab_name", tab_name);
		element.addAttribute("sql", sql);
		element.addAttribute("order", order);
		this.writeList(element, queryConditon, "queryConditon");
		this.writeList(element, paramList2, "paramList2");
	}
	public void readFromXml(Element element){
		this.display = element.attributeValue("display");
		this.queryType =Integer.parseInt(element.attributeValue("queryType"));
		String flag = element.attributeValue("transFlag");
		if(flag!=null){
			this.transFlag =Integer.parseInt(element.attributeValue("transFlag"));
		}
		this.isFenye =Integer.parseInt(element.attributeValue("isFenye"));
		this.tab_name = element.attributeValue("tab_name");
		this.sql = element.attributeValue("sql");
		this.order = element.attributeValue("order");
		queryConditon = this.readList(element, "queryConditon");
		paramList2 = this.readList(element, "paramList2");
	}
	
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public int getQueryType() {
		return queryType;
	}
	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}
	public String getTab_name() {
		return tab_name;
	}
	public void setTab_name(String tabName) {
		tab_name = tabName;
	}
	public int getIsFenye() {
		return isFenye;
	}
	public void setIsFenye(int isFenye) {
		this.isFenye = isFenye;
	}
 
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<HashMap<String, String>> getParamList2() {
		return paramList2;
	}
	public void setParamList2(List<HashMap<String, String>> paramList2) {
		this.paramList2 = paramList2;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public List<HashMap<String, String>> getQueryConditon() {
		return queryConditon;
	}
	public void setQueryConditon(List<HashMap<String, String>> queryConditon) {
		this.queryConditon = queryConditon;
	}
	@Override
	public List<HashMap<String, String>> getParamList() {
		List<HashMap<String,String>> paramList = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> row = new HashMap<String,String>();
		row.put("paramName", "ipage");
		row.put("paramType", "com.utils.entity.Ipage");
		paramList.add(row);
		return paramList;
	}
	public int getTransFlag() {
		return transFlag;
	}
	public void setTransFlag(int transFlag) {
		this.transFlag = transFlag;
	}
	
	
}
