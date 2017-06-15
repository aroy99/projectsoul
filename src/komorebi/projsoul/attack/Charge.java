package komorebi.projsoul.attack;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.CollisionDetector;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.gameplay.Camera;
import komorebi.projsoul.map.MapHandler;
import komorebi.projsoul.states.Game;
import komorebi.projsoul.attack.ElementalProperty;

import java.awt.Rectangle;

/**
 * A charge attack for Bruno
 * 
 * @author Andrew Faulkenberry
 * @author Aaron Roy
 */
public class Charge extends Melee {
 
  private Rectangle future;
  
  private int attackIndex;
  
  public Charge(){}
  
  private Charge(float x, float y, float dx, float dy, Face dir, int attack)
  {
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
    this.currentDir = dir;
    this.attack = attack;
    perAether = ElementalProperty.EARTH;//Charge's base typing is EARTH
    character = Characters.BRUNO;
    
    rightAttack = new Animation(1,8,12);
    rightAttack.add(299,654,28,32);
    
    leftAttack = new Animation(1,8,12);
    leftAttack.add(299,654,28,32,0,true);
    
    downAttack = new Animation(1,8,12);
    downAttack.add(124,726,27,30);
    
    upAttack = new Animation(1,8,12);
    upAttack.add(191, 586, 24, 30);
    
    hitBox = new Rectangle((int) x, (int) y, 28, 30);
    future = new Rectangle((int) x, (int) y, 28, 30);
  }
  
  
  @Override
  public AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack) {
    return new Charge(x,y,dx,dy,dir,attack);
  }

  @Override
  public void update() {
    
    boolean[] col = CollisionDetector.checkCollisions(x,y,dx,dy);

    if(!col[0] || !col[2]){
      Camera.shake(8, 2, 1);
      dy=0;
    }
    if(!col[1] || !col[3]){
      Camera.shake(8, 2, 1);
      dx=0;
    }
    
    future.x += dx;
    future.y += dy;
    
    for (Enemy enemy: MapHandler.getEnemies())
    {
      if (enemy.getHitBox().intersects(future))
      {
        enemy.inflictPain(attack, currentDir, Characters.BRUNO, perAether);
        dx = -dx;
        dy = -dy;
      }
    }
    this.x += dx;
    this.y += dy;
    
    hitBox.x = (int) x;
    hitBox.y = (int) y;

    if (dy > 0){ 
      dy -= 0.25;
    }
    if (dy < 0){ 
      dy += 0.25;
    }
    if (dx > 0){ 
      dx -= 0.25;
    }
    if (dx < 0){ 
      dx += 0.25;
    }
    
    attackIndex++;
   
    
  }
  
  public boolean isStopped()
  {
    return attackIndex >= 20;
  }

}
