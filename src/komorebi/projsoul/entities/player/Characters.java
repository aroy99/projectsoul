package komorebi.projsoul.entities.player;

public enum Characters {
  CASPIAN("Caspian", 0), FLANNERY("Flannery", 1),
  SIERRA("Sierra", 2), BRUNO("Bruno", 3);
  
  private String name;
  private int num;
  
  private Characters(String s, int num)
  {
    name = s;
    this.num = num;
  }
  
  public String getName()
  {
    return name;
  }
  
  public int getNumber()
  {
    return num;
  }
  
  public static Characters getCharacter(int i)
  {
    for (Characters c: Characters.values())
    {
      if (c.getNumber() == i) return c;
    }
    
    return null;
  }
}
