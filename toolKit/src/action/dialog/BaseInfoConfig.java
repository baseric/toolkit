package action.dialog;

import java.sql.Connection;
import java.util.HashMap;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import util.BaseInfoUtil;
import util.ConnFactory;
import util.EditorUtil;
import util.Log;
import util.PropertyReader;

public class BaseInfoConfig extends TitleAreaDialog {

	private Text text_2;
	private Text text_1;
	private Text text;
	private Text password;
	private Text username;
	private Text url;
	private Text driver;
	
	private Button button_1 = null;
	private Button button_2 = null;
	
	private Button button_3 = null;
	private Button button_4 = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public BaseInfoConfig(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		PropertyReader reader = new PropertyReader();
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		final GridData gd_container = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_container.widthHint = 412;
		container.setLayoutData(gd_container);

		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setBounds(0, 0, 508, 306);

		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("基础数据库配置");

		final TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("基础配置");

		final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite_1);
		String isCompOnSave = reader.getPropertyValue("isComplierOnSave");
		String isInsertDB = reader.getPropertyValue("isInsertDB");

		final Group group = new Group(composite_1, SWT.NONE);
		group.setText("保存时是否生成代码");
		group.setBounds(10, 60, 480, 50);

		button_1 = new Button(group, SWT.RADIO);
		button_1.setBounds(10, 25,33, 17);
		button_1.setText("是");
		
		button_2 = new Button(group, SWT.RADIO);
		button_2.setBounds(49, 25,33, 17);
		button_2.setText("否");
		if("N".equals(isCompOnSave)){
			button_2.setSelection(true);
			button_1.setSelection(false);
		}else{
			button_1.setSelection(true);
			button_2.setSelection(false);
		}
		final Group group_1 = new Group(composite_1, SWT.NONE);
		group_1.setText("同步页面是否存入数据库");
		group_1.setBounds(10, 10, 480, 44);

		button_3 = new Button(group_1, SWT.RADIO);
		button_3.setBounds(10, 20,33, 17);
		button_3.setText("是");

		button_4 = new Button(group_1, SWT.RADIO);
		button_4.setBounds(49, 20,33, 17);
		button_4.setText("否");
		
		if("N".equals(isInsertDB)){
			button_4.setSelection(true);
		}else{
			button_3.setSelection(true);
		}

		final Label label = new Label(composite_1, SWT.NONE);
		label.setText("jsp代码生成路径");
		label.setBounds(10, 135, 99, 17);

		text = new Text(composite_1, SWT.BORDER);
		text.setBounds(115, 132, 375, 25);
		text.setText(reader.getPropertyValue("jspPath"));
		
		final Label label_1 = new Label(composite_1, SWT.NONE);
		label_1.setText("java代码生成路径");
		label_1.setBounds(10, 176, 99, 17);

		text_1 = new Text(composite_1, SWT.BORDER);
		text_1.setBounds(116, 173, 374, 25);
		text_1.setText(reader.getPropertyValue("javaPath"));

		final Label label_2 = new Label(composite_1, SWT.NONE);
		label_2.setText("资源文件路径");
		label_2.setBounds(10, 213, 99, 17);

		text_2 = new Text(composite_1, SWT.BORDER);
		text_2.setBounds(115, 210, 375, 25);
		text_2.setText(reader.getPropertyValue("resourcePath"));
		final Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);

		final Label driverLabel = new Label(composite, SWT.NONE);
		driverLabel.setText("driver：");
		driverLabel.setBounds(10, 10, 50, 17);

		driver = new Text(composite, SWT.BORDER);
		driver.setBounds(81, 7, 303, 25);
		driver.setText(reader.getPropertyValue("driver"));
		final Label urlLabel = new Label(composite, SWT.NONE);
		urlLabel.setText("url:");
		urlLabel.setBounds(10, 48, 31, 17);

		url = new Text(composite, SWT.BORDER);
		url.setBounds(81, 45, 303, 25);
		url.setText(reader.getPropertyValue("url"));
		final Label usernameLabel = new Label(composite, SWT.NONE);
		usernameLabel.setText("username：");
		usernameLabel.setBounds(10, 83, 66, 17);

		username = new Text(composite, SWT.BORDER);
		username.setBounds(81, 80, 303, 25);
		username.setText(reader.getPropertyValue("username"));
		final Label passwordLabel = new Label(composite, SWT.NONE);
		passwordLabel.setText("password：");
		passwordLabel.setBounds(10, 123, 66, 17);

		password = new Text(composite, SWT.BORDER);
		password.setBounds(81, 120, 303, 25);
		password.setText(reader.getPropertyValue("password"));
		final Button button = new Button(composite, SWT.NONE);
		button.setText("测试连接");
		button.setBounds(81, 168, 66, 27);
		setTitle("基础信息配置");
		setMessage("刷新基础配置和保存表单到数据库会用到数据库配置");
		//
		return area;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		final TitleAreaDialog dialog = this;
		createButton(parent, 12, "刷新基础配置",
				false);
		this.getButton(12).addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				final String driverStr = driver.getText();
				final String urlStr = url.getText();
				final String usernameStr = username.getText();
				final String passwordStr = password.getText();
				final IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				new Thread(){
					@Override
					public void run() {
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								dialog.setErrorMessage(null);
								dialog.setMessage("获取数据库连接......");
							}
						}); 
						try {
							Connection con = ConnFactory.getConnection(driverStr,urlStr,usernameStr,passwordStr);
							BaseInfoUtil util = new BaseInfoUtil();
							Display.getDefault().syncExec(new Runnable() {
								public void run() {
									dialog.setErrorMessage(null);
									dialog.setMessage("加载基础配置信息......");
								}
							}); 
							util.uploadTableData(con);
							EditorUtil.refreshAllContents(workbenchWindow);
							Display.getDefault().syncExec(new Runnable() {
								public void run() {
									dialog.setErrorMessage(null);
									dialog.setMessage("加载基础配置信息完毕");
								}
							}); 
						} catch (Exception e) {
							final String message = e.getMessage();
							Log.write("", e);
							Display.getDefault().syncExec(new Runnable() {
							    public void run() {
							    	dialog.setErrorMessage("获取数据库连接失败，错误原因："+message+",请检查数据源配置");
							    }
							});
						}
					}
					
				}.start();
				
			}
		});
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(533, 481);
	}

	@Override
	protected void okPressed() {
		// TODO Auto-generated method stub
		PropertyReader reader = new PropertyReader();
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("driver", driver.getText());
		map.put("url", url.getText());
		map.put("username", username.getText());
		map.put("password", password.getText());
		map.put("isComplierOnSave", button_2.getSelection()?"N":"Y");
		map.put("isInsertDB",  button_4.getSelection()?"N":"Y");
		
		map.put("resourcePath", text_2.getText());
		map.put("javaPath", text_1.getText());
		map.put("jspPath", text.getText());
		reader.setPropertyValue(map);
		super.okPressed();
	}

}
