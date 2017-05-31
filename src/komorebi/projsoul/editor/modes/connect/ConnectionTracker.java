package komorebi.projsoul.editor.modes.connect;

import java.util.ArrayList;

public class ConnectionTracker {

  private ArrayList<Connection> connections;
  
  public ConnectionTracker()
  {
    connections = new ArrayList<Connection>();
  }
  
  public void addConnectionIfNotAlreadyPresent(Connection c)
  {
    if (!contains(c))
      connections.add(c);
  }
  
  private boolean contains(Connection con)
  {
    for (Connection c: connections)
    {
      if (c.equals(con))
        return true;
    }
    
    return false;
  }
}
