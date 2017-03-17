/**
 * Behaviors.java    Feb 6, 2017, 9:28:03 AM
 */
package komorebi.projsoul.ai.node.leaf;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.Enemy;

import java.util.Random;

/**
 * The different behaviors an enemy can have
 * 
 * @author  Aaron Roy
 */
public abstract class Behavior extends Node{

  protected static final Random GEN = new Random();  
  Enemy parent;
  
  public Behavior(Enemy parent){
    this.parent = parent;
  }
  
  public abstract BehaviorStates getState();
  
}