package ui.model.layout;

import ui.model.abstractModel.ContainerModel;

@SuppressWarnings("serial")
public class LayoutPanelModel extends ContainerModel{
	public LayoutPanelModel(String modelName){
		super(modelName);
		this.setVal("id","layout"+System.currentTimeMillis());
	}
}
