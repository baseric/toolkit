package action.wizard;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import toolkit.Activator;
import util.Log;
import action.wizard.crudpage.BasicInfoPage;
import action.wizard.crudpage.ListPage;
import action.wizard.crudpage.ModifyConfigPage;
public class CRUDWizard extends Wizard
implements INewWizard 
{
  private BasicInfoPage fPage = new BasicInfoPage();
  private ListPage fPage2 = new ListPage();
  private ModifyConfigPage fPage3 = new ModifyConfigPage();
  public CRUDWizard()
  {
    setDialogSettings(Activator.getDefault().getDialogSettings());
    setWindowTitle("单表CURD代码生成");
    setNeedsProgressMonitor(true);
  }
  
  public void init(IWorkbench workbench, IStructuredSelection selection)
  {
  }

  public boolean performFinish()
  {
		IRunnableWithProgress process = new IRunnableWithProgress() {
			public void run(final IProgressMonitor m)  throws InvocationTargetException, InterruptedException {
				try {
					Display.getDefault().syncExec(new Runnable() {
					    public void run() {
					    	try {
					    		m.beginTask("创建目录", 2);
					    		fPage.createFolder();
								m.beginTask("生成列表查询配置文件，并生成代码", 2);
								fPage.createSearchList();
								m.beginTask("生成新增配置文件，并生成代码", 2);
								fPage.createInsertCode();
								fPage.createService();
								m.beginTask("生成修改配置文件，并生成代码", 2);
								fPage.createModifyCode();
								//页面生成
								
						    	m.setTaskName("保存页面信息到数据库");
						    	m.worked(1);
							} catch (Exception e) {
								e.printStackTrace();
							}
					    }
					});
			    } catch (Exception e) {
			    	Log.write("", e);
			    	m.setTaskName(e.getMessage());
			    } finally {
			    	m.done();
			    }
			}
		};
		try {
			getContainer().run(true, false, process);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
  }
  public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
    throws CoreException
  {
     
  }

  public void addPages()
  {
	this.fPage.setList(fPage2);
	this.fPage.setModify(fPage3);
    addPage(this.fPage);
    addPage(this.fPage2);
    addPage(this.fPage3);
  }
}