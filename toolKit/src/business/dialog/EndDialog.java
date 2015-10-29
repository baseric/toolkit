package business.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import util.EditorUtil;
import util.PublicTableSet;
import business.model.logic.EndModel;

public class EndDialog extends TitleAreaDialog {

	private Table table;
	private Text desc;
	private Text display;
	private TableViewer writeParam = null;
	private EndModel end = null;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public EndDialog(Shell parentShell,EndModel end) {
		super(parentShell);
		this.end = end;
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		try{
			Composite container = new Composite(area, SWT.NONE);
			container.setLayoutData(new GridData(GridData.FILL_BOTH));
	
			final TabFolder tabFolder = new TabFolder(container, SWT.NONE);
			tabFolder.setBounds(0, 10, 792, 388);
	
			final TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
			tabItem_1.setText("参数设置");
	
			final Composite composite = new Composite(tabFolder, SWT.NONE);
			tabItem_1.setControl(composite);
	
			final Group group = new Group(composite, SWT.NONE);
			group.setText("输出到参数设置");
			group.setBounds(0, 47, 784, 301);
	
			writeParam = new TableViewer(group, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
			table = writeParam.getTable();
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			table.setBounds(10, 20, 714, 271);
			List<String> params = new ArrayList<String>();
			end.getAllParamName(params);
			PublicTableSet.jspTable(end,writeParam,params);
			writeParam.setInput(end.getWriteParam());
			final Button button_1 = new Button(group, SWT.NONE);
			button_1.setText("新增");
			button_1.setBounds(730, 20, 50, 27);
	
			final Button button_2 = new Button(group, SWT.NONE);
			button_2.setText("删除");
			button_2.setBounds(730, 56, 50, 27);
			button_1.addMouseListener(new MouseAdapter() {
				int count = 0;
				public void mouseDown(MouseEvent e) {
					try {
						HashMap<String,String> row = new HashMap<String,String>();
						row.put("paramName", "name"+count++);
						row.put("translation", "N");
						row.put("desc", "");
						row.put("key", String.valueOf(System.currentTimeMillis()));
						writeParam.add(row);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			button_2.addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					try {
						Table tab = writeParam.getTable();
						TableItem[] item = tab.getSelection();
						for(int i = 0;i<item.length;i++){
							TableItem[] all = tab.getItems();
							for(int j = 0;j<all.length;j++){
								if(all[j]==item[i]){
									tab.remove(j);
								}
							}
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			final Label label = new Label(composite, SWT.NONE);
			label.setBounds(10, 24,59, 17);
			label.setText("显示名称：");
	
			display = new Text(composite, SWT.BORDER);
			display.setBounds(119, 21,447, 25);
			display.setText(end.getDisplay());
			final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
			tabItem.setText("备注信息");
	
			final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
			tabItem.setControl(composite_1);
	
			desc = new Text(composite_1, SWT.BORDER);
			desc.setBounds(10, 10, 565, 288);
	 
			setMessage("设置方法属性");
		}catch(Exception e){
			e.printStackTrace();
		}
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

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(808, 553);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("信息");
	}
	@Override
	protected Point getInitialLocation(Point initialSize) {
		// TODO Auto-generated method stub
		int nLocationX = Display.getCurrent().getClientArea().width / 2 - initialSize.x / 2;
	    int nLocationY = Display.getCurrent().getClientArea().height / 2 - initialSize.y / 2;
		return new Point(nLocationX,nLocationY);
	}
	@Override
	protected void okPressed() {
		end.setDisplay(display.getText());
		end.setWriteParam(PublicTableSet.getTableData(writeParam));
		EditorUtil.fireEditorDirty();
		super.okPressed();
	}

}
