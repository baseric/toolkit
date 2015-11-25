package ui.util;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import ui.UIAbstractModel;
import ui.model.DefComponentModel;
import ui.model.UIContentsModel;
import ui.model.abstractModel.ContainerModel;
import ui.model.liger.FormModel;
import util.ConnFactory;
import util.EditorUtil;
import util.FileUtil;
import util.FreemarkerUtil;
import util.Log;
import util.PropertyReader;
import exception.ComplierException;

public class UIGenerateCode {
	private IProject project = null;
	
	public IProject getProject() {
		return project;
	}
	public void setProject(IProject project) {
		this.project = project;
	}
	public void asyncFormToDb(File file,UIContentsModel contents,IProject project) throws Exception{
		Log.write("同步页面："+file.getName());
		String tx_code = contents.val("tx_code");
		FormModel form = getFormModel(contents);
		if(form!=null){
			if(tx_code==null||"".equals(tx_code)){
				throw new Exception("页面:"+file.getName()+"没有配置交易代码");
			}
			PropertyReader reader = new PropertyReader();
			List<UIAbstractModel> children = form.getChildren();
			String isInsertDB = reader.getPropertyValue("isInsertDB");
			
			String table1 = "com_page_def";
			String table2 = "com_page_detail";
			//删除页面信息定义表记录
			String sql1 = "delete from "+table1+" where tx_code='"+tx_code+"'";
			
			//插入页面信息定义表记录
			String insert = "";
			Map<String,String> values = contents.getAttrNameAndValue();
			values.remove("style");
			String projectPath = project.getLocation().toFile().getAbsolutePath();
			String filePath = file.getAbsolutePath();
			filePath = filePath.replace(projectPath, "").replace("\\", "/").replace("ui", "jsp");
			values.put("url",filePath);
			insert = ConnFactory.getInserSql(table1, values);
			
			//删除原页面表单要素项信息
			String sql2 = "delete from "+table2+" where tx_code='"+tx_code+"'";
			
			//插入页面表单要素项信息
			List<Map<String,String>> formElements = new ArrayList<Map<String,String>>();
			int count = 0;
			for(UIAbstractModel element:children) {
				String name = element.val("name");
				String display = element.val("label");
				String type = element.val("type");
				String tx_type = element.val("tx_type");
				Map<String,String> map = new HashMap<String,String>();
				map.put("tx_code", tx_code);
				map.put("form_id", form.val("id"));
				map.put("name", name);
				map.put("disp", display);
				map.put("el_type", type);
				map.put("tx_type", tx_type);
				map.put("seqn", String.valueOf(count++));
				formElements.add(map);
			}
			String insertList = ConnFactory.getInsertListSql(table2,formElements);
			
			Log.write("\n"+sql1+";\n"+ 
					  insert+";\n"+
					  sql2+";\n"+
					  insertList);
			if("Y".equals(isInsertDB)){
				Connection con = ConnFactory.getConnection(reader.getPropertyValue("driver"),reader.getPropertyValue("url"),reader.getPropertyValue("username"),reader.getPropertyValue("password"));
				try{
					ConnFactory.deleteRow(sql1, con);
					//插入页面信息定义表记录
					ConnFactory.insertMap(table1,values, con);
					//删除表单记录信息
					ConnFactory.deleteRow(sql2, con);
					ConnFactory.insertList(table2, formElements, con);
					con.commit();
				} catch(Exception e) {
					con.rollback();
					throw e;
				} finally {
					
				}
			}
		}
	}
	private FormModel getFormModel(ContainerModel container){
		List<UIAbstractModel> children = container.getChildren();
		for(int i = 0;i<children.size();i++){
			UIAbstractModel model = children.get(i);
			if(model instanceof FormModel){
				return (FormModel)model;
			}else if(model instanceof ContainerModel){
				FormModel form = getFormModel((ContainerModel)model);
				if(form instanceof FormModel){
					return (FormModel)form;
				}
			}
		}
		return null;
	}
	/**
     *	导入配置模版生成页面代码 
	 * @author tianming
	 * @param uploadFile
	 * @return
	 */
	public String createJSP(File file,UIContentsModel contents,IProject project) {
		Map<String,Object> root = new HashMap<String,Object>();
		String flag = "0000";
		File outputFile = null;
		String pageHtml = "";
		HashMap<String,String> pageInfo = new HashMap<String,String>();
		String name = file.getName();
		name = name.replace(".ui", ".jsp");
		PropertyReader reader = new PropertyReader();
		String basePath = project.getLocation().toFile().getAbsolutePath() + reader.getPropertyValue("resourcePath");
		try {
			pageInfo.put("includeJs", "");//引入的js
			pageInfo.put("bindEvent","");//绑定的事件
			pageInfo.put("loadData","");//绑定的事件
			pageInfo.put("form_toolbar","");//表单工具栏
			pageInfo.put("jqueryInitComp","");//UI组件
			pageInfo.put("optionsCol", "");
			//同步属性
			EditorUtil.refreshAbstractModel(contents);
			pageHtml = contents.toHtml(pageInfo,0)[0];
			
			/**查找配置文件**/
			FreemarkerUtil freemarkUtil = new FreemarkerUtil();
			String ftl_path = basePath + "/plugin_resource/ftl/";
			/**查找配置文件**/
			String filePath = file.getAbsolutePath();//页面配置文件路径
			filePath = filePath.replace(basePath.replace("/","\\"), "").replace("\\"+file.getName(),"");
			String jspPath = FileUtil.getJspFilePath(project,file);
			outputFile = new File(jspPath);//生成页面
			Log.write("生成jsp路径："+outputFile.getAbsolutePath());
			if(!outputFile.exists()){
				File dir = outputFile.getParentFile();
				if(!dir.exists()){
					dir.mkdirs();
				}
				outputFile.createNewFile();
			}
			
			root.put("javascriptFileName", name.replace(".jsp", ".js"));//引入的js
			root.put("html", pageHtml);//pageInfo用于收集页面信息
			root.put("hidden", "");//pageInfo用于收集页面信息
			root.put("javascriptCode", contents.javascriptCode);
			if(pageInfo.get("includeJs")==null)pageInfo.put("includeJs","");
			if(pageInfo.get("bindEvent")==null)pageInfo.put("bindEvent","");
			if(pageInfo.get("jqueryInitComp")==null)pageInfo.put("jqueryInitComp","");
			if(pageInfo.get("loadData")==null)pageInfo.put("loadData","");
			root.put("pageInfo",pageInfo);//放入freemarker的数据源
			root.put("version", "1");
			
			
			freemarkUtil.createCode(ftl_path, root, "jsp.ftl", outputFile);
		    project.getFile(reader.getPropertyValue("jspPath")+filePath+"/"+name).refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Exception e) {
			if(e instanceof ComplierException){
				Log.write(e.getMessage());
			}else{
				Log.write("文件编译过程中发生错误："+file.getName());
				Log.write("", e);
			}
			flag = "0001";
		}finally{
			root = null;
		}
		return flag;
	}
    /**
	 * 将页面信息保存到XML文件
	 * @param file
	 * @param jsp
	 * @author tianming
	 * @2014-9-30
	 */
	public void saveToFile(File file,UIContentsModel contents){
		Document document = DocumentHelper.createDocument();
		try{
			Element root = document.addElement(contents.getModelName());
			root.addAttribute("_class", contents.getClass().getName());
			for(int i = 0;i<contents.getAttributes().size();i++){
				Map<String,String> attr = contents.getAttributes().get(i);
				root.addAttribute(attr.get("attr_name").trim(), attr.get("value"));
			}
			
			this.writerXML(root,contents.getChildren());
			 /** 格式化输出,类型IE浏览一样 */  
	        OutputFormat format = OutputFormat.createPrettyPrint();  
	        format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(new FileOutputStream(file),format);
			/**页面引入的js脚本***/
			List<String> js = contents.getJs();
			for(int i = 0;i<js.size();i++){
				Element element = root.addElement("javascript");
				element.addAttribute("path", js.get(i));
			}
			/***当前页面编辑的脚本****/
			Element element = root.addElement("jscode");
			element.addCDATA(contents.javascriptCode.toString());
			//element.setText();
			writer.write(document);
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public void writerXML(Element root,List<UIAbstractModel> children){
		for(UIAbstractModel model:children){
			Element element = root.addElement(model.getModelName());
			element.addAttribute("_class", model.getClass().getName());
			element.addAttribute("_x", model.x);
			element.addAttribute("_y", model.y);
			element.addAttribute("_width", model.getWidth());
			element.addAttribute("_height", model.getHeight());
			for(int i = 0;i<model.getAttributes().size();i++){
				Map<String,String> attr = model.getAttributes().get(i);
				element.addAttribute(attr.get("attr_name").trim(), attr.get("value"));
			}
			Field[] fields = model.getClass().getFields();
			for(int i = 0;i<fields.length;i++){
				Field f = fields[i];
				try {
					Class typeClass = f.getType();
					if(typeClass==List.class){
						this.writeList(element, (List)f.get(model), f.getName());
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(model instanceof ContainerModel&&!(model instanceof DefComponentModel)){
				ContainerModel container = (ContainerModel)model;
				if(container.getChildren().size()>0){
					writerXML(element,container.getChildren());
				}
			}
		}
	}
	 /**
	 * 从XML文件读取页面配置信息
	 * @param file
	 * @param jsp
	 * @author tianming
	 * @2014-9-30
	 */
	public UIAbstractModel readFile(File file){
		SAXReader saxReader = new SAXReader();
		UIAbstractModel model = null;
		try {
			Document document = saxReader.read(file);
			Element root = document.getRootElement();
			try{
				model = parseXML(root);
			}catch(Exception e){e.printStackTrace();}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return model;
	}
	@SuppressWarnings("unchecked")
	public UIAbstractModel parseXML(Element root){
		String className = root.attributeValue("_class");
		if(className==null||"".equals(className)) return null;
		UIAbstractModel model = null;
		try {
			if(className.indexOf("DefComponentModel")>-1){
				model = new DefComponentModel(root.getName());
				IFile file =(IFile) project.findMember(model.val("comp_dir"));
				UIContentsModel contents = (UIContentsModel)this.readFile(file.getLocation().toFile());
				ContainerModel container = (ContainerModel)model;
				container.setChildren(contents.getChildren());
				for(int i = 0;i<container.getChildren().size();i++){
					UIAbstractModel c = container.getChildren().get(i);
					c.parentModel = container;
				}
			}else{
				Class clazz = Class.forName(className);
				Constructor constructor = clazz.getConstructor(String.class);
				model = (UIAbstractModel)constructor.newInstance(root.getName());
				//解析所有属性
				 List<Attribute> list = root.attributes();     
				for (int i = 0; i < list.size(); i++)     
				{   
					Attribute attr =  list.get(i);
					String name = attr.getName();
					String value = root.attributeValue(name);
					Map<String, String> map = null;
					
					for(int j = 0;j<model.getAttributes().size();j++){
						Map<String, String> temp = model.getAttributes().get(j);
						if(name.equals(temp.get("attr_name").trim())){
							map = temp;
							break;
						}
					}
				    if(map!=null){
				    	map.put("value", value);
				    }
				    map = null;
				}
				//解析list类型的属性
				Field[] fields = clazz.getFields();
				for(int i = 0;i<fields.length;i++){
					Field f = fields[i];
					try {
						f.setAccessible(true);
						if(f.getType()==List.class){
							f.set(model, readList(root,f.getName()));
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				model.setX(root.attributeValue("_x"));
				model.setY(root.attributeValue("_y"));
				model.setWidth(root.attributeValue("_width"));
				model.setHeight(root.attributeValue("_height"));
			}
			List<Element> children = root.elements();
			if(children!=null&&children.size()>0){
				ContainerModel container = (ContainerModel)model;
				for ( Element element : children) {
					if("javascript".equals(element.getName())){
						UIContentsModel contents = (UIContentsModel)model;
						contents.getJs().add(element.attributeValue("path"));
					}if("jscode".equals(element.getName())){//页面javascript代码
						UIContentsModel contents = (UIContentsModel)model;
						contents.javascriptCode.append(element.getStringValue());
					}else{
						UIAbstractModel child = parseXML(element);
						if(child!=null){
							child.parentModel = container;
							container.addChild(child);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
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
}
