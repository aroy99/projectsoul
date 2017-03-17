/**
 * Invincible.java    Mar 1, 2017, 9:12:19 AM
 */
package komorebi.projsoul.ai.node.decorator;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.Enemy;

import org.junit.runners.ParentRunner;

/**
 * Makes the enemy invincible while doing an action
 *
 * @author Aaron Roy
 */
public class Invincible extends Node {

  private Enemy parent;
  private Node child;
  
  /**
   * @param parent The enemy this node is controlling
   * @param child The child this node is modifying
   */
  public Invincible(Enemy parent, Node child) {
    this.parent = parent;
    this.child = child;
  }

  @Override
  public void open() {
    super.open();
    parent.setInvincible(true);
  }
  
  @Override
  public Status update() {
    Status status = child.update();
    if(status != Status.RUNNING){
      close();
    }
    return status;
  }
  
  @Override
  public void close(){
    super.close();
    parent.setInvincible(false);
  }

}
