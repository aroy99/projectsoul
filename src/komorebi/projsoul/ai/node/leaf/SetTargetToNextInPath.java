/**
 * SetTargetToNextInPath.java    Mar 10, 2017, 9:37:17 AM
 */
package komorebi.projsoul.ai.node.leaf;

import komorebi.projsoul.ai.SquareGrid.Location;
import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.SmartEnemy;

import java.util.ArrayList;

/**
 * @author Aaron Roy
 */
public class SetTargetToNextInPath extends Node{

  SmartEnemy parent;
  ArrayList<Location> path;
  
  public SetTargetToNextInPath(SmartEnemy parent) {
    this.parent = parent;
  }
  
  @Override
  public Status update() {
    path = parent.getPath();
    
    Location prev = path.remove(0);

    if(!path.isEmpty()){
      Location curr = path.get(0);
      decideNextTarget(prev, curr);
    }
    
    return null;
  }

  private void decideNextTarget(Location prev, Location curr) {
    
    float nextX, nextY;
    
    if(curr.x > prev.x){
      nextX = curr.x*16+8;
    }else if(curr.x < prev.x){
      nextX = curr.x*16-8;
      curr = path.set(0, new Location(curr.x-1, curr.y));
    }else {
      nextX = curr.x*16;
    }
  
    if(curr.y > prev.y){
      nextY = curr.y*16+8;
    }else if(curr.y < prev.y){
      nextY = curr.y*16-8;
      curr = path.set(0, new Location(curr.x, curr.y-1));
    }else {
      nextY = curr.y*16;
    }
    
    parent.setTarget(nextX, nextY);
  }

  
}
