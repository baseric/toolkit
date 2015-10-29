package business.command;

import org.eclipse.gef.commands.Command;

import business.AbstractModel;
import business.model.AbstractConnectionModel;

public class ReconnectConnectionCommand extends Command {
	public AbstractConnectionModel connection;
	public AbstractModel newSource = null,newTarget = null;
	public AbstractModel oldSource,oldTarget;
	@Override
	public void execute() {
		 oldSource = connection.source;
		 oldTarget = connection.target;
		 if(newSource!=null){
			 connection.detachSource();
			 connection.setSource(newSource);
			 connection.attachSource();
		 }
		 if(newTarget!=null){
			 connection.detachTarget();
			 connection.setTarget(newTarget);
			 connection.attachTarget();
		 }
	}

	@Override
	public void undo() {
		 
	}

	public AbstractConnectionModel getConnection() {
		return connection;
	}

	public void setConnection(AbstractConnectionModel connection) {
		this.connection = connection;
	}

	public AbstractModel getNewSource() {
		return newSource;
	}

	public void setNewSource(AbstractModel newSource) {
		this.newSource = newSource;
	}

	public AbstractModel getNewTarget() {
		return newTarget;
	}

	public void setNewTarget(AbstractModel newTarget) {
		this.newTarget = newTarget;
	}
	
}
