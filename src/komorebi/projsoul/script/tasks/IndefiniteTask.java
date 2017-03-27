package komorebi.projsoul.script.tasks;

import komorebi.projsoul.entities.Person.ActionState;
import komorebi.projsoul.script.Lock;

public class IndefiniteTask extends Task {

  public IndefiniteTask(ActionState action, Precedence precedence, Lock lock) {
    super(action, precedence, lock);
  }
  
  public void finish()
  {
    super.finish();
  }
}
