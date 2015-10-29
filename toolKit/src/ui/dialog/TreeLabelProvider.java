package ui.dialog;
 
import java.util.HashMap;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class TreeLabelProvider implements ILabelProvider { 
    public Image getImage(Object element) { 
        return null; 
    } 
    @SuppressWarnings("unchecked")
	public String getText(Object element) { 
    	HashMap table = (HashMap)element;
        return "["+table.get("tab_name")+"]"+table.get("tab_dsc"); 
    }
	public void addListener(ILabelProviderListener listener) {}
	public void dispose() {}
	public boolean isLabelProperty(Object element, String property) {return false;}
	public void removeListener(ILabelProviderListener listener) {} 

 }
