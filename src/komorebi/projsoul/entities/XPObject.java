package komorebi.projsoul.entities;

import java.awt.Rectangle;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.states.Game;

public class XPObject extends Entity {

  private int xp;
  private boolean[] hitBy;
  private Rectangle area;
  
  private float dx, dy;
  
  private boolean destroyMe;
  
  private static final int GRAV_RADIUS = 12;
  
  public XPObject(float x, float y, int xp, boolean[] hitBy) {
    super(x, y, 8, 8);
    
    area = new Rectangle((int) x, (int) y, 8, 8);
    this.hitBy = hitBy; 
    
    this.xp = xp;
  }

  @Override
  public void update() {
    setX(getX() + dx);
    y += dy;
    
    area.x += dx;
    area.y += dy;
  }
  
  public void eat()
  {
    destroyMe = true;
    
    int halfXP = (int) Math.ceil(xp / (double) 2);
    
    //Gives half the XP to the player who picked up the object
    Map.getPlayer().giveXP(halfXP);
    
    int denom = 0;
    
    for (boolean b: hitBy)
    {
      if (b) denom++;
    }
    
    int split = (int) Math.ceil(halfXP / (double) denom);
    
    //Splits the remaining half amongst all players who contributed
    //to the kill
    for (int i = 0; i < hitBy.length; i++)
    {
      if (hitBy[i])
      {
        Game.getMap().giveXP(Characters.getCharacter(i), split);  
      }
    }
    
  }

  @Override
  public void render() {
     Draw.rectCam(getX(), y, sx, sy, 1, 45, 9, 53, 11);
  }
  
  public boolean destroyed()
  {
    return destroyMe;
  }
  
  public Rectangle getHitBox()
  {
    return area;
  }
  
  public boolean withinRadius(Rectangle r)
  {
    
    return (Map.distanceBetween(r.x, r.y, getX()+4, y+4)<GRAV_RADIUS) ||
        (Map.distanceBetween(r.x+r.width, r.y, getX()+4, y+4)<GRAV_RADIUS) ||
        (Map.distanceBetween(r.x+r.width, r.y+r.height, getX()+4, y+4)<GRAV_RADIUS) ||
        (Map.distanceBetween(r.x, r.y+r.height, getX()+4, y+4)<GRAV_RADIUS);
  }
  
  public void guide(float tarX, float tarY)
  {
    if (getX() < tarX) dx = 1;
    else if (getX() > tarX) dx = -1;
    
    if (y < tarY) dy = 1;
    else if (y > tarY) dy = -1;
  }
  
  public void setSpeed(float dx, float dy)
  {
    this.dx = dx;
    this.dy = dy;
  }

}
