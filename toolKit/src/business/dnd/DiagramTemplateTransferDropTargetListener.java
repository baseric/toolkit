package business.dnd;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;

public class DiagramTemplateTransferDropTargetListener extends
AbstractTransferDropTargetListener  {
	 //初始化命令所需要的工厂，第二步创建   
	 private IFile file = null;
     public DiagramTemplateTransferDropTargetListener(EditPartViewer viewer, Transfer xfer) { 
        super(viewer, xfer);  
     }  
	  //应根据需求选择自己的类型，默认为处理文本拖放的TextTransfer    
	 public DiagramTemplateTransferDropTargetListener(EditPartViewer viewer,IFile file) {  
	         super(viewer,LocalSelectionTransfer.getTransfer());  
	         this.file = file;
	 }  
 
	@SuppressWarnings("unchecked")
	@Override
	public void dragOver(DropTargetEvent event) {
		LocalSelectionTransfer transfer = (LocalSelectionTransfer)this.getTransfer();
		ISelection selection = transfer.getSelection();
		if(selection instanceof StructuredSelection){
			 StructuredSelection select = (StructuredSelection)selection;
			 if(!(select.getFirstElement() instanceof HashMap)){
				 event.detail = DND.DROP_NONE;
			 }else{
				 super.dragOver(event);
			 }
		 }
	}
	/* 
	  *返回GEF中创建模型所需要的request，对request的设置采用工厂模式 
	  */  
	protected Request createTargetRequest() {
		 try {
			 Point point =  this.getDropLocation();
			 LocalSelectionTransfer transfer = (LocalSelectionTransfer)this.getTransfer();
			 ISelection selection = transfer.getSelection();
			 if(selection instanceof TreeSelection){
				 TreeSelection treeSelect = (TreeSelection)selection;
				 Object element = treeSelect.getFirstElement();
				 ElementFactory factory = new ElementFactory(element,file);  
			     CreateRequest request = new CreateRequest();  
			     request.setFactory(factory);  
			     request.setLocation(point);
			     return request;
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return null;
	  }  
	     
	  protected void updateTargetRequest() {}  

}
