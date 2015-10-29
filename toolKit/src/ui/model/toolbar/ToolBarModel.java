package ui.model.toolbar;

import ui.model.abstractModel.ContainerModel;

public class ToolBarModel extends ContainerModel {
	private static final long serialVersionUID = -7936074652763073691L;
	public ToolBarModel(String modelName){
		super(modelName);
		this.setVal("id", "toolbar_"+System.currentTimeMillis());
	}
}
