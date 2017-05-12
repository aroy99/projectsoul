package komorebi.projsoul.script.decision;

public class IfStatement extends DecisionStatement {

  private Predicate predicate;
  
  public IfStatement(Predicate predicate)
  {
    this.predicate = predicate;
  }
  
  public boolean evaluatesTrue()
  {
    return predicate.evaluatesTrue();
  }
}
