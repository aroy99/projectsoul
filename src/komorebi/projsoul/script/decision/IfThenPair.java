package komorebi.projsoul.script.decision;

public class IfThenPair extends DecisionStatement {

  private Predicate predicate;
  
  public IfThenPair(Predicate predicate)
  {
    this.predicate = predicate;
  }
  
  public boolean evaluatesTrue()
  {
    return predicate.evaluatesTrue();
  }
}
