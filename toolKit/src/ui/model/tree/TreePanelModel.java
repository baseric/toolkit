package ui.model.tree;

import ui.model.abstractModel.ContainerModel;
import util.GloableParam;

@SuppressWarnings("serial")
public class TreePanelModel extends ContainerModel{
 
	public TreePanelModel(String modelName){
		super(modelName);
		this.setX("20");
		this.setY("20");
		this.setHeight("300");
		this.setVal("id","treepanel_"+System.currentTimeMillis());
		this.setWidth(String.valueOf(GloableParam.containerWidth));
	}
}
