/**
 * Menu.java Jul 6, 2016, 6:17:53 PM
 */
package komorebi.projsoul.states;

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.audio.Song;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.script.EarthboundFont;
import komorebi.projsoul.script.TextHandler;

/**
 * 
 * @author Andrew Faulkenberry
 */
public class Menu extends State {

  private TextHandler text;
  private int pickIndex;
 
  /**
   * Creates a menu object
   */
  public Menu()
  {
    text = new TextHandler();
    text.write("Project Soul",18,185,new EarthboundFont(4));
    
    //text.write("Start", 72, 64, new EarthboundFont(1));
    text.write("PRESS   ENTER", 28, 23, new EarthboundFont(3));
    pickIndex = 1;
   
  }
  @Override
  public void getInput() {
    if (KeyHandler.keyClick(Key.C))
    {
      System.out.println("I acknowladge yall");
    }
    if(KeyHandler.keyClick(Key.ENTER)){
      System.out.println("I acknowladge youy");
      
      GameHandler.switchState(States.GAME);
      AudioHandler.play(Game.getMap().getSong(), true);
      //GameHandler.switchState(States.SAVELIST); goal
    }

  }
  @Override
  public void update() {
	  
  }
  @Override
  public void render() {

	  
    Draw.rect(0, 0, 294, 304, 0, 0, 600, 573, 14);
    text.render();
    
    switch (pickIndex)
    {
      case 1:
    	  //Draws the old Picker
        //Draw.rect(60, 64, 8, 8, 0, 0, 8, 8, 7);
    	  Draw.rect(18, 31, 8, 8, 0, 0, 8, 8, 7);
    	  
        break;
      default: break;
    }

  }

}
