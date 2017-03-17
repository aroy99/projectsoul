/**
 * ShootBehavior.java    Feb 10, 2017, 9:11:17 AM
 */
package komorebi.projsoul.ai.node.leaf;

import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.Shooter;
import komorebi.projsoul.map.Map;

/**
 * The enemy shoots its projectile out
 *
 * @author Aaron Roy
 */
public class ShootBehavior extends Behavior {

  private int shootCount;
  private final int maxShoot;
  private final float projectileSpeed;
  private final int aggroDistance;
  private float currDist;
  private float y;
  private float x;
  private float targetY;
  private float targetX;

  public ShootBehavior(Shooter parent, int maxShoot, float projSpeed, 
                         int aggroDistance) {
    super(parent);
    this.maxShoot = maxShoot;
    projectileSpeed = projSpeed;
    this.aggroDistance = aggroDistance;
  }

  @Override
  public Status update() {
    shootCount--;
    calculateDistance();
    
    if(shouldRealignShot(targetX, targetY, x, y)){
      return Status.FAIL;
    }
              
    if(shootCount <= 0){
      float aDx = 0, aDy = 0;
  
      switch (parent.getDirection())
      {
        case DOWN:
          aDy = -projectileSpeed;
          break;
        case LEFT:
          aDx = -projectileSpeed;
          break;
        case RIGHT:
          aDx = projectileSpeed;
          break;
        case UP:
          aDy = projectileSpeed;
          break;
        default:
          break;
      }
  
      shootCount = (int) (currDist/aggroDistance * (GEN.nextInt(maxShoot)+maxShoot));
      
      if(((Shooter)parent).shoot(aDx, aDy)){
        return Status.SUCCESS;
      }
      return Status.FAIL;
    }
    
    return Status.RUNNING;
  }

  private void calculateDistance() {
    targetX = Map.getPlayer().getX();
    targetY = Map.getPlayer().getY();
    
    x = parent.getX();
    y = parent.getY();
    
    currDist = Map.distanceBetween(x,y,targetX,targetY);
  }
  
  @Override
  public BehaviorStates getState(){
    return BehaviorStates.SHOOT;
  }
  
  public void setShootCount(int shootCount){
    this.shootCount = shootCount;
  }
  
  private boolean shouldRealignShot(float targetX, float targetY, float x, float y) {
    switch(parent.getDirection()){  
      case DOWN: case UP:
        float deX = targetX-x;
        if(Math.abs(deX) > 8){
          return true;
        }
        break;
      case LEFT: case RIGHT:
        float deY = targetY-y;
        if(Math.abs(deY) > 8){
          return true;
        }
        break;
      default:
    }
    return false;

  }

  
}
