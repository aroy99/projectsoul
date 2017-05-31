/**
 * Status.java    Feb 20, 2017, 5:35:25 PM
 */
package komorebi.projsoul.ai.node;

/**
 * The current status of this behavior
 *
 * @author Aaron Roy
 */
public enum Status{
  /** The behavior is currently still running, continue please*/
  RUNNING, 
  /** The behavior is done, move on to the next one*/
  SUCCESS,
  /** The behavior failed cleanly, move on to the next one*/
  FAIL, 
  /** The behavior was interrupted in an unexpected way, handle it*/
  INTERUPTED;
}