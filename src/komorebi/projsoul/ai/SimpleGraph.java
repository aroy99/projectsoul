/**
 * SimpleGraph.java	   Nov 28, 2016, 9:27:39 AM
 */
package komorebi.projsoul.ai;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * A class that represents a simple graph or connected vertices and edges
 *  
 * @param <L> What you want to use as a map identifier
 * 
 * @author Aaron Roy
 */
public class SimpleGraph<L> {
  private HashMap<L, L[]> edges;
  
  public SimpleGraph(HashMap<L, L[]> edges){
    this.edges = edges;
  }
  
  public L[] getEdges(L loc){
    return edges.get(loc);
  }
  
  /**
   * Searches in all directions for a location
   * 
   * @param start The location to start in the map
   */
  public void breadthFirstSearch(L start){
    LinkedList<L> frontier = new LinkedList<L>();
    frontier.push(start);
    
    HashSet<L> visited = new HashSet<L>();
    visited.add(start);
    
    while(!frontier.isEmpty()){
      L current = frontier.pop();
      
      
      System.out.println("Visiting " + current);
      
      for(L next: getEdges(current)){
        if(!visited.contains(next)){
          frontier.push(next);
          visited.add(next);
        }
      }
    }
  }
  
  
}
