package ui.command;

import org.eclipse.gef.commands.Command;

import ui.UIAbstractModel;
import ui.model.abstractModel.ContainerModel;
import ui.model.layout.CenterModel;

public class DeleteCommand extends Command {
	private ContainerModel contentsModel = null;
	private UIAbstractModel form = null; 
	public ContainerModel getContentsModel() {
		return contentsModel;
	}
	public void setContentsModel(Object contentsModel) {
		this.contentsModel =(ContainerModel) contentsModel;
	}
	public UIAbstractModel getHelloModel() {
		return form;
	}
	public void setHelloModel(Object helloModel) {
		this.form = (UIAbstractModel)helloModel;
	}
	public void execute() {
		if(form instanceof CenterModel){
			form = form.parentModel;
			contentsModel = (ContainerModel)form.parentModel;
		}
		
		UIAbstractModel temp = form.getDefComponentByParent();
		if(temp!=null){
			contentsModel =(ContainerModel) temp.parentModel;
			form = temp;
		}
		contentsModel.removeChild(form);
	}
	@Override
	public void undo() {
		contentsModel.addChild(form); 
	}
	
}
