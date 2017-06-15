package komorebi.projsoul.states;

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Main;
import komorebi.projsoul.map.MapHandler;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.TextHandler;



public class SaveList extends State {

  private TextHandler text = new TextHandler();
  @SuppressWarnings("unused")
  private int pickIndex;

  public String Option1, Option2, Option3;

  public SaveList(){
    text = new TextHandler();

    Option1 =  Main.getGame().saveLoc;
    Option2 = Main.getGame().saveLoc;
    Option3 = Main.getGame().saveLoc;
    text.write("File 1 : "+Option1,28,165,new EarthboundFont(2));
    text.write("File 2 : "+Option2,28,115,new EarthboundFont(2));
    text.write("File 3 : "+Option3,28,65,new EarthboundFont(2));

    pickIndex = 1;

  }


  @Override
  public void getInput() {
    if (KeyHandler.keyClick(Key.C))
    {
      System.out.println("I see you");
    }


    if(KeyHandler.keyClick(Key.ENTER)){
      System.out.println("You saved to " /*insert name of Save File*/);
      GameHandler.switchState(States.GAME);
      AudioHandler.play(MapHandler.getActiveMap().getSong(), true);
    }
  }
  @Override
  public void update() {
    // TODO Auto-generated method stub

  }
  @Override
  public void render() {

    Draw.rect(0, 0, 284, 284, 599, 572, 600, 573, 14);
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