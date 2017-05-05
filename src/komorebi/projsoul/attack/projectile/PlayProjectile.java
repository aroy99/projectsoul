package komorebi.projsoul.attack.projectile;

import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.map.MapHandler;

import java.awt.Rectangle;

/**
 * Represents a projectile shot by a player
 *
 * @author Aaron Roy
 */
public abstract class PlayProjectile extends Projectile{
  
  public Characters character;
    
  public PlayProjectile(float x, float y, float dx, float dy, Face dir, int attack,
                          ProjectileType type) {
    super(x, y, dx, dy, dir, attack, type);
  }
  
  public PlayProjectile(){}

  @Override
  public void update() {    
    for (Enemy enemy: MapHandler.getEnemies())
    {
      if (enemy.getHitBox().intersects(new Rectangle((int) (x+dx), 
          (int) (y+dy), (int) area.getWidth(), 
          (int) area.getHeight())))
      {
        destroyMe = true;
        if (!enemy.invincible())
        {
          enemy.inflictPain(attack, currentDir, character);
          //DEBUG Print out projectile attack on hit
          System.out.println(attack);
        }
      }
        
    } 
    
    super.update();

  }

  

}
