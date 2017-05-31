/**
 * CollisionDetector.java    May 16, 2017, 9:35:07 AM
 */
package komorebi.projsoul.engine;

import static komorebi.projsoul.map.Map.SIZE;

import komorebi.projsoul.map.MapHandler;

/**
 * Handles collision detection
 *
 * @author Aaron Roy
 */
public class CollisionDetector {
  
  private static int lowX, lowY;
  private static boolean[][] collision;
  
  public static void initialize(int lowX, int lowY, boolean[][] collision){
    CollisionDetector.lowX = lowX;
    CollisionDetector.lowY = lowY;
    CollisionDetector.collision = collision;
  }
  
  //TODO Move to a Physics class
  /**
   * Checks the collisions between all four points of the character
   * 
   * @param x Player's X
   * @param y Player's Y
   * @param dx Delta x of Player
   * @param dy Delta y of Player
   * @return {Never, Eat, Slimy, Worms}
   */
  public static boolean[] checkCollisions(float x, float y, float dx, float dy){
    
    int xLIncSpd, xRIncSpd, xRNotIncSpd, xLNotIncSpd;
    int yBIncSpd, yTIncSpd, yBNotIncSpd, yTNotIncSpd;
    
    if(x+dx >= 0){
      //Speed affected
      xLIncSpd = (int)((x+dx)/SIZE)-lowX; //Left
      xRIncSpd = (int)((x+15+dx)/SIZE)-lowX;  //Right

      //Speed Unaffected
      xLNotIncSpd = (int)((x)/SIZE)-lowX; //Left
      xRNotIncSpd = (int)((x+15)/SIZE)-lowX;  //Right
    }else{
      //Speed affected
      xLIncSpd = (int)((x-15+dx)/SIZE)-lowX; //Left
      xRIncSpd = (int)((x+dx)/SIZE)-lowX;  //Right

      //Speed Unaffected
      xLNotIncSpd = (int)((x-15)/SIZE)-lowX; //Left
      xRNotIncSpd = (int)((x)/SIZE)-lowX;  //Right
    }
    
    if(y+dx >= 0){
      //Speed affected
      yBIncSpd = (int)((y+dy)/SIZE)-lowY; //Bottom
      yTIncSpd = (int)((y+15+dy)/SIZE)-lowY;  //Top

      //Speed Unaffected
      yBNotIncSpd = (int)((y)/SIZE)-lowY; //Bottom
      yTNotIncSpd = (int)((y+15)/SIZE)-lowY;  //Top
    }else{
      //Speed affected
      yBIncSpd = (int)((y-15+dy)/SIZE)-lowY; //Bottom
      yTIncSpd = (int)((y+dy)/SIZE)-lowY;  //Top

      //Speed Unaffected
      yBNotIncSpd = (int)((y-15)/SIZE)-lowY; //Bottom
      yTNotIncSpd = (int)((y)/SIZE)-lowY;  //Top
    }
    
    int bufX = Math.abs(xLIncSpd*SIZE - (int) (x +dx));
    int bufY = Math.abs(yBIncSpd*SIZE - (int) (y +dy));   
    
    boolean[] ret = new boolean[4];
        
    ret[1] = xRIncSpd < collision[0].length-1; //East
    ret[3] = xLIncSpd >= 0; //West
    ret[0] = yTIncSpd < collision.length-1; //North
    ret[2] = yBIncSpd >= 0; //South

    ret[0] = ret[0] && collision[yTIncSpd][xLNotIncSpd] &&
                        collision[yTIncSpd][xRNotIncSpd];  //North
    
    ret[2] = ret[2] && collision[yBIncSpd][xLNotIncSpd] &&
                        collision[yBIncSpd][xRNotIncSpd];  //South

    ret[1] = ret[1] && collision[yBNotIncSpd][xRIncSpd] && 
                        collision[yTNotIncSpd][xRIncSpd];  //East
    
    ret[3] = ret[3] && collision[yBNotIncSpd][xLIncSpd] && 
                        collision[yTNotIncSpd][xLIncSpd];  //West

    //DEBUG Show collision values
    if(KeyHandler.keyClick(Key.Q)){
      System.out.println(xLIncSpd + ", " + xRIncSpd + ", " + yBIncSpd + ", " + yTIncSpd);
      System.out.println("dx: " + dx + ", dy: " + dy + "\n" + 
          collision[yTIncSpd][xLIncSpd]+ ", " +collision[yTIncSpd][xRIncSpd]+ ", \n" +
          collision[yBIncSpd][xLIncSpd]+ ", " +collision[yBIncSpd][xRIncSpd]);
      System.out.println("Never: " + ret[0] + ", Eat: " + ret[1] + 
          ", Slimy: " + ret[2] + ", Worms: " + ret[3]);
    }

    if (KeyHandler.keyClick(Key.B))
    {
      System.out.println(collision[yTIncSpd][xLNotIncSpd]);
      System.out.println(collision[yTIncSpd][xRNotIncSpd]);
    }

    return ret;
  }
  
