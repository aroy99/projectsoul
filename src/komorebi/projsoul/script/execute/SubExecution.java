package komorebi.projsoul.script.execute;

import komorebi.projsoul.engine.ThreadHandler.TrackableThread;
import komorebi.projsoul.script.read.Branch;

public class SubExecution extends Execution {

  private TrackableThread returnTo;
  
  public SubExecution(Branch branch, TrackableThread returnTo) {
    super(branch);
    
    this.returnTo = returnTo;
  }
  
  public void run()
  {
    super.run();
    
    returnTo.unlock();
  }

  
}
