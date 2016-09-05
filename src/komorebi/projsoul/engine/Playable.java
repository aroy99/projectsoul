/**
 * Playable.java  May 30, 2016, 11:32:57 AM
 */
package komorebi.projsoul.engine;

/**
 * Represents an object that needs input
 * 
 * @author Aaron Roy
 * @version 0.0.1.0
 */
public interface Playable extends Renderable{
  
  /**
   * Gets input from the player and stores it in the appropriate booleans
   * 
   * <p>Also makes sure certain inputs aren't repeated
   */
  void getInput();
}
