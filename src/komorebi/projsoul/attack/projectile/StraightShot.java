/**
 * SaturnBlast.java    Jan 2, 2017, 9:30:52 AM
 */
package komorebi.projsoul.attack.projectile;

import komorebi.projsoul.attack.AttackInstance;
import komorebi.projsoul.entities.Face;

import java.awt.Rectangle;

/**
 * An enemy projectile that just goes straight
 *
 * @author Aaron Roy
 */
public class StraightShot extends EnemyProjectile {

  /**
   * Creates a new SaturnBlast
   * 
   * @param x The x location (in the map) of the projectile
   * @param y The y location (in the map) of the projectile
   * @param dx The x velocity, in pixels per frame
   * @param dy The y velocity, in pixels per frame
   * @param dir Direction this projectile is facing
   * @param attack The damage this will do
   */
  public StraightShot(float x, float y, float dx, float dy, Face dir, int attack, 
                        ProjectileType type){
    super(x,y,dx,dy,dir,attack, type);        
    area = new Rectangle((int) x, (int) y, 11, 8);
  }
  
  public StraightShot(){}
  
  @Override
  public AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack)
  {
    return new StraightShot(x,y,dx,dy,dir,attack, ProjectileType.WATER);
  }
  
}
