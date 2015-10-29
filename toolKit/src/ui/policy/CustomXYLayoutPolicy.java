package ui.policy;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import ui.UIAbstractModel;
import ui.command.ChangeConstraintCommand;
import ui.command.CreateCommand;
import ui.model.JspIncludeModel;
import ui.model.PanelModel;
import ui.model.UIContentsModel;
import ui.model.abstractModel.ContainerModel;
import ui.model.forms.FormElementModel;
import ui.model.forms.IframeModel;
import ui.model.forms.TableModel;
import ui.model.grid.ColumnBarModel;
import ui.model.grid.ColumnModel;
import ui.model.grid.GridModel;
import ui.model.layout.CenterModel;
import ui.model.layout.LayoutPanelModel;
import ui.model.layout.LeftModel;
import ui.model.layout.TopModel;
import ui.model.liger.FormFieldModel;
import ui.model.liger.FormModel;
import ui.model.tabpanel.TabItemModel;
import ui.model.tabpanel.TabModel;
import ui.model.toolbar.ToolBarItemModel;
import ui.model.toolbar.ToolBarModel;
import ui.model.tree.TreePanelModel;
 
public class CustomXYLayoutPolicy extends XYLayoutEditPolicy {
	public static ContainerModel currentIn = null;
	public CustomXYLayoutPolicy(){}
	/**
	 * 调整大小时生成命令
	 */
	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		try{
			ChangeConstraintCommand command = new ChangeConstraintCommand();
			command.setModel(child.getModel());
			command.setConstraint(constraint);
			return command;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 新增节点
	 */
	@Override
	protected Command getCreateCommand(CreateRequest req) {
		try{
			CreateCommand create = new CreateCommand();
			UIAbstractModel model = (UIAbstractModel)req.getNewObject();
			if(this.getHost().getModel() instanceof FormElementModel){
				return null;
			}
			ContainerModel container = (ContainerModel)this.getHost().getModel();
			container.firePropertyChange("move_in", null, null);
			if(currentIn!=null){
				if(container!=currentIn)
					currentIn.firePropertyChange("move_out", null, null);
			}
			currentIn = container;
			if(container instanceof GridModel){
				GridModel grid = (GridModel)container;
				if(model instanceof ColumnModel){
					for(int i = 0;i<grid.getChildren().size();i++){
						if(grid.getChildren().get(i) instanceof ColumnBarModel){
							container = (ContainerModel)grid.getChildren().get(i);
							break;
						}
					}
				}else if(model instanceof ToolBarItemModel||model instanceof FormElementModel){
					for(int i = 0;i<grid.getChildren().size();i++){
						if(grid.getChildren().get(i) instanceof ToolBarModel){
							container = (ContainerModel)grid.getChildren().get(i);
							break;
						}
					}
				}
			}
			if(validate(container,model)){
				create.setContentsModel(container);
				create.setChild(model);
				return create;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 向当前节点移入子节点
	 */
	@Override
	protected Command createAddCommand(EditPart child, Object constraint) {
		try{
			CreateCommand create = new CreateCommand();
			UIAbstractModel model = (UIAbstractModel)child.getModel();
			if(this.getHost().getModel() instanceof FormElementModel){
				return null;
			}
			ContainerModel container = (ContainerModel)this.getHost().getModel();
			container.firePropertyChange("move_in", null, null);
			if(currentIn!=null){
				if(container!=currentIn)
					currentIn.firePropertyChange("move_out", null, null);
			}
			currentIn = container;
			if(validate(container,model)){
				create.setContentsModel(container);
				create.setChild(model);
				return create;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 *功能描述： 校验容器是否能添加加入的子节点
	 *@param container
	 *@param child
	 *@return
	 * @author  tianming 
	 * @2014-11-28
	 */
	public boolean validate(ContainerModel container,UIAbstractModel child){
		try {
			if(container instanceof UIContentsModel){//页面可添加表单、表格、iframe
				if(child instanceof JspIncludeModel||child instanceof PanelModel ||child instanceof FormModel || child instanceof LayoutPanelModel || child instanceof TabModel || child instanceof FormModel || child instanceof TableModel||child instanceof GridModel||child instanceof IframeModel){
					return true;
				}else if(child instanceof UIContentsModel && !"UIContentsModel".equals(child.getModelName())){
					return true;
				}else{
					return false;
				}
			}else if(container instanceof PanelModel){//
				if( child instanceof FormModel){
					return true;
				}else if(child instanceof UIContentsModel && !"UIContentsModel".equals(child.getModelName())){
					return true;
				}else {
					return false;
				}
			}else if(container instanceof ColumnBarModel){//表格列
				if(child instanceof ColumnModel){
					return true;
				}else {
					return false;
				}
			}else if(container instanceof ToolBarModel){//工具栏
				if(child instanceof FormFieldModel||child instanceof ToolBarItemModel){
					return true;
				}else {
					return false;
				}
			}else if(container instanceof TabModel){//标签页面板
				if(child instanceof TabItemModel){
					return true;
				}else {
					return false;
				}
			}else if(container instanceof TabItemModel){//标签页项
				TabItemModel tabItem = (TabItemModel)container;
				if(tabItem.getChildren().size()>0) return false;//标签页内已有内容，禁止再添加
				
				if( child instanceof FormModel||child instanceof GridModel||child instanceof IframeModel){
					return true;
				}else if(child instanceof UIContentsModel && !"UIContentsModel".equals(child.getModelName())){
					return true;
				}else {
					return false;
				}
			}else if(container instanceof CenterModel||container instanceof LeftModel||container instanceof TopModel){//
				if(container instanceof CenterModel&&(child instanceof LeftModel||child instanceof TopModel)){
					List<UIAbstractModel> children = container.getChildren();
					for(int i = 0;i<children.size();i++){
						if(child.getModelName().equals(children.get(i).getModelName())){
							return false;
						}
					}
					return true;
				}
				if(  child instanceof LayoutPanelModel || child instanceof ToolBarModel||child instanceof PanelModel||child instanceof FormModel|| child instanceof TreePanelModel || child instanceof TabModel || child instanceof FormModel || child instanceof TableModel||child instanceof GridModel||child instanceof IframeModel){
					return true;
				}else if(child instanceof UIContentsModel && !"UIContentsModel".equals(child.getModelName())){
					return true;
				}else {
					return false;
				}
			}else if(container instanceof FormModel){//表单
				if( child instanceof FormFieldModel||child instanceof ToolBarModel){
					return true;
				}else {
					return false;
				}
			}else if(container instanceof GridModel){//表格
				if( child instanceof ColumnModel || child instanceof ToolBarItemModel||child instanceof ToolBarModel){
					return true;
				}else {
					return false;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
