/**
 * AirSlash.java    Jan 17, 2017, 9:17:47 AM
 */
package komorebi.projsoul.attack.projectile;

import komorebi.projsoul.attack.AttackInstance;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.player.Characters;

/**
 * Sierra's projectile attack
 *
 * @author Aaron Roy
 */
public class AirSlash extends PlayProjectile {

  private static final int MAX_TIME = 50;
  private int counter = MAX_TIME;
  private int maxAttack;
  
  /**
   * Creates a new AirSlash projectile
   * 
   * @param x The x location (in the map) of the projectile
   * @param y The y location (in the map) of the projectile
   * @param dx The x velocity, in pixels per frame
   * @param dy The y velocity, in pixels per frame
   * @param dir Direction this projectile is facing
   * @param attack The damage this will do
   */
  private AirSlash(float x, float y, float dx, float dy, Face dir, int attack){
    super(x, y, dx, dy, dir, attack, ProjectileType.AIR);
    
    maxAttack = attack;
    character = Characters.SIERRA;
  }
  
  public AirSlash() {}
  
  @Override
  public void update() {
    super.update();
    if(!attacks[index].playing() || counter <= 0){
      destroyMe = true;
    }
    counter--;
        
    attack = (int)((float)maxAttack*counter/MAX_TIME);
  }
  
  @Override
  public AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack) {
    return new AirSlash(x,y,dx,dy,dir,attack);
  }

}
