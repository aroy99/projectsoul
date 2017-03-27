package komorebi.projsoul.script.utils;

import komorebi.projsoul.entities.NPC;

public class AreaScript {

  private String script;
  private int tx, ty;
  private NPC executeUpon;
  
  public AreaScript(String name, int tx, int ty)
  {
    script = name;
    
    this.tx = tx;
    this.ty = ty;
  }
  
  public void executeUpon(NPC npc)
  {
    executeUpon = npc;
  }
  
  public String getName()
  {
    return script;
  }
  
  public void setLocation(int tx, int ty)
  {
    this.tx = tx;
    this.ty = ty;
  }
  
  /*TODO: MAKE MORE ROBUST*/
  public boolean isLocationIntersected(int playx, int playy)
  {
    return tx == playx && ty == playy;
  }
  
}
