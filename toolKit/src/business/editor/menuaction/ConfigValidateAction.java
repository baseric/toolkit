package business.editor.menuaction;

import images.IconFactory;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import business.AbstractModel;
import business.EditPartWithListener;
import business.editor.menuaction.command.ConfigDialogCommand;
import business.model.ContentsModel;
 

public class ConfigValidateAction extends SelectionAction {
	public IFile file = null;
	public ContentsModel contents = null;
	public ConfigValidateAction(IWorkbenchPart part) {
		super(part);
	    this.setImageDescriptor(IconFactory.getImageDescriptor("business/node.gif"));
	}
	public void run() {
	    execute(createCommand());
	}
	
	private Command createCommand() {
	  try {
			List<?> objects = getSelectedObjects();
			    if (objects.isEmpty())
			        return null;
			    
			    for (Iterator<?> iter = objects.iterator(); iter.hasNext();) {
			        Object obj = iter.next();
			        	ConfigDialogCommand command = new ConfigDialogCommand();
			        	command.setModel((AbstractModel)((EditPartWithListener)obj).getModel());
			        	command.setActionId(this.getId());
			        	command.setPath(file.getLocation().toFile().getAbsolutePath());
			        	command.setProject_name(file.getProject().getName());
			            return command;
			    }
		} catch (Exception e) {
			//e.printStackTrace();
		}
	    
	    return null;
	}

	protected boolean calculateEnabled() {
	    Command cmd = createCommand();
	    if (cmd == null)
	        return false;
	    return cmd.canExecute(); 
	}

}
