package action.wizard;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.eclipse.swt.widgets.Display;

import util.ConnFactory;
import util.Log;
import util.thread.ThreadTask;

public class InitConnectionThread extends Thread {
	public HashMap<String,String> info = null;
	public ConnAbstractWizardPage dialog = null;
	public String datasource = "";
	public ThreadTask task = null;
	public InitConnectionThread(ConnAbstractWizardPage dialog,String datasource,HashMap<String,String> info,ThreadTask task){
		this.info = info;
		this.dialog = dialog;
		this.datasource = datasource;
		this.task = task;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String driver = info.get("driver");
		String url = info.get("url");
		String username = info.get("username");
		String password = info.get("password");
		Connection con = null;
		try {
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					dialog.setErrorMessage(null);
					dialog.setMessage("正在获取数据源【"+datasource+"】连接，请稍等......");
				}
			}); 
			con = ConnFactory.getConnection(driver,url,username,password);
			dialog.con = con;
			task.doTask();
			try {
				Display.getDefault().syncExec(new Runnable() {
				    public void run() {
				    	dialog.setMessage("");
				    }
				});
			} catch (Exception e) {
			}
		} catch (Exception e) {
			//e.printStackTrace();
			final String message = e.getMessage();
			final Exception e2 = e;
			try {
				Display.getDefault().syncExec(new Runnable() {
				    public void run() {
				    	Log.write("", e2);
				    	dialog.setErrorMessage("获取数据库连接失败，错误原因："+message+",请检查数据源配置");
				    }
				});
			} catch (Exception e1) {
			} 
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e1) {
				}
			}
		}

	}
	
}
