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
    
    float sideX = 0, sideY = 0;
    
    int sign;
    
    WaterKunai leftKunai = null;
    WaterKunai rightKunai = null;
    
    switch (dir){
      case DOWN: case UP:
        sideX = 0.5f*dy;
        sideY = 0.87f*dy;
        sign  = (int) Math.signum(dy);
            
        leftKunai  = new WaterKunai(-sideX, sign*Math.abs(sideY), this);
        rightKunai = new WaterKunai( sideX, sign*Math.abs(sideY), this);
        break;
      case LEFT: case RIGHT:
        sideX = 0.87f*dx;
        sideY = 0.5f*dx; 
        sign  = (int) Math.signum(dx);
        
        leftKunai   = new WaterKunai(sign*Math.abs(sideX), -sideY, this);
        rightKunai  = new WaterKunai(sign*Math.abs(sideX),  sideY, this);
        break;
      default:
        break;
    }

    ProjectileAttack.newAttack(leftKunai);
    ProjectileAttack.newAttack(rightKunai);
    
    character = Characters.CASPIAN;
  }
  
  private WaterKunai(float dx, float dy, WaterKunai parent){
    super(parent.x,parent.y,dx,dy,parent.currentDir,parent.attack, ProjectileType.WATER);

    character = Characters.CASPIAN;
  }
  
  public WaterKunai(){}
  
  @Override
  public void update(){
    super.update();
  }
  
  @Override
  public void play(){
    super.play();
  }
  
  public AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack)
  {
    return new WaterKunai(x,y,dx,dy,dir,attack);
  }
}
