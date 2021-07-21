package immunesystem;

import java.awt.Graphics2D;
import java.awt.geom.*;

import main.BodyPanel;

import java.awt.Color;

//In charge of updating and drawing itself. Its ability to support macrophages by supplying them with more energy is done within the superclass

public class TCell extends BaseImmuneCell{

	float speedConstant = 0.7f;
	
	private Ellipse2D.Double cellBody;
	private Ellipse2D.Double cellCenter;
	
	//This geom is used as its hitbox. Any macrophage in this area will be given more energy
	private Ellipse2D.Double effectArea;
	
	public TCell(float posX, float posY, float velX, float velY, int sizeX, int sizeY, double scale) {
		super(posX, posY, velX, velY, sizeX, sizeY, scale);
		
		energy = 150;
		
		cellBody = new Ellipse2D.Double();
		cellCenter = new Ellipse2D.Double();
		effectArea = new Ellipse2D.Double();
		
		setDrawingAttributes();
		setOutline();
	}

	@Override
	public void move() {
		pos.add(vel);	
		collisionWallDetection();
		vel.add(accel);	
		vel.mult(speedConstant);	
		
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
		
		g2.setColor(new Color(255,143,65));
		g2.fill(cellCenter);
		
		g2.setTransform(original);
	}
	
	private void setDrawingAttributes() {
		cellBody.setFrame(-SIZE_X/2,-SIZE_Y/2,SIZE_X,SIZE_Y);
		cellCenter.setFrame(-5,-5,10,10);
		effectArea.setFrame(-250,-250,500,500);
	}

	private void setOutline() {
		outline.add(new Area(cellBody));
		outline.add(new Area(effectArea));
	}
}
