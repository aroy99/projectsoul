/**
 * SmartEnemy.java     Nov 23, 2016, 8:52:55 AM
 */
package komorebi.projsoul.entities.enemy;

import komorebi.projsoul.ai.SquareGrid.Location;
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
import komorebi.projsoul.map.Map;

import java.util.ArrayList;
import java.util.HashMap;

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
  private WeightedSquareGrid grid;

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
  public SmartEnemy(float x, float y, EnemyType type, int distanceFromPlay, boolean[][] collision) {
    this(x, y, type, distanceFromPlay, collision, 1);
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
  public SmartEnemy(float x, float y, EnemyType type, int distanceFromPlay, 
      boolean[][] collision, int level) {
    super(x, y, type, distanceFromPlay, level);

    regAni.setSpeed((int)(15/SPEED));

    grid = new WeightedSquareGrid(collision);

    
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

    /**
    if (Map.distanceBetween(x,y,targetX,targetY) > maxPlayDist && 
        (dx != 0 || dy != 0)){
      dx = 0;
      dy = 0;
      regAni.stop();
    }

    if(!hurt && Map.distanceBetween(x, y, targetX,targetY) <= maxPlayDist && 
        (ptx != ctx || pty != cty)){
      calculatePath();
      regAni.resume();

    }

    if(!path.isEmpty() && new Location(rtx, rty).equals(path.get(0))){
      Location prev = path.remove(0);

      if(!path.isEmpty()){
        Location curr = path.get(0);
        decideNextTarget(prev, curr);
      }else{
        nextX = targetX;
        nextY = targetY;
        fineMode = true;
      }
    }

    if (!hurt && Map.distanceBetween(x,y,targetX,targetY) <= maxPlayDist)
    {      
      float triX = Math.abs(nextX-x);
      float triY = Math.abs(nextY-y);
      float theta = (float)Math.atan(triY/triX);

      moveToTarget(theta);

      //DEBUG Smart Enemy Velocity, Key.V
      if(KeyHandler.keyClick(Key.V)){
        printFormatedParameters(triX, triY, theta);
      }

    }
     **/

    enemyUpdate();
  }


  private void recalculateInstanceVariables() {
    ptx = ctx;
    pty = cty;

    targetX = Map.getPlayer().getX();
    targetY = Map.getPlayer().getY();

    ctx = (int)targetX/16;
    cty = (int)targetY/16;

    rtx = (int)(x/16);
    rty = (int)(y/16);
  }


  private void calculatePath() {
    HashMap<Location, Location> cameFrom = new HashMap<Location, Location>();
    HashMap<Location, Double> costSoFar = new HashMap<Location, Double>();

    Location start = new Location(rtx, rty);
    Location goal = new Location(ctx, cty);

    grid.aStarSearch(start, goal, cameFrom, costSoFar);

    path = grid.reconstructPath(start, goal, cameFrom);
    fineMode = false;
  }


  private void decideNextTarget(Location prev, Location curr) {
    if(curr.x > prev.x){
      nextX = curr.x*16+8;
    }else if(curr.x < prev.x){
      nextX = curr.x*16-8;
      curr = path.set(0, new Location(curr.x-1, curr.y));
    }else {
      nextX = curr.x*16;
    }

    if(curr.y > prev.y){
      nextY = curr.y*16+8;
    }else if(curr.y < prev.y){
      nextY = curr.y*16-8;
      curr = path.set(0, new Location(curr.x, curr.y-1));
    }else {
      nextY = curr.y*16;
    }
  }


  private void moveToTarget(float theta) {
    if (nextX > x)
    {
      dx = SPEED*(float)Math.cos(theta);
    } else if (nextX < x)
    {
      dx = -SPEED*(float)Math.cos(theta);
    }else{
      dx = 0;
    }

    if (nextY > y)
    {
      dy = SPEED*(float)Math.sin(theta);
    } else if (nextY < y)
    {
      dy = -SPEED*(float)Math.sin(theta);
    }else {
      dy = 0;
    }
  }


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
