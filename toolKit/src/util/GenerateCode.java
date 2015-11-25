package util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.draw2d.geometry.Rectangle;

import business.AbstractModel;
import business.model.ContentsModel;
import business.model.LineConnectionModel;
import business.model.action.StartModel;
import business.model.logic.LogicStartModel;
import exception.ComplierException;

public class GenerateCode {
	 
    /**
     * 生成代码前准备数据
     * @param root
     * @param contents
     * @return
     * @throws Exception
     * 2015-1-13
     * @tianming
     */
    public List<HashMap<String,String>> preparedData(Map<String,Object> root,ContentsModel contents) throws Exception{
    	root.put("startId","");
		root.put("import","");
		List<AbstractModel> children = contents.getChildren();
		List<HashMap<String,String>> childrenMap = new ArrayList<HashMap<String,String>>();
		//初始化生成代码的配置信息
		for(int i = 0;i<children.size();i++){
			AbstractModel model = children.get(i);
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("id",model.getId());
			children.get(i).checkParamExsit(children.get(i).getInParamList());//检查当前节点使用的变量是否存在
			String code = children.get(i).toCode(root,65);
			map.put("code", code);
			children.get(i).firePropertyChange("success", null, null);//校验该节点是否合法
			if(model instanceof StartModel||model instanceof LogicStartModel){//如果是开始节点
				/*List<AbstractConnectionModel> links = model.getSourceConnection();
				if(links.size()==1){//开始节点只能有一个连出线
*/					root.put("startId", model.getId());
				/*}else{
					Log.write("开始节点的连出线有且仅能有一条");
					throw new Exception();
				}*/
			}
			
			childrenMap.add(map);
		}
		return childrenMap;
    }
  
