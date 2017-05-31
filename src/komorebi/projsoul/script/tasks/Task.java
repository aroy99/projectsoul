package komorebi.projsoul.script.tasks;

import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.engine.ThreadHandler.TrackableThread;
import komorebi.projsoul.entities.Person.ActionState;

public class Task {

  public enum Precedence {
    BACKGROUND, FOREGROUND;
  }
  
  public Task(ActionState action, Precedence precedence)
  {
    this.action = action;
    this.precedence = precedence;
    
    waiting = ThreadHandler.currentThread();
  }
  
  protected boolean finished;
  private TrackableThread waiting;
  
  protected ActionState action;
  protected Precedence precedence;
  
  public boolean isFinished()
  {
    return finished;
  }
  
  protected void finish()
  {
    finished = true;
    waiting.unlock();
  }
  
  public boolean takesPrecedenceOver(Task task)
  {
    return (precedence.ordinal() > task.precedence().ordinal());
  }
  
  public Precedence precedence()
  {
    return precedence;
  }
  
  public boolean hasHighPrecedence()
  {
    return precedence == Precedence.FOREGROUND;
  }
}
