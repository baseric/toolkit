package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Combo;

public class CacheInit {
	public static HashMap<String,List<Map<String,String>>> optionCache = null;
	public static HashMap<String,List<Map<String,String>>> attrCache = null;//
	public static List<Map<String,String>> modelInfoCache = null;//
	private CacheInit() {
		
	}
	public static void refreshCache(){
		if(optionCache!=null){
			optionCache.clear();
			optionCache = null;
			//getOptionCacheInstance();
		}
		if(attrCache!=null){
			attrCache.clear();
			attrCache = null;
			//getAttrCacheInstance();
		}
		if(modelInfoCache!=null){
			modelInfoCache.clear();
			modelInfoCache = null;
			//getModelCacheInstance();
		}
	}
	public static HashMap<String,List<Map<String,String>>> getOptionCacheInstance(List<Map<String,String>> data){
		if(optionCache==null){
			BaseInfoUtil util = new BaseInfoUtil();
			if(data==null){
				data = util.readXML("dev_options");
			}
			optionCache = new HashMap<String,List<Map<String,String>>>();

			String type = "";
			List<Map<String,String>> temp = null;
			for(int i = 0;i<data.size();i++){
				if(!type.equals(data.get(i).get("componet_type"))){
					if(!"".equals(type)){
						optionCache.put(type, temp);
					}
					type = data.get(i).get("componet_type");
					temp = new ArrayList<Map<String,String>>();
				}
				temp.add(data.get(i));
			}
			optionCache.put(type, temp);
		}
		return optionCache;
	}
	public static HashMap<String,List<Map<String,String>>> getAttrCacheInstance(List<Map<String,String>> data){
		if(attrCache==null){
			BaseInfoUtil util = new BaseInfoUtil();
			if(data==null){
				data = util.readXML("dev_model_attribute");
			}
			attrCache = new HashMap<String,List<Map<String,String>>>();

			String type = "";
			List<Map<String,String>> temp = null;
			for(int i = 0;i<data.size();i++){
				if(!type.equals(data.get(i).get("model_name"))){
					if(!"".equals(type)){
						attrCache.put(type, temp);
					}
					type = data.get(i).get("model_name");
					temp = new ArrayList<Map<String,String>>();
				}
				temp.add(data.get(i));
			}
			attrCache.put(type, temp);
		}
		return attrCache;
	}
	public static List<Map<String,String>> getModelCacheInstance(List<Map<String,String>> data){
		if(modelInfoCache==null){
			BaseInfoUtil util = new BaseInfoUtil();
			if(data==null){
				data = util.readXML("dev_model");
			}
			modelInfoCache = data;
		}
		return modelInfoCache;
	}
	public static Map<String,String> getModelMap(String modelName){
		getModelCacheInstance(null);
		for(int i = 0;i<modelInfoCache.size();i++){
			if(modelName.equals(modelInfoCache.get(i).get("model_name"))){
				return modelInfoCache.get(i);
			}
		}
		return null;
	}
	public static String getModelTemplate(String modelName){
		Map<String,String> model = CacheInit.getModelMap(modelName);
		if(model!=null){
			return model.get("code_template");
		}else{
			return "";
		}
	}
	
	public static List<Map<String,String>> getAttrValue(String key){
		return getAttrCacheInstance(null).get(key);
	}
	public static List<Map<String,String>> getValue(String key){
		return getOptionCacheInstance(null).get(key);
	}
	/**
	 * 
	 * @param key
	 * @return
	 * 2015-5-12
	 * @tianming
	 */
	public static String[] getTextArray(String key){
		List<Map<String,String>> options = CacheInit.getValue(key);
		if(options==null){
			options = new ArrayList<Map<String,String>>();
		}
		String[] array = new String[options.size()];
		for(int i = 0;i<options.size();i++){
			array[i] = options.get(i).get("optiontext");
		}
		return array;
	}
	public static String getValueByIndex(String key,int index){
		List<Map<String,String>> options = CacheInit.getValue(key);
		return options.get(index).get("optionvalue");
	}
	public static String getValueByText(String key,String text){
		List<Map<String,String>> options = CacheInit.getValue(key);
		if(options!=null){
			for(int i = 0;i<options.size();i++){
				Map<String,String> opt = options.get(i);
				if(text.equals(opt.get("optiontext"))){
					return opt.get("optionvalue");
				}
			}
		}else{
			System.out.println("key="+key+" is null");
		}
		return "";
	}
	public static String getTextByValue(String key,String value){
		List<Map<String,String>> options = CacheInit.getValue(key.trim());
		if(options!=null){
			for(int i = 0;i<options.size();i++){
				Map<String,String> opt = options.get(i);
				if(value.equals(opt.get("optionvalue"))){
					return opt.get("optiontext");
				}
			}
		}else{
			Log.write("key="+key+",value="+value+",下拉选择未取到对应中文解释");
		}
		return "";
	}
	/**
	 * 获取数据源可选项
	 * @return
	 * 2015-4-27
	 * @tianming
	 */
	public static Map<String,Object> getDataSource(Combo combo){
		PropertyReader reader = new PropertyReader();
		return reader.initDataSourceSelect(combo,"Y");
	}
	/**
	 * 获取数据源可选项
	 * @return
	 * 2015-4-27
	 * @tianming
	 */
	public static Map<String,Object> getDataSource(Combo combo,String dic){
		PropertyReader reader = new PropertyReader();
		if("Y".equals(dic)){
			return reader.getDicDataSource(combo);
		}
		return reader.initDataSourceSelect(combo,"N");
	}
}
