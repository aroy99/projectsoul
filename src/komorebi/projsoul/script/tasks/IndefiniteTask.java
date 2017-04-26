package komorebi.projsoul.script.tasks;

import komorebi.projsoul.entities.Person.ActionState;

public class IndefiniteTask extends Task {

  public IndefiniteTask(ActionState action, Precedence precedence) {
    super(action, precedence);
  }
  
  public void finish()
  {
    super.finish();
  }
}
