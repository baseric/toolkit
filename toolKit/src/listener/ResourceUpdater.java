package listener;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;

import util.FileUtil;
import util.Log;

public class ResourceUpdater implements IResourceChangeListener {

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
			if (event.getDelta().getKind()==IResourceDelta.REMOVED) {
				IResource resource = event.getResource();
				if(resource instanceof IFile){
					IFile file = (IFile)resource;
					Log.write("删除文件:"+file.getLocation().toFile().getAbsolutePath());
					if(file.getFileExtension().endsWith(".ui")){
						File jsp = new File(FileUtil.getJspFilePath(file.getProject(),file.getLocation().toFile()));
						if(jsp.exists()){
							jsp.delete();
							Log.write("删除文件:"+jsp.getAbsolutePath());
						}
					}
				}
			}
		
		
	}

}
