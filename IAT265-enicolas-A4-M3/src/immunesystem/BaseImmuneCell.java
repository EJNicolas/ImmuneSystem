package immunesystem;

import java.awt.*;
import java.util.ArrayList;
import processing.core.*;
import main.BaseCell;

/*
 * Superclass in which all of the immune cells will inherit from.
 * Contains fields for each cell's energy and has a method that allows the cell to follow the closest object selected from an arraylist
 */

public abstract class BaseImmuneCell extends BaseCell{
	
	protected PVector accel = new PVector();
	protected int energy;
	
	BaseImmuneCell(float posX, float posY, float velX, float velY, int sizeX, int sizeY, double scale) {
		super(posX, posY, velX, velY, sizeX, sizeY, scale);	
	}
	
	public abstract void move();
	
	public abstract void drawCell(Graphics2D g2);
	
	
	public void decreaseEnergy(int n) {
		energy-=n;
	}
	
	//Goes through an array list of an object and moves towards the closest one
	public void goTowards(ArrayList<BaseCell> targetList) {
		try {
			if(targetList.size()!=0) {
				double distA, distB;
				BaseCell chosenCell = null;
				BaseCell otherChosenCell = null;
				
				for(int i=0;i<targetList.size();i++) {
					BaseCell a = targetList.get(i);
					distA = PVector.dist(pos,a.getPos());
					
					for(int j = i + 1;j<targetList.size();j++) {
						BaseCell b = targetList.get(j);
						distB = PVector.dist(pos,b.getPos());
						
						if(distA < distB) {
							otherChosenCell = a;
							} else {
								otherChosenCell = b;
							}
						
						if(!(chosenCell!=null)) chosenCell = otherChosenCell;
						else {
							float distCell = PVector.dist(pos,chosenCell.getPos());
							float distOtherCell = PVector.dist(pos,otherChosenCell.getPos());
							
							if(distCell > distOtherCell) chosenCell = otherChosenCell;	
						}		
					}
				}
				
				if(targetList.size()==1) {
					chosenCell = targetList.get(0);
				}
				
				if(chosenCell!=null) {
					accel.x = chosenCell.getPos().x - pos.x;
					accel.y = chosenCell.getPos().y - pos.y;
					accel.normalize();
				}
			} else {
				accel.x = 0;
				accel.y = 0;
			}
		}catch(Exception e) {
			System.out.println("Error in BaseImmuneCell goTowrads() method");
		}
	}
	
	
	//moves towards a selected object
	public void goTowards(BaseCell b) {
			accel.x = b.getPos().x - pos.x;
			accel.y = b.getPos().y - pos.y;
			accel.normalize();
	}
	
}
