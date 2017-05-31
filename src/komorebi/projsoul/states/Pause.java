/**
 * Pause.java    Jul 26, 2016, 11:29:54 AM
 */
package komorebi.projsoul.states;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Main;
import komorebi.projsoul.engine.Save;
import komorebi.projsoul.gameplay.Item;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.Font;
import komorebi.projsoul.script.text.SpeechHandler;
import komorebi.projsoul.script.text.TextHandler;

/**
 * 
 * @author Andrew Faulkenberry
 */
public class Pause extends State {

  private PopUp pop;
  private TextHandler text = new TextHandler();
  private int pickIndex;
  private int prevMon, prevConf;
  
  public enum PopUp
  {
    HATS, ITEMS, OPTIONS, SAVE, NONE;
    
    public int pickIndex;
    private TextHandler text = new TextHandler();
    
    public void update()
    {
      switch (this)
      {
        case HATS:
          break;
        case ITEMS:
          break;
        case OPTIONS:
          break;
        case SAVE:
        	break;
        default:
          break;
      }
    }
    
    public void render()
    {
      switch (this)
      {
        case HATS:
          Draw.rect(5, 5, 200, 200, 0, 60, 200, 260, 6);
          break;
        case ITEMS:
          Draw.rect(5, 5, 200, 200, 0, 60, 200, 260, 6);
          int x=10, y=180;
          
          for (Item item: Main.getGame().getItems())
          {
            item.render(x, y);
            text.write(item.type().getIDString().replace(
                item.type().getIDString().substring(0, 1), 
                item.type().getIDString().substring(0, 1).toUpperCase()), 
                x, y-12, new EarthboundFont(1));
            text.write(item.type().getNiftyTidbit(), x+44, y, new EarthboundFont(1));
            y-=40;
          }
          break;
        case OPTIONS:
          Draw.rect(5, 5, 200, 200, 0, 60, 200, 260, 6);
          
          switch (pickIndex)
          {
            case 1:
              Draw.rect(12, 170, 8, 8, 0, 0, 8, 8, 7);
              break;
            case 2:
              Draw.rect(52, 170, 8, 8, 0, 0, 8, 8, 7);
              break;
            case 3:
              Draw.rect(98, 170, 8, 8, 0, 0, 8, 8, 7);
              break;
            case 4:
              Draw.rect(152, 170, 8, 8, 0, 0, 8, 8, 7);
              break;
            case 5:
              Draw.rect(12, 15, 8, 8, 0, 0, 8, 8, 7);
              break;
            default: break;
          }
          break;
        default:
          break;
        
      }

      text.render();

    }
    
    public void write(String s, int x, int y, Font font)
    {
      text.write(s, x, y, font);
    }
    
    public void click(Pause p)
    {
      switch (this)
      {
        case HATS:
          break;
        case ITEMS:
          break;
        case NONE:
          break;
        case OPTIONS:
          switch (pickIndex)
          {
            case 1:
              SpeechHandler.setScrolling(false);
            //DEBUG Scrolling speeds
              System.out.println("Off");
              break;
            case 2:
              SpeechHandler.setSpeed(1);
              System.out.println("Fast");
              break;
            case 3:
              SpeechHandler.setSpeed(3);
              System.out.println("Normal");
              break;
            case 4:
              SpeechHandler.setSpeed(5);
              System.out.println("Slow");
              break;
            case 5:
              p.setPopUp(PopUp.NONE);
              break;
          }
        case SAVE:
        	break;
            default:
          break;
        
      }
      
      
    }
    
    public void setPickIndex(int i) 
    {
      pickIndex = i;
    }
  }
  
