package komorebi.projsoul.entities;

import komorebi.projsoul.map.Map;

public class Chaser extends Enemy {
  
  float targetX, targetY;
  
  private final float speed = 0.5f;
  private boolean tooFar;
  
  private float maxClydeDist;

  
  /**
   * 
   * @param x
   * @param y
   * @param sx
   * @param sy
   * @param distanceFromClyde
   */
  public Chaser(float x, float y, int sx, int sy, float distanceFromClyde) {
    super(x, y, sx, sy);
    
    maxClydeDist = distanceFromClyde;
  }

  @Override
  public void update()
  {
    super.update();
   
    targetX = Map.getClyde().getX();
    targetY = Map.getClyde().getY();
    
    if (distanceBetween(x,y,targetX,targetY)>maxClydeDist && (dx!=0 || dy!=0))
    {
      dx = 0;
      dy = 0;
    }
    
    if (!invincible && distanceBetween(x,y,targetX,targetY)<=maxClydeDist)
    {
      if (targetX>x && Math.abs(targetX-x)>12)
      {
        dx = speed;
      } else if (targetX<x)
      {
        dx = -speed;
      }
      
      if (targetY>y && Math.abs(targetY-y)>12)
      {
        dy = speed;
      } else if (targetY<y)
      {
        dy = -speed;
      }
    }
  }
}
