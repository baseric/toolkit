package business.dialog;

import images.IconFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class FileDialog extends TitleAreaDialog {
	private Tree tree;
	private String rootPath = "";
	private HashMap<String,Object> params = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public FileDialog(Shell parentShell,HashMap<String,Object> params,String rootPath) {
		super(parentShell);
		this.params = params;
		this.rootPath = rootPath;
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
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
						}
					}else if(arg0 instanceof HashMap){
						HashMap<String,Object> file = (HashMap<String,Object>)arg0;
						return IconFactory.getImageDescriptor("business/"+String.valueOf(file.get("icon"))).createImage();
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
						return String.valueOf(file.get("name"));
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
						}else if(file.getName().endsWith(".class")){
								String path = file.getAbsolutePath().replace(rootPath, "").replace(".class", "").replace("\\",".");
								try {
									File programRootDir = new File(rootPath);
								    URLClassLoader classLoader = new URLClassLoader(new URL[]{programRootDir.toURI().toURL()});
									Class clazz = Class.forName(path, false, classLoader);
									Method[] ms = clazz.getMethods();
									Constructor[]  constructors = clazz.getConstructors();
									List<HashMap<String,Object>> childrens = new ArrayList<HashMap<String,Object>>();
									for(int i = 0;i<ms.length;i++){
										HashMap<String,Object> map = new HashMap<String,Object>();
										Class[] types = ms[i].getParameterTypes();
										Class outparm = ms[i].getReturnType();
										String parameters = "";
										for(int j = 0;j<types.length;j++){
											//
											if(types[j].isArray()){
												parameters += types[j].getName().replace("[L", "").replace(";", "[]")+", ";
											}else{
												parameters += types[j].getName()+",";
											}
										}
										map.put("icon", "80.gif");
										map.put("file", file);
										map.put("name", ms[i].getName()+" ( "+parameters+" )");
										map.put("nodeName", ms[i].getName());
										map.put("in_param", types);
										map.put("out_param", outparm);
										map.put("path", path);
										map.put("constructors", constructors);
										childrens.add(map);
									}
									return childrens.toArray();
								} catch (Exception e) {
									e.printStackTrace();
								}
						}
					}
					return null;
				}
				public boolean hasChildren(Object arg0) {
					if(arg0 instanceof File){
						File f = (File)arg0;
						if(f.isDirectory()||f.getName().endsWith("class")){
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
		setTitle("选择文件");
		setMessage("选择最底层节点");
		//
		return container;
	}
	 
	/** 
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(389, 556);
	}
	@Override
	protected Point getInitialLocation(Point initialSize) {
		// TODO Auto-generated method stub
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - initialSize.x / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - initialSize.y / 2;
		return new Point(nLocationX,nLocationY);
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void okPressed() {
		try {
			TreeItem[] items = tree.getSelection();
			if(items[0].getData() instanceof HashMap){
				HashMap<String,Object> f = (HashMap<String,Object>)items[0].getData();
				
				Set<String> keys = f.keySet();
				for(String key : keys){
					params.put(key, f.get(key));
				}
				super.okPressed();
			}else{
				setErrorMessage("请选择方法");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
