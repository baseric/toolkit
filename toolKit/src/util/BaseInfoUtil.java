package util;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class BaseInfoUtil {
	public String rootPath = System.getProperty("user.home")+"/.mytools/";
	public void writerXML(String tab_name,List<Map<String,String>> list){
		Document document = DocumentHelper.createDocument();
		try{
			Element root = document.addElement("table");
			root.addAttribute("tab_name", tab_name);
			for(Map<String,String> row:list){
				Element element = root.addElement("row");//节点名
				Set<String> columns = row.keySet();
				for(String colName:columns){
					Element col = element.addElement(colName);
					col.setText(row.get(colName));
				}
			}
			 /** 格式化输出,类型IE浏览一样 */  
	        OutputFormat format = OutputFormat.createPrettyPrint();  
	        format.setEncoding("GBK");
			XMLWriter writer = new XMLWriter(new FileWriter(rootPath+tab_name+".xml"),format);
			writer.write(document);
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> readXML(String tab_name){
		SAXReader saxReader = new SAXReader();
		List<Map<String,String>> data = new ArrayList<Map<String,String>>();
		try {
			Document document = saxReader.read(rootPath+tab_name+".xml");
			Element root = document.getRootElement();
			List<Element> children = root.elements();
			for(Element row:children){
				HashMap<String,String> rowdata = new HashMap<String,String>();
				List<Element> columns = row.elements();
				for(Element column:columns){
					String colName = column.getName();
					String value = column.getText();
					rowdata.put(colName, value);
				}
				data.add(rowdata);
			}
		}  catch (Exception e){
			e.printStackTrace();
		}
		return data;
	}
	
	public void uploadTableData(Connection con){
		CacheInit.refreshCache();
		List<Map<String,String>> data = ConnFactory.queryList("select * from dev_options order by componet_type , orderby ", con);
		this.writerXML("dev_options", data);
		CacheInit.getOptionCacheInstance(data);
		data = ConnFactory.queryList("select * from  dev_model_attribute where use_yn='Y' order by model_name , seqn ", con);
		this.writerXML("dev_model_attribute", data);
		CacheInit.getAttrCacheInstance(data);
		data = ConnFactory.queryList("select * from  dev_model  ", con);
		this.writerXML("dev_model", data);
		CacheInit.getModelCacheInstance(data);
		data = ConnFactory.queryList("select * from dev_comp_menu order by up_comp_name ,seqn", con);
		this.writerXML("dev_comp_menu", data);
		data = ConnFactory.queryList("select * from dev_common_method order by seqn", con);
		this.writerXML("dev_common_method", data);
		data = ConnFactory.queryList("select * from dev_method_param order by method_name,param_seqn", con);
		this.writerXML("dev_method_param", data);
		data = ConnFactory.queryList("select * from dev_template order by temp_type", con);
		this.writerXML("dev_template", data);
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
