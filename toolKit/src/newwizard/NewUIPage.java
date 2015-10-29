package newwizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import util.BaseInfoUtil;

public class NewUIPage extends WizardPage {
	private Combo combo;
	private Text containerText;
	private Canvas canvas = null;
	private Text fileText;
	private List<Map<String,String>> list = null;
	private ISelection selection;
	private Image image = null;
	private String type = "";
	private String rootPath = "";

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NewUIPage(ISelection selection,String type,String rootPath) {
		super("wizardPage");
		setTitle("创建页面配置文件");
		setDescription("");
		this.selection = selection;
		this.type = type;
		this.rootPath = rootPath;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		Label label = new Label(container, SWT.NULL);
		label.setBounds(5, 10, 75, 17);
		label.setText("&页面存放路径:");

		containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		containerText.setBounds(94, 7, 636, 23);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setBounds(736, 5, 64, 27);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setBounds(5, 44, 39, 17);
		label.setText("&文件名:");

		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		fileText.setBounds(94, 41, 196, 23);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		initialize();
		dialogChanged();
		setControl(container);

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setBounds(296, 44, 84, 17);
		label_1.setText("选择模版文件：");

		combo = new Combo(container, SWT.NONE);
		combo.setBounds(386, 41, 410, 25);
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				templateChanage();
			}
		});
		BaseInfoUtil util = new BaseInfoUtil();
		list = util.readXML("dev_template");
		List<String> uiTemp = new ArrayList<String>();
		uiTemp.add("空白页面");
		for(int i = 0;i<list.size();i++){
			if(type.equals(list.get(i).get("temp_type"))){
				uiTemp.add(list.get(i).get("temp_desc"));
			}
		}
		String[] options = new String[uiTemp.size()];
		uiTemp.toArray(options);
		combo.setItems(options);
		combo.select(0);

		canvas = new Canvas(container, SWT.NONE);
		canvas.setBounds(5, 70, 801, 406);
		canvas.addPaintListener(new PaintListener(){
		       public void paintControl(PaintEvent e){
		    	   	  if(image!=null){
		    	   		  final Rectangle bounds=image.getBounds();
			              int picwidth=bounds.width;        //图片宽
			              int picheight=bounds.height;                //图片高
			              int width = 793;
			              int height = 398;
			              if(picwidth<=width){
			            	  width = picwidth;
			            	  height = picheight;
			              }
			              e.gc.drawImage(image,0,0,picwidth,picheight,0,0, width,height);
		    	   	  }
		       }

		});
	}

	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				containerText.setText(container.getFullPath().toString());
			}
		}
		fileText.setText("new_file."+type);
	}
	private void templateChanage(){
		String temp_desc = combo.getText();
		for(int i = 0;i<list.size();i++){
			if(type.equals(list.get(i).get("temp_type"))&&temp_desc.equals(list.get(i).get("temp_desc"))){
				ImageData imageData = new ImageData(rootPath+"/template/images/"+list.get(i).get("temp_pic"));
				image = new Image(Display.getCurrent(), imageData); 
				canvas.redraw();
				break;

			}
		}
	}
	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		String fileName = getFileName();

		if (getContainerName().length() == 0) {
			updateStatus("文件存放路径必须存在!");
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("文件存放路径必须存在!");
			return;
		}
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
		if (fileName.length() == 0) {
			updateStatus("不是有效的文件名");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("不是有效的文件名");
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase(type) == false) {
				updateStatus("文件的扩展名必须是 \""+type+"\"");
				return;
			}
		}
		
		IContainer base = (IContainer) container;
		final IFile file = base.getFile(new Path(fileName));
		if(file.exists()){
			updateStatus("文件名已存在");
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getFileName() {
		return fileText.getText();
	}
	public String getTemplateFile(){
		String temp_desc = combo.getText();
		if(temp_desc.equals("空白页面")) return null;
		for(int i = 0;i<list.size();i++){
			if(type.equals(list.get(i).get("temp_type"))&&temp_desc.equals(list.get(i).get("temp_desc"))){
				return rootPath+"/template/"+list.get(i).get("temp_name");
			}
		}
		return null;
	}
}
