package bodycells;

import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import util.Util;

import java.awt.BasicStroke;
import java.awt.Color;
import main.BodyPanel;
import processing.core.PApplet;

/*
 * In charge of updating and drawing itself and managing its own infection
 */

public class BodyCell extends BaseBodyCell{
	
	private double angle = 0;
	
	private Ellipse2D.Double bodyHitBox;
	private BufferedImage body;
	private Ellipse2D.Double nucleus;
	private Arc2D.Double smallER;
	private Arc2D.Double largeER;
	private Ellipse2D.Double mitochondria;
	private Rectangle2D.Double microtube;
	
	private AffineTransform point = new AffineTransform();
	
	//A veriable to hold its own infected version of itself
	private BaseBodyCell infectedCell;
	
	private int lowerInfectedLevelTimer = 250;
	
	//Variables needed for the perlin noise
	private GeneralPath dnaPath = new GeneralPath();
	private float noiseSeed = Util.random(0,10);
	private PApplet pa = new PApplet();
	private float dnaAngle = 0;
	private int dnaLength = 150;
	private float dnaRadius = 1;
	private float dnaX, dnaY;
	private float dnaLastX = 0;
	private float dnaLastY = 0;
	
	public BodyCell(float posX, float posY, float velX, float velY, int sizeX, int sizeY, double scale) {
		super(posX, posY, velX, velY, sizeX, sizeY, scale);
		
		body = Util.loadImage("assets/animalCellBody.png");
		bodyHitBox = new Ellipse2D.Double();
		nucleus = new Ellipse2D.Double();
		smallER = new Arc2D.Double();
		largeER = new Arc2D.Double();
		mitochondria  = new Ellipse2D.Double();
		microtube = new Rectangle2D.Double();
		
		
		infectedTimer = 300;
		
		setDrawingAttributes();
		setOutline();
	}	

	@Override
	public void move() {
		pos.add(vel);
		collisionWallDetection();
	
		if(infected || infectedLevel!=0) createIntruder();
	}
	
	private void createIntruder() {
		
		//If infected with a virus
		
		//This is to remove the old infected cell it had so it can replace it with another one
		if(infectedTimer==200) {
			BodyPanel.infectedCellList.remove(infectedCell);
			infectedCell=null;
		}
		
		//If it isnt infected by a bavteria, then do these
		if(infectedLevel==0) {
			infectedTimer--;
			if(infectedTimer==0) {
				//spawns viruses at its position
				for(int i=0;i<(int)Util.random(5,10);i++) {
					BodyPanel.addVirusAtPosition(pos.x,pos.y);
				}
				
				//Removes itself and its infected self from the list
				BodyPanel.infectedCellList.remove(infectedCell);
				BodyPanel.bodyCellList.remove(this);
			}
		}
		
		//If infected with bacteria
		//If the cell has been touched by bacteria twice
		if(infectedLevel >= 2) {
			//spawns bacteria at its position
			for(int i=0;i<(int)Util.random(10,12);i++) {				
				BodyPanel.addBacteriaAtPosition(pos.x,pos.y);
			}
			//Removes itself and its infected self from the list
			BodyPanel.infectedCellList.remove(infectedCell);
			BodyPanel.bodyCellList.remove(this);
		}
		
		//If the cell is infected with bacteria, it can go back to normal if it hasnt been touched for long enough
		if(infectedLevel!=0) {
			lowerInfectedLevelTimer--;
			if(lowerInfectedLevelTimer<0) {
				infectedLevel =0;
				BodyPanel.infectedCellList.remove(infectedCell);
			}
		}
	}
	
	@Override
	public void drawCell(Graphics2D g2) {
		AffineTransform original = g2.getTransform();
		
		g2.translate(pos.x,pos.y);
		g2.scale(scale,scale);
		g2.rotate(angle);
		
		if(infectedCell==null) angle+=0.02;
		angle+=0.002;
		point = g2.getTransform();
		
		hitBox = outline.createTransformedArea(point);
		
		g2.setColor(new Color(255,192,116));
		//g2.fill(body);
		g2.drawImage(body,-40,-40,null);
		g2.setColor(new Color(168,61,167));
		g2.fill(nucleus);
		
		g2.setColor(new Color(180,80,187));
		g2.setStroke(new BasicStroke(2));
		g2.draw(smallER);
		
		g2.setStroke(new BasicStroke(4));
		g2.draw(largeER);
		
		g2.setColor(new Color(255,116,0));
		g2.translate(8,20);
		g2.rotate(0.8);
		g2.fill(mitochondria);
		
		g2.setTransform(point);
		
		g2.setColor(new Color(240,226,38));
		g2.translate(20,-10);
		g2.rotate(0.3);
		g2.fill(microtube);
		g2.setTransform(point);
		
		g2.setColor(new Color(90,4,176));
		g2.translate(-8,-8);
		g2.scale(0.2,0.2);
		g2.setStroke(new BasicStroke(8));
		g2.draw(dnaPath);
		
		g2.setTransform(original);	
	}
	
	private void setDrawingAttributes() {
		bodyHitBox.setFrame(-SIZE_X/2,-SIZE_Y/2,SIZE_X,SIZE_Y);
		nucleus.setFrame(-SIZE_X/2+10,-SIZE_Y/2 + 10, SIZE_X/2,SIZE_X/2);
		
		//sets the path for the DNA strand
		for(int i=0;i<dnaLength;i++) {
			dnaRadius += 0.1;
			noiseSeed  += 0.05;
			float noiseRadius = dnaRadius + (pa.noise(noiseSeed) * 100) - 100;
			
			float ra = Util.radians(dnaAngle +=5);
			
			dnaX = noiseRadius * (float) Math.cos(ra);
			dnaY = noiseRadius * (float) Math.sin(ra);
			
			dnaPath.moveTo(dnaX, dnaY);
			dnaPath.lineTo(dnaLastX, dnaLastY);
			
			// store the ending location for next round drawing
			dnaLastX = dnaX;
			dnaLastY = dnaY;
			
		}
		
		smallER.setArc(-SIZE_X/2+10 - 2.5,-SIZE_Y/2 + 10 - 2.5, SIZE_X/2 + 5,SIZE_X/2 + 5,0,60,Arc2D.Double.OPEN);
		largeER.setArc(-SIZE_X/2+10 - 10,-SIZE_Y/2 + 10 - 10, SIZE_X/2 + 20,SIZE_X/2 + 20,0,60,Arc2D.Double.OPEN);
		mitochondria.setFrame(-10,-7.5,20,15);
		microtube.setFrame(8,5,10,2);
		
		
	}
	
	private void setOutline() {
		outline.add(new Area(bodyHitBox));
	}

	@Override
	protected BaseBodyCell getBaseBodyCell() {
		return this;
	}
	
	public AffineTransform getPointTransformation() {
		return point;
	}
	
	public void setInfectedCell(BaseBodyCell bci) {
		infectedCell = bci;
	}
	
	public boolean hasInfectedCell() {
		if(infectedCell!=null) return true;
		else return false;
	}

}
