/**

 * NPC.java  Jun 9, 2016, 3:09:11 PM
 **/
package komorebi.projsoul.entities;

import java.awt.Rectangle;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Main;
import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.Lock;
import komorebi.projsoul.script.tasks.Task.Precedence;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.SpeechHandler;


/**
 * 
 * @author Andrew Faulkenberry
 */
public class NPC extends Person {

  private String name;

  private boolean started;

  public Face direction = Face.DOWN;

  private Rectangle[] surround = new Rectangle[4];

  private NPCType type;

  public Rectangle future;

  private String[] names = new String[4];

  private boolean isTalking, isWalking;

  private String walkScript, talkScript;
  
  private static final int TOP = 0, RIGHT = 1, BOTTOM = 2, LEFT = 3;

  boolean hangOn;
  boolean interruptWalking;

  /**
   * @param x The x location (in pixels) of the bottom left corner of the NPC
   * @param y The y location (in pixels) of the bottom left corner of the NPC
   */
  public NPC(String name, float x, float y, NPCType type) {
    super(x,y,16,24);

    area = new Rectangle((int) x, (int) y, 16, 24);

    this.name = name;

    setAttributes(type);

    text = new SpeechHandler();
    SpeechHandler.setSpeed(3);

    surround[TOP] = new Rectangle(16, 24);
    surround[RIGHT] = new Rectangle(16, 24);
    surround[BOTTOM] = new Rectangle(16, 24);
    surround[LEFT] = new Rectangle(16, 24);

    future = new Rectangle((int) this.x, (int) this.y, 16, 24);

    names[0] = "Top";
    names[1] = "Right";
    names[2] = "Bottom";
    names[3] = "Left";
    
    currentAni = downAni;
  }


  /* (non-Javadoc)
   * @see komorebi.clyde.engine.Renderable#update()
   */
  @Override

  /**z
   * Updates the behavior of the NPC, such as speed and movement
   */
  public void update() {

    super.update();

    refreshSurroundingRectangles();
    
  }
  
  private void refreshSurroundingRectangles()
  {
    surround[TOP].setLocation((int) this.x, (int) this.y+24);
    surround[RIGHT].setLocation((int) this.x + 16, (int) this.y);
    surround[BOTTOM].setLocation((int) this.x, (int) this.y - 24);
    surround[LEFT].setLocation((int) this.x - 16, (int) this.y);
    
    future.setLocation((int) x, (int) y);
  }

  /* (non-Javadoc)
   * @see komorebi.clyde.engine.Renderable#render()
   */
  @Override

  /**
   * Renders the image of the NPC on-screen
   */
  public void render() {

    super.render();

    text.render();

  }

  public void setNPCType(NPCType type)
  {
    setAttributes(type);
  }

  /**
   * Creates all of the required objects for the specified NPC type
   * 
   * @param type The NPC type to set the attributes
   */
  private void setAttributes(NPCType type)
  {
    this.type = type;
    switch (type){
      case POKEMON:
        leftAni = new Animation(3,8,16,24,3);
        rightAni = new Animation(3,8,16,24,3);
        upAni = new Animation(3,8,16,24,3);
        downAni = new Animation(3,8,16,24,3);

        downAni.add(1, 0);
        downAni.add(18, 0);
        downAni.add(35, 0);

        leftAni.add(51, 0);
        leftAni.add(67, 0);
        leftAni.add(83, 0);

        rightAni.add(51, 0, true);
        rightAni.add(67, 0, true);
        rightAni.add(83, 0, true);

        upAni.add(100, 0);
        upAni.add(117, 0);
        upAni.add(134, 0);
        break;
      case NESS:
        leftAni = new Animation(2,8,16,24,4);
        rightAni = new Animation(2,8,16,24,4);
        upAni = new Animation(2,8,16,24,4);
        downAni = new Animation(2,8,16,24,4);

        downAni.add(0, 0);
        downAni.add(17, 0);

        upAni.add(34, 0);
        upAni.add(34, 0, true);

        leftAni.add(51, 0);
        leftAni.add(68, 0);

        rightAni.add(51, 0, true);
        rightAni.add(68, 0, true);
        break;
      default:
        leftAni = new Animation(3,8,16,24,3);
        rightAni = new Animation(3,8,16,24,3);
        upAni = new Animation(3,8,16,24,3);
        downAni = new Animation(3,8,16,24,3);

        downAni.add(1, 0);
        downAni.add(18, 0);
        downAni.add(35, 0);

        leftAni.add(51, 0);
        leftAni.add(67, 0);
        leftAni.add(83, 0);

        rightAni.add(51, 0, true);
        rightAni.add(67, 0, true);
        rightAni.add(83, 0, true);

        upAni.add(100, 0);
        upAni.add(117, 0);
        upAni.add(134, 0);
        break;

    }
  }

  /**
   * Relocates the NPC to a specific spot on the screen
   * @param x The x cooridnate of the new bottom left corner, in pixels, of the NPC
   * @param y The y coordinate of the new bottom left corner, in pixels, of the NPC
   */
  public void setPixLocation(int x, int y)
  {
    this.x = x;
    surround[0].setLocation(x, y+24);
    surround[1].setLocation(x+16, y);
    surround[2].setLocation(x, y-24);
    surround[3].setLocation(x-16, y);

    this.y = y;
  }

