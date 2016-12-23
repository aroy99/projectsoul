package komorebi.projsoul.entities;

import komorebi.projsoul.attack.FireBall;
import komorebi.projsoul.attack.FireRingInstance;
import komorebi.projsoul.attack.ProjectileAttack;
import komorebi.projsoul.attack.RingOfFire;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.HUD;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MagicBar;

public class Flannery extends Player {

  private ProjectileAttack<FireBall> projectile;
  private RingOfFire ring;
  
  
  private Animation leftThrow;
  private Animation rightThrow;
  private Animation upThrow;
  private Animation downThrow;

  private Animation currentAnimation;

  public static int attack = 60, defense = 50, 
      maxHealth = 50, maxMagic = 40, money = 0; 
  public static int level = 1, xp = 0, nextLevelUp = 10;
  
  public Flannery(float x, float y) {

    super(x,y);

    character = Characters.FLANNERY;

    upAni =    new Animation(6, 8, 12);
    downAni =  new Animation(6, 8, 12);
    leftAni =  new Animation(6, 8, 12);
    rightAni = new Animation(6, 8, 12);

    hurtUpAni = new Animation(2,8,26,29,12);
    hurtDownAni = new Animation(2,8,28,31,12);
    hurtRightAni = new Animation(2,8,24,31,12);
    hurtLeftAni = new Animation(2,8,24,31,12);
    characterDeathAni = new Animation(2,30,12,false);

    downAni.add(6,52,23,31);
    downAni.add(31,52,22,31);
    downAni.add(57,52,23,32);
    downAni.add(83,53,23,31);
    downAni.add(110,52,23,32);
    downAni.add(137,52,22,31);

    upAni.add(6,131,21,32);
    upAni.add(30,131,24,32);
    upAni.add(58,132,23,31);
    upAni.add(84,131,21,32);
    upAni.add(109,131,24,32);
    upAni.add(137,132,23,31);

    rightAni.add(8,91,16,32,0,true);
    rightAni.add(30,91,16,32,0,true);
    rightAni.add(52,92,22,31,0,true);
    rightAni.add(78,92,20,32,0,true);
    rightAni.add(102,92,16,32,0,true);
    rightAni.add(123,93,18,31,0,true);

    leftAni.add(8,91,16,32);
    leftAni.add(30,91,16,32);
    leftAni.add(52,92,22,31);
    leftAni.add(78,92,20,32);
    leftAni.add(102,92,16,32);
    leftAni.add(123,93,18,31);

    hurtUpAni.add(374,227);
    hurtUpAni.add(376, 263);

    hurtDownAni.add(314, 224);
    hurtDownAni.add(314, 261);

    hurtRightAni.add(346,225,true);
    hurtRightAni.add(347,261,true);

    hurtLeftAni.add(346,225);
    hurtLeftAni.add(347,261);

    downThrow = new Animation(6,8,12,false);
    downThrow.add(315,100,21,32);
    downThrow.add(339,100,20,32);
    downThrow.add(362,100,21,32);
    downThrow.add(387,100,22,32);
    downThrow.add(412,100,22,32);
    downThrow.add(438,100,22,32);

    leftThrow = new Animation(6,8,12,false);
    leftThrow.add(439,138,21,32);
    leftThrow.add(414,138,22,32);
    leftThrow.add(388,138,22,32);
    leftThrow.add(363,138,21,32);
    leftThrow.add(339,138,20,32);
    leftThrow.add(307,138,29,32);

    rightThrow = new Animation(6,8,12,false);
    rightThrow.add(439,138,21,32,0,true);
    rightThrow.add(414,138,22,32,0,true);
    rightThrow.add(388,138,22,32,0,true);
    rightThrow.add(363,138,21,32,0,true);
    rightThrow.add(339,138,20,32,0,true);
    rightThrow.add(307,138,29,32,0,true);

    upThrow = new Animation(6,8,12,false);
    upThrow.add(431,180,23,32);
    upThrow.add(408,180,18,32);
    upThrow.add(385,180,18,32);
    upThrow.add(355,180,23,32);
    upThrow.add(333,180,18,32);
    upThrow.add(313,180,16,32);
    
	characterDeathAni.add(101,6,15,32,1,false);
    
    magic = new MagicBar(maxMagic);
    health = new HUD(maxHealth, money);

    projectile = new ProjectileAttack<FireBall>(new FireBall());
    ring = new RingOfFire(new FireRingInstance());
    
    attack1 = projectile;
    attack2 = ring;
   
  }

  public void update()
  {
    super.update();
    
   
    
   
    if (isAttacking && attack1 == projectile && !currentAnimation.playing())
    {
      isAttacking = false;
      currentAnimation = null;
    }

    if (KeyHandler.keyClick(Key.X) && !isAttacking && magic.hasEnoughMagic(
        10))
    {              
      if (attack1 == projectile)
      {
        upAni.hStop();
        downAni.hStop();
        leftAni.hStop();
        rightAni.hStop();

        isAttacking = true;
        
        int aDx = 0, aDy = 0;
        
        switch (dir)
        {
          case DOWN:
            aDy = -2;
            currentAnimation = downThrow;
            break;
          case LEFT:
            aDx = -2;
            currentAnimation = leftThrow;
            break;
          case RIGHT:
            aDx = 2;
            currentAnimation = rightThrow;
            break;
          case UP:
            aDy = 2;
            currentAnimation = upThrow;
            break;
          default:
            break;          
        }

        currentAnimation.resume();
        magic.changeMagicBy(-10);
        
        attack1.newAttack(x,y,aDx,aDy,dir,attack);
      }
      
      if (attack1 == ring)
      {
        ring.newXMark(x+8, y+16);
      }
    }
    
    if (KeyHandler.keyDown(Key.X) && !isAttacking && magic.hasEnoughMagic(
        10) && attack1 == ring)
    {
      canMove = false;
      
      dx = 0;
      dy = 0;
      
      ring.getInput();
      ring.update();
    } else if (KeyHandler.keyRelease(Key.X) && !isAttacking && magic.hasEnoughMagic(
        10) && attack1 == ring)
    {
      ring.newAttack(0, 0, 0, 0, dir, attack);
      canMove = true;
      magic.changeMagicBy(-10);
    }
    }

  


  public void render()
  {
    super.render();
    
    if (attack1 == ring && ring.isAiming())
    {
      ring.render();
    }
    
  }

  @Override
  public void renderAttack() {
    if (attack1 == projectile)
    {
      currentAnimation.playCam(x, y);
    }
  }

  @Override
  public void levelUp() {

    level++;

    Flannery.xp-=nextLevelUp;
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

  public void giveXP(int xp) {
    Flannery.xp += xp;
    
    if (Flannery.xp >= nextLevelUp)
    {
      levelUp();
    }
  }

}
