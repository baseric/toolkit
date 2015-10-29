package business.editor;

import images.IconFactory;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.stackview.CommandStackInspectorPage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;

import toolkit.Activator;
import util.GenerateCode;
import util.Log;
import util.PartFactory;
import util.PropertyReader;
import business.dnd.DiagramTemplateTransferDropTargetListener;
import business.editor.menuaction.CreateCodeAction;
import business.editor.menuaction.OpenSourceAction;
import business.model.ContentsModel;
import business.model.LineConnectionModel;

@SuppressWarnings("deprecation")
public class LogicEditor extends GraphicalEditorWithFlyoutPalette {
	private GraphicalViewer view = null;
	private ContentsModel parent = null;
	private List<Map<String,String>> toolsList = null;
	private List<String> actionList = new ArrayList<String>();
	private boolean isDirty = false;
	public LogicEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}
	public ContentsModel getContentsModel(){
		return parent;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected PaletteRoot getPaletteRoot() {
		PaletteRoot root = new PaletteRoot();
		PaletteGroup toolGroup = new PaletteGroup("工具");
		ToolEntry tool1 = new SelectionToolEntry();
		toolGroup.add(tool1);
		root.setDefaultEntry(tool1);
		
		tool1 = new MarqueeToolEntry();
		toolGroup.add(tool1);
		
		tool1 = new ConnectionCreationToolEntry("连接", "连接", new SimpleFactory(LineConnectionModel.class), IconFactory.getImageDescriptor("business/small/connection.gif"), null);
		toolGroup.add(tool1);
		
		root.add(toolGroup);
		SAXReader saxReader = new SAXReader();
		try {
			toolsList = new ArrayList<Map<String,String>>();
			BufferedInputStream in =(BufferedInputStream) Activator.getInputStreamByName("/logic.xml");
			Document document = saxReader.read(in);
			Element rootElement = document.getRootElement();
			List<Element> drawers = rootElement.elements();
			for(int i = 0;i<drawers.size();i++){
				Element drawerEle = drawers.get(i);
				PaletteDrawer drawer = new PaletteDrawer(drawerEle.attributeValue("text"));
				if(drawerEle.attribute("close")!=null&&"Y".equals(drawerEle.attributeValue("close"))){
					drawer.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
				}
				List<Element> tools = drawerEle.elements();
				for(int j = 0;j<tools.size();j++){
					Element tool = tools.get(j);
					if("tool".equalsIgnoreCase(tool.getName())){
						String text = tool.attributeValue("text");
						ImageDescriptor image = IconFactory.getImageDescriptor("business/small/"+tool.attributeValue("icon"));
						CreationToolEntry creationEntry = new CreationToolEntry(text,text,
																				new SimpleFactory(Class.forName(tool.attributeValue("class"))),
																				image,null);
						drawer.add(creationEntry);
						Map<String,String> map = new HashMap<String,String>();
						map.put("text", text);
						map.put("class", tool.attributeValue("class"));
						map.put("icon", tool.attributeValue("icon"));
						toolsList.add(map);
					}else if("Separator".equalsIgnoreCase(tool.getName())){
						PaletteSeparator split = new PaletteSeparator();
						drawer.add(split);
					}
				}
				root.add(drawer);
			}
			in.close();
		} catch (DocumentException e) {
		} catch (Exception e){
			e.printStackTrace();
		}
		return root;
	}
	public void commandStackChanged(EventObject event) {
		if (isDirty()) {
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
		super.commandStackChanged(event);
	}
	public boolean isDirty(){
		return isDirty||this.getCommandStack().isDirty();
	}
	public void firDirty(){
		this.isDirty = true;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		view.setContents(parent);
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		view = getGraphicalViewer();
		view.setEditPartFactory(new PartFactory());
		ContextMenuProvider menuProvider = new ActionLogicEditorContextMenuProvider(view, this.getActionRegistry(),actionList);
		view.setContextMenu(menuProvider);
		view.addDropTargetListener(new DiagramTemplateTransferDropTargetListener(view,((IFileEditorInput) getEditorInput()).getFile()));
	}
	@SuppressWarnings("unchecked")
	protected void createActions() {
		super.createActions();
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		AlignmentAction left = new AlignmentAction((IWorkbenchPart)this, PositionConstants.LEFT);
		left.setText("左对齐");
		AlignmentAction center = new AlignmentAction((IWorkbenchPart)this, PositionConstants.CENTER);
		center.setText("居中对齐");
		AlignmentAction right = new AlignmentAction((IWorkbenchPart)this, PositionConstants.RIGHT);
		right.setText("右对齐");
		AlignmentAction top = new AlignmentAction((IWorkbenchPart)this, PositionConstants.TOP);
		top.setText("向上对齐");
		AlignmentAction bottom = new AlignmentAction((IWorkbenchPart)this, PositionConstants.BOTTOM);
		bottom.setText("向下对齐");
		IAction[] actions = new IAction[]{
				left,center,right,top,bottom,
				new CreateCodeAction(this,file,parent,"logic.ftl"),
				new OpenSourceAction(this,file)
		};
		for(int i = 0;i<actions.length;i++){
			//生存代码菜单
		    IAction action=actions[i];
		    getActionRegistry().registerAction(action);
		    getSelectionActions().add(action.getId());
		    actionList.add(action.getId());
		}
	}
	/**
	 * 保存文件
	 */
	public void doSave(IProgressMonitor monitor) {
		try {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			try{
				getCommandStack().markSaveLocation();
			}catch(Exception e){
				//Log.write("",e);
			}
			this.isDirty = false;
			firePropertyChange(IEditorPart.PROP_DIRTY);
			GenerateCode code = new GenerateCode();
			
			code.saveToFile(file.getLocation().toFile(), parent);
			file.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			PropertyReader reader = new PropertyReader();
			if(!"N".equals(reader.getPropertyValue("isComplierOnSave"))){
				code.createAction(parent,file.getLocation().toFile(),file.getProject(),"logic.ftl");
			}
		} catch (Exception ioe) {
			Log.write("", ioe);
		}
	}
 
	/**
	 *打开文件 
	 */
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		try {
			IFile file = ((IFileEditorInput) input).getFile();
			setPartName(file.getName());
			GenerateCode code = new GenerateCode();
			this.parent = (ContentsModel)code.readFile(file.getLocation().toFile());
			if(this.parent==null){
				this.parent = new ContentsModel();
			}
			this.parent.setFile(file);
		}catch (Exception e) { 
			handleLoadException(e); 
		}
	}
	private void handleLoadException(Exception e) {
		this.parent = new ContentsModel();
	}
	@SuppressWarnings({ "unchecked"})
	public Object getAdapter(Class type) {
		if (type == CommandStackInspectorPage.class)
			return new CommandStackInspectorPage(getCommandStack());
		if (type == ActionRegistry.class)
			return getActionRegistry();
		return super.getAdapter(type);
	}
	 
}
