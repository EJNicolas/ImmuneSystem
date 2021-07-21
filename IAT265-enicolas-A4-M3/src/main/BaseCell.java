package main;

import java.awt.*;
import java.awt.geom.Area;

import processing.core.*;

/*
 * Abstract super class for all cells (including intruders such as bacteria and viruses)
 */

public abstract class BaseCell {
	
	protected PVector pos;
	protected PVector vel;	
	
	protected final int SIZE_X;
	protected final int SIZE_Y;
	protected double scale;
	
	//this needs to be here so i can put the collision method here. All cells will have these anyway
	protected Area outline = new Area();
	protected Area hitBox = new Area();
	
	protected BaseCell(float posX, float posY, float velX, float velY, int sizeX, int sizeY, double s){
		pos = new PVector(posX,posY);
		vel = new PVector(velX,velY);
		SIZE_X = sizeX;
		SIZE_Y = sizeY;
		scale = s;
	}
	
	public abstract void move();
	public abstract void drawCell(Graphics2D g2);	
	
	protected void collisionWallDetection() {
		if(pos.x + SIZE_X/2 * scale > 1200 || pos.x - SIZE_X/2 * scale < 0) {
			vel.x*=-1;
		}

		if(pos.y + SIZE_Y/2 * scale > 800 || pos.y - SIZE_Y/2 * scale < 0) {
			vel.y*=-1;
		}
	}
	
	
	protected boolean collideWith(BaseCell b) {
		 if(hitBox.intersects(b.hitBox.getBounds2D()) && b.hitBox.intersects(hitBox.getBounds2D())) return true;
		 else return false;
	}
	
	public PVector getPos() {
		return pos;
	}
	
	public PVector getVel() {
		return vel;
	}
	
	public void setVel(float x, float y) {
		vel.x = x;
		vel.y = y;
	}
	
	public void setScale(double s) {
		scale = s;
	}
	
	public double getScale() {
		return scale;
	}
	
	public int getSizeX() {
		return SIZE_X;
	}
	
	public int getSizeY() {
		return SIZE_Y;
	}
	
}