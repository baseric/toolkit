package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConnFactory {
	public static Connection getConnection(){
		Connection _con = null;
		try {
			PropertyReader reader = new PropertyReader();
			Class.forName(reader.getPropertyValue("driver"));
			DriverManager.setLoginTimeout(1);
			_con = DriverManager.getConnection(reader.getPropertyValue("url"),reader.getPropertyValue("username"),reader.getPropertyValue("password"));
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return _con;
	}
	public static void closeConn(Connection con){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 短链接，窗口专用
	 * @param driver
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 * 2015-4-28
	 * @tianming
	 */
	public static Connection getConnection(String driver,String url,String username,String password) throws Exception{
		Class.forName(driver);
		Connection con = DriverManager.getConnection(url,username,password);
		con.setAutoCommit(false);
		return con;
	}
	/**
 	 * 描述：通用查询方法，将结果已List<Map<String,String>>形式返回
 	 * @param sql
 	 * @param con
 	 * @return
 	 * 作者：tianming
 	 * 日期：2014-3-13
 	 */
	public static List<Map<String,String>> queryList(String sql ,Connection con){
			PreparedStatement ps1 = null;
			ResultSet rs1 = null;
			Map<String,String> row = null;
			List<Map<String,String>> list = null;
			try{
				if(con==null) return null; 
				System.out.println(sql);
				list = new ArrayList<Map<String,String>>();
				ps1 = con.prepareStatement(sql);
				rs1 = ps1.executeQuery();
				ResultSetMetaData rsm = rs1.getMetaData();
				while(rs1.next()){
					row = new HashMap<String,String>();
					int columnCount = rsm.getColumnCount();
					for(int i = 1;i<=columnCount;i++){
						String val = rs1.getString(rsm.getColumnName(i));
						row.put(rsm.getColumnName(i).toLowerCase(),val==null?"":val);
					}
					list.add(row);
					row = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
			}
			return list;
	}
	/**
	 * 插入实体
	 * @param table
	 * @param data
	 * @param con
	 * @throws Exception
	 * 2015-6-26
	 * @tianming
	 */
	public static String insertMap(String table , Map<String,String> data,Connection con)throws Exception{
		PreparedStatement ps1 = null;
		Set<String> keys = data.keySet();
		String sql = "insert into "+table+" (";
		String vals = "";
		List<String> values = new ArrayList<String>();
		for(String key : keys){
			sql += key+",";
			vals +="'"+data.get(key)+"',";
			values.add(data.get(key));
		}
		sql = sql.substring(0,sql.length()-1);
		sql +=")";
		vals = vals.substring(0,vals.length()-1);
		
		ps1 = con.prepareStatement(sql+" values ("+vals+")");
		ps1.execute();
		ps1.close();
		return sql+" values ("+vals+")";
	}
	/**
	 * 插入实体
	 * @param table
	 * @param data
	 * @param con
	 * @throws Exception
	 * 2015-6-26
	 * @tianming
	 */
	public static String getInserSql(String table , Map<String,String> data)throws Exception{
		Set<String> keys = data.keySet();
		String sql = "insert into "+table+" (";
		String vals = "";
		for(String key : keys){
			sql += key+",";
			vals +="'"+data.get(key)+"',";
		 
		}
		sql = sql.substring(0,sql.length()-1);
		sql +=")";
		vals = vals.substring(0,vals.length()-1);
		return sql+" values ("+vals+")";
	}
	/**
	 * 插入实体
	 * @param table
	 * @param data
	 * @param con
	 * @throws Exception
	 * 2015-6-26
	 * @tianming
	 */
	public static void insertList(String table , List<Map<String,String>> data,Connection con)throws Exception{
		if(data==null||data.size()==0) return ;
		PreparedStatement ps1 = null;
		Set<String> keys = data.get(0).keySet();
		String sql = "insert into "+table+" (";
		String vals = "";
		List<String> attrname = new ArrayList<String>();
		for(String key : keys){
			sql += key+",";
			vals +="?,";
			attrname.add(key);
		}
		sql = sql.substring(0,sql.length()-1);
		sql +=")";
		vals = vals.substring(0,vals.length()-1);
		
		ps1 = con.prepareStatement(sql+" values ("+vals+")");
		
		for(int i = 0;i<data.size();i++){
			Map<String,String> row = data.get(i);
			for(int j = 0;j<attrname.size();j++){
				ps1.setString(j+1, row.get(attrname.get(j)));
			}
			ps1.addBatch();
		}
		ps1.executeBatch();
		ps1.close();
	}
	/**
	 * 插入实体
	 * @param table
	 * @param data
	 * @param con
	 * @throws Exception
	 * 2015-6-26
	 * @tianming
	 */
	public static String getInsertListSql(String table , List<Map<String,String>> data)throws Exception{
		if(data==null||data.size()==0) return "";
		Set<String> keys = data.get(0).keySet();
		String sql = "insert into "+table+" (";
		String vals = "";
		List<String> attrname = new ArrayList<String>();
		for(String key : keys){
			sql += key+",";
			vals +="{"+attrname.size()+"},";
			attrname.add(key);
		}
		sql = sql.substring(0,sql.length()-1);
		sql +=")";
		vals = vals.substring(0,vals.length()-1);
		String result = "";
		for(int i = 0;i<data.size();i++){
			Map<String,String> row = data.get(i);
			Object[] args= new String[attrname.size()];
			for(int j = 0;j<attrname.size();j++){
				args[j] = "'"+row.get(attrname.get(j))+"'";
			}
			result += MessageFormat.format(sql+" values ("+vals+");\n", args);
		}
		return result;
	}
	/**
	 * 删除记录
	 * @param table
	 * @param data
	 * @param con
	 * @throws Exception
	 * 2015-6-26
	 * @tianming
	 */
	public static void deleteRow(String sql,Connection con)throws Exception{
		PreparedStatement ps1 = null;
		ps1 = con.prepareStatement(sql);
		ps1.execute();
		ps1.close();
	}
}
