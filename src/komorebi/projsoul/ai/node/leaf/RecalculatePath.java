/**
 * RecalculatePath.java     Mar 10, 2017, 9:12:29 AM
 */
package komorebi.projsoul.ai.node.leaf;

import komorebi.projsoul.ai.Location;
import komorebi.projsoul.ai.WeightedSquareGrid;
import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.SmartEnemy;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.map.MapHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Recalculates the enemy path
 *
 * @author Aaron Roy
 */
public class RecalculatePath extends Node {

  SmartEnemy parent;
  WeightedSquareGrid grid;
  
  private int ctx, cty;
  private int rtx, rty;

  /**
   * Creates the node
   * 
   * @param parent The enemy this node controls
   * @param grid The grid to a* search on
   */
  public RecalculatePath(SmartEnemy parent, WeightedSquareGrid grid) {
    this.parent = parent;
    this.grid = grid;
  }
  
  
  @Override
  public Status update() {
    System.out.println("Recalculating path");
    
    Player player = MapHandler.getPlayer();
    
    ctx = (int)player.getX()/16-MapHandler.getLowX();
    cty = (int)player.getY()/16-MapHandler.getLowY();

    rtx = (int)(parent.getX()/16)-MapHandler.getLowX();
    rty = (int)(parent.getY()/16)-MapHandler.getLowY();

    
    HashMap<Location, Location> cameFrom = new HashMap<Location, Location>();
    HashMap<Location, Double> costSoFar = new HashMap<Location, Double>();
  
    Location start = new Location(rtx, rty);
    Location goal = new Location(ctx, cty);
  
    grid.aStarSearch(start, goal, cameFrom, costSoFar);
  
    ArrayList<Location> path = grid.reconstructPath(start, goal, cameFrom);
    
    grid.drawGrid(1, start, goal, null, null, path);
    
    parent.setPath(path);
    
    return Status.SUCCESS;
  }

}
