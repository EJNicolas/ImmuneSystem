package intruder;

import java.awt.*;
import processing.core.*;
import main.BaseCell;

//Abstract super class for each type of intruder. They just drift around so there is nothing much here

public abstract class BaseIntruder extends BaseCell {
 
	
	protected BaseIntruder(float posX, float posY, float velX, float velY, int sizeX, int sizeY, int scale) {
		super(posX, posY, velX, velY, sizeX, sizeY, scale);
		
	}
	public abstract void move();
	public abstract void drawCell(Graphics2D g2);
	
}
