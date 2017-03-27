package komorebi.projsoul.script.tasks;

import komorebi.projsoul.entities.Person.ActionState;
import komorebi.projsoul.script.Lock;

public class Task {

  public enum Precedence {
    BACKGROUND, FOREGROUND;
  }
  
  public Task(ActionState action, Precedence precedence, Lock lock)
  {
    this.action = action;
    this.precedence = precedence;
    this.lock = lock;
  }
  
  protected Lock lock;
  protected boolean finished;

  protected ActionState action;
  protected Precedence precedence;
  
  public boolean isFinished()
  {
    return finished;
  }
  
  protected void finish()
  {
    finished = true;
    lock.resumeThread();
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
