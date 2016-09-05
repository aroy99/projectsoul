/**
 * AreaScript.java  Jun 13, 2016, 9:38:40 AM
 */
package komorebi.projsoul.script;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.entities.Clyde;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.map.EditorMap;

/**
 * 
 * @author Andrew Faulkenberry
 */
public class AreaScript extends Script{

  private boolean hasRun = false;
  private boolean isRepeatable;

  protected float x, y;
  protected int tx, ty;

  /**
   * Creates a new area script
   * 
   * @param s The name of the script in res/scripts/
   * @param x The x location (in tiles) of the script
   * @param y The y location (in tiles) of the script
   * @param repeat Whether the script can be repeated or not
   */
  public AreaScript(String s, float x, float y, boolean repeat)
  {
    tx = (int)(x-EditorMap.getX())/16;
    ty = (int)(y-EditorMap.getY())/16;
    script = s;
    isRepeatable = repeat;
    this.x=x;
    this.y=y;

  }

  /**
   * Creates a new area script
   * @param s The name of the script in res/scripts/
   * @param x The x location (in tiles) of the script
   * @param y The y location (in tiles) of the script
   * @param repeat Whether the script can be repeated or not
   * @param person The NPC to whom the script will be applied
   */
  public AreaScript(String s, float x, float y, boolean repeat, NPC person)
  {
    tx = (int)(x-EditorMap.getX())/16;
    ty = (int)(y-EditorMap.getY())/16;
    
    script = s;
    isRepeatable = repeat;
    this.x=x;
    this.y=y;
    npc = person;

  }

  /**
   * Executes the script
   */
  public void run()
  {
    
    hasRun = true;

    super.run();

  }

  public boolean isLocationIntersected(Clyde clyde)
  {
    return (clyde.getTileX()==getTileX() && clyde.getTileY()==getTileY());
  }

  public boolean hasRun()
  {
    return hasRun;
  }

  public int getTileX() {
    return (int) x/16;
  }

  public int getTileY() {
    return (int) y/16;
  }
  
  /**
   * @return the original tile x of this Script
   */
  public int getOrigTX(){
    return tx;
  }

  /**
   * @return the original tile y of this Script
   */
  public int getOrigTY(){
    return ty;
  }


  public float getX(){
    return x;
  }
  
  public float getY(){
    return y;
  }
  
  public void setAbsoluteLocation(float x, float y)
  {
    this.x=x;
    this.y=y;
  }
  
  public void move(float dx, float dy)
  {
    x+=dx;
    y+=dy;
  }

  public String getName()
  {
    return script;
  }
  
  public NPC getNPC(){
    return npc;
  }
  
  public boolean hasNPC(){
    return npc != null;
  }


  /* (non-Javadoc)
   * @see komorebi.clyde.script.Script#abort()
   */
  @Override
  public void abort() {
    
  }

  /**
   * Renders the "S" tile
   */
  public void render() {
    Draw.rect(x, y, 16, 16, 32, 0, 2);
  }

}
