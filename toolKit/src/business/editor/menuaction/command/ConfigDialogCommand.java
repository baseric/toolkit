package business.editor.menuaction.command;

import org.eclipse.gef.commands.Command;

import business.AbstractModel;
import business.model.ContentsModel;

public class ConfigDialogCommand extends Command {


	private AbstractModel model = null;
	private String actionId = "";
	private String path = "";
	private String project_name = "";
	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String projectName) {
		project_name = projectName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public AbstractModel getModel() {
		return model;
	}

	public void setModel(AbstractModel model) {
		this.model = model;
	}

	@Override
	public boolean canExecute() {
		// TODO Auto-generated method stub
		if("configContents".equals(this.getActionId())&& model instanceof ContentsModel){
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		super.execute();
	 
	}

}
