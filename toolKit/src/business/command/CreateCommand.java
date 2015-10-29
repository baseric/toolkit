package business.command;

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import business.AbstractModel;
import business.model.ContainerModel;
import business.model.action.StartModel;
import business.model.logic.TransBeginModel;
import business.model.logic.TransEndModel;

public class CreateCommand extends Command {
	private ContainerModel contentsModel = null;
	private AbstractModel node = null;
	private Rectangle nodeRect = null;
	public Rectangle getNodeRect() {
		return nodeRect;
	}
	public void setNodeRect(Rectangle nodeRect) {
		this.nodeRect = nodeRect;
	}
	public ContainerModel getContentsModel() {
		return contentsModel;
	}
	public void setContentsModel(Object contentsModel) {
		this.contentsModel =(ContainerModel) contentsModel;
	}
 
	public AbstractModel getNode() {
		return node;
	}
	public void setNode(AbstractModel node) {
		this.node = node;
	}
	public void execute() {
		node.setRect(nodeRect);
		node.parentModel = contentsModel;
		contentsModel.addChild(node);
		if(node instanceof TransBeginModel){
			Rectangle rect = nodeRect.getCopy();
			rect.x = rect.x +150;
			rect.y = rect.y +13;
			TransEndModel end = new TransEndModel();
			end.setRect(rect);
			end.parentModel = contentsModel;
			contentsModel.addChild(end);
		}
	}
	
	@Override
	public boolean canExecute() {
		if(node instanceof StartModel){
			List<AbstractModel> children = contentsModel.getChildren();
			for(int i = 0;i<children.size();i++){
				if(children.get(i) instanceof StartModel){
					return false;
				}
			}
		}
		return super.canExecute();
	}
	@Override
	public void undo() {
		contentsModel.removeChild(node);
	}
}
