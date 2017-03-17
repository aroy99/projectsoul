/**
 * Succeeder.java     Mar 16, 2017, 11:00:21 PM
 */
package komorebi.projsoul.ai.node.decorator;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;

/**
 * Always returns success regardless of what the child returns
 *
 * @author Aaron Roy
 */
public class Succeeder extends Node {

  private Node child;
  
  /**
   * @param parent The enemy this node is controlling
   * @param child The child this node is modifying
   */
  public Succeeder(Node child) {
    this.child = child;
  }

  
  @Override
  public Status update() {
    Status status = child.update();
    
    return Status.SUCCESS;

  }

}
