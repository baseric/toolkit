package ui.dialog.formdialog;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import ui.model.UIContentsModel;
import util.EditorUtil;
import business.dialog.openResource.OpenUtil;

public class ConfigContentsDialog extends TitleAreaDialog {
	private UIContentsModel model = null;
	private FormData fd_group = null;
	private Group group = null;
	private IFile file = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public ConfigContentsDialog(Shell parentShell,UIContentsModel model,String path,IFile file) {
		super(parentShell);
		this.model = model;
		//this.path = path;
		this.file = file;
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FillLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);

		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("页面引入js脚本");

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FormLayout());
		tabItem.setControl(composite);

		group = new Group(composite, SWT.NONE);
		group.setLayout(new RowLayout(SWT.VERTICAL));
		fd_group = new FormData();
		fd_group.bottom = new FormAttachment(100, -5);
		fd_group.top = new FormAttachment(0, 25);
		fd_group.right = new FormAttachment(0, 922);
		fd_group.left = new FormAttachment(0, 0);
		group.setLayoutData(fd_group);
		group.setText("js脚本文件");

		final Link eclipseorgLink_1 = new Link(composite, SWT.NONE);
		final FormData fd_eclipseorgLink_1 = new FormData();
		fd_eclipseorgLink_1.bottom = new FormAttachment(0, 22);
		fd_eclipseorgLink_1.top = new FormAttachment(0, 5);
		fd_eclipseorgLink_1.right = new FormAttachment(0, 157);
		fd_eclipseorgLink_1.left = new FormAttachment(0, 10);
		eclipseorgLink_1.setLayoutData(fd_eclipseorgLink_1);
		eclipseorgLink_1.setText("<a href=\"http://www.eclipse.org\">增加一行</a>");
		final ConfigContentsDialog dialogCurrent= this;
		eclipseorgLink_1.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				/*FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(),group,path);
				dialog.parent = dialogCurrent;
				dialog.open();*/
				
				IFile js = OpenUtil.openSearch(file.getProject(), "/WebRoot/", "js");
				dialogCurrent.addJS(js.getLocation().toFile().getAbsolutePath());
			}
		});
		setMessage("jquery文件已经默认加载，不需要重复引入");
		//
		
		
		List<String> js = model.getJs();
		for(int i = 0;i<js.size();i++){
			try {
				addJS(js.get(i));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return area;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(946, 593);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("页面配置");
	}
	@Override
	protected Point getInitialLocation(Point initialSize) {
		// TODO Auto-generated method stub
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - initialSize.x / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - initialSize.y / 2;
		return new Point(nLocationX,nLocationY);
	}
	
	/**
	 * 引入js增加一行
	 * @param js
	 * 2014-12-24
	 * @tianming
	 */
	public void addJS(String js){
		final Composite composite_3 = new Composite(group, SWT.NONE);
		composite_3.setLayout(new RowLayout());
		final RowData rd_composite_3 = new RowData();
		rd_composite_3.height = 25;
		rd_composite_3.width = 910;
		composite_3.setLayoutData(rd_composite_3);

		final Label label_1 = new Label(composite_3, SWT.NONE);
		label_1.setAlignment(SWT.CENTER);
		final RowData rd_label_1 = new RowData();
		rd_label_1.width = 91;
		label_1.setLayoutData(rd_label_1);
		label_1.setText("[删除]");
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				composite_3.dispose();
				group.pack();
			}
		});
		final Label label = new Label(composite_3, SWT.NONE);
		final RowData rd_label = new RowData();
		rd_label.width = 796;
		label.setLayoutData(rd_label);
		if(js.indexOf("WebRoot")>-1){
			label.setText(js.split("WebRoot")[1]);
		}else{
			label.setText(js);
		}
		group.pack();
		group.setLayoutData(fd_group);
	}

	@Override
	protected void okPressed() {
		// TODO Auto-generated method stub
		List<String> js = model.getJs();
		js.clear();
		Control[] children = group.getChildren();
		for(int i = 0;i<children.length;i++){
			Label label = (Label)((Composite)children[i]).getChildren()[1];
			js.add(label.getText());
		}
		EditorUtil.fireEditorDirty();
		super.okPressed();
	}
	
	
}
