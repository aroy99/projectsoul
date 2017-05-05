/**
 * SmartEnemy.java     Nov 23, 2016, 8:52:55 AM
 */
package komorebi.projsoul.entities.enemy;

import komorebi.projsoul.ai.Location;
import komorebi.projsoul.ai.WeightedSquareGrid;
import komorebi.projsoul.ai.node.composite.MemSequence;
import komorebi.projsoul.ai.node.composite.Priority;
import komorebi.projsoul.ai.node.composite.Sequence;
import komorebi.projsoul.ai.node.decorator.Inverter;
import komorebi.projsoul.ai.node.decorator.Succeeder;
import komorebi.projsoul.ai.node.leaf.IdleBehavior;
import komorebi.projsoul.ai.node.leaf.MoveToTarget;
import komorebi.projsoul.ai.node.leaf.RecalculatePath;
import komorebi.projsoul.ai.node.leaf.SetTargetToNextInPath;
import komorebi.projsoul.ai.node.leaf.SetTargetToPlayer;
import komorebi.projsoul.ai.node.leaf.WalkBehavior;
import komorebi.projsoul.ai.node.leaf.conditions.IsPlayerInRange;
import komorebi.projsoul.ai.node.leaf.conditions.IsTargetInSameTile;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.map.MapHandler;

import java.util.ArrayList;

/**
 * A smart enemy that can go everywhere no prob
 *
 * @author Aaron Roy
 */
public class SmartEnemy extends Chaser {

  private float nextX, nextY;
  private int rtx, rty;           //Current tile x
  private int ptx, pty, ctx, cty; //Player tile x
  private ArrayList<Location> path = new ArrayList<Location>();
  private static WeightedSquareGrid grid;

  private static final int MAX_IDLE = 60;
  private static final int MAX_WALK = 120;
  
  private static final int CLOSE_ENOUGH = 4;


  private boolean fineMode = false; //Represents if the enemy is searching with tiles or pixels

  protected static final float SPEED = 1f;

  Priority root;

  /**
   * Creates a smart enemy that can move around obstacles
   * 
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param type The sprite of this enemy
   * @param radius The maximum distance the enemy can be from the player
   *      and still chase him/her
   */
  public SmartEnemy(float x, float y, EnemyType type, int distanceFromPlay) {
    this(x, y, type, distanceFromPlay, 1);
  }


  /**
   * Creates a smart enemy that can move around obstacles
   * 
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param type The sprite of this enemy
   * @param distanceFromPlay The maximum distance the enemy can be from the player
   *      and still chase him/her
   * @param level The level of this enemy
   */
  public SmartEnemy(float x, float y, EnemyType type, int distanceFromPlay, int level) {
    super(x, y, type, distanceFromPlay, level);

    regAni.setSpeed((int)(15/SPEED));
  }
  
  /**
   * Does the rest of the initialization for the SmartEnemy, since it needs
   * additional information that is created after enemies get loaded
   */
  public void initGrid(){
    
    if(grid == null){
      grid = new WeightedSquareGrid(MapHandler.getCollision());
    }
    //Refer to https://docs.google.com/drawings/d/1Ht_5zxyptG1DSIq-I9YlRkH7Walw8kxPpIkbin3Elz4/edit
    root = new Priority(
        new Sequence(
            new IsPlayerInRange(this, maxPlayDist),
            new Succeeder(
                new Sequence(
                    new Inverter(
                        new IsTargetInSameTile(this)
                        ),
                    new RecalculatePath(this, grid)
                    )
                ),
            new MoveToTarget(this, SPEED, CLOSE_ENOUGH),
            new Priority(
                new SetTargetToNextInPath(this),
                new SetTargetToPlayer(this)
                )
            ),
        new MemSequence(                             //Walk around
            new IdleBehavior(this, MAX_IDLE),
            new WalkBehavior(this, MAX_WALK, SPEED/2)
            )
        );

  }

  @Override
  public void update(){
    recalculateInstanceVariables();
    root.update();
    enemyUpdate();
  }


  private void recalculateInstanceVariables() {
    ptx = ctx;
    pty = cty;

    targetX = MapHandler.getPlayer().getX();
    targetY = MapHandler.getPlayer().getY();

    ctx = (int)targetX/16;
    cty = (int)targetY/16;

    rtx = (int)(x/16);
    rty = (int)(y/16);
  }


  @SuppressWarnings("unused")
  private void printFormatedParameters(float triX, float triY, float theta) {
    System.out.println("Velocity   : " + dx + ", " + dy);
    System.out.println("Trig Stuff : " + triX + ", " + triY + ", " + theta);

    System.out.println("Next Loc   : " + nextX + ", " + nextY);
    if(!path.isEmpty()){
      System.out.println("NextPath   : " + path.get(0).x*16 + ", " + path.get(0).y*16);
    }
    System.out.println("Enemy Grid : " + rtx*16 + ", " + rty*16);
    System.out.println("Enemy Pix  : " + x + ", " + y);
    System.out.println("Fine Mode? : " + fineMode);
    System.out.println("---------------------------------------------\n");
  }


  public void render(){
    Draw.rectCam(nextX, nextY, 16, 16, 17, 16, 18, 17, 2); //Green
    Draw.rectCam(rtx*16, rty*16, 16, 16, 18, 16, 19, 17, 2); //Blue
    if(!path.isEmpty()){ //Red
      Draw.rectCam(path.get(0).x*16,path.get(0).y*16, 16, 16, 16, 16, 17, 17, 2);
    }
    super.render();
  }


  public ArrayList<Location> getPath(){
    return path;
  }

  /**
   * @param path The new path for the enemy to follow
   */
  public void setPath(ArrayList<Location> path) {
    this.path = path;
  }

}
