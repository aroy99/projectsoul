/**
 * WeightedSquareGrid.java	   Dec 4, 2016, 9:35:48 PM
 */
package komorebi.projsoul.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * 
 *
 * @author Aaron Roy
 */
public class WeightedSquareGrid extends SquareGrid {
  
  
  private HashSet<Location> obstacles = new HashSet<Location>();
  private boolean[][] walls;
  
  /**
   * Represents a Location with priority for the queue
   *
   * @author Aaron Roy
   */
  private static class PriorityLoc extends Location implements Comparable<PriorityLoc> {
    
    public double priority;
    
    public PriorityLoc(int nx, int ny, double priority){
      super(nx, ny);
      this.priority = priority;
    }
    
    public PriorityLoc(Location loc, double priority){
      super(loc.x, loc.y);
      this.priority = priority;
    }

    @Override
    public int compareTo(PriorityLoc loc) {
      if(loc.priority > priority){
        return -1;
      }
      if(loc.priority < priority){
        return 1;
      }
      return 0;
    }
    
  }
  
  /**
   * Creates a new weighted SquareGrid
   * 
   * @param nwidth Width in tiles
   * @param nheight Height in tiles
   */
  public WeightedSquareGrid(int nwidth, int nheight) {
    super(nwidth, nheight);
    walls = new boolean[height][width];
  }
  
  /**
   * Creates a new weighted SquareGrid from a collision map
   * 
   * @param obstacles The map to input
   */
  public WeightedSquareGrid(boolean[][] obstacles){
    super(obstacles[0].length, obstacles.length);
    
    walls = obstacles;
    
    for(int i = 0; i < height; i++){
      for(int j = 0; j < width; j++){
        if(!obstacles[i][j]){
          this.obstacles.add(new Location(j, i));
        }
      }
    }
  }
  
  private double cost(Location from, Location to){
    double cost = 1;
    
    if(from.x != to.x && from.y != to.y){
      cost *= (99/70.0-0.000073f);
      if(obstacles.contains(new Location(to.x-1, to.y)) ||
          obstacles.contains(new Location(to.x+1, to.y)) ||
          obstacles.contains(new Location(to.x, to.y-1)) ||
          obstacles.contains(new Location(to.x, to.y+1))){
        cost*= 10000;
      }
    }
    
    cost *= obstacles.contains(to) ? 10000 : 1;
    
    return cost;
  }
  
  /**
   * Uses dijikstra's algorithm to find a way from a start point to a goal,
   * minding obstacles
   * 
   * @param start Start location
   * @param goal End location
   * @param cameFrom A HashMap to pass that will contain the path from the goal 
   *                  to the start
   * @param costSoFar A HashMap to pass that will contain the costs for each path
   */
  public void dijikstraSearch(Location start, Location goal, 
       HashMap<Location, Location> cameFrom, HashMap<Location, Double> costSoFar){    
    
    PriorityQueue<PriorityLoc> frontier = new PriorityQueue<PriorityLoc>();
    frontier.add(new PriorityLoc(start, 0));
    
    cameFrom.put(start, start);
    costSoFar.put(start, 0.0);
    
    while(!frontier.isEmpty()){
      Location current = frontier.remove();
            
      if(current.equals(goal)){
        System.out.println("Done");
        break;
      }
      
      for(Location next : neighbors(current)){
        double newCost = costSoFar.get(current) + cost(current, next);
        if(!costSoFar.containsKey(next) || newCost < costSoFar.get(next)){
          costSoFar.put(next, newCost);
          cameFrom.put(next, current);
          frontier.add(new PriorityLoc(next, newCost));
        }
      }
    }
    
  }
  
  /**
   * Calculates a heuristic for A*, giving preference to paths on the straight
   * line between the start and goal
   * 
   * @param curr Current location
   * @param start Where you started
   * @param goal The goal
   * @return The predicted cost of the path
   */
  public double heuristic(Location curr, Location start, Location goal){
    double dx1 = curr.x - goal.x;
    double dy1 = curr.y - goal.y;
    
    double dx2 = start.x - goal.x;
    double dy2 = start.y - goal.y;
    
    double dx = Math.abs(dx1);
    double dy = Math.abs(dy1);
    
    double cross = Math.abs(dx1*dy2 - dx2*dy1);
    
    return (dx + dy) + (99/70.0-2)*Math.min(dx, dy) + cross;
  }
  
  /**
   * The OP algorithm that can find a path from a start to a goal quickly and 
   * efficiently
   * 
   * @param start Start location
   * @param goal End location
   * @param cameFrom A HashMap to pass that will contain the path from the goal 
   *                  to the start
   * @param costSoFar A HashMap to pass that will contain the costs for each path
   */
  public void aStarSearch(Location start, Location goal, 
      HashMap<Location, Location> cameFrom, HashMap<Location, Double> costSoFar){
    
    PriorityQueue<PriorityLoc> frontier = new PriorityQueue<PriorityLoc>();
    frontier.add(new PriorityLoc(start, 0));
    
    cameFrom.put(start, start);
    costSoFar.put(start, 0.0);
    
    while(!frontier.isEmpty()){
      Location current = frontier.remove();
            
      if(current.equals(goal)){
        break;
      }
      
      for(Location next : neighbors(current)){
        double newCost = costSoFar.get(current) + cost(current, next);
        if(!costSoFar.containsKey(next) || newCost < costSoFar.get(next)){
          costSoFar.put(next, newCost);
          
          double priority = newCost + heuristic(current, start, goal);
          frontier.add(new PriorityLoc(next, priority));
          cameFrom.put(next, current);
        }
      }
    }

  }
  
  /**
   * Draws a grid in the console, each parameter can be null
   * 
   * @param cameFrom A map of directions to the start
   * @param costSoFar A map containing the cost between each location
   * @param path A path from one point to another
   */
  public void drawGrid(int space, Location start, Location goal, 
      HashMap<Location, Location> cameFrom, 
      HashMap<Location, Double> costSoFar,
      ArrayList<Location> path){
    String returnee = "";
    
    for(int i = height-1; i >= 0; i--){
      for(int j = 0; j < width; j++){
        String let = ".";
        Location id = new Location(j, i);
        
        if(obstacles.contains(id)){
          let = "#";
        }
        else if(cameFrom != null && cameFrom.containsKey(id)){
          int x2, y2;
          x2 = cameFrom.get(id).x;
          y2 = cameFrom.get(id).y;
          
          if(x2 == j+1 && y2 == i+1){ let = "UR"; }
          else if(x2 == j-1 && y2 == i+1){ let = "UL"; }
          else if(x2 == j-1 && y2 == i-1){ let = "DL"; }
          else if(x2 == j+1 && y2 == i-1){ let = "DR"; }
          else if(x2 == j+1){ let = ">"; }
          else if(x2 == j-1){ let = "<"; }
          else if(y2 == i+1){ let = "^"; }
          else if(y2 == i-1){ let = "v"; }
          else { let = "*";}
        }
        else if(costSoFar != null && costSoFar.containsKey(id)){
          let = ""+Math.round(costSoFar.get(id));
        }
        else if (path != null && path.contains(id)){
          let = "@";
        }
        
        if(start.x == j && start.y == i){
          let = "S";
        }
        
        if(goal.x == j && goal.y == i){
          let = "G";
        }
        
        while(let.length() < space){
          let += " ";
        }
        
        returnee += let;
      }
      returnee += "\n";
    }
    
    System.out.println(returnee);;
  }
  

}
