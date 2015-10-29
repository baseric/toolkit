package ui.dialog.tablemodify.cellEditor;

import java.util.HashMap;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import ui.UIAbstractModel;
import ui.dialog.tablemodify.cellEditor.dialog.ValidateConfigDialog;

public class ValidateCellEditor extends DialogCellEditor {
	   private UIAbstractModel model = null;
	   public ValidateCellEditor(Composite parent,UIAbstractModel model,HashMap<String,String> map){
	       super(parent);
	       this.model = model;
	       this.updateContents(map.get("value"));
	   }
	   protected Object openDialogBox(Control cellEditorWindow) {
		   ValidateConfigDialog dialog = new ValidateConfigDialog(Display.getCurrent().getActiveShell(),model.val("validate"));
		   Object obj = dialog.open();
		   if(obj==null){
			   obj = model.val("validate");
		   }
		   model.setVal("validate",String.valueOf(obj));
		   return obj;
	   }
	   
	   protected Button createButton(Composite parent){
	        Button result = new Button(parent, SWT.PUSH);
	        result.setText("...");  
	        return result;
	   }
}
