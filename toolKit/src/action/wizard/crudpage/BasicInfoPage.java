package action.wizard.crudpage;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ui.UIAbstractModel;
import ui.model.UIContentsModel;
import ui.model.grid.ColumnBarModel;
import ui.model.grid.ColumnModel;
import ui.model.grid.GridModel;
import ui.model.liger.FormFieldModel;
import ui.model.liger.FormModel;
import ui.model.toolbar.ToolBarItemModel;
import ui.util.UIGenerateCode;
import util.CacheInit;
import util.ConnFactory;
import util.DicUtil;
import util.FileUtil;
import util.FreemarkerUtil;
import util.GenerateCode;
import util.Log;
import util.PublicTableSet;
import util.thread.ThreadTask;
import action.wizard.ConnAbstractWizardPage;
import action.wizard.InitConnectionThread;
import business.model.ContentsModel;
import business.model.action.JspModel;
import business.model.action.MethodModel;
import business.model.action.PackageVariableModel;
import business.model.action.StartModel;
import business.model.logic.QueryModel;

public class BasicInfoPage extends ConnAbstractWizardPage {
	private Combo combo_1;
	private Text curd_modifyctlText;
	private Text curd_insertctlText_1;
	private Text curd_listctlText;
	private Text curd_resultuiText;
	private Text curd_modifyuiText;
	private Text curd_insertuiText;
	private Text curd_searchlistuiText;
	private Text financemangeText;
	private Text pagePath;
	private Text controllerPath;
	private Text text;
	private Combo combo;
	private Map<String,Object> allDataSource;
	private AutoCompleteField au = null;
	private ListPage list = null;
	private ModifyConfigPage list2 = null;
	private HashMap<String,String> info = null;
	private List<String> names = null;
	private IProject[] projects = null;
	
