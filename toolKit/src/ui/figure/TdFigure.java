package ui.figure;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import util.ColorValue;

public class TdFigure extends Figure {
	public AbstractBorder org_broder = null;
	public TdFigure(Rectangle rect){
		try{
			this.setBounds(rect);
			setBorder(new CompoundBorder(new AbstractBorder() {
				public Insets getInsets(IFigure figure) {
					return new Insets(0, 0, 0, 0);
				}
				public void paint(IFigure figure, Graphics graphics, Insets insets) {
					Point left =  new Point(getPaintRectangle(figure, insets).getTopRight().x-1,getPaintRectangle(figure, insets).getTopRight().y);
					Point right = new Point(getPaintRectangle(figure, insets).getBottomRight().x-1,getPaintRectangle(figure, insets).getBottomRight().y);
					graphics.setForegroundColor(ColorValue.getColor());
					graphics.drawLine(left, right);
				}
			},new MarginBorder(0)));
			
			XYLayout layout = new XYLayout();
			this.setLayoutManager(layout);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
