/**
 * Buttons.java    Aug 6, 2016, 10:06:07 PM
 */
package komorebi.projsoul.editor;

import static komorebi.projsoul.engine.MainE.HEIGHT;

import javax.swing.JOptionPane;

import org.lwjgl.input.Mouse;

import komorebi.projsoul.editor.modes.connect.ConnectMode;
import komorebi.projsoul.editor.modes.connect.CreateNewWorld;
import komorebi.projsoul.editor.modes.connect.FindExistingWorld;
import komorebi.projsoul.editor.modes.connect.World;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MainE;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.EditorMap.Modes;

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
          EditorMap.setMode(Modes.EVENT); break;
        case 3:
          tryOpenConnectMode();
          break;
        case 4: 
          EditorMap.editMapHeader();
          break;
        case 5:
          EditorMap.changeGrid();  break;
        case 6:
          Editor.newMap();    break;
        case 7:
          if(!map.hasPath()){
            map.newSave();
          }else{
            map.save();
          }
          break;
        case 8:
          map.newSave(); break;
        case 9:    
          Editor.revertMap(); break;
        case 10:
          Editor.loadMap(); break;
        case 11:
          Editor.testGame(); break;
        case 12:
          //TODO Undo
          System.out.println("Undo");
          break;
        case 13:
          //TODO Redo
          System.out.println("Redo");
          break;

        default:
          //Do nothing, invalid (and impossible, I hope)
          //DEBUG Button problemz
          System.err.println("Button Error");
      }
    }

  }
  
  private void tryOpenConnectMode()
  { 
    System.out.println("Try open connect");
    
    if (Editor.getMap().doesConnectModeHaveWorld())
    {
      EditorMap.setMode(Modes.CONNECT);
    } else if (World.hasWorldContainingMap(map.getName()))
    {      
      ConnectMode.setWorld(World.findWorldContainingMap(map.getName()).build());
      EditorMap.setMode(Modes.CONNECT);
    } else
    {
      showFindAWorldDialog(Editor.getMap().getName());
    }
  }
  
  private void showFindAWorldDialog(String map)
  {
    Object[] options = {"Create a New World", "Find an Existing World",
        "Cancel"};
        
    int response = JOptionPane.showOptionDialog(null, 
        map + " is not contained within a world.  Where would you like to add it?", 
        "No World Found", JOptionPane.YES_NO_CANCEL_OPTION, 
        JOptionPane.WARNING_MESSAGE, null, options, options[0]);
    
    if (response == JOptionPane.YES_OPTION)
    {
      CreateNewWorld create = new CreateNewWorld(map)
      {

        @Override
        public void uponSuccess() {
          World world = getSelectedWorld().build();
          ConnectMode.setWorld(world);
          
          EditorMap.setMode(Modes.CONNECT);
          
        }
        
      };
      
      (new Thread(create)).start();
    } else if (response == JOptionPane.NO_OPTION)
    {
      FindExistingWorld find = new FindExistingWorld()
      {
        @Override
        public void uponSuccess() {
          World world = getSelectedWorld().build();
          ConnectMode.setWorld(world);
          
          EditorMap.setMode(Modes.CONNECT);
        }
      };
      
      (new Thread(find)).start();
    }
    
  }
  
  
  
  private static class Lock {
    
    /**
     * Pauses the current thread
     */
    public void lock()
    {          
      synchronized (this)
      {        
        try
        {
          wait();
        } catch (InterruptedException e)
        {
          e.printStackTrace();
        }
      }
    }
    
    /**
     * Resumes the thread originally paused by this lock
     */
    public void unlock()
    {          
      synchronized (this)
      {
        notifyAll();
      }
    }
  }


  @Override
  public void render() {
    Draw.rect(0, HEIGHT-BUTTON_SIZE, BUTTON_SIZE*8, BUTTON_SIZE, 0, 32, 128, 48, 2);
    Draw.rect(BUTTON_SIZE*8, HEIGHT-BUTTON_SIZE, BUTTON_SIZE*4, BUTTON_SIZE, 
        0, 48, 64, 64, 2);

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
    return (Mouse.getX()/MainE.getScale() < BUTTON_SIZE*12 &&
        Mouse.getY()/MainE.getScale() > HEIGHT-BUTTON_SIZE);
  }

}
