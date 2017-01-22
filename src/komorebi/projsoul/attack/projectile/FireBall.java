
package komorebi.projsoul.attack.projectile;

import komorebi.projsoul.attack.AttackInstance;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.player.Characters;

/**
 * Flannery's fireball
 * 
 * @author Andrew Faulkenberry
 */
public class FireBall extends PlayProjectile {
    
  /**
   * Creates a new FireBall projectile
   * 
   * @param x The x location (in the map) of the projectile
   * @param y The y location (in the map) of the projectile
   * @param dx The x velocity, in pixels per frame
   * @param dy The y velocity, in pixels per frame
   * @param dir Direction this projectile is facing
   * @param attack The damage this will do
   */
  private FireBall(float x, float y, float dx, float dy, Face dir, int attack){
    super(x,y,dx,dy,dir,attack, ProjectileType.FIRE);
    
    character = Characters.FLANNERY;  
  }
  
  public FireBall(){}
  
  public AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack)
  {
    return new FireBall(x,y,dx,dy,dir,attack);
  }
}
