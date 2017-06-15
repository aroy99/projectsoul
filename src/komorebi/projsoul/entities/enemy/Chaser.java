package komorebi.projsoul.entities.enemy;

import komorebi.projsoul.ai.node.composite.MemSequence;
import komorebi.projsoul.ai.node.composite.Priority;
import komorebi.projsoul.ai.node.composite.Sequence;
import komorebi.projsoul.ai.node.leaf.IdleBehavior;
import komorebi.projsoul.ai.node.leaf.MoveToTarget;
import komorebi.projsoul.ai.node.leaf.SetTargetToPlayer;
import komorebi.projsoul.ai.node.leaf.WalkBehavior;
import komorebi.projsoul.ai.node.leaf.conditions.IsPlayerInRange;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.EditorMap.Modes;
import komorebi.projsoul.map.MapHandler;
import komorebi.projsoul.states.Death;

/**
 * An enemy that chases the player
 *
 * @author Aaron Roy
 */
public class Chaser extends Enemy{

  float dist;             //Calculated distance between this enemy and the player

  protected static final float SPEED = 1.5f;
  
  private static final int MAX_IDLE = 60;
  private static final int MAX_WALK = 120;

  protected int maxPlayDist;

  protected int red, green, blue;

  public static final int baseAttack = 50;
  public static final int baseDefense = 50;
  public static final int baseHealth = 100;
  
  private Priority root;

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

    regAni.setSpeed((int)(6/(SPEED/2)));
    System.out.println(1);
    
    generateHitboxColors();
    
    root = new Priority(
        new Sequence(
            new IsPlayerInRange(this, maxPlayDist),
            new SetTargetToPlayer(this),
            new MoveToTarget(this, SPEED, 0)
            ),
        new MemSequence(                             //Walk around
            new IdleBehavior(this, MAX_IDLE),
            new WalkBehavior(this, MAX_WALK, SPEED/2)
            )
        );
  }

  /**
   * Updates the chaser's status and location
   */
  public void update()
  {   
    if(Death.playable)
    {
      dx = 0;
      dy = 0;
      
      root.update();
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
    if(MapHandler.isHitBox){
      Draw.circCam(x, y, maxPlayDist, red, blue, green, 64);
    }
    

    
    if (invincible)
    {
      if(health>=0)
      {
        Draw.rectCam(this.getX()-2, this.getY()+23, (int) (health*0.41), 2, 5, 2, 7, 2, 11);
      }
      Draw.rectCam(this.getX()-3, this.getY()+22, 22, 4, 0, 317, 22, 321, 11);
      hitAni.playCam(x, y);
    } else if (dying)
    {
      deathAni.playCam(x, y);
    } else
    {
      Draw.rectCam(x, y, sx, sy, 0, 0, 16, 21, 0, 11);
      Draw.rectCam(this.getX()-3, this.getY()+22, 22, 4, 0, 317, 22, 321, 11);
      if(health>=0)
      {
        Draw.rectCam(this.getX()-2, this.getY()+23, (int) (health*0.41), 2, 5, 2, 7, 2, 11);
      }
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

  private void generateHitboxColors() {
    red = (int)(Math.random()*255);
    green = (int)(Math.random()*255);
    blue = (int)(Math.random()*255);
  }

}
