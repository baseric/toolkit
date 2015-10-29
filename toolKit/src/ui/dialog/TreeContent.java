package ui.dialog;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
public class TreeContent implements ITreeContentProvider { 
    @SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
        if(inputElement instanceof List){
        	List list = (List)inputElement;
        	return list.toArray();
        }
        return null;  
    } 
    @SuppressWarnings("unchecked")
	public Object[] getChildren(Object parentElement) {
    	 if(parentElement instanceof HashMap){
    		 HashMap comp = (HashMap)parentElement;
         	return ((List)comp.get("children")).toArray();
         }
         return null;
    } 
    @Override
    public Object getParent(Object element) { 
        return null; 
    }
    public boolean hasChildren(Object element) { 
    	/*if(element instanceof AController){
    		AController comp = (AController)element;
         	return comp.isContainer&&comp.children.size()>0;
         }*/
    	return false;  
    }
	public void dispose() {}
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {} 
 }