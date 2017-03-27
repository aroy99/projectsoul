package komorebi.projsoul.script.commands.abstracts;

import komorebi.projsoul.script.Lock;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;
import komorebi.projsoul.script.read.Request;
import komorebi.projsoul.script.tasks.Task.Precedence;

public abstract class Command {

  protected Lock lock;
  private boolean appliesToPlayer;
    
  public void provideLock(Lock lock)
  {
    this.lock = lock;
  }
  
  public void setApplicableToPlayer(boolean b)
  {
    appliesToPlayer = b;
  }
  
  public Request[] askForRequests()
  {
    return new Request[0];
  }
 
  public boolean appliesToPlayer()
  {
    return appliesToPlayer;
  }
  
  public abstract void interpret(String data) throws InvalidScriptSyntaxException;
}
