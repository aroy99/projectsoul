/**
 * SequenceNode.java    Feb 24, 2017, 9:22:29 AM
 */
package komorebi.projsoul.ai.node.composite;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.ai.node.leaf.IdleBehavior;

/**
 * This node loops though its children and stops when one of them returns
 * FAIL
 * 
 * @author Aaron Roy
 */
public class Sequence extends Node {

  protected Node[] children;
  
  /**
   * @param nodes The children of this class
   */
  public Sequence(Node... nodes) {
    children = nodes;
  }
  
  @Override
  public Status update() {
    for(Node node: children){
      Status result = node.update();
      if(result != Status.SUCCESS){
        return result;
      }
    }
    return Status.SUCCESS;
  }

}
