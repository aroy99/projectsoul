/**
 * MagicEnemy.java     Jan 5, 2017, 9:29:44 AM
 */
package komorebi.projsoul.entities.enemy;

/**
 * An enemy that uses magic to attack
 *
 * @author Aaron Roy
 */
public abstract class MagicEnemy extends Enemy {

  protected int magic;
  protected int magicAttack;
  
  /**
   * @return The base magic of this enemy
   */
  public abstract int baseMagic();
  
  /**
   * @return The base magic attack of this enemy
   */
  public abstract int baseMagicAttack();


  /**
   * Creates a standard enemy
   * 
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param type The sprite of this enemy
   * @param level The level of this enemy
   */
  public MagicEnemy(float x, float y, EnemyType type, int level) {
    super(x, y, type, level);

    magic = baseMagic() + level;
    magicAttack = baseMagicAttack() + level;
  }

}
