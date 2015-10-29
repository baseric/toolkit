package business.model.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import business.AbstractModel;

@SuppressWarnings("serial")
public class EndModel extends AbstractModel {
	private List<HashMap<String,String>> writeParam =  new ArrayList<HashMap<String,String>>();
	public EndModel(){
		this.setDisplay("结束");
		this.setIcon("end.png");
	}
	@Override
	public List<HashMap<String, String>> getInParamList() {
		// TODO Auto-generated method stub
		return this.writeParam;
	}
	@Override
	public String toCode(Map<String, Object> root,int offset) throws Exception {
		HashMap<String,Object> obj = new HashMap<String,Object>();
		computeNextStep(obj);
		obj.put("writeParam", writeParam);
		return getCodeByTemplate(obj);
	}
	public void writeInXml (Element element){
		element.addAttribute("display", display);
		this.writeList(element, writeParam, "params");
	}
	public void readFromXml(Element element){
		this.display = element.attributeValue("display");
		writeParam = this.readList(element, "params");
	}
	public String getDisplay() {
		return display;
		
	}

	public void setDisplay(String display) {
		this.display = display;
		this.firePropertyChange("refresh", null, null);
	}
	public List<HashMap<String, String>> getWriteParam() {
		return writeParam;
	}

	public void setWriteParam(List<HashMap<String, String>> writeParam) {
		this.writeParam = writeParam;
	}
	@Override
	public List<HashMap<String, String>> getParamList() {
		// TODO Auto-generated method stub
		return writeParam;
	}
}
