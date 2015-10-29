package ui.editpart.grid;
 
import images.IconFactory;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import business.AbstractModel;

import ui.UIAbstractModel;
import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;
import ui.editpart.toolbar.ToolBarEditPart;
import util.ColorValue;

public class GridEditPart extends ContainerPart{
	private Figure figure = null;
	private ImageFigure head = null;
	private Label label = null;
	protected IFigure createFigure() {
		UIAbstractModel model = (UIAbstractModel)this.getModel();
		Rectangle rect = new Rectangle( 20, 20,Integer.parseInt(model.getWidth()),Integer.parseInt(model.getHeight()));
		figure = new Figure();
		figure.setBounds(rect);
		XYLayout layout = new XYLayout();
		figure.setBorder(new LineBorder(ColorValue.getColor()));
		figure.setLayoutManager(layout);
		
		Rectangle titleRect = new Rectangle(20,30,0,0);
		label = new Label();
		label.setTextAlignment(PositionConstants.LEFT);
		label.setLabelAlignment(PositionConstants.LEFT);
		head = new ImageFigure();
		head.setBounds(titleRect);
		head.setImage(IconFactory.getImageDescriptor("ui/grid/gridtitle.png").createImage());
		head.add(label);
		figure.add(head);
		return figure;
	}
	/**
	 *功能描述： 重新设置子元素的位置坐标
	 * @author  tianming
	 * @2014-12-8
	 */
	@SuppressWarnings("unchecked")
	public void reLocationChildren(){
		try {
			UIAbstractModel grid = (UIAbstractModel)this.getModel();
			ToolBarEditPart toolbar = null;
			ColumnBarEditPart columnsbar = null;
			BottomBarEditPart bottom = null;
			
			int y = figure.getBounds().y;
			int base_x = figure.getBounds().x;
			List children = this.getChildren();
			for(int i = 0;i<children.size();i++){
				UIEditPartWithListener childPart = (UIEditPartWithListener)children.get(i);
				if(childPart instanceof ToolBarEditPart){
					toolbar = (ToolBarEditPart)childPart;
				}if(childPart instanceof ColumnBarEditPart){
					columnsbar = (ColumnBarEditPart)childPart;
				}if(childPart instanceof BottomBarEditPart){
					bottom = (BottomBarEditPart)childPart;
				}
			}
			
			int offset = 0;
			if(head!=null){//标题栏
				Rectangle rect = head.getBounds();
				rect.y = y;
				rect.height = 28;
				rect.x = base_x;
				rect.width = figure.getBounds().width;
				head.setBounds(rect);
				label.setBounds(rect);
				String title = ((UIAbstractModel)this.getModel()).val("title");
				if(title!=null&&title.length()>0){
					head.setVisible(true);
					label.setText("    "+title);
					offset +=rect.height;
				}else{
					head.setVisible(false);
				}
			}
			if(toolbar!=null){//顶部工具栏
				Rectangle rect = toolbar.getFigure().getBounds();
				rect.y = y + offset ;
				rect.height = 30;
				offset +=rect.height;
				rect.x = base_x;
				rect.width = figure.getBounds().width;
				toolbar.getFigure().setBounds(rect);
				toolbar.getFigure().repaint();
				toolbar.reSetInner();
				if(toolbar instanceof ContainerPart){
					((ContainerPart)toolbar).reLocationChildren();
				}
			}
			if(columnsbar!=null){//列表项
				Rectangle rect = columnsbar.getFigure().getBounds();
				rect.y = y+offset ;
				rect.height = 25;
				rect.x = base_x;
				rect.width = figure.getBounds().width;
				columnsbar.getFigure().setBounds(rect);
				columnsbar.getFigure().repaint();
				columnsbar.reSetInner();
				if(columnsbar instanceof ContainerPart){
					((ContainerPart)columnsbar).reLocationChildren();
				}
			}
			if(bottom!=null){//底部工具栏
				Rectangle rect = bottom.getFigure().getBounds();
				rect.y = figure.getBounds().height+y-28;
				rect.height = 28;
				rect.x = base_x;
				rect.width = figure.getBounds().width;
				bottom.getFigure().setBounds(rect);
				bottom.getFigure().repaint();
				bottom.reSetInner();
				
				if("true".equals(grid.val("pagination"))){
					bottom.getFigure().setVisible(true);
				}else{
					bottom.getFigure().setVisible(false);
				}
				if(bottom instanceof ContainerPart){
					((ContainerPart)bottom).reLocationChildren();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
