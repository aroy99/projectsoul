/**
 * Inverter.java    Mar 9, 2017, 9:25:15 AM
 */
package komorebi.projsoul.ai.node.decorator;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;

/**
 * Inverts the output of the child, making failure success, and success failure
 *
 * @author Aaron Roy
 */
public class Inverter extends Node{

  private Node child;
  
  /**
   * @param parent The enemy this node is controlling
   * @param child The child this node is modifying
   */
  public Inverter(Node child) {
    this.child = child;
  }

  
  @Override
  public Status update() {
    Status status = child.update();
    if(status == Status.SUCCESS){
      return Status.FAIL;
    }
    
    if(status == Status.FAIL){
      return Status.SUCCESS;
    }
    
    return status;

  }

}
