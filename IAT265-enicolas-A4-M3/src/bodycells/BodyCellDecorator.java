package bodycells;

import java.awt.Graphics2D;
import java.awt.geom.*;

//Super class decorator for the body cell

public class BodyCellDecorator extends BaseBodyCell {

	protected BaseBodyCell parentBodyCell;
	
	protected BodyCellDecorator(float posX, float posY, float velX, float velY, int sizeX, int sizeY, double scale) {
		super(posX, posY, velX, velY, sizeX, sizeY, scale);
		// TODO Auto-generated constructor stub
	}
	
	protected BodyCellDecorator(BaseBodyCell b) {
		super(b.getPos().x, b.getPos().y, b.getVel().x, b.getVel().y, b.getSizeX(), b.getSizeY(), b.getScale());
		parentBodyCell = b;
	}

	@Override
	public void move() {
	}

	@Override
	public void drawCell(Graphics2D g2) {
		parentBodyCell.drawCell(g2);		
	}
	
	public BaseBodyCell getBaseBodyCell() {
		return parentBodyCell.getBaseBodyCell();
	}

	@Override
	protected AffineTransform getPointTransformation() {
		return parentBodyCell.getPointTransformation();
	}

}
