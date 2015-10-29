package util;

import java.util.List;
import java.util.Map;

public class DicUtil {
	/**
	 * 
	 * @param input_type
	 * @return
	 */
	public static String getModelTypeByInputType(String input_type){
		List<Map<String,String>> options = CacheInit.getValue("input_type");
		for(int i = 0;i<options.size();i++){
			String value = options.get(i).get("optionvalue");
			String text = options.get(i).get("optiontext");
			if(value.equals(input_type)){
				return text;
			}
		}
		return "Text";
	}
}