	/**
	 * 生成资源目录
	 * @throws Exception
	 */
	public void createFolder()throws Exception{
		String controllerPath  = this.controllerPath.getText()+"/"+text.getText().toLowerCase();//controller存放路径
		String pagePath = this.pagePath.getText()+"/"+text.getText().toLowerCase();
		String financemangeText = this.financemangeText.getText()+"/"+text.getText().toLowerCase()+"/service";
		IProject project = this.getSelectProject();
		IFolder pageFloder = project.getFolder(pagePath);
		if(!pageFloder.exists()){
			pageFloder.create(true,true,null);
		}
		IFolder ctrlFloder = project.getFolder(controllerPath);
		if(!ctrlFloder.exists()){
			ctrlFloder.create(true,true,null);
		}
		IFolder financemangeTextf = project.getFolder(financemangeText);
		if(!financemangeTextf.exists()){
			financemangeTextf.create(true,true,null);
		}
	}
	public IProject getSelectProject(){
		String projectName = names.get(combo_1.getSelectionIndex());
		for(int i = 0;i<projects.length;i++){
			if(projectName.equals(projects[i].getName())){
				return projects[i];
			}
		}
		return null;
	}
	public IFolder getPageFolder(){
		String pagePath = this.pagePath.getText()+"/"+text.getText().toLowerCase();
		IProject project = this.getSelectProject();
		IFolder pageFloder = project.getFolder(pagePath);
		return pageFloder;
	}
	public IFolder getControllerFolder(){
		String controllerPath  = this.controllerPath.getText()+"/"+text.getText().toLowerCase();//controller存放路径
		IProject project = this.getSelectProject();
		IFolder ctrlFloder = project.getFolder(controllerPath);
		return ctrlFloder;
	}
	public IFolder getServiceFolder(){
		String controllerPath  = this.financemangeText.getText()+"/"+text.getText().toLowerCase()+"/service";//service存放路径
		IProject project = this.getSelectProject();
		IFolder ctrlFloder = project.getFolder(controllerPath);
		return ctrlFloder;
	}
	public IFile copyFileByTemplate(IProject project,IFolder resourceFolder,String templateName,String fileName,int i)throws Exception{
		IFile file = (IFile)project.findMember("/plugin_resource/template/curd/"+templateName);//加载模版配置文件
		FileUtil.copyFile(file.getLocation().toFile(), resourceFolder.getLocation().toFile(),fileName);//生成列表查询配置文件
		resourceFolder.refreshLocal(1,null);//刷新文件夹
		file = (IFile)project.findMember((i==0?controllerPath.getText():pagePath.getText())+"/"+text.getText().toLowerCase() +"/"+fileName);
		return file;
	}
	/**
	 * 生成新增配置文件及代码
	 * 文件名规则：
	 * 			新增页面： 	 automake/page/表名/表名Modify.ui
	 * 			Controller：   automake/controller/表名/表名ModifyController.ctl
	 * @throws Exception
	 */
	public void createModifyCode()throws Exception{
		IProject project = this.getSelectProject();
		String tableName = text.getText().toLowerCase();
		List<HashMap<String,String>> columns = PublicTableSet.getTableData(list.tableViewer);
		IFolder pageFloder = this.getPageFolder();
		String insertJsp = tableName + "Modify.ui";
		String insertResultJsp = tableName + "ModifyResult.ui";
		UIGenerateCode code = new UIGenerateCode();
		IFile file = copyFileByTemplate(project,pageFloder,curd_insertuiText.getText(),insertJsp,1);//生成新增页面配置文件
		IFile file2 = copyFileByTemplate(project,pageFloder,curd_resultuiText.getText(),insertResultJsp,1);//生成新增页面配置文件
		//读取生成的配置文件信息，生成数据模型
		UIContentsModel contents = (UIContentsModel)code.readFile(file.getLocation().toFile());
		contents.setFile(file);
		UIContentsModel result = (UIContentsModel)code.readFile(file2.getLocation().toFile());
		result.setFile(file2);
		//取得表单对象
		FormModel form = (FormModel)contents.getModelByName(contents, "FormModel");
		FormModel resultForm = (FormModel)result.getModelByName(result, "FormModel");
		//生成表单要素项
		for(int i = 0;i<columns.size();i++){
			HashMap<String,String> map =  columns.get(i);
			UIAbstractModel child = null;
			String comp_name = DicUtil.getModelTypeByInputType(map.get("input_type"));
			String col_name = map.get("col_name").toLowerCase();
			child = new FormFieldModel(comp_name);
			child.setVal("label",map.get("col_dsc"));
			child.setVal("name",col_name);
			child.setVal("tab_name",tableName);
			child.setVal("value","${"+tableName+"."+col_name+"}");
			child.parentModel = form;
			form.getChildren().add(child);
			UIAbstractModel label = new FormFieldModel("Label");
			label.setVal("name", col_name);
			//if("Text".equals(comp_name)){
				label.setVal("value", "${"+tableName+"."+col_name+"}");
			//}else{
			//	label.setVal("value", "${"+tableName+"."+col_name+"_zh}");
			//}
			label.setVal("label", map.get("col_dsc"));
			label.setVal("type", "label");
			resultForm.getChildren().add(label);
		}
		String modifySelectController = firstUpper(tableName) + "SelectForModifyController";
		form.setVal("url",  controllerPath.getText()+tableName+"/"+firstUpper(tableName) + "ModifyController.do" );
		code.saveToFile(file.getLocation().toFile(), contents);
		code.createJSP(file.getLocation().toFile(), contents, project);
		
		code.saveToFile(file2.getLocation().toFile(), result);
		code.createJSP(file2.getLocation().toFile(), result, project);
		pageFloder.refreshLocal(1,null);
		
		
		GenerateCode javaCode = new GenerateCode();
		IFolder ctrlFloder = this.getControllerFolder();
		
		//生成查询实体controller
		
		IFile file3 =  copyFileByTemplate(project,ctrlFloder,"curd_modify_select.ctl",modifySelectController+".ctl",0);//加载模版配置文件;
		ContentsModel select = (ContentsModel)javaCode.readFile(file3.getLocation().toFile());
		StartModel start = (StartModel)select.getModelByType(StartModel.class);
		MethodModel method = (MethodModel)select.getModelByType(MethodModel.class);
		method.setClassPath(financemangeText.getText().replace("/src/", "").replaceAll("/", ".")+"."+tableName+".service."+firstUpper(tableName)+"Service");
		method.setMethodName(tableName+"SelectFor");
		method.setComboMethodTxt(tableName+"SelectFor(com.dhcc.financemanage.entity."+firstUpper(tableName)+"Entity)");
		HashMap<String,String> out = new HashMap<String,String>();
		out.put("paramType", "com.dhcc.financemanage.entity."+firstUpper(tableName)+"Entity");
		out.put("paramName", tableName.toLowerCase());
		out.put("isArray", "N");
		method.getOut().add(out);
		HashMap<String,String> in = new HashMap<String,String>();
		in.put("paramType", "com.dhcc.financemanage.entity."+firstUpper(tableName)+"Entity");
		in.put("paramName",tableName+"Entity");
		in.put("isArray", "N");
		method.getIn().add(in);
		PackageVariableModel packageModel = (PackageVariableModel)select.getModelByType(PackageVariableModel.class);
		packageModel.setParamType(1);
		packageModel.setTableName(tableName);
		packageModel.setParamName(tableName+"Entity");
		for(int i = 0;i<columns.size();i++){
			HashMap<String,String> map =  columns.get(i);
			HashMap<String,String> param = new HashMap<String,String>();
			if("是".equals(map.get("is_key"))){
				param.put("paramName", map.get("col_name").toLowerCase());
				param.put("paramName2", map.get("col_name").toLowerCase());
				param.put("paramType", "String");
				param.put("isDecode", "N");
				start.getParameter().add(param);
				
				HashMap<String,String> param2 = new HashMap<String,String>();
				param2.put("key", map.get("col_name").toLowerCase());
				param2.put("value", map.get("col_name").toLowerCase());
				param2.put("coltype", "VARCHAR2");
				packageModel.getValues().add(param2);
			}
		}
		JspModel jsp = (JspModel)select.getModelByType(JspModel.class);
		jsp.setReturnType("0");
		jsp.setPath(pagePath.getText()+"/"+tableName+"/"+tableName+"Modify.jsp");
		HashMap<String,String> returnParam = new HashMap<String,String>();
		returnParam.put("paramType", "com.dhcc.financemanage.entity."+firstUpper(tableName)+"Entity");
		returnParam.put("paramName", tableName.toLowerCase());
		returnParam.put("translation", "Y");
		returnParam.put("tableNames", tableName.toLowerCase());
		jsp.getWriteParam().add(returnParam);
		javaCode.saveToFile(file3.getLocation().toFile(), select);
		javaCode.createAction(select, file3.getLocation().toFile(), project, "action.ftl");
		
		//生成Controller配置信息
		String modifyController = firstUpper(tableName) + "ModifyController";
		file =  copyFileByTemplate(project,ctrlFloder,curd_modifyctlText.getText(),modifyController+".ctl",0);//加载模版配置文件;
		ContentsModel graphic = (ContentsModel)javaCode.readFile(file.getLocation().toFile());
		start = (StartModel)graphic.getModelByType(StartModel.class);
		start.jsp = pagePath.getText()+"/"+tableName +"/"+insertJsp;
		packageModel = (PackageVariableModel)graphic.getModelByType(PackageVariableModel.class);
		packageModel.setParamType(1);
		packageModel.setTableName(tableName);
		packageModel.setParamName(tableName+"Entity");
		for(int i = 0;i<columns.size();i++){
			HashMap<String,String> map =  columns.get(i);
			HashMap<String,String> param = new HashMap<String,String>();
			param.put("paramName", map.get("col_name").toLowerCase());
			param.put("paramName2", map.get("col_name").toLowerCase());
			param.put("paramType", "String");
			param.put("isDecode", "N");
			start.getParameter().add(param);
			
			HashMap<String,String> param2 = new HashMap<String,String>();
			param2.put("key", map.get("col_name").toLowerCase());
			param2.put("value", map.get("col_name").toLowerCase());
			param2.put("coltype", "VARCHAR2");
			packageModel.getValues().add(param2);
		}
		method = null;
		method = (MethodModel)graphic.getModelByType(MethodModel.class);
		in = new HashMap<String,String>();
		in.put("paramType", "com.dhcc.financemanage.entity."+firstUpper(tableName)+"Entity");
		in.put("paramName", tableName+"Entity");
		in.put("isArray", "N");
		method.getIn().add(in);
		out = new HashMap<String,String>();
		out.put("paramType", "com.dhcc.financemanage.entity."+firstUpper(tableName)+"Entity");
		out.put("paramName", tableName.toLowerCase());
		out.put("isArray", "N");
		method.getOut().add(out);
		method.setMethodName(tableName+"Modify");
		method.setComboMethodTxt(tableName+"Modify(com.dhcc.financemanage.entity."+firstUpper(tableName)+"Entity)");
		method.setClassPath(financemangeText.getText().replace("/src/", "").replaceAll("/", ".")+"."+tableName+".service."+firstUpper(tableName)+"Service");
		jsp = (JspModel)graphic.getModelByType(JspModel.class);
		jsp.setReturnType("0");
		jsp.setPath(pagePath.getText()+"/"+tableName+"/"+tableName+"ModifyResult.jsp");
		HashMap<String,String> param = new HashMap<String,String>();
		param.put("paramType", "com.dhcc.financemanage.entity."+firstUpper(tableName)+"Entity");
		param.put("paramName", tableName.toLowerCase());
		param.put("translation", "Y");
		param.put("tableNames", tableName.toLowerCase());
		jsp.getWriteParam().add(param);
		javaCode.saveToFile(file.getLocation().toFile(), graphic);
		javaCode.createAction(graphic, file.getLocation().toFile(), project, "action.ftl");
		ctrlFloder.refreshLocal(1,null);//刷新文件夹
	}
	
