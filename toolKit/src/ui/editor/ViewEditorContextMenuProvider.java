package ui.editor;

import java.util.List;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

public class ViewEditorContextMenuProvider extends ContextMenuProvider {
	private ActionRegistry actionRegistry;
	private List<String> actionList = null;
	public ViewEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry,List<String> actionList) {
		super(viewer);
		this.actionList = actionList;
	    actionRegistry = registry;
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		IAction action = null;
        try {
			GEFActionConstants.addStandardActionGroups(menu);
			for(int i = 0;i<actionList.size();i++){//自定义菜单
			    action = actionRegistry.getAction(actionList.get(i));
			    menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
			}
			action = actionRegistry.getAction(ActionFactory.DELETE.getId());//删除
			menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
			/*action = actionRegistry.getAction(ActionFactory.COPY.getId());//复制
			menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
			action = actionRegistry.getAction(ActionFactory.PASTE.getId());//粘帖
			menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
			action = actionRegistry.getAction(ActionFactory.UNDO.getId());
			menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
			action = actionRegistry.getAction(ActionFactory.REDO.getId());
			menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
