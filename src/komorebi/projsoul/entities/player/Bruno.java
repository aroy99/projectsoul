package komorebi.projsoul.entities.player;

import static komorebi.projsoul.engine.KeyHandler.button;

import komorebi.projsoul.attack.Charge;
import komorebi.projsoul.attack.MeleeAttack;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.KeyHandler.Control;
import komorebi.projsoul.gameplay.HUD;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.gameplay.MagicBar;

public class Bruno extends Player {

  public static int attack = 45, defense = 60, 
      maxHealth = 55, maxMagic = 40;
  public static int level = 1, xp = 0, nextLevelUp = 10;
  
  private MeleeAttack<Charge> melee = new MeleeAttack<Charge>(new Charge());

  public Bruno(float x, float y) {


    super(x,y);

    character = Characters.BRUNO;

    upAni =    new Animation(10, 8, 12);
    downAni =  new Animation(8, 8, 12);
    leftAni =  new Animation(12, 8, 12);
    rightAni = new Animation(12, 8, 12);

    hurtUpAni = new Animation(2,8,24,31,12);
    hurtDownAni = new Animation(2,8,24,29,12);
    hurtRightAni = new Animation(2,8,32,32,12);
    hurtLeftAni = new Animation(2,8,32,32,12);

    downAni.add(162,761,24,30);
    downAni.add(191,761,24,30);
    downAni.add(218,759,24,32);
    downAni.add(246,760,24,31);
    downAni.add(274,761,24,30);
    downAni.add(246,760,24,31);
    downAni.add(218,759,24,32);
    downAni.add(191,761,24,30);

    upAni.add(204,514,24,32);
    upAni.add(232,515,24,31);
    upAni.add(260,515,24,31);
    upAni.add(288,516,23,30);
    upAni.add(260,515,24,31);
    upAni.add(232,515,24,31);
    upAni.add(204,514,24,32);
    upAni.add(177,516,23,30);
    upAni.add(149,516,24,30);
    upAni.add(177,516,23,30);

    rightAni.add(150,618,22,32,0,true);
    rightAni.add(175,620,25,30,0,true);
    rightAni.add(203,621,24,29,0,true);
    rightAni.add(175,620,25,30,0,true);
    rightAni.add(150,618,22,32,0,true);
    rightAni.add(125,619,22,31,0,true);
    rightAni.add(99,619,22,31,0,true);
    rightAni.add(66,620,28,30,0,true);
    rightAni.add(35,621,28,29,0,true);
    rightAni.add(66,620,28,30,0,true);
    rightAni.add(99,619,22,31,0,true);
    rightAni.add(125,619,22,31,0,true);

    leftAni.add(150,618,22,32);
    leftAni.add(175,620,25,30);
    leftAni.add(203,621,24,29);
    leftAni.add(175,620,25,30);
    leftAni.add(150,618,22,32);
    leftAni.add(125,619,22,31);
    leftAni.add(99,619,22,31);
    leftAni.add(66,620,28,30);
    leftAni.add(35,621,28,29);
    leftAni.add(66,620,28,30);
    leftAni.add(99,619,22,31);
    leftAni.add(125,619,22,31);

    hurtUpAni.add(231,551);
    hurtUpAni.add(362,761);

    hurtDownAni.add(154, 727);
    hurtDownAni.add(396, 764);

    hurtRightAni.add(195,654,true);
    hurtRightAni.add(426,763,true);

    hurtLeftAni.add(195,654);
    hurtLeftAni.add(426,763);

    magic = new MagicBar(maxMagic);
    health = new HUD(maxHealth);

    attack1 = melee;

  }

  @Override
  public void renderAttack() {
    if (attack1 == melee)
    {
      melee.getAttackInstance().play(x, y);
    }
   
    
  }
  
  public void update()
  {
    
    super.update();
    
    if (isAttacking && attack1 == melee)
    {
      melee.update();
      x = melee.getAttackInstance().getX();
      y = melee.getAttackInstance().getY();
      
      if (melee.getAttackInstance().isStopped())
      {
        isAttacking = false;
        noContact = false;
      }
    }
    
    if (button(Control.ATTACK) && !isAttacking)
    {
      isAttacking = true;
      
      float aDx = 0f, aDy = 0f;
      
      switch (dir)
      {
        case UP:
          aDy = 5f;
          break;
        case DOWN:
          aDy = -5f;
          break;
        case LEFT:
          aDx = -5f;
          break;
        case RIGHT:
          aDx = 5f;
          break;
      }
      
      if (attack1 == melee)
      {
        attack1.newAttack(x, y, aDx, aDy, dir, attack);
        isAttacking = true;
        
        magic.changeMagicBy(-10);
      }
      
    }
  }

  @Override
  public void levelUp() {

    level++;

    Bruno.xp-=nextLevelUp;
    nextLevelUp += 10;

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
    Bruno.xp += xp;

    if (Bruno.xp >= nextLevelUp)
    {
      levelUp();
    }
  }
  
  public boolean isCharging()
  {
    return noContact && isAttacking;
  }
}
