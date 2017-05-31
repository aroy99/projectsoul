package komorebi.projsoul.editor;

public enum LayerType {
  COVERAGE("Coverage", 89, 82),
  L3("Layer 3", 73, 82),
  STRUCTURES("Structures", 57, 82),
  TERRAIN("Terrain", 41, 82);
  
  int notSelectedX, notSelectedY;
  String str;
  
  static int[] subNums = new int[4];
  
  public static String generateName(LayerType t)
  {
    String ret = t.toString().substring(0, 3) + subNums[numOf(t)];
    subNums[numOf(t)]++;
    return ret;
  }
  
  public String toString()
  {
    return str;
  }
  
  public int getTexX()
  {
    return notSelectedX;
  }
  
  public int getTexY()
  {
    return notSelectedY;
  }
  
  private LayerType(String str, int texx, int texy)
  {
    this.str = str;
    this.notSelectedX = texx;
    this.notSelectedY = texy;
  }
  
  public static int numOf(LayerType t)
  {
    switch (t)
    {
      case TERRAIN:
        return 0;
      case STRUCTURES:
        return 1;
      case L3:
        return 2;
      case COVERAGE:
        return 3;
      default:
        return -1;
    }
  }
  
  public static LayerType layerNumber(int num)
  {
    switch (num)
    {
      case 0:
        return TERRAIN;
      case 1:
        return STRUCTURES;
      case 2:
        return L3;
      case 3:
        return COVERAGE;
      default:
        return null;
    }
  }
}
