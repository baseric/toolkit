package ui.editpart.tabpanel;
 
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;
import ui.figure.TabFigure;
import ui.model.tabpanel.TabModel;

public class TabEditPart extends ContainerPart{
	public TabFigure figure = null;
	public String currentAdd = "";
	protected IFigure createFigure() {
		try {
			TabModel model = (TabModel)this.getModel();
			Rectangle rect = new Rectangle( 20, 20,Integer.parseInt(model.getWidth()),Integer.parseInt(model.getHeight()));
			figure = new TabFigure(rect);
			figure.itemContainer.setVisible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return figure;
	}
	
	@Override
	public IFigure getContentPane() {
		return figure.labelContainer;
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
		int x = this.getFigure().getBounds().x-10;
		try {
			List children = this.getChildren();
			for(int i = 0;i<children.size();i++){
				UIEditPartWithListener model = (UIEditPartWithListener)children.get(i);
				if(part == model){
					break;
				}
				if(part instanceof TabItemEditPart){
					Rectangle fRect = model.getFigure().getBounds();
					x += fRect.width;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return x;
	}
	/**
	 * 
	 * 2014-12-13
	 * @tianming
	 */
	@Override
	public void reLocationChildren() {
		try{
			int y = figure.getBounds().y;
			List children = this.getChildren();
			for(int i = 0;i<children.size();i++){
				UIEditPartWithListener childPart = (UIEditPartWithListener)children.get(i);
				int x = this.getLocationX(childPart);
				Rectangle rect = childPart.getFigure().getBounds();
				rect.x = x + 22+i*1;
				rect.y = y + 1;
				childPart.getFigure().setBounds(rect);
				childPart.getFigure().repaint();
				if(childPart instanceof TabItemEditPart){
					TabItemEditPart tabItem = (TabItemEditPart)childPart;
					Label name = tabItem.name;
					Rectangle nameRect = rect.getCopy();
					nameRect.x = nameRect.x +8;
					name.setBounds(nameRect);
					if(childPart instanceof ContainerPart){
						((ContainerPart)childPart).reLocationChildren();
					}
				}
			}
			Rectangle bounds = figure.getBounds();
			//重新设置标题
			Rectangle name = figure.labelContainer.getBounds();
			name.width = bounds.width+7;
			name.y = bounds.y-1;
			name.x = bounds.x-1;
			figure.labelContainer.setBounds(name);//标签页背景图片
			figure.itemContainer.setBounds(new Rectangle(name.x+1,name.y +29,bounds.width - 2,bounds.height - 29));//容器
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
