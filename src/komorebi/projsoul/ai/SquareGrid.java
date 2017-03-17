/**
 * SquareGrid.java     Nov 29, 2016, 9:39:20 AM
 */
package komorebi.projsoul.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Represents a square grid to be used with pathfinding and other endeavors
 *
 * @author Aaron Roy
 */
public class SquareGrid {
  
  /**
   * Represents a Location on a grid with an X and Y
   *
   * @author Aaron Roy
   */
  public static class Location{
    public final int x;
    public final int y;
    public Location(int nx, int ny){
      x = nx;
      y = ny;
    }
    
    @Override
    public boolean equals(Object chec){
      if(chec instanceof Location){
        Location check = (Location)chec;
        return check.x == x && check.y == y;
      }
      return false;
    }
    
    public int hashCode(){
      return 31*x+y;
    }
    
    public String toString(){
      return x + ", " + y;
    }
  }
  
  //Diagonal directions
  private static final Location[] DIRS =  {new Location(-1, 1), new Location(0, 1),
      new Location(1, 1), new Location(1, 0), new Location(1, -1),
      new Location(0, -1), new Location(-1, -1), new Location(-1, 0)};
  
  
  //Orthoganal directions
  /*
  private static final Location[] DIRS =  {new Location(0, 1),
      new Location(1, 0),
      new Location(0, -1), new Location(-1, 0)};
  */
  
  protected int width, height;
  HashSet<Location> walls = new HashSet<Location>();
  
  /**
   * Creates a new SquareGrid
   * 
   * @param nwidth Width in tiles
   * @param nheight Height in tiles
   */
  public SquareGrid(int nwidth, int nheight){
    width = nwidth;
    height = nheight;
  }
  
  //TODO Create generation from boolean map
    
  private boolean inBounds(Location l){
    return 0 <= l.x && l.x < width && 0 <= l.y && l.y < height;
  }
  
  private boolean passable(Location id){
    return !walls.contains(id);
  }
  
  /**
   * @param id The location to start looking
   * @return The neighbors of this location that can be passed through
   */
  public ArrayList<Location> neighbors(Location id){
    int x = id.x;
    int y = id.y;
    int dx,dy;
    
    ArrayList<Location> results = new ArrayList<Location>();
    
    for(Location l: DIRS){
      dx = l.x;
      dy = l.y;
      
      Location next = new Location(x + dx, y + dy);
      if(inBounds(next) && passable(next)){
        results.add(next);
      }
    }
    
    if((x+y)%2 == 0){
      Collections.reverse(results);
    }
    
    return results;
  }
  
  /**
   * @param start Where to start searching
   * @param goal The goal to stop at
   * @return A HashMap containing the path from the goal to the start
   */
  public HashMap<Location, Location> breadthFirstSearch(Location start, 
                                                           Location goal){
    LinkedList<Location> frontier = new LinkedList<Location>();
    frontier.add(start);
    
    HashMap<Location, Location> cameFrom = new HashMap<Location, Location>();
    cameFrom.put(start, start);
    
    while(!frontier.isEmpty()){
      Location current = frontier.pop();
      
      if (current.equals(goal)){
        System.out.println("Found goal: " + current);
        break;
      }
      System.out.println("---Neighbors of " + current + "---");
      for(Location next: neighbors(current)){
        if(!cameFrom.containsKey(next)){
          frontier.add(next);
          cameFrom.put(next, current);
          System.out.println(next);
        }
      }
    }
    
    return cameFrom;
  }
  
  /**
   * Reconstructs a path from a start to a goal from a HashMap containing 
   * directions
   * 
   * @param start Where to start
   * @param goal Where to go
   * @param cameFrom The HashMap to use when consulting directions
   * @return A list of locations to go to next
   */
  public ArrayList<Location> reconstructPath (Location start, Location goal, 
                                         HashMap<Location, Location> cameFrom){
    ArrayList<Location> path = new ArrayList<Location>();
    Location current = goal;
    
    while(!current.equals(start)){
      current = cameFrom.get(current);
      path.add(current);
    }
    
    path.add(start);
    
    Collections.reverse(path);
    return path;
  }
}
