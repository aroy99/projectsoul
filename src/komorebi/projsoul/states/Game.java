
/**
 * Game.java        May 16, 2016, 9:52:23 PM
 */
package komorebi.projsoul.states;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.Item;
import komorebi.projsoul.engine.Item.Items;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Main;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.AreaScript;
import komorebi.projsoul.script.Execution;
import komorebi.projsoul.script.Fader;
import komorebi.projsoul.script.InstructionList;
import komorebi.projsoul.script.Instructions;
import komorebi.projsoul.script.Lock;
import komorebi.projsoul.script.SignHandler;
import komorebi.projsoul.script.SpeechHandler;
import komorebi.projsoul.script.Task;
import komorebi.projsoul.script.Task.TaskWithNumber;
import komorebi.projsoul.script.Task.TaskWithString;
import komorebi.projsoul.script.TextHandler;
import komorebi.projsoul.script.Word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the game
 * 
 * @author Aaron Roy
 * @version 
 */
public class Game extends State{
  
  public ArrayList<Item> items = new ArrayList<Item>();
  
  public boolean[] booleans;

  private boolean hasText, hasChoice;
  private int pickIndex;
  private int maxOpt;

  private SpeechHandler speaker;
  
  private TextHandler currMap;
  
  private int mapDisplayCount = 120;
  private int displayY = 15;

  private ArrayList<Lock> waitingLocks;
  private ArrayList<Int> pauseFrames;
  
  private int confidence, money;
  
  public static String testLoc;
  
  public class Int {
    private int val;
    
    public Int(int value)
    {
      val = value;
    }
    
    public void increment()
    {
      val++;
    }
    
    public void decrement()
    {
      val--;
    }
    
    public int intValue()
    {
      return val;
    }
  }

  /**
   * Creates the player and loads the map
   */
  public Game(){
    map = new Map("res/maps/"+testLoc);
    
    currMap = new TextHandler();
    currMap.write(map.getTitle(), 5, Main.HEIGHT-13, 8);

    pauseFrames = new ArrayList<Int>();
    waitingLocks = new ArrayList<Lock>();

    booleans = new boolean[256];

    confidence = 0;
    money = 15;


  }

  /* (non-Javadoc)
   * @see komorebi.clyde.states.State#getInput()
   */
  @Override
  public void getInput() {    
    if (KeyHandler.keyClick(Key.C))
    {
      if (speaker!=null)
      {
        if (speaker.isWaitingOnParagraph())
        {
          speaker.nextParagraph();
        } else {
          if (!speaker.alreadyAsked())
          {
            speaker.skipScroll();
          } else
          {
            if (hasText)
            {
              speaker.clear();

              if (hasChoice) {
                
                speaker.branch(pickIndex);
              }

              hasChoice=false;
              hasText=false;
              
              if (speaker instanceof SignHandler)
              {
                SignHandler sign = (SignHandler) speaker;
                sign.disengage();
              }
              
              speaker = null;
            }
          }
        } 
      } 
    }


    //TODO Remove map debug features
    map.getInput();

    if (KeyHandler.keyClick(Key.LEFT))
    {
      if (hasChoice && KeyHandler.keyDown(Key.LEFT))
      {
        pickIndex--;
        if (pickIndex < 1) {
          pickIndex = maxOpt;
        }
        
        speaker.setPickerIndex(pickIndex);

      }  
    }

    if (KeyHandler.keyClick(Key.RIGHT))
    {
      if (hasChoice && KeyHandler.keyDown(Key.RIGHT))
      {
        pickIndex++;
        if (pickIndex > maxOpt) {
          pickIndex = 1;
        }
        
        speaker.setPickerIndex(pickIndex);
      }
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
    map.update();
    Fader.update();

    for (Iterator<Int> it = pauseFrames.iterator(); it.hasNext();)
    {      
      Int i = it.next();
      if(i != null){
        i.decrement();
        if (i.intValue() == 0)
        {
          waitingLocks.get(pauseFrames.indexOf(i)).resumeThread();
          waitingLocks.remove(pauseFrames.indexOf(i));
          it.remove();
        }
      }else{
        it.remove();
      }

    }



  }

  /* (non-Javadoc)
   * @see komorebi.clyde.states.State#render()
   */
  @Override
  public void render() {
    map.render();
    Map.getClyde().magicBar().render();
    
    if(mapDisplayCount > 0 || displayY > 0){
      Draw.rect(2, Main.HEIGHT-displayY, 100, 12, 1, 1, 2, 2, 6);
      currMap.render(new Word(map.getTitle(), 5, Main.HEIGHT-displayY+2));
      mapDisplayCount--;
      
      if(mapDisplayCount < 0){
        displayY--;
      }
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
    this.speaker = talk;
    this.hasText = true;
  }

  /**
   * Pauses the game for a specified number of frames
   * 
   * @param frames The number of frames to be paused
   * @param lock The script execution whose thread will be locked while the game
   *        is paused
   */
  public void pause(int frames, Lock lock)
  {
    pauseFrames.add(new Int(frames));
    waitingLocks.add(lock);

    lock.pauseThread();
  }

  /**
   * Sets the speech bubble currently presenting a question to the player
   * 
   * @param text The asking NPC's SpeechHandler object
   */
  public void setAsker(SpeechHandler text)
  {
    this.speaker = text;
    this.hasText = true;
    this.hasChoice = true;
    //this.choosesLeft = true;

    pickIndex = 1;
  }

  public void setMaxOptions(int i)
  {
    maxOpt = i;
  }

  /**
   * Returns the NPC with a specified name
   * 
   * @param s The name of the NPC to be retrieved
   * @return The NPC with name, s
   */
  public NPC getNpc(String s)
  {
    for (NPC person: NPC.getNPCs())
    {
      if (person.getName().equals(s))
      {
        return person;
      }
    }
    return null;
  }

  /**
   * Fades the screen out, loads a new map and fades the screen back in
   * @param newMap The new map to be loaded (sans .map extension)
   */
  public void warp(String newMap)
  {
    InstructionList instructions = new InstructionList("Main");
    instructions.add(new Task(Instructions.FADE_OUT));
    
    instructions.add(new TaskWithString(Instructions.LOAD_MAP, newMap));
    
    instructions.add(new TaskWithNumber(Instructions.WAIT, 40));
    instructions.add(new Task(Instructions.FADE_IN));


    (new Thread(new Execution(null, instructions))).start();

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
  
  public void setFlag(int index, boolean b)
  {
    booleans[index] = b;
  }
  
  public boolean checkFlag(int index)
  {
    return booleans[index];
  }
}