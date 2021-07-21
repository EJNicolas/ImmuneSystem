package background;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

//Has all of the capillary threads. In charge of translating the canvas so they appear as they do on screen.
//I use a clone of the initial capillary and perform transformations on it to seem like the two are connected to each other

public class Capillary {
	
	private CapillaryThread capillary;
	private CapillaryThread capillaryOtherSide;
	
	private double posX, posY;
	
	Capillary(double x, double y, double len, double wid, double ang, int layer){
		posX = x;
		posY = y;
		capillary= new CapillaryThread(len, wid, ang, layer, 0);
		//creates a clone of the first one.
		capillaryOtherSide = capillary;
	}
	
	public void drawCapillary(Graphics2D g2) {
		AffineTransform original = g2.getTransform();
		
		g2.translate(posX,posY);
		AffineTransform point = g2.getTransform();
		
		g2.rotate(1.57);
		Color leftArteryColor = (new Color(219,51,32));
		capillary.drawCapillary(g2,leftArteryColor);
		
		g2.setTransform(point);
		//mirrors the other capillary on the y axis
		g2.scale(1,-1);
		//moves it to the other side of the screen so it goes where the vein is
		g2.translate(710,0);
		//rotates it 90 degrees counter clockwise
		g2.rotate(-1.57);
		Color rightArteryColor = new Color(76,77,219);
		capillaryOtherSide.drawCapillary(g2,rightArteryColor);
		
		g2.setTransform(original);
	}
	
	
	
}
