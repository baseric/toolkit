package util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.swt.widgets.Combo;

import toolkit.Activator;


public class PropertyReader {
	private String propFile = "ui";
	public PropertyReader(){}
	public PropertyReader(String propFile){
		this.propFile = propFile;
	}
	/**
	 * 取得配置文件信息
	 * @param property
	 * @return
	 * 2015-4-28
	 * @tianming
	 */
	public String getPropertyValue(String property){
		Properties prop = this.getProperty();
		return prop.getProperty(property);
	}
	/**
	 * 设置配置文件信息
	 * @param propties
	 * 2015-4-28
	 * @tianming
	 */
	public void setPropertyValue(HashMap<String,String> propties){
		Properties prop = this.getProperty();
		Set<String> keys = propties.keySet();
		for(String key:keys){
			prop.setProperty(key, propties.get(key));
		}
		this.writerProperties(prop);
	}
	public Properties getProperty(){
		Properties prop = new Properties();
		String path = null;
		try {
			String basePath = System.getProperty("user.home")+"/.mytools/";
			if(!(new File(basePath).exists())){
				new File(basePath).mkdirs();
			}
			/**查找配置文件**/
			path = basePath+"/"+propFile+".properties";
			if(!(new File(path)).exists()){
				this.createProperties(path);
			}
			prop.load(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	/**
	 * 创建配置文件
	 * @param path
	 * @author tianming
	 * @2014-10-1
	 */
	public void createProperties(String path){
		BufferedInputStream in =(BufferedInputStream) Activator.getInputStreamByName("/"+propFile+".properties");
		try {
			FileOutputStream out = new FileOutputStream(path);
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			out.write(buffer);
			out.flush();
			out.close();
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writerProperties(Properties prop){
		String basePath = System.getProperty("user.home")+"/.mytools/";
		String path = basePath+"/"+propFile+".properties";
		try {
			OutputStream fos = new FileOutputStream(path);
			prop.store(fos,"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
	}
	/**
	 * 读取配置文件，获得所有数据源配置信息
	 * @return
	 * 2015-4-28
	 * @tianming
	 */
	public Map<String,Object> getAllDataSource(){
		Properties prop = new Properties();
		String path = null;
		Map<String,Object> allDataSource = new HashMap<String,Object>();
		try {
			String basePath = System.getProperty("user.home")+"/.mytools/";
			if(!(new File(basePath).exists())){
				new File(basePath).mkdirs();
			}
			/**查找配置文件**/
			path = basePath+"/dataSource.properties";
			if(!(new File(path)).exists()){
				this.createProperties(path);
			}
			prop.load(new FileInputStream(path));
			
			HashMap<String,String> temp = new HashMap<String,String>();
			Enumeration<String> enu = (Enumeration<String>) prop.propertyNames();  
		    while (enu.hasMoreElements()) {  
				String key = enu.nextElement();
				temp.put(key.split("\\.")[0],"");
			}  
		    Set<String> dataSourceNames = temp.keySet();
		    for(String key:dataSourceNames){
		    	HashMap<String,String> row = new HashMap<String,String>();
		    	row.put("dataSourceName",key);
				row.put("driver",prop.getProperty(key+".driver"));
				row.put("url",prop.getProperty(key+".url"));
				row.put("username",prop.getProperty(key+".username"));
				row.put("password",prop.getProperty(key+".password"));
				row.put("isDatadic",prop.getProperty(key+".isDatadic"));
				row.put("owner",prop.getProperty(key+".owner"));
				row.put("isDefault",prop.getProperty(key+".isDefault"));
				allDataSource.put(key, row);
		    }
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allDataSource;
	}
	/**
	 *  取得所有非数据字典库的数据源
	 * @param combo
	 * @param getAllDic
	 * @return
	 * 2015-4-28
	 * @tianming
	 */
	public Map<String,Object> initDataSourceSelect(Combo combo,String getAllDic){
		Map<String,Object> allDataSource = this.getAllDataSource();
		Set<String> keys = allDataSource.keySet();
		Object[] array = keys.toArray();
		List<String> dataSources = new ArrayList<String>();
		//String[] dataSources = new String[array.length];
		int count = 0;
		for(int i = 0;i<array.length;i++){
			Map<String,String> dataSource = (Map<String,String>)allDataSource.get(String.valueOf(array[i]));
			String isDefault = dataSource.get("isDefault");
			if("N".equals(getAllDic)){
				if("Y".equals(dataSource.get("isDatadic"))){
					continue;
				}
			}
			dataSources.add(String.valueOf(array[i]));
			if("Y".equals(isDefault)){
				count = i;
			}
		}
		String[] arr = new String[dataSources.size()];
		dataSources.toArray(arr);
		combo.setItems(arr);
		combo.select(count);
		return allDataSource;
	}
	/**
	 * 取得所有数据字典库的数据源
	 * @param combo
	 * @return
	 * 2015-6-19
	 * @tianming
	 */
	public Map<String,Object> getDicDataSource(Combo combo){
		Map<String,Object> allDataSource = this.getAllDataSource();
		Set<String> keys = allDataSource.keySet();
		Object[] array = keys.toArray();
		List<String> dataSources = new ArrayList<String>();
		int count = 0;
		for(int i = 0;i<array.length;i++){
			Map<String,String> dataSource = (Map<String,String>)allDataSource.get(String.valueOf(array[i]));
			String isDefault = dataSource.get("isDefault");
			if("Y".equals(dataSource.get("isDatadic"))){
				dataSources.add(String.valueOf(array[i]));
			}
			if("Y".equals(isDefault)){
				count = i;
			}
		}
		String[] arr = new String[dataSources.size()];
		dataSources.toArray(arr);
		combo.setItems(arr);
		combo.select(count);
		return allDataSource;
	}
}
