/**
 * Camera.java    Aug 21, 2016, 9:01:33 PM
 */
package komorebi.projsoul.gameplay;

import komorebi.projsoul.engine.Main;
import komorebi.projsoul.entities.Face;
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
  
  private static Face previous = Face.UP;
  private static Face current = Face.DOWN;
  private static int shakeCount;
  private static int step = 1;
  private static float intensity = 1;
  
    
  /**
   * Moves the camera by the specified amount
   * 
   * @param dx delta x
   * @param dy delta y
   */
  public static void move(float dx, float dy){
    boolean[] check = Game.getMap().checkBoundaries(x, y, dx, dy);
    
    if(check.length == 3){
      return;
    }
    
    if(check[0]){
      x += dx;
    }
    if(check[1]){
      y += dy;
    }
  }
  
  /**
   * Updates the camera's variables and effects
   */
  public static void update(){
        
    if(shakeCount >= 0){
      
      if(shakeCount % step == 0){
      
        center(Map.getPlayer().getX(), Map.getPlayer().getY());

        switch (current) {
          case DOWN:
            y-=intensity;
            break;
          case LEFT:
            x-=intensity;
            break;
          case RIGHT:
            x+=intensity;
            break;
          case UP:
            y+=intensity;
            break;
          default:
            System.out.println("shake failed");
            break;
        }

        if(current == previous || current.opposite() == previous){
          previous = current;
          current = current.rotate();
        }else{
          previous = current;
          current = current.opposite();
        }
      }
      
      shakeCount-=1;
      
    }else{
      center(Map.getPlayer().getX(), Map.getPlayer().getY());
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
   * Centers the camera on the player at the specified location
   * 
   * @param x new x
   * @param y new y
   */
  public static void center(float x, float y){
    Camera.x = x-128+8;
    Camera.y = y-112+12;
  }
  
  
  /**
   * Centers the camera on the player unless the player is near the edge of the
   * map, in which case the camera centers as closely to the player's center as
   * it can without exceeding the bounds of the map.
   * @param x The player's current x
   * @param y The player's current y
   * @param mapSx The horizontal size of the map (in pixels)
   * @param mapSy The vertical size of the map (in pixels)
   */
  public static void center(float x, float y, int mapSx, int mapSy)
  {
    if (x-128+8 >= 0 && x-128+8 <= mapSx-Main.WIDTH)
    {
      Camera.x = x-128+8;
    } else if (x-128+8 < 0)
    {
      Camera.x = 0;
    } else if (x-128+8 > mapSx-Main.WIDTH)
    {
      Camera.x = mapSx-Main.WIDTH;
    }
    
    if (y-112+12 >= 0 && y-112+12 <= mapSy-Main.HEIGHT)
    {
      Camera.y = y-112+12;
    } else if (y-112+12 < 0)
    {
      Camera.y = 0;
    } else if (y-112+12 > mapSy-Main.HEIGHT)
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
  
  /**
   * Shakes the camera
   * 
   * @param frames How many frames to shake the camera
   * @param intensity How far to shake it
   * @param step Which frames to shake it
   */
  public static void shake(int frames, float intensity, int step){
    shakeCount = frames;
    Camera.intensity = intensity;
    Camera.step = step;
  }

  
}
