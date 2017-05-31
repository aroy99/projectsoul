/**
 * Menu.java Jul 6, 2016, 6:17:53 PM
 */
package komorebi.projsoul.states;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.TextHandler;

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
    text.write("Project Soul", 32, 160, new EarthboundFont(2));
    text.write("Start", 72, 64, new EarthboundFont(1));
    pickIndex = 1;
  }

  @Override
  public void getInput() {
    if (KeyHandler.keyClick(Key.C))
    {
      System.out.println("I acknowladge yall");

      //Blah
    }
    if(KeyHandler.keyClick(Key.ENTER)){
      System.out.println("I acknowladge youy");
      GameHandler.switchState(States.GAME);
    }

  }

  @Override
  public void update() {


  }

  @Override
  public void render() {
    Draw.rect(0, 0, 284, 284, 0, 0, 1, 1, 1);
    text.render();

    switch (pickIndex)
    {
      case 1:
        Draw.rect(60, 64, 8, 8, 0, 0, 8, 8, 7);
        break;
      default: break;
    }

  }

}
