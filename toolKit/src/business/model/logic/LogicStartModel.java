package business.model.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import business.AbstractModel;

@SuppressWarnings("serial")
public class LogicStartModel extends AbstractModel {
	private List<HashMap<String,String>> parameter =  new ArrayList<HashMap<String,String>>();
	public LogicStartModel(){
		this.setDisplay("开始");
		this.setIcon("start.png");
	}
	
	public String toCode(Map<String,Object> root,int offset){
		HashMap<String,Object> obj = new HashMap<String,Object>();
		computeNextStep(obj);
		obj.put("parameter", parameter);
		return getCodeByTemplate(obj);
	}
	public List<HashMap<String, String>> getParameter() {
		return parameter;
	}
	public void setParameter(List<HashMap<String, String>> parameter) {
		this.parameter = parameter;
	}
	public void writeInXml (Element element){
		writeList(element,this.getParameter(),"parameter");
	}
	public void readFromXml(Element element){
		this.parameter = readList(element, "parameter");
	}

	@Override
	public List<HashMap<String, String>> getParamList() {
		// TODO Auto-generated method stub
		return parameter;
	}
}
