package bodycells;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;

//Draws small green dots on the cell

public class BodyCellSlightInfected extends BodyCellDecorator{

	public BodyCellSlightInfected(BaseBodyCell b) {
		super(b);
	}
	
	public void drawCell(Graphics2D g2) {
		super.drawCell(g2);
		parentBodyCell.drawCell(g2);
		AffineTransform original = g2.getTransform();
		
		BaseBodyCell baseCell = getBaseBodyCell();
		g2.setTransform(baseCell.getPointTransformation());
		
		g2.setColor(new Color(85,204,101));
		
		g2.fillOval(-10,5,5,5);
		g2.fillOval(20,-15,5,5);
		g2.fillOval(-5,15,5,5);
		
		g2.setTransform(original);
		
	}
	
	

}
