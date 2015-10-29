package business.command;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import business.AbstractModel;
public class ChangeConstraintCommand extends Command {
	private AbstractModel model = null;
	private Rectangle constraint = null;
	private Rectangle oldConstraint = null;
	public void setModel(Object model) {
		this.model =(AbstractModel) model;
	}
	public void setConstraint(Object constraint) {
		oldConstraint = this.model.getRect();
		this.constraint =(Rectangle) constraint;
	}
	@Override
	public void execute() {
		model.setRect(this.constraint);
		
	}
 
	@Override
	public void undo() {
		model.setRect(this.oldConstraint);
	}

}
