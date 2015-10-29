package business.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
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
import util.PropertyReader;
import util.PublicTableSet;
import business.codesource.AnalysisJavaFile;
import business.model.action.MethodModel;

public class MethodDialog extends TitleAreaDialog {

	private Combo isSpringBean;
	private Combo methodName;
	private Table table_1;
	private Table table;
	private Text classPath;
	private Text text_5;
	private Text display;
	private MethodModel method = null;
	private TableViewer inViewer = null;
	private TableViewer outViewer = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public MethodDialog(Shell parentShell,MethodModel method) {
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
		display.setBounds(70, 22, 98, 25);
		

		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setBounds(0, 53, 758, 473);

		final TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("条件判断");

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite);

		final Group group = new Group(composite, SWT.NONE);
		group.setText("方法输入参数");
		group.setBounds(0, 10, 740, 329);

		inViewer = new TableViewer(group,SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION|SWT.BORDER);
		table = inViewer.getTable();
		PublicTableSet.inOutParamSet(method,"in",inViewer,paramName);
		table.setBounds(10, 52, 720, 267);
		

		final Label label_3 = new Label(group, SWT.NONE);
		label_3.setText("选择调用方法：");
		label_3.setBounds(10, 24, 82, 17);

		methodName = new Combo(group,SWT.NONE);
		methodName.setVisibleItemCount(20);
		methodName.setRedraw(true);
		methodName.computeSize(563,141);
		methodName.setSize(633,141);
		methodName.setBounds(98, 21, 632, 25);
		methodName.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent e) {
                int key = methodName.getSelectionIndex();
                methodChanage(key); 
            }
        });
		final Group group_2 = new Group(composite, SWT.NONE);
		group_2.setText("方法返回参数");
		group_2.setBounds(0, 345, 740, 100);

		outViewer = new TableViewer(group_2, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table_1 = outViewer.getTable();
		PublicTableSet.inOutParamSet(method,"out",outViewer,paramName);
		table_1.setBounds(10, 20, 720, 70);
		
		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("备注信息");

		final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_1);

		text_5 = new Text(composite_1, SWT.BORDER);
		text_5.setBounds(10, 10, 677, 288);

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("类名：");
		label_1.setBounds(174, 25, 36, 17);

		classPath = new Text(container, SWT.BORDER);
		classPath.setBounds(215, 22, 383, 25);
		//初始化数据
		if(method.getClassPath()!=null&&!"".equals(method.getClassPath())){
			classPath.setText(method.getClassName()+"--"+method.getClassPath());
		}
		classPath.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				try {
					IProject project = method.getFile().getProject();
					String projectPath = project.getLocation().toFile().getAbsolutePath();
					String info = classPath.getText();
					String[] temp = info.split("--");
					if(temp!=null&&temp.length==2){
						PropertyReader reader = new PropertyReader();
						String javaPath = reader.getPropertyValue("javaPath");
						File classFile = new File(projectPath+javaPath+temp[1].replace(".","\\")+".java");
						if(classFile.exists()){
							initComboDataByFile(classFile);
						}
					}
				 } catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		TextContentAdapter ad = new TextContentAdapter(){};
		PropertyReader reader = new PropertyReader();
		String javaPath = reader.getPropertyValue("javaPath");
		List<String> files = new ArrayList<String>();
		File file = new File(method.getFile().getProject().getLocation().toFile().getAbsolutePath()+javaPath+"financemanage");
		this.getFiles(file, files);
		String[] names = new String[files.size()];
		files.toArray(names);
		new AutoCompleteField(classPath,ad, names);
		
		display.setText(method.getDisplay());
		if(method.getClassPath()!=null&&method.getClassPath().length()>0){
			String filePaht = method.getFile().getProject().getLocation().toFile().getAbsolutePath()+javaPath+method.getClassPath().replace(".", "\\")+".java";
			initComboDataByFile(new File(filePaht));
			methodName.select(getComboIndxByItem(methodName,method.getComboMethodTxt()));
			inViewer.setInput(method.getIn());
			outViewer.setInput(method.getOut());
		}

		final Label label_4 = new Label(container, SWT.NONE);
		label_4.setText("Spring的Bean：");
		label_4.setBounds(604, 25, 98, 17);
		String[] arr = new String[]{"是","否"};
		isSpringBean = new Combo(container, SWT.NONE);
		isSpringBean.setBounds(708, 22, 50, 25);
		isSpringBean.setItems(arr);
		int indx = Integer.parseInt(method.getIsSpringBean());
		isSpringBean.select(indx);
		setMessage("设置方法属性");
		return area;
	}
	
	public void getFiles(File file,List<String> container){
		File[] files = file.listFiles();
		PropertyReader reader = new PropertyReader();
		String javaPath = reader.getPropertyValue("javaPath");
		for(int i = 0;i<files.length;i++){
			File f = files[i];
			if(f.isDirectory()){
				getFiles(f,container);
			}else{
				String path = f.getAbsolutePath();
				path = path.split(javaPath.replace("/","\\\\"))[1];
				String className = f.getName().replace(".java", "");
				container.add(className +"--" +path.replaceAll(".java", "").replaceAll("\\\\",".") );
			}
		}
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
			
			String[] methods = analy.getMethodListString(methodList);
			methodName.setItems(methods);
			methodName.select(0);
			methodChanage(0);
		}catch(Exception e){
			
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
		return new Point(784, 691);
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
			String[] temp = path.split("--");
			if(!"".equals(path)&&temp!=null&&temp.length!=2){
				this.setErrorMessage("类名不正确");
				return;
			}
			String nodeName = display.getText();
			method.setDisplay(nodeName);
			method.setClassPath(temp[1]);
			method.setClassName(temp[0]);
			if(methodName.getSelectionIndex()>-1)
				method.setComboMethodTxt(methodName.getItem(methodName.getSelectionIndex()));
			method.setIn(PublicTableSet.getTableData(inViewer));
			method.setOut(PublicTableSet.getTableData(outViewer));
			String methodNameStr = method.getComboMethodTxt().split("\\(")[0];
			method.setMethodName(methodNameStr);
			
			method.setIsSpringBean(isSpringBean.getSelectionIndex()+"");
			EditorUtil.fireEditorDirty();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.okPressed();
	}

}