    /**
     *	导入配置模版生成代码 
	 * @author tianming
	 * @param uploadFile
	 * @return
	 */
	public String createAction(ContentsModel contents,File file,IProject project,String templateName) {
		Map<String,Object> root = new HashMap<String,Object>(); 
		String flag = "0000";
		File outputFile = null;
		String fileName = file.getName();
		List<HashMap<String,String>> childrenMap = null;
		//List<AbstractModel> children = contents.getChildren();
		PropertyReader reader = new PropertyReader();
		String basePath = project.getLocation().toFile().getAbsolutePath() + reader.getPropertyValue("resourcePath");
		try {
			if(contents.getChildren().size()==0) return "";
			String filePath = file.getAbsolutePath();//配置文件路径
			String projectPath = project.getLocation().toFile().getAbsolutePath();//工程路径
			contents.projectPath = projectPath;
			//准备数据
			childrenMap = preparedData(root,contents);
			
			
			filePath = filePath.replace(basePath.replace("/", "\\"), "").replace("\\"+fileName,"");
			
			//获取类名
			fileName = fileName.replace(".ctl", ".java").replace(".logic",".java");
			
			outputFile = new File(FileUtil.getJavaFilePath(project, file));//生成代码的目录
			Log.write("生成源文件路径："+outputFile.getAbsolutePath());
			if(!outputFile.exists()){
				File dir = outputFile.getParentFile();
				if(!dir.exists()){
					dir.mkdirs();
				}
				outputFile.createNewFile();
			}
			
			//生成包名
			String packagePath = filePath.replace("\\", ".");
			root.put("actionName", fileName.replace(".java", ""));//类名
			root.put("package",packagePath);//包名
			root.put("root", childrenMap);
			
			
			String path =basePath+"plugin_resource/ftl";
			FreemarkerUtil freemark = new FreemarkerUtil();
			freemark.createCode(path,root, templateName, outputFile);
			project.getFile(reader.getPropertyValue("javaPath")+filePath+"/"+fileName).refreshLocal(IResource.DEPTH_INFINITE, null);//刷新源文件
		} catch (Exception e) {
			if(!(e instanceof ComplierException)){
				Log.write("", e);
				flag = "0001";
			}else{
				Log.write(file.getName()+" 有错，没有编译完成" );
			}
		}finally{
			root = null;
		}
		return flag;
	}
    /**
	 * 将页面信息保存到文件
	 * @param file
	 * @param jsp
	 * @author tianming
	 * @2014-9-30
	 */
	public void saveToFile(File file,ContentsModel contents){
		Document document = DocumentHelper.createDocument();
		try{
			Element root = document.addElement("ContentsModel");
			root.addAttribute("class", contents.getClass().getName());
			root.addAttribute("count", String.valueOf(contents.count));
			this.writerXML(root,contents.getChildren());
			 /** 格式化输出,类型IE浏览一样 */  
	        OutputFormat format = OutputFormat.createPrettyPrint();  
	        format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(new FileOutputStream(file),format);
			writer.write(document);
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 将配置信息写入到xml文件
	 * @param root
	 * @param children
	 * 2015-1-2
	 * @tianming
	 */
	public void writerXML(Element root,List<AbstractModel> children){
		for(AbstractModel model:children){
			Element element = root.addElement(model.getClass().getSimpleName().replace(".java", ""));//节点名
			element.addAttribute("class", model.getClass().getName());//节点对应的模型类
			int x = model.getRect().x;
			int y = model.getRect().y;
			if(x<=0){
				x = 20;
			}
			if(y<=0){
				y = 20;
			}
			element.addAttribute("x", String.valueOf(x));//节点坐标
			element.addAttribute("y", String.valueOf(y));
			element.addAttribute("id", model.getId());
			if(model.getSourceConnection().size()>0){//保存节点的连出线
				for(int i = 0;i<model.getSourceConnection().size();i++){
					LineConnectionModel line = (LineConnectionModel) model.getSourceConnection().get(i);
					Element link = element.addElement("link");
					link.addAttribute("to", line.getTarget().getId());
					link.addAttribute("display", line.getDisplay());
					link.addAttribute("type",line.getType());
					
					link.addAttribute("paramName", line.getParamName());
					link.addAttribute("opt", line.getOpt());
					link.addAttribute("paramName2", line.getParamName2());
					link.addAttribute("valueType", line.getValueType());
					
					link.addAttribute("className", line.getClassName());
					link.addAttribute("javaEl", line.getJavaEl());
				}
			}
			model.writeInXml(element);//将模型自身的属性写入element中
		}
	}
	public AbstractModel readFile(File file){
		SAXReader saxReader = new SAXReader();
		AbstractModel model = null;
		try {
			Document document = saxReader.read(file);
			Element root = document.getRootElement();
			try{
				model = parseXML(root);
			}catch(Exception e){}
		} catch (DocumentException e) {
		} catch (Exception e){
			e.printStackTrace();
		}
		return model;
	}
 
	@SuppressWarnings("unchecked")
	public AbstractModel parseXML(Element root){
		String className = root.attributeValue("class");
		String count = root.attributeValue("count");
		ContentsModel model = null;
		List<Map<String,Object>> lines = new ArrayList<Map<String,Object>>();
		try {
			Class clazz = Class.forName(className);
			model = (ContentsModel)clazz.newInstance();
			if(count!=null){
				model.count = Integer.parseInt(count);
			}
			
			List<Element> children = root.elements();
			if(children!=null&&children.size()>0){
				for ( Element element : children ) {
					Rectangle rect = new Rectangle();
					String name = element.attributeValue("class");
					String x = element.attributeValue("x");
					String y = element.attributeValue("y");
					String id = element.attributeValue("id");
					Class clazz2 = Class.forName(name);
					AbstractModel child = (AbstractModel)clazz2.newInstance();
					rect.x = Integer.parseInt(x)+16;
					rect.y = Integer.parseInt(y)+16;
					child.setRect(rect);
					child.parentModel = model;
					model.addChild(child);
					child.setId(id);
					child.readFromXml(element);
					
					List<Element> links = element.elements("link");
					if(links!=null&&links.size()>0){
						for(int i = 0;i<links.size();i++){
							Element link = links.get(i);
							Map<String,Object> line = new HashMap<String,Object>();
							line.put("from", id);
							line.put("to", link.attributeValue("to"));
							line.put("link", link);
							lines.add(line);
						}
					}
				}
				for(int i = 0;i<lines.size();i++){
					Map<String,Object> line = lines.get(i);
					String fromId = String.valueOf(line.get("from"));
					String toId = String.valueOf(line.get("to"));
					AbstractModel source = model.getModelById(fromId);
					AbstractModel target = model.getModelById(toId);
					LineConnectionModel connection = new LineConnectionModel() ;
					connection.setSource(source);
					connection.attachSource();
					connection.setTarget(target);
					connection.attachTarget();
					
					Element link = (Element)line.get("link");
					connection.setDisplay(link.attributeValue("display"));
					connection.setType(link.attributeValue("type"));
					
					connection.setParamName(link.attributeValue("paramName"));
					connection.setOpt(link.attributeValue("opt"));
					connection.setParamName2(link.attributeValue("paramName2"));
					connection.setValueType(link.attributeValue("valueType"));
					
					connection.setClassName(link.attributeValue("className"));
					connection.setJavaEl(link.attributeValue("javaEl"));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}
	
}
