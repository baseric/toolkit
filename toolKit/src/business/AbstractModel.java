package business;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.geometry.Rectangle;

import util.FreemarkerUtil;
import util.Log;
import util.PropertyReader;
import business.model.AbstractConnectionModel;
import business.model.ContentsModel;
import business.model.LineConnectionModel;
import business.model.action.ExcelModel;
import business.model.action.JspModel;
import business.model.action.MethodModel;
import business.model.logic.EndModel;
import exception.ComplierException;

public abstract class AbstractModel implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	private PropertyChangeSupport listener = new PropertyChangeSupport(this);
	private Rectangle rect = null;
	private String id = "id_10000";
	public String display = "";
	private String icon = "";
	public int index = 0;
	private IFile file = null;
	private List<AbstractConnectionModel> sourceConnection = new ArrayList<AbstractConnectionModel>();
	private List<AbstractConnectionModel> targetConnection = new ArrayList<AbstractConnectionModel>();
	public AbstractModel parentModel = null;
	public String getCodeByTemplate(HashMap<String,Object> obj){
		String templateFile = this.getClass().getSimpleName().replace("Model", "")+".ftl";
		PropertyReader reader = new PropertyReader();
		String ftl_path = projectPath()+reader.getPropertyValue("resourcePath")+"/plugin_resource/ftl/action/";
		FreemarkerUtil util = new FreemarkerUtil();
		return util.getStringByFtl(ftl_path, obj,templateFile.toLowerCase());
	}
	public String projectPath(){
		ContentsModel parent = (ContentsModel)parentModel;
		return parent.projectPath;
	}
	/**
	 * 检查变量是否存在
	 * @param list
	 * @throws Exception
	 * 2015-1-23
	 * @tianming
	 */
	public void checkParamExsit(List<HashMap<String,String>> list)throws Exception{
		if(list==null){
			return;
		}
		List<String> params = new ArrayList<String>();
		this.getAllParamName(params);
		for(int i = 0;i<list.size();i++){
			HashMap<String,String> param = list.get(i);
			String paramName = param.get("paramName");
			if(!params.contains(paramName)){
				Log.write(this.getClass().getSimpleName()+" -> "+this.getDisplay()+" -> 变量["+paramName+"]不存在，编译失败");
				this.firePropertyChange("error", null, null);
				throw new ComplierException("");
			}
		}
		if(sourceConnection.size()==0 &&!(this instanceof EndModel)&&!(this instanceof JspModel)&&!(this instanceof ExcelModel)){
			Log.write(this.getClass().getSimpleName()+" -> "+this.getDisplay()+" -> 没有连出线，编译失败");
			this.firePropertyChange("error", null, null);
			throw new ComplierException("");
		}
		if(sourceConnection.size()>0){//有连出线时
			int count = 0;
			for(int i = 0;i<sourceConnection.size();i++){
				LineConnectionModel model = (LineConnectionModel)sourceConnection.get(i);
				if("0".equals(model.getType())){//空条件
					count ++;
				}else if("1".equals(model.getType())){//条件判断
					
				}
			}
			if(count!=1){
				Log.write(this.getClass().getSimpleName()+" -> "+this.getDisplay()+" -> 空条件的连接线有且仅能有一条，编译失败");
				this.firePropertyChange("error", null, null);
				throw new ComplierException("");
			}
		}
	}
	public void addSourceConnection(Object source){
		this.sourceConnection.add((AbstractConnectionModel)source);
		if(this instanceof MethodModel){
			MethodModel model = (MethodModel)this;
			LineConnectionModel line = (LineConnectionModel)source;
			List<HashMap<String,String>> out = model.getOut();
			if(out!=null&&out.size()>0){
				HashMap<String,String> temp = out.get(0);
				line.setParamName(temp.get("paramName"));
			}
			
		}
		this.firePropertyChange("addSource", null, null);
	}
	
	public void importClass(Map<String, Object> root,String classPath){
		if(String.valueOf(root.get("import")).indexOf(classPath)==-1)
			root.put("import", root.get("import")+"\n import "+classPath+";");
	}
	public void addTargetConnection(Object target){
		this.targetConnection.add((AbstractConnectionModel)target);
		this.firePropertyChange("addTarget", null, null);
	}
	public List<AbstractConnectionModel> getSourceConnection() {
		return sourceConnection;
	}
	public List<AbstractConnectionModel> getTargetConnection() {
		return targetConnection;
	}
	public void removeSourceConnection(Object source){
		this.sourceConnection.remove(source);
		this.firePropertyChange("removeSource", null, null);
	}
	public void removeTargetConnection(Object target){
		this.targetConnection.remove(target);
		this.firePropertyChange("removeTarget", null, null);
	}
	public IFile getFile() {
		return file;
	}
	public void setFile(IFile file) {
		this.file = file;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Rectangle getRect() {
		return rect;
	}
	public void setRect(Rectangle rect) {
		this.rect = rect;
		this.firePropertyChange("relocation", null,null);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public void addListener(PropertyChangeListener l){
		listener.addPropertyChangeListener(l);
	}
	public String getDisplay() {
		if(display==null){
			this.display = "";
		}
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
		this.firePropertyChange("refresh", null, null);
	}
	public void deleteListener(PropertyChangeListener l){
		listener.removePropertyChangeListener(l);
	}
	public void firePropertyChange(String propName,Object oldValue,Object newValue){
		listener.firePropertyChange(propName, oldValue, newValue);
	}
 
	public void writeInXml (Element element){
		
	}
	public void readFromXml(Element element){
		
	}
	public abstract String toCode(Map<String,Object> root,int offset) throws Exception;
	
	/**
	 * 将list写入xml
	 * @param element
	 * @param parameter
	 * @param name
	 * 2015-5-9
	 * @tianming
	 */
	public void writeList(Element element,List<HashMap<String,String>> parameter,String name){
		Element inElement = element.addElement(name);
		if(parameter!=null){
			for(int i = 0;i<parameter.size();i++){
				HashMap<String,String> param = parameter.get(i);
				Element paramElement = inElement.addElement("parm");
				Set<String> keys = param.keySet();
				for(String key:keys){
					paramElement.addAttribute(key,String.valueOf(param.get(key)));
				}
			}
		}
	}
	public void writeArray(Element element,String[] parameter,String name){
		Element inElement = element.addElement(name);
		if(parameter!=null){
			for(int i = 0;i<parameter.length;i++){
				String param = parameter[i];
				Element paramElement = inElement.addElement("parm");
				paramElement.addAttribute("value", param);
			}
		}
	}
	@SuppressWarnings("unchecked")
	public Object[] readArray(Element element,String name){
		 Element inElement = element.element(name);
		 List<Element> inList = inElement.elements();
		 List<String> in = new ArrayList<String>();
		 for(int i = 0;i<inList.size();i++){
			 Element e = inList.get(i);
			 in.add(e.attributeValue("value"));
		 }
		 return in.toArray();
	}
	@SuppressWarnings("unchecked")
	public List<HashMap<String,String>> readList(Element element,String name){
		 Element inElement = element.element(name);
		 if(inElement!=null){
			 List<Element> inList = inElement.elements();
			 List<HashMap<String,String>> in = new ArrayList<HashMap<String,String>>();
			 for(int i = 0;i<inList.size();i++){
				 Element e = inList.get(i);
				 HashMap<String,String> param = new HashMap<String,String>();
				 List<Attribute> keys = e.attributes();
				 for(Attribute key:keys){
					param.put(key.getName(), key.getStringValue());
				 }
				 in.add(param);
			 }
			 return in;
		 }
		 return null;
	}
	
	public String getGenMethodName(){
		String methodName = this.getClass().getSimpleName().replace("Model","").toLowerCase();
		return methodName;
	}
	/**
	 * 计算下一步的判断条件
	 * @param obj
	 * 2015-1-5
	 * @tianming
	 */
	public void computeNextStep(HashMap<String,Object> obj){
		List<AbstractConnectionModel> links = this.getSourceConnection();
		String judget = "";
		String blankLine = "";
		for(int i = 0;i<links.size();i++){
			LineConnectionModel link = (LineConnectionModel)links.get(i);
			AbstractModel nextStep = link.getTarget();
			if("0".equals(link.getType())){//找到空条件的连接线
				blankLine = nextStep.getId().substring(3);
			}else{
				if("1".equals(link.getType())){//判断条件
					String opt = link.getOpt();
					String valueType = link.getValueType();
					if("0".equals(valueType)){//常量
						judget += "\t\t\t\t\t\t\tif(com.base.ALogicUtil.checkForJudget(root.get(\""+link.getParamName()+"\"),\""+opt+"\", \""+link.getParamName2()+"\")){\n" +
								  "\t\t\t\t\t\t\t	root.put(\"nextStepId\", "+nextStep.getId().substring(3)+" );\n" +
								  "\t\t\t\t\t\t\t}\n";
					}else{//变量
						judget += "\t\t\t\t\t\t\tif(com.base.ALogicUtil.checkForJudget(root.get(\""+link.getParamName()+"\"),\""+opt+"\", root.get(\""+link.getParamName2()+"\"))){\n" +
						  "\t\t\t\t\t\t\t	root.put(\"nextStepId\", "+nextStep.getId().substring(3)+" );\n" +
						  "\t\t\t\t\t\t\t}\n";
					}
				}else if("3".equals(link.getType())){//jexl表达式
					judget += "\t\t\t\t\t\t\tif(com.base.ALogicUtil.executeExpress(\""+link.getJavaEl()+"\",root)){\n" +
							  "\t\t\t\t\t\t\t	root.put(\"nextStepId\", "+nextStep.getId().substring(3)+" );\n" +
							  "\t\t\t\t\t\t\t}\n";
				}

			}
		}
		judget = "root.put(\"nextStepId\","+blankLine+");\n"+judget;
		obj.put("lines", judget);
		obj.put("display", this.getDisplay());
		obj.put("id", this.getId());
	}
	/**
	 * //获取当前节点可提供的变量
	 * @return
	 * 2015-7-3
	 * @tianming
	 */
	public abstract List<HashMap<String,String>> getParamList();
	/**
	 * 获取当前节点使用的变量
	 * @return
	 * 2015-7-3
	 * @tianming
	 */
	public List<HashMap<String,String>> getInParamList(){
		return null;
	}
	/**
	 * 递归向上查找可用的变量
	 */
	public void getParamInfo(HashMap<String,String> paramAll,AbstractModel source){
		List<HashMap<String,String>> params = source.getParamList();
		if(params!=null){
			for(int i = 0;i<params.size();i++){
				HashMap<String,String> param = params.get(i);
				paramAll.put(param.get("paramName"),param.get("paramType"));
			}
		}
		for(int i = 0;i<source.targetConnection.size();i++){
			AbstractConnectionModel line = source.targetConnection.get(i);
			AbstractModel model = line.getSource();
			getParamInfo(paramAll,model);
		}
	}
	public List<String> getAllParamName(HashMap<String,String> paramAll){
		List<String> list = new ArrayList<String>();
		Set<String> paramNames = paramAll.keySet();
		for(String name:paramNames){
			list.add(name);
		}
		return list;
	}
	public void getAllParamName(List<String> list){
		HashMap<String,String> paramAll = new HashMap<String,String>();
		this.getParamInfo(paramAll, this);
		Set<String> paramNames = paramAll.keySet();
		for(String name:paramNames){
			list.add(name);
		}
	}
	/**
	 * 表格中选择完变量名称后设置变量类型
	 * @param p
	 * @param property
	 * @param value
	 * 2015-4-28
	 * @tianming
	 */
	public void setParamType(HashMap<String,String> p,String property,Object value){
		 AbstractModel model = this;
		 HashMap<String,String> params = new HashMap<String,String>();
		 this.getParamInfo(params,this);
		 //根据变量名设置变量的类型
		 if(model!=null&&("paramName".equals(property))){
			 String type = params.get(String.valueOf(value));
			 p.put("paramType", type);
		 }
	}
}
