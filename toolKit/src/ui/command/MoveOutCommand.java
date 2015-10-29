package ui.command;

import org.eclipse.gef.commands.Command;

import ui.UIAbstractModel;
import ui.model.abstractModel.ContainerModel;

public class MoveOutCommand extends Command{
	private ContainerModel contentsModel = null;
	private UIAbstractModel form = null;
	public ContainerModel getContentsModel() {
		return contentsModel;
	}
	public void setContentsModel(Object contentsModel) {
		this.contentsModel =(ContainerModel) contentsModel;
	}
 
	public UIAbstractModel getForm() {
		return form;
	}
	public void setForm(UIAbstractModel form) {
		this.form = form;
	}
	public void execute() {
		//Log.write("执行生成节点的命令：MoveOutCommand");
		contentsModel.removeChild(form);
	}
}
