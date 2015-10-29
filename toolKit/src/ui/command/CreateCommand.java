package ui.command;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.gef.commands.Command;

import ui.UIAbstractModel;
import ui.model.DefComponentModel;
import ui.model.UIContentsModel;
import ui.model.abstractModel.ContainerModel;
import ui.model.forms.TableModel;
import ui.model.forms.TitleModel;
import ui.model.grid.BottomBarModel;
import ui.model.grid.ColumnBarModel;
import ui.model.grid.GridModel;
import ui.model.layout.CenterModel;
import ui.model.layout.LayoutPanelModel;
import ui.model.layout.LeftModel;
import ui.model.layout.TopModel;
import ui.model.liger.FormModel;
import ui.model.toolbar.ToolBarModel;
import ui.util.UIGenerateCode;

public class CreateCommand extends Command {
	private ContainerModel contentsModel = null;
	private UIAbstractModel child = null;
	public ContainerModel getContentsModel() {
		return contentsModel;
	}
	public void setContentsModel(Object contentsModel) {
		this.contentsModel =(ContainerModel) contentsModel;
	}
 
	public UIAbstractModel getForm() {
		return child;
	}
	public void setChild(UIAbstractModel form) {
		this.child = form;
	}
	public void execute() {
		try {
			if(child instanceof LeftModel||child instanceof TopModel){
				contentsModel =(ContainerModel) contentsModel.parentModel;
			}else if(child instanceof TableModel){
				boolean flag = true;
				TableModel table = (TableModel)child;
				for(int i = 0;i<table.getChildren().size();i++){
					if(table.getChildren().get(i) instanceof TitleModel){
						flag = false;
						break;
					}
				}
				if(flag){
					((ContainerModel)child).addChild(new TitleModel("TitleModel"));
				}
			}else if(child instanceof LayoutPanelModel){
				if(((LayoutPanelModel) child).getChildren().size()==0){
					LayoutPanelModel layout = (LayoutPanelModel) child;
					LeftModel left = new LeftModel("LeftModel");
					TopModel top = new TopModel("TopModel");
					CenterModel center = new CenterModel("CenterModel");
					layout.addChild(left);
					layout.addChild(center);
					layout.addChild(top);
					left.parentModel = child;
					center.parentModel = child;
					top.parentModel = layout;
				}
			}else if((child instanceof ToolBarModel) && (contentsModel instanceof FormModel)){
				child.parentModel = contentsModel;
				contentsModel.addChild(child,0);
				return ;
			}else if(child instanceof GridModel){
				if(((GridModel) child).getChildren().size()==0){
					ColumnBarModel columnBar = new ColumnBarModel("ColumnBarModel");
					BottomBarModel bottomBar = new BottomBarModel("BottomBarModel");
					((GridModel) child).addChild(columnBar);
					((GridModel) child).addChild(bottomBar);
					columnBar.parentModel = child;
					bottomBar.parentModel = child;
				}
			}else if(child instanceof UIContentsModel){
				UIGenerateCode code = new UIGenerateCode();
				DefComponentModel component = new DefComponentModel(child.getModelName());
				IProject project = contentsModel.getFile().getProject();
				IFile file =(IFile) project.findMember(component.val("comp_dir"));
				code.setProject(project);
				UIContentsModel contents = (UIContentsModel)code.readFile(file.getLocation().toFile());
				component.setChildren(contents.getChildren());
				for(int i = 0;i<component.getChildren().size();i++){
					UIAbstractModel c = component.getChildren().get(i);
					c.parentModel = component;
				}
				child = component;
			}
			
			child.parentModel = contentsModel;
			contentsModel.addChild(child);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void undo() {
		contentsModel.removeChild(child);
	}
}
