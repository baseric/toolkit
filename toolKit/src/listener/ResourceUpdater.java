package listener;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;

public class ResourceUpdater implements IResourceChangeListener {

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
			//处理POST_CHANGE事件
			if (event.getType() == IResourceChangeEvent.POST_CHANGE){
				return;
			}else if (event.getType() == IResourceChangeEvent.PRE_BUILD){
				System.out.println();
				return;
			}
	}

}
