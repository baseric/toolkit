package action.dialog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

public class NewComponentDialog extends TitleAreaDialog {

	
	private Text newComp;
	private Text comp_desc;
	private Text fileName;
	private Text path;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public NewComponentDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		final GridData gd_container = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_container.widthHint = 412;
		container.setLayoutData(gd_container);

		final Label label = new Label(container, SWT.NONE);
		label.setText("文件路径：");
		label.setBounds(10, 10, 86, 17);

		path = new Text(container, SWT.BORDER);
		path.setEditable(false);
		path.setText("/plugin_resource/def_component/");
		path.setBounds(102, 7, 301, 25);

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("文件名称：");
		label_1.setBounds(10, 43, 85, 17);

		fileName = new Text(container, SWT.BORDER);
		fileName.setText("new_cmp.cmp");
		fileName.setBounds(102, 40, 395, 25);

		final Label label_2 = new Label(container, SWT.NONE);
		label_2.setText("组件中文描述：");
		label_2.setBounds(10, 109, 86, 17);

		comp_desc = new Text(container, SWT.BORDER);
		comp_desc.setText("业务组件");
		comp_desc.setBounds(102, 106, 395, 25);

		final Button button = new Button(container, SWT.NONE);
		button.setText("Browse...");
		button.setBounds(411, 5, 86, 27);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});

		final Label label_3 = new Label(container, SWT.NONE);
		label_3.setText("组件名称：");
		label_3.setBounds(10, 80, 86, 17);

		newComp = new Text(container, SWT.BORDER);
		newComp.setText("newComp");
		newComp.setBounds(102, 75, 395, 25);
		setTitle("新增业务组件");
		//
		return area;
	}
	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				path.setText(((Path) result[0]).toString());
			}
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
		return new Point(523, 304);
	}

	@Override
	protected void okPressed() {
		final String pathStr = path.getText();
		final String fileNameStr = fileName.getText();
		final String compDesc = comp_desc.getText();
		final String newCompStr = newComp.getText();
		final TitleAreaDialog dialog = this;
		IRunnableWithProgress process = new IRunnableWithProgress() {
			public void run(IProgressMonitor m)  throws InvocationTargetException, InterruptedException {
				try {
					m.beginTask("初始化组件.....", 2);
					IResource container = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(new Path(pathStr));
					IContainer base = (IContainer) container;
					final IFile file = base.getFile(new Path(fileNameStr));
					if(file.exists()){
						dialog.setErrorMessage("文件名已存在");
						return;
					}
					InputStream stream = openContentStream(compDesc,newCompStr);
					file.create(stream, true, null);
					file.refreshLocal(1,null);
					stream.close();
			    	m.worked(1);
			    } catch (Exception e) {
			    	e.printStackTrace();
			    	m.setTaskName(e.getMessage());
			    } finally {
			    	m.done();
			    }
			}
		};
		ProgressMonitorDialog d = new ProgressMonitorDialog(null);
		try {
			d.run(true, false, process);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.okPressed();
	}
	private InputStream openContentStream(String comp_desc,String comp_name) {
		String contents =
			"<?xml version=\"1.0\" encoding=\"GBK\"?><DefComponentModel comp_name='"+comp_name+"' comp_desc='"+comp_desc+"'  _class=\"ui.model.UIContentsModel\"></DefComponentModel>";
		return new ByteArrayInputStream(contents.getBytes());
	}
}
