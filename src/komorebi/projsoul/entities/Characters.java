package komorebi.projsoul.entities;

public enum Characters {
  CASPIAN("Caspian"), FLANNERY("Flannery"), SIERRA("Sierra"), BRUNO("Bruno");
  
  private String name;
  
  private Characters(String s)
  {
    name = s;
  }
  
  public String getName()
  {
    return name;
  }
}
