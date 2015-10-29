package business.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import util.EditorUtil;
import business.model.LineConnectionModel;

public class LinkDialog extends TitleAreaDialog {

	private Text text_5;
	private Combo valueType;
	private Text valueName;
	private Combo optType;
	private Text paramName;
	private Text className;
	private Text javaEl;
	private Combo eqType;
	private Text display;
	private LineConnectionModel line = null;
	
	private String[] optTypes = new String[]{"isNull","is not Null","equals","not equals","isTrue","isFalse","<=",">=","<",">","=="};
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public LinkDialog(Shell parentShell,LineConnectionModel line) {
		super(parentShell);
		this.line = line;
	}

	@Override
	protected void okPressed() {
		try {
			line.setType(String.valueOf(eqType.getSelectionIndex()));
			line.setParamName(paramName.getText());
			int index = optType.getSelectionIndex();
			if(index!=-1)
				line.setOpt(optType.getItem(index));
			line.setParamName2(valueName.getText());
			line.setValueType(String.valueOf(valueType.getSelectionIndex()));
			
			line.setClassName(className.getText());
			
			line.setJavaEl(javaEl.getText());
			
			line.setDisplay(display.getText());
			EditorUtil.fireEditorDirty();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.okPressed();
	}
	public void refreshVisual(int key){
		if(key==0){
        	setEnable1(false);
        	javaEl.setEnabled(false);
        	className.setEnabled(false);
        }else if(key==1){
        	setEnable1(true);
        	javaEl.setEnabled(false);
        	className.setEnabled(false);
        }else if(key == 2){
        	setEnable1(false);
        	javaEl.setEnabled(false);
        	className.setEnabled(true);
        }else if(key==3){
        	setEnable1(false);
        	javaEl.setEnabled(true);
        	className.setEnabled(false);
        }
	}
	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Label label = new Label(container, SWT.NONE);
		label.setText("显示名称：");
		label.setBounds(5, 25, 59, 17);

		display = new Text(container, SWT.BORDER);
		display.setText(line.getDisplay());
		display.setBounds(70, 22, 336, 25);

		final Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("判断类型");
		label_1.setBounds(428, 25, 59, 17);
		
		int type = Integer.parseInt(line.getType());
		eqType = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		eqType.add("空条件");
		eqType.add("判断条件");
		eqType.add("判断类");
		eqType.add("java表达式");
		eqType.select(type);
		eqType.setBounds(504, 22, 163, 25);
		eqType.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent e) {
                int key = eqType.getSelectionIndex();
                refreshVisual(key);
            }
        });

		final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setBounds(0, 60, 705, 338);

		final TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("条件判断");

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite);

		final Group group = new Group(composite, SWT.NONE);
		group.setText("设置条件判断");
		group.setBounds(0, 10, 671, 90);

		final Label label_3 = new Label(group, SWT.NONE);
		label_3.setText("变量：");
		label_3.setBounds(10, 43, 31, 17);

		paramName = new Text(group, SWT.BORDER);
		paramName.setEnabled(false);
		paramName.setBounds(65, 40, 113, 25);
		paramName.setText(line.getParamName());
		
		List<String> param_names = new ArrayList<String>();
		line.getSource().getAllParamName(param_names);
		String[] arr = new String[param_names.size()];
		param_names.toArray(arr);
		TextContentAdapter ad = new TextContentAdapter(){};
		new AutoCompleteField(paramName,ad , arr);
		optType = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY);
		optType.setEnabled(false);
		optType.setItems(optTypes);
		for(int i = 0;i<optTypes.length;i++){
			if(line.getOpt().equals(optTypes[i])){
				optType.select(i);
				break;
			}
		}
		optType.setBounds(185, 40, 123, 25);

		valueName = new Text(group, SWT.BORDER);
		valueName.setEnabled(false);
		valueName.setBounds(317, 40, 134, 25);
		valueName.setText(line.getParamName2());
		final Label label_4 = new Label(group, SWT.NONE);
		label_4.setText("值类型");
		label_4.setBounds(465, 45, 45, 17);

		valueType = new Combo(group, SWT.NONE);
		valueType.setEnabled(false);
		valueType.add("常量");
		valueType.add("变量");
		valueType.select(Integer.parseInt(line.getValueType()));
		valueType.setBounds(517, 40, 143, 25);

		final Group group_1 = new Group(composite, SWT.NONE);
		group_1.setText("设置判断类");
		group_1.setBounds(0, 106, 671, 90);

		final Label label_2 = new Label(group_1, SWT.NONE);
		label_2.setText("判断类");
		label_2.setBounds(10, 38, 49, 17);

		className = new Text(group_1, SWT.BORDER);
		className.setEnabled(false);
		className.setBounds(65, 35, 538, 25);
		className.setText(line.getClassName());
		final Button button = new Button(group_1, SWT.NONE);
		button.addMouseListener(new MouseAdapter() {
			public void mouseDown(final MouseEvent e) {
				try {
					String rootPath = "E:\\runtime-EclipseApplication\\financemanage_yq\\WebRoot\\WEB-INF\\classes\\";
					FileDialog d = new FileDialog(Display.getCurrent().getActiveShell(),null,rootPath);
					d.open();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		       
			}
		});
		button.setText("......");
		button.setBounds(614, 33, 50, 27);

		final Group group_2 = new Group(composite, SWT.NONE);
		group_2.setText("设置JEXL表达式");
		group_2.setBounds(0, 202, 697, 90);

		javaEl = new Text(group_2, SWT.BORDER);
		javaEl.setEnabled(false);
		javaEl.setBounds(10, 26, 677, 54);
		javaEl.setText(line.getJavaEl());
		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("备注信息");

		final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_1);

		text_5 = new Text(composite_1, SWT.BORDER);
		text_5.setBounds(10, 10, 677, 288);
 
		setMessage("设置连线的属性");
		
		refreshVisual(type);
		//
		return area;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	@Override
	protected Point getInitialLocation(Point initialSize) {
		// TODO Auto-generated method stub
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - initialSize.x / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - initialSize.y / 2;
		return new Point(nLocationX,nLocationY);
	}
	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(693, 563);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("信息");
	}
	
	public void setEnable1(boolean enabled){
		paramName.setEnabled(enabled);
		optType.setEnabled(enabled);
		valueName.setEnabled(enabled);
		valueType.setEnabled(enabled);
	}
}
