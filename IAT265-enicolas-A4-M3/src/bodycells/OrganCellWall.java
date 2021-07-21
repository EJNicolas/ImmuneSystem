package bodycells;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;

//The lining of cells that protect the organ cells. This class draws them and sets a variable whether objects can pass through them or not

public class OrganCellWall extends BaseBodyCell{
	
	private Ellipse2D.Double cellBody;
	private Rectangle2D.Double cellHitBox;
	
	//boolean that states if objects can hit them or not.
	private boolean passable;
	
	public OrganCellWall(float posX, float posY, float velX, float velY, int sizeX, int sizeY, double scale) {
		super(posX, posY, velX, velY, sizeX, sizeY, scale);
		
		cellBody = new Ellipse2D.Double();
		cellHitBox = new Rectangle2D.Double();
		
		passable = false;
		
		setDrawingAttributes();
		setOutline();
	}

	@Override
	public void move() {
		
	}

	@Override
	public void drawCell(Graphics2D g2) {
		// TODO Auto-generated method stub
		AffineTransform original = g2.getTransform();
		
		g2.translate(pos.x,pos.y);
		if(scale<=0) {
			scale=0;
		}
		
		if(scale>=1) {
			scale=1;
		}
		g2.scale(scale, scale);
		
		AffineTransform point = g2.getTransform();
		
		hitBox = outline.createTransformedArea(point);
		
		g2.setColor(new Color(232,65,143));
		if(passable) g2.setColor(new Color(70,59,71));
		g2.fill(cellBody);
		
		g2.setTransform(original);
		
	}
	
	public void setPassable(boolean b) {
		passable = b;
	}
	
	public boolean getPassable() {
		return passable;
	}
	
	private void setDrawingAttributes() {
		cellBody.setFrame(-SIZE_X/2,-SIZE_Y/2,SIZE_X,SIZE_Y);
		cellHitBox.setFrame(-SIZE_X/2,-SIZE_Y/2,SIZE_X,SIZE_Y);
	}
	
	private void setOutline() {
		outline.add(new Area(cellHitBox));
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
