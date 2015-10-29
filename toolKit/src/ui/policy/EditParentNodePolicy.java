package ui.policy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import ui.UIAbstractModel;
import ui.command.MoveOutCommand;

public class EditParentNodePolicy extends ContainerEditPolicy {
	/**
	 * 目标从当前节点移出时
	 */
	@Override
	protected Command getOrphanChildrenCommand(GroupRequest request) {
		try{
			MoveOutCommand command = new MoveOutCommand();
			AbstractGraphicalEditPart editPart =(AbstractGraphicalEditPart) request.getEditParts().get(0);
			command.setContentsModel(this.getHost().getModel());
			command.setForm((UIAbstractModel)editPart.getModel());
			return command;
		}catch(Exception e){
			
		}
		return null;
	}
	
	@Override
	protected Command getCreateCommand(CreateRequest arg0) {
		return null;
	}

}
