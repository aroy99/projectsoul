/**

 * NPC.java  Jun 9, 2016, 3:09:11 PM
 **/
package komorebi.projsoul.entities;

import java.awt.Rectangle;
import java.util.ArrayList;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Main;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.EarthboundFont;
import komorebi.projsoul.script.Execution;
import komorebi.projsoul.script.Lock;
import komorebi.projsoul.script.SpeechHandler;
import komorebi.projsoul.script.TalkingScript;
import komorebi.projsoul.script.WalkingScript;


/**
 * 
 * @author Andrew Faulkenberry
 */
public class NPC extends Entity {

  private static ArrayList<NPC> npcs = new ArrayList<NPC>();

  private String name;

  private boolean started;

  public Face direction = Face.DOWN;
  private SpeechHandler text;
  
  private Rectangle[] surround = new Rectangle[4];
  
  private boolean isVisible, isMoving, isRunning, isWaiting;
  private boolean hasInstructions;

  private Execution instructor;

  private NPCType type;

  private int dx, dy;
  private int framesToGo;

  private int xTravelled;
  private int yTravelled;

  public Rectangle future;

  private String[] names = new String[4];
  private TalkingScript talkScript;

  private boolean isTalking, isWalking;

  private WalkingScript walkScript;

  Animation rightAni, leftAni, downAni, upAni;

  private int prevdx, prevdy;
  private int prevFrames;
  private Face prevDir;
  private boolean wasHangingOn;
  
  private Lock lock;
  private Lock prevLock;

  boolean hangOn;
  
  int tx, ty;

  /**
   * @param x The x location (in pixels) of the bottom left corner of the NPC
   * @param y The y location (in pixels) of the bottom left corner of the NPC
   */
  public NPC(String name, float x, float y, NPCType type) {
    super(x,y,16,24);
    tx = (int)(x-EditorMap.getX())/16;
    ty = (int)(y-EditorMap.getY())/16;
    
    area = new Rectangle((int) x, (int) y, 16, 24);

    this.name = name;

    setAttributes(type);

    isMoving=false;
    hasInstructions=false;
    isVisible = true;

    text = new SpeechHandler();
    SpeechHandler.setSpeed(3);

    surround[0] = new Rectangle((int) this.x, (int) this.y+24, 16, 24);
    surround[1] = new Rectangle((int) this.x + 16, (int) this.y, 16, 24);
    surround[2] = new Rectangle((int) this.x, (int) this.y - 24, 16, 24);
    surround[3] = new Rectangle((int) this.x - 16, (int) this.y, 16, 24);

    future = new Rectangle((int) this.x, (int) this.y, 16, 24);

    names[0] = "Top";
    names[1] = "Right";
    names[2] = "Bottom";
    names[3] = "Left";

  }

  public NPC(String name)
  {
    super(0,0,16,24);
    
    this.name = name;
    ent=Entities.NPC;

    isVisible = false;

    isMoving = false;
    hasInstructions=false;

    text = new SpeechHandler();

  }


  /* (non-Javadoc)
   * @see komorebi.clyde.engine.Renderable#update()
   */
  @Override

  /**z
   * Updates the behavior of the NPC, such as speed and movement
   */
  public void update() {
            
    System.out.println(framesToGo);
    
    if (framesToGo <= 0 && hasInstructions)
    {
      isMoving=false;
      isRunning=false;
      isWaiting=false;
      dx=0;
      dy=0;

      hasInstructions=false;

      if (lock != null)
      {
        lock.resumeThread();
      } 

    }
    
    if (isMoving)
    {      
      if (!hangOn && !frontClear() && isWalking)
      {
        hangOn = true;

        prevdx = dx;
        prevdy = dy;

        dx=0;
        dy=0;
        
        prevDir = direction;

        downAni.hStop();
        upAni.hStop();
        leftAni.hStop();
        rightAni.hStop();


      } else if (hangOn && frontClear() && isWalking)
      {        
        hangOn = false;

        dx = prevdx;
        dy = prevdy;
        
        direction = prevDir;
      }

      rx+=dx;
      ry+=dy;

      if (isRunning)
      {
        downAni.setSpeed(8);
        leftAni.setSpeed(8);
        rightAni.setSpeed(8);
        upAni.setSpeed(8);

      } else
      {
        downAni.setSpeed(4);
        leftAni.setSpeed(4);
        rightAni.setSpeed(4);
        upAni.setSpeed(4);
      }

      if (!hangOn)
      {
        switch (direction)
        {
          case DOWN:
            downAni.resume();
            break;
          case LEFT:
            leftAni.resume();
            break;
          case RIGHT:
            rightAni.resume();
            break;
          case UP:
            upAni.resume();
            break;
          default:
            break;
        }
      }
    } else
    {
      downAni.hStop();
      upAni.hStop();
      leftAni.hStop();
      rightAni.hStop();
    }
    
    x+=dx;
    xTravelled+=dx;

    y+=dy;
    yTravelled+=dy;
    
    future.x+=dx;
    future.y+=dy;
    
    area.x += dx;
    area.y += dy;

    for (Rectangle r: surround)
    {
      r.x += dx;
      r.y += dy;
    }

    if (!hangOn || isTalking)
    {
      if (dx != 0) {
        framesToGo-=Math.abs(dx);
      } else if (dy != 0){
        framesToGo-=Math.abs(dy);
      } else if (isWaiting){
        framesToGo--;
      }
    }



  }

