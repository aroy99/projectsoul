package komorebi.projsoul.entities;

import java.awt.Rectangle;

import komorebi.projsoul.attack.MeleeAttack;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.HUD;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MagicBar;

public class Caspian extends Player {
  
  public static int attack = 50, defense = 50, 
      maxHealth = 50, maxMagic = 50;
  public static int level = 1, xp = 0, nextLevelUp = 10;
  
  private MeleeAttack melee;

  public Caspian(float x, float y) {
    super(x, y);

    character = Characters.CASPIAN;
    
    upAni =    new Animation(6, 8, 11);
    downAni =  new Animation(6, 8, 11);
    leftAni =  new Animation(6, 8, 11);
    rightAni = new Animation(6, 8, 11);

    hurtUpAni = new Animation(2,8,16,35,11);
    hurtDownAni = new Animation(2,8,16,34,11);
    hurtRightAni = new Animation(2,8,14,33,11);
    hurtLeftAni = new Animation(2,8,14,33,11);

    downAni.add(8,162,16,34);
    downAni.add(28,164,17,32);
    downAni.add(49,161,18,35);
    downAni.add(71,162,16,34);
    downAni.add(91,164,17,32);
    downAni.add(112,161,18,35);

    upAni.add(8,204,16,35);
    upAni.add(28,207,18,32);
    upAni.add(50,206,18,33);
    upAni.add(71,204,16,35);
    upAni.add(91,207,18,32);
    upAni.add(113,206,18,33);

    rightAni.add(3,247,21,32);
    rightAni.add(30,246,14,33);
    rightAni.add(52,245,14,34);
    rightAni.add(72,247,22,32);
    rightAni.add(99,246,14,33);
    rightAni.add(120,245,15,34);
    
    rightAni.setPausedFrame(99,246,14,33);

    leftAni.add(3,247,21,32,0,true);
    leftAni.add(30,246,14,33,0,true);
    leftAni.add(52,245,14,34,0,true);
    leftAni.add(72,247,22,32,0,true);
    leftAni.add(99,246,14,33,0,true);
    leftAni.add(120,245,15,34,0,true);
    
    leftAni.setPausedFrame(99,246,14,33,0,true);

    hurtUpAni.add(8,204);
    hurtUpAni.add(141, 205);

    hurtDownAni.add(8, 162);
    hurtDownAni.add(141, 163);

    hurtRightAni.add(30, 246);
    hurtRightAni.add(141, 246);

    hurtLeftAni.add(30, 246, true);
    hurtLeftAni.add(141, 246, true);

    melee = new MeleeAttack(Characters.CASPIAN);
    
    magic = new MagicBar(maxMagic);
    health = new HUD(maxHealth);
    
  }
  
  public void update()
  {
    super.update();
   
    
    if (isAttacking && !melee.playing())
    {
      isAttacking = false;
    }
    
    if (KeyHandler.keyClick(Key.X) && !isAttacking && magic.hasEnoughMagic(
        (int) (10*(attack/Player.MEAN_STAT))))
    {        
      upAni.hStop();
      downAni.hStop();
      leftAni.hStop();
      rightAni.hStop();

      isAttacking = true;
      melee.newAttack(dir);
      
      magic.changeMagicBy(-10);
    }

    if (!isAttacking)
    {
      melee.setDirection(dir);
    } 

    melee.update((int) x, (int) y);
  }
  
  public Rectangle getAttackHitBox()
  {
    return melee.getHitBox();
  }

  @Override
  public void renderAttack() {
    melee.play(x, y);
  }

  @Override
  public void levelUp() {
    
    level++;
    
    int nAtt = (int) (Math.random()*3 + 1);
    int nDef = (int) (Math.random()*3 + 1);
    
    int nMag = (int) (Math.random()*8 + 3);
    int nHth = (int) (Math.random()*8 + 3);
    
    attack += nAtt;
    defense += nDef;
    maxMagic += nMag;
    maxHealth += nHth;
    
    magic.addToMaxMagic(nMag);
    health.addToMaxHealth(nHth);
  }

  @Override
  public void giveXP(int xp) {
    Caspian.xp += xp;
    
    if (Caspian.xp >= nextLevelUp)
    {
      levelUp();
      Caspian.xp-=nextLevelUp;
      
      //TODO This is not the final incrementation of xp
      nextLevelUp += 10;
    }
  }


}
