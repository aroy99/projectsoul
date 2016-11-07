
/**
 * Game.java        May 16, 2016, 9:52:23 PM
 */
package komorebi.projsoul.states;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.HUD;
import komorebi.projsoul.engine.Item;
import komorebi.projsoul.engine.Item.Items;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MagicBar;
import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.entities.Player;
import komorebi.projsoul.entities.SignPost;
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

/**
 * Represents the game
 * 
 * @author Aaron Roy
 * @version 
 */
public class Game extends State{

  public ArrayList<NPC> npcs;
  public ArrayList<AreaScript> scripts;
  public ArrayList<Item> items = new ArrayList<Item>();
 
  public boolean[] booleans;

  private boolean hasText, hasChoice;
  private int pickIndex;
  private int maxOpt;

  private SpeechHandler speaker;

  private BufferedReader read;
  
  private int confidence, money;
  
  public int framesToGo;
  public boolean isPaused;
  public Lock lock;
  
  public static String testLoc;
  
  public HUD hud;
  public Death death;
  
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

    npcs = new ArrayList<NPC>();
    scripts = new ArrayList<AreaScript>();

    booleans = new boolean[256];

    confidence = 0;
    money = 15;
    
    hud = new HUD(confidence);
    death = new Death();
    


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
              
              speaker.releaseLocks();
              speaker = null;
            }
          }
        } 
      } 
    }
              
             


    //TODO Debug
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
        //choosesLeft=!choosesLeft;

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
    // TODO Auto-generated method stub  
	  hud.update();
	  death.update();
	  
    KeyHandler.getInput();
    
    if (isPaused)
    {
      framesToGo--;
      
      if (framesToGo<=0)
      {
        isPaused = false;
        lock.resumeThread();
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
    Fader.render();

  }


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
   * @param frames The number of frames to be paused
   * @param ex The script execution whose thread will be locked while the game
   *        is paused
   */


  public void pause(int frames, Lock lock)
  {
    framesToGo = frames;
    isPaused = true;
    
    this.lock = lock;
    lock.pauseThread();
    
  }

  /**
   * Sets the NPC currently presenting a question to the player
   * @param npc The asking NPCS
   */
  public void setAsker(SpeechHandler talk)
  {
    this.speaker = talk;
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
   * Loads a given map into the game
   * @param mapFile The name of the file (sans .txt) in the res/ folder
   */
  public void loadMap(String mapFile)
  {
    try {
      read = new BufferedReader(
          new FileReader(new File("res/maps/"+mapFile+".txt")));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
    String s;

    try {
      while ((s = read.readLine()) != null) {
        if (s.startsWith("npc"))
        {
          s = s.replace("npc ", "");
          String[] split = s.split(" ");

          NPC.add(new NPC(split[0], Integer.parseInt(split[1]), 
              Integer.parseInt(split[2]), NPCType.toEnum(split[3])));
        } else if (s.startsWith("script"))
        {
          s = s.replace("script ", "");
          String[] split = s.split(" ");

          scripts.add(new AreaScript(split[0], Integer.parseInt(split[1]), 
              Integer.parseInt(split[2]), false, NPC.get(split[3])));
        }

      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


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