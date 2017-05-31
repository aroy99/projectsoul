package komorebi.projsoul.script.execute;

import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.script.read.Branch;

public class LoopableExecution extends Execution {

  public LoopableExecution(Branch branch) {
    super(branch);
  }
  
  @Override
  public void run()
  {
    while (!ThreadHandler.currentThread().isTerminated())
    {
      super.run();
    }
  }

}
