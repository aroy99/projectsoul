/**
 * Node.java    Feb 23, 2017, 9:24:36 AM
 */
package komorebi.projsoul.ai.node;

/**
 * The most basic node in a behavior tree
 *
 * @author Aaron Roy
 */
public abstract class Node {

  @Deprecated
  protected Status status;
  
  @Deprecated
  public Status getStatus(){
    return status;
  }
  
  /**
   * Called when entering a node
   */
  public void open(){}
  
  /**
   * Should be called when a node returns success
   */
  public void close(){}
  public abstract Status update();
}
