/**
 * EnemyProjectile.java    Jan 2, 2017, 9:47:26 AM
 */
package komorebi.projsoul.attack.projectile;

import komorebi.projsoul.attack.WaterBarrier;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.player.Caspian;
import komorebi.projsoul.entities.player.Characters;
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
    
    WaterBarrier barr = Caspian.support.getAttackInstance();
    if(barr != null && barr.playing()){
      Rectangle rect = area;
      float[] xs = {rect.x, rect.x+rect.width};
      float[] ys = {rect.y, rect.y+rect.height};

      outer:
        for(int i = 0; i < 2; i++){
          for (int j = 0; j < 2; j++){

            float distance = Map.distanceBetween(barr.getX(), barr.getY(), xs[j], ys[i]);

            if(barr.intersectsCirc(distance)){
              destroyMe = true;
              break outer;
            }
          }
        }

    }
    
    


    super.update();
    
  }
}
