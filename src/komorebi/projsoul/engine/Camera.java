/**
 * Camera.java		Aug 21, 2016, 9:01:33 PM
 */
package komorebi.projsoul.engine;

import komorebi.projsoul.map.Map;
import komorebi.projsoul.states.Game;

/**
 * Handles what's visible on the screen
 * 
 * @author Aaron Roy
 */
public class Camera{
  private static float x;
  private static float y;
    
  /**
   * Moves the camera by the specified amount
   * 
   * @param dx delta x
   * @param dy delta y
   */
  public static void move(float dx, float dy){
    boolean[] check = Game.getMap().checkBoundaries(x, y, dx, dy);
    
    if(check.length == 3){
      center(Map.getClyde().getX(), Map.getClyde().getY());
    }
    
    if(check[0]){
      x += dx;
    }
    if(check[1]){
      y += dy;
    }
  }
  
  /**
   * Moves the camera to the specified location
   * 
   * @param x new x
   * @param y new y
   */
  public static void setLoc(float x, float y){
    Camera.x = x;
    Camera.y = y;
  }

  /**
   * Centers the camera on clyde at the specified location
   * 
   * @param x new x
   * @param y new y
   */
  public static void center(float x, float y){
    Camera.x = x-128+8;
    Camera.y = y-112+12;
  }
  
  public static void center(float x, float y, int mapSx, int mapSy)
  {
    if (x-128+8>=0 && x-128+8<=mapSx-Main.WIDTH)
    {
      Camera.x = x-128+8;
    } else if (x-128+8<0)
    {
      Camera.x = 0;
    } else if (x-128+8>mapSx-Main.WIDTH)
    {
      Camera.x = mapSx-Main.WIDTH;
    }
    
    if (y-112+12>=0 && y-112+12<=mapSy-Main.HEIGHT)
    {
      Camera.y = y-112+12;
    } else if (y-112+12<0)
    {
      Camera.y = 0;
    } else if (y-112+12>mapSy-Main.HEIGHT)
    {
      Camera.y = mapSy - Main.HEIGHT;
    }
  }

  
  
  public static float getX() {
    return x;
  }

  public static float getY() {
    return y;
  }

  
}
