package intruder;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.geom.*;
import processing.core.*;

//Update their position and draw itself. Its just a circle bouncing around the screen so theres nothing too special in here
//How it multiplies is in the BodyCell class

public class Virus extends BaseIntruder{

	private Ellipse2D.Double body;
	
	private double angle = 0;
	
	public Virus(float posX, float posY, float velX, float velY, int sizeX, int sizeY, int scale) {
		super(posX, posY, velX, velY, sizeX, sizeY, scale);
		
		body = new Ellipse2D.Double();
		
		setDrawingAttributes();
		setOutline();
	}

	@Override
	public void move() {
		pos.add(vel);
		collisionWallDetection();
	}

	@Override
	public void drawCell(Graphics2D g2) {
		AffineTransform original = g2.getTransform();
		
		g2.translate(pos.x,pos.y);
		g2.scale(scale,scale);
		g2.rotate(angle);
		
		AffineTransform point = g2.getTransform();
		hitBox = outline.createTransformedArea(point);
		
		g2.setColor(new Color(133,146,255));
		g2.fill(body);
		
		g2.setTransform(original);
	}
	
	
	private void setDrawingAttributes() {
		body.setFrame(-2.5,-2.5,5,5);
	}
	
	private void setOutline() {
		outline.add(new Area(body));
	}
	
}
