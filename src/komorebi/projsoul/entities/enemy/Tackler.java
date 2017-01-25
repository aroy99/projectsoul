/**
 * Tackler.java    Jan 23, 2017, 9:12:10 AM
 */
package komorebi.projsoul.entities.enemy;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.gameplay.Camera;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.EditorMap.Modes;
import komorebi.projsoul.map.Map;

import java.util.Random;

/**
 * 
 *
 * @author Aaron Roy
 */
public class Tackler extends Enemy {

  float targetX, targetY; //Location of the player
  float currDist;         //Calculated distance between this enemy and the player
  
  /**
   * The different states a tackler can be in
   *
   * @author Aaron Roy
   */
  public enum TackleStates{
    IDLE, WALK, LINE_UP, TACKLE, STUN, WAIT;
  }
  
  private TackleStates currState = TackleStates.IDLE;
  
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
  
  //Stats
  public static final int baseAttack = 25, baseDefense = 50, baseHealth = 250;

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
  }
  
  @Override
  public void update(){
    if(!hurt){
      dx = 0;
      dy = 0;

      targetX = Map.getPlayer().getX();
      targetY = Map.getPlayer().getY();

      currDist = Map.distanceBetween(x,y,targetX,targetY);

      if(currState != TackleStates.STUN && currState != TackleStates.WAIT){
        if (currState != TackleStates.IDLE && currState != TackleStates.WALK && 
            currDist > distance){
          regAni.stop();
          currState = TackleStates.IDLE;
        }

        if(currState != TackleStates.TACKLE && 
            currState != TackleStates.LINE_UP && currDist <= distance){
          regAni.resume();
          regAni.setSpeed((int)(6/RUN_SPEED));

          //DEBUG Labels
          System.out.println("Switch to Line UP");
          currState = TackleStates.LINE_UP;
        }
      }

      switch (currState) {
        //Idle and Walk Animations run as a pair
        case IDLE:
          idleCount--;

          if(idleCount <= 0){
            idleCount = GEN.nextInt(30)+30;
            currState = TackleStates.WALK;
            regAni.resume();
            regAni.setSpeed((int)(6/WALK_SPEED));
          }
          break;
        case WALK:        
          walkCount--;

          if(walkCount <= 0){
            walkCount = GEN.nextInt(60)+60;
            direction = Face.random();
            currState = TackleStates.IDLE;
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

        //Attack and Tackle work as a pair
        case LINE_UP:
          float delX = targetX-x;
          float delY = targetY-y;

          if(Math.abs(delX) < 8 || Math.abs(delY) < 8){
            //DEBUG Labels
            System.out.println("Switch to TackLe");
            regAni.setSpeed((int)(6/TACKLE_SPEED));
            invincible = true;
            currState = TackleStates.TACKLE;
          }

          else if(Math.abs(delX) < Math.abs(delY)){
            if(delX < 0){
              dx = -RUN_SPEED;
            }
            else if(delX > 0){
              dx = RUN_SPEED;
            }

            if (delY < 0) {
              direction = Face.DOWN;
            }
            else if (delY > 0) {
              direction = Face.UP;
            }
          }

          else if(Math.abs(delY) < Math.abs(delX)){
            if(delY < 0){
              dy = -RUN_SPEED;
            }
            else if(delY > 0){
              dy = RUN_SPEED;
            }

            if (delX < 0) {
              direction = Face.LEFT;
            }
            else if (delX > 0) {
              direction = Face.RIGHT;
            }
          }

          break;
        case TACKLE:
          
          float dist = 0;
          switch (direction) {
            case DOWN:  dist = targetY - y; break;
            case LEFT:  dist = targetX - x; break; 
            case RIGHT: dist = x - targetX; break; 
            case UP:    dist = y - targetY; break; 
            default:
          }
          if(dist > 48){
            invincible = false;
            regAni.resume();
            regAni.setSpeed((int)(6/RUN_SPEED));
            currState = TackleStates.LINE_UP;
          }
                    
          if(hitWall){
            invincible = false;
            stunCount = MAX_STUN;
            Camera.shake(8, 2, 1);
            regAni.hStop();
            currState = TackleStates.STUN;
          }
          
          if(hitPlayer){
            waitCount = MAX_WAIT;
            regAni.hStop();
            currState = TackleStates.WAIT;
          }
          
          switch (direction)
          {
            case DOWN:
              dy = -TACKLE_SPEED;
              break;
            case LEFT:
              dx = -TACKLE_SPEED;
              break;
            case RIGHT:
              dx = TACKLE_SPEED;
              break;
            case UP:
              dy = TACKLE_SPEED;
              break;
            default:
              break;
          }
          break;
        case STUN:
          stunCount--;
          if(stunCount <= 0){
            regAni.resume();
            regAni.setSpeed((int)(6/RUN_SPEED));
            currState = TackleStates.LINE_UP;
          }
          break;
        case WAIT:
          waitCount--;
          if(waitCount <= 0){
            invincible = false;
            regAni.resume();
            regAni.setSpeed((int)(6/RUN_SPEED));
            currState = TackleStates.LINE_UP;
          }
          break;
        default:
          break;

      }
    }

    super.update();

  }
  
  @Override
  public void render() {
    //TODO Better implementation
    if(EditorMap.getMode() == Modes.EVENT){
      Draw.circ(x, y, distance, red, blue, green, 64);

    }
    if(Map.isHitBox){
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
