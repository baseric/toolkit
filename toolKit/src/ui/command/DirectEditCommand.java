package ui.command;

import org.eclipse.gef.commands.Command;

import ui.UIAbstractModel;
import ui.model.forms.element.LabelModel;
import ui.model.grid.ColumnModel;
import ui.model.liger.FormFieldModel;
import ui.model.tabpanel.TabItemModel;
import ui.model.toolbar.ToolBarItemModel;

public class DirectEditCommand extends Command {
	private String oldText,newText;
	private UIAbstractModel model = null;
	@Override
	public void execute() {
		if(model instanceof LabelModel){
			LabelModel label = (LabelModel)model;
			oldText = label.val("label");
			label.setVal("label",newText);
		}else if(model instanceof ColumnModel){
			ColumnModel column = (ColumnModel)model;
			oldText = column.val("title");
			column.setVal("title",newText);
		}else if(model instanceof TabItemModel){
			TabItemModel column = (TabItemModel)model;
			oldText = column.val("title");
			column.setVal("title",newText);
		}else if(model instanceof FormFieldModel){
			FormFieldModel column = (FormFieldModel)model;
			oldText = column.val("label");
			column.setVal("label",newText);
		}else if(model instanceof ToolBarItemModel){
			ToolBarItemModel column = (ToolBarItemModel)model;
			oldText = column.val("text");
			column.setVal("text",newText);
		}
	}
	public String getOldText() {
		return oldText;
	}
	public void setOldText(String oldText) {
		this.oldText = oldText;
	}
	public String getNewText() {
		return newText;
	}
	public void setNewText(String newText) {
		this.newText = newText;
	}
	public UIAbstractModel getModel() {
		return model;
	}
	public void setModel(UIAbstractModel model) {
		this.model = model;
	}
	public void undo(){
		if(model instanceof LabelModel){
			LabelModel label = (LabelModel)model;
			label.setVal("label",oldText);
		}else if(model instanceof ColumnModel){
			ColumnModel column = (ColumnModel)model;
			column.setVal("display",oldText);
		}
	}
}
