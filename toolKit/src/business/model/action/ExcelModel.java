package business.model.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import business.AbstractModel;

@SuppressWarnings("serial")
public class ExcelModel extends AbstractModel {
	private String path = "";
	private String returnType = "0";
	private String fileName = "";
	private List<HashMap<String,String>> writeParam = new ArrayList<HashMap<String,String>>();
	private List<HashMap<String,String>> varList = new ArrayList<HashMap<String,String>>();
	public ExcelModel(){
		this.setDisplay("导出excel");
		this.setIcon("excel.png");
	}
	
	@Override
	public List<HashMap<String, String>> getInParamList() {
		// TODO Auto-generated method stub
		return this.writeParam;
	}
	@Override
	public String toCode(Map<String, Object> root,int offset) throws Exception{
		HashMap<String,Object> obj = new HashMap<String,Object>();
		computeNextStep(obj);
		obj.put("writeParam", writeParam);
		if(varList==null){
			varList =  new ArrayList<HashMap<String,String>>();
		}
		obj.put("varList", varList);
		obj.put("returnType", returnType);
		obj.put("path", path);
		obj.put("fileName", fileName);
		return getCodeByTemplate(obj);
	}
	public void writeInXml (Element element){
		element.addAttribute("path", path);
		element.addAttribute("fileName", fileName);
		element.addAttribute("display", this.getDisplay());
		element.addAttribute("returnType", returnType);
		this.writeList(element, writeParam, "params");
		this.writeList(element, varList, "varList");
	}
	public void readFromXml(Element element){
		this.path = element.attributeValue("path", path);
		this.fileName = element.attributeValue("fileName", fileName);
		this.setDisplay(element.attributeValue("display"));
		this.returnType = element.attributeValue("returnType");
		writeParam = this.readList(element, "params");
		varList = this.readList(element,"varList");
	}
	
 	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<HashMap<String, String>> getWriteParam() {
		return writeParam;
	}

	public void setWriteParam(List<HashMap<String, String>> writeParam) {
		this.writeParam = writeParam;
	}
	
	public List<HashMap<String, String>> getVarList() {
		return varList;
	}

	public void setVarList(List<HashMap<String, String>> varList) {
		this.varList = varList;
	}

	@Override
	public List<HashMap<String, String>> getParamList() {
		return null;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


}
