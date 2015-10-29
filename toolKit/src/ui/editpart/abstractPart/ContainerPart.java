package ui.editpart.abstractPart;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

import ui.UIAbstractModel;
import ui.UIEditPartWithListener;
import ui.editpart.UIContentsEditPart;
import ui.editpart.directedit.DefCellEditorLocator;
import ui.editpart.directedit.MyDirectEditManager;
import ui.editpart.forms.TableEditPart;
import ui.editpart.grid.GridEditPart;
import ui.editpart.liger.FormEditPart;
import ui.model.UIContentsModel;
import ui.model.abstractModel.ContainerModel;
import ui.model.toolbar.ToolBarModel;
import util.Log;

public abstract class ContainerPart extends UIEditPartWithListener{
	public boolean isEventSource = false;
	public MyDirectEditManager directManager = null;
	public Border orgBorder = null;
	public Border inLine = null;
	public boolean in = false;
	public int offset = 0;
	protected void createEditPolicies() {
		installPolicy();
	}
	/**
	 * 处理直接编辑功能
	 * 
	 * 2014-12-23
	 * @tianming
	 */
	public void performDirectEdit(){
		if(directManager==null){
			directManager = new MyDirectEditManager(this, TextCellEditor.class, new DefCellEditorLocator(this.getFigure()));
		}
		directManager.show();
	}
	@SuppressWarnings("restriction")
	protected void refreshVisuals() {
		try{
			ContainerModel form = (ContainerModel)this.getModel();
			Rectangle rect = form.getRect();
			Rectangle old = figure.getBounds();
			if(rect!=null){
				rect.x = old.x;
				rect.y = old.y;
				if(this.getModel() instanceof ToolBarModel){
					rect.height = old.height;
				}
				//修改当前节点的位置信息
				if(this.getParent() instanceof UIContentsEditPart &&(this instanceof TableEditPart||this instanceof GridEditPart||this instanceof FormEditPart)){
					((GraphicalEditPart)getParent()).setLayoutConstraint(this,this.getFigure(),rect);
				}
				try {
					WorkbenchWindow workbenchWindow = (WorkbenchWindow)PlatformUI.getWorkbench().getActiveWorkbenchWindow();   
					IStatusLineManager lineManager = workbenchWindow.getStatusLineManager();
					lineManager.setMessage("[ type:"+form.getClass().getSimpleName()+", x:"+rect.x +", y="+rect.y+", width="+rect.width+", height="+rect.height+"]");
				} catch (Exception e) {
				}
				
				this.getFigure().setBounds(rect);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
			try{
				if(inLine==null){
					inLine = new CompoundBorder(new AbstractBorder() {
						public Insets getInsets(IFigure figure) {
							return new Insets(0, 0, 0, 0);
						}
						public void paint(IFigure figure, Graphics graphics, Insets insets) {
							Rectangle rect = figure.getBounds().getCopy();
							rect.x = rect.x +1;
							rect.width = rect.width -2;
							rect.height = rect.height -2;
							graphics.setForegroundColor(ColorConstants.red);
							graphics.drawRectangle(rect);
						}
					},new MarginBorder(0));
				}
				if("move_in".equals(evt.getPropertyName())){
					if(!this.in){
						orgBorder = this.getFigure().getBorder();
						this.getFigure().setBorder(inLine);
						this.in = true;
					}
				}else if("move_out".equals(evt.getPropertyName())){
					if(this.in){
						this.getFigure().setBorder(this.orgBorder);
						this.in = false;
					}
				}
				refreshVisuals();
				refreshChildren();
				UIContentsModel contents = getContentModel((UIEditPartWithListener)this.getParent());
				contents.firePropertyChange("relocation", null, null);
			}catch(Exception e){
				Log.write("", e);
			}
	}
	protected List<?> getModelChildren() {
		try{
			return ((ContainerModel)this.getModel()).getChildren();
		}catch(Exception e){
			Log.write("", e);
		}
		return null;
	}
	/**
	 *功能描述： 重新设置子元素的位置坐标
	 * @author  tianming
	 * @2014-12-8
	 */
	public void reLocationChildren() {
		try {
			List<UIEditPartWithListener> childrens = this.getChildren();
			Rectangle tdRect = this.getFigure().getBounds();
			for(int i = 0;i<childrens.size();i++){
				UIEditPartWithListener part = childrens.get(i);
				Rectangle rect = part.getFigure().getBounds();
				int x = this.getLocationX(part);
				rect.x = tdRect.x + x + (i+1)*offset;
				rect.y = tdRect.y ;
				rect.height = tdRect.height ;
				part.getFigure().setBounds(rect);
				if(part instanceof ContainerPart){
					((ContainerPart)part).reLocationChildren();
				}
				part.reSetInner();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	public int getLocationX(UIEditPartWithListener part){
		int x = 0;
		try {
			List children = this.getModelChildren();
			for(int i = 0;i<children.size();i++){
				UIAbstractModel model = (UIAbstractModel)children.get(i);
				if(part.getModel()==model){
					break;
				}
				Rectangle fRect = this.getEditPartByModel(model).getFigure().getBounds();
				x += fRect.width;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return x;
	}
	/**
	 *功能描述： 计算指定节点在y轴方向的位置
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
	 * 根据控制器找对应数据模型在子节点中的索引
	 * @param child
	 * @return
	 * 2014-12-16
	 * @tianming
	 */
	@SuppressWarnings("unchecked")
	public int getModelIndex(UIEditPartWithListener child){
		try{
			List children = this.getModelChildren();
			UIAbstractModel childModel = (UIAbstractModel)child.getModel();
			int count = 0;
			for(int i = 0;i < children.size();i++){
				UIAbstractModel model = (UIAbstractModel)children.get(i);
				if(model == childModel){
					break;
				}
				if(childModel.getClass().getSimpleName().equals(model.getClass().getSimpleName())){
					count ++;
				}
			}
			return count ;
		}catch(Exception e){
			Log.write("", e);
		}
		return 0;
	}
	/**
	 * 在子节点中根据数据模型查找控制器
	 * @param model
	 * @return
	 * 2014-12-16
	 * @tianming
	 */
	@SuppressWarnings("unchecked")
	public UIEditPartWithListener getEditPartByModel(UIAbstractModel model){
		try {
			List children = this.getChildren();
			for(int i = 0;i<children.size();i++){
				UIEditPartWithListener f = (UIEditPartWithListener)children.get(i);
				if(f.getModel()==model){
					return f;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
