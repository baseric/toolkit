package ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;

import ui.model.DefComponentModel;
import ui.model.UIContentsModel;
import ui.model.abstractModel.ContainerModel;
import ui.model.grid.ColumnModel;
import ui.model.layout.LeftModel;
import ui.model.layout.TopModel;
import util.CacheInit;
import util.StringBufferUtil;
import util.TemplateUtil;
import exception.ComplierException;

public abstract class UIAbstractModel implements Cloneable, Serializable{
	private static final long serialVersionUID = 1L;
	public UIAbstractModel parentModel = null;
	private PropertyChangeSupport listener = new PropertyChangeSupport(this);
	private List<Map<String,String>> attributes = null;
	private Map<String,String> modelInfo = null;
	private IFile file = null;
	public String x = "0";
	public String y = "0";
	private String width = "0";
	private String height = "0";
	private String modelName = "";
	public UIAbstractModel(String modelName){
		this.modelName = modelName;
		this.getModelInfo();
		this.getAttributes();
	}
	
	public IFile getFile() {
		return file;
	}

	public void setFile(IFile file) {
		this.file = file;
	}

	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName){
		this.modelName = modelName;
	}
	public Map<String, String> getModelInfo() {
		if(modelInfo==null){
			modelInfo = CacheInit.getModelMap(modelName);
			if(modelInfo!=null){
				if(modelInfo.get("width")!=null){
					width = modelInfo.get("width");
				}
				if(modelInfo.get("height")!=null){
					height = modelInfo.get("height");
				}
			}
		}
		return modelInfo;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getAttributes() {
		if(attributes==null){
			attributes =  new ArrayList<Map<String,String>>();
			List<Map<String,String>> list = CacheInit.getAttrValue(modelName);
			if(list!=null){
				for(int i = 0;i<list.size();i++){
					HashMap<String,String> temp = (HashMap<String,String>)list.get(i);
					HashMap<String,String> clone = (HashMap<String,String>)temp.clone();
					attributes.add(clone);
				}
			}
		}
		return attributes;
	}
	public Map<String,String> getAttrNameAndValue(){
		List<Map<String,String>> list = this.getAttributes();
		Map<String,String> values = new HashMap<String,String>();
		for(int i = 0;i<list.size();i++){
			Map<String,String> temp = list.get(i);
			values.put(temp.get("attr_name"), temp.get("value"));
			temp = null;
		}
		return values;
	}
	public void setAttributes(List<Map<String, String>> attributes) {
		this.attributes = attributes;
	}
	
	public void setModelInfo(Map<String, String> modelInfo) {
		this.modelInfo = modelInfo;
	}

	public static boolean isInteger(String value) {  
		 try {   
			 Integer.parseInt(value);   
			 return true;  
		 } catch (NumberFormatException e) {   return false;  } 
	}
	/**
	 * 更新属性的值
	 * @param attr
	 * @param value
	 * 2015-4-29
	 * @tianming
	 */
	public void setVal(String attr,String value){
		this.getAttributes();
		for(int i = 0;i<attributes.size();i++){
			Map<String,String> attribute = attributes.get(i);
			if(attr.equals(attribute.get("attr_name").trim())){
				attribute.put("value", value);
				setAsyncWidth(2);
				this.firePropertyChange("refresh", attr, value);
				break;
			}
		}
	}
	/**
	 * 取得属性的值
	 * @param attr
	 * @return
	 * 2015-5-2
	 * @tianming
	 */
	public String val(String attr){
		String value = "";
		this.getAttributes();
		for(int i = 0;i<attributes.size();i++){
			Map<String,String> attribute = attributes.get(i);
			if(attr.equals(attribute.get("attr_name"))){
				value = attribute.get("value");
			}
		}
		if(value==null){
			value = "";
		}
		return value;
	}
	/**
	 * 如果是容器组件获取子组件的生成代码
	 * @param map
	 * @param lvl
	 * @return
	 * 2015-5-2
	 * @tianming
	 */
	public StringBuffer getChildrenHtml(HashMap<String,String> map,int lvl,HashMap<String,Object> root)throws Exception{
		StringBuffer childHtml = new StringBuffer();
		if(this instanceof ContainerModel){
		   ContainerModel container = (ContainerModel)this;
		   List<UIAbstractModel> children = container.getChildren();
		   lvl +=1;
		   if(children!=null){
			   for(int i = 0;i<children.size();i++){
				  String[] child = children.get(i).toHtml(map,lvl);
				  childHtml.append(child[0]);
				  if(root!=null){
					  if(child.length>1){
						  String childAttr = child[1];
						  if(childAttr!=null&&childAttr.length()>0){
							  String _modelName = children.get(i).getModelName();
							  if("FormModel".equals(this.getModelName())){
								  _modelName = "formField";
							  }
							  ArrayList<String> list = (ArrayList<String>)root.get(_modelName);
							  if(list==null){
								  list = new ArrayList<String>();
								  root.put(_modelName, list);
							  }
							  list.add("{"+childAttr+"}");
						  }
					  }
				  }
			   } 
		   }
		}
		return childHtml;
	}
	/**
	 * 拼接html的属性 和事件  （jsp标签同html标签处理方式相同）
	 * @return
	 * 2015-4-30
	 * @tianming
	 */
	public String initHtmlAttr(HashMap<String,String> map){
		StringBuffer str = new StringBuffer();
		for(int i = 0;i<attributes.size();i++){
			Map<String,String> attribute = attributes.get(i);
			if("1".equals(attribute.get("attr_type"))){//属性
				String value = attribute.get("value");
				if(value!=null&&value.length()>0){
					str.append(" "+attribute.get("attr_name")+"=\""+value+"\" ");
				}
			}else if("2".equals(attribute.get("attr_type"))){//事件
				String id = this.val("id");
				String eventType = attribute.get("attr_name");
				String methodName = attribute.get("value");
				if(id.length()>0&&eventType!=null&&eventType.length()>0&&methodName!=null&&methodName.length()>0){
					map.put("bindEvent", map.get("bindEvent")+"\t\t\t$('#"+this.val("id")+"')."+attribute.get("attr_name")+"(function(){"+attribute.get("value")+"()});\n");
				}
			}
		}
		return str.toString();
	}
	/**
	 * 拼接UI组件的属性
	 * @return
	 * 2015-4-30
	 * @tianming
	 */
	@SuppressWarnings("unchecked")
	public String initUIAttr(String offset,boolean isNextLine,HashMap<String,Object> root)throws Exception{
		StringBuffer str = new StringBuffer();
		if(this instanceof ContainerModel){
			   ContainerModel container = (ContainerModel)this;
			   List<UIAbstractModel> children = container.getChildren();
			   if(children!=null){
				   StringBuffer childAttr = new StringBuffer();
				   String modelName = this.getModelName();
				   String temp = "";
				   for(int i = 0;i<children.size();i++){
					   String temp2 = children.get(i).getModelName();
					   if("FormModel".equals(this.getModelName())){
						   temp2 = "formField";
					   }
					   if(temp2.equals(temp)) continue;
					   ArrayList<String> attr = (ArrayList<String>)root.get(temp2);
					   if(attr!=null){
						   for(String attr_val:attr){
							   if(attr_val!=null&&!"".equals(attr_val)){
								   childAttr.append(attr_val+",");
							   }
						   }
					   }
					   temp = temp2;
				   } 
				   if(childAttr.length()>0){
					   str.append(modelName+":["+childAttr.substring(0, childAttr.length()-1)+"],");
				   }
			   }
		}
	
		for(int i = 0;i<attributes.size();i++){
			Map<String,String> attribute = attributes.get(i);
			String value = attribute.get("value");
			if("N".equals(attribute.get("isnull"))){
				if(value==null||"".equals(value)){
					throw new ComplierException("组件"+this.getModelName()+"的属性"+attribute.get("attr_name")+"不允许为空,编译失败");
				}
			}
			if("3".equals(attribute.get("attr_type"))){//属性
				
				if(value!=null&&value.length()>0){
					String name = attribute.get("attr_name");
					String value_type = attribute.get("value_type");
					if("function".equals(value_type)||"array".equals(value_type)||"boolean".equals(value_type)||"number".equals(value_type)){
						str.append(offset+" "+name+":"+value+","+(isNextLine?"\n":""));
					} else{
						str.append(offset+" "+name+":\""+value+"\","+(isNextLine?"\n":""));
					}
				}
			}else if("4".equals(attribute.get("attr_type"))){//事件
				if(attribute.get("value")!=null&&!"".equals(attribute.get("value"))){
					String method = attribute.get("value");
					str.append(offset+" "+attribute.get("attr_name")+":"+method+",");
				}
			}
		}
		String result = "";
		if(str.length()>0){
				result = str.substring(0, str.length()-1);
		}
		return result.toString();
	}
	public String initDataOptions(HashMap<String,Object> root)throws Exception{
		StringBuffer str = new StringBuffer();
		for(int i = 0;i<attributes.size();i++){
			Map<String,String> attribute = attributes.get(i);
			String value = attribute.get("value");
			if("N".equals(attribute.get("isnull"))){
				if(value==null||"".equals(value)){
					throw new ComplierException("组件"+this.getModelName()+"的属性"+attribute.get("attr_name")+"不允许为空,编译失败");
				}
			}
			if("5".equals(attribute.get("attr_type"))){//属性
				if(value!=null&&value.length()>0){
					String name = attribute.get("attr_name");
					String value_type = attribute.get("value_type");
					if("function".equals(value_type)||"array".equals(value_type)||"boolean".equals(value_type)||"number".equals(value_type)){
						str.append( name+":"+value+",");
					} else{
						str.append( name+":\""+value+"\",");
					}
					
					
				}
			}
		}
		String result = "";
		if(str.length()>0){
				result = str.substring(0, str.length()-1);
		}else{
			result = str.toString();
		}
		return result;
	}
	/**
	 * 根据配置信息生成代码
	 * @param map
	 * @param lvl
	 * @return
	 * 2015-5-2
	 * @tianming
	 */
	public String[] toHtml(HashMap<String, String> map,int lvl)throws Exception{
		includeJs(getModelInfo(),map);//引入当前组件依赖的javascript脚本
		String tagname = modelInfo.get("tagname");
		StringBufferUtil pageInfo = new StringBufferUtil();
		HashMap<String,Object> root = new HashMap<String,Object>();//存放当前组件所有的属性的值
		for(int i = 0;i<attributes.size();i++){
			Map<String,String> att = attributes.get(i);
			root.put(att.get("attr_name"),att.get("value"));
		}
		pageInfo.offset = lvl;
		String attr = this.initHtmlAttr(map);
		String codeTemplate = modelInfo.get("code_template");
		String childrenHtml = this.getChildrenHtml(map, lvl,root).toString();
		if(this instanceof DefComponentModel){
			pageInfo.appendChildrenHtml(childrenHtml);
			return new String[]{pageInfo.toString(),""};
		}else{
			//初始化html代码
			if(codeTemplate!=null&&codeTemplate.length()>0){
				root.put("attr", attr);
				TemplateUtil util = new TemplateUtil(codeTemplate);
				pageInfo.append(util.analysisTemplate(root)+"\n");
			}else{
				if(tagname!=null&&tagname.length()>0){
					String dataOptions = this.initDataOptions(root);
					if(dataOptions.length()>0){
						if(tagname.indexOf(":")==-1){
							attr += " data-options='"+dataOptions+"'";
						}else {
							attr += " data_options='"+dataOptions+"'";
						}
					}
					if(childrenHtml==null||childrenHtml.length()==0){
						pageInfo.append("<"+tagname+" "+attr+" ></"+tagname+">\n");
					}else{
						pageInfo.append("<"+tagname+" "+attr+" >\n");
						pageInfo.appendChildrenHtml(childrenHtml);
						pageInfo.append("</"+tagname+">\n");
					}
				}
			}
			String id = this.val("id");
			//如果是ui组件，生成js代码
			String compAttr = this.initUIAttr("", false,root);//当前组件的属性
			String initMethod = modelInfo.get("init_method");
			String parentModelName = "";
			String pid = "";
			if(this.parentModel!=null){
				parentModelName = this.parentModel.getModelName();
				pid = this.parentModel.val("id") ;
			}
			if(initMethod!=null&&!"".equals(initMethod)){
					if(!"ToolBar".equals(this.getModelName())||(!"GridModel".equals(parentModelName)&&"ToolBar".equals(this.getModelName()))){
						compAttr = "\t\t\t"+initMethod+"('"+id+"',{"+compAttr+"},'"+parentModelName+"','"+pid+"');\n";
						map.put("jqueryInitComp",compAttr + map.get("jqueryInitComp"));
						compAttr = null;
					}
			}
			
			if( "ColumnModel".equals(this.getModelName())){
				String colName = this.val("field");
				String tab_name = this.val("tab_name");
				if(colName!=null&&colName.length()>0&&tab_name!=null&&tab_name.length()>0){
					map.put("optionsCol",map.get("optionsCol")+tab_name+","+colName+"@");
				}
			}
			return new String[]{pageInfo.toString(),compAttr};
		}
	}
	public void includeJs(Map<String,String> modelInfo,HashMap<String, String> map){
		try {
			String includeJs = modelInfo.get("includejs");
			if(includeJs!=null&&includeJs.length()>0){
				String[] temp = includeJs.split(",");
				for(int i = 0;i<temp.length;i++){
					this.includeJs(temp[i], map);
				}
			}
		} catch (Exception e) {
			System.out.print(this.getClass().getName());
			System.out.println(this.getModelName());
			e.printStackTrace();
		}
	}
	/**
     * 描述：使用当前组件需要额外引入的js
     * @param js
     * @param map
     * 作者：tianming
     * 日期：2014-09-26
     */
    public void includeJs(String js,HashMap<String,String> map){
    	String ui = "common/resource/lib/";
    	if(js!=null&&js.indexOf("jquery")>-1){//如果引入的是ligerUI
    		js =ui+js;
    	}
    	if(js!=null&&map.get("includeJs").indexOf(js)==-1){//避免重复引入脚本
    		map.put("includeJs",map.get("includeJs")+"\t<script src='<%=basePath%>"+js+"' type='text/javascript'></script>\n");
    	}
    }
	public void setAsyncWidth(int type){
		if(type==1){//由width同步到attr
			if(this instanceof LeftModel){
				LeftModel left = (LeftModel)this;
				left.setVal("width", width);
			}else if(this instanceof TopModel){
				TopModel top = (TopModel)this;
				top.setVal("height", height);
			}else if(this instanceof ColumnModel){
				ColumnModel column = (ColumnModel)this;
				column.setVal("width", width);
			}
		}else if(type==2){
			if(this instanceof ColumnModel){
				ColumnModel column = (ColumnModel)this;
				this.width = column.val("width");
			}
		}
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public void addListener(PropertyChangeListener l){
		listener.addPropertyChangeListener(l);
	}
	public void deleteListener(PropertyChangeListener l){
		listener.removePropertyChangeListener(l);
	}
	public void firePropertyChange(String propName,Object oldValue,Object newValue){
		listener.firePropertyChange(propName, oldValue, newValue);
	}
	
	public String[] getAllCompId(){
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		UIContentsModel contents = null;
		UIAbstractModel temp = this;
		while(true){
			if(temp==null||temp instanceof UIContentsModel){
				contents = (UIContentsModel)temp;
				break;
			}
			temp = temp.parentModel;
		}
		contents.getAllUIComponentID(list,(ContainerModel)contents);
		String[] arr = new String[list.size()];
		for(int i = 0;i<list.size();i++){
			arr[i] = list.get(i).get("id");
		}
		return arr;
	}
	public void getAllCompId(List<HashMap<String,String>> list){
		UIContentsModel contents = null;
		UIAbstractModel temp = this;
		while(true){
			if(temp==null||temp instanceof UIContentsModel){
				contents = (UIContentsModel)temp;
				break;
			}
			temp = temp.parentModel;
		}
		contents.getAllUIComponentID(list,(ContainerModel)contents);
	}
	
	public UIAbstractModel getDefComponentByParent(){
		UIAbstractModel model = this;
		while(model!=null){
			if(model instanceof DefComponentModel){
				return model;
			}else{
				model = model.parentModel;
			}
		}
		return null;
	}
}
