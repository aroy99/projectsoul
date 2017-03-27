/**
 * Mode.java    Aug 4, 2016, 6:21:09 PM
 */
package komorebi.projsoul.editor.modes;

import static komorebi.projsoul.engine.KeyHandler.controlDown;

import komorebi.projsoul.editor.Palette;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MainE;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.engine.Renderable;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.TileList;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.TextHandler;

import org.lwjgl.input.Mouse;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.logging.Handler;

/**
 * Represents one of the three modes for editing in Clyde's
 * 
 * @author Aaron Roy
 */
public abstract class Mode implements Renderable{
  
  protected static boolean lButtonIsDown, lButtonWasDown;//Left Button Clicked
  protected static boolean rButtonIsDown, rButtonWasDown;//Right Button Clicked
  protected static boolean mButtonIsDown, mButtonWasDown;//Middle Button Pressed
  
  protected static TileList[][] tiles;

  
  protected static boolean mouseSame;                    //Mouse is in same pos as last frame

  protected static int mx, my;            //To track mouse position
  protected static int pmx, pmy;            //To track previous mouse position
  
  protected static int clickTimer;
  protected static final int DOUBLE_CLICK_TIME = 20;
  protected static boolean lButtonDoubleClicked;
  
  protected static boolean rStartDragging, rIsDragging;//Starting a group selection
  protected static boolean lStartDragging, lIsDragging;//Is making a group selection
  protected static boolean isSelection;                //A selection is active

  protected static int initX, initY; //Location at the beginning of a drag

  public static final int SIZE = 16;         //Width and height of a tile
  
  protected static TextHandler status = new TextHandler();
    
  /**
   * Gets input in a static way
   */
  public static void getModeInput(){    
    mouseSame = getMouseX() == mx && getMouseY() == my &&
        (lButtonIsDown || rButtonIsDown);
        
    pmx = mx;
    mx = getMouseX();
    
    pmy = my;
    my = getMouseY();
    
    status.clear();
    status.write("Mouse location: " + mx + ", " + my, 50, 1, new EarthboundFont(1));
//    System.out.println("Mouse location: " + mx + ", " + my);
//    System.out.println("P-Mouse location: " + pmx + ", " + pmy + ", " + mouseSame);

    
    lButtonWasDown = lButtonIsDown;
    lButtonIsDown = Mouse.isButtonDown(0);
    
    if(lButtonIsDown && !lButtonWasDown && clickTimer == 0){
      clickTimer = DOUBLE_CLICK_TIME;
    }

    if(lButtonIsDown && !lButtonWasDown && clickTimer > 0 && 
        clickTimer != DOUBLE_CLICK_TIME){
      System.out.println("Double click");
      lButtonDoubleClicked = true;
      clickTimer = 0;
    }else{
      lButtonDoubleClicked = false;
    }
    
    if(clickTimer > 0){
      clickTimer--;
    }
    

        
    rButtonWasDown = rButtonIsDown;
    rButtonIsDown = Mouse.isButtonDown(1) && !controlDown();

    
    mButtonWasDown = mButtonIsDown;
    mButtonIsDown = KeyHandler.keyClick(Key.MBUTTON);

    rStartDragging = Mouse.isButtonDown(1) && controlDown() && 
        !rIsDragging;

    rIsDragging = Mouse.isButtonDown(1) && rIsDragging || rStartDragging;

    lStartDragging = Mouse.isButtonDown(0) && controlDown() && 
        !lIsDragging;

    lIsDragging = Mouse.isButtonDown(0) && lIsDragging || lStartDragging;
  }
  
  /**
   * Renders the status bar, containing:
   * -Tile Mouse X and Y
   *
   */
  public static void renderStatus(){
    status.render();
  }
  
  /**
   * Converts Mouse X into a tile index, adjusting for map position
   * @return adjusted mouse x
   */
  protected static int getMouseX(){
    return ((Mouse.getX()/MainE.getScale())-(int)EditorMap.getX())/(16);
  }

  /**
   * Converts Mouse Y into a tile index, adjusting for map position
   * @return adjusted mouse y
   */
  protected static int getMouseY() {
    return ((Mouse.getY()/MainE.getScale())-(int)EditorMap.getY())/(16);
  }

  /**
   * Checks if the Mouse is in bounds of the map
   * @return Mouse is in map
   */
  protected static boolean checkMapBounds() {
    return (Mouse.getX()/MainE.getScale() < Palette.xOffset*16 ||
        Mouse.getY()/MainE.getScale() < Palette.yOffset*16) &&
        (getMouseY() >= 0 &&
        getMouseY() < tiles.length &&
        getMouseX() >= 0 &&
        getMouseX() < tiles[0].length);
  }
  
  public static void setMap(TileList[][] map){
    tiles = map;
  }
  
  /**
   * Checks if the tile is valid
   * 
   * @param tx the X index of the tile
   * @param ty the Y index of the tile
   * @return true if the tile is in bounds
   */
  protected boolean checkTileBounds(int tx, int ty) {
    return ty >= 0 &&           
        ty < tiles.length && 
        tx >= 0 &&           
        tx < tiles[0].length;
  }


}
