package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.TableItem;

import business.AbstractModel;
import business.dialog.tableEdit.ComboSelectionAdapter;
import business.dialog.tableEdit.MyComboBoxCellEditor;
import business.dialog.tableEdit.MyTextCellEditor;
import business.dialog.tableEdit.TextCellModifier;
import business.model.action.PackageVariableModel;
import business.model.logic.QueryModel;
import business.model.logic.SingleTabOptModel;

public class PublicTableSet {
	@SuppressWarnings("unchecked")
	public static List<HashMap<String,String>> getTableData(TableViewer tableViewer){
		TableItem[] items= tableViewer.getTable().getItems();
		List<HashMap<String,String>> parameter = new ArrayList<HashMap<String,String>>();
		for(int i = 0;i<items.length;i++){
			TableItem item = items[i];
			HashMap<String,String> row = (HashMap<String,String>)item.getData();
			parameter.add(row);
		}
		return parameter;
	}
	/**
	 * 设置方法弹出窗口中表格的列和编辑功能
	 * @param tableViewer
	 * @return
	 * 2014-12-31
	 * @tianming
	 */
	public static List<String[]> inOutParamSet(AbstractModel model ,String type ,TableViewer tableViewer,List<String> paramName){
		List<String[]> columns = new ArrayList<String[]>();
		String[] arr = new String[paramName.size()];
		try {
			paramName.toArray(arr);
			columns.add(new String[]{"305","参数类型","paramType"});
			columns.add(new String[]{"104","变量名","paramName"});
			columns.add(new String[]{"44","数组","isArray"});
			columns.add(new String[]{"190","描述","desc"});
			ViewerUtil.initTable(tableViewer, columns);
			TextCellModifier  modifier = new TextCellModifier(tableViewer,null);   
			CellEditor[] cellEditor = new CellEditor[4];      
			cellEditor[0] = null;
			cellEditor[1] = "in".equals(type)?new MyComboBoxCellEditor(tableViewer,arr,new ComboSelectionAdapter(),model):new TextCellEditor(tableViewer.getTable());
			cellEditor[2] = null;
			cellEditor[3] = new TextCellEditor(tableViewer.getTable());
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}
	
	public static List<String[]> variableTable(AbstractModel model ,TableViewer tableViewer){
		List<String[]> columns = new ArrayList<String[]>();
		try {
			columns.add(new String[]{"105","变量名","paramName"});
			columns.add(new String[]{"84","变量类型","paramType"});
			columns.add(new String[]{"64","数组","isArray"});
			columns.add(new String[]{"234","变量初始值","paramValue"});
			columns.add(new String[]{"160","描述","desc"});
			ViewerUtil.initTable(tableViewer, columns);
			TextCellModifier  modifier = new TextCellModifier(tableViewer,model);   
			CellEditor[] cellEditor = new CellEditor[5];      
			cellEditor[0] = new TextCellEditor(tableViewer.getTable());      
			cellEditor[1] = new MyComboBoxCellEditor(tableViewer,new String[]{"String","POJO","JEXL"},new ComboSelectionAdapter(),model);
			cellEditor[2] = new MyComboBoxCellEditor(tableViewer,new String[]{"Y","N"},new ComboSelectionAdapter(),model);
			cellEditor[3] = new MyTextCellEditor(model,tableViewer,"paramValue");
			cellEditor[4] = new TextCellEditor(tableViewer.getTable());
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}
	
	public static List<String[]> startTable(String type,AbstractModel model ,TableViewer tableViewer){
		List<String[]> columns = new ArrayList<String[]>();
		//PropertyReader reader = new PropertyReader("param");
		//String sessionParam = "";
		try {
			
			columns.add(new String[]{"230","变量名","paramName"});
			columns.add(new String[]{"230","参数名","paramName2"});
			columns.add(new String[]{"70","参数类型","paramType"});
			if("parameter".equals(type)){
				columns.add(new String[]{"70","是否解码","isDecode"});
			}
			 
			ViewerUtil.initTable(tableViewer, columns);
			 
			TextCellModifier  modifier = new TextCellModifier(tableViewer,model);   
			CellEditor[] cellEditor = new CellEditor["parameter".equals(type)?4:3];      
			cellEditor[0] = new TextCellEditor(tableViewer.getTable());      
			cellEditor[1] = "session".equals(type)?new MyComboBoxCellEditor(tableViewer,CacheInit.getTextArray("sessionParam"),new ComboSelectionAdapter(),model):new TextCellEditor(tableViewer.getTable());
			cellEditor[2] = new MyComboBoxCellEditor(tableViewer,new String[]{"String","int","float","POJO"},new ComboSelectionAdapter(),model);
			if("parameter".equals(type)){
				cellEditor[3] = new MyComboBoxCellEditor(tableViewer,new String[]{"Y","N"},new ComboSelectionAdapter(),model);
			}
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}
	public static List<String[]> logicStartTable(AbstractModel model ,TableViewer tableViewer){
		List<String[]> columns = new ArrayList<String[]>();
		try {
			columns.add(new String[]{"204","变量类型","paramType"});
			columns.add(new String[]{"125","变量名","paramName"});
			columns.add(new String[]{"135","描述","desc"});
			ViewerUtil.initTable(tableViewer, columns);
			 
			TextCellModifier  modifier = new TextCellModifier(tableViewer,model);   
			CellEditor[] cellEditor = new CellEditor[3];      
			cellEditor[0] = new MyComboBoxCellEditor(tableViewer,new String[]{"String","HashMap","POJO"},new ComboSelectionAdapter(),model) ;      
			cellEditor[1] = new TextCellEditor(tableViewer.getTable()) ;
			cellEditor[2] = new TextCellEditor(tableViewer.getTable()) ;
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}
 
	public static List<String[]> jspTable(AbstractModel model ,TableViewer tableViewer,List<String> paramNames){
		List<String[]> columns = new ArrayList<String[]>();
		String[] arr = new String[paramNames.size()];
		try {
			paramNames.toArray(arr);
			columns.add(new String[]{"105","变量名","paramName"});
			columns.add(new String[]{"155","变量类型","paramType"});
			columns.add(new String[]{"60","是否翻译","translation"});
			columns.add(new String[]{"184","表名","tableNames"});
			columns.add(new String[]{"73","翻译标志位","transFlag"});
			columns.add(new String[]{"130","描述","desc"});
			columns.add(new String[]{"1","描述","key"});
			ViewerUtil.initTable(tableViewer, columns);
			TextCellModifier  modifier = new TextCellModifier(tableViewer,model);   
			CellEditor[] cellEditor = new CellEditor[7];      
			cellEditor[0] = new MyComboBoxCellEditor(tableViewer,arr,new ComboSelectionAdapter(),model);
			cellEditor[1] = null;
			cellEditor[2] = new MyComboBoxCellEditor(tableViewer,new String[]{"Y","N"},new ComboSelectionAdapter(),model);
			cellEditor[3] = new TextCellEditor(tableViewer.getTable());
			cellEditor[4] = new MyComboBoxCellEditor(tableViewer,new String[]{"0","1"},new ComboSelectionAdapter(),model);
			cellEditor[5] = new TextCellEditor(tableViewer.getTable());
			cellEditor[6] = new TextCellEditor(tableViewer.getTable());
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}
	public static List<String[]> logicbackTable(AbstractModel model ,TableViewer tableViewer){
		List<String[]> columns = new ArrayList<String[]>();
		try {
			columns.add(new String[]{"234","变量类型","paramType"});
			columns.add(new String[]{"205","变量名","paramName"});
			columns.add(new String[]{"105","描述","desc"});
			columns.add(new String[]{"1","描述","key"});
			ViewerUtil.initTable(tableViewer, columns);
			TextCellModifier  modifier = new TextCellModifier(tableViewer,model);   
			CellEditor[] cellEditor = new CellEditor[3];      
			cellEditor[0] = null;      
			cellEditor[1] = new TextCellEditor(tableViewer.getTable()) ;
			cellEditor[2] = new TextCellEditor(tableViewer.getTable()) ;
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}
	public static List<String[]> logicinTable(AbstractModel model ,TableViewer tableViewer,List<String> paramName){
		List<String[]> columns = new ArrayList<String[]>();
		String[] arr = new String[paramName.size()];
		try {
			paramName.toArray(arr);
			columns.add(new String[]{"234","变量类型","paramType"});
			columns.add(new String[]{"205","变量名","paramName"});
			columns.add(new String[]{"105","描述","desc"});
			columns.add(new String[]{"1","描述","key"});
			ViewerUtil.initTable(tableViewer, columns);
			TextCellModifier  modifier = new TextCellModifier(tableViewer,model);   
			CellEditor[] cellEditor = new CellEditor[3];      
			cellEditor[0] = null;
			cellEditor[1] = new MyComboBoxCellEditor(tableViewer,arr,new ComboSelectionAdapter(),model);
			cellEditor[2] = new TextCellEditor(tableViewer.getTable());
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}
	/**
	 *  列表查询组件 - 动态查询参数
	 * @param model
	 * @param tableViewer
	 * @param paramNames
	 * @return
	 * 2015-4-16
	 * @tianming
	 */
	public static List<String[]> queryTable(AbstractModel model ,TableViewer tableViewer,List<String> paramNames){
		List<String[]> columns = new ArrayList<String[]>();
		String[] arr = new String[paramNames.size()];
		try {
			paramNames.toArray(arr);
			columns.add(new String[]{"105","连接符","link"});
			columns.add(new String[]{"155","列名","colName"});
			columns.add(new String[]{"80","比较符","comp"});
			columns.add(new String[]{"150","取值变量","paramName"});
			columns.add(new String[]{"150","变量类型","paramType"});
			ViewerUtil.initTable(tableViewer, columns);
			TextCellModifier  modifier = new TextCellModifier(tableViewer,model);   
			CellEditor[] cellEditor = new CellEditor[6];      
			cellEditor[0] = new MyComboBoxCellEditor(tableViewer,new String[]{"and","or"},new ComboSelectionAdapter(),model);
			cellEditor[1] = new TextCellEditor(tableViewer.getTable());
			cellEditor[2] = new MyComboBoxCellEditor(tableViewer,new String[]{"=",">","<","!=","like"},new ComboSelectionAdapter(),model);
			cellEditor[3] = new MyComboBoxCellEditor(tableViewer,arr,new ComboSelectionAdapter(),model);
			cellEditor[4] = null;
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}
	/**
	 *  列表查询组件 - 动态查询参数
	 * @param model
	 * @param tableViewer
	 * @param paramNames
	 * @return
	 * 2015-4-16
	 * @tianming
	 */
	public static List<String[]> validateTable(AbstractModel model ,TableViewer tableViewer,List<String> paramNames){
		List<String[]> columns = new ArrayList<String[]>();
		String[] arr = new String[paramNames.size()];
		String[] validateType = CacheInit.getTextArray("validateType");
		try {
			paramNames.toArray(arr);
			columns.add(new String[]{"150","取值变量","paramName"});
			columns.add(new String[]{"150","变量类型","validateType"});
			columns.add(new String[]{"150","变量类型","validateParam"});
			ViewerUtil.initTable(tableViewer, columns);
			TextCellModifier  modifier = new TextCellModifier(tableViewer,model);   
			CellEditor[] cellEditor = new CellEditor[3];      
			cellEditor[0] = new MyComboBoxCellEditor(tableViewer,arr,new ComboSelectionAdapter(),model);
			cellEditor[1] = new MyComboBoxCellEditor(tableViewer,validateType,new ComboSelectionAdapter(),model);;
			cellEditor[2] = new TextCellEditor(tableViewer.getTable());;
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}
	/**
	 * 列表查询组件 - 预编译参数
	 * @param model
	 * @param tableViewer
	 * @param paramNames
	 * @return
	 * 2015-4-16
	 * @tianming
	 */
	public static List<String[]> queryPrepareTable(QueryModel model ,TableViewer tableViewer,List<String> paramNames){
		List<String[]> columns = new ArrayList<String[]>();
		String[] arr = new String[paramNames.size()];
		try {
			paramNames.toArray(arr);
			columns.add(new String[]{"260","取值变量","paramName"});
			columns.add(new String[]{"230","变量类型","paramType"});
			ViewerUtil.initTable(tableViewer, columns);
			TextCellModifier  modifier = new TextCellModifier(tableViewer,model);   
			CellEditor[] cellEditor = new CellEditor[3];      
			cellEditor[0] = new MyComboBoxCellEditor(tableViewer,arr,new ComboSelectionAdapter(),model);
			cellEditor[1] = null;
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}

	/**
	 * 单表操作组件-绑定列
	 * @param model
	 * @param tableViewer
	 * @param paramNames
	 * @return
	 * 2015-4-16
	 * @tianming
	 */
	public static List<String[]> optTabPrepareTable(SingleTabOptModel model ,TableViewer tableViewer,List<String> paramNames){
		List<String[]> columns = new ArrayList<String[]>();
		String[] arr = new String[paramNames.size()];
		try {
			paramNames.toArray(arr);
			columns.add(new String[]{"440","字段名称","cname"});
			ViewerUtil.initTable(tableViewer, columns);
			TextCellModifier  modifier = new TextCellModifier(tableViewer,model);   
			CellEditor[] cellEditor = new CellEditor[1];      
			cellEditor[0] = new TextCellEditor(tableViewer.getTable());;
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}
	/**
	 * 单表操作组件-预编译参数
	 * @param model
	 * @param tableViewer
	 * @param paramNames
	 * @return
	 * 2015-4-16
	 * @tianming
	 */
	public static List<String[]> tabColumnPrepareTable(SingleTabOptModel model ,TableViewer tableViewer,List<String> paramNames){
		List<String[]> columns = new ArrayList<String[]>();
		String[] arr = new String[paramNames.size()];
		try {
			paramNames.toArray(arr);
			columns.add(new String[]{"260","取值变量","paramName"});
			columns.add(new String[]{"230","变量类型","paramType"});
			ViewerUtil.initTable(tableViewer, columns);
			TextCellModifier  modifier = new TextCellModifier(tableViewer,model);   
			CellEditor[] cellEditor = new CellEditor[3];      
			cellEditor[0] = new MyComboBoxCellEditor(tableViewer,arr,new ComboSelectionAdapter(),model);
			cellEditor[1] = null;
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}
	/**
	 * 单表操作组件-绑定列
	 * @param model
	 * @param tableViewer
	 * @param paramNames
	 * @return
	 * 2015-4-16
	 * @tianming
	 */
	public static List<String[]> packageVariable(PackageVariableModel model ,TableViewer tableViewer,List<String> paramNames){
		List<String[]> columns = new ArrayList<String[]>();
		String[] arr = new String[paramNames.size()];
		try {
			paramNames.toArray(arr);
			columns.add(new String[]{"300","字段名称","key"});
			columns.add(new String[]{"300","绑定变量","value"});
			ViewerUtil.initTable(tableViewer, columns);
			TextCellModifier  modifier = new TextCellModifier(tableViewer,model);   
			CellEditor[] cellEditor = new CellEditor[2];      
			cellEditor[0] = null;
			cellEditor[1] = new MyComboBoxCellEditor(tableViewer,arr,new ComboSelectionAdapter(),model);
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}
	
	public static List<String[]> crudListTable(TableViewer tableViewer){
		List<String[]> columns = new ArrayList<String[]>();
		try {
			columns.add(new String[]{"170","字段名","col_name"});
			columns.add(new String[]{"240","字段描述","col_dsc"});
			columns.add(new String[]{"60","是否显示","disp_yn"});
			columns.add(new String[]{"130","是否作为查询条件","query_yn"});
			ViewerUtil.initTable(tableViewer, columns);
			
			TextCellModifier  modifier = new TextCellModifier(tableViewer,null);   
			CellEditor[] cellEditor = new CellEditor[4];
			cellEditor[0] = null;
			cellEditor[1] = null;
			cellEditor[2] = new MyComboBoxCellEditor(tableViewer,new String[]{"是","否"},null,null);
			cellEditor[3] = new MyComboBoxCellEditor(tableViewer,new String[]{"是","否"},null,null);
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}
	public static List<String[]> crudModifyTable(TableViewer tableViewer){
		List<String[]> columns = new ArrayList<String[]>();
		try {
			columns.add(new String[]{"120","字段名","col_name"});
			columns.add(new String[]{"240","字段描述","col_dsc"});
			columns.add(new String[]{"80","是否表单项","is_element"});
			columns.add(new String[]{"60","是否更新","is_modify"});
			columns.add(new String[]{"130","是否更新依赖字段","is_key"});
			ViewerUtil.initTable(tableViewer, columns);
			
			TextCellModifier  modifier = new TextCellModifier(tableViewer,null);   
			CellEditor[] cellEditor = new CellEditor[5];
			cellEditor[0] = null;
			cellEditor[1] = null;
			cellEditor[2] = new MyComboBoxCellEditor(tableViewer,new String[]{"是","否"},null,null);
			cellEditor[3] = new MyComboBoxCellEditor(tableViewer,new String[]{"是","否"},null,null);
			cellEditor[4] = new MyComboBoxCellEditor(tableViewer,new String[]{"是","否"},null,null);
			tableViewer.setCellEditors(cellEditor);
			//设置修改器   
			tableViewer.setCellModifier(modifier);
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return columns;
	}
}
