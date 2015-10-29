package ui.editpart.directedit;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.widgets.Text;

import ui.UIAbstractModel;
import ui.model.forms.element.LabelModel;
import ui.model.grid.ColumnModel;
import ui.model.liger.FormFieldModel;
import ui.model.tabpanel.TabItemModel;
import ui.model.toolbar.ToolBarItemModel;

public class MyDirectEditManager extends DirectEditManager {
	private UIAbstractModel model = null;
	public MyDirectEditManager(GraphicalEditPart source, Class editorType,
			CellEditorLocator locator) {
		super(source, editorType, locator);
		model = (UIAbstractModel)source.getModel();
	}

	@Override
	protected void initCellEditor() {
		// TODO Auto-generated method stub
		String value = "";
		if(model instanceof LabelModel){
			LabelModel label = (LabelModel)model;
			value = label.val("label");
		}else if(model instanceof ColumnModel){
			ColumnModel column = (ColumnModel)model;
			value = column.val("title");
		}else if(model instanceof TabItemModel){
			TabItemModel column = (TabItemModel)model;
			value = column.val("title");
		}else if(model instanceof FormFieldModel){
			FormFieldModel column = (FormFieldModel)model;
			value = column.val("label");
		}else if(model instanceof ToolBarItemModel){
			ToolBarItemModel column = (ToolBarItemModel)model;
			value = column.val("text");
		}
		getCellEditor().setValue(value);
		Text text = (Text)getCellEditor().getControl();
		text.selectAll();
	}

}
