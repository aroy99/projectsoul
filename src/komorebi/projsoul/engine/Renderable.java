/**
 * Renderable.java  May 30, 2016, 11:34:10 AM
 */

package komorebi.projsoul.engine;

/**
 * Represents an object that can update itself and display
 * 
 * @author Aaron Roy
 */
public interface Renderable {
  /**
   * Updates this objects variables based on the input and game logic
   */
  void update();

  /**
   * Renders this object based on its location
   */
  void render();
}
