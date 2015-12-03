package business.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import util.EditorUtil;
import util.PublicTableSet;
import business.dialog.openResource.OpenUtil;
import business.model.action.JspModel;

public class JspDialog extends TitleAreaDialog {

	private Text text;
	private Combo returnType;
	private Table table;
	private Text path;
	private Text desc;
	private Text display;
	private TableViewer writeParam = null;
	private TableViewer  tableViewer_1 = null;
	private JspModel jsp = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public JspDialog(Shell parentShell,JspModel jsp) {
		super(parentShell);
		this.jsp = jsp;
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		try{
			Composite container = new Composite(area, SWT.NONE);
			container.setLayoutData(new GridData(GridData.FILL_BOTH));
	
			final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
			tabFolder.setBounds(0, 10, 792, 428);
	
			final TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
			tabItem_1.setText("参数设置");
	
			final Composite composite = new Composite(tabFolder, SWT.NONE);
			tabItem_1.setControl(composite);
	
			final Group group = new Group(composite, SWT.NONE);
			group.setText("输出到页面的变量");
			group.setBounds(0, 95, 784, 141);
	
			writeParam = new TableViewer(group, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
			table = writeParam.getTable();
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			table.setBounds(10, 20, 714, 109);
			List<String> params = new ArrayList<String>();
			jsp.getAllParamName(params);
			PublicTableSet.jspTable(jsp,writeParam,params);
			writeParam.setInput(jsp.getWriteParam());
			final Button button_1 = new Button(group, SWT.NONE);
			button_1.setText("新增");
			button_1.setBounds(730, 20, 50, 27);
	
			final Button button_2 = new Button(group, SWT.NONE);
			button_2.setText("删除");
			button_2.setBounds(730, 56, 50, 27);
			button_1.addMouseListener(new MouseAdapter() {
				int count = 0;
				public void mouseDown(MouseEvent e) {
					try {
						HashMap<String,String> row = new HashMap<String,String>();
						row.put("paramName", "name"+count++);
						row.put("translation", "N");
						row.put("desc", "");
						row.put("tableNames", "");
						row.put("transFlag", "0");
						row.put("key", String.valueOf(System.currentTimeMillis()));
						writeParam.add(row);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			button_2.addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					try {
						Table tab = writeParam.getTable();
						TableItem[] item = tab.getSelection();
						for(int i = 0;i<item.length;i++){
							TableItem[] all = tab.getItems();
							for(int j = 0;j<all.length;j++){
								if(all[j]==item[i]){
									tab.remove(j);
								}
							}
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			final Label label = new Label(composite, SWT.NONE);
			label.setBounds(10, 24,59, 17);
			label.setText("显示名称：");
	
			display = new Text(composite, SWT.BORDER);
			display.setBounds(119, 21,656, 25);
			display.setText(jsp.getDisplay());
			final Label label_1 = new Label(composite, SWT.NONE);
			label_1.setBounds(203, 55,50, 17);
			label_1.setText("jsp路径：");
	
			int returnTypeIndx = Integer.parseInt(jsp.getReturnType());
			path = new Text(composite, SWT.BORDER);
			path.setBounds(259, 52,460, 25);
			path.setText(jsp.getPath());
			if(returnTypeIndx!=0){
				path.setEnabled(false);
			}
			final Button button = new Button(composite, SWT.CENTER);
			button.setBounds(725, 50,50, 27);
			button.setText(".....");
			button.addMouseListener(new MouseAdapter() {
				public void mouseDown(final MouseEvent e) {
					try {
						String rootPath = jsp.getFile().getProject().getLocation().toFile().getAbsolutePath();
						IFile file = OpenUtil.openSearch(jsp.getFile().getProject(), "WebRoot", "jsp");
						if(file!=null){
							File f = file.getLocation().toFile();
							String filePath = f.getAbsolutePath();
							path.setText("/"+filePath.replace(rootPath+"\\WebRoot\\", "").replace("\\", "/"));
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			       
				}
			});
			returnType = new Combo(composite, SWT.NONE);
			returnType.add("同步");
			returnType.add("异步");
			returnType.select(returnTypeIndx);
			returnType.setBounds(119, 52, 78, 25);
			returnType.addSelectionListener(new SelectionAdapter(){
	            @Override
	            public void widgetSelected(SelectionEvent e) {
	                int key = returnType.getSelectionIndex();
	               	if(key==0){
	               		path.setEnabled(true);
	               		button.setEnabled(true);
	               	}else{
	               		button.setEnabled(false);
	               		path.setEnabled(false);
	               	}
	            }
	        });
			final Label label_2 = new Label(composite, SWT.NONE);
			label_2.setText("返回jsp数据类型：");
			label_2.setBounds(10, 61, 107, 17);
	
			
			final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
			tabItem.setText("备注信息");
	
			final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
			tabItem.setControl(composite_1);
	
			desc = new Text(composite_1, SWT.BORDER);
			desc.setBounds(10, 10, 565, 288);
	 
			setMessage("设置方法属性");
			
			
			
			final Group group2 = new Group(composite, SWT.NONE);
			group2.setText("输入到页面的常量");
			group2.setBounds(0, 242, 784, 156);
			
			

			tableViewer_1 = new TableViewer(group2, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
			table = tableViewer_1.getTable();
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			final List<String[]> columns = PublicTableSet.variableTable(jsp,tableViewer_1);
			table.setBounds(10, 20, 719, 126);
			tableViewer_1.setInput(jsp.getVarList());

			final Button button_4 = new Button(group2, SWT.NONE);
			button_4.setBounds(735, 55,45, 27);
			button_4.setText("删除");
			button_4.addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					try {
						Table tab = tableViewer_1.getTable();
						TableItem[] item = tab.getSelection();
						for(int i = 0;i<item.length;i++){
							TableItem[] all = tab.getItems();
							for(int j = 0;j<all.length;j++){
								if(all[j]==item[i]){
									tab.remove(j);
								}
							}
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			final Button button3 = new Button(group2, SWT.NONE);
			button3.setText("新增");
			button3.setBounds(735, 20, 45, 27);
			button3.addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					try {
						HashMap<String,String> row = new HashMap<String,String>();
						for(int i = 0;i<columns.size();i++){
							row.put(columns.get(i)[2], "");
						}
						row.put("paramName","name");
						row.put("paramType","String");
						row.put("isArray", "N");
						tableViewer_1.add(row);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});

			text = new Text(container, SWT.BORDER);
			text.setBounds(156, 450, 578, 23);
			text.setText(jsp.getDefClassPath());
			final Label label_3 = new Label(container, SWT.NONE);
			label_3.setText("自定义返回结果实现类：");
			label_3.setBounds(10, 453, 140, 17);

			final Label label_4 = new Label(container, SWT.NONE);
			label_4.setText("注：自定义返回结果实现类需要实现IReturnModel接口");
			label_4.setBounds(10, 485, 724, 17);
		}catch(Exception e){
			e.printStackTrace();
		}
		//
		return area;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
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
		return new Point(808, 671);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("信息");
	}
	@Override
	protected Point getInitialLocation(Point initialSize) {
		// TODO Auto-generated method stub
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - initialSize.x / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - initialSize.y / 2;
		return new Point(nLocationX,nLocationY);
	}
	@Override
	protected void okPressed() {
		jsp.setDisplay(display.getText());
		jsp.setPath(path.getText());
		jsp.setDefClassPath(text.getText());
		jsp.setReturnType(String.valueOf(returnType.getSelectionIndex()));
		jsp.setWriteParam(PublicTableSet.getTableData(writeParam));
		jsp.setVarList(PublicTableSet.getTableData(tableViewer_1));
		EditorUtil.fireEditorDirty();
		super.okPressed();
	}

}
