package business.model.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import business.AbstractModel;

@SuppressWarnings("serial")
public class LogicModel extends AbstractModel {
	private String logicPath = "";
	private List<HashMap<String,String>> inList =  new ArrayList<HashMap<String,String>>();
	private List<HashMap<String,String>> outList =  new ArrayList<HashMap<String,String>>();
	public LogicModel(){
		this.setDisplay("业务逻辑");
		this.setIcon("logic.png");
	}

	@Override
	public List<HashMap<String, String>> getInParamList() {
		// TODO Auto-generated method stub
		return inList;
	}

	@Override
	public String toCode(Map<String, Object> root,int offset) throws Exception{
		HashMap<String,Object> obj = new HashMap<String,Object>();
		String temp = logicPath;
		obj.put("id", this.getId());
		if(!"".equals(temp)){
			temp = temp.replace(".logic", "");
			temp = temp.replace("\\",".");
			String[] name = temp.split("\\.");
			String className = name[name.length-1];
			temp = temp.replace("."+className, "");
			obj.put("className", className);
		}
		obj.put("inList", inList);
		obj.put("outList", outList);
		computeNextStep(obj);
		 
		return this.getCodeByTemplate(obj);
	}

	@Override
	public void readFromXml(Element element) {
		this.logicPath = element.attributeValue("logicPath");
		this.setDisplay(element.attributeValue("name"));
		this.inList = readList(element, "in");
		this.outList = readList(element, "out");
	}

	@Override
	public void writeInXml(Element element) {
		element.addAttribute("logicPath", logicPath);
		element.addAttribute("name", this.getDisplay());
		writeList(element, inList, "in");
		writeList(element, outList, "out");
	}

	public String getLogicPath() {
		return logicPath;
	}

	public void setLogicPath(String logicPath) {
		this.logicPath = logicPath;
	}

	public List<HashMap<String, String>> getInList() {
		return inList;
	}

	public void setInList(List<HashMap<String, String>> inList) {
		this.inList = inList;
	}

	public List<HashMap<String, String>> getOutList() {
		return outList;
	}

	public void setOutList(List<HashMap<String, String>> outList) {
		this.outList = outList;
	}

	@Override
	public List<HashMap<String, String>> getParamList() {
		return null;
	}
	
}
