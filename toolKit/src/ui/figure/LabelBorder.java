package ui.figure;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;

public class LabelBorder extends AbstractBorder{
	public Insets getInsets(IFigure figure) {
		return new Insets(0, 0, 0, 0);
	}
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		Point left =  new Point(getPaintRectangle(figure, insets).getBottomLeft().x-1,getPaintRectangle(figure, insets).getBottomLeft().y-1);
		Point right = new Point(getPaintRectangle(figure, insets).getBottomRight().x-1,getPaintRectangle(figure, insets).getBottomRight().y-1);
		graphics.setForegroundColor(ColorConstants.gray);
		graphics.drawLine(left, right);
	}
}
