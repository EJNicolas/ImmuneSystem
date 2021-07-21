package bodycells;

import java.awt.*;
import java.awt.geom.AffineTransform;

import processing.core.*;
import main.BaseCell;

/*
 * Base abstract calls for all of the body cells (body cells, organ cells)
 * The difference with this and the base cell is that this has methods and fields that tell a cell's infection
 */

public abstract class BaseBodyCell extends BaseCell {

	protected boolean infected;
	protected int infectedLevel = 0;
	protected int infectedTimer;
	
	protected BaseBodyCell(float posX, float posY, float velX, float velY, int sizeX, int sizeY, double scale) {
		super(posX, posY, velX, velY, sizeX, sizeY, scale);
		// TODO Auto-generated constructor stub
	}
	
	public abstract void move();
	public abstract void drawCell(Graphics2D g2);	
	protected abstract BaseBodyCell getBaseBodyCell();
	protected abstract AffineTransform getPointTransformation();
	
	public void setInfected(boolean b) {
		infected = b;
	}
	
	public boolean getInfected() {
		return infected;
	}
	
	public void raiseInfectedLevel() {
		infectedLevel++;
	}
	
	public int getInfectedLevel() {
		return infectedLevel;
	}
	
	public int getInfectedTimer() {
		return infectedTimer;
	}
	

}