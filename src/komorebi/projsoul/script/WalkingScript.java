/**
 * WalkingScript.java  
 * Jul 7, 2016, 3:06:51 PM
 */
package komorebi.projsoul.script;

import komorebi.projsoul.entities.NPC;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class WalkingScript extends Script {
  
  public WalkingScript(String s, NPC npc)
  {
    this.script = s;
    this.npc = npc;
  }
  
  /**
   * Runs the walking script
   */
  public void run()
  {
    execution.setLoopable(true);
    super.run();
  }
  
 
  /* (non-Javadoc)
   * @see komorebi.clyde.script.Script#abort()
   */
  @Override
  public void abort() {
    isInterrupted = true;
    pause();

  }
}
