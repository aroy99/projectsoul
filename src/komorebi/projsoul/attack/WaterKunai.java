package komorebi.projsoul.attack;

import java.awt.Rectangle;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.attack.ElementalProperty;

public class WaterKunai extends Projectile {

  public WaterKunai(float x, float y, float dx, float dy, Face dir, int attack)
  {
    super(x,y,dx,dy,dir,attack);
    preAether=ElementalProperty.WATER;
    character = Characters.CASPIAN;
    
    downAttack = new Animation(4,8,12,false);
    upAttack = new Animation(4,8,12,false);
    rightAttack = new Animation(4,8,12,false);
    leftAttack = new Animation(4,8,12,false);
    
    rightAttack.add(811, 13, 10, 9);
    rightAttack.add(828, 12, 14, 9);
    rightAttack.add(851, 13, 14, 9);
    rightAttack.add(875, 13, 19, 9);
    rightAttack.setPausedFrame(875, 13, 19, 9);
    
    leftAttack.add(811, 13, 10, 9, 0, true);
    leftAttack.add(828, 12, 14, 9, 0, true);
    leftAttack.add(851, 13, 14, 9, 0, true);
    leftAttack.add(875, 13, 19, 9, 0, true);
    leftAttack.add(875, 13, 19, 9, 0, true);
    leftAttack.setPausedFrame(875, 13, 19, 9, 0, true);
    
    upAttack.add(811, 13, 10, 9, 1, false);
    upAttack.add(828, 12, 14, 9, 1, false);
    upAttack.add(851, 13, 14, 9, 1, false);
    upAttack.add(875, 13, 19, 9, 1, false);
    upAttack.setPausedFrame(875, 13, 19, 9, 1, false);
    
    downAttack.add(811, 13, 10, 9, 1, true);
    downAttack.add(828, 12, 14, 9, 1, true);
    downAttack.add(851, 13, 14, 9, 1, true);
    downAttack.add(875, 13, 19, 9, 1, true);
    downAttack.setPausedFrame(875, 13, 19, 9, 1, true);
    
    area = new Rectangle((int) x, (int) y, 10, 9);

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
