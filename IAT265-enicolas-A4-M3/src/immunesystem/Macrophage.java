package immunesystem;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.geom.*;
import main.BodyPanel;
import processing.core.*;

/*
 * Updates and draws itself. Its ability to eat and remove intruders is done within the BodyPanel class
 */

public class Macrophage extends BaseImmuneCell{
	
	float speedConstant = 0.8f;
	
	private Ellipse2D.Double cellBody;
	
	public Macrophage(float posX, float posY, float velX, float velY, int sizeX, int sizeY, double scale) {
		super(posX, posY, velX, velY, sizeX, sizeY, scale);
		
		energy = 400;
		
		cellBody = new Ellipse2D.Double();
		
		setDrawingAttributes();
		setOutline();
	}

	@Override
	public void move() {
		pos.add(vel);		//add velocity to position
		collisionWallDetection();
		vel.add(accel);	
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
		
		AffineTransform point = g2.getTransform();
		hitBox = outline.createTransformedArea(point);
		
		g2.setColor(new Color(180,180,180));
		
		g2.fill(cellBody);
		g2.setTransform(original);
		
	}
	
	public void increaseEnergy(int a) {
		energy+=a;
	}
	
	private void setDrawingAttributes() {
		cellBody.setFrame(-SIZE_X/2,-SIZE_Y/2,SIZE_X,SIZE_Y);
	}
	
	private void setOutline() {
		outline.add(new Area(cellBody));
	}
	
}
