package ui.dialog.tablemodify.cellEditor;

import java.util.HashMap;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import ui.UIAbstractModel;
import ui.dialog.tablemodify.cellEditor.dialog.ContextMenuConfigDialog;

public class ContextMenuConfigCellEditor extends DialogCellEditor {
	   private UIAbstractModel model = null;
	   public ContextMenuConfigCellEditor(Composite parent,UIAbstractModel model,HashMap<String,String> map){
	       super(parent);
	       this.model = model;
	   }
	   protected Object openDialogBox(Control cellEditorWindow) {
		   ContextMenuConfigDialog dialog = new ContextMenuConfigDialog(Display.getCurrent().getActiveShell(),model);
		   Object obj = dialog.open();
		   if(obj==null){
			   obj = model.val("contextMenu");
		   }
		   model.setVal("contextMenu",String.valueOf(obj));
		   return obj;
	   }
	   
	   protected Button createButton(Composite parent){
	        Button result = new Button(parent, SWT.PUSH);
	        result.setText("...");  
	        return result;
	   }
}
