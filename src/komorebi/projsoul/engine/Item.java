/**
 * Item.java		Aug 3, 2016, 11:49:22 AM
 */
package komorebi.projsoul.engine;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class Item {
    
  public enum Items
  {
    KEY("key", "a key"),
    NOTEBOOK("notebook", "a notebook"),
    GLASSES("glasses", "Mr. Maggio\'s glasses"),
    CLIPPY("clippy", "Clippy"),
    NONE("none", "nothing");
    
    private String printStr;
    private String idStr;
    
    private Items(String idStr, String printStr)
    {
      this.printStr = printStr;
      this.idStr = idStr;
    }
    
    public String getPrintString()
    {
      return printStr;
    }
    
    public String getIDString()
    {
      return idStr;
    }
    
    public static Items getItem(String s)
    {
      for (Items item: Items.values())
      {
        if (item.getIDString().equals(s)) return item;
      }
      return NONE;
    }
    
    public String getNiftyTidbit() {
      switch (this)
      {
        case KEY:
          return "Opens stuff n\' shit";
        case NOTEBOOK:
          return "That thing you doodle in";
        case GLASSES:
          return "Intensify sight and nerdiness";
        case CLIPPY:
          return "Provides marriage advice";
        default:
          return "";
      }
    }
  }
  
  private Items item;
  
  public Item(Items type)
  {
    item = type;
  }
  
  public void render(int x, int y)
  {
    switch (item)
    {
      case KEY:
        Draw.rect(x, y, 16, 16, 0, 0, 16, 16, 10);
        break;
      case NOTEBOOK:
        Draw.rect(x, y, 16, 16, 16, 16, 32, 32, 10);
        break;
      case GLASSES:
        Draw.rect(x, y, 16, 16, 32, 32, 48, 48, 10);
        break;
      case CLIPPY:
        Draw.rect(x, y, 16, 16, 48, 48, 64, 64, 10);
        break;
      default:
        break;
      
    }
  }
  
  public Items type()
  {
    return item;
  }

  
}
