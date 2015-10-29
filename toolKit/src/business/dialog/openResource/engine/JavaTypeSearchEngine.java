package business.dialog.openResource.engine;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import business.dialog.openResource.dialogs.JavaTypeNameMatch;
/**
 * 
 * @author CuiKun cuikunbj@cn.ibm.com
 *
 */
public class JavaTypeSearchEngine {

	private JavaTypeItemsFilter filter;
	private JavaTypeSearchRequestor requestor;
	private IProject project = null;//搜索的工程
	private String basePath = "";//起始路径
	private String extendName = "";//收索的文件类型
	public JavaTypeSearchEngine(JavaTypeSearchRequestor requestor,IProject project,String basePath,String extendName) {
		this.requestor = requestor;
		filter = this.requestor.getPluginSearchFilter();
		this.project = project;
		this.basePath = basePath;
		this.extendName = extendName;
	}

public void search() throws Exception {
	IContainer container = null;
	if(basePath!=null&&!"".equals(basePath)){
		container = project.getFolder(this.basePath);
	}else{
		container = project;
	}
    searchForFile(container);
}

private void searchForFile(IContainer parent)
			throws Exception {
		IResource[] members;
		try {
			members = parent.members();
		} catch (CoreException e) {
			throw e;
		}
		for (IResource resource : members) {
			if (resource instanceof IContainer) {
				IContainer container = (IContainer) resource;
				searchForFile(container);
			} else if (resource instanceof IFile) {
				IFile file = (IFile) resource;
				if(file.getFileExtension()==null){
					continue;
				}
				if ("".equals(extendName)||extendName.indexOf(file.getFileExtension())>-1) {
					String authorName = getJavaTypeAuthorName(file);
					if (filter.matches(authorName)) {
						requestor.add(new JavaTypeNameMatch(file, authorName));
					}
				}
			}
		}
	}

	public String getJavaTypeAuthorName(IFile file)
			throws Exception {
		try {
			return file.getName();
		} catch (Exception e) {
			throw  e;
		}
	}
}
