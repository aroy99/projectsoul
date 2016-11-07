/**
 * TileMode.java   Aug 4, 2016, 6:16:33 PM
 */
package komorebi.projsoul.editor.modes;

import komorebi.projsoul.editor.Palette;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.TileList;

import org.lwjgl.opengl.Display;

/**
 * The tile editing part of the editor
 * 
 * @author Aaron Roy
 */
public class TileMode extends Mode implements Playable{

  private static TileList[][] selection;
  private static Palette pal = new Palette();

  @Override
  public void getInput() {
    pal.getInput();
  }
  
  @Override
  public void update(){
    pal.update();
    
    //Sets mouse tile to the one from the palette
    if(lButtonIsDown && checkMapBounds() && !isSelection){
  
      tiles[my][mx] = pal.getSelected();
  
      EditorMap.setUnsaved();
      if(Display.getTitle().charAt(Display.getTitle().length()-1) != '*'){
        Display.setTitle(Display.getTitle()+"*");
      }
    }
  
    //Sets palette's selected to mouse tile
    if(rButtonIsDown && checkMapBounds() && !rButtonWasDown){
      pal.setLoc(tiles[getMouseY()][getMouseX()]);
      clearSelection();
  
    }
  
    //Flood Fills tiles
    if(mButtonIsDown && !mButtonWasDown && checkMapBounds()){
      int mx = getMouseX();
      int my = getMouseY();
  
      flood(mx, my, tiles[my][mx]);
      EditorMap.setUnsaved();
    }
  
    //Creates a selection
    if(rStartDragging){
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
      createSelection();
    }
  
    if(isSelection && lButtonIsDown && checkMapBounds() && !lButtonWasDown){
      for(int i = 0; i < selection.length; i++){
        for (int j = 0; j < selection[0].length; j++) {
          if(checkTileBounds(getMouseY()+i, getMouseX()+j)){
            tiles[getMouseY()+i][getMouseX()+j] = 
                selection[i][j];
          }
        }
      }
      EditorMap.setUnsaved();
    }
  
  }


  @Override
  public void render(){
    //Render selection
    if(selection != null){
      for (int i = 0; i < selection.length; i++) {
        for (int j = 0; j < selection[0].length; j++) {
          Draw.rect(EditorMap.getX()+tiles[0].length*SIZE+j*SIZE, 
              EditorMap.getY()+i*SIZE, SIZE, SIZE, 
              selection[i][j].getX(), selection[i][j].getY(), 1);
        }
      }
      //Render preview block
      if(checkMapBounds()){
        Draw.rect(EditorMap.getX()+mx*SIZE, EditorMap.getY()+my*SIZE, 
            selection[0].length*SIZE, selection.length*SIZE, 
            16, 16,16,16, 2);
      }
    }
    
    EditorMap.renderGrid();
    
    pal.render();
  }

  /**
   * Creates a new selection
   */
  private void createSelection() {
    selection = new TileList[Math.abs(getMouseY()-initY)+1]
        [Math.abs(getMouseX()-initX)+1];
    int firstX, lastX;
    int firstY, lastY;
  
    firstX = Math.min(initX, getMouseX());
    firstY = Math.min(initY, getMouseY());
  
    lastX = Math.max(initX, getMouseX());
    lastY = Math.max(initY, getMouseY());
  
  
    for(int i = 0; i <= lastY - firstY; i++){
      for(int j = 0; j <= lastX - firstX; j++){
        selection[i][j] =  tiles[firstY+i][firstX+j];
      }
    }
  
    isSelection = true;
  }

  public TileList[][] getSelection(){
    return selection;
  }

  public static void setSelection(TileList[][] sel){
    selection = sel;
  }




  /**
   * Recursive method that flood fills tiles
   * 
   * @param mouseX starting tile x
   * @param mouseY starting tile y
   * @param type tile to search and destroy
   */
  private void flood(int mouseX, int mouseY, TileList type) {
    if (mouseX < 0 || mouseX >= tiles[0].length ||
        mouseY < 0 || mouseY >= tiles.length){
      return;
    }
    if(tiles[mouseY][mouseX] != type || 
        tiles[mouseY][mouseX] == pal.getSelected()){
      return;
    }

    tiles[mouseY][mouseX] = pal.getSelected();
    flood(mouseX-1, mouseY,   type);
    flood(mouseX+1, mouseY,   type);
    flood(mouseX,   mouseY+1, type);
    flood(mouseX,   mouseY-1, type);
  }

  /**
   * Sets isSelection to s
   * 
   * @param selec value to set the selection to
   */
  public static void setIsSelection(boolean selec) {
    isSelection = selec;
  }

  /**
   * Clears the selection, making it disappear
   */
  public static void clearSelection(){
    selection = null;
    isSelection = false;
  }

}
