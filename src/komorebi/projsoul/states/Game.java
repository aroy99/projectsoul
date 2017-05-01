
/**
 * Game.java        May 16, 2016, 9:52:23 PM
 */
package komorebi.projsoul.states;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Main;
import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.engine.ThreadHandler.TrackableThread;
import komorebi.projsoul.gameplay.HUD;
import komorebi.projsoul.gameplay.Item;
import komorebi.projsoul.gameplay.Item.Items;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.Fader;
import komorebi.projsoul.script.text.DialogueBox;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.SpeechHandler;
import komorebi.projsoul.script.text.TextHandler;
import komorebi.projsoul.script.text.Word;

/**
 * Represents the game
 * 
 * @author Aaron Roy
 * @version 
 */
public class Game extends State{

  public ArrayList<Item> items = new ArrayList<Item>();
  
  private DialogueBox dialogue;

  private TextHandler currMap;

  private int mapDisplayCount = 120;
  private int displayY = 15;

  private BufferedReader read;

  private int confidence, money;

  public int framesToGo;
  public boolean isPaused;
  public TrackableThread waiting;

  public static String testLoc;

  public HUD hud;
  public Death death;

  /**
   * Creates the player and loads the map
   */
  public Game(){
    map = new Map("res/maps/"+testLoc);

    currMap = new TextHandler();
    currMap.write(map.getTitle(), 5, Main.HEIGHT-13);

    confidence = 0;
    money = 15;

    hud = new HUD(confidence); //TODO WHY KEVIN
    death = new Death();

    dialogue = new DialogueBox();

  }



  /* (non-Javadoc)
   * @see komorebi.clyde.states.State#getInput()
   */
  @Override
  public void getInput() {      
    
    if (dialogue.hasDialogue() && KeyHandler.firstKeyClick(Key.C))
    {
      dialogue.next();
    } 

    //TODO Remove map debug features
    map.getInput();

    if (KeyHandler.keyClick(Key.LEFT) && dialogue.hasQuestion())
    {
      dialogue.movePickerLeft();
    }

    if (KeyHandler.keyClick(Key.RIGHT) && dialogue.hasQuestion())
    {
      dialogue.movePickerRight();
    } 

    if ((KeyHandler.keyDown(Key.LCTRL) || KeyHandler.keyDown(Key.RCTRL)) &&
        KeyHandler.keyClick(Key.M))
    {
      GameHandler.switchState(States.MENU);
    }

    if (KeyHandler.keyClick(Key.P))
    {
      GameHandler.switchState(States.PAUSE);
    }

  }

  /* (non-Javadoc)
   * @see komorebi.clyde.states.State#update()
   */
  @Override
  public void update() {
    // TODO Auto-generated method stub  
    hud.update();
    death.update();


    if (isPaused)
    {
      framesToGo--;

      if (framesToGo<=0)
      {
        isPaused = false;
        waiting.unlock();
      }
    }

    map.update();
    Fader.update();

  }

  /* (non-Javadoc)
   * @see komorebi.clyde.states.State#render()
   */
  @Override
  public void render() {
    map.render();
    Map.getPlayer().renderHUD();
    if(mapDisplayCount > 0 || displayY > 0){
      Draw.rect(2, Main.HEIGHT-displayY, 100, 12, 1, 1, 2, 2, 6);
      currMap.render(new Word(map.getTitle(), 5, Main.HEIGHT-displayY+2, new EarthboundFont(1)));
      mapDisplayCount--;

      if(mapDisplayCount < 0){
        displayY--;
      }
    }
    
    if (dialogue.hasDialogue())
    {
      dialogue.render();
    }
    
    Fader.render();

  }


  /**
   * @return The current map
   */
  public static Map getMap(){
    return map;
  }

  public static void setMap(Map m)
  {
    map = m;
  }

  public void setSpeaker(SpeechHandler talk)
  {
    dialogue.setSpeaker(talk);
  }

  /**
   * Pauses the game for a specified number of frames
   * 
   * @param frames The number of frames to be paused
   * @param lock The script execution whose thread will be locked while the game
   *        is paused
   */
  public void pause(int frames)
  {
    framesToGo = frames;
    isPaused = true;

    this.waiting = ThreadHandler.currentThread();
    ThreadHandler.lockCurrentThread();

  }

  /**
   * Sets the NPC currently presenting a question to the player
   * @param npc The asking NPCS
   */
  public void setAsker(SpeechHandler talk)
  {
    dialogue.setAsker(talk);
  }

  public void receiveItem(Items item)
  {
    items.add(new Item(item));
  }

  public ArrayList<Item> getItems()
  {
    return items;
  }

  public int getMoney()
  {
    return money;
  }

  public int getConfidence()
  {
    return confidence;
  }

  public void giveMoney(int add)
  {
    money += add;
  }

  public void takeMoney(int subtract)
  {
    money -= subtract;
  }

  public void giveConfidence(int add)
  {
    confidence += add;
  }
}