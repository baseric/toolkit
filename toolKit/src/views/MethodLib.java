package views;

import images.IconFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

import business.codesource.AnalysisJavaFile;

public class MethodLib extends ViewPart {

	public static final String ID = "views.MethodLib"; //
	private Tree tree = null;
	private String rootPath = "E:\\runtime-EclipseApplication\\financemanage_yq\\src\\base\\util\\";
	/**
	 * Create contents of the view part
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());

		final TreeViewer treeViewer = new TreeViewer(container, SWT.BORDER);
		tree = treeViewer.getTree();
		final GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_tree.heightHint = 379;
		tree.setLayoutData(gd_tree);
		try {
			 treeViewer.setLabelProvider(new ILabelProvider(){
				@SuppressWarnings("unchecked")
				public Image getImage(Object arg0) {
					if(arg0 instanceof File){
						File file = (File)arg0;
						if(file.isDirectory()){
							return IconFactory.getImageDescriptor("business/folder.gif").createImage();
						}else if(file.getName().endsWith(".java")){
							return IconFactory.getImageDescriptor("business/class.gif").createImage();
						}
					}else if(arg0 instanceof HashMap){
						return IconFactory.getImageDescriptor("business/80.gif").createImage();
					}
					return IconFactory.getImageDescriptor("business/node.gif").createImage();
				}
				@SuppressWarnings("unchecked")
				public String getText(Object arg0) {
					if(arg0 instanceof File){
						File file = (File)arg0;
						return String.valueOf(file.getName());
					}else if(arg0 instanceof HashMap){
						HashMap<String,Object> file = (HashMap<String,Object>)arg0;
						return String.valueOf(file.get("methodName"));
					}else {
						return null;
					}
				}
				public void addListener(ILabelProviderListener arg0) { }
				public void dispose() { }
				public boolean isLabelProperty(Object arg0, String arg1) {
					return false;
				}
				public void removeListener(ILabelProviderListener arg0) { }
			 });
			 treeViewer.setContentProvider(new ITreeContentProvider(){
				public void dispose() { }
				public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
				public Object getParent(Object arg0) { return null; }
				@SuppressWarnings("unchecked")
				public Object[] getChildren(Object arg0) {
					if(arg0 instanceof File){
						File file = (File)arg0;
						if(file.isDirectory()){
							File[] children = file.listFiles();
							List<File> directory = new ArrayList<File>();
							List<File> files = new ArrayList<File>();
							for(int i = 0;i<children.length;i++){
								if(children[i].isDirectory()){
									directory.add(children[i]);
								}else{
									files.add(children[i]);
								}
							}
							directory.addAll(files);
							return directory.toArray();
						}else if(file.getName().endsWith(".java")){
							AnalysisJavaFile javaFile = new AnalysisJavaFile();
							List<HashMap> list = javaFile.getJavaInfo(file);
							return list.toArray();
						}
					}
					return null;
				}
				public boolean hasChildren(Object arg0) {
					if(arg0 instanceof File){
						File f = (File)arg0;
						if(f.isDirectory()||f.getName().endsWith("java")){
							return true;
						}else{
							return false;
						}
					}
					return false ;
				}
				@Override
				public Object[] getElements(Object arg0) {
					if(arg0 instanceof File){
						File file = (File)arg0;
						File[] children = file.listFiles();
						List<File> directory = new ArrayList<File>();
						List<File> files = new ArrayList<File>();
						for(int i = 0;i<children.length;i++){
							if(children[i].isDirectory()){
								directory.add(children[i]);
							}else{
								files.add(children[i]);
							}
						}
						directory.addAll(files);
						return directory.toArray();
					}
					return null;
				}
				
			 });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		treeViewer.setInput(new File(rootPath));
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		final LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer()   ;
	    Transfer[] transfers = new Transfer[] {
	    		transfer
	    };
	    treeViewer.addDragSupport(ops, transfers, new DragSourceAdapter(){
			@Override
			public void dragStart(DragSourceEvent event) {
				event.data = treeViewer.getSelection();   
				transfer.setSelection(treeViewer.getSelection());
				return;   
			}
	    	
	    });
	   // Tree tree = treeViewer.getTree();
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Create the actions
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar
	 */
	private void initializeToolBar() {
		@SuppressWarnings("unused")
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu
	 */
	private void initializeMenu() {
		@SuppressWarnings("unused")
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
