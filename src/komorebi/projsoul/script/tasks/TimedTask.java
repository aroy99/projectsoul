package komorebi.projsoul.script.tasks;

import komorebi.projsoul.entities.Person.ActionState;

public class TimedTask extends Task {
    
  public TimedTask(ActionState action, Precedence precedence, int frames)
  {
    super(action, precedence);
    framesRemaining = frames;
  }
  
  private int framesRemaining;  
  
  protected int decrement = 1;
  
  public void decrement()
  {
    framesRemaining-=decrement;
    
    if (framesRemaining <= 0)
    {
      finish();
    }
  }
  
  public int ticksLeft()
  {
    return framesRemaining;
  }
  
  public ActionState action()
  {
    return action;
  } 
}
