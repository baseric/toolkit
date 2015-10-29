package util;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import ui.UIAbstractModel;
import ui.editpart.tabpanel.TabEditPart;
import ui.model.UIContentsModel;
import business.AbstractModel;
import business.EditPartWithListener;
import business.editpart.ContentsEditPart;
import business.editpart.LineConnectionEditPart;
import business.model.ContentsModel;

public class PartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart arg0, Object arg1) {
		// TODO Auto-generated method stub
		Class<?> clazz = arg1.getClass();
		String className = clazz.getName();
		try {
			className = className.replace(".model.", ".editpart.");
			Class<?> clazz2 = Class.forName(className.replace("Model", "EditPart"));
			EditPart part = (EditPart)clazz2.newInstance();
			try {
				String classStr = clazz.getName();
				if(classStr.startsWith("business")&&!(part instanceof ContentsEditPart||part instanceof LineConnectionEditPart)){
					EditPartWithListener parent = (EditPartWithListener)arg0;
					ContentsModel contents = parent.getContentModel(parent);
					AbstractModel model = (AbstractModel)arg1;
					model.setFile(contents.getFile());
					model.parentModel = contents;
					if("id_10000".equals(model.getId())){
						model.setId("id_"+(10000+(++contents.count)));
					}
				}else if(classStr.startsWith("ui")&&!(part instanceof ContentsEditPart||part instanceof LineConnectionEditPart)){
					ui.UIEditPartWithListener parent = (ui.UIEditPartWithListener)arg0;
					if(parent!=null){
						UIContentsModel contents = parent.getContentModel(parent);
						UIAbstractModel model = (ui.UIAbstractModel)arg1;
						model.setFile(contents.getFile());
					}
				}else if(arg0 instanceof TabEditPart){
					TabEditPart tab = (TabEditPart)arg0;
					tab.currentAdd = clazz.getSimpleName();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			part.setModel(arg1);
			return part;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
