package ui.outline;

import images.IconFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;

import ui.UIAbstractModel;
import ui.model.abstractModel.ContainerModel;
import ui.model.forms.FormButtonModel;
import ui.model.forms.FormElementModel;
import ui.model.forms.element.LabelModel;
import ui.model.grid.ColumnModel;
import ui.model.liger.FormFieldModel;
import ui.model.tabpanel.TabItemModel;
import ui.model.toolbar.ToolBarItemModel;
import ui.outline.policy.MyComponentEditPolicy;
import ui.policy.DeleteEditPolicy;

public class FormTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener{
	private List<Map<String,String>> toolsList = null;
	public FormTreeEditPart(Object model,List<Map<String,String>> toolsList  ) {
		super(model);
		this.toolsList = toolsList;
	}
	@Override
	protected void activateEditPolicies() {
		super.activateEditPolicies();
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new MyComponentEditPolicy());
		installEditPolicy("1", new DeleteEditPolicy());
	}
	public void propertyChange(PropertyChangeEvent evt) {
		refreshVisuals();
		if(getModel() instanceof ContainerModel){
			refreshChildren();
		}
	}
	public void activate() {
		super.activate();
		((UIAbstractModel) getModel()).addListener(this);
	}
	public void deactivate() {
		super.deactivate();
		((UIAbstractModel) getModel()).deleteListener(this);
	}
	protected List<?> getModelChildren() {
		if(getModel() instanceof ContainerModel){
			return ((ContainerModel) getModel()).getChildren();
		}else{
			return null;
		}
	}
	protected void refreshVisuals() {
		String name = getModel().getClass().getSimpleName();
		setWidgetText(name);
		try{
			String iconStr = "folder.gif";
			for(int i = 0;i<toolsList.size();i++){
				Map<String,String> tool = toolsList.get(i);
				if(tool.get("class").equals(getModel().getClass().getName())){
					iconStr = tool.get("icon");
					break;
				}
			}
			Image icon = IconFactory.getImageDescriptor("ui/"+iconStr).createImage();
			setWidgetImage(icon);
			if(getModel() instanceof LabelModel){
				FormElementModel model = (FormElementModel)getModel();
				setWidgetText(model.val("label"));
			}else if(getModel() instanceof ToolBarItemModel){ 
				ToolBarItemModel model = (ToolBarItemModel)getModel();
				setWidgetText(model.val("text"));
			}else if(getModel() instanceof FormButtonModel){ 
				FormButtonModel model = (FormButtonModel)getModel();
				setWidgetText(model.val("value"));
			}else if(getModel() instanceof TabItemModel){
				TabItemModel model = (TabItemModel)getModel();
				setWidgetText(model.val("title"));
			}else if(getModel() instanceof FormElementModel){
				FormElementModel model = (FormElementModel)getModel();
				setWidgetText(model.val("name").toLowerCase());
			}else if(getModel() instanceof ColumnModel){
				ColumnModel model = (ColumnModel)getModel();
				setWidgetText(model.val("title")+"("+model.val("field").toLowerCase()+")");
			}else if(getModel() instanceof FormFieldModel){
				FormFieldModel model = (FormFieldModel)getModel();
				setWidgetText(model.val("label")+"("+model.val("name").toLowerCase()+")");
			}
		}catch(Exception e){
			Image icon = IconFactory.getImageDescriptor("ui/folder.gif").createImage();
			setWidgetImage(icon);
		}
	}
}
