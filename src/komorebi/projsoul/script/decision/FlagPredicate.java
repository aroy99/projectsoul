package komorebi.projsoul.script.decision;

public class FlagPredicate implements Predicate {

  private Flag flag;
  
  public FlagPredicate(Flag flag)
  {
    this.flag = flag;
  }
  
  @Override
  public boolean evaluatesTrue() {
    return flag.getValue();
  }

}
