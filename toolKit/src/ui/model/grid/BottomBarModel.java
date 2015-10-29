package ui.model.grid;

import ui.model.abstractModel.ContainerModel;
import util.GloableParam;

public class BottomBarModel extends ContainerModel {
	private static final long serialVersionUID = -7936074652763073691L;
	public BottomBarModel(String modelName){
		super(modelName);
		this.setHeight("300");
		this.setWidth(String.valueOf(GloableParam.containerWidth));
	}
 
}
