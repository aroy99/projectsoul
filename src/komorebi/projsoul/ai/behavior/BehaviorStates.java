/**
 * BehaviorStates.java    Jan 25, 2017, 9:35:51 AM
 */
package komorebi.projsoul.ai.behavior;

/**
 * All of the behaviors in the game
 *
 * @author Aaron Roy
 */
public enum BehaviorStates {
  /** The enemy isn't doing anything*/
  IDLE, 
  /** The enemy walks in some direction*/
  WALK, 
  /** The enemy tries to line up with the player*/
  LINE_UP, 
  /** The enemy charges at the player*/
  TACKLE, 
  /** The enemy is stunned and can't do anything*/
  STUN, 
  /** The enemy waits before attacking again*/
  WAIT;
}
