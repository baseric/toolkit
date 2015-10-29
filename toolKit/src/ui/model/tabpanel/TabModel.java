package ui.model.tabpanel;

import ui.model.abstractModel.ContainerModel;

@SuppressWarnings("serial")
public class TabModel extends ContainerModel{
	public TabModel(String modelName) {
		super(modelName);
		this.setVal("id","tab_"+System.currentTimeMillis());
	}
}
