/**
 * MemSequence.java    Feb 25, 2017, 10:18:15 AM
 */
package komorebi.projsoul.ai.node.composite;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;

/**
 * A sequence that remembers its spot
 *
 * @author Aaron Roy
 */
public class MemSequence extends Node {

  protected Node[] children;

  protected int memIndex;
  
  /**
   * @param nodes The children of this class
   */
  public MemSequence(Node... nodes) {
    children = nodes;
  }
  
  @Override
  public Status update() {    
    for(int i = memIndex; i < children.length; i++){
      Node node = children[i];
      
      Status result = node.update();
      if(result != Status.SUCCESS){
        if(result == Status.RUNNING){
          memIndex = i;
        }else if(result == Status.FAIL || result == Status.INTERUPTED){
          memIndex = 0;
        }
        return result;
      }
    }
    memIndex = 0;
    return Status.SUCCESS;

  }

}
