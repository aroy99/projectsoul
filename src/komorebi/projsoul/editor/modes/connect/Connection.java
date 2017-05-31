package komorebi.projsoul.editor.modes.connect;

public class Connection {

  private String root, extension;
  private int x, y;
  
  public Connection(String root, String extension, int x, int y)
  {
    this.root = root;
    this.extension = extension;
    
    this.x = x;
    this.y = y;
  }
  
  public String getRoot()
  {
    return root;
  }
  
  public String getExtension()
  {
    return extension;
  }
  
  public int getX()
  {
    return x;
  }
  
  public int getY()
  {
    return y;
  }
  
  public boolean equals(Connection c)
  {
    return identicalTo(c) || equivalentTo(c);
  }
  
  private boolean identicalTo(Connection c)
  {
    return root.equals(c.getRoot()) &&
        extension.equals(c.getExtension()) &&
        x == c.getX() && y == c.getY();
  }
  
  /**
   * 
   * @param c The connect map to compare to
   * @return true if the connection c is the reverse-equivalent of this
   * connection
   */
  private boolean equivalentTo(Connection c)
  {
    return root.equals(c.getExtension()) &&
        extension.equals(c.getRoot()) &&
        x == -c.getX() && y == -c.getY();
  }
}
