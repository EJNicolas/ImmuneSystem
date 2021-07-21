package intruder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.*;
import java.awt.geom.Ellipse2D;
import main.BodyPanel;
import util.Util;

//Update their position and draw itself. Bacteria have its own multiply function which creates another one of itself after a certain duration
//Its ability to multiply through cells is within the BodyCell class

public class Bacteria extends BaseIntruder{
	
	private Ellipse2D.Double body;
	private Ellipse2D.Double bodyWall;
	
	private int multiplyTimer = 50;

	public Bacteria(float posX, float posY, float velX, float velY, int sizeX, int sizeY, int scale) {
		super(posX, posY, velX, velY, sizeX, sizeY, scale);
		
		body = new Ellipse2D.Double();
		bodyWall = new Ellipse2D.Double();
		
		setDrawingAttributes();
		setOutline();
	}
	
	@Override
	public void move() {
		pos.add(vel);
		collisionWallDetection();
		
		multiply();
	}

	@Override
	public void drawCell(Graphics2D g2) {
		AffineTransform original = g2.getTransform();
		
		g2.translate(pos.x,pos.y);
		g2.scale(scale,scale);
		float angle = vel.heading();
		g2.rotate(angle);
		
		AffineTransform point = g2.getTransform();
		hitBox = outline.createTransformedArea(point);
		
		g2.setColor(new Color(50,235,90));
		g2.fill(bodyWall);
		
		g2.setColor(new Color(130,255,143));
		g2.fill(body);
		
		g2.setTransform(original);
	}
	
	
	private void setDrawingAttributes() {
		body.setFrame(-7.5,-5.5,15,10);
		bodyWall.setFrame(-10,-7.5,20,15);
	}
	
	private void setOutline() {
		outline.add(new Area(bodyWall));
	}
	
	private void multiply(){
		multiplyTimer--;
		if(multiplyTimer<0) {
			multiplyTimer = 500;
			BodyPanel.addBacteriaAtPosition(pos.x,pos.y);
		}
	}
	
	
}
