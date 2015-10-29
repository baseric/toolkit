package ui.editpart.forms;

import images.IconFactory;

import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import ui.UIAbstractModel;
import ui.editpart.abstractPart.ContainerPart;
import ui.editpart.forms.element.LabelEditPart;
import ui.model.forms.FormElementModel;
import util.GloableParam;
import util.Log;
 

public  class FormElementEditPart extends ContainerPart{
//	public Label label = null;
	public Label value = null;//输入框
	public Label icon = null;//图标
	private int elementWidth = GloableParam.elementWidth;//表单项的长度
	private Label required = null;//必输项 * 号
	public void performRequest(Request req) {
		super.performRequest(req);
		if(this instanceof LabelEditPart){
			if(req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)){
				performDirectEdit();
				return;
			}
		}
	}
	@Override
	public void reSetInner() {
		try {	
			    Rectangle parentRect = this.getFigure().getBounds();
				if(this.value!=null){
					//调整输入项的坐标
					Rectangle valueRect = this.value.getBounds();
					valueRect.x = parentRect.x + 5 ;
					valueRect.y = parentRect.y +3;
					valueRect.width = parentRect.width - 10;
					valueRect.height = parentRect.height - 4;
					this.value.setBounds(valueRect);
					if(icon!=null){
						//调整图标的坐标
						Rectangle iconRect = this.icon.getBounds();
						iconRect.x =valueRect.x + valueRect.width - 21;
						iconRect.y =parentRect.y +3;
						iconRect.height = parentRect.height - 4;
						this.icon.setBounds(iconRect);
					}
					Rectangle reqRect = required.getBounds();
					reqRect.x = valueRect.x + valueRect.width;
					reqRect.y = valueRect.y +8;
					this.required.setBounds(reqRect);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void createEditPolicies() {
		installPolicy();
	}
	public IFigure getFormElement(IFigure element,Label icon){
		try{
			this.value = (Label)element;
			this.icon = icon;
			ContainerPart td = (ContainerPart)this.getParent();
			Rectangle rect = td.getFigure().getBounds();
			Figure input = new Figure();
			input.setLayoutManager(new XYLayout());
			UIAbstractModel model = (UIAbstractModel)this.getModel();
			elementWidth = Integer.parseInt(model.getWidth());
			//设置输入项
			element.setBorder(new LineBorder(ColorConstants.gray));
			element.setBounds(new Rectangle(rect.x,rect.y,elementWidth-9,rect.height-6));
			//设置图标
			if(icon!=null){
				icon.setBounds(new Rectangle(elementWidth-22,2,20,rect.height-6));
				input.add(icon);
			}
			input.add(element);
			
			required = new Label();
			required.setForegroundColor(ColorConstants.red);
			required.setBounds(new Rectangle(elementWidth+1,11,8,8));
			input.add(required);
			
			input.setBounds(new Rectangle(rect.x,rect.y,elementWidth,rect.height));
			input.setBorder(null);
			return input;
		}catch(Exception e){
			Log.write("出现异常", e);
		}
		return null;
	}
	protected void refreshVisuals() {
		try{
			this.setTooltips();
		}catch(Exception e){
			Log.write("", e);
		}
		super.refreshVisuals();
	}
	public void setTooltips(){
		IFigure figure = this.getFigure();
		FormElementModel model = (FormElementModel)this.getModel();
		Label tips = new Label();
		String text = "  id："+model.val("id")+" \n"+
		 "  name："+model.val("name")+" \n"+
		 "  校验： "+model.val("validate")+"\n"+
		 "  事件处理：\n";
		if(!"".equals(model.val("onclick"))){
			text+="    onclick  =  "+model.val("onclick")+"\n";
		}
		if(!"".equals(model.val("onfocus"))){
			text+="    onfocus   =  "+model.val("onfocus")+"\n";
		}
		if(!"".equals(model.val("onchange"))){
			text+="    onchange  =  "+model.val("onchange")+"\n";
		}
		if(!"".equals(model.val("onblur"))){
			text+="    onblur    =  "+model.val("onblur")+"\n";
		}
		tips.setText(text);
		figure.setToolTip(tips);
		if(this.required!=null){
			if(model.val("validate")!=null&&model.val("validate").indexOf("required")>-1){
				this.required.setText("*");
			}else{
				this.required.setText("");
			}
		}
		if(value!=null)
			value.setText(" "+model.val("validate"));
	}
	

	@Override
	protected IFigure createFigure() {
		try{
			value = new Label();
			value.setLabelAlignment(PositionConstants.LEFT);
			Label icon = null;
			UIAbstractModel model = (UIAbstractModel)this.getModel();
			Map<String,String> modelInfo = model.getModelInfo();
			if(modelInfo.get("icon")!=null&&modelInfo.get("icon").length()>0){
				icon = new Label();
				icon.setIcon(IconFactory.getImageDescriptor("ui/"+modelInfo.get("icon")).createImage());
			}
			IFigure input = this.getFormElement(value,icon);
			return input;
		}catch(Exception e){
			Log.write("出现异常", e);
		}
		return null;
	}
}
