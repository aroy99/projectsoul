/**
 * WarpScript.java  Jul 6, 2016, 1:51:41 PM
 */
package komorebi.projsoul.script;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.script.Task.TaskWithNumber;
import komorebi.projsoul.script.Task.TaskWithString;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class WarpScript extends AreaScript {

  String map;
  /** Creates a new warp script object
   * @param map The map file to which the warp will send Clyde
   * @param x The x location of the warp (tiles)
   * @param y The y location of the warp (tiles)
   * @param repeat Whether the warp is repeatable
   */
  public WarpScript(String map, float x, float y, boolean repeat) {
    super(map, x, y, repeat);
    this.map = map;
  }
  
  public String getMap()
  {
    return map;
  }
  
  public void setMap(String newMap){
    map = newMap;
  }
  
  public void read()
  {
   
    InstructionList instructions = new InstructionList("Main");
    instructions.add(new Task(Instructions.FADE_OUT));
    
    instructions.add(new TaskWithString(Instructions.LOAD_MAP, map));
    
    instructions.add(new TaskWithNumber(Instructions.WAIT, 40));
    instructions.add(new Task(Instructions.FADE_IN));
    
    
    execution = new Execution(null, instructions);
  }
  
  /**
   * Renders the "W" tile
   */
  public void render(){
    Draw.rect(x, y, 16, 16, 48, 0, 2);
  }

}