  public static void guidePlayer(float x, float y, float dx, float dy)
  {
    
    int xLIncSpd, xRIncSpd, xRNotIncSpd, xLNotIncSpd;
    int yBIncSpd, yTIncSpd, yBNotIncSpd, yTNotIncSpd;
    
    int sx = 15;
    int sy = 15;
    
    if(x+dx >= 0){
      //Speed affected
      xLIncSpd = (int)((x+dx)/SIZE)-lowX; //Left
      xRIncSpd = (int)((x+sx+dx)/SIZE)-lowX;  //Right

      //Speed Unaffected
      xLNotIncSpd = (int)((x)/SIZE)-lowX; //Left
      xRNotIncSpd = (int)((x+sx)/SIZE)-lowX;  //Right
    }else{
      //Speed affected
      xLIncSpd = (int)((x-sx+dx)/SIZE)-lowX; //Left
      xRIncSpd = (int)((x+dx)/SIZE)-lowX;  //Right

      //Speed Unaffected
      xLNotIncSpd = (int)((x-sx)/SIZE)-lowX; //Left
      xRNotIncSpd = (int)((x)/SIZE)-lowX;  //Right
    }
    
    if(y+dy >= 0){
      //Speed affected
      yBIncSpd = (int)((y+dy)/SIZE)-lowY; //Bottom
      yTIncSpd = (int)((y+sy+dy)/SIZE)-lowY;  //Top

      //Speed Unaffected
      yBNotIncSpd = (int)((y)/SIZE)-lowY; //Bottom
      yTNotIncSpd = (int)((y+sy)/SIZE)-lowY;  //Top
    }else{
      //Speed affected
      yBIncSpd = (int)((y-sy+dy)/SIZE)-lowY; //Bottom
      yTIncSpd = (int)((y+dy)/SIZE)-lowY;  //Top

      //Speed Unaffected
      yBNotIncSpd = (int)((y-sy)/SIZE)-lowY; //Bottom
      yTNotIncSpd = (int)((y)/SIZE)-lowY;  //Top
    }
    
    int bufLX = Math.abs((xLIncSpd + lowX)*SIZE - (int) (x +dx));
    int bufRX = Math.abs((xRIncSpd + lowX)*SIZE - (int) (x +dx));

    int bufBY = Math.abs((yBIncSpd + lowY)*SIZE - (int) (y +dy));
    int bufTY = Math.abs((yTIncSpd + lowY)*SIZE - (int) (y +dy));
    
    boolean[] ret = new boolean[4];

    if(!MapHandler.isOutside()){
      ret[1] = xRIncSpd < collision[0].length; //East
      ret[3] = xLIncSpd-1 >= 0; //West
      ret[0] = yTIncSpd < collision.length; //North
      ret[2] = yBIncSpd-1 >= 0; //South
    }else{
      for (int i = 0; i < ret.length; i++) {
        ret[i] = true;
      }
    }


    //If player is moving into a wall but his sprite doesn't completely touch the wall
    int guideTolerance = 6;
    
    if (KeyHandler.keyClick(Key.T))
    {
      System.out.format("BuffLX: %d, BuffRX: %d\nBuffBY: %d, BuffTY: %d\n", 
          bufLX, bufRX, bufBY, bufTY);
      System.out.println(xLIncSpd*SIZE);
    }

    
    if (ret[0] && (collision[yTIncSpd][xLNotIncSpd] ^ collision[yTIncSpd][xRNotIncSpd])) {
      
      // Player moving up
      if (collision[yTIncSpd][xLNotIncSpd] && (SIZE - bufLX) >= guideTolerance) {
        MapHandler.getPlayer().guide(-1, 0);
      } else if (collision[yTIncSpd][xRNotIncSpd] && bufLX >= guideTolerance) {
        MapHandler.getPlayer().guide(1, 0);
      }
    }else if (ret[2] && (collision[yBIncSpd][xLNotIncSpd] ^ collision[yBIncSpd][xRNotIncSpd])) {
      
      // Player moving down
      if (collision[yBIncSpd][xLNotIncSpd] && (SIZE - bufLX) >= guideTolerance) {
        MapHandler.getPlayer().guide(-1, 0);
      } else if (collision[yBIncSpd][xRNotIncSpd] && bufLX >= guideTolerance) {
        MapHandler.getPlayer().guide(1, 0);
      }
    }else if (ret[1] && (collision[yBNotIncSpd][xRIncSpd] ^ collision[yTNotIncSpd][xRIncSpd])) {
      
      // Player moving right
      if (collision[yBNotIncSpd][xRIncSpd] && (bufTY >= guideTolerance)) {
        MapHandler.getPlayer().guide(0, -1);
      } else if (collision[yTNotIncSpd][xRIncSpd] && (SIZE - bufTY) >= guideTolerance) {
        MapHandler.getPlayer().guide(0, 1);
      }

    }else if (ret[3] && (collision[yBNotIncSpd][xLIncSpd] ^ collision[yTNotIncSpd][xLIncSpd])) {
      
      // Player moving left
      if (collision[yBNotIncSpd][xLIncSpd] && (bufTY >= guideTolerance)) {
        MapHandler.getPlayer().guide(0, -1);
      } else if (collision[yTNotIncSpd][xLIncSpd] && (SIZE - bufTY) >= guideTolerance) {
        MapHandler.getPlayer().guide(0, 1);
      }
    }
     
  }

}
