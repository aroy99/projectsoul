/**
 * PriorityNode.java     Feb 23, 2017, 9:24:28 AM
 */
package komorebi.projsoul.ai.node.composite;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;

/**
 * This node loops though its children and stops when one of them returns
 * SUCCESS or RUNNING
 *
 * @author Aaron Roy
 */
public class Priority extends Node{
  
  
  protected Node[] children;

  /**
   * @param nodes The children of this class
   */
  public Priority(Node... nodes) {
    children = nodes;
  }

  @Override
  public Status update() {
    for(Node node: children){
      Status result = node.update();
      if(result != Status.FAIL){
        return result;
      }
    }
    return Status.FAIL;
  }

}
