package action;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;

public class ToolBarAction extends ActionBarContributor {
    protected void buildActions() {
        addRetargetAction(new UndoRetargetAction());
        addRetargetAction(new RedoRetargetAction());
        addRetargetAction(new DeleteRetargetAction());
        
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.CENTER));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM));
    }
    protected void declareGlobalActionKeys() {

    }
    public void contributeToToolBar(IToolBarManager toolBarManager) {
        toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
        toolBarManager.add(getAction(ActionFactory.REDO.getId()));
        toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
        
        toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_LEFT));
        toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_RIGHT));
        toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_CENTER));
        toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_TOP));
        toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_BOTTOM));
    }
	@Override
	public void setActiveEditor(IEditorPart editor) {
		try {
			super.setActiveEditor(editor);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

}
