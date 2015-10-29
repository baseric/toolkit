package ui.figure;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import ui.model.grid.ColumnModel;
import util.ColorValue;

public class ColumnFigure extends Label {
	public ColumnFigure(ColumnModel model){
		int width = Integer.parseInt(model.getWidth());
		int height = Integer.parseInt(model.getHeight());
		try{
			this.setBounds(new Rectangle(0,0,width,height+3));
			setBorder(new CompoundBorder(new AbstractBorder() {
				public Insets getInsets(IFigure figure) {
					return new Insets(0, 0, 0, 0);
				}
				public void paint(IFigure figure, Graphics graphics, Insets insets) {
					Point left =  new Point(getPaintRectangle(figure, insets).getTopRight().x-1,getPaintRectangle(figure, insets).getTopRight().y);
					Point right = new Point(getPaintRectangle(figure, insets).getBottomRight().x-1,getPaintRectangle(figure, insets).getBottomRight().y);
					graphics.setForegroundColor(ColorConstants.lightGray);
					graphics.drawLine(left, right);
				}
			},new MarginBorder(0)));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
