package komorebi.projsoul.attack;

import java.awt.Rectangle;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.entities.Face;

public class FireBall extends Projectile {
    
  public FireBall(float x, float y, float dx, float dy, Face dir, int attack)
  {    
    super(x,y,dx,dy,dir,attack);
    
    character = Characters.FLANNERY;
    
    downAttack = new Animation(4,8,12,false);
    leftAttack = new Animation(4,8,12,false);
    rightAttack = new Animation(4,8,12,false);
    upAttack = new Animation(4,8,12,false);

    rightAttack.add(810,2,11,8);
    rightAttack.add(827,1,15,9);
    rightAttack.add(848,1,18,9);
    rightAttack.add(874,0,20,10);
    rightAttack.setPausedFrame(874,0,20,10);
    
    leftAttack.add(810,2,11,8,0,true);
    leftAttack.add(827,1,15,9,0,true);
    leftAttack.add(848,1,18,9,0,true);
    leftAttack.add(874,0,20,10,0,true);
    leftAttack.setPausedFrame(874,0,20,10,0,true);

    upAttack.add(810,2,11,8,1,false);
    upAttack.add(827,1,15,9,1,false);
    upAttack.add(848,1,18,9,1,false);
    upAttack.add(874,0,20,10,1,false);
    upAttack.setPausedFrame(874,0,20,10,1,false);
    
    downAttack.add(810,2,11,8,1,true);
    downAttack.add(827,1,15,9,1,true);
    downAttack.add(848,1,18,9,1,true);
    downAttack.add(874,0,20,10,1,true);
    downAttack.setPausedFrame(874,0,20,10,1,true);
        
    area = new Rectangle((int) x, (int) y, 11, 8);
  
  }
  
  public FireBall()
  {
    
  }
  
  public AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack)
  {
    return new FireBall(x,y,dx,dy,dir,attack);
  }
}
