/**
 * Palette.java     May 22, 2016, 2:23:43 PM
 */




/**
 * Palette.java     May 22, 2016, 2:23:43 PM
 */

package komorebi.projsoul.editor;

import static komorebi.projsoul.engine.KeyHandler.controlDown;

import komorebi.projsoul.editor.modes.TileMode;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.MainE;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.TileList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;


/**
 * The current palette to choose from
 * 
 * @author Aaron Roy
 */
public class Palette implements Playable{

  private boolean scripting;

  public static final int HEIGHT = 32;
  public static final int WIDTH = 8;


  public static final int SIZE = 16;  //Width and height of a tile

  //Holds current palette tiles
  private TileList[][] tiles = new TileList[HEIGHT][WIDTH];

  /**Offset of palette in tiles*/
  public static int xOffset = Display.getWidth() / (MainE.scale * 16) - WIDTH;
  public static int yOffset = 0;

  //Selector X and Y, in tiles
  private int selX = xOffset;
  private int selY = yOffset + HEIGHT - 1;

  //The Selector itself
  private Animation selection;

  //Removes repeated input
  private boolean lButtonWasDown, lButtonIsDown; //Left Click
  private boolean rButtonWasDown, rButtonIsDown; //Right Click

  //Special commands
  private boolean startDragging;                //Starting a group selection
  private boolean isDragging;                   //Is making a group selection

  private int initX, initY; //Location at the beginning of a drag




  /**
   * Creates a blank palette
   */
  public Palette(){

    int k = 0;
    
    for (int i = tiles.length-1; i >= 0; i--){
      for (int j = 0; j < tiles[0].length/2; j++, k++){
        tiles[i][j] = TileList.getTile(k);
      }
    }
    for (int i = tiles.length-1; i >= 0; i--){
      for (int j = tiles[0].length/2; j < tiles[0].length; j++, k++){
        tiles[i][j] = TileList.getTile(k);
      }
    }


    selection = new Animation(8, 8, 16, 16, 2);
    for(int i=3; i >= 0; i--){
      selection.add(0 , 0 , i);
      selection.add(16, 0 , i);
    }
  }

  @Override
  public void getInput(){
            
    lButtonWasDown = lButtonIsDown;
    lButtonIsDown = Mouse.isButtonDown(0);
    rButtonWasDown = rButtonIsDown;
    rButtonIsDown = Mouse.isButtonDown(1);
        
    startDragging = Mouse.isButtonDown(1) && controlDown() && 
        !isDragging;
    
    isDragging = Mouse.isButtonDown(1) && controlDown();
  }


  @Override
  public void update(){
    
    if (checkBounds() && lButtonIsDown && !lButtonWasDown) {
      selX = getMouseX()+xOffset;
      selY = getMouseY()+yOffset;
      TileMode.clearSelection();
    }
    
    if(checkBounds() && rButtonIsDown && !rButtonWasDown && 
        !controlDown()){
      TileMode.clearSelection();
    }
    
    if(startDragging){
      initX = getMouseX();
      initY = getMouseY();
      
      if(initX < 0){
        initX = 0;
      }else if(initX >= WIDTH){
        initX = WIDTH-1;
      }
      
      if(initY < 0){
        initY = 0;
      }else if(initY >= HEIGHT){
        initY = HEIGHT-1;
      }
    }
    if(isDragging && checkBounds()){
      createSelection();
    }


  }

  @Override
  public void render(){
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        Draw.rect((j+xOffset)*SIZE, (i+yOffset)*SIZE, SIZE, SIZE, 
            tiles[i][j].getX(), tiles[i][j].getY(), 1);
        if(EditorMap.grid){
          Draw.rect((j+xOffset)*SIZE, (i+yOffset)*SIZE, SIZE, SIZE, 0, 16, SIZE,
              16+SIZE, 2);
        }
      }
    }
    
    selection.play(selX*16, selY*16);
  }

  public TileList getSelected(){
    return tiles[selY-yOffset][selX-xOffset];
  }

  /**
   * Sets the location of the selector
   * 
   * @param tl The tile to look for
   */
  public void setLoc(TileList tl){
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        if (tl == tiles[i][j]) {
          selX = j+xOffset;
          selY = i+yOffset;
          return;
        }
      }
    }
  }


  /**
   * Reloads the Palette
   */
  @Deprecated
  public void reload(){
    xOffset = Display.getWidth()/(MainE.scale*16) - 4;    
    yOffset = Display.getHeight()/(MainE.scale*16) - 14;  
    selX = (int)(xOffset*Editor.xSpan);
    selY = (int)(yOffset*Editor.ySpan);

    System.out.println("SelX: "+selX + ", SelY: "+selY);
  }


  /**
   * Creates a new selection
   */
  private void createSelection() {
    TileList[][] sel = new TileList[Math.abs(getMouseY()-initY)+1]
        [Math.abs(getMouseX()-initX)+1];
    int firstX, lastX;
    int firstY, lastY;

    firstX = Math.min(initX, getMouseX());
    firstY = Math.min(initY, getMouseY());

    lastX = Math.max(initX, getMouseX());
    lastY = Math.max(initY, getMouseY());


    for(int i = 0; i <= lastY - firstY; i++){
      for(int j = 0; j <= lastX - firstX; j++){
        sel[i][j] = tiles[firstY+i][firstX+j];
      }

    }

    TileMode.setSelection(sel);

    TileMode.setIsSelection(true);
  }


  /**
   * Checks if the Mouse is in bounds of the palette
   * @return Mouse is in palette
   */
  private boolean checkBounds() {
    return (Mouse.getX()/MainE.getScale() >= Palette.xOffset*16 &&
        Mouse.getY()/MainE.getScale() >= Palette.yOffset*16) &&
        Mouse.getY()/MainE.getScale() < (Palette.yOffset+HEIGHT)*16;
  }

  /**
   * Converts Mouse X into a tile index, adjusting for palette position
   * @return adjusted mouse x
   */
  private int getMouseX(){
    return Mouse.getX()/MainE.getScale()/16-xOffset;
  }


  /**
   * Converts Mouse Y into a tile index, adjusting for palette position
   * @return adjusted mouse y
   */
  private int getMouseY() {
    return Mouse.getY()/MainE.getScale()/16-yOffset;
  }

}