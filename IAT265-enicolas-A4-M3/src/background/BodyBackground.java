package background;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;

//Draws the background. Also has the reference variable for the recursion pattern in the background


public class BodyBackground {
	
	private Capillary backgroundCapillary;
	private Capillary backgroundCapillary2;
	private Rectangle2D.Double background;
	private Rectangle2D.Double leftArtery;
	private Rectangle2D.Double rightArtery;
	private Rectangle2D.Double organArea;
	
	public BodyBackground(){
		
		backgroundCapillary = new Capillary(80,200,120,30,0,3);
		backgroundCapillary2 = new Capillary(80,600,120,30,0,3);
		
		
		background = new Rectangle2D.Double();
		leftArtery = new Rectangle2D.Double();
		rightArtery = new Rectangle2D.Double();
		organArea = new Rectangle2D.Double();
		
		background.setFrame(0,0,1200,800);
		leftArtery.setFrame(50,0,100,1200);
		rightArtery.setFrame(700,0,100,1200);
		organArea.setFrame(980,0,300,1200);
		
	}
	
	public void drawBackground(Graphics2D g2) {
		AffineTransform original = g2.getTransform();
		
		g2.setColor(new Color(255,82,103));
		g2.fill(background);
		
		g2.setColor(new Color(219,51,32));
		g2.fill(leftArtery);
		
		g2.setColor(new Color(76,77,219));
		g2.fill(rightArtery);
		
		g2.setColor(new Color(104,79,179));
		g2.fill(organArea);
		
		backgroundCapillary.drawCapillary(g2);
		backgroundCapillary2.drawCapillary(g2);
		
		g2.setTransform(original);
	}
	
	
	
	
	
}
