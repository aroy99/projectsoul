package komorebi.projsoul.entities;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.map.EditorMap.Modes;
import komorebi.projsoul.states.Game;

public class Chaser extends Enemy {

  float targetX, targetY;

  private final float speed = 2f;

  private int maxClydeDist;
  
  private int red, green, blue;


  /**
   * Creates an enemy that will chase the player within a certain range
   * 
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param type The sprite of this enemy
   * @param distanceFromPlay The maximum distance the enemy can be from the player
   *      and still chase him/her
   */
  public Chaser(float x, float y,  EnemyType type, int distanceFromPlay) {
    super(x, y, type);

    maxClydeDist = 16*distanceFromPlay;
    System.out.println("Chaser: " + maxClydeDist);
    
    red = (int)(Math.random()*255);
    green = (int)(Math.random()*255);
    blue = (int)(Math.random()*255);
  }

  /**
   * Updates the chaser's status and location
   */
  public void update()
  {   
    super.update();

    targetX = Map.getClyde().getX();
    targetY = Map.getClyde().getY();

    if (distanceBetween(x,y,targetX,targetY) > maxClydeDist && (dx != 0 || dy != 0))
    {
      dx = 0;
      dy = 0;
    }

    if (!invincible && distanceBetween(x,y,targetX,targetY) <= maxClydeDist)
    {
      float triX = Math.abs(targetX-x);
      float triY = Math.abs(targetY-y);
      float theta = (float)Math.atan(triY/triX);
      
      if (targetX > x && triX > 12)
      {
        dx = speed*(float)Math.cos(theta);
      } else if (targetX < x)
      {
        dx = -speed*(float)Math.cos(theta);
      }

      if (targetY > y && triY > 12)
      {
        dy = speed*(float)Math.sin(theta);
      } else if (targetY < y)
      {
        dy = -speed*(float)Math.sin(theta);
      }
    }
  }
  
  @Override
  public void render() {
    
    if(EditorMap.getMode() == Modes.EVENT){
      Draw.circ(x, y, maxClydeDist, red, blue, green, 64);
    }
    if(Map.isHitBox){
      Draw.circCam(x, y, maxClydeDist, red, blue, green, 64);
    }
    
    super.render();
  }
  
  @Override
  public String getBehavior(){
    return "chaser";
  }
  
  public int getOriginalRadius(){
    return maxClydeDist/16;
  }
    
}
