package business.editor;

import java.util.List;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

public class ActionLogicEditorContextMenuProvider extends ContextMenuProvider {
	private ActionRegistry actionRegistry;
	private List<String> actionList = null;
	public ActionLogicEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry,List<String> actionList) {
		super(viewer);
	    actionRegistry = registry;
	    this.actionList = actionList;
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
        GEFActionConstants.addStandardActionGroups(menu);
        
        IAction action = actionRegistry.getAction(ActionFactory.DELETE.getId());
        menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
        action = actionRegistry.getAction(ActionFactory.UNDO.getId());
        menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
        action = actionRegistry.getAction(ActionFactory.REDO.getId());
        menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
        
        for(int i = 0;i<actionList.size();i++){
            action = actionRegistry.getAction(actionList.get(i));
            if(action instanceof AlignmentAction){
            	menu.appendToGroup(GEFActionConstants.GROUP_VIEW, action);
            }else{
            	menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
            }
        }
        
        
	}
	
}