  public Pause()
  {
    super();
    text.write("Hats", 188, 200, new EarthboundFont(1));
    text.write("Items", 188, 185, new EarthboundFont(1));
    text.write("Options", 188, 170, new EarthboundFont(1));
    text.write("Save", 188, 155, new EarthboundFont(1));
    text.write("Cancel", 188, 140, new EarthboundFont(1));
    
    text.write(Main.getGame().getMoney() + " Cs", 6, 195, new EarthboundFont(1));
    text.write(Main.getGame().getConfidence() + " conf", 6, 205, new EarthboundFont(1));
    pickIndex = 1;
    
    prevMon = Main.getGame().getMoney();
    prevConf = Main.getGame().getConfidence();
    
    pop = PopUp.NONE;
  }
  
  @Override
  public void getInput() {
    switch (pop)
    {
      case HATS:
        break;
      case ITEMS:
        break;
      case NONE:
        if (KeyHandler.keyClick(Key.UP))
        {
          pickIndex--;
        } else if (KeyHandler.keyClick(Key.DOWN))
        {
          pickIndex++;
        }
       
        if (pickIndex<1) pickIndex = 5;
        if (pickIndex>5) pickIndex = 1;
        
        if (KeyHandler.keyClick(Key.ENTER))
        {
          click();
        }
        break;
      case OPTIONS:
        if (KeyHandler.keyClick(Key.RIGHT))
        {
          pop.pickIndex++;
        } else if (KeyHandler.keyClick(Key.LEFT))
        {
          pop.pickIndex--;
        }
        
        if (pop.pickIndex<1) pop.pickIndex = 5;
        if (pop.pickIndex>5) pop.pickIndex = 1;
        
        if (KeyHandler.keyClick(Key.SPACE))
        {
          pop.click(this); 
        }
        break;
      case SAVE:
    	break;
      default:
        break;
      
    }
     
  }

  @Override
  public void update() {
    
    pop.update();
  }

  @Override
  public void render() {
    Draw.rect(245, 3, 220, 75, 0, 0, 220, 59, 1, 6); //draws the menu
    
    Draw.rect(5, 190, 50, 28, 100, 40, 101, 41, 6); //draws the top-left flat color
    
    text.render();
    
    switch (pickIndex)
    {
      case 1:
        Draw.rect(178, 200, 8, 8, 0, 0, 8, 8, 7);
        break;
      case 2:
        Draw.rect(178, 185, 8, 8, 0, 0, 8, 8, 7);
        break;
      case 3:
        Draw.rect(178, 170, 8, 8, 0, 0, 8, 8, 7);
        break;
      case 4:
        Draw.rect(178, 155, 8, 8, 0, 0, 8, 8, 7);
        break;
      case 5:
        Draw.rect(178, 140, 8, 8, 0, 0, 8, 8, 7);
        break;
      default: break;
    }
    
    
    
    
    pop.render();
  }
  
  public void click()
  {
    switch (pickIndex)
    {
      case 1:
        pop = PopUp.HATS;
        break;
      case 2:
        pop = PopUp.ITEMS;
        break;
      case 3:
        pop = PopUp.OPTIONS;
        pop.write("Text Scrolling:", 10, 185, new EarthboundFont(1));
        pop.write("Off", 22, 170, new EarthboundFont(1));
        pop.write("Fast",62, 170, new EarthboundFont(1));
        pop.write("Normal",108,170, new EarthboundFont(1));
        pop.write("Slow", 162, 170, new EarthboundFont(1));
        pop.write("Return", 22, 15, new EarthboundFont(1));
        pop.setPickIndex(1);
        break;
      case 4:
        Save.newSaveGame();
        System.out.println("Save!");
        break;
      case 5:
        GameHandler.switchState(States.GAME);
        break;
    }
  }
  
  public void setPopUp(PopUp pop)
  {
    this.pop = pop;
  }
  
  public void reload()
  {
    text.replace(prevMon + " Cs", 
        Main.getGame().getMoney() + " Cs");
    text.replace(prevConf + " conf", 
        Main.getGame().getConfidence() + " conf");
    
    prevMon = Main.getGame().getMoney();
    prevConf = Main.getGame().getConfidence();
  }

}
