/**
 * Dummy.java	   Nov 18, 2016, 9:46:48 PM
 */
package komorebi.projsoul.entities.enemy;

/**
 * An enemy that does nothing
 *
 * @author Aaron Roy
 */
public class Dummy extends Enemy {

  /**
   * @param x
   * @param y
   * @param type
   */
  public Dummy(float x, float y, EnemyType type, int level) {
    super(x, y, type, level);
  }

  /**
   * @see komorebi.projsoul.entities.enemy.Enemy#xpPerLevel()
   */
  @Override
  public int xpPerLevel() {
    return 0;
  }

  /**
   * @see komorebi.projsoul.entities.enemy.Enemy#baseAttack()
   */
  @Override
  public int baseAttack() {
    return 0;
  }

  /**
   * @see komorebi.projsoul.entities.enemy.Enemy#baseDefense()
   */
  @Override
  public int baseDefense() {
    return 0;
  }

  /**
   * @see komorebi.projsoul.entities.enemy.Enemy#baseHealth()
   */
  @Override
  public int baseHealth() {
    return 100;
  }

}
