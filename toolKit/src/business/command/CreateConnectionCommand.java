package business.command;

import org.eclipse.gef.commands.Command;

import business.AbstractModel;
import business.model.AbstractConnectionModel;

public class CreateConnectionCommand extends Command {
	//链接的起点和终点
	private AbstractModel source,target;
	private AbstractConnectionModel connection ;
	@Override
	public boolean canExecute() {
		return source!=null&&target!=null&&source!=target;
	}
	@Override
	public void execute() {
		try {
			connection.attachSource();
			connection.attachTarget();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void undo() {
		connection.detachSource();
		connection.detachTarget();
	}
	public AbstractModel getSource() {
		return source;
	}
	public void setSource(AbstractModel source) {
		this.source = source;
		connection.setSource(source);
	}
	public AbstractModel getTarget() {
		return target;
	}
	public void setTarget(AbstractModel target) {
		this.target = target;
		connection.setTarget(target);
	}
	public AbstractConnectionModel getConnection() {
		return connection;
	}
	public void setConnection(AbstractConnectionModel connection) {
		this.connection = connection;
	}
	
}