  /* (non-Javadoc)
   * @see komorebi.clyde.engine.Renderable#render()
   */
  @Override

  /**
   * Renders the image of the NPC on-screen
   */
  public void render() {

    if (isVisible) {
      switch (direction)
      {
        case DOWN:
          downAni.playCam(x,y);
          break;
        case LEFT:
          leftAni.playCam(x,y);
          break;
        case RIGHT:
          rightAni.playCam(x,y);
          break;
        case UP:
          upAni.playCam(x,y);
          break;
        default:
          break;
      }

      text.render();

      Draw.rectCam(area.x, area.y, area.width, area.height, 
          220, 0, 221, 1, 6);
      
    }

  }

  /**
   * Moves the NPC indefinitely in a given direction
   * @param dir The direction in which the NPC should move
   */
  public void walk(Face dir)
  {

    isMoving=true;
    isRunning=false;
    this.direction = dir;

    switch (dir)
    {
      case DOWN:
        dx=0;
        dy=-1;
        break;
      case LEFT:
        dx=-1;
        dy=0;
        break;
      case RIGHT:
        dx=1;
        dy=0;
        break;
      case UP:
        dx=0;
        dy=1;
        break;
      default:
        break;

    }

  }

  /**
   * Moves the NPC a given number of tiles in a specified direction
   * 
   * @param dir The direction in which the NPC should move
   * @param tiles The number of tiles the NPC should move, where one tile is 
   *         equal to 16 pixels 
   */
  private void walk(Face dir, int tiles)
  {

    hasInstructions=true;
    framesToGo = tiles*16;
    isMoving=true;
    isRunning=false;
    this.direction = dir;

    switch (dir)
    {
      case DOWN:
        dx=0;
        dy=-1;
        break;
      case LEFT:
        dx=-1;
        dy=0;
        break;
      case RIGHT:
        dx=1;
        dy=0;
        break;
      case UP:
        dx=0;
        dy=1;
        break;
      default:
        break;

    }

  }

  /**
   * Moves the NPC a given number of tiles in a specified direction, pausing the
   * thread
   * 
   * @param dir The direction in which the NPC should move
   * @param tiles The number of tiles the NPC should move, where one tile is 
   *         equal to 16 pixels 
   * @param ex The new thread to run the command The new thread to run the command
   */
  @Deprecated
  public void walk(Face dir, int tiles, Execution ex)
  {
    this.instructor = ex;

    walk(dir,tiles);
    instructor.getLock().pauseThread();

  }

  public void walk(Face dir, int tiles, Lock lock)
  {

    this.lock = lock;

    walk(dir,tiles);
    lock.pauseThread();
  }

  /**
   * Moves the NPC a given number of tiles in a specified direction at a brisk 
   * pace
   * 
   * @param dir The direction in which the NPC should move
   * @param tiles The number of tiles the NPC should move, where one tile is 
   *         equal to 16 pixels 
   */
  private void jog(Face dir, int tiles)
  {
    hasInstructions=true;
    framesToGo = tiles*16;
    isMoving=true;
    isRunning=true;
    this.direction = dir;

    switch (dir)
    {
      case DOWN:
        dx=0;
        dy=-2;
        break;
      case LEFT:
        dx=-2;
        dy=0;
        break;
      case RIGHT:
        dx=2;
        dy=0;
        break;
      case UP:
        dx=0;
        dy=2;
        break;
      default:
        break;

    }
  }


  /**
   * Moves the NPC a given number of tiles in a specified direction at a brisk 
   * pace, pausing the thread
   * 
   * @param dir The direction in which the NPC should move
   * @param tiles The number of tiles the NPC should move, where one tile is
   *         equal to 16 pixels 
   * @param instructor The new thread to run the command The new thread to run the command
   */
  @Deprecated
  public void jog(Face dir, int tiles, Execution instructor)
  { 

    this.instructor = instructor;
    jog(dir,tiles);
    this.instructor.getLock().pauseThread();

  }

  public void jog(Face dir, int tiles, Lock lock)
  {
    this.lock = lock;
    jog(dir,tiles);
    lock.pauseThread();
  }

  /**
   * Turns the NPC to face a different direction
   * 
   * @param dir The direction for the NPC to face
   */
  public void turn(Face dir)
  {
    direction=dir;
  }

