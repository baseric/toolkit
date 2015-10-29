package ui.dialog.tablemodify.cellEditor;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import ui.UIAbstractModel;
import ui.dialog.tablemodify.DefDialogCellEditor;
import ui.dialog.tablemodify.cellEditor.dialog.MehtodConfigDialog;

public class MethodCellEditor extends DefDialogCellEditor {
	   private UIAbstractModel model = null;
	   public MethodCellEditor(Composite parent,UIAbstractModel model,HashMap<String,String> map){
	       super(parent);
	       this.model = model;
	   }
	   protected Object openDialogBox(Control cellEditorWindow) {
		   MehtodConfigDialog dialog = new MehtodConfigDialog(Display.getCurrent().getActiveShell(),String.valueOf(this.getValue()),model);
		   Object obj = dialog.open();
		   if(obj==null){
			   obj = String.valueOf(this.getValue());
		   }
		   return obj;
	   }
	   
	   protected Button createButton(Composite parent){
	        Button result = new Button(parent, SWT.PUSH);
	        result.setText("...");  
	        return result;
	   }
}
