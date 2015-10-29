package util;

import java.util.HashMap;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

import business.AbstractModel;
import business.model.action.JspModel;
import business.model.action.LogicModel;
import business.model.action.MethodModel;
import business.model.action.StartModel;
import business.model.action.VariableModel;
import business.model.logic.EndModel;
import business.model.logic.LogicStartModel;

public class ToolTipUtil {
	public static IFigure setToolTips(Object model){
		String temp = " 显示名称："+((AbstractModel)model).getDisplay()+"\n";
		if(model instanceof StartModel){
			StartModel start = (StartModel)model;
			temp += " 请求参数：\n";
			List<HashMap<String,String>> parameter = start.getParameter();
			if(parameter!=null){
				for(int i = 0;i<parameter.size();i++){
					temp += "   "+parameter.get(i).get("paramType")+"  "+parameter.get(i).get("paramName")+" \n";
				}
			}
			temp += "\n 属性参数：\n";
			List<HashMap<String,String>> attr = start.getAttribute();
			if(attr!=null){
				for(int i = 0;i<attr.size();i++){
					temp += "   "+attr.get(i).get("paramType")+"  "+attr.get(i).get("paramName")+" \n";
				}
			}
			temp += "\n Session参数：\n";
			List<HashMap<String,String>> session = start.getSession();
			if(session!=null){
				for(int i = 0;i<session.size();i++){
					temp += "   "+session.get(i).get("paramType")+"  "+session.get(i).get("paramName")+" \n";
				}
			}
			return getTipsFigure(temp);
		}else if(model instanceof LogicStartModel){
			LogicStartModel start = (LogicStartModel)model;
			temp += " 输入参数：\n";
			List<HashMap<String,String>> parameter = start.getParameter();
			if(parameter!=null){
				for(int i = 0;i<parameter.size();i++){
					temp += "   "+parameter.get(i).get("paramType")+"  "+parameter.get(i).get("paramName")+" \n";
				}
			}
			return getTipsFigure(temp);
		}else if(model instanceof VariableModel){
			VariableModel variable = (VariableModel)model;
			temp += " 变量：\n";
			List<HashMap<String,String>> attr = variable.getVarList();
			if(attr!=null){
				for(int i = 0;i<attr.size();i++){
					String isArray = attr.get(i).get("isArray");
					temp +=  "   "+ attr.get(i).get("paramType")+("Y".equals(isArray)?"[]":"")+"   "+attr.get(i).get("paramName")+"=" + attr.get(i).get("paramValue")+"  \n";
				}
			}
			return getTipsFigure(temp);
		}else if(model instanceof MethodModel){
			MethodModel method = (MethodModel)model;
			temp += " 类路径："+method.getClassPath()+"\n";
			temp += " 方法名："+method.getMethodName()+"\n\n";
			temp += " 输入参数：\n";
			List<HashMap<String,String>> in = method.getIn();
			if(in!=null){
				for(int i = 0;i<in.size();i++){
					temp += "   "+in.get(i).get("paramType")+"  "+in.get(i).get("paramName")+" \n";
				}
			}
			temp += "\n 输出参数：\n";
			List<HashMap<String,String>> out = method.getOut();
			if(out!=null){
				for(int i = 0;i<out.size();i++){
					temp += "   "+out.get(i).get("paramType")+"  "+out.get(i).get("paramName")+" \n";
				}
			}
			return getTipsFigure(temp);
		}else if(model instanceof LogicModel){
			LogicModel method = (LogicModel)model;
			temp += " 输入参数：\n";
			List<HashMap<String,String>> in = method.getInList();
			if(in!=null){
				for(int i = 0;i<in.size();i++){
					temp += "   "+in.get(i).get("paramType")+"  "+in.get(i).get("paramName")+" \n";
				}
			}
			temp += "\n 输出参数：\n";
			List<HashMap<String,String>> out = method.getOutList();
			if(out != null){
				for(int i = 0;i<out.size();i++){
					temp += "   "+out.get(i).get("paramType")+"  "+out.get(i).get("paramName")+" \n";
				}
			}
			return getTipsFigure(temp);
		}else if(model instanceof EndModel){
			EndModel end = (EndModel)model;
			temp += " 输入参数：\n";
			List<HashMap<String,String>> out = end.getWriteParam();
			if(out!=null){
				for(int i = 0;i<out.size();i++){
					temp += "   "+out.get(i).get("paramType")+"  "+out.get(i).get("paramName")+" \n";
				}
			}
			return getTipsFigure(temp);
		}else if(model instanceof JspModel){
			JspModel end = (JspModel)model;
			temp += " 返回客户端数据形式："+("0".equals(end.getReturnType())?"同步":"异步")+"\n";
			temp += " 返回jsp路径:"+end.getPath()+" \n";
			temp += " 输出到页面的参数：\n";
			List<HashMap<String,String>> out = end.getWriteParam();
			if(out!=null){
				for(int i = 0;i<out.size();i++){
					temp += "   "+out.get(i).get("paramType")+"  "+out.get(i).get("paramName")+" \n";
				}
			}
			return getTipsFigure(temp);
		}
		
		return null;
	}
	
	public static IFigure getTipsFigure(String info){
		Label tips = new Label();
		tips.setText(info);
		return tips;
	}
}
