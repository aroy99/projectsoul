/**
 * Shooter.java	   Dec 20, 2016, 9:15:13 AM
 */
package komorebi.projsoul.entities.enemy;

import komorebi.projsoul.ai.WeightedSquareGrid;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.map.EditorMap.Modes;

import java.util.Random;

import javax.swing.plaf.BorderUIResource.CompoundBorderUIResource;

/**
 * 
 *
 * @author Aaron Roy
 */
public class Shooter extends Enemy {

  float targetX, targetY; //Location of the player
  float currDist;         //Calculated distance between this enemy and the player

  protected static final float RUN_SPEED = 1.5f;
  protected static final float WALK_SPEED = 0.5f;

  protected int red, green, blue;


  /**
   * The different states a shooter can be in
   *
   * @author Aaron Roy
   */
  public enum ShootStates{
    IDLE, WALK, ATTACK, SHOOT, RUN, TIRED;
  }

  private ShootStates currState = ShootStates.IDLE;

  protected final int distance;
  protected final int runDistance;

  private static final int MAX_IDLE = 60;
  private static final int MAX_WALK = 120;
  private static final int MAX_SHOOT = 120;

  private int idleCount = MAX_IDLE;
  private int walkCount = MAX_WALK;
  private int shootCount = MAX_SHOOT;

  private static final Random GEN = new Random();

  private Face direction = Face.DOWN;

  public static final int baseAttack = 25;
  public static final int baseMagicAttack = 50;
  public static final int baseDefense = 50;
  public static final int baseHealth = 100;
  public static final int baseMagic = 50;


  /**
   * Creates an enemy that tries to run away from the player and shoot at a distance
   * 
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param type The sprite of this enemy
   * @param radius The distance the enemy tries to be away from the player
   */
  public Shooter(float x, float y, EnemyType type, int radius) {
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
  public Shooter(float x, float y, EnemyType type, int radius, int level) {
    super(x, y, type, level);

    distance = 16*radius;
    runDistance = distance/4;

    regAni.stop();

    red = (int)(Math.random()*255);
    green = (int)(Math.random()*255);
    blue = (int)(Math.random()*255);  
  }

  @Override
  public void update(){
    dx = 0;
    dy = 0;

    targetX = Map.getPlayer().getX();
    targetY = Map.getPlayer().getY();

    currDist = Map.distanceBetween(x,y,targetX,targetY);

    if (currState != ShootStates.IDLE && currState != ShootStates.WALK && 
        currDist > distance)
    {
      regAni.stop();
      currState = ShootStates.IDLE;
    }

    if (currState != ShootStates.RUN && !invincible && currDist <= runDistance)
    {
      currState = ShootStates.RUN;
      regAni.resume();
      regAni.setSpeed((int)(6/RUN_SPEED));
    }

    if(currState != ShootStates.ATTACK && currState != ShootStates.SHOOT && 
        currDist <= distance && currDist > runDistance){
      regAni.resume();
      regAni.setSpeed((int)(6/RUN_SPEED));

      //DEBUG Labels
      System.out.println("Switch to Attack");
      currState = ShootStates.ATTACK;
    }

    switch (currState) {
      //Idle and Walk Animations run as a pair
      case IDLE:
        idleCount--;

        if(idleCount <= 0){
          idleCount = GEN.nextInt(30)+30;
          currState = ShootStates.WALK;
          regAni.resume();
          regAni.setSpeed((int)(6/WALK_SPEED));
        }
        break;
      case WALK:        
        walkCount--;

        if(walkCount <= 0){
          walkCount = GEN.nextInt(60)+60;
          direction = Face.random();
          currState = ShootStates.IDLE;
          regAni.stop();
        }

        switch (direction) {
          case DOWN: dy = -WALK_SPEED; break;
          case LEFT: dx = -WALK_SPEED; break;
          case RIGHT:dx =  WALK_SPEED; break;
          case UP:   dy =  WALK_SPEED; break;
          default:;
        }

        break;

      //Attack and Shoot work as a pair
      case ATTACK:
        float delX = targetX-x;
        float delY = targetY-y;

        if(Math.abs(delX) < 8 || Math.abs(delY) < 8){
          //DEBUG Labels
          System.out.println("Switch to SHoot"); 
          regAni.stop();
          currState = ShootStates.SHOOT;
        }

        else if(Math.abs(delX) < Math.abs(delY)){
          if(Math.signum(delX) == -1){
            dx = -RUN_SPEED;
          }
          else if(Math.signum(delX) == 1){
            dx = RUN_SPEED;
          }
        }

        else if(Math.abs(delY) < Math.abs(delX)){
          if(Math.signum(delY) == -1){
            dy = -RUN_SPEED;
          }
          else if(Math.signum(delY) == 1){
            dy = RUN_SPEED;
          }
        }

        break;
      case SHOOT:
        shootCount--;
        if(shootCount <= 0){
          
          shootCount = GEN.nextInt(60)+60;
        }
        
        break;
        
      case RUN:
        float triX = Math.abs(targetX-x);
        float triY = Math.abs(targetY-y);
        float theta = (float)Math.atan(triY/triX);

        if (targetX > x && triX > 12)
        {
          dx = -RUN_SPEED*(float)Math.cos(theta);
        } else if (targetX < x)
        {
          dx = RUN_SPEED*(float)Math.cos(theta);
        }

        if (targetY > y && triY > 12)
        {
          dy = -RUN_SPEED*(float)Math.sin(theta);
        } else if (targetY < y)
        {
          dy = RUN_SPEED*(float)Math.sin(theta);
        }
        break;
      case TIRED:
        break;
      default:
        break;
    }

    super.update();

  }


  @Override
  public void render() {
    //TODO Better implementation
    if(EditorMap.getMode() == Modes.EVENT){
      Draw.circ(x, y, distance, red, blue, green, 64);
      Draw.circ(x, y, runDistance, red, blue, green, 64);

    }
    if(Map.isHitBox){
      Draw.circCam(x, y, distance, red, blue, green, 64);
      Draw.circCam(x, y, runDistance, red, blue, green, 64);
    }

    super.render();
  }

  @Override
  public int xpPerLevel() {
    return 20;
  }

  @Override
  public int baseAttack() {
    return baseAttack;
  }

  public int baseMagicAttack() {
    return baseMagicAttack;
  }

  @Override
  public int baseDefense() {
    return baseDefense;
  }

  @Override
  public int baseHealth() {
    return baseHealth;
  }

  public int baseMagic() {
    return baseMagic;
  }

  @Override
  public String getBehavior(){
    return "shooter";
  }


}