	/**
	 * 生成新增配置文件及代码
	 * 文件名规则：
	 * 			新增页面： 	 automake/page/表名/表名Insert.ui
	 * 			Controller：   automake/controller/表名/表名InsertController.ctl
	 * @throws Exception
	 */
	public void createInsertCode()throws Exception{
		IProject project = this.getSelectProject();
		String tableName = text.getText().toLowerCase();
		List<HashMap<String,String>> columns = PublicTableSet.getTableData(list.tableViewer);
		IFolder pageFloder = this.getPageFolder();
		String insertJsp = tableName + "Insert.ui";
		String insertResultJsp = tableName + "InsertResult.ui";
		UIGenerateCode code = new UIGenerateCode();
		IFile file = copyFileByTemplate(project,pageFloder,curd_insertuiText.getText(),insertJsp,1);//生成新增页面配置文件
		IFile file2 = copyFileByTemplate(project,pageFloder,curd_resultuiText.getText(),insertResultJsp,1);//生成新增页面配置文件
		//读取生成的配置文件信息，生成数据模型
		UIContentsModel contents = (UIContentsModel)code.readFile(file.getLocation().toFile());
		contents.setFile(file);
		UIContentsModel result = (UIContentsModel)code.readFile(file2.getLocation().toFile());
		result.setFile(file2);
		//取得表单对象
		FormModel form = (FormModel)contents.getModelByName(contents, "FormModel");
		FormModel resultForm = (FormModel)result.getModelByName(result, "FormModel");
		//生成表单要素项
		for(int i = 0;i<columns.size();i++){
			HashMap<String,String> map =  columns.get(i);
			UIAbstractModel child = null;
			String comp_name = DicUtil.getModelTypeByInputType(map.get("input_type"));
			String col_name = map.get("col_name").toLowerCase();
			child = new FormFieldModel(comp_name);
			child.setVal("label",map.get("col_dsc"));
			child.setVal("name",col_name);
			child.setVal("tab_name",tableName);
			child.parentModel = form;
			form.getChildren().add(child);
			UIAbstractModel label = new FormFieldModel("Label");
			label.setVal("name", col_name);
			//if("Text".equals(comp_name)){
				label.setVal("value", "${"+tableName+"."+col_name+"}");
			//}else{
			//	label.setVal("value", "${"+tableName+"."+col_name+"_zh}");
			//}
			label.setVal("label", map.get("col_dsc"));
			label.setVal("type", "label");
			resultForm.getChildren().add(label);
		}
		String insertController = firstUpper(tableName) + "InsertController";
		form.setVal("url", controllerPath.getText()+insertController+".do");
		code.saveToFile(file.getLocation().toFile(), contents);
		code.createJSP(file.getLocation().toFile(), contents, project);
		
		code.saveToFile(file2.getLocation().toFile(), result);
		code.createJSP(file2.getLocation().toFile(), result, project);
		pageFloder.refreshLocal(1,null);
		
		//生成Controller配置信息
		GenerateCode javaCode = new GenerateCode();
		IFolder ctrlFloder = this.getControllerFolder();
		
		file =  copyFileByTemplate(project,ctrlFloder,curd_insertctlText_1.getText(),insertController+".ctl",0);//加载模版配置文件;

		ContentsModel graphic = (ContentsModel)javaCode.readFile(file.getLocation().toFile());
		StartModel start = (StartModel)graphic.getModelByType(StartModel.class);
		start.jsp = pagePath.getText()+"/"+tableName +"/"+insertJsp;
		PackageVariableModel packageModel = (PackageVariableModel)graphic.getModelByType(PackageVariableModel.class);
		packageModel.setParamType(1);
		packageModel.setTableName(tableName);
		packageModel.setParamName(tableName+"Entity");
		for(int i = 0;i<columns.size();i++){
			HashMap<String,String> map =  columns.get(i);
			HashMap<String,String> param = new HashMap<String,String>();
			param.put("paramName", map.get("col_name").toLowerCase());
			param.put("paramName2", map.get("col_name").toLowerCase());
			param.put("paramType", "String");
			param.put("isDecode", "N");
			start.getParameter().add(param);
			
			HashMap<String,String> param2 = new HashMap<String,String>();
			param2.put("key", map.get("col_name").toLowerCase());
			param2.put("value", map.get("col_name").toLowerCase());
			param2.put("coltype", "VARCHAR2");
			packageModel.getValues().add(param2);
		}
		
		MethodModel method = (MethodModel)graphic.getModelByType(MethodModel.class);
		HashMap<String,String> in = new HashMap<String,String>();
		in.put("paramType", "com.dhcc.financemanage.entity."+firstUpper(tableName)+"Entity");
		in.put("paramName", tableName+"Entity");
		in.put("isArray", "N");
		method.getIn().add(in);
		HashMap<String,String> out = new HashMap<String,String>();
		out.put("paramType", "com.dhcc.financemanage.entity."+firstUpper(tableName)+"Entity");
		out.put("paramName", tableName.toLowerCase());
		out.put("isArray", "N");
		method.getOut().add(out);
		method.setMethodName(tableName+"Insert");
		method.setComboMethodTxt(tableName+"Insert(com.dhcc.financemanage.entity."+firstUpper(tableName)+"Entity)");
		method.setClassPath(financemangeText.getText().replace("/src/", "").replaceAll("/", ".")+"."+tableName+".service."+firstUpper(tableName)+"Service");
		JspModel jsp = (JspModel)graphic.getModelByType(JspModel.class);
		jsp.setReturnType("0");
		jsp.setPath(pagePath.getText()+"/"+tableName+"/"+tableName+"InsertResult.jsp");
		HashMap<String,String> param = new HashMap<String,String>();
		param.put("paramType", "com.dhcc.financemanage.entity."+firstUpper(tableName)+"Entity");
		param.put("paramName", tableName.toLowerCase());
		param.put("translation", "Y");
		param.put("tableNames", tableName.toLowerCase());
		jsp.getWriteParam().add(param);
		javaCode.saveToFile(file.getLocation().toFile(), graphic);
		javaCode.createAction(graphic, file.getLocation().toFile(), project, "action.ftl");
		ctrlFloder.refreshLocal(1,null);//刷新文件夹
	}
	/**
	 * 生成业务层代码
	 * @throws Exception
	 */
	public void createService() throws Exception{
		
		IProject project = this.getSelectProject();
		String projectPath = project.getLocation().toFile().getAbsolutePath();
		FreemarkerUtil util = new FreemarkerUtil();
		String tableName = text.getText().toLowerCase();
		String path = financemangeText.getText() + "/"+tableName;
		File outputFile = new File(projectPath + path+"/service/"+firstUpper(tableName)+"Service.java");
		Log.write("生成业务层代码："+outputFile.getAbsolutePath());
		if(!outputFile.exists()){
			outputFile.createNewFile();
		}
		Map<String,Object> root = new HashMap<String,Object>();
		root.put("package", path.replace("/src/","").replaceAll("/", ".")+".service");
		root.put("tableName", tableName.toLowerCase());
		root.put("columns", PublicTableSet.getTableData(list.tableViewer));
		String ftl_path = projectPath + "/plugin_resource/ftl/";
		util.createCode(ftl_path, root, "crudService.ftl", outputFile);
		IFolder folder = this.getServiceFolder();
		folder.refreshLocal(1,null);
	}
	public String firstUpper(String tableName){
		return (tableName.substring(0,1).toUpperCase()+tableName.substring(1,tableName.length()).toLowerCase());
	}
	/**
	 * 生成新增配置文件及代码
	 * 文件名规则：
	 * 			新增页面： 	 automake/page/表名/表名List.ui
	 * 			Controller：   automake/controller/表名/表名ListController.ctl
	 * @throws Exception
	 */
	public void createSearchList() throws Exception{
		IProject project = this.getSelectProject();
		String tableName = text.getText().toLowerCase();
		List<HashMap<String,String>> columns = PublicTableSet.getTableData(list.tableViewer);
		IFolder pageFloder = this.getPageFolder();
		
		UIGenerateCode code = new UIGenerateCode();
		IFile file = (IFile)project.findMember("/plugin_resource/template/curd/"+curd_searchlistuiText.getText());//加载模版配置文件
		FileUtil.copyFile(file.getLocation().toFile(), pageFloder.getLocation().toFile(),tableName + "List.ui");//生成列表查询配置文件
		pageFloder.refreshLocal(1,null);//刷新文件夹
		file = (IFile)project.findMember(pagePath.getText()+"/"+tableName +"/"+tableName+ "List.ui");
		UIContentsModel contents = (UIContentsModel)code.readFile(file.getLocation().toFile());//读取生成的配置文件信息，生成数据模型
		contents.setFile(file);
		ColumnBarModel columnsBar = (ColumnBarModel)contents.getModelByName(contents, "ColumnBarModel");
		GridModel grid = (GridModel)contents.getModelByName(contents, "GridModel");
		FormModel form = (FormModel)contents.getModelByName(contents, "FormModel");
		ToolBarItemModel OpenWindow = (ToolBarItemModel)contents.getModelByName(contents,"OpenWindow");
		OpenWindow.setVal("url",pagePath.getText()+"/"+tableName +"/"+tableName+ "Insert.jsp");
		OpenWindow.setVal("title","新增");
		ToolBarItemModel ModifyWindow = (ToolBarItemModel)contents.getModelByName(contents,"ModifyWindow");
		ModifyWindow.setVal("url",controllerPath.getText()+"/"+tableName +"/"+ firstUpper(tableName) + "SelectForModifyController.do");
		ModifyWindow.setVal("title","修改");
		//通过配置信息，生成表格列
		String sql = "";
		List<HashMap<String,String>> queryList = new ArrayList<HashMap<String,String>>();
		for(int i = 0;i<columns.size();i++){
			HashMap<String,String> map =  columns.get(i);
			String disp_yn = map.get("disp_yn");
			String query_yn = map.get("query_yn");
			if("是".equals(disp_yn)){
				ColumnModel model = new ColumnModel("ColumnModel");
				model.setVal("field", map.get("col_name").toLowerCase());
				model.setVal("title", map.get("col_dsc"));
				sql += map.get("col_name")+",";
				columnsBar.addChild(model);
				model.setFile(file);//增加列
			}
			//增加查询条件
			if("是".equals(query_yn)){
				queryList.add(map);
				UIAbstractModel child = null;
				String comp_name = DicUtil.getModelTypeByInputType(map.get("input_type"));
				String col_name = map.get("col_name").toLowerCase();
				child = new FormFieldModel(comp_name);
				child.setVal("label",map.get("col_dsc"));
				child.setVal("name",col_name);
				child.setVal("tab_name",tableName);
				child.parentModel = form;
				form.getChildren().add(child);
			}
		}
		//配置查询url
		grid.setVal("url",controllerPath.getText()+tableName +"/"+firstUpper(tableName)+"ListController.do");
		code.saveToFile(file.getLocation().toFile(), contents);
		code.createJSP(file.getLocation().toFile(), contents, project);
		pageFloder.refreshLocal(1,null);
		
		//生成Controller配置信息
		GenerateCode javaCode = new GenerateCode();
		IFolder ctrlFloder = this.getControllerFolder();
		file = (IFile)project.findMember("/plugin_resource/template/curd/"+curd_listctlText.getText());//加载模版配置文件
		FileUtil.copyFile(file.getLocation().toFile(), ctrlFloder.getLocation().toFile(),firstUpper(tableName) + "ListController.ctl");//生成列表查询配置文件
		ctrlFloder.refreshLocal(1,null);//刷新文件夹
		file = (IFile)project.findMember(controllerPath.getText()+"/"+tableName+"/"+firstUpper(tableName) + "ListController.ctl");//加载模版配置文件
		ContentsModel graphic = (ContentsModel)javaCode.readFile(file.getLocation().toFile());
		
		QueryModel query = (QueryModel)graphic.getModelByType(QueryModel.class);
		query.setTab_name(tableName);
		query.setSql("select "+sql.substring(0,sql.length()-1)+" from "+tableName);
		
		StartModel start = (StartModel)graphic.getModelByType(StartModel.class);
		start.jsp = pagePath.getText()+"/"+tableName +"/"+tableName+ "List.ui";
		
		JspModel jsp = (JspModel)graphic.getModelByType(JspModel.class);
		jsp.setReturnType("1");
		HashMap<String,String> temp = new HashMap<String,String>();
		temp.put("paramName", "ipage");
		temp.put("paramType", "com.utils.entity.Ipage");
		temp.put("translation", "N");
		jsp.getWriteParam().add(temp);
		for(int i = 0;i<queryList.size();i++){
			HashMap<String,String> map =  columns.get(i);
			HashMap<String,String> param = new HashMap<String,String>();
			param.put("paramName", map.get("col_name").toLowerCase());
			param.put("paramName2", map.get("col_name").toLowerCase());
			param.put("paramType", "String");
			param.put("isDecode", "N");
			//增加查询条件
			start.getParameter().add(param);
			
			HashMap<String,String> queryCondition = new HashMap<String,String>();
			queryCondition.put("link", "and");
			queryCondition.put("colName", map.get("col_name").toLowerCase());
			queryCondition.put("comp", "=");
			queryCondition.put("paramName", map.get("col_name").toLowerCase());
			queryCondition.put("paramType", "String");
			query.getQueryConditon().add(queryCondition);
		}
		javaCode.saveToFile(file.getLocation().toFile(), graphic);
		javaCode.createAction(graphic, file.getLocation().toFile(), project, "action.ftl");
		ctrlFloder.refreshLocal(1,null);//刷新文件夹
	}
	
	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public BasicInfoPage() {
		super("wizardPage");
		setTitle("单表CURD基础信息配置");
		setDescription("");
	}
	public void setList(ListPage page){
		this.list = page;
	}
	public void setModify(ModifyConfigPage modify){
		this.list2 = modify;
	}
	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		initialize();
		setControl(container);

