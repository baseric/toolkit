package business.dialog.openResource.util;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.util.BundleUtility;

import toolkit.Activator;
import business.dialog.openResource.dialogs.JavaTypeNameMatch;

/**
 * @author CuiKun
 * 
 */
public class JavaTypeInfoUtil {

	private static final String CONCAT_STRING = " - ";

	public JavaTypeInfoUtil() {

	}

	public String getFullyQualifiedText(JavaTypeNameMatch type)
			throws Exception {
		StringBuffer result = new StringBuffer();
		String authorName = type.getAuthorName();
		if (authorName != null) {
			result.append("\"" + authorName + "\"");
			result.append(CONCAT_STRING);
		}
		result.append(type.getSimpleTypeName());
		String containerName = type.getPackageName();
		if (containerName.length() > 0) {
			result.append(CONCAT_STRING);
			result.append(containerName);
		}
		result.append(CONCAT_STRING);
		result.append(type.getContainerName());
		return result.toString();
	}

	public Image getContributedImageDescriptor(Object element) {
		URL url = BundleUtility.find(Activator.getDefault().getBundle(),"/icons/class.gif");
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
		Image image = new Image(PlatformUI.getWorkbench().getDisplay(),
				descriptor.getImageData());
		return image;
	}
}