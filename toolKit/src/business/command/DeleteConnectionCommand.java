package business.command;

import org.eclipse.gef.commands.Command;

import business.model.AbstractConnectionModel;

public class DeleteConnectionCommand extends Command {
	private AbstractConnectionModel connection;
	public void execute(){
		connection.detachSource();
		connection.detachTarget();
	}
	public void undo(){
		connection.attachSource();
		connection.attachTarget();
	}
	public AbstractConnectionModel getConnection() {
		return connection;
	}
	public void setConnection(AbstractConnectionModel connection) {
		this.connection = connection;
	}
	
}
