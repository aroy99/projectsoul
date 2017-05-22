package komorebi.projsoul.entities;

import java.awt.Rectangle;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.states.Game;

public class BaseXPObject extends Entity {

	protected int xp;
	protected Rectangle area;
	protected float dx;
	protected float dy;
	protected boolean destroyMe;
	protected static final int GRAV_RADIUS = 12;

	public BaseXPObject(float x, float y, int xp) 
	{
		super(x, y, 8, 8);
		this.xp = xp;
		area = new Rectangle((int) x, (int) y, 8, 8);
	}

	@Override
	public void update() {
	    x += dx;
	    y += dy;
	    
	    area.x += dx;
	    area.y += dy;
	  }

	public void eat() 
	{
	    destroyMe = true;
	    Map.getPlayer().giveXP(xp);
	    
	}

	@Override
	public void render() {
	     Draw.rectCam(x, y, sx, sy, 1, 45, 9, 53, 11);
	  }

	public boolean destroyed() {
	    return destroyMe;
	  }

	public Rectangle getHitBox() {
	    return area;
	  }

	public boolean withinRadius(Rectangle r) {
	    
	    return (Map.distanceBetween(r.x, r.y, x+4, y+4)<GRAV_RADIUS) ||
	        (Map.distanceBetween(r.x+r.width, r.y, x+4, y+4)<GRAV_RADIUS) ||
	        (Map.distanceBetween(r.x+r.width, r.y+r.height, x+4, y+4)<GRAV_RADIUS) ||
	        (Map.distanceBetween(r.x, r.y+r.height, x+4, y+4)<GRAV_RADIUS);
	  }

	public void guide(float tarX, float tarY) {
	    if (x < tarX) dx = 1;
	    else if (x > tarX) dx = -1;
	    
	    if (y < tarY) dy = 1;
	    else if (y > tarY) dy = -1;
	  }

	public void setSpeed(float dx, float dy) {
	    this.dx = dx;
	    this.dy = dy;
	  }

}