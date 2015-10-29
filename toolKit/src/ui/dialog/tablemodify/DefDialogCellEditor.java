package ui.dialog.tablemodify;

import java.text.MessageFormat;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
public abstract class DefDialogCellEditor extends CellEditor {
    public static final String CELL_EDITOR_IMG_DOTS_BUTTON = "cell_editor_dots_button_image";//$NON-NLS-1$
    private Composite editor;
    private Control contents;
    private Text defaultLabel;
    private Button button;
	private FocusListener buttonFocusListener;
    private Object value = null;

    static {
        ImageRegistry reg = JFaceResources.getImageRegistry();
        reg.put(CELL_EDITOR_IMG_DOTS_BUTTON, ImageDescriptor.createFromFile(
        		DefDialogCellEditor.class, "images/dots_button.gif"));//$NON-NLS-1$
    }
    private class DialogCellLayout extends Layout {
        public void layout(Composite editor, boolean force) {
            Rectangle bounds = editor.getClientArea();
            Point size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
            if (contents != null) {
				contents.setBounds(0, 0, bounds.width - size.x, bounds.height);
			}
            button.setBounds(bounds.width - size.x, 0, size.x, bounds.height);
        }

        public Point computeSize(Composite editor, int wHint, int hHint,
                boolean force) {
            if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
            Point contentsSize = contents.computeSize(SWT.DEFAULT, SWT.DEFAULT,
                    force);
            Point buttonSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT,
                    force);
            Point result = new Point(buttonSize.x, Math.max(contentsSize.y,
                    buttonSize.y));
            return result;
        }
    }
    private static final int defaultStyle = SWT.NONE;
    public DefDialogCellEditor() {
        setStyle(defaultStyle);
    }
    protected DefDialogCellEditor(Composite parent) {
        this(parent, defaultStyle);
    }
    protected DefDialogCellEditor(Composite parent, int style) {
        super(parent, style);
    }
    protected Button createButton(Composite parent) {
        Button result = new Button(parent, SWT.DOWN);
        result.setText("..."); //$NON-NLS-1$
        return result;
    }
    protected Control createContents(Composite cell) {
        defaultLabel = new Text(cell, SWT.LEFT);
        defaultLabel.setFont(cell.getFont());
        defaultLabel.setBackground(cell.getBackground());
        defaultLabel.addFocusListener(new FocusListener(){
	    	 public void focusGained(FocusEvent e) {
	    	 }
	    	 public void focusLost(FocusEvent e) {
	    		 Text source = (Text)e.getSource();
	    		 doSetValue(source.getText());
	    	 }
	     });
        defaultLabel.addKeyListener(new KeyListener(){
	    	 public void keyPressed(KeyEvent e) {
	    		 if(e.keyCode==13){
	    			 Text source = (Text)e.getSource();
	    			 doSetValue(source.getText());
	    		 }
	    	 }
	    	 public void keyReleased(KeyEvent e) {
	    	 }
	     });
        return defaultLabel;
    }

    /* (non-Javadoc)
     * Method declared on CellEditor.
     */
    protected Control createControl(Composite parent) {

        Font font = parent.getFont();
        Color bg = parent.getBackground();

        editor = new Composite(parent, getStyle());
        editor.setFont(font);
        editor.setBackground(bg);
        editor.setLayout(new DialogCellLayout());

        contents = createContents(editor);
        updateContents(value);

        button = createButton(editor);
        button.setFont(font);

        button.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.character == '\u001b') { // Escape
                    fireCancelEditor();
                }
            }
        });
        
        button.addFocusListener(getButtonFocusListener());
        
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
            	button.removeFocusListener(getButtonFocusListener());
                
            	Object newValue = openDialogBox(editor);
            	button.addFocusListener(getButtonFocusListener());

            	if (newValue != null) {
                    boolean newValidState = isCorrect(newValue);
                    if (newValidState) {
                        markDirty();
                        doSetValue(newValue);
                    } else {
                        setErrorMessage(MessageFormat.format(getErrorMessage(),
                                new Object[] { newValue.toString() }));
                    }
                    fireApplyEditorValue();
                }
            }
        });

        setValueValid(true);

        return editor;
    }
    public void deactivate() {
    	if (button != null && !button.isDisposed()) {
    		button.removeFocusListener(getButtonFocusListener());
    	}
    	
		super.deactivate();
	}
    protected Object doGetValue() {
        return value;
    }
    protected void doSetFocus() {
    	if(defaultLabel!=null){
    		defaultLabel.selectAll();
    		defaultLabel.setFocus();
   	    }
    }
    private FocusListener getButtonFocusListener() {
    	if (buttonFocusListener == null) {
    		buttonFocusListener = new FocusListener() {
				public void focusGained(FocusEvent e) {
					// Do nothing
				}
				public void focusLost(FocusEvent e) {
					DefDialogCellEditor.this.focusLost();
				}
    		};
    	}
    	
    	return buttonFocusListener;
	}
 
    protected void doSetValue(Object value) {
        this.value = value;
        updateContents(value);
    }
 
    protected Text getDefaultLabel() {
        return defaultLabel;
    }
 
    protected abstract Object openDialogBox(Control cellEditorWindow);
 
    protected void updateContents(Object value) {
        if (defaultLabel == null) {
			return;
		}

        String text = "";//$NON-NLS-1$
        if (value != null) {
			text = value.toString();
		}
        defaultLabel.setText(text);
    }
}
