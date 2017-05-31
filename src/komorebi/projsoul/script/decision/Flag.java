package komorebi.projsoul.script.decision;

public class Flag {

  private String identifier;
  private boolean value;
  
  public Flag(String identifier)
  {
    this.identifier = identifier;
  }
  
  public void setValue(boolean value)
  {
    this.value = value;
  }
  
  public boolean getValue()
  {
    return value;
  }
  
  public String getIdentifier()
  {
    return identifier;
  }
}
