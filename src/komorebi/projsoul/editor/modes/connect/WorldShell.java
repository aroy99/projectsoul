package komorebi.projsoul.editor.modes.connect;

import java.util.ArrayList;

import komorebi.projsoul.map.ConnectMap;

public class WorldShell {

  private String worldName;
  private ArrayList<String> mapsWithinWorld;
  
  public WorldShell(String worldName)
  {
    this.worldName = worldName;
    mapsWithinWorld = new ArrayList<String>();
  }
  
  public void addMap(String map)
  {
    mapsWithinWorld.add(map);
  }
  
  public String getWorldName()
  {
    return worldName;
  }
  
  public boolean contains(String contains)
  {    
    for (String map: mapsWithinWorld)
    {      
      if (map.equals(contains))
        return true;
    }
    
    return false;
  }
  
  public String toString()
  {
    String ret = worldName + "\n#ref ";
    
    for (int i = 0; i < mapsWithinWorld.size(); i++)
    {
      ret += mapsWithinWorld.get(i);
      
      if (i != mapsWithinWorld.size() - 1)
      {
        ret += ", ";
      }
    }
    
    return ret;
  }
  
  public World build()
  {    
    World world = new World(worldName);
    
    for (String map: mapsWithinWorld)
    {
      world.addMap(new ConnectMap("res/maps/" + map, 0, 0));
    }
    
    return world;
  }
}
