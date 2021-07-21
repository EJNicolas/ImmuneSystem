package bodycells;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;

//Decorator for the body cell. Draws multiple large green dots on the cell

public class BodyCellInfected extends BodyCellDecorator{

	public BodyCellInfected(BaseBodyCell b) {
		super(b);
	}
	
	public void drawCell(Graphics2D g2) {
		super.drawCell(g2);
		parentBodyCell.drawCell(g2);
		AffineTransform original = g2.getTransform();
		
		BaseBodyCell baseCell = getBaseBodyCell();
		g2.setTransform(baseCell.getPointTransformation());
		
		g2.setColor(new Color(85,204,101));
		g2.fillOval(-5,-5,10,10);
		g2.fillOval(10,10,10,10);
		g2.fillOval(-30,20,10,10);
		g2.fillOval(10,-15,10,10);
		g2.fillOval(12,22,10,10);
		g2.fillOval(-25,-5,10,10);
		
		g2.setTransform(original);
		
	}
	
	
}
