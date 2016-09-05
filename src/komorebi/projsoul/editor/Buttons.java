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

  public static final int SIZE = 32;         //Width and height of a button

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
          EditorMap.setMode(Modes.NPC);; break;
        case 3: break;
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

        default:
          //Do nothing, invalid (and impossible, I hope)
          System.out.println("This shouldn't be happening m8");
      }
    }

  }

  @Override
  public void render() {
    Draw.rect(0, HEIGHT-SIZE, SIZE*8, SIZE, 0, 32, 128, 48, 2);
    Draw.rect(SIZE*8, HEIGHT-SIZE, SIZE*3, SIZE, 0, 48, 48, 64, 2);

    if(checkButtonBounds()){
      int x = Mouse.getX()/(SIZE*MainE.getScale())*SIZE;
      int y = HEIGHT - SIZE;

      Draw.rect(x, y, SIZE, SIZE, 64, 0, 2);
    }

  }

  /**
   * Checks if the Mouse is in bounds of the buttons
   * @return Mouse is on a button
   */
  private boolean checkButtonBounds() {
    return (Mouse.getX()/MainE.getScale() < WIDTH-SIZE*14 &&
        Mouse.getY()/MainE.getScale() > HEIGHT-SIZE);
  }

}