		final Label label = new Label(container, SWT.NONE);
		label.setText("生成controller路径:");
		label.setBounds(10, 46, 121, 17);

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("选择数据源:");
		label_1.setBounds(10, 15, 121, 17);
		combo = new Combo(container, SWT.NONE);
		combo.setBounds(137, 7, 121, 25);
		allDataSource = CacheInit.getDataSource(combo,"Y");
		dataSourceChange();
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				dataSourceChange();
			}
		});
		final Label label_2 = new Label(container, SWT.NONE);
		label_2.setText("表名：");
		label_2.setBounds(276, 15, 40, 17);

		text = new Text(container, SWT.BORDER);
		text.setBounds(322, 7, 109, 25);
		text.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent arg0) {
				System.out.println(text.getText());
				String owner = info.get("owner");
				List<Map<String,String>> tables = ConnFactory.queryList("select col_name,col_dsc,input_type from data_table where owner_USR='"+owner.toUpperCase()+"' and tab_name='"+(text.getText().toUpperCase())+"'", con);
				List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
				for(int i = 0;i<tables.size();i++){
					Map<String,Object> row = new HashMap<String,Object>();
					row.put("col_name", tables.get(i).get("col_name").toLowerCase());
					row.put("col_dsc", tables.get(i).get("col_dsc"));
					row.put("input_type", tables.get(i).get("input_type"));
					row.put("disp_yn","是");
					row.put("query_yn","否");
					row.put("is_element", "是");
					row.put("is_modify", "是");
					row.put("is_key", "否");
					data.add(row);
					row = null;
				}
				list.tableViewer.setInput(data);
				list2.tableViewer.setInput(data);
			}
			
		});
		controllerPath = new Text(container, SWT.BORDER);
		controllerPath.setText("/automake/controller/");
		controllerPath.setBounds(137, 43, 472, 25);

		final Button button_1 = new Button(container, SWT.NONE);
		button_1.setText("button");
		button_1.setBounds(615, 41, 64, 27);

		final Label label_3 = new Label(container, SWT.NONE);
		label_3.setText("生成PAGE存放路径:");
		label_3.setBounds(10, 85, 121, 17);

		pagePath = new Text(container, SWT.BORDER);
		pagePath.setText("/automake/page/");
		pagePath.setBounds(137, 82, 472, 25);

		final Button button_1_1 = new Button(container, SWT.NONE);
		button_1_1.setBounds(615, 80, 64, 27);
		button_1_1.setText("button");

		final Label label_3_1 = new Label(container, SWT.NONE);
		label_3_1.setBounds(10, 125, 121, 17);
		label_3_1.setText("生成Service存放路径:");

		financemangeText = new Text(container, SWT.BORDER);
		financemangeText.setText("/src/financemanage");
		financemangeText.setBounds(137, 122, 472, 25);

		final Button button_1_1_1 = new Button(container, SWT.NONE);
		button_1_1_1.setBounds(615, 120, 64, 27);
		button_1_1_1.setText("button");

		final Label label_3_1_1 = new Label(container, SWT.NONE);
		label_3_1_1.setBounds(10, 160, 121, 17);
		label_3_1_1.setText("列表查询模版:");

		curd_searchlistuiText = new Text(container, SWT.BORDER);
		curd_searchlistuiText.setText("curd_searchList.ui");
		curd_searchlistuiText.setBounds(137, 157, 167, 25);

		curd_insertuiText = new Text(container, SWT.BORDER);
		curd_insertuiText.setText("curd_insert.ui");
		curd_insertuiText.setBounds(137, 190, 167, 25);

		final Label label_3_1_1_1 = new Label(container, SWT.NONE);
		label_3_1_1_1.setBounds(10, 193, 121, 17);
		label_3_1_1_1.setText("新增页面模版:");

		final Label label_3_1_1_1_1 = new Label(container, SWT.NONE);
		label_3_1_1_1_1.setBounds(10, 225, 121, 17);
		label_3_1_1_1_1.setText("修改页面模版:");

		curd_modifyuiText = new Text(container, SWT.BORDER);
		curd_modifyuiText.setText("curd_modify.ui");
		curd_modifyuiText.setBounds(137, 225, 167, 25);

		final Label label_3_1_1_1_1_1 = new Label(container, SWT.NONE);
		label_3_1_1_1_1_1.setBounds(10, 265, 121, 17);
		label_3_1_1_1_1_1.setText("结果页面模版:");

		curd_resultuiText = new Text(container, SWT.BORDER);
		curd_resultuiText.setText("curd_result.ui");
		curd_resultuiText.setBounds(137, 262, 541, 25);

		final Label label_4 = new Label(container, SWT.NONE);
		label_4.setText("Controller模版");
		label_4.setBounds(327, 160, 91, 17);

		final Label label_4_1 = new Label(container, SWT.NONE);
		label_4_1.setBounds(327, 193, 91, 17);
		label_4_1.setText("Controller模版");

		curd_listctlText = new Text(container, SWT.BORDER);
		curd_listctlText.setText("curd_list.ctl");
		curd_listctlText.setBounds(424, 157, 254, 25);

		curd_insertctlText_1 = new Text(container, SWT.BORDER);
		curd_insertctlText_1.setText("curd_insert.ctl");
		curd_insertctlText_1.setBounds(424, 190, 254, 25);

		curd_modifyctlText = new Text(container, SWT.BORDER);
		curd_modifyctlText.setText("curd_modify.ctl");
		curd_modifyctlText.setBounds(424, 222, 254, 25);

		final Label label_4_1_1 = new Label(container, SWT.NONE);
		label_4_1_1.setBounds(327, 225, 91, 17);
		label_4_1_1.setText("Controller模版");

		final Label label_5 = new Label(container, SWT.NONE);
		label_5.setText("所选工程：");
		label_5.setBounds(454, 15, 64, 17);

		projects = ResourcesPlugin.getWorkspace().getRoot().getProjects(); 
		names = new ArrayList<String>();
		for(int i = 0;i<projects.length;i++){
			if(projects[i].isOpen()){
				names.add(projects[i].getName());
			}
		}
		String[] temps = new String[names.size()];
		combo_1 = new Combo(container, SWT.NONE);
		combo_1.setBounds(527, 7, 152, 25);
		combo_1.setItems(names.toArray(temps));
		combo_1.select(0);
	}

	private void initialize() {
	}
	@SuppressWarnings("unchecked")
	private void dataSourceChange(){
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		int index = combo.getSelectionIndex();
		if(index>-1){
			String comText = combo.getItem(index);
			info = (HashMap<String,String>)allDataSource.get(comText);
			final String owner = info.get("owner");
			InitConnectionThread initCon = new InitConnectionThread(this, comText, info,new ThreadTask(){
				@Override
				public void doTask() {
					List<Map<String,String>> tables = ConnFactory.queryList("select tab_name from dic_table where owner_USR='"+owner+"'", con);
					final String[] arr = new String[tables.size()];
					for(int i = 0;i<tables.size();i++){
						arr[i] = tables.get(i).get("tab_name");
					}
					if(au==null){
						Display.getDefault().syncExec(new Runnable() {
						    public void run() {
						    	System.out.println(arr.length);
								TextContentAdapter ad = new TextContentAdapter(){};
								au = new AutoCompleteField(text,ad , arr);
						    }
						});
					}else{
						Display.getDefault().syncExec(new Runnable() {
						    public void run() {
						    	au.setProposals(arr);
						    }
						});
					}			
				}
			});
			initCon.start();
		}
	}
}