  /**
   * Turns the NPC to face a different direction, pausing the thread
   * 
   * @param dir The direction for the NPC to face
   * @param instructor The new thread to run the command The new thread to run the command
   */
  public void turn(Face dir, Execution instructor)
  {
    this.instructor = instructor;
    turn(dir);

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
  public void setAttributes(NPCType type)
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
   * Moves the NPC to a new tile
   * 
   * @param tx The tile x location of the bottom left corner of the NPC
   * @param ty The tile y location of the bottom left corner of the NPC
   */
  public void setTileLocation(int tx, int ty){
    this.x=tx*16;
    this.y=ty*16;
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
   * @param instructor The new thread to run the command
   */
  public String ask(String[] args, Execution ex, Lock lock)
  {
    text.write(args[0], 20, 58, new EarthboundFont(1));
    if (args.length>1) text.write(args[1], 30, 40, new EarthboundFont(1));
    if (args.length>2) text.write(args[2], 100, 40, new EarthboundFont(1));
    if (args.length>3) text.write(args[3], 30, 22, new EarthboundFont(1));
    if (args.length>4) text.write(args[4], 100, 22, new EarthboundFont(1));

    this.instructor = ex;
    this.lock = lock;

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
    return ((int) rx)/16;
  }

  public int getTileY()
  {
    return ((int) ry)/16;
  }

  /**
   * @return the original tile x of this NPC
   */
  public int getOrigTX(){
    return tx;
  }

  /**
   * @return the original tile y of this NPC
   */
  public int getOrigTY(){
    return ty;
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

  public static void add(NPC person)
  {
    npcs.add(person);
  }

  /**
   * Returns the NPC that matches the name provided
   * 
   * @param s The name input
   * @return The NPC, if none found, null
   */
  public static NPC get(String s)
  {
    for (NPC n: npcs)
    {
      if (n.getName().equals(s)) 
      {
        return n;
      }
    }

    return null;
  }

  public static ArrayList<NPC> getNPCs()
  {
    return npcs;
  }

  public void setVisible(boolean b)
  {
    isVisible = b;
  }

  public int getXTravelled()
  {
    return xTravelled;
  }

  public int getYTravelled()
  {
    return yTravelled;
  }

  

  /**
   * Runs the NPC's talking script when Clyde prompts them
   */
  public void approach()
  {
    prevFrames = framesToGo;
    wasHangingOn = hangOn;
    
    prevLock = lock;
    
    abortWalkingScript();
    talkScript.run();
  }

  public void disengage()
  {
    if (isTalking)
    {
      walkScript.resume();
      talkScript.setIsRunning(false);
      isWalking = true;
    } else
    {
      isWalking = false;
      walkScript.setIsRunning(false);
    }
    isTalking = false;    
    
    if (prevFrames > 0)
    {
      isMoving = true;
      hasInstructions = true;
    }
    
    hangOn = wasHangingOn;
    framesToGo = prevFrames;
    
    lock = prevLock;

  }

  public void setTalkingScript(TalkingScript nScript)
  {
    talkScript = nScript;
  }

  public void setWalkingScript(WalkingScript nScript)
  {
    walkScript = nScript;
  }

  public void abortTalkingScript()
  {
    talkScript.pause();
    isTalking = false;
    isWalking = true;
  }

  public void abortWalkingScript()
  {
    walkScript.pause();
    isTalking = true;
    isWalking = false;
  }

  public TalkingScript getTalkingScript()
  {
    return talkScript;
  }

  public WalkingScript getWalkingScript()
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
    walkScript.run();
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
  public void goTo(boolean horizontal, int tx, Lock lock)
  {
    if (horizontal)
    {
      if (x>tx*16)
      {
        walk(Face.LEFT, getTileX()-tx);
      } else if (x<tx*16)
      {
        walk(Face.RIGHT, tx-getTileX(), lock);
      }
    } else
    {
      if (y>tx*16)
      {
        walk(Face.DOWN, getTileY()-tx, lock);
      } else if (y<tx*16)
      {
        walk(Face.UP, tx-getTileY(), lock);
      }
    }

  }

  /**
   * @return Whether the player is standing right in the NPC's path
   */
  public boolean frontClear()
  {
    boolean get;
    
    future.x+=dx;
    future.y+=dy;
    
    if (direction == Face.LEFT || direction == Face.RIGHT)
    {
      future.grow(1, 0);
    } else
    {
      future.grow(0, 1);
    }
    
    get = !future.intersects(Map.getPlayer().getArea());

    if (direction == Face.LEFT || direction == Face.RIGHT)
    {
      future.grow(-1, 0);
    } else
    {
      future.grow(0, -1);
    }
    
    future.x-=dx;
    future.y-=dy;
    
    return get;
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
        (surround[3].intersects(clyde) && direction == Face.RIGHT)) && !isTalking;
  }
  
  public void say(String s, Lock lock)
  {
    text.write(s, 20, 58, new EarthboundFont(1));
    Main.getGame().setSpeaker(text);
    text.setAndLock(lock);
  }
  
  public void pause(int frames, Lock lock)
  {
    framesToGo = frames;
    isWaiting = true;
    hasInstructions = true;
    
    this.lock = lock;
    lock.pauseThread();
    
  }
}