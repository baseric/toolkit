package ui.outline.policy;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.TreeContainerEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import ui.UIAbstractModel;
import ui.model.abstractModel.ContainerModel;
import ui.outline.ContentsTreeEditPart;
import ui.outline.FormTreeEditPart;
import ui.outline.command.MoveCommand;
import util.Log;

public class MyComponentEditPolicy extends TreeContainerEditPolicy {

/*    
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		try{
			DeleteCommand delete = new DeleteCommand();
			delete.setContentsModel(getHost().getParent().getModel());
			delete.setHelloModel(getHost().getModel());
			return delete;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}*/
	protected Command getMoveCommand(ChangeBoundsRequest req) {
		EditPart parent = getHost().getParent();
		if (parent != null) {
			ChangeBoundsRequest request = new ChangeBoundsRequest(
					REQ_MOVE_CHILDREN);
			request.setEditParts(getHost());
			request.setLocation(req.getLocation());
			return parent.getCommand(request);
		}
		return UnexecutableCommand.INSTANCE;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected Command getAddCommand(ChangeBoundsRequest req) {
		try{
		    List<FormTreeEditPart> list = req.getEditParts();
		    FormTreeEditPart move = list.get(0);
		    ContainerModel container = null;
		    if(move.getParent() instanceof FormTreeEditPart){
		    	FormTreeEditPart parent = (FormTreeEditPart)move.getParent();
		    	container = (ContainerModel)parent.getModel();
		    }else if(move.getParent() instanceof ContentsTreeEditPart){
		    	ContentsTreeEditPart parent = (ContentsTreeEditPart)move.getParent();
		    	container = (ContainerModel)parent.getModel();
		    }
		    TreeViewer tree = (TreeViewer)move.getViewer();
		    Tree treeObj = (Tree)tree.getControl();
		    Point p = req.getLocation();
		    TreeItem item = treeObj.getItem(new org.eclipse.swt.graphics.Point(p.x,p.y));
		    int i = 0;
		    TreeItem parentNode = item.getParentItem();
		    TreeItem[] children = null;
		    if(parentNode!=null){
		    	children = parentNode.getItems();
		    }else {
		    	children = treeObj.getItems();
		    }
		    for(;i<children.length;i++){
		    	if(item==children[i]){
		    		break;
		    	}
		    }
		    MoveCommand command = new MoveCommand();
		    command.setIndex(i);
		    command.setModel((UIAbstractModel)move.getModel());
		    command.setContainer(container);
		    return command;
		}catch(Exception e){
			Log.write("", e);
		}
		return null;
	}

	@Override
	protected Command getCreateCommand(CreateRequest arg0) {
		return null;
	}

	@Override
	protected Command getMoveChildrenCommand(ChangeBoundsRequest arg0) {
		// TODO Auto-generated method stub
		//Log.write("xxxxxgetMoveChildrenCommand");
		return new MoveCommand();
	}
 
}
