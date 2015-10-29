package ui.editpart;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;

import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;
import ui.model.UIContentsModel;
import ui.policy.CustomXYLayoutPolicy;
import ui.policy.EditParentNodePolicy;
import ui.policy.SelectPartPolicy;

public class UIContentsEditPart extends ContainerPart{
	private Layer figure = null;
	protected IFigure createFigure() {
		figure = new Layer();
		figure.setLayoutManager(new XYLayout());
		return figure;
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CustomXYLayoutPolicy());
		installEditPolicy("1", new EditParentNodePolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new SelectPartPolicy());
	}
	protected List<?> getModelChildren() {
		return ((UIContentsModel)this.getModel()).getChildren();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		try {
			if(evt.getPropertyName().equals("children")||evt.getPropertyName().equals("relocationChild")){
				this.refreshChildren();
			}
			reLocationChildren();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	/**
	 *功能描述： 计算指定节点在x轴方向的位置
	 *@param model
	 *@return
	 * @author  tianming 
	 * @2014-12-6
	 */
	@SuppressWarnings("unchecked")
	public int getLocationY(UIEditPartWithListener part){
		int y = 0;
		try {
			List children = this.getChildren();
			for(int i = 0;i<children.size();i++){
				UIEditPartWithListener model = (UIEditPartWithListener)children.get(i);
				if(part == model){
					break;
				}
				y += model.getFigure().getBounds().height;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return y;
	}
	/**
	 * 
	 * 
	 * 2014-12-13
	 * @tianming
	 */
	@SuppressWarnings("unchecked")
	public void reLocationChildren() {
		try {
			List<UIEditPartWithListener> children = this.getChildren();
			for(int i = 0;i<children.size();i++){
					UIEditPartWithListener childPart = (UIEditPartWithListener)children.get(i);
					Rectangle rect = childPart.getFigure().getBounds();
					rect.y = getLocationY(childPart) + 20 +i*2;
					this.setLayoutConstraint(childPart,childPart.getFigure(),rect);
					childPart.reSetInner();
					childPart.getFigure().repaint();
					if(childPart instanceof ContainerPart){
						((ContainerPart)childPart).reLocationChildren();
					}
			}
			figure.repaint();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
}
