/**
 * PathFindingTest.java    Nov 28, 2016, 9:42:59 AM
 */
package komorebi.projsoul.ai;

import komorebi.projsoul.ai.SquareGrid.Location;
import komorebi.projsoul.map.Map;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 *
 * @author Aaron Roy
 */
public class PathFindingTest {
  
  /**
   * Starts the pathfinding test
   * 
   * @param args Does absolutely nothing
   */
  public static void main(String[] args){
    Map testMap = new Map("res/maps/Maze.map");
    
    WeightedSquareGrid grid = new WeightedSquareGrid(testMap.getCollision());
    Location start = new Location(10, 10);
    Location goal = new Location(1, 1);
    
    HashMap<Location, Location> cameFrom = new HashMap<Location, Location>();
    HashMap<Location, Double> costSoFar = new HashMap<Location, Double>();
    
    grid.aStarSearch(start, goal, cameFrom, costSoFar);
        
//    HashMap<Location, Location> print = grid.breadthFirstSearch(start, goal);
    
    grid.drawGrid(3, start, goal, cameFrom, null, null);
    
    System.out.println();
    
    grid.drawGrid(3, start, goal, null, costSoFar, null);
    
    System.out.println();
    
    ArrayList<Location> path = grid.reconstructPath(start, goal, cameFrom);
    grid.drawGrid(1, start, goal, null, null, path);
    
    System.out.println("Start: " + start.x + ", " + start.y);
    System.out.println("Goal: " + goal.x + ", " + goal.y);
    
  }
}
