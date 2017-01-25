package komorebi.projsoul.entities.enemy;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.states.Death;
import komorebi.projsoul.map.EditorMap.Modes;
import komorebi.projsoul.states.Game;

public class Chaser extends Enemy {

  float targetX, targetY; //Location of the player
  float dist;             //Calculated distance between this enemy and the player

  protected static final float speed = .25f;

  protected int maxPlayDist;

  protected int red, green, blue;


  public static final int baseAttack = 50;
  public static final int baseDefense = 50;
  public static final int baseHealth = 100;

  /**
   * Creates an enemy that will chase the player within a certain range
   * 
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param distanceFromPlay The maximum distance the enemy can be from the player
   *      and still chase him/her
   */
  public Chaser(float x, float y, EnemyType type, int distanceFromPlay) {
    this(x,y,type,distanceFromPlay,1);
  }

  public Chaser(float x, float y, EnemyType type, int distanceFromPlay, int level) {
    super(x, y, type, level);

    maxPlayDist = 16*distanceFromPlay;

    regAni.setSpeed((int)(6/speed));
    
    red = (int)(Math.random()*255);
    green = (int)(Math.random()*255);
    blue = (int)(Math.random()*255);  
  }

  /**
   * Updates the chaser's status and location
   */
  public void update()
  {   
    targetX = Map.getPlayer().getX();
    targetY = Map.getPlayer().getY();
    if(Death.playable)
    {

      dist = Map.distanceBetween(x,y,targetX,targetY);
      
      if (dist > maxPlayDist && (dx!=0 || dy!=0))
      {
        dx = 0;
        dy = 0;
        regAni.stop();
      }

      if (!hurt && dist <= maxPlayDist)
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
    
    super.update();
  }
  
  protected void enemyUpdate(){
    super.update();
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



  @Override
  public void render() {
    //TODO Better implementation
    if(EditorMap.getMode() == Modes.EVENT){
      Draw.circ(x, y, maxPlayDist, red, blue, green, 64);
    }
    if(Map.isHitBox){
      Draw.circCam(x, y, maxPlayDist, red, blue, green, 64);
    }

    super.render();
  }

  @Override
  public String getBehavior(){
    return "chaser";
  }

  public int getOriginalRadius(){
    return maxPlayDist/16;
  }

}
