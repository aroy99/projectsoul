/**
 * Buttons.java    Aug 6, 2016, 10:06:07 PM
 */
package komorebi.projsoul.editor;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MainE;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.EditorMap.Modes;

import static komorebi.projsoul.engine.MainE.HEIGHT;
import static komorebi.projsoul.engine.MainE.WIDTH;

import org.lwjgl.input.Mouse;

/**
 * 
 * @author Aaron Roy
 */
public class Buttons implements Playable{

  public static final int BUTTON_SIZE = 32;         //Width and height of a button

  public EditorMap map;

  public Buttons(EditorMap m){
    map = m;
  }

  public void setMap(EditorMap m){
    map = m;
  }

  @Override
  public void getInput(){

  }

  @Override
  public void update() {
    if(KeyHandler.keyClick(Key.LBUTTON) && checkButtonBounds()){
      switch(Mouse.getX()/(32*MainE.scale)){
        case 0:
          EditorMap.setMode(Modes.TILE); break;
        case 1:
          EditorMap.setMode(Modes.MOVE); break;
        case 2:
          EditorMap.setMode(Modes.EVENT);; break;
        case 3: 
          //TODO Edit Map Header
          break;
        case 4:
          EditorMap.changeGrid();  break;
        case 5:
          Editor.newMap();    break;
        case 6:
          if(map.getPath() == null){
            map.newSave();
          }else{
            map.save();
          }
          break;
        case 7:
          map.newSave(); break;
        case 8:    
          Editor.revertMap();      break;
        case 9:
          Editor.loadMap(); break;
        case 10:
          Editor.testGame(); break;
        case 11:
          //TODO Undo
          break;
        case 12:
          //TODO Redo
          break;

        default:
          //Do nothing, invalid (and impossible, I hope)
          //TODO Debug
          System.out.println("This shouldn't be happening m8");
      }
    }

  }

  @Override
  public void render() {
    Draw.rect(0, HEIGHT-BUTTON_SIZE, BUTTON_SIZE*8, BUTTON_SIZE, 0, 32, 128, 48, 2);
    Draw.rect(BUTTON_SIZE*8, HEIGHT-BUTTON_SIZE, BUTTON_SIZE*3, BUTTON_SIZE, 0, 48, 48, 64, 2);

    if(checkButtonBounds()){
      int x = Mouse.getX()/(BUTTON_SIZE*MainE.getScale())*BUTTON_SIZE;
      int y = HEIGHT - BUTTON_SIZE;

      Draw.rect(x, y, BUTTON_SIZE, BUTTON_SIZE, 64, 0, 2);
    }

  }

  /**
   * Checks if the Mouse is in bounds of the buttons
   * @return Mouse is on a button
   */
  private boolean checkButtonBounds() {
    return (Mouse.getX()/MainE.getScale() < WIDTH-BUTTON_SIZE*14 &&
        Mouse.getY()/MainE.getScale() > HEIGHT-BUTTON_SIZE);
  }

}
