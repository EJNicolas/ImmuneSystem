package background;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import util.*;
import main.BodyPanel;

//Each branch in the capillary. Uses recursion to draw the whole object. Each thread is responsible of drawing itself and creating its sub-branches

public class CapillaryThread {

	private double length, width;
	private double angle;
	
	private CapillaryThread top = null;
	private CapillaryThread middle = null;
	private CapillaryThread bottom = null;
	
	private Rectangle2D.Double thread;
	
	private float shortenBranch = 0.65f;
	private float shortenCenterBranch = 0.6f;
	
	private double topAngle = -0.3f;
	private double bottomAngle = 0.3f;
	
	private double absoluteAngle;
	
	private double scale = 1;
	private double increaseScale = 0.0001;
	private int increaseScaleTimer = 60;
	
	CapillaryThread(double len, double wid, double ang, int layer, double absAng){
		length = len;
		width = wid;
		angle = ang;
		
		absoluteAngle = absAng += angle;
		
		thread = new Rectangle2D.Double(-width/2, -length, width, length);
		
		if(layer>=1) {
top = new CapillaryThread((length * shortenBranch) * Util.random(1,1.2), width * shortenBranch, topAngle + Util.random(-0.2 * topAngle,0.2 * topAngle), layer-1, absoluteAngle);
			
			middle = new CapillaryThread((length * shortenCenterBranch) * Util.random(1,1.2), width * shortenCenterBranch, Util.random(-0.2,0.2), layer-1, absoluteAngle);
		
			bottom = new CapillaryThread((length * shortenBranch) * Util.random(1,1.2), width * shortenBranch, bottomAngle + Util.random(-0.2 * bottomAngle,0.2 * bottomAngle), layer-1, absoluteAngle);
		}
		
		if(layer==0){
			middle = new CapillaryThread((75) * Util.random(1,1.2), width * shortenCenterBranch, -absoluteAngle + Util.random(-0.2,0.2), layer-1, absoluteAngle);
		}
		
	}
	
	//I have color as a parameter here so I can easily change the color for each capillary
	public void drawCapillary(Graphics2D g2, Color c) {
		AffineTransform tr = g2.getTransform();
		
		//sets the drawing space for the branch
		g2.rotate(angle);
		g2.scale(scale,scale);
		
		scale+=increaseScale;
		
		//changes the rate in which scale is changed according to the body's energy level
		if(BodyPanel.getEnergy()>3100) increaseScaleTimer-=2;
		else if(BodyPanel.getEnergy()>2500) increaseScaleTimer-=5;
		else if(BodyPanel.getEnergy()>0) increaseScaleTimer-=1;
		
		//Makes the expanding and contacting effect
		if(increaseScaleTimer<0) {
			increaseScale*=-1;
			increaseScaleTimer=60;
		}
		
		g2.setColor(c);
		g2.fill(thread);
		
		g2.translate(0, -length);
		if (top != null) top.drawCapillary(g2,c);
		if (middle != null) middle.drawCapillary(g2,c);		
		if (bottom != null) bottom.drawCapillary(g2,c);
		g2.setTransform(tr);
		
	}
	
}
