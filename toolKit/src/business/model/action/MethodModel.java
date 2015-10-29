package business.model.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import business.AbstractModel;
import business.model.AbstractConnectionModel;
import business.model.LineConnectionModel;

@SuppressWarnings("serial")
public class MethodModel extends AbstractModel {
	private String methodName = "";//方法名
	private String classPath = "";//调用方法类路径
	private String className = "";//调用方法类名
	private String isSpringBean = "0";
	private String comboConTxt = "";//
	private String comboMethodTxt = "";
	private List<HashMap<String, String>> in = new ArrayList<HashMap<String, String>>();//传入参数
	private List<HashMap<String, String>> out = new ArrayList<HashMap<String, String>>();//返回值
	private List<HashMap<String, String>> conParam = new ArrayList<HashMap<String, String>>();//构造方法参数
 
	public MethodModel() {
		this.setDisplay("自定义方法");
		this.setIcon("method.png");
	}
	@Override
	public List<HashMap<String, String>> getInParamList() {
		// TODO Auto-generated method stub
		List<HashMap<String,String>> temp = new ArrayList<HashMap<String, String>>();
		temp.addAll(this.conParam);
		temp.addAll(this.in);
		return temp;
	}
	@Override
	public String toCode(Map<String, Object> root, int offset) throws Exception{
		HashMap<String,Object> obj = new HashMap<String,Object>();
		obj.put("id", this.getId());
		obj.put("classPath",this.getClassPath());
		String[] temp = this.getClassPath().split("\\.");
		String className = temp[temp.length-1];
		obj.put("methodName",this.getMethodName());
		obj.put("isSpringBean",this.getIsSpringBean());
		obj.put("className", className);
		//构造函数参数
		String conParams = "";
		if(conParam!=null){
			for(int i = 0;i<conParam.size();i++){
				conParams +="("+conParam.get(i).get("paramType")+")root.get(\""+conParam.get(i).get("paramName")+"\")";
				if(i!=conParam.size()-1){
					conParams += ",";
				}
			}
		}
		obj.put("conParams", conParams);
		//方法参数
		String methodParams = "";
		if(in!=null){
			for(int i = 0;i<in.size();i++){
				methodParams +="("+in.get(i).get("paramType")+")root.get(\""+in.get(i).get("paramName")+"\")";
				if(i!=in.size()-1){
					methodParams += ",";
				}
			}
		}
		obj.put("methodParams", methodParams);
		//调用方法的返回值
		if (out!=null&&out.size() > 0) {
			HashMap<String, String> returnVaue = out.get(0);
			obj.put("returnType",returnVaue.get("paramType"));
			obj.put("returnParam", returnVaue.get("paramName"));
		}else{
			obj.put("returnType","");
			obj.put("returnParam", "");
		}
		computeNextStep(obj);
		return getCodeByTemplate(obj);
	}
	@Override
	public void readFromXml(Element element) {
		this.classPath = element.attributeValue("methodPath");
		this.className = element.attributeValue("className");
		if(this.className==null||"".equals(this.className)){
			if(this.classPath!=null&&!"".equals(this.classPath)){
				String[] temp = this.classPath.split("\\.");
				if(temp!=null&&temp.length>1){
					this.className = temp[temp.length-1];
				}
			}
		}
		this.display = element.attributeValue("display");
		this.methodName = element.attributeValue("methodName");
		this.comboConTxt = element.attributeValue("comboConTxt");
		this.comboMethodTxt = element.attributeValue("comboMethodTxt");
		this.isSpringBean = element.attributeValue("isSpringBeans");
		if(this.isSpringBean==null||this.isSpringBean.length()==0){
			this.isSpringBean = "0";
		}
		this.in = readList(element, "in");
		this.out = readList(element, "out");
		this.conParam = readList(element, "conParam");
	}

	@Override
	public void writeInXml(Element element) {
		element.addAttribute("methodPath", classPath);
		element.addAttribute("className", className);
		element.addAttribute("display", this.display);
		element.addAttribute("methodName", methodName);
		element.addAttribute("comboConTxt", comboConTxt);
		element.addAttribute("comboMethodTxt", comboMethodTxt);
		element.addAttribute("isSpringBean", isSpringBean);
		writeList(element, in, "in");
		writeList(element, out, "out");
		writeList(element, conParam, "conParam");
	}
	public List<HashMap<String, String>> getConParam() {
		return conParam;
	}
	public void setConParam(List<HashMap<String, String>> conParam) {
		this.conParam = conParam;
	}
	public String getMethodName() {
		return methodName;
	}
	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
		this.firePropertyChange("refresh", null, null);
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public List<HashMap<String, String>> getIn() {
		return in;
	}

	public void setIn(List<HashMap<String, String>> in) {
		this.in = in;
	}

	public List<HashMap<String, String>> getOut() {
		return out;
	}

	public void setOut(List<HashMap<String, String>> out) {
		this.out = out;
		if(out!=null&&out.size()>0){
			HashMap<String,String> returnMap = out.get(0);
			List<AbstractConnectionModel> links = this.getSourceConnection();
			for(int i = 0;i<links.size();i++){
				LineConnectionModel link = (LineConnectionModel)links.get(i);
				link.setParamName(returnMap.get("paramName"));
			}
		}
	}
	public String getComboConTxt() {
		return comboConTxt;
	}
	public void setComboConTxt(String comboConTxt) {
		this.comboConTxt = comboConTxt;
	}
	public String getComboMethodTxt() {
		return comboMethodTxt;
	}
	public void setComboMethodTxt(String comboMethodTxt) {
		this.comboMethodTxt = comboMethodTxt;
	}
	@Override
	public List<HashMap<String, String>> getParamList() {
		// TODO Auto-generated method stub
		return this.getOut();
	}
	public String getIsSpringBean() {
		return isSpringBean;
	}
	public void setIsSpringBean(String isSpringBean) {
		this.isSpringBean = isSpringBean;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
}
