package ui.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import ui.UIAbstractModel;
import ui.command.DirectEditCommand;

public class DirectEditPolicyImpl extends DirectEditPolicy {
	/**
	 *  当选中cellEditor,修改文本,cellEditor失去焦点前执行
	 * @param arg0
	 * @return
	 * 2014-12-23
	 * @tianming
	 */
	@Override
	protected Command getDirectEditCommand(DirectEditRequest arg0) {
		DirectEditCommand command = new DirectEditCommand();
		Object model = getHost().getModel();
		command.setModel((UIAbstractModel)model);
		command.setNewText((String)arg0.getCellEditor().getValue());
		return command;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest arg0) {
		// TODO Auto-generated method stub

	}

}
