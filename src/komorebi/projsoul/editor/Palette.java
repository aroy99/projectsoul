/**
 * Palette.java     May 22, 2016, 2:23:43 PM
 */




/**
 * Palette.java     May 22, 2016, 2:23:43 PM
 */

package komorebi.projsoul.editor;

import static komorebi.projsoul.engine.KeyHandler.controlDown;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import komorebi.projsoul.editor.modes.TileMode;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.MainE;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.script.TextHandler;


/**
 * The current palette to choose from
 * 
 * @author Aaron Roy
 */
public class Palette implements Playable{

  private boolean scripting;
  private TextHandler text;

  public static final int HEIGHT = 16;
  public static final int WIDTH = 8;
  public static final int L_BUTTON = 6, R_BUTTON = 7;

  private boolean top;


  public static final int SIZE = 16;  //Width and height of a tile

  //Holds current palette tiles
  private ArrayList<Tileset> tilesets = new ArrayList<Tileset>();

  //int[HEIGHT][WIDTH]

  /**Offset of palette in tiles*/
  public static int xOffset = Display.getWidth() / (MainE.scale * 16) - WIDTH;
  public static int yOffset = 0;
  public static int yOffset2 = HEIGHT + 1;

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

  private String topFile, botFile;

  private int initX, initY; //Location at the beginning of a drag

  private int currTsetBot = 0;
  private int currTsetTop = 1;



  /**
   * Creates a blank palette
   */

  public Palette(){

    tilesets.add(new Tileset("new1.tset"));
    tilesets.add(new Tileset("ThisNameIsIntentionallyExcessivelyLongToTestTheWriteMindLengthMethod.tset"));

    selection = new Animation(8, 8, 16, 16, 2);
    for(int i=3; i >= 0; i--){
      selection.add(0 , 0, i);
      selection.add(16, 0, i);
    }

    text = new TextHandler();
    text.writeMindLength(tilesets.get(currTsetTop).getFilePath(), xOffset*SIZE+2, 
        (yOffset2+HEIGHT)*SIZE+2, 92); //write top tileset's file name
    text.writeMindLength(tilesets.get(currTsetBot).getFilePath(), xOffset*SIZE+2,
        HEIGHT*SIZE+2, 92);

    topFile = tilesets.get(currTsetTop).getFilePath();
    botFile = tilesets.get(currTsetBot).getFilePath();

  }

  public Palette(String filePath)
  {    
    tilesets.add(new Tileset(filePath)); 
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

    if (!TileMode.isInRemoveMode())
    {
      if (checkBottomBounds() && ((lButtonIsDown && !lButtonWasDown)
          || (rButtonIsDown && !rButtonWasDown)))
      {
        top = false;
      }

      if (checkTopBounds() && ((lButtonIsDown && !lButtonWasDown)
          || (rButtonIsDown && !rButtonWasDown)))
      {
        top = true;
      }

      if (checkBottomBounds() && lButtonIsDown && !lButtonWasDown) {

        selX = getMouseX()+xOffset;
        selY = getMouseY()+yOffset;

        TileMode.clearSelection();
      }

      if (checkTopBounds() && lButtonIsDown && !lButtonWasDown) {
        selX = getMouseX()+xOffset;
        selY = getMouseY()+yOffset;

        TileMode.clearSelection();
      }

      if((checkBottomBounds() || checkTopBounds()) && rButtonIsDown && !rButtonWasDown && 
          !controlDown()){
        top = checkTopBounds();
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

        if(!top && initY < 0){
          initY = 0;
        }else if(!top && initY >= HEIGHT){
          initY = HEIGHT-1;
        } else if (top && initY > HEIGHT*2+1)
        {
          initY = HEIGHT*2;
        } else if (top && initY < HEIGHT)
        {
          initY = HEIGHT;
        }
      }
      if(isDragging && (checkBottomBounds() || checkTopBounds())){
        createSelection();
      }
    } else
    {
      boolean rmvTop = false, rmvAtAll = false;
      
      if (lButtonIsDown & !lButtonWasDown)
      {
        if (checkTopBounds() && currTsetTop!=-1)
        {
          tilesets.remove(currTsetTop);
          rmvTop = rmvAtAll = true;

        } else if (checkBottomBounds() && currTsetBot!=-1)
        {
          tilesets.remove(currTsetBot);
          rmvAtAll = true;
        }
        
        if (tilesets.size()==1)
        {
          currTsetTop = 0;
          currTsetBot = -1;
          
          topFile = tilesets.get(currTsetTop).getFilePath();
          botFile = null;
          
          if (selY < 16)
          {
            selX = xOffset;
            selY = HEIGHT*2;
          }
          
        } else if (tilesets.size()==0)
        {
          currTsetTop = currTsetBot = -1;
          botFile = topFile = null;
        } else if (rmvAtAll)
        {
          if (rmvTop)
          {
            if (currTsetBot != 0)
            {
              currTsetTop = 0;
            } else
            {
              currTsetTop = 1;
            }
            
            topFile = tilesets.get(currTsetTop).getFilePath();
          } else
          {
            if (currTsetTop != 0)
            {
              currTsetBot = 0;
            } else
            {
              currTsetBot = 1;
            }
            
            botFile = tilesets.get(currTsetBot).getFilePath();
          }
        }
        
        updateFileNames();
        TileMode.clearSelection();
      }
    }


    if (lButtonIsDown && !lButtonWasDown)
    {
      switch (scrollButtons())
      {
        case 0:
          currTsetTop--;

          while (currTsetTop < 0 || currTsetTop==currTsetBot)
          {
            currTsetTop--;

            if (currTsetTop < 0)
            {
              currTsetTop = tilesets.size()-1;
            }
          }

          topFile = tilesets.get(currTsetTop).getFilePath();
          updateFileNames();

          break;
        case 1:
          currTsetTop++;

          while (currTsetTop >= tilesets.size() || currTsetTop==currTsetBot)
          {
            currTsetTop++;

            if (currTsetTop >= tilesets.size())
            {
              currTsetTop = 0;
            }
          }

          topFile = tilesets.get(currTsetTop).getFilePath();
          updateFileNames();
          break;
        case 2:
          currTsetBot--;

          while (currTsetBot < 0 || currTsetTop==currTsetBot)
          {
            currTsetBot--;

            if (currTsetBot < 0)
            {
              currTsetBot = tilesets.size()-1;
            }
          }

          botFile = tilesets.get(currTsetBot).getFilePath();
          updateFileNames();
          break;
        case 3:
          currTsetBot++;

          while (currTsetBot >= tilesets.size() || currTsetTop==currTsetBot)
          {
            currTsetBot++;

            if (currTsetBot >= tilesets.size())
            {
              currTsetBot = 0;
            }
          }

          botFile = tilesets.get(currTsetBot).getFilePath();
          updateFileNames();
          break;
        default:
          break;
      }
    }

  }

