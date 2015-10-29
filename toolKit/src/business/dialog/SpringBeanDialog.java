package business.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
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
import org.eclipse.swt.widgets.Text;

import util.EditorUtil;
import util.PublicTableSet;
import business.codesource.AnalysisJavaFile;
import business.dialog.openResource.OpenUtil;
import business.model.action.MethodModel;

public class SpringBeanDialog extends TitleAreaDialog {

	private Combo methodName;
	private Table table_2;
	private Combo constructName;
	private Table table_1;
	private Table table;
	private Text classPath;
	private Text text_5;
	private Text display;
	private MethodModel method = null;
	private TableViewer inViewer = null;
	private TableViewer outViewer = null;
	private TableViewer constructionViewer = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public SpringBeanDialog(Shell parentShell,MethodModel method) {
		super(parentShell);
		this.method = method;
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		List<String> paramName = new ArrayList<String>();
		method.getAllParamName(paramName);
		
		final Label label = new Label(container, SWT.NONE);
		label.setText("显示名称：");
		label.setBounds(5, 25, 59, 17);

		display = new Text(container, SWT.BORDER);
		display.setBounds(70, 22, 165, 25);
		

		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setBounds(0, 53, 677, 473);

		final TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("条件判断");

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite);

		final Group group = new Group(composite, SWT.NONE);
		group.setText("方法输入参数");
		group.setBounds(0, 167, 671, 172);

		inViewer = new TableViewer(group,SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION|SWT.BORDER);
		table = inViewer.getTable();
		PublicTableSet.inOutParamSet(method,"in",inViewer,paramName);
		table.setBounds(10, 52, 651, 110);
		

		final Label label_3 = new Label(group, SWT.NONE);
		label_3.setText("选择调用方法：");
		label_3.setBounds(10, 24, 82, 17);

