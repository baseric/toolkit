package business.editor.menuaction.command;

import org.eclipse.core.resources.IFile;
import org.eclipse.gef.commands.Command;

import business.model.ContentsModel;

public class CreateCodeCommand extends Command {
	public IFile file = null;
	public IFile getFile() {
		return file;
	}

	public void setFile(IFile file) {
		this.file = file;
	}
	public ContentsModel contents = null;
	public ContentsModel getContents() {
		return contents;
	}

	public void setContents(ContentsModel contents) {
		this.contents = contents;
	}

	@Override
	public boolean canExecute() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
	
		 
	}

}
