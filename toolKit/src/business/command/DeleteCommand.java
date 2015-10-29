package business.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import business.AbstractModel;
import business.model.AbstractConnectionModel;
import business.model.ContainerModel;

public class DeleteCommand extends Command {
	private ContainerModel contentsModel = null;
	private AbstractModel form = null; 
	public ContainerModel getContentsModel() {
		return contentsModel;
	}
	public void setContentsModel(Object contentsModel) {
		this.contentsModel =(ContainerModel) contentsModel;
	}
	public AbstractModel getHelloModel() {
		return form;
	}
	public void setHelloModel(Object helloModel) {
		this.form = (AbstractModel)helloModel;
	}
	@SuppressWarnings("unchecked")
	public List sourceConnections = new ArrayList();
	@SuppressWarnings("unchecked")
	public List targetConnections = new ArrayList();
	@SuppressWarnings("unchecked")
	public void execute() {
		sourceConnections.addAll(form.getSourceConnection());
		targetConnections.addAll(form.getTargetConnection());
		
		for(int i = 0;i<sourceConnections.size();i++){
			AbstractConnectionModel connection = (AbstractConnectionModel)sourceConnections.get(i);
			connection.detachSource();
			connection.detachTarget();
		}
		
		for(int i = 0;i<targetConnections.size();i++){
			AbstractConnectionModel connection = (AbstractConnectionModel)targetConnections.get(i);
			connection.detachSource();
			connection.detachTarget();
		}
		
		contentsModel.removeChild(form);
	}
	@Override
	public void undo() {
		contentsModel.addChild(form); 
		
		try {
			for(int i = 0;i<sourceConnections.size();i++){
				AbstractConnectionModel connection = (AbstractConnectionModel)sourceConnections.get(i);
				connection.attachSource();
				connection.attachTarget();
			}
			
			for(int i = 0;i<targetConnections.size();i++){
				AbstractConnectionModel connection = (AbstractConnectionModel)targetConnections.get(i);
				connection.attachSource();
				connection.attachTarget();
			}
			sourceConnections.clear();
			targetConnections.clear();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
