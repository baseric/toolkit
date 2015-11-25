package business.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TableViewer;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import ui.model.UIContentsModel;
import ui.util.UIGenerateCode;
import util.EditorUtil;
import util.PropertyReader;
import util.PublicTableSet;
import business.dialog.buttonAdapter.AddAdapter;
import business.dialog.buttonAdapter.DeleteAdapter;
import business.dialog.openResource.OpenUtil;
import business.model.action.StartModel;

public class StartDialog extends TitleAreaDialog {

	private Text inputJsp;
	private Table table;
	private Text text;
	private StartModel start = null;
	private TableViewer tableViewer_1 = null;
	private TableViewer tableViewer_2 = null;
	private TableViewer tableViewer_3 = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public StartDialog(Shell parentShell,StartModel start) {
		super(parentShell);
		this.start = start;
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

		final Label label = new Label(container, SWT.NONE);
		label.setText("显示名称：");
		label.setBounds(10, 21, 67, 17);

		text = new Text(container, SWT.BORDER);
		text.setBounds(83, 18, 185, 25);
		text.setText(start.getDisplay());
		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setBounds(10, 49, 737, 311);

		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("从参数取值");
		final Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		tableViewer_1 = initTab("parameter",composite,start.getParameter());

		final TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("从属性取值");
		final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite_1);
		tableViewer_2 = initTab("attribute",composite_1,start.getAttribute());
		
		
		final TabItem tabItem_2 = new TabItem(tabFolder, SWT.NONE);
		tabItem_2.setText("从session取值");
		final Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tabItem_2.setControl(composite_2);
		tableViewer_3 = initTab("session",composite_2,start.getSession());

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("数据输入页面：");
		label_1.setBounds(274, 21, 79, 17);

		inputJsp = new Text(container, SWT.BORDER);
		inputJsp.setBounds(359, 18, 332, 25);
		inputJsp.setText(start.jsp==null?"":start.jsp);
		final Button button = new Button(container, SWT.NONE);
		button.setBounds(697, 16, 50, 27);
		button.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				try {
					IProject project = start.getFile().getProject();
					String rootPath = project.getLocation().toFile().getAbsolutePath();
					IFile file = OpenUtil.openSearch(project, "", "ui");
					if(file!=null){
						File f = file.getLocation().toFile();
						String filePath = f.getAbsolutePath();
						inputJsp.setText(filePath.replace(rootPath, "").replace("\\", "/"));
						
						UIGenerateCode code = new UIGenerateCode();
						code.setProject(project);
						UIContentsModel contents = (UIContentsModel)code.readFile(file.getLocation().toFile());
						List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
						contents.getFormUIComponentID(list, contents);
						
						for(int i = 0;i<list.size();i++){
							HashMap<String,String> map = list.get(i);
							map.put("paramName", map.get("id"));
							map.put("paramName2", map.get("name"));
							map.put("paramType", "String");
							map.remove("id");
							map.remove("name");
							map.remove("type");
						}
						tableViewer_1.setInput(list);
					}
				 } catch (Exception e1) {
					e1.printStackTrace();
				}
		       
			}
		});
		button.setText(".....");
		setMessage("取值范围为request");
		//
		return area;
	}
	public TableViewer initTab(String type,Composite composite,List<HashMap<String,String>> data){
		Group group = new Group(composite, SWT.NONE);
		group.setText("输入参数");
		group.setBounds(0, 0, 720, 275);
		TableViewer tableViewer = new TableViewer(group, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		PublicTableSet.startTable(type,start,tableViewer);
		tableViewer.setInput(data);
		table.setBounds(10, 21, 632, 246);
		Button button = new Button(group, SWT.NONE);
		button.setData(tableViewer);
		HashMap<String,String> init = new HashMap<String,String>();
		init.put("paramName","name");
		init.put("paramType","String");
		button.addMouseListener(new AddAdapter(tableViewer, init));
		button.setText("新增");
		button.setBounds(647, 21, 62, 27);

		Button button_1 = new Button(group, SWT.NONE);
		button_1.setBounds(647, 57, 62, 27);
		button_1.setText("删除");
		button_1.setData(tableViewer);
		button_1.addMouseListener(new DeleteAdapter(tableViewer));
		Button button_2 = new Button(group, SWT.NONE);
		button_2.setBounds(647, 90, 62, 27);
		button_2.setText("上移");

		Button button_3 = new Button(group, SWT.NONE);
		button_3.setBounds(647, 125, 62, 27);
		button_3.setText("下移");
		return tableViewer;
	}
	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, 12, "刷新request参数",
				true);
		Button refresh = this.getButton(12);
		refresh.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				String input = inputJsp.getText();
				IProject project = start.getFile().getProject();
				PropertyReader reader = new PropertyReader();
				if(input!=null&&input.length()>0){
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
					String resourcePath = reader.getPropertyValue("resourcePath");
					IResource resource = root.findMember(new Path("/"+project.getName()+resourcePath+input));
					if(resource instanceof IFile){
						IFile file = (IFile)resource;
						UIGenerateCode code = new UIGenerateCode();
						code.setProject(project);
						UIContentsModel contents = (UIContentsModel)code.readFile(file.getLocation().toFile());
						List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
						contents.getFormUIComponentID(list, contents);
						
						for(int i = 0;i<list.size();i++){
							HashMap<String,String> map = list.get(i);
							map.put("paramName", map.get("id"));
							map.put("paramName2", map.get("name"));
							map.put("paramType", "String");
							map.remove("id");
							map.remove("name");
							map.remove("type");
						}
						tableViewer_1.setInput(list);
					}
				}
			}
		});
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	@Override
	protected Point getInitialSize() {
		return new Point(773, 525);
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
		List<HashMap<String,String>> parameter = PublicTableSet.getTableData(tableViewer_1);
		List<HashMap<String,String>> attr = PublicTableSet.getTableData(tableViewer_2);
		List<HashMap<String,String>> session =PublicTableSet.getTableData(tableViewer_3);
		start.setParameter(parameter);
		start.setAttribute(attr);
		start.setSession(session);
		start.jsp = inputJsp.getText();
		start.setDisplay(text.getText());
		EditorUtil.fireEditorDirty();
		super.okPressed();
	}
	
}
