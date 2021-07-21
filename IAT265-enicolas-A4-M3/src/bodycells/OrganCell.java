package bodycells;

import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import main.BodyPanel;

import java.awt.Color;
import util.Util;


/*
 * Handles updating, drawing, and managing its infection itself
 * This class exists because it draws itself differently from the body cell and the amount of bacteria they spawn is different.
 */

public class OrganCell extends BaseBodyCell {

	private Ellipse2D.Double cellBody;
	
			
	public OrganCell(float posX, float posY, float velX, float velY, int sizeX, int sizeY, double scale) {
		super(posX, posY, velX, velY, sizeX, sizeY, scale);
		
		cellBody = new Ellipse2D.Double();
		infectedTimer = 300;
		
		
		setDrawingAttributes();
		setOutline();
	}

	@Override
	public void move() {
		pos.add(vel);
		if(infected || infectedLevel!=0) createIntruder();
	}
	
	private void createIntruder() {
		
			infectedTimer--;
			if(infectedTimer==0) {
				for(int i=0;i<(int)Util.random(1,2);i++) {
					BodyPanel.addVirusAtPosition(pos.x,pos.y);
					
				}
				BodyPanel.bodyCellList.remove(this);
				BodyPanel.infectedCellList.remove(this);
			}
		
			
			if(infectedLevel>=2) {
				for(int i=0;i<(int)Util.random(3,4);i++) {
					BodyPanel.addBacteriaAtPosition(pos.x,pos.y);
				}
				BodyPanel.bodyCellList.remove(this);
			}
	}
	
	

	@Override
	public void drawCell(Graphics2D g2) {
		AffineTransform original = g2.getTransform();
		
		g2.translate(pos.x,pos.y);
		g2.scale(scale,scale);
		
		AffineTransform point = g2.getTransform();
		
		hitBox = outline.createTransformedArea(point);
		
		g2.setColor(new Color(255,73,71));
		g2.fill(cellBody);
		
		g2.setTransform(original);
		
	}
	
	private void setDrawingAttributes() {
		cellBody.setFrame(-SIZE_X/2,-SIZE_X/2,SIZE_X,SIZE_Y);
	}
	
	private void setOutline() {
		outline.add(new Area(cellBody));
	}

	@Override
	protected BaseBodyCell getBaseBodyCell() {
		return this;
	}

	@Override
	protected AffineTransform getPointTransformation() {
		// TODO Auto-generated method stub
		return null;
	}

}
