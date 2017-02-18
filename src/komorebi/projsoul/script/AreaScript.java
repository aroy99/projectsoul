/**
 * AreaScript.java  Jun 13, 2016, 9:38:40 AM
 */
package komorebi.projsoul.script;

import komorebi.projsoul.editor.Editable;
import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.map.EditorMap;

/**
 * 
 * @author Andrew Faulkenberry
 */
public class AreaScript extends Script implements Editable{

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
    this.x=x;
    this.y=y;
    
    tx = (int)(x-EditorMap.getX())/16;
    ty = (int)(y-EditorMap.getY())/16;
    script = s;
    isRepeatable = repeat;
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
    this.x=x;
    this.y=y;
    
    tx = (int)(x-EditorMap.getX())/16;
    ty = (int)(y-EditorMap.getY())/16;
    script = s;
    isRepeatable = repeat;
    
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

  public boolean isLocationIntersected(int tx, int ty)
  {
    return (tx==getTileX() && ty==getTileY());
  }

  public boolean hasRun()
  {
    return hasRun;
  }

  public int getTileX() {
    return (int) (x/16);
  }

  public int getTileY() {
    return (int) (y/16);
  }
  
  public int getOrigTX(){
    return tx;
  }

  public int getOrigTY(){
    return ty;
  }


  public float getX(){
    return x;
  }
  
  public float getY(){
    return y;
  }
  
  public void setPixLocation(int x, int y)
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

  public void setScript(String newScript){
    script = newScript;
  }
  
  public NPC getNPC(){
    return npc;
  }

  public void setNPC(NPC newNPC){
    npc = newNPC;
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
  public void renderE() {
    Draw.rectZoom(x, y, 16, 16, 32, 0, 48, 16, 2, 
        Editor.zoom(), EditorMap.getX(), EditorMap.getY());
  }
  
  /**
   * @return Whether the script is repeatable
   */
  public boolean isRepeatable() {
    return isRepeatable;
  }
  
  /**
   * @param isRepeatable Whether the script will repeat or not
   */
  public void setRepeatable(boolean isRepeatable) {
    this.isRepeatable = isRepeatable;
  }

  /**
   * Moves the Script to a new tile (should only be used in Editor)
   * 
   * @param tx The tile x location of the bottom left corner of the Script
   * @param ty The tile y location of the bottom left corner of the Script
   */
  public void setTileLocation(int tx, int ty){
    this.x=tx*16+EditorMap.getX();
    this.y=ty*16+EditorMap.getY();
    
    this.tx = tx;
    this.ty = ty;
  }


}
