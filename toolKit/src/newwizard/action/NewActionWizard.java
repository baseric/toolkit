package newwizard.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import newwizard.NewUIPage;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;


public class NewActionWizard extends Wizard implements INewWizard {
	private NewUIPage page;
	private ISelection selection;

	public NewActionWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	

	public void addPages() {
		String rootPath = null;
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer){
					container = (IContainer) obj;
				}else{
					container = ((IResource) obj).getParent();
				}
				IProject project = container.getProject();
				rootPath =project.getLocation().toFile().getAbsolutePath()+"/plugin_resource/";
			}
		}
		page = new NewUIPage(selection,"ctl",rootPath);
		addPage(page);
	}

	public boolean performFinish() {
		final String containerName = page.getContainerName();
		final String fileName = page.getFileName();
		final String templateFile = page.getTemplateFile();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName,templateFile, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}
	

	private void doFinish(
		String containerName,
		String fileName,
		String templateFile,
		IProgressMonitor monitor)
		throws CoreException {
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = openContentStream(templateFile);
			file.create(stream, true, monitor);
			stream.close();
		} catch (IOException e) {
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}
	
	private InputStream openContentStream(String templateFile) {
		String contents =
			"<?xml version=\"1.0\" encoding=\"GBK\"?><ContentsModel class=\"business.model.ContentsModel\" count=\"5\"></ContentsModel>";
		if(templateFile!=null){
			contents = NewActionWizard.readFile(templateFile);
		}
		return new ByteArrayInputStream(contents.getBytes());
	}

	/**
	  * 
	  * 描述：读文件
	  * @param desFilePath
	  * @return
	  * 作者：tianming
	  * 日期：2013-3-30
	  */
	 public static String readFile(String desFilePath){
		File f = new File(desFilePath);
		FileInputStream in = null;
		StringBuffer content = new StringBuffer();
		byte[] b = null;
		try {
			in = new FileInputStream(f);
			b = new byte[in.available()];
	        in.read(b);
			content.append(new String(b,"GB18030"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		f = null;
		b = null;
		return content.toString();
	 }
	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "Tttttttt", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}
