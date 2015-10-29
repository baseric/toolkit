package business.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import business.command.DeleteCommand;

public class DeleteEditPolicy extends ComponentEditPolicy {

	@Override
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
	}

}
