/**
 * EnemyProjectile.java    Jan 2, 2017, 9:47:26 AM
 */
package komorebi.projsoul.attack.projectile;

import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.map.Map;

import java.awt.Rectangle;

/**
 * A projectile that is shot out of an enemy
 *
 * @author Aaron Roy
 */
public abstract class EnemyProjectile extends Projectile {

  public EnemyProjectile(float x, float y, float dx, float dy, 
                            Face dir, int attack, ProjectileType type){    
    super(x,y,dx,dy,dir,attack, type);
  }
  
  public EnemyProjectile() {}
  
  @Override
  public void update(){
    Player player = Map.getPlayer();
    
    if (player.getHitBox().intersects(new Rectangle((int) (x+dx), 
        (int) (y+dy), (int) area.getWidth(), 
        (int) area.getHeight())))
    {
      destroyMe = true;
      if (!player.invincible())
      {
        player.inflictPain(attack, dx, dy);
      }
    }

    super.update();
    
  }
}
