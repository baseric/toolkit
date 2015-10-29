package ui.dialog.tablemodify.cellEditor.dialog;

import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ui.UIAbstractModel;
import util.KeyCode;

public class HotKeyConfigDialog extends Dialog {

	private Text text;
	protected String result;
	protected Shell shell;
	private List<HashMap<String,String>> list = null;
	private UIAbstractModel model = null;
	/**
	 * Create the dialog
	 * @param parent
	 * @param style
	 */
	public HotKeyConfigDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Create the dialog
	 * @param parent
	 */
	public HotKeyConfigDialog(Shell parent,UIAbstractModel model) {
		this(parent, SWT.NONE);
		this.model = model;
	}

	/**
	 * Open the dialog
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - 210 / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - 342 / 2;
	    shell.setLocation(nLocationX, nLocationY);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}

	/**
	 * Create contents of the dialog
	 */
	protected void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(465, 123);
		shell.setText("快捷键绑定");
		
		final Button button = new Button(shell, SWT.NONE);
		button.setBounds(155, 60, 65, 27);
		button.setText("确定");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				result = text.getText();
				shell.close();
			}
		}); 

		final Label label = new Label(shell, SWT.NONE);
		label.setText("快捷键：");
		label.setBounds(10, 27, 58, 17);

		text = new Text(shell, SWT.BORDER);
		text.setEditable(false);
		text.setBounds(74, 24, 277, 25);
		text.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				text.setText(getKeysInfo(evt));
			}
		});
		final Button button_1 = new Button(shell, SWT.NONE);
		button_1.setText("关闭");
		button_1.setBounds(255, 60, 58, 27);
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shell.close();
			}
		}); 
		final Button removeButton = new Button(shell, SWT.NONE);
		removeButton.setText("remove");
		removeButton.setBounds(356, 22, 79, 27);
	}
	public String getKeysInfo(KeyEvent evt){
		String info = "";
		if(evt.stateMask==SWT.CTRL){
			info +="Ctrl_";
		}else if(evt.stateMask==SWT.ALT){
			info +="Alt_";
		}else if(evt.stateMask==SWT.SHIFT){
			info +="Shift_";
		}
		
		KeyCode key = new KeyCode();
		String str = key.getKeyName(evt.keyCode);
		if(str!=null){
			info += str;
		}
		return info;
	}
}
 