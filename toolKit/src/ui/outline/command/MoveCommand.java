package ui.outline.command;

import org.eclipse.gef.commands.Command;

import ui.UIAbstractModel;
import ui.model.abstractModel.ContainerModel;
import util.Log;

public class MoveCommand extends Command {
	private ContainerModel container = null;
	private UIAbstractModel model = null;
	private int index = 0;
	public ContainerModel getContainer() {
		return container;
	}

	public void setContainer(ContainerModel container) {
		this.container = container;
	}

	public UIAbstractModel getModel() {
		return model;
	}

	public void setModel(UIAbstractModel model) {
		this.model = model;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean canExecute() {
		return true;
	}

	@Override
	public void execute() {
		Log.write("reloadtion");
		container.reLocationIndex(model, index);
	}

}
