package ui.dialog.tablemodify.cellEditor;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import ui.UIAbstractModel;
import ui.dialog.tablemodify.DefDialogCellEditor;
import ui.dialog.tablemodify.cellEditor.dialog.HotKeyConfigDialog;

public class HotKeyCellEditor extends DefDialogCellEditor {
		private  UIAbstractModel model = null;	
		public HotKeyCellEditor(Composite parent,UIAbstractModel model,HashMap<String,String> map){
			super(parent);
			this.model = model;
		}
		protected Object openDialogBox(Control cellEditorWindow) {
			HotKeyConfigDialog dialog = new HotKeyConfigDialog(Display.getCurrent().getActiveShell(),model);
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