  /**
   * Asks a question, creating a message box and pausing the thread
   * 
   * @param args The options to write
   * @param lock The new thread to run the command
   */
  public String ask(String[] args, Lock lock)
  {
    text.write(args[0], 20, 58, new EarthboundFont(1));
    if (args.length>1) text.write(args[1], 30, 40, new EarthboundFont(1));
    if (args.length>2) text.write(args[2], 100, 40, new EarthboundFont(1));
    if (args.length>3) text.write(args[3], 30, 22, new EarthboundFont(1));
    if (args.length>4) text.write(args[4], 100, 22, new EarthboundFont(1));

    //options = args;
    text.setOptions(args);
    text.drawPicker(1);

    Main.getGame().setMaxOptions(args.length-1);
    Main.getGame().setAsker(text);

    text.setLockAndPause(lock);
    return text.getAnswer();
  }

  public int getTileX()
  {
    return ((int) x)/16;
  }

  public int getTileY()
  {
    return ((int) y)/16;
  }

  public void setPickerIndex(int i)
  {
    text.setPickerIndex(i);
  }

  public NPCType getType()
  {
    return type;
  }

  public String getName()
  {
    return name;
  }


  /**
   * Runs the NPC's talking script when the player prompts them
   */
  public void approach()
  {
    isTalking = true;
    
    interrupt();
    runTalkingScript();
  }

  public void disengage()
  {
    letContinue();
    isTalking = false;
  }


  public void setTalkingScript(String nScript)
  {
    talkScript = nScript;
  }

  public void setWalkingScript(String nScript)
  {
    walkScript = nScript;
  }
 
  public String getTalkingScript()
  {
    return talkScript;
  }

  public String getWalkingScript()
  {
    return walkScript;
  }

  public boolean isTalking()
  {
    return isTalking;
  }

  public boolean isWalking()
  {
    return isWalking;
  }

  public void setIsTalking(boolean b)
  {
    isTalking = b;
  }

  public void setIsWalking(boolean b)
  {
    isWalking = b;
  }
  
  public void runWalkingScript()
  {
    isWalking = true;
    started = true;
    
    ThreadHandler.newLoop(walkScript, this, Map.getPlayer());
  }
  
  public void runTalkingScript()
  {
    isWalking = false;
    started = true;
    
    ThreadHandler.newThread(talkScript, this, Map.getPlayer());
  }

  public void move(float dx, float dy)
  {
    x+=dx;
    y+=dy;
    
    surround[0].setLocation((int) x, (int) y+24);
    surround[1].setLocation((int) x+16, (int) y);
    surround[2].setLocation((int) x, (int) y-24);
    surround[3].setLocation((int) x-16, (int) y);
  }


  public boolean started()
  {
    return started;
  }

  public Rectangle intersectedHitbox(Rectangle clyde)
  {
    for (int i=0; i<4; i++)
    {
      if (surround[i].intersects(clyde))
      {
        return surround[i];
      }
    }

    return null;
  }

  public Face faceMe(Rectangle clyde)
  {
    int number=-1;
    for (int i=0; i<4; i++)
    {
      if (surround[i].intersects(clyde))
      {
        number = i;
      }
    }

    switch (number)
    {
      case 0: return Face.DOWN;
      case 1: return Face.LEFT;
      case 2: return Face.UP;
      case 3: return Face.RIGHT;
      default: return null;
    }
  }

  /**
   * Directs the NPC to a specific tile location
   * @param horizontal Whether to move horizontally (true) or vertically (false)
   * @param tx The tile to move to
   * @param lock Lock to wait on
   */
  public void goTo(boolean horizontal, int tx, Precedence precedence, 
      Lock lock)
  {
    if (horizontal)
    {
      if (x>tx*16)
      {
        for (int i = 0; i < getTileX()-tx; i++)
          walk(Face.LEFT, lock);
      } else if (x<tx*16)
      { for (int i = 0; i < getTileX()-tx; i++)
        walk(Face.RIGHT, lock);
      }
    } else
    {
      if (y>ty*16)
      {for (int i = 0; i < getTileX()-tx; i++)
        walk(Face.DOWN, lock);
      } else if (y<ty*16)
      {
        for (int i = 0; i < ty-getTileY(); i++)
          walk(Face.UP, lock);
      }
    }

  }

  /**
   * @return Whether the player is standing right in the NPC's path
   */
  public boolean canMove(float dx, float dy)
  {
    future.x += dx;
    future.y += dy;
    
    if (future.intersects(Map.getPlayer().getArea()))
    {
      future.x -= dx;
      future.y -= dy;
      
      return false;
    }
    
    future.x -= dx;
    future.y -= dy;
    
    return true; 
    
  }

  public Rectangle getArea()
  {
    return area;
  }

  /**
   * 
   * @param clydeX
   * @param clydeY
   * @return
   */
  public boolean isApproached(Rectangle clyde, Face direction)
  {    
    return ((surround[0].intersects(clyde) && direction == Face.DOWN) ||
        (surround[1].intersects(clyde) && direction == Face.LEFT) ||
        (surround[2].intersects(clyde) && direction == Face.UP) ||
        (surround[3].intersects(clyde) && direction == Face.RIGHT)) && 
        !isTalking;
  }

  public void setName(String newName){
    name = newName;
  }
}