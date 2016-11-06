package komorebi.projsoul.entities;

import komorebi.projsoul.map.Map;

public class Chaser extends Enemy {
  
  float targetX, targetY;
  
  private final float speed = 0.5f;
  
  private float maxClydeDist;
  
  public static final int baseAttack = 35;
  public static final int baseDefense = 50;
  public static final int baseHealth = 50;

  
  /**
   * Creates an enemy that will chase the player within a certain range
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param sx The horizontal size, in pixels, of the enemy
   * @param sy The vertical size, in pixels, of the enemy
   * @param distanceFromClyde The maximum distance the enemy can be from the player
   *      and still chase him/her
   */
  public Chaser(float x, float y, int sx, int sy, float distanceFromPlay) {
    this(x,y,sx,sy,distanceFromPlay,1);
  }
  
  public Chaser(float x, float y, int sx, int sy, float distanceFromPlay, int level) {
    super(x, y, sx, sy, level);
    
    maxClydeDist = distanceFromPlay;
  }
  
  /**
   * Updates the chaser's status and location
   */
  public void update()
  {   
    super.update();
    
    targetX = Map.getPlayer().getX();
    targetY = Map.getPlayer().getY();
    
    if (Map.distanceBetween(x,y,targetX,targetY)>maxClydeDist && (dx!=0 || dy!=0))
    {
      dx = 0;
      dy = 0;
    }
    
    if (!invincible && Map.distanceBetween(x,y,targetX,targetY)<=maxClydeDist)
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

  @Override
  public int xpPerLevel() {
    return 10;
  }

  @Override
  public int baseAttack() {
    return baseAttack;
  }

  @Override
  public int baseDefense() {
    return baseDefense;
  }

  @Override
  public int baseHealth() {
    return baseHealth;

  }
 
}
