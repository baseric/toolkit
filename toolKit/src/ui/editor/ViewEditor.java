package ui.editor;

import images.IconFactory;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.gef.ui.stackview.CommandStackInspectorPage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import ui.editor.createFactory.BeanFactory;
import ui.editor.menuaction.CreateCodeAction;
import ui.editor.menuaction.InitFormElementAction;
import ui.editor.menuaction.InitGridColumnAction;
import ui.editor.menuaction.OpenUrlAction;
import ui.editpart.UIContentsEditPart;
import ui.model.UIContentsModel;
import ui.outline.TreePartFactory;
import ui.util.UIGenerateCode;
import util.BaseInfoUtil;
import util.PartFactory;
import business.editor.menuaction.GetActionPathAction;
import business.editor.menuaction.OpenSourceAction;

@SuppressWarnings("deprecation")
public class ViewEditor extends GraphicalEditorWithFlyoutPalette {
	private GraphicalViewer view = null;
	private UIContentsModel parent = null;
	private List<Map<String,String>> toolsList = null;//大纲视图用到的
	private List<String> actionList = new ArrayList<String>();
	public boolean isDirty = false;
	public ViewEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}
	public UIContentsModel getContentsModel(){
		return parent;
	}
	@Override
	protected PaletteRoot getPaletteRoot() {
		PaletteRoot root = new PaletteRoot();
		PaletteGroup toolGroup = new PaletteGroup("工具");
		ToolEntry tool1 = new SelectionToolEntry();
		toolGroup.add(tool1);
		root.setDefaultEntry(tool1);
		tool1 = new MarqueeToolEntry();
		toolGroup.add(tool1);
		root.add(toolGroup);
		try {
			toolsList = new ArrayList<Map<String,String>>();
			BaseInfoUtil util = new BaseInfoUtil();
			List<Map<String,String>> list = util.readXML("dev_comp_menu");
			HashMap<String,PaletteDrawer> tempMap = new HashMap<String,PaletteDrawer>();
			for(int i = 0;i<list.size();i++){
				Map<String,String> item = list.get(i);
				String disp_yn = item.get("disp_yn");
				if(!"N".equals(disp_yn)){
					if("drawer".equals(item.get("com_type"))){
							PaletteDrawer drawer = new PaletteDrawer(item.get("comp_desc"));
							tempMap.put(item.get("comp_name"),drawer);
							root.add(drawer);
							if(item.get("closed")!=null&&"Y".equals(item.get("closed"))){
								drawer.setInitialState(PaletteDrawer.INITIAL_STATE_CLOSED);
							}
						
					}else if("tool".equals(item.get("com_type"))){
						PaletteDrawer drawer = tempMap.get(item.get("up_comp_name"));
						if(drawer!=null){
							String text = item.get("comp_desc");
							String className = item.get("class");
							String comp_name = item.get("comp_name");
							String icon = item.get("icon");
							CreationToolEntry creationEntry = new CreationToolEntry(text,text,
									new BeanFactory(Class.forName(className),comp_name),
									IconFactory.getImageDescriptor("ui/"+icon),null);
							drawer.add(creationEntry);
							Map<String,String> map = new HashMap<String,String>();
							map.put("text", text);
							map.put("class", className);
							map.put("icon", icon);
							toolsList.add(map);
						}
					}else if("separator".equals(item.get("com_type"))){
						PaletteDrawer drawer = tempMap.get(item.get("up_comp_name"));
						if(drawer!=null){
							PaletteSeparator split = new PaletteSeparator();
							drawer.add(split);
						}
					}
				}
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}finally{
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


	@SuppressWarnings("unchecked")
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		view.setContents(parent);
		try {
			Map map = view.getEditPartRegistry();
			UIContentsEditPart contents = (UIContentsEditPart)map.get(parent);
			contents.reLocationChildren();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		view = getGraphicalViewer();
		view.setEditPartFactory(new PartFactory());
		ContextMenuProvider menuProvider = new ViewEditorContextMenuProvider(view, this.getActionRegistry(),actionList);
		view.setContextMenu(menuProvider);
		/*//创建键盘处理的句柄   
		KeyHandler keyHandler = new KeyHandler();  
		//按delete时删除action,发起DELETE类型的事件，然后捕获   
		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), getActionRegistry().getAction(GEFActionConstants.DELETE));  
		//按F2时编辑   
		keyHandler.put(KeyStroke.getPressed(SWT.F2, 0), getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
		//按F2时编辑   
		keyHandler.put(KeyStroke.getPressed(SWT.F2, 0), getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));  
		//按Ctrl+Z撤销   
		keyHandler.put(KeyStroke.getReleased(' ',122,SWT.CTRL), getActionRegistry().getAction(GEFActionConstants.UNDO));  
		//按Ctrl+Y反撤销   
		keyHandler.put(KeyStroke.getReleased(' ',121,SWT.CTRL), getActionRegistry().getAction(GEFActionConstants.REDO));  
		//按Ctrl+A全选   
		keyHandler.put(KeyStroke.getReleased(' ',97,SWT.CTRL), getActionRegistry().getAction(GEFActionConstants.SELECT_ALL)); 
		//按Ctrl+A全选   
		keyHandler.put(KeyStroke.getReleased('',100,SWT.CTRL), getActionRegistry().getAction(GEFActionConstants.DELETE));  
		view.setKeyHandler(keyHandler);  
*/
	}
	@SuppressWarnings("unchecked")
	protected void createActions() {
		super.createActions();
		try {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			IAction[] actions = new IAction[]{
					new OpenUrlAction(this,file),//Grid打开对应URL的action文件
					//new GlobalParamAction(this,file,parent),//设置页面全局变量
					new InitFormElementAction(this,file),//根据数据字典初始化表单项
					new InitGridColumnAction(this,file),//根据数据字典初始化列
					//new ConfigModelAttrAction(this,file),//配置属性
					new CreateCodeAction(this,file,parent),//生成代码
					new OpenSourceAction(this,file),//打开生成的源码
					new GetActionPathAction(this,file)
			};
			for(int i = 0;i<actions.length;i++){
				//生存代码菜单
			    IAction action=actions[i];
			    getActionRegistry().registerAction(action);
			    getSelectionActions().add(action.getId());
			    actionList.add(action.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存文件
	 */
	public void doSave(IProgressMonitor monitor) {
			getCommandStack().markSaveLocation();
			isDirty = false;
			firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	public void fireDirty(){
		this.isDirty = true ;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        updateActions(getSelectionActions());
    }
	/**
	 *打开文件 
	 */
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		try {
			IFile file = ((IFileEditorInput) input).getFile();
			setPartName(file.getName());
			UIGenerateCode code = new UIGenerateCode();
			code.setProject(file.getProject());
			this.parent = (UIContentsModel)code.readFile(file.getLocation().toFile());
			if(this.parent==null){
				this.parent = new UIContentsModel("UIContentsModel");
			}
			this.parent.setFile(file);
		}catch (Exception e) { 
			handleLoadException(e); 
		}
	}
	private void handleLoadException(Exception e) {
		this.parent = new UIContentsModel("ContentsModel");
	}
	OutlinePage _outline = null;
	@SuppressWarnings({ "unchecked"})
	public Object getAdapter(Class type) {
		if (type == CommandStackInspectorPage.class)
			return new CommandStackInspectorPage(getCommandStack());
		if (type == ActionRegistry.class)
			return getActionRegistry();
		if (type == IContentOutlinePage.class&&_outline==null) {
			_outline = new OutlinePage();
			return new OutlinePage();
		}
		return super.getAdapter(type);
	}
	class OutlinePage extends ContentOutlinePage {
		private Control outline;

		public OutlinePage() {
			super(new TreeViewer());
		}

		public void init(IPageSite pageSite) {
			super.init(pageSite);
			ActionRegistry registry = getActionRegistry();
			IActionBars bars = pageSite.getActionBars();
			String id = ActionFactory.UNDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.REDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.DELETE.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			bars.updateActionBars();
		}

		public void createControl(Composite parent) {
			outline = getViewer().createControl(parent);
			getSelectionSynchronizer().addViewer(getViewer());
			getViewer().setEditDomain(getEditDomain());
			TreePartFactory treePart = new TreePartFactory();
			treePart.toolsList = toolsList;
			getViewer().setEditPartFactory(treePart);
			getViewer().setContents(getContentsModel());
			ContextMenuProvider menuProvider = new ViewEditorContextMenuProvider(getViewer(), getActionRegistry(),actionList);
			getViewer().setContextMenu(menuProvider);
		}

		public Control getControl() {
			return outline;
		}

		public void dispose() {
			getSelectionSynchronizer().removeViewer(getViewer());
			super.dispose();
		}
	}
}
