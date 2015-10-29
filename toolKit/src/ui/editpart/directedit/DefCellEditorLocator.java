package ui.editpart.directedit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

public class DefCellEditorLocator implements CellEditorLocator {
	private IFigure figure = null;
	
	public DefCellEditorLocator(IFigure figure) {
		this.figure = figure;
	}

	@Override
	public void relocate(CellEditor arg0) {
		Text text = (Text)arg0.getControl();
		Rectangle rect = figure.getBounds().getCopy();
		figure.translateToAbsolute(rect);
		text.setBounds(rect.x, rect.y+1, rect.width-1, rect.height-2);
	}

}
