package util;

import java.util.HashMap;
import java.util.Map;

public class TemplateUtil {
	 private String splitLeft = "%";
	 private String splitRight = "&";
	 private String template = null;
	 private HashMap<String,String> value = null;
	public TemplateUtil(String template){
		this.template = template;
	}
	 /**
	  * 
	  * 功能描述： 
	  * @param sql
	  * @return
	  * 作者：田明
	  * 日期：2012-9-9
	  */
     public String analysisTemplate(Map<String,Object> value){
    	 if(template==null){
    		 return template;
    	 }
    	 String[] chars = template.split("");
    	 Stack stack = new Stack(2);
    	 String expression = "";
    	 String str = "";
    	 for(int i = 0;i<chars.length;i++){
    		 String c = chars[i];
    		 if(splitLeft.equals(c)){
    			 stack.push('a');//入栈
    		 }else if(splitRight.equals(c)){
    			 stack.pop();//出栈
    			 str += value.get(expression);
    			 expression = "";
    		 }else{
	    		 if(!stack.isEmpty()){
	    			 expression+=c;
	    		 }else{
	    			 str +=c;
	    		 }
    		 }
    	 }
    	 stack = null;
    	 return str;
     }
     /**
      * 功能描述：处理表达式
      * @param exp
      * @return
      * 作者：田明
      * 日期：2012-9-9
      */
     public String integerExp(String exp){
         return value.get(exp);
     }
     public static void main(String[] args){
    	 TemplateUtil util = new TemplateUtil("<fm:tbl  [attr]></fm:tbl>");
    	 HashMap<String,String> value = new HashMap<String,String>();
    	 value.put("attr", "xxxxxxxxxxxx");
    	// System.out.println(util.analysisTemplate(value));
     }
}