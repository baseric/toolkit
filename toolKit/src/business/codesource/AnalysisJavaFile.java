package business.codesource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class AnalysisJavaFile {
	@SuppressWarnings("unchecked")
	public List<HashMap> getJavaInfo(File file){
		  List<HashMap> list = new ArrayList<HashMap>();
		  try{
		        JdtAst jdt = new JdtAst(); 
		        CompilationUnit result = jdt.getCompilationUnit(file.getAbsolutePath()); 
		        List importList = result.imports();// 获取导入的包         
		        TypeDeclaration type = (TypeDeclaration) result.types().get(0);// 获取文件中的第一个类声明(包含注释)         
		        MethodDeclaration[] methodList = type.getMethods();// 获取方法的注释以及方法体         
		        for(int i = 0;i<methodList.length;i++){
		        	HashMap<String,Object> method = new HashMap<String,Object>();
		        	Type method_type = methodList[i].getReturnType2();// 获取返回值类型 如 void  
				    SimpleName method_name = methodList[i].getName();// 获取方法名 main         
				    List parameters = methodList[i].parameters();// 获取参数:[String[] args]   
				    if(method_type!=null){
				    	method.put("returnType",getType(importList,method_type.toString()));//方法返回值类型
				    }
				    parameters = getClassTypeByImport(importList,parameters);
				    method.put("name", String.valueOf(method_name));
				    method.put("methodName", method_name+"("+getParameterListToStr(parameters)+")");//方法名
				    method.put("parameters", parameters);//方法参数列表
				    method.put("file", file);
		        	list.add(method);
		        }
		          
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  return list;
	}
	/**
	 * 将参数列表转换成字符串的形式
	 * @param parameters
	 * @return
	 * 2015-1-14
	 * @tianming
	 */
	public String getParameterListToStr(List<String> parameters){
		String temp = "";
		for(int j = 0;j<parameters.size();j++){
			if(j!=parameters.size()-1){
				temp += parameters.get(j).split("@")[0]+",";
			}else{
				temp += parameters.get(j).split("@")[0];
			}
		}
		return temp;
	}
	@SuppressWarnings("unchecked")
	public String[] getMethodListString(List<HashMap> methodList){
		List<String> list = new ArrayList<String>();
		//初始化构造
		for(int i = 0;i<methodList.size();i++){
			HashMap<String,Object> method = methodList.get(i);
			String temp =  String.valueOf(method.get("methodName"));
			if(method.get("returnType")!=null){
				list.add(temp);
			}
		}
		String[] arr = new String[list.size()];
		list.toArray(arr);
		return arr;
	}
	@SuppressWarnings("unchecked")
	public String[] getConListString(List<HashMap> methodList){
		List<String> list = new ArrayList<String>();
		//初始化构造
		for(int i = 0;i<methodList.size();i++){
			HashMap<String,Object> method = methodList.get(i);
			String temp = String.valueOf(method.get("methodName"));
			if(method.get("returnType")==null){
				list.add(temp);
			}
		}
		String[] arr = new String[list.size()];
		list.toArray(arr);
		return arr;
	}
	/**
	 * 根据Import的内容匹配方法参数的类型
	 * @param importList
	 * @param parameters
	 * @return
	 * 2015-1-20
	 * @tianming
	 */
	@SuppressWarnings("unchecked")
	public List<String> getClassTypeByImport(List importList ,List parameters){
		  List<String> paramList = new ArrayList<String>();
		  for(int i = 0;i<parameters.size();i++){
			  SingleVariableDeclaration param = (SingleVariableDeclaration)parameters.get(i);
			  String type = String.valueOf(param.getType());
			  String name = String.valueOf(param.getName());
			  String str =  getType(importList,type)+"@"+name;
			  paramList.add(str);
		  }
		  return paramList;
	}
	/**
	 * 取得泛型的类型
	 * @param importList
	 * @param type
	 * @return
	 * 2015-1-20
	 * @tianming
	 */
	@SuppressWarnings("unchecked")
	public String getType(List importList,String type){
		String fx = "";
		if(type.indexOf("<")>-1){
			  fx = type.split("<")[1];
			  fx = fx.split(">")[0];
			  fx = getType(importList,fx);
			  type = type.split("<")[0];
		}
		
		for(int j = 0;j<importList.size();j++){
			  ImportDeclaration importDec =(ImportDeclaration) importList.get(j);
			  String importStr = String.valueOf(importDec.getName());
			  if(importStr.endsWith("."+type)){
				  if(fx.length()>0){
					  return importStr+"<"+fx+">";
				  }else{
					  return importStr;
				  }
			  }
	    }
		
		return type;
	}
}