  @Override
  public void render() {
    if (currTsetBot != -1)
    {
      for (int i = 0; i < tilesets.get(currTsetBot).tiles().length; i++) {
        for (int j = 0; j < tilesets.get(currTsetBot).tiles()[0].length; j++) {


          Draw.tile((j+xOffset)*SIZE, (i+yOffset)*SIZE, 
              Draw.getTexX(tilesets.get(currTsetBot).tiles()[i][j]), 
              Draw.getTexY(tilesets.get(currTsetBot).tiles()[i][j]), 
              Draw.getTexture(tilesets.get(currTsetBot).tiles()[i][j]));
          if(EditorMap.grid){
            Draw.rect((j+xOffset)*SIZE, (i+yOffset)*SIZE, SIZE, SIZE, 0, 16, SIZE,
                16+SIZE, 2);
          }
        }
      }
    } 

    if (currTsetTop != -1)
    {
      for (int i = 0; i < tilesets.get(currTsetTop).tiles().length; i++) {
        for (int j = 0; j < tilesets.get(currTsetTop).tiles()[0].length; j++) {

          Draw.tile((j+xOffset)*SIZE, (i+yOffset2)*SIZE, 
              Draw.getTexX(tilesets.get(currTsetTop).tiles()[i][j]), 
              Draw.getTexY(tilesets.get(currTsetTop).tiles()[i][j]), 
              Draw.getTexture(tilesets.get(currTsetTop).tiles()[i][j]));
          if(EditorMap.grid){
            Draw.rect((j+xOffset)*SIZE, (i+yOffset2)*SIZE, SIZE, SIZE, 0, 16, SIZE,
                16+SIZE, 2);
          }
        }
      }
    }

    if (TileMode.isInRemoveMode())
    {
      if (checkTopBounds() && currTsetTop != -1)
      {
        Draw.rect(xOffset*SIZE, (HEIGHT+1)*SIZE, 
            WIDTH*SIZE, HEIGHT*SIZE, 220, 12, 221, 13, 6);
      } else if (checkBottomBounds() && currTsetBot != -1)
      {
        Draw.rect(xOffset*SIZE, 0, 
            WIDTH*SIZE, HEIGHT*SIZE, 220, 12, 221, 13, 6);
      }
    }

    if (currTsetBot != -1)
    {
      Draw.rect(xOffset*SIZE, HEIGHT*SIZE, 128, 16, 0, 66, 128, 82, 2);
    }
    
    if (currTsetTop != -1)
    {
      Draw.rect(xOffset*SIZE, (HEIGHT*2+1)*SIZE, 128, 16, 0, 66, 128, 82, 2);
    }

    text.render();

    if (!tilesets.isEmpty())
    {
      selection.play(selX*16, selY*16);
    }
  }

