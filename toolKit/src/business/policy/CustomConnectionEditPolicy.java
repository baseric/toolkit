package business.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import business.command.DeleteConnectionCommand;
import business.model.AbstractConnectionModel;

public class CustomConnectionEditPolicy extends ConnectionEditPolicy {

	@Override
	protected Command getDeleteCommand(GroupRequest arg0) {
		DeleteConnectionCommand command = new DeleteConnectionCommand();
		command.setConnection((AbstractConnectionModel)this.getHost().getModel());
		return command;
	}

}
