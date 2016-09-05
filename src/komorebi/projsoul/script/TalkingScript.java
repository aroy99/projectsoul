/**
 * TalkingScript.java  Jul 6, 2016, 7:02:29 PM
 */
package komorebi.projsoul.script;

import komorebi.projsoul.entities.NPC;

/**
 * 
 * 
 * @author Andrew Faulkenberry
 */
public class TalkingScript extends Script {
    
  public TalkingScript(String script, NPC npc)
  {
    this.script = script;
    this.npc = npc;
  }
  
  public String getScript()
  {
    return script;
  }
  
  public NPC getNPC()
  {
    return npc;
  }

  /* (non-Javadoc)
   * @see komorebi.clyde.script.Script#abort()
   */
  @Override
  public void abort() {
    setIsRunning(false);
  }
  
  /**
   * Sets whether the talking script is running at a given moment
   * If it is not, will signify as such to the associated NPC
   */
  public void setIsRunning(boolean b)
  {
    super.setIsRunning(b);
    if (!b)
    {
      npc.setIsTalking(b);
    }
  }
}
