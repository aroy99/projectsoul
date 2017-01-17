
package komorebi.projsoul.attack.projectile;

import komorebi.projsoul.attack.AttackInstance;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.player.Characters;

import java.awt.Rectangle;

/**
 * Flannery's fireball
 * 
 * @author Andrew Faulkenberry
 */
public class FireBall extends PlayProjectile {
    
  public FireBall(float x, float y, float dx, float dy, Face dir, int attack){
    super(x,y,dx,dy,dir,attack, ProjectileType.FIRE);
    
    character = Characters.FLANNERY;
           
    area = new Rectangle((int) x, (int) y, 11, 8);
  
  }
  
  public FireBall(){}
  
  public AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack)
  {
    return new FireBall(x,y,dx,dy,dir,attack);
  }
}
