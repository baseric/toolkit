package business.model.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import business.AbstractModel;

@SuppressWarnings("serial")
public class StartModel extends AbstractModel {
	public String jsp = "";
	private List<HashMap<String,String>> parameter =  new ArrayList<HashMap<String,String>>();
	private List<HashMap<String,String>> attribute =  new ArrayList<HashMap<String,String>>();
	private List<HashMap<String,String>> session =  new ArrayList<HashMap<String,String>>();
	public StartModel(){
		this.setDisplay("开始");
		this.setIcon("start.png");
	}
	
	public String toCode(Map<String,Object> root,int offset){
		HashMap<String,Object> obj = new HashMap<String,Object>();
		computeNextStep(obj);
		obj.put("parameter", parameter);
		for(int i = 0;i<parameter.size();i++){
			if(parameter.get(i).get("isDecode")==null){
				parameter.get(i).put("isDecode","N");
			}
		}
		obj.put("attribute", attribute);
		obj.put("session", session);
		obj.put("input", jsp);
		return getCodeByTemplate(obj);
	}
	public List<HashMap<String, String>> getParameter() {
		return parameter;
	}
	public void setParameter(List<HashMap<String, String>> parameter) {
		this.parameter = parameter;
	}
	public List<HashMap<String, String>> getAttribute() {
		return attribute;
	}
	public void setAttribute(List<HashMap<String, String>> attribute) {
		this.attribute = attribute;
	}
	public List<HashMap<String, String>> getSession() {
		return session;
	}
	public void setSession(List<HashMap<String, String>> session) {
		this.session = session;
	}
	public void writeInXml (Element element){
		element.addAttribute("jsp", jsp);
		writeList(element,this.getParameter(),"parameter");
		writeList(element,this.getAttribute(),"Attribute");
		writeList(element,this.getSession(),"session");
	}
	public void readFromXml(Element element){
		this.parameter = readList(element, "parameter");
		this.attribute = readList(element,"Attribute");
		this.session = readList(element,"session");
		this.jsp = element.attributeValue("jsp");
	}

	@Override
	public List<HashMap<String, String>> getParamList() {
		List<HashMap<String,String>> paramList = new ArrayList<HashMap<String,String>>();
		paramList.addAll(this.getAttribute());
		paramList.addAll(this.getParameter());
		paramList.addAll(this.getSession());
		
		HashMap<String,String> row = new HashMap<String,String>();
		row.put("paramName", "pageInfo");
		row.put("paramType", "java.util.HashMap");
		paramList.add(row);
		
		row = new HashMap<String,String>();
		row.put("paramName", "request");
		row.put("paramType", "javax.servlet.http.HttpServletRequest");
		paramList.add(row);
		return paramList;
	}
}
