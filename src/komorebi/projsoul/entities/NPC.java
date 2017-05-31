/**

 * NPC.java  Jun 9, 2016, 3:09:11 PM
 **/
package komorebi.projsoul.entities;

import komorebi.projsoul.engine.Main;
import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.map.MapHandler;
import komorebi.projsoul.script.tasks.Task.Precedence;
import komorebi.projsoul.script.text.Font;
import komorebi.projsoul.script.text.MenuFont;
import komorebi.projsoul.script.text.SpeechHandler;

import java.awt.Rectangle;


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
  private static Font font = new MenuFont(1);


  /**
   * @param x The x location (in pixels) of the bottom left corner of the NPC
   * @param y The y location (in pixels) of the bottom left corner of the NPC
   */
  public NPC(String name, float x, float y, NPCType type) {
    super(x,y,16,24);

    area = new Rectangle((int) x, (int) y, 16, 24);

    this.name = name;

    setNPCType(type);

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

    sprites.turn(Face.DOWN);
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

  }

  public void setNPCType(NPCType type)
  {
    this.type = type;
    this.sprites = type.getNewSpriteSet();
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
  public String ask(String[] args)
  {
    text.clear();
    text.write(args[0], 20, 58, font);

    if (args.length > 1){
      text.write(args[1], 30, 40, font);
    }
    if (args.length > 2){
      text.write(args[2], 100, 40,font);
    }
    if (args.length > 3){
      text.write(args[3], 30, 22, font);
    }
    if (args.length > 4){
      text.write(args[4], 100, 22,font);
    }


    //options = args;
    text.setOptions(args);

    Main.getGame().setAsker(text);

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

    ThreadHandler.newLoop(walkScript, this, MapHandler.getPlayer());
  }

  public void runTalkingScript()
  {
    isWalking = false;
    started = true;

    ThreadHandler.newThread(talkScript, this, MapHandler.getPlayer());
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
  public void goTo(boolean horizontal, int tx, Precedence precedence)
  {
    if (horizontal)
    {
      if (x>tx*16)
      {
        for (int i = 0; i < getTileX()-tx; i++)
          walk(Face.LEFT);
      } else if (x<tx*16)
      { for (int i = 0; i < getTileX()-tx; i++)
        walk(Face.RIGHT);
      }
    } else
    {
      if (y>ty*16)
      {for (int i = 0; i < getTileX()-tx; i++)
        walk(Face.DOWN);
      } else if (y<ty*16)
      {
        for (int i = 0; i < ty-getTileY(); i++)
          walk(Face.UP);
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

    if (future.intersects(MapHandler.getPlayer().getArea()))
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
  public boolean isApproached(Rectangle player, Face direction)
  {    
    return ((surround[0].intersects(player) && direction == Face.DOWN) ||
        (surround[1].intersects(player) && direction == Face.LEFT) ||
        (surround[2].intersects(player) && direction == Face.UP) ||
        (surround[3].intersects(player) && direction == Face.RIGHT)) && 
        !isTalking;
  }

  public void setName(String newName){
    name = newName;
  }

  public Rectangle getSurroundingRectangle(Face dir)
  {
    switch (dir)
    {
      case DOWN:
        return surround[BOTTOM];
      case LEFT:
        return surround[LEFT];
      case RIGHT:
        return surround[RIGHT];
      default:
        return surround[TOP];
    }
  }

  /**
   * Destroys all outstanding threads that this NPC has running
   */
  public void cleanUp(){
    //    talkScript.close();
    //    walkScript.close();
  }
}