package komorebi.projsoul.attack.projectile;

import komorebi.projsoul.attack.AttackInstance;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.player.Characters;

/**
 * Caspian's water kunai projectile
 *
 * @author Andrew Faulkenberry
 * @author Aaron Roy
 */
public class WaterKunai extends PlayProjectile {

  private WaterKunai(float x, float y, float dx, float dy, Face dir, int attack){
    super(x,y,dx,dy,dir,attack, ProjectileType.WATER);
    
    character = Characters.CASPIAN;
  }
  
  public WaterKunai()
  {
    super();
  }
  
  public AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack)
  {
    return new WaterKunai(x,y,dx,dy,dir,attack);
  }
}
