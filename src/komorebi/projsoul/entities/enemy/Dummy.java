/**
 * Dummy.java   Nov 18, 2016, 9:46:48 PM
 */
package komorebi.projsoul.entities.enemy;

import komorebi.projsoul.ai.node.leaf.BehaviorStates;

/**
 * An enemy that does nothing
 *
 * @author Aaron Roy
 */
public class Dummy extends Enemy {

  /**
   * Creates a dummy that does nothing
   * 
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param type The sprite of this enemy
   * @param level The level of this enemy
   */
  public Dummy(float x, float y, EnemyType type, int level) {
    super(x, y, type, level);
  }
  
  @Override
  public void update() {
    super.update();
    if(!hurt){
      dx *= 0.9;
      dy *= 0.9;
    }
  }
  
  @Override
  public int xpPerLevel() {
    return 10000;
  }

  @Override
  public int baseAttack() {
    return 0;
  }

  @Override
  public int baseDefense() {
    return 0;
  }

  @Override
  public int baseHealth() {
    return 500;
  }

}
