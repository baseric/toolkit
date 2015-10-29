package business.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
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

import util.EditorUtil;
import util.GenerateCode;
import util.PublicTableSet;
import business.AbstractModel;
import business.dialog.openResource.OpenUtil;
import business.model.ContentsModel;
import business.model.action.LogicModel;
import business.model.logic.EndModel;
import business.model.logic.LogicStartModel;

public class LogicDialog extends TitleAreaDialog {

	private Table table_1;
	private Table table;
	private Text logicPath;
	private Text text_5;
	private Text display;
	private LogicModel logic = null;
	private TableViewer in ;
	private TableViewer out ;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public LogicDialog(Shell parentShell,LogicModel logic) {
		super(parentShell);
		this.logic = logic;
	}

	@Override
	protected void okPressed() {
		logic.setDisplay(display.getText());
		logic.setLogicPath(logicPath.getText());
		logic.setInList(PublicTableSet.getTableData(in));
		logic.setOutList(PublicTableSet.getTableData(out));
		EditorUtil.fireEditorDirty();
		super.okPressed();
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
		label.setBounds(5, 25, 59, 17);

		display = new Text(container, SWT.BORDER);
		display.setBounds(70, 22, 165, 25);

		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setBounds(0, 53, 677, 345);

		final TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("条件判断");

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite);

		final Group group = new Group(composite, SWT.NONE);
		group.setText("输入参数");
		group.setBounds(0, 10, 671, 192);

		in = new TableViewer(group, SWT.BORDER|SWT.FULL_SELECTION);
		table = in.getTable();
		List<String> paramName = new ArrayList<String>();
		logic.getAllParamName(paramName);
		PublicTableSet.logicinTable(logic,in,paramName);
		table.setBounds(10, 25, 651, 157);

		final Group group_2 = new Group(composite, SWT.NONE);
		group_2.setText("返回参数");
		group_2.setBounds(0, 210, 671, 100);

		out = new TableViewer(group_2, SWT.BORDER|SWT.FULL_SELECTION);
		table_1 = out.getTable();
		PublicTableSet.logicbackTable(logic,out);
		table_1.setBounds(10, 20, 651, 70);

		
		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("备注信息");

		final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_1);

		text_5 = new Text(composite_1, SWT.BORDER);
		text_5.setBounds(10, 10, 677, 288);

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("文件路径：");
		label_1.setBounds(241, 25, 56, 17);

		logicPath = new Text(container, SWT.BORDER);
		logicPath.setEditable(false);
		logicPath.setBounds(303, 22, 308, 25);

		final Button button = new Button(container, SWT.CENTER);
		button.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				IFile file = OpenUtil.openSearch(logic.getFile().getProject(), "", "logic");
				if(file!=null){
					String path = file.getLocation().toFile().getAbsolutePath();
					String projectPath = logic.getFile().getProject().getLocation().toFile().getAbsolutePath();
					logicPath.setText(path.replace(projectPath+"\\", ""));
					parseLogicFile(file.getLocation().toFile());
				}
			}
		});
		button.setText(".....");
		button.setBounds(620, 20, 50, 27);
 
		setMessage("设置业务逻辑属性");
		
		logicPath.setText(logic.getLogicPath());
		display.setText(logic.getDisplay());
		in.setInput(logic.getInList());
		out.setInput(logic.getOutList());
		//
		return area;
	}
	public void parseLogicFile(File file){
		//解析该业务逻辑类的入口参数和返回参数类型
		try {
			GenerateCode code = new GenerateCode();
			ContentsModel contents = (ContentsModel)code.readFile(file);
			List<AbstractModel> children = contents.getChildren();
			for(int i = 0;i<children.size();i++){
				AbstractModel model = children.get(i);
				if(model instanceof LogicStartModel){
					LogicStartModel start = (LogicStartModel)model;
					in.setInput(start.getParameter());
				}else if(model instanceof EndModel){
					EndModel end = (EndModel)model;
					out.setInput(end.getWriteParam());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, 110, "刷新参数列表",
				false);
		Button button = getButton(110);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String projectPath = logic.getFile().getProject().getLocation().toFile().getAbsolutePath();
				String file = logicPath.getText();
				parseLogicFile(new File(projectPath+"/"+file));
			}
			
		});
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	@Override
	protected Point getInitialLocation(Point initialSize) {
		// TODO Auto-generated method stub
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - initialSize.x / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - initialSize.y / 2;
		return new Point(nLocationX,nLocationY);
	}
	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(693, 563);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("信息");
	}

}
