package ui.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import ui.command.DeleteCommand;
import ui.model.grid.BottomBarModel;
import ui.model.grid.ColumnBarModel;

public class DeleteEditPolicy extends ComponentEditPolicy {
	/**
	 * 删除节点
	 * @param deleteRequest
	 * @return
	 * 2015-4-12
	 * @tianming
	 */
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		try{
			Object model = this.getHost().getModel();
			if( !(model instanceof ColumnBarModel)&&
				!(model instanceof BottomBarModel) 
				){
				
				DeleteCommand delete = new DeleteCommand();
				delete.setContentsModel(getHost().getParent().getModel());
				delete.setHelloModel(getHost().getModel());
				return delete;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
