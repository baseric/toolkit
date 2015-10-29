package business.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import business.AbstractModel;
import business.command.CreateConnectionCommand;
import business.command.ReconnectConnectionCommand;
import business.model.AbstractConnectionModel;

public class CustomNodeEditPolicy extends GraphicalNodeEditPolicy {
	/**
	 * 第三步：将命令从栈中取出，设置终点
	 * @param request
	 * @return
	 * 2014-12-28
	 * @tianming
	 */
	@Override 
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		try {
			CreateConnectionCommand command = (CreateConnectionCommand)request.getStartCommand();
			command.setTarget((AbstractModel)this.getHost().getModel());
			return command;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		try {
			//第一步：创建命令
			CreateConnectionCommand command = new CreateConnectionCommand();
			command.setConnection((AbstractConnectionModel)request.getNewObject());
			command.setSource((AbstractModel)this.getHost().getModel());
			//第二步：将命令放入栈中
			request.setStartCommand(command);
			
			return command;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 重新定位连接线的源点
	 */
	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		ReconnectConnectionCommand command = new ReconnectConnectionCommand();
		command.setConnection((AbstractConnectionModel)request.getConnectionEditPart().getModel());
		command.setNewSource((AbstractModel)getHost().getModel());
		return command;
	}
	/**
	 * 重新定位连接线的终点
	 */
	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		ReconnectConnectionCommand command = new ReconnectConnectionCommand();
		command.setConnection((AbstractConnectionModel)request.getConnectionEditPart().getModel());
		command.setNewTarget((AbstractModel)getHost().getModel());
		return command;
	}

}
