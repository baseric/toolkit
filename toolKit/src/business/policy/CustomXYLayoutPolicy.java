package business.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import business.AbstractModel;
import business.command.ChangeConstraintCommand;
import business.command.CreateCommand;
public class CustomXYLayoutPolicy extends XYLayoutEditPolicy {
	public CustomXYLayoutPolicy(){}
	/**
	 * 调整大小时生成命令
	 */
	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		try{
			ChangeConstraintCommand command = new ChangeConstraintCommand();
			command.setModel(child.getModel());
			command.setConstraint(constraint);
			return command;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 新增节点
	 */
	@Override
	protected Command getCreateCommand(CreateRequest req) {
		try{
			Rectangle rect = (Rectangle)getConstraintFor(req);
			CreateCommand create = new CreateCommand();
			AbstractModel model = (AbstractModel)req.getNewObject();
			create.setContentsModel(this.getHost().getModel());
			create.setNode(model);
			create.setNodeRect(rect);
			return create;
		}catch(Exception e){
			//e.printStackTrace();
		}
		return null;
	}
}
