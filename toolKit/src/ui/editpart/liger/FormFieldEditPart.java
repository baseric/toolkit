package ui.editpart.liger;

import images.IconFactory;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.ui.IViewPart;

import toolkit.Activator;
import ui.UIAbstractModel;
import ui.editpart.abstractPart.ContainerPart;
import ui.figure.LabelBorder;
import ui.model.liger.FormFieldModel;
import ui.model.liger.FormModel;
import util.GloableParam;
import util.Log;
import views.PropertyView;
 

public  class FormFieldEditPart extends ContainerPart{
	public Label label = null;
	public Label value = null;//输入框
	public Label icon = null;//图标
	private int elementWidth = GloableParam.elementWidth;//表单项的长度
	private Label required = null;//必输项 * 号
	public void performRequest(Request req) {
		super.performRequest(req);
		if(req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)){
			performDirectEdit();
			return;
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String type = evt.getPropertyName();
		String attrName = String.valueOf(evt.getOldValue());
		String value = String.valueOf(evt.getNewValue());
		if("refresh".equals(type)&&"type".equals(attrName)){
			FormFieldModel model = (FormFieldModel)this.getModel();
			List<Map<String,String>> oldAttribute = model.getAttributes();
			model.setAttributes(null);
			model.setModelInfo(null);
			model.setModelName(value);
			List<Map<String,String>> newAttribute = model.getAttributes();
			model.getModelInfo();
			for(int i = 0;i<oldAttribute.size();i++){
				Map<String,String> old = oldAttribute.get(i);
				for(int j = 0;j<newAttribute.size();j++){
					if(old.get("attr_name").equals(newAttribute.get(j).get("attr_name"))){
						newAttribute.get(j).put("value",old.get("value"));
						break;
					}
				}
			}
			IViewPart viewPart = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("views.PropertyView");    
			try {
				if(viewPart instanceof PropertyView){
					PropertyView view = (PropertyView)viewPart;
					view.setDataByModel(model);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.propertyChange(evt);
	}

	@Override
	public void reSetInner() {
		try {
			FormFieldModel field = (FormFieldModel)this.getModel();
			int inputWidth =  200;
			int labelWidth = 100;
			int space = 20;
			if(this.getParent().getModel() instanceof FormModel){
				FormModel form = (FormModel)this.getParent().getModel();
				inputWidth = Integer.parseInt(form.val("inputWidth"));//输入框的宽度
				labelWidth = Integer.parseInt(form.val("labelWidth"));//标签的宽度
				space = Integer.parseInt(form.val("space"));//表单项间距
			}
			int height = Integer.parseInt(field.getModelInfo().get("height"));//输入项的高度
			
			int colspan = Integer.parseInt(field.val("colspan")==null||"".equals(field.val("colspan"))?"1":field.val("colspan"));//输入项所占的列数
		    Rectangle parentRect = this.getFigure().getBounds();//整个表单项
		    parentRect.height = height;//表单项的高度
		    parentRect.width = inputWidth + labelWidth;//表单项的宽度 = 输入项的宽度 + 标签的宽度
			if(this.value!=null){
				//调整文本标签的坐标
				Rectangle labelRect = this.label.getBounds();
				labelRect.x = parentRect.x +5;
				labelRect.y = parentRect.y + 2;
				labelRect.width = labelWidth;
				labelRect.height = height;
				label.setBounds(labelRect);
				label.setText(field.val("label")+":");
				label.setLabelAlignment(PositionConstants.LEFT);
				
				//调整输入项的坐标
				Rectangle valueRect = this.value.getBounds();
				valueRect.x = parentRect.x + labelRect.width + 5 ;
				valueRect.y = parentRect.y +2;
				
				if(colspan>1){
					inputWidth = inputWidth +(colspan-1)*(labelWidth+inputWidth+space);
					parentRect.width = inputWidth + labelWidth;//表单项的宽度 = 输入项的宽度 + 标签的宽度
				}
				
				valueRect.width = inputWidth - 10;
				valueRect.height = height-3;
				this.value.setBounds(valueRect);
				if(!"Label".equals(field.getModelName())){
					this.value.setBorder(new LineBorder(ColorConstants.gray));
				}else{
					this.value.setBorder(new CompoundBorder(new LabelBorder(),new MarginBorder(0)));
				}
				if("true".equals(field.val("disabled"))){
					this.value.setBackgroundColor(ColorConstants.lightGray);
				}else{
					this.value.setBackgroundColor(ColorConstants.white);
				}
				this.value.setOpaque(true);

				if(icon==null){
					if(field.getModelInfo().get("icon")!=null&&field.getModelInfo().get("icon").length()>0){
						icon = new Label();
						this.getFigure().add(icon);
					}
				}
				if(icon!=null){
					if(field.getModelInfo().get("icon")!=null&&field.getModelInfo().get("icon").length()>0){
						icon.setIcon(IconFactory.getImageDescriptor("ui/"+field.getModelInfo().get("icon")).createImage());
					}else {
						icon.setIcon(null);
					}
					//调整图标的坐标
					Rectangle iconRect = this.icon.getBounds();
					iconRect.x = valueRect.x + valueRect.width - 29;
					iconRect.y = parentRect.y +1;
					iconRect.height = parentRect.height -1;
					iconRect.width = 34;
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
	public IFigure getFormElement(IFigure element,Label icon){
		try{
			FormFieldModel field = (FormFieldModel)this.getModel();
			int width = 200;
			
			if(this.getParent().getModel() instanceof FormModel){
				FormModel form = (FormModel)this.getParent().getModel();
				width = Integer.parseInt(form.val("inputWidth"));
			}
			
			this.label = new Label();
			this.value = (Label)element;
			this.icon = icon;
			
			Figure input = new Figure();
			input.setLayoutManager(new XYLayout());
			input.add(label);
			//设置输入项
			if(!"Label".equals(field.getModelName())){
				element.setBorder(new LineBorder(ColorConstants.gray));
			}else{
				element.setBorder(new CompoundBorder(new LabelBorder(),new MarginBorder(0)));
			}
			//设置图标
			input.add(element);
			if(icon!=null){
				input.add(icon);
			}
			
			required = new Label();
			required.setForegroundColor(ColorConstants.red);
			required.setBounds(new Rectangle(elementWidth+1,11,8,8));
			input.add(required);
			
			input.setBounds(new Rectangle(0,0,width,Integer.parseInt(field.getModelInfo().get("height"))));
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
		FormFieldModel model = (FormFieldModel)this.getModel();
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
			UIAbstractModel model = (UIAbstractModel)this.getModel();
			value = new Label();
			value.setLabelAlignment(PositionConstants.LEFT);
			Label icon = null;
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
