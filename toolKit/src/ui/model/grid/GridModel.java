package ui.model.grid;

import ui.model.abstractModel.ContainerModel;

public class GridModel extends ContainerModel {
	private static final long serialVersionUID = -7936074652763073691L;

	public GridModel(String modelName){
		super(modelName);
		this.setVal("id","grid"+System.currentTimeMillis());
	}
}
