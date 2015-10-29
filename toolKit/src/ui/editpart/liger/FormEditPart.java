package ui.editpart.liger;
 
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.UIEditPartWithListener;
import ui.editpart.abstractPart.ContainerPart;
import ui.model.liger.FormFieldModel;
import ui.model.liger.FormModel;
import ui.model.toolbar.ToolBarModel;
import util.ColorValue;
public class FormEditPart extends ContainerPart{
	protected IFigure createFigure() {
		Label figure = null;
		try {
			FormModel model = (FormModel)this.getModel();
			Rectangle rect = new Rectangle( 20, 20,Integer.parseInt(model.getWidth()),Integer.parseInt(model.getHeight()));
			figure = new Label();
			figure.setBorder(new LineBorder(ColorValue.getColor(),1));
			figure.setBounds(rect);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return figure;
	}
	/**
	 *功能描述： 重新设置子元素的位置坐标
	 * @author  tianming
	 * @2014-12-8
	 */
	@SuppressWarnings("unchecked")
	public void reLocationChildren() {
		try {
			Rectangle formRect = this.getFigure().getBounds();
			FormModel form = (FormModel)this.getModel();
			int inputWidth = Integer.parseInt(form.val("inputWidth"));
			int labelWidth = Integer.parseInt(form.val("labelWidth"));
			int space = Integer.parseInt(form.val("space"));
			int columnNum = Integer.parseInt(form.val("columnNum"));//每行列数
			String layout = form.val("layout");//每行列数
			if("0".equals(layout)){
				columnNum = formRect.width/(inputWidth+labelWidth+space);
			}
			List<UIEditPartWithListener> childrens = this.getChildren();
			
			int rowNum = 0;
			int colNum = 0;
			
			int offsetY = 10;
			for(int i = 0;i<childrens.size();i++){
				UIEditPartWithListener part = childrens.get(i);
				if(part.getModel() instanceof FormFieldModel){
					Rectangle rect = part.getFigure().getBounds();
					if(i!=0&&colNum%columnNum==0){
						rowNum ++;
						colNum = 0;
					}
					rect.x = formRect.x +  offsetY + colNum*(inputWidth+labelWidth+space);
					rect.y = formRect.y +	offsetY + rowNum*29;
					part.getFigure().setBounds(rect);
					part.reSetInner();
					FormFieldModel model =(FormFieldModel) part.getModel();
					int colspan = Integer.parseInt(model.val("colspan")==null||"".equals(model.val("colspan"))?"1":model.val("colspan"));//输入项所占的列数
					if(colspan>columnNum){
						colspan = columnNum;
					}
					colNum = colNum+colspan;
				}else if(part.getModel() instanceof ToolBarModel){
					Rectangle rect = part.getFigure().getBounds();
					ToolBarModel toolbar = (ToolBarModel)part.getModel();
					String position = toolbar.val("position");
					if(!"bottom".equals(position)){//顶部
						rect.y = formRect.y ;
					}else{//底部
						rect.y = formRect.y+formRect.height-rect.height;
					}
					rect.x = formRect.x ;
					rect.width = formRect.width;
					
					part.getFigure().setBounds(rect);
				}
				if(part instanceof ContainerPart){
					((ContainerPart)part).reLocationChildren();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
