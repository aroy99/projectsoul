package komorebi.projsoul.entities.enemy;

public enum EnemyAI {
  NONE("none"), CHASER("chaser");
  
  
  private String str;
  private EnemyAI(String s)
  {
    str = s;
  }
  
  public String toString()
  {
    return str;
  }
  
  public static EnemyAI toEnum(String val)
  {
    switch (val)
    {
      case "none":
        return NONE;
      case "chaser":
        return CHASER;
      default:
        return NONE;
    }
  }
  
  public static String[] valuesAsString()
  {
    String[] vals = new String[values().length];
    
    for (int i = 0; i < values().length; i++)
    {
      vals[i] = values()[i].toString();
    }
    
    return vals;
  }
}
