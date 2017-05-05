/**
 * Tackler.java    Jan 23, 2017, 9:12:10 AM
 */
package komorebi.projsoul.entities.enemy;

import komorebi.projsoul.ai.node.composite.MemSequence;
import komorebi.projsoul.ai.node.composite.Priority;
import komorebi.projsoul.ai.node.composite.Sequence;
import komorebi.projsoul.ai.node.decorator.Invincible;
import komorebi.projsoul.ai.node.leaf.Behavior;
import komorebi.projsoul.ai.node.leaf.BehaviorStates;
import komorebi.projsoul.ai.node.leaf.IdleBehavior;
import komorebi.projsoul.ai.node.leaf.LineUpBehavior;
import komorebi.projsoul.ai.node.leaf.ShakeCamera;
import komorebi.projsoul.ai.node.leaf.TackleBehavior;
import komorebi.projsoul.ai.node.leaf.WalkBehavior;
import komorebi.projsoul.ai.node.leaf.conditions.IsHittingPlayer;
import komorebi.projsoul.ai.node.leaf.conditions.IsHittingWall;
import komorebi.projsoul.ai.node.leaf.conditions.IsPlayerInRange;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.EditorMap.Modes;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.map.MapHandler;

import java.util.HashMap;
import java.util.Random;

/**
 * An enemy that lines up and tries to charge at the player
 *
 * @author Aaron Roy
 */
public class Tackler extends Enemy {

  float targetX, targetY; //Location of the player
  float currDist;         //Calculated distance between this enemy and the player
  
  
  private BehaviorStates currState = BehaviorStates.IDLE;
  
  protected final int distance;
  
  protected static final float RUN_SPEED = 1f;
  protected static final float WALK_SPEED = 0.5f;
  protected static final float TACKLE_SPEED = 2f;

  
  private static final int MAX_IDLE = 60;
  private static final int MAX_WALK = 120;
  private static final int MAX_STUN = 60;
  private static final int MAX_WAIT = 30;
  
  private int idleCount = MAX_IDLE;
  private int walkCount = MAX_WALK;
  private int stunCount = MAX_STUN;
  private int waitCount = MAX_WAIT;

  private static final Random GEN = new Random();
    
  //DEBUG radius
  protected int red, green, blue;
  
  private Face direction = Face.DOWN;
  
  private Priority root;
  
  //Stats
  public static final int baseAttack = 37, baseDefense = 50, baseHealth = 250;
  private static final int TOLERANCE = 12;

  /**
   * Creates an enemy that tries to run away from the player and shoot at a distance
   * 
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param type The sprite of this enemy
   * @param radius The distance the enemy tries to be away from the player
   */
  public Tackler(float x, float y, EnemyType type, int radius){
    this(x, y, type, radius, 1);
  }
  
  /**
   * Creates an enemy that tries to run away from the player and shoot at a distance
   * 
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param type The sprite of this enemy
   * @param radius The distance the enemy tries to be away from the player
   * @param level The level of this enemy
   */
  public Tackler(float x, float y, EnemyType type, int radius, int level){
    super(x, y, type, level);
    
    distance = 16*radius;
    
    regAni.stop();
    
    red = (int)(Math.random()*255);
    green = (int)(Math.random()*255);
    blue = (int)(Math.random()*255);
    
    Behavior[] behaviors = new Behavior[]{
        new IdleBehavior(this, MAX_IDLE),
        new WalkBehavior(this, MAX_WALK, WALK_SPEED),
        new LineUpBehavior(this, RUN_SPEED, TOLERANCE),
    };
    this.behaviors = new HashMap<>();
    for(Behavior currBehavior: behaviors){
      this.behaviors.put(currBehavior.getState(), currBehavior);
    }
    
    
    //Just see 
    //https://drive.google.com/open?id=1RM_jia5cyQiPZ4u_SoY9sr2EkXuvCJXd_rGmrUFrUkg
    root = new Priority(
        new Sequence(
            new IsPlayerInRange(this, distance),
            new MemSequence(                       //Line up and Tackle
                new LineUpBehavior(this, RUN_SPEED, TOLERANCE),
                new Invincible(this, 
                    new TackleBehavior(this, TACKLE_SPEED)
                    ),
                new Priority(
                    new MemSequence(     //Wait for the player to get knockback
                        new IsHittingPlayer(this),
                        new Invincible(this, 
                            new IdleBehavior(this, MAX_WAIT)
                            )
                        ),
                    new MemSequence(                  //Hit a wall, get stunned
                        new Sequence(
                            new IsHittingWall(this),
                            new ShakeCamera(8, 2, 1)
                            ),
                        new IdleBehavior(this, MAX_STUN)
                        )
                    )
                )
            ), 
        new MemSequence(                             //Walk around
            new IdleBehavior(this, MAX_IDLE),
            new WalkBehavior(this, MAX_WALK, WALK_SPEED)
            )
        );

  }
  
  @Override
  public void update(){
    if(!hurt){
      dx = 0;
      dy = 0;

      targetX = MapHandler.getPlayer().getX();
      targetY = MapHandler.getPlayer().getY();

      currDist = MapHandler.distanceBetween(x,y,targetX,targetY);

      root.update();
    }
    

    super.update();

  }
  
  @Override
  public void render() {
    //TODO Better implementation
    if(EditorMap.getMode() == Modes.EVENT){
      Draw.circ(x, y, distance, red, blue, green, 64);

    }
    if(MapHandler.isHitBox){
      Draw.circCam(x, y, distance, red, blue, green, 64);
    }

    super.render();
  }

  @Override
  public int xpPerLevel() {
    return 100;
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
  public String getBehavior(){
    return "tackler";
  }
}
