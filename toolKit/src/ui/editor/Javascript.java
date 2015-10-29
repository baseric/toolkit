package ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
/**
 * JAVASCRIPT 编辑器
 * @author Administrator
 *
 */
public class Javascript extends EditorPart {

	private Text text;
	public static final String ID = "ui.editor.Javascript"; //$NON-NLS-1$
	private boolean bDirty = false;
	private String js = null;
	public Javascript(String jsCode){
		js = jsCode;
	}
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.setSite(site);
	}
	public String getJsCode(){
		return text.getText();
	}
	/**
	 * Create contents of the editor part
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		final StackLayout stackLayout = new StackLayout();
		container.setLayout(stackLayout);

		text = new Text(container, SWT.BORDER | SWT.WRAP |SWT.MULTI|SWT.V_SCROLL );
		text.setFont(new Font(parent.getDisplay(),"Consolas",10,SWT.NORMAL));
		text.setText(js);
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				if(!isDirty()){
					setDirty(true);
					firePropertyChange(IEditorPart.PROP_DIRTY);
				}
			}
		});
		stackLayout.topControl = text;
		//
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		setDirty(false);
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}



	@Override
	public boolean isDirty() {
		return bDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		text.setFocus();
	}
	public void setDirty(boolean b){
		bDirty = b;
	}
}