		methodName = new Combo(group, SWT.READ_ONLY);
		methodName.setRedraw(true);
		methodName.computeSize(563,141);
		methodName.setSize(563,141);
		methodName.setBounds(98, 21, 563, 141);
		methodName.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent e) {
                int key = methodName.getSelectionIndex();
                methodChanage(key); 
            }
        });
		final Group group_2 = new Group(composite, SWT.NONE);
		group_2.setText("方法返回参数");
		group_2.setBounds(0, 345, 671, 100);

		outViewer = new TableViewer(group_2, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table_1 = outViewer.getTable();
		PublicTableSet.inOutParamSet(method,"out",outViewer,paramName);
		table_1.setBounds(10, 20, 651, 70);
		

		final Group group_1 = new Group(composite, SWT.NONE);
		group_1.setText("构造函数参数");
		group_1.setBounds(0, 0, 669, 161);

		final Label label_2 = new Label(group_1, SWT.NONE);
		label_2.setText("构造方法：");
		label_2.setBounds(10, 26, 70, 17);

		constructName = new Combo(group_1, SWT.READ_ONLY);
		constructName.setBounds(98, 23, 561, 25);
		
		constructName.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent e) {
                int key = constructName.getSelectionIndex();
                constructChanage(key); 
            }
        });
		constructionViewer = new TableViewer(group_1, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table_2 = constructionViewer.getTable();

		PublicTableSet.inOutParamSet(method,"in",constructionViewer,paramName);
		table_2.setBounds(10, 51, 649, 100);
		
		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("备注信息");

		final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_1);

		text_5 = new Text(composite_1, SWT.BORDER);
		text_5.setBounds(10, 10, 677, 288);

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("方法路径：");
		label_1.setBounds(241, 25, 59, 17);

		classPath = new Text(container, SWT.BORDER);
		classPath.setEditable(false);
		classPath.setBounds(304, 22, 306, 25);
		
		final Button button = new Button(container, SWT.CENTER);
		button.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				try {
					IProject project = method.getFile().getProject();
					IFile file = OpenUtil.openSearch(project, "src/", "java");
					if(file!=null){
						String path = file.getLocation().toFile().getAbsolutePath();
						String projectPath = project.getLocation().toFile().getAbsolutePath();
						path = path.replace(projectPath+"\\src\\", "").replace(".java", "");
						String classStr =path .replace("\\",".");
						classPath.setText(classStr);
						//initComboDataByClass(classStr);
						initComboDataByFile(file.getLocation().toFile());
					}
				 } catch (Exception e1) {
					e1.printStackTrace();
				}
		       
			}
		});
		button.setText(".....");
		button.setBounds(616, 20, 50, 27);
		
		//初始化数据
		classPath.setText(method.getClassPath());
		display.setText(method.getDisplay());
		if(method.getClassPath()!=null&&method.getClassPath().length()>0){
			String filePaht = method.getFile().getProject().getLocation().toFile().getAbsolutePath()+"\\src\\"+method.getClassPath().replace(".", "\\")+".java";
			initComboDataByFile(new File(filePaht));
			int conIndex = getComboIndxByItem(constructName,method.getComboConTxt());
			constructName.select(conIndex);
			if(method.getConParam()!=null){
				constructionViewer.setInput(method.getConParam());
			}else{
				constructName.select(0);
				constructChanage(0);
			}
			methodName.select(getComboIndxByItem(methodName,method.getComboMethodTxt()));
			inViewer.setInput(method.getIn());
			outViewer.setInput(method.getOut());
		}
		setMessage("设置方法属性");
		return area;
	}
	/**
	 * 根据显示值获取下拉选项的索引
	 * @param combo
	 * @param name
	 * @return
	 * 2015-1-15
	 * @tianming
	 */
	public int getComboIndxByItem(Combo combo,String name){
		String[] items = combo.getItems();
		for(int i = 0;i<items.length;i++){
			if(name.equals(items[i])){
				return i;
			}
		}
		return 0;
	}
	private HashMap<String,HashMap<String,Object>> methodMap = null;
	/**
	 * 初始化下拉列表的数据
	 * @param classStr
	 * 2015-1-13
	 * @tianming
	 */
	@SuppressWarnings("unchecked")
	public void initComboDataByFile(File file){
		methodMap = new HashMap<String,HashMap<String,Object>>();
		try{
			AnalysisJavaFile analy = new AnalysisJavaFile();
			List<HashMap> methodList = analy.getJavaInfo(file);
			//初始化构造
			for(int i = 0;i<methodList.size();i++){
				HashMap<String,Object> method = methodList.get(i);
				String temp = String.valueOf(method.get("methodName"));
				methodMap.put(temp, method);
			}		
			
			String[] cons = analy.getConListString(methodList);
			constructName.setItems(cons);
			constructName.select(0);
			constructChanage(0);
			
			String[] methods = analy.getMethodListString(methodList);
			methodName.setItems(methods);
			methodName.select(0);
			methodChanage(0);
		}catch(Exception e){
			
		}
	}

	@SuppressWarnings("unchecked")
	public void constructChanage(int index){
		try {
			if(constructName.getItems().length>0){
				String constr =  constructName.getItem(index);
				HashMap<String,Object> method = methodMap.get(constr);
				if(method!=null){
					List<String> parameters = (List)method.get("parameters");
					List<HashMap<String,String>> temp = new ArrayList<HashMap<String,String>>();
					for(int i = 0;i<parameters.size();i++){
						String cls = parameters.get(i);
						HashMap<String,String>  param = new HashMap<String,String>();
						param.put("paramType", cls.split("@")[0]);
						param.put("paramName", cls.split("@")[1]);
						param.put("isArray","false");
						param.put("desc", "");
						temp.add(param);
					}
					constructionViewer.setInput(temp);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 方法名发生变化时
	 * @param index
	 * 2015-1-14
	 * @tianming
	 */
	@SuppressWarnings("unchecked")
	public void methodChanage(int index){
		try {
			String constr =  methodName.getItem(index);
			HashMap<String,Object> method = methodMap.get(constr);
			List<String> parameters = (List)method.get("parameters");
			List<HashMap<String,String>> temp = new ArrayList<HashMap<String,String>>();
			for(int i = 0;i<parameters.size();i++){
				String cls = parameters.get(i);
				HashMap<String,String>  param = new HashMap<String,String>();
				param.put("paramType", cls.split("@")[0]);
				param.put("paramName", cls.split("@")[1]);
				param.put("isArray","false");
				param.put("desc", "");
				temp.add(param);
			}
			inViewer.setInput(temp);
			String type = String.valueOf(method.get("returnType"));
			if(!"void".equals(type)){
				List<HashMap<String,String>> returnBack = new ArrayList<HashMap<String,String>>();
				HashMap<String,String>  param = new HashMap<String,String>();
				param.put("paramType", type);
				param.put("paramName", "name");
				param.put("isArray","false");
				param.put("desc", "");
				returnBack.add(param);
				outViewer.setInput(returnBack);
			}else{
				outViewer.setInput(null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		return new Point(692, 691);
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
		try {
			String path = classPath.getText();
			String nodeName = display.getText();
			method.setDisplay(nodeName);
			method.setClassPath(path);
			int index = constructName.getSelectionIndex();
			if(index!=-1){
				method.setComboConTxt(constructName.getItem(index));
			}else{
				method.setComboConTxt("");
			}
			if(methodName.getSelectionIndex()>-1)
				method.setComboMethodTxt(methodName.getItem(methodName.getSelectionIndex()));
			method.setConParam(PublicTableSet.getTableData(constructionViewer));
			method.setIn(PublicTableSet.getTableData(inViewer));
			method.setOut(PublicTableSet.getTableData(outViewer));
			String methodNameStr = method.getComboMethodTxt().split("\\(")[0];
			method.setMethodName(methodNameStr);
			EditorUtil.fireEditorDirty();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.okPressed();
	}

}
