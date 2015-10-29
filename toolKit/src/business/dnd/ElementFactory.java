package business.dnd;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jdt.internal.core.SourceMethod;
import org.eclipse.jdt.internal.core.SourceType;

import business.codesource.AnalysisJavaFile;
import business.model.action.MethodModel;

@SuppressWarnings("restriction")
public class ElementFactory implements CreationFactory {
	private Object dragData = null;
	private IFile file = null;
	public ElementFactory(Object dragData,IFile file){
		 this.dragData = dragData;
		 this.file = file;
	}
	public ElementFactory(Object dragData){
		this.dragData = dragData;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Object getNewObject() {
		MethodModel model = new MethodModel();
		AnalysisJavaFile analy = new AnalysisJavaFile();
		String name = null;
		String temp = "";
		if(dragData instanceof SourceMethod){
			SourceMethod method = (SourceMethod)dragData;
			name = method.getElementName();
			try {
				String[] parameterType = method.getParameterTypes();
				for(int i = 0;i<parameterType.length;i++){
					String paramType = parameterType[i];
					if(paramType.startsWith("[Q")){
						temp += paramType.substring(2,paramType.length()-1) + "[]";
					}else if(paramType.startsWith("Q")){
						temp += paramType.substring(1, paramType.length()-1);
					}
					if(i!=parameterType.length-1){
						temp += ",";
					}
				}
				SourceType type = (SourceType)method.getParent();
				model.setDisplay(name);
				model.setClassPath(type.getFullyQualifiedName());
				model.setMethodName(name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String filePath = file.getProject().getLocation().toFile().getAbsolutePath()+"\\src\\"+model.getClassPath().replace(".", "\\")+".java";
			List<HashMap> methodList = analy.getJavaInfo(new File(filePath));
			for(int i = 0;i<methodList.size();i++){
				HashMap<String,Object> meth = methodList.get(i);
				String methodName = String.valueOf(meth.get("methodName"));//方法名
				List<HashMap<String,String>> inList = new ArrayList<HashMap<String,String>>();//方法参数
				List<HashMap<String,String>> returnBack = new ArrayList<HashMap<String,String>>();//方法返回值类型
				String returnType = String.valueOf(meth.get("returnType"));
				String copyTT= "";
				if(methodName.equals(name)){//方法名匹配
					List parameters = (List)meth.get("parameters");
					String tt = "";
					
					for(int j = 0;j<parameters.size();j++){
						String splitType = String.valueOf(parameters.get(j)).split("@")[0];
						if(j!=parameters.size()-1){
							copyTT += splitType+",";
						}else{
							copyTT += splitType;
						}
						
						String[] arrSplitType = splitType.split("\\.");
						splitType = arrSplitType[arrSplitType.length-1];
						if(j!=parameters.size()-1){
							tt += splitType+",";
						}else{
							tt += splitType;
						}
						
						HashMap<String,String> paramMap = new HashMap<String,String>();
						paramMap.put("paramType", String.valueOf(parameters.get(j)).split("@")[0]);
						paramMap.put("paramName", String.valueOf(parameters.get(j)).split("@")[1]);
						paramMap.put("isArray","false");
						paramMap.put("desc", "");
						inList.add(paramMap);
					}
					if(!"void".equals(returnType)){
						HashMap<String,String>  param = new HashMap<String,String>();
						param.put("paramType", returnType);
						param.put("paramName", "name");
						param.put("isArray","false");
						param.put("desc", "");
						returnBack.add(param);
					}
					if(tt.equals(temp)){//参数列表匹配
						model.setComboMethodTxt(methodName+"("+copyTT+")");
						model.setIn(inList);
						model.setOut(returnBack);
						break;
					}
				}
			}
		 }else if(dragData instanceof HashMap){
			 HashMap method = (HashMap)dragData;
			 name = (String)method.get("methodName");
			 String nameNoParameter = (String)method.get("name");
			 File java_file =(File) method.get("file");
			 try {
				String path = java_file.getAbsolutePath();
				String projectPath = file.getProject().getLocation().toFile().getAbsolutePath();
				
				model.setDisplay(nameNoParameter);
				model.setClassPath(path.replace(projectPath+"\\src\\", "").replace("\\", ".").replace(".java", ""));
				model.setMethodName(nameNoParameter);
			 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
			 List<HashMap> methodList = analy.getJavaInfo(java_file);
			 for(int i = 0;i<methodList.size();i++){
				HashMap<String,Object> meth = methodList.get(i);
				String methodName = String.valueOf(meth.get("methodName"));//方法名
				List<HashMap<String,String>> inList = new ArrayList<HashMap<String,String>>();//方法参数
				List<HashMap<String,String>> returnBack = new ArrayList<HashMap<String,String>>();//方法返回值类型
				String returnType = String.valueOf(meth.get("returnType"));
				if(name.equals(methodName)){
					model.setComboMethodTxt(methodName);
					List parameters = (List)meth.get("parameters");
					for(int j = 0;j<parameters.size();j++){
						HashMap<String,String> paramMap = new HashMap<String,String>();
						paramMap.put("paramType", String.valueOf(parameters.get(j)).split("@")[0]);
						paramMap.put("paramName", String.valueOf(parameters.get(j)).split("@")[1]);
						paramMap.put("isArray","false");
						paramMap.put("desc", "");
						inList.add(paramMap);
					}
					if(!"void".equals(returnType)){
						HashMap<String,String>  param = new HashMap<String,String>();
						param.put("paramType", returnType);
						param.put("paramName", "name");
						param.put("isArray","false");
						param.put("desc", "");
						returnBack.add(param);
					}
					model.setIn(inList);
					model.setOut(returnBack);
				}
			 }
		 }
		
		return model;
	}

	@Override
	public Object getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

}
