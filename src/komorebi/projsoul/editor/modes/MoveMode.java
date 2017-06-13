/**
 * MoveMode.java		Aug 4, 2016, 6:56:08 PM
 */
package komorebi.projsoul.editor.modes;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.EditorMap.Modes;

import org.lwjgl.opengl.Display;

/**
 * Movement Permissions Editor
 * 
 * @author Aaron Roy
 * @version 
 */
@SuppressWarnings("unused")
public class MoveMode extends Mode{

  private boolean[][] collision;

  /**
   * @param col The collision data for the map
   */
  public MoveMode(boolean[][] col) {
    collision = col;
  }

  @Override
  public void update(){
    if(rButtonIsDown && checkMapBounds() && !mouseSame){
      collision[my][mx] = false;
      EditorMap.setUnsaved();
    }
    
    if(lButtonIsDown && checkMapBounds() && !mouseSame){
      collision[my][mx] = true;
      EditorMap.setUnsaved();
    }

    //Flood Fills tiles
    if(mButtonIsDown && !mButtonWasDown && checkMapBounds()){

      flood(mx, my, !collision[my][mx]);

      EditorMap.setUnsaved();
    }

    //Creates a selection
    if(rStartDragging || lStartDragging){
      initX = getMouseX();
      initY = getMouseY();
      
      if(initX < 0){
        initX = 0;
      }else if(initX >= tiles[0].length){
        initX = tiles[0].length-1;
      }
      
      if(initY < 0){
        initY = 0;
      }else if(initY >= tiles.length){
        initY = tiles.length-1;
      }
    }

    if(rIsDragging && checkMapBounds()){
      createSelection(false);
    }

    if(lIsDragging && checkMapBounds()){
      createSelection(true);
    }
  }

  @Override
  public void render(){
    float x = EditorMap.getX();
    float y = EditorMap.getY();
    
    for (int i = 0; i < collision.length; i++) {
      for (int j = 0; j < collision[0].length; j++) {
        if(EditorMap.checkTileInBounds(x+j*SIZE, y+i*SIZE)){
          if(collision[i][j]){
            //Red transparent pixel
            Draw.rect(x+j*SIZE, y+i*SIZE, SIZE, SIZE, 16, 16, 16, 16, 2);
          }else{
            //Green transparent pixel
            Draw.rect(x+j*SIZE, y+i*SIZE, SIZE, SIZE, 17, 16, 17, 16, 2);
          }
        }
      }
    }
    
    EditorMap.renderGrid();

  }

  /**
   * Creates a new selection
   */
  private void createSelection(boolean type) {
    int firstX, lastX;
    int firstY, lastY;
  
    firstX = Math.min(initX, getMouseX());
    firstY = Math.min(initY, getMouseY());
  
    lastX = Math.max(initX, getMouseX());
    lastY = Math.max(initY, getMouseY());
  
  
    for(int i = firstY; i <= lastY; i++){
      for(int j = firstX; j <= lastX; j++){
        collision[i][j] = type;
      }
    }
  }

  /**
   * Recursive method that flood fills collisions
   * 
   * @param mouseX starting tile x
   * @param mouseY starting tile y
   * @param type tile to search and destroy
   */
  private void flood(int mouseX, int mouseY, boolean type) {
    if (mouseX < 0 || mouseX >= collision[0].length ||
        mouseY < 0 || mouseY >= collision.length){
      return;
    }
    if(collision[mouseY][mouseX] == type){
      return;
    }

    collision[mouseY][mouseX] = type;
    flood(mouseX-1, mouseY,   type);
    flood(mouseX+1, mouseY,   type);
    flood(mouseX,   mouseY+1, type);
    flood(mouseX,   mouseY-1, type); 
  }

}
