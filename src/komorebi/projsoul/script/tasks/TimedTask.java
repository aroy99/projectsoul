package komorebi.projsoul.script.tasks;

import komorebi.projsoul.entities.Person.ActionState;
import komorebi.projsoul.script.Lock;
import komorebi.projsoul.script.tasks.Task.Precedence;

public class TimedTask extends Task {
    
  public TimedTask(ActionState action, Precedence precedence, int frames, 
      Lock lock)
  {
    super(action, precedence, lock);
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
  
  public ActionState action()
  {
    return action;
  }
  
  public void lock()
  {
    lock.pauseThread();
  }
  

  
}
