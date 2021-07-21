package immunesystem;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;

import main.BodyPanel;

/*
 * Antibodies update and draw themselves. Their ability to stop intruders is done within the BodyPanel class
 */

public class Antibody extends BaseImmuneCell{
	
	private double angle = 0;
	
	private float speedConstant = 0.9f;
	
	private Line2D.Double threadTop;
	private Line2D.Double threadLeft;
	private Line2D.Double threadRight;
	
	//This bounding area is what will be used for its hitbox since lines as area wont work
	private Ellipse2D.Double boundingArea;
	
	
	public Antibody(float posX, float posY, float velX, float velY, int sizeX, int sizeY, double scale) {
		super(posX, posY, velX, velY, sizeX, sizeY, scale);
		
		energy = 200;
		
		threadTop = new Line2D.Double();
		threadLeft = new Line2D.Double();
		threadRight = new Line2D.Double();
		
		boundingArea = new Ellipse2D.Double();	
		setDrawingAttributes();
		setOutline();
	}

	@Override
	public void move() {
		pos.add(vel);		//add velocity to position
		collisionWallDetection();
		vel.add(accel);		//constantly add the acceleration vector to the velocity to make smooth turns. BONUS ATTEMPT
		if(accel.x != 0 && accel.y != 0) vel.mult(speedConstant);
		
		energy--;
		if(energy<0) {
			BodyPanel.immuneCellList.remove(this);
		}
	}

	@Override
	public void drawCell(Graphics2D g2) {
		AffineTransform original = g2.getTransform();
		
		g2.translate(pos.x,pos.y);
		g2.scale(scale,scale);
		g2.rotate(angle);
		angle+=0.01;
		
		AffineTransform point = g2.getTransform();
		
		hitBox = outline.createTransformedArea(point);
		
		g2.setColor(new Color(85,126,255));
		g2.draw(threadTop);
		g2.draw(threadLeft);
		g2.draw(threadRight);
		
		g2.setTransform(original);
		
	}
	
	private void setDrawingAttributes() {
		threadTop.setLine(0,0,0,-5);
		threadLeft.setLine(0,0,-5,3);
		threadRight.setLine(0,0,5,3);
		
		boundingArea.setFrame(-2.5,-2.5,8,8);
	}
	
	private void setOutline() {
		outline.add(new Area(boundingArea));
	}
	
	

}
