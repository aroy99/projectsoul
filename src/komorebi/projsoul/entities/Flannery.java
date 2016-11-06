package komorebi.projsoul.entities;

import komorebi.projsoul.attack.ProjectileAttack;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.HUD;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MagicBar;
import komorebi.projsoul.states.Death;

public class Flannery extends Player {

  private ProjectileAttack projectile;
  private Animation leftThrow;
  private Animation rightThrow;
  private Animation upThrow;
  private Animation downThrow;
  
  private Animation currentAnimation;
  
  public static int attack = 60, defense = 50, 
      maxHealth = 50, maxMagic = 40; 
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
    
    magic = new MagicBar(maxMagic);
    //health = new HUD(maxHealth);
    
    projectile = new ProjectileAttack(Characters.FLANNERY);
    
  }
  
  public void update()
  {
    super.update();
    
    if (projectile.isActive())
    {
      projectile.update();
    }
    
    if (isAttacking && !currentAnimation.playing())
    {
      isAttacking = false;
      currentAnimation = null;
    }
    if(Death.playable)
    {
    if (KeyHandler.keyClick(Key.X) && !isAttacking && magic.hasEnoughMagic(
        10))
    {        
      upAni.hStop();
      downAni.hStop();
      leftAni.hStop();
      rightAni.hStop();

      isAttacking = true;
                 
      switch (dir)
      {
        case DOWN:
          currentAnimation = downThrow;
          projectile.newAttack(x,y,0,-1,dir,(int) (25*(attack/Player.MEAN_STAT)));
          break;
        case LEFT:
          currentAnimation = leftThrow;
          projectile.newAttack(x,y,-1,0,dir,(int) (25*(attack/Player.MEAN_STAT)));
          break;
        case RIGHT:
          currentAnimation = rightThrow;
          projectile.newAttack(x,y,1,0,dir,(int) (25*(attack/Player.MEAN_STAT)));
          break;
        case UP:
          currentAnimation = upThrow;
          projectile.newAttack(x,y,0,1,dir,(int) (25*(attack/Player.MEAN_STAT)));
          break;
        default:
          break;
      }
      
      currentAnimation.resume();
      
      magic.changeMagicBy(-10);
    }
    }
    
   }
  
  
  public void render()
  {
    super.render();
    if (projectile.isActive())
    {
      projectile.play();
    }
  }

  @Override
  public void renderAttack() {
     currentAnimation.playCam(x, y);
  }

  @Override
  public void levelUp() {
    int nAtt = (int) (Math.random()*3 + 1);
    int nDef = (int) (Math.random()*3 + 1);
    
    int nMag = (int) (Math.random()*8 + 3);
    int nHth = (int) (Math.random()*8 + 3);
    
    attack += nAtt;
    defense += nDef;
    maxMagic += nMag;
    maxHealth += nHth;
    
    magic.addToMaxMagic(nMag);
    //health.addToMaxHealth(nHth);
    
  }
 
  public void giveXP(int xp) {
    Flannery.xp += xp;
    
    System.out.println("Flannery++");
    
    if (Flannery.xp >= nextLevelUp)
    {
      levelUp();
      Flannery.xp-=nextLevelUp;
      
      //TODO This is not the final incrementation of xp
      nextLevelUp += 10;
    }
  }

}
