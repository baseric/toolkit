package ui.figure;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import util.ColorValue;

public class TrFigure extends Figure {
	public TrFigure(Rectangle rect){
		try{
			this.setBounds(rect);
			setBorder(new CompoundBorder(new AbstractBorder() {
				public Insets getInsets(IFigure figure) {
					return new Insets(0, -1, 0, 0);
				}
				public void paint(IFigure figure, Graphics graphics, Insets insets) {
					Point left = new Point(getPaintRectangle(figure, insets).getBottomLeft().x,getPaintRectangle(figure, null).getBottomLeft().y-1);
					Point right = new Point(getPaintRectangle(figure, insets).getBottomRight().x,getPaintRectangle(figure, null).getBottomRight().y-1);
					graphics.setForegroundColor(ColorValue.getColor());
					graphics.drawLine(left, right);
				}
			},new MarginBorder(0)));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
