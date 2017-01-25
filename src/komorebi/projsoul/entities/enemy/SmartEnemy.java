/**
 * SmartEnemy.java     Nov 23, 2016, 8:52:55 AM
 */
package komorebi.projsoul.entities.enemy;

import komorebi.projsoul.ai.SquareGrid.Location;
import komorebi.projsoul.ai.WeightedSquareGrid;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.map.Map;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A smart enemy that can go everywhere no prob
 *
 * @author Aaron Roy
 */
public class SmartEnemy extends Chaser {

  private float targetX, targetY;

  private float nextX, nextY;
  private int rtx, rty;           //Current tile x
  private int ptx, pty, ctx, cty; //Player tile x
  private ArrayList<Location> path = new ArrayList<Location>();
  private WeightedSquareGrid grid;
  
  private boolean fineMode = false; //Represents if the enemy is searching with tiles or pixels

  protected static final float speed = 1f;

  /**
   * Creates a smart enemy that can move around obstacles
   * 
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param type The sprite of this enemy
   * @param radius The maximum distance the enemy can be from the player
   *      and still chase him/her
   */
  public SmartEnemy(float x, float y, EnemyType type, int radius, boolean[][] collision) {
    this(x, y, type, radius, collision, 1);
  }


  /**
   * Creates a smart enemy that can move around obstacles
   * 
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param type The sprite of this enemy
   * @param radius The maximum distance the enemy can be from the player
   *      and still chase him/her
   * @param level The level of this enemy
   */
  public SmartEnemy(float x, float y, EnemyType type, int radius, 
      boolean[][] collision, int level) {
    super(x, y, type, radius, level);

    regAni.setSpeed((int)(15/speed));
    
    grid = new WeightedSquareGrid(collision);
  }

  @Override
  public void update(){        
    ptx = ctx;
    pty = cty;

    targetX = Map.getPlayer().getX();
    targetY = Map.getPlayer().getY();

    ctx = (int)targetX/16;
    cty = (int)targetY/16;

    rtx = (int)(x/16);
    rty = (int)(y/16);

    if (Map.distanceBetween(x,y,targetX,targetY) > maxPlayDist && 
        (dx != 0 || dy != 0)){
      dx = 0;
      dy = 0;
      regAni.stop();
    }

    if(!hurt && Map.distanceBetween(x, y, targetX,targetY) <= maxPlayDist && 
        (ptx != ctx || pty != cty)){
      HashMap<Location, Location> cameFrom = new HashMap<Location, Location>();
      HashMap<Location, Double> costSoFar = new HashMap<Location, Double>();

      Location start = new Location(rtx, rty);
      Location goal = new Location(ctx, cty);

      grid.aStarSearch(start, goal, cameFrom, costSoFar);

      path = grid.reconstructPath(start, goal, cameFrom);
      fineMode = false;

      //DEBUG Draw Grid
//      grid.drawGrid(1, start, goal, null, null, path);
      
      regAni.resume();

    }

    if(!path.isEmpty() && new Location(rtx, rty).equals(path.get(0))){
      Location prev = path.remove(0);

      if(!path.isEmpty()){
        Location curr = path.get(0);

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


        //DEBUG Draw Location and Grid
//        System.out.println(nextX + ", " + nextY);
//        System.out.println(path.get(0).x*16 + ", " + path.get(0).y*16);

//        System.out.println(path.size());

        grid.drawGrid(1, new Location(rtx, rty), new Location(ctx, cty), null, null, path);
      }else{
        nextX = targetX;
        nextY = targetY;
        fineMode = true;
      }
      
      //DEBUG Draw Location and Grid
//      System.out.println(nextX + ", " + nextY);
      if(!path.isEmpty()){
//        System.out.println("Calc Loc : " + path.get(0).x*16 + ", " + path.get(0).y*16);
      }
    }

    if (!hurt && Map.distanceBetween(x,y,targetX,targetY) <= maxPlayDist)
    {   
      /*
      float ex, ey;
      
      if(fineMode){
        ex = x;
        ey = y;
      }else{
        ex = rtx*16;
        ey = rty*16;
      }
      
      float triX = Math.abs(nextX-ex);
      float triY = Math.abs(nextY-ey);
      float theta = (float)Math.atan(triY/triX);
      
      if (nextX > ex)
      {
        dx = speed*(float)Math.cos(theta);
      } else if (nextX < ex)
      {
        dx = -speed*(float)Math.cos(theta);
      }else{
        dx = 0;
      }

      if (nextY > ey)
      {
        dy = speed*(float)Math.sin(theta);
      } else if (nextY < ey)
      {
        dy = -speed*(float)Math.sin(theta);
      }else {
        dy = 0;
      }
      */
      
      float triX = Math.abs(nextX-x);
      float triY = Math.abs(nextY-y);
      float theta = (float)Math.atan(triY/triX);
      
      if (nextX > x)
      {
        dx = speed*(float)Math.cos(theta);
      } else if (nextX < x)
      {
        dx = -speed*(float)Math.cos(theta);
      }else{
        dx = 0;
      }

      if (nextY > y)
      {
        dy = speed*(float)Math.sin(theta);
      } else if (nextY < y)
      {
        dy = -speed*(float)Math.sin(theta);
      }else {
        dy = 0;
      }

      
      /*
      if (nextX > x)
      {
        dx = speed;
      } else if (nextX < x)
      {
        dx = -speed;
      }else{
        dx = 0;
      }

      if (nextY > y)
      {
        dy = speed;
      } else if (nextY < y)
      {
        dy = -speed;
      }else {
        dy = 0;
      }
      */
      
      //DEBUG Smart Enemy Velocity, Key.V
      if(KeyHandler.keyClick(Key.V)){
        System.out.println("Velocity   : " + dx + ", " + dy);
        System.out.println("Trig Stuff : " + triX + ", " + triY + ", " + theta);
        
        System.out.println("Next Loc   : " + nextX + ", " + nextY);
        if(!path.isEmpty()){
          System.out.println("NextPath   : " + path.get(0).x*16 + ", " + path.get(0).y*16);
        }
        System.out.println("Enemy Grid : " + rtx*16 + ", " + rty*16);
        System.out.println("Enemy Pix  : " + x + ", " + y);
        System.out.println("Fine Mode? : " + fineMode);
        System.out.println();
        
      }

    }


    enemyUpdate();
  }
  
  public void render(){
    Draw.rectCam(nextX, nextY, 16, 16, 17, 16, 18, 17, 2); //Green
    Draw.rectCam(rtx*16, rty*16, 16, 16, 18, 16, 19, 17, 2); //Blue
    if(!path.isEmpty()){ //Red
      Draw.rectCam(path.get(0).x*16,path.get(0).y*16, 16, 16, 16, 16, 17, 17, 2);
    }
    super.render();
  }
  
}