  public int getSelected(){
    if (selY < HEIGHT)
    {
      return tilesets.get(currTsetBot).tiles()[selY-yOffset][selX-xOffset];
    } else
    {
      return tilesets.get(currTsetTop).tiles()[selY-yOffset2][selX-xOffset];
    }
  }

  /**
   * Sets the location of the selector
   * 
   * @param tl The tile to look for
   */
  public void setLoc(int tl){
    for (int i = 0; i < tilesets.get(currTsetBot).tiles().length; i++) {
      for (int j = 0; j < tilesets.get(currTsetBot).tiles()[0].length; j++) {
        if (tl == tilesets.get(currTsetBot).tiles()[i][j]) {
          selX = j+xOffset;
          selY = i+yOffset;
          return;
        }
      }
    }
  }

  /**
   * Creates a new selection
   */
  private void createSelection() {

    if (top)
    {
      int[][] sel = new int[Math.abs(Math.max(getMouseY(),17)-initY)+1]
          [Math.abs(getMouseX()-initX)+1];
      int firstX, lastX;
      int firstY, lastY;      

      firstX = Math.min(initX, getMouseX());
      firstY = Math.min(initY, getMouseY())-17;

      lastX = Math.max(initX, getMouseX());
      lastY = Math.max(initY, getMouseY())-17;


      firstY = Math.max(firstY, 0); //Ensures index cannot be smaller than 0

      for(int i = 0; i <= lastY - firstY; i++){
        for(int j = 0; j <= lastX - firstX; j++){
          sel[i][j] = tilesets.get(currTsetTop).tiles()[firstY+i][firstX+j];
        }

      }
      TileMode.setSelection(sel);

    } else
    {
      int[][] sel = new int[Math.abs(getMouseY()-initY)+1]
          [Math.abs(getMouseX()-initX)+1];
      int firstX, lastX;
      int firstY, lastY;

      firstX = Math.min(initX, getMouseX());
      firstY = Math.min(initY, getMouseY());

      lastX = Math.max(initX, getMouseX());
      lastY = Math.max(initY, getMouseY());

      for(int i = 0; i <= lastY - firstY; i++){
        for(int j = 0; j <= lastX - firstX; j++){
          sel[i][j] = tilesets.get(currTsetBot).tiles()[firstY+i][firstX+j];
        }

      }
      TileMode.setSelection(sel);

    }

    TileMode.setIsSelection(true);
  }


  /**
   * Checks if the Mouse is in bounds of the palette
   * @return Mouse is in palette
   */
  private boolean checkBottomBounds() {

    return Mouse.getX()/MainE.getScale() >= Palette.xOffset*16 && 
        Mouse.getY()/MainE.getScale() >= Palette.yOffset*16 &&
        Mouse.getY()/MainE.getScale() < (Palette.yOffset+HEIGHT)*16
        && currTsetBot != -1;

  }

  private boolean checkTopBounds()
  {
    return Mouse.getX()/MainE.getScale() >= Palette.xOffset*16 && 
        Mouse.getY()/MainE.getScale() >= Palette.yOffset2*16 &&
        Mouse.getY()/MainE.getScale() < (Palette.yOffset+HEIGHT*2+1)*16
        && currTsetTop != - 1;
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

  public void addTileset(String file)
  {
    tilesets.add(new Tileset(file));
    if (currTsetTop == -1)
    {
      currTsetTop = 0;     
      topFile = tilesets.get(currTsetTop).getFilePath();
    } else if (currTsetBot == -1)
    {
      currTsetBot = 1;
      botFile = tilesets.get(currTsetBot).getFilePath();
    } else
    {
      currTsetTop = tilesets.size()-1;
      topFile = tilesets.get(currTsetTop).getFilePath();
    }
    
    updateFileNames();
  }

  public int scrollButtons()
  {
    if (getMouseX()==L_BUTTON && getMouseY()==HEIGHT*2+1)
      return 0;
    if (getMouseX()==R_BUTTON && getMouseY()==HEIGHT*2+1)
      return 1;
    if (getMouseX()==L_BUTTON && getMouseY()==HEIGHT)
      return 2;
    if (getMouseX()==R_BUTTON && getMouseY()==HEIGHT)
      return 3;

    return -1;
  }

  public void updateFileNames()
  {
    text.clear();
    
    if (topFile!=null)
    {
      text.writeMindLength(topFile, xOffset*SIZE+2, (yOffset2+HEIGHT)*SIZE+2, 92);
    }
    
    if (botFile!=null)
    {
      text.writeMindLength(botFile, xOffset*SIZE+2, HEIGHT*SIZE+2, 92);
    }
  }

}