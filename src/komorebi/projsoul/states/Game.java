
/**
 * Game.java        May 16, 2016, 9:52:23 PM
 */
package komorebi.projsoul.states;

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.audio.Song;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Save;
import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.engine.ThreadHandler.TrackableThread;
import komorebi.projsoul.gameplay.Camera;
import komorebi.projsoul.gameplay.HUD;
import komorebi.projsoul.gameplay.Item;
import komorebi.projsoul.gameplay.Item.Items;
import komorebi.projsoul.map.MapHandler;
import komorebi.projsoul.script.Fader;
import komorebi.projsoul.script.text.DialogueBox;
import komorebi.projsoul.script.text.SpeechHandler;

import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Represents the game
 * 
 * @author Aaron Roy
 * @version 
 */
public class Game extends State{

  public ArrayList<Item> items = new ArrayList<Item>();

  private DialogueBox dialogue;

  private BufferedReader read;

  private int confidence, money;

  public int framesToGo;
  public boolean isPaused;
  public TrackableThread waiting;

  public static String testLoc;
  public static String saveLoc;

  public Death death;
  public Song piedPiper = Song.CHAOS;
  static Save saveFile;

  /**
   * Creates the player and loads the map
   */
  public Game(){
    saveFile = new Save(saveLoc);  
    MapHandler.initialize(testLoc);

    confidence = 0;
    money = 15;

    death = new Death();

    dialogue = new DialogueBox();

  }

  @Override
  public void getInput() {      

    if (dialogue.hasDialogue() && KeyHandler.firstKeyClick(Key.C))
    {
      dialogue.next();
    } 

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
      AudioHandler.play(piedPiper, true);
    }

    if (KeyHandler.keyClick(Key.P))
    {
      GameHandler.switchState(States.PAUSE);
    }

    if ((KeyHandler.keyDown(Key.LCTRL) || KeyHandler.keyDown(Key.RCTRL)) &&
        KeyHandler.keyClick(Key.N))
    {
      GameHandler.switchState(States.SAVELIST);
      AudioHandler.play(Song.SIERRA, true);
    }

    if(KeyHandler.keyClick(Key.Y))
    {
      GameHandler.switchState(States.BANKSTATE);
    }
    else if(KeyHandler.keyClick(Key.V))
    {
      GameHandler.switchState(States.SHOPSTATE);
    }
    else if(KeyHandler.keyClick(Key.I))
    {
      GameHandler.switchState(States.INVENTORYSTATE);
    }



    MapHandler.getInput();

  }

  @Override
  public void update() {
    death.update();
    Camera.update();

    if (isPaused)
    {
      framesToGo--;

      if (framesToGo <= 0)
      {
        isPaused = false;
        waiting.unlock();
      }
    }

    MapHandler.update();
    Fader.update();

  }

  @Override
  public void render() {
    MapHandler.render();


    if (dialogue.hasDialogue())
    {
      dialogue.render();
    }

    Fader.render();

  }

  /**
   * @return The current Save File (.kom)
   */
  public static Save getSave(){
    return saveFile;
  }

  public static void setSave(Save s)
  {
    saveFile = s;
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

  public BufferedReader getRead() {
    return read;
  }

  public void setRead(BufferedReader read) {
    this.read = read;
  }
}