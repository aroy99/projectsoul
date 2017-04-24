package komorebi.projsoul.entities;

import java.awt.Rectangle;

import komorebi.projsoul.attack.MeleeAttack;
import komorebi.projsoul.attack.ProjectileAttack;
import komorebi.projsoul.attack.WaterKunai;
import komorebi.projsoul.attack.WaterSword;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.HUD;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.states.Game;

public class Caspian extends Player {

  public static int attack = 50, defense = 50, 
      maxHealth = 50, maxMagic = 50, money = 0;
  public static int level = 1, xp = 0, nextLevelUp = 10;
  
  private MeleeAttack<WaterSword> melee;
  private ProjectileAttack<WaterKunai> proj;

  private Animation leftThrow;
  private Animation rightThrow;
  private Animation upThrow;
  private Animation downThrow;

  private Animation currentAnimation;

  public Caspian(float x, float y) {
    super(x, y);

    character = Characters.CASPIAN;

    upAni =    new Animation(6, 8, 11);
    downAni =  new Animation(6, 8, 11);
    leftAni =  new Animation(6, 8, 11);
    rightAni = new Animation(6, 8, 11);

    upThrow = new Animation(3,8,11,false);
    downThrow = new Animation(3,8,11,false);
    rightThrow = new Animation(3,8,11,false);
    leftThrow = new Animation(3,8,11,false);

    hurtUpAni = new Animation(2,8,16,35,11);
    hurtDownAni = new Animation(2,8,16,34,11);
    hurtRightAni = new Animation(2,8,14,33,11);
    hurtLeftAni = new Animation(2,8,14,33,11);
    characterDeathAni = new Animation(2,30,11,false);

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

    upThrow.add(50,206,18,33);
    upThrow.add(50,206,18,33);
    upThrow.add(50,206,18,33);

    downThrow.add(49,161,18,35);
    downThrow.add(49,161,18,35);
    downThrow.add(49,161,18,35);

    rightThrow.add(52,245,14,34);
    rightThrow.add(52,245,14,34);
    rightThrow.add(52,245,14,34);

    leftThrow.add(52,245,14,34,0,true);
    leftThrow.add(52,245,14,34,0,true);
    leftThrow.add(52,245,14,34,0,true);
    
	characterDeathAni.add(8,162,16,35,1,false);
  
    melee = new MeleeAttack<WaterSword>(new WaterSword());
    proj = new ProjectileAttack<WaterKunai>(new WaterKunai());
    
    health = new HUD(maxHealth, money, maxMagic);

    attack1 = melee;
    attack2 = proj;
  }

  public void update()
  {
	super.update();
	
    if (isAttacking)
    {
      if (attack1 == melee)
      {
        melee.update(x, y);
        
        if (!melee.playing())
        {
          isAttacking = false;
        } 

        for (Enemy enemy: Game.getMap().getEnemies())
        {
          if (melee.getAttackInstance().getHitBox().intersects(enemy.getHitBox()) 
              && !enemy.invincible())
          {
            enemy.inflictPain((int) (Player.getAttack(Characters.CASPIAN)), dir,
                Characters.CASPIAN);
          }

        }
      } else if (attack1 == proj)
      {
        if (!currentAnimation.playing())
        {
          isAttacking = false;
        }

      }
    }
    
   
    if (KeyHandler.keyClick(Key.X) && !isAttacking && health.hasEnoughMagic(
        10))
    {        

      isAttacking = true;

      int aDx = 0, aDy = 0;

      if (attack1 == melee)
      {
        upAni.hStop();
        downAni.hStop();
        leftAni.hStop();
        rightAni.hStop();

        health.changeMagicBy(-10);
      } else if (attack1 == proj)
      {        
        switch (dir)
        {
          case DOWN:
            aDy = -3;
            currentAnimation = downThrow;
            break;
          case LEFT:
            aDx = -3;
            currentAnimation = leftThrow;
            break;
          case RIGHT:
            aDx = 3;
            currentAnimation = rightThrow;
            break;
          case UP:
            aDy = 3;
            currentAnimation = upThrow;
            break;
          default:
            break;          
        }

        currentAnimation.resume();
        
        health.changeMagicBy(-10);
      }

      if (attack1 != null)
      {
        attack1.newAttack(x,y,aDx,aDy,dir,attack);
      }
    }
    }
  

  public Rectangle getAttackHitBox()
  {
    return melee.getAttackInstance().getHitBox();
  }

  @Override
  public void renderAttack() {

    if (attack1 == melee)
    {
      melee.getAttackInstance().play(x, y);
    } else if (attack1 == proj)
    {
      currentAnimation.playCam(x, y);
    }

  }
  @Override
  public void levelUp() {

    level++;

    Caspian.xp-=nextLevelUp;
    nextLevelUp += 10;

    int nAtt = (int) (Math.random()*3 + 1);
    int nDef = (int) (Math.random()*3 + 1);

    int nMag = (int) (Math.random()*8 + 3);
    int nHth = (int) (Math.random()*8 + 3);

    attack += nAtt;
    defense += nDef;
    maxMagic += nMag;
    maxHealth += nHth;

    health.addToMaxMagic(nMag);
    health.addToMaxHealth(nHth);
  }

  
  public void giveXP(int xp) {
    Caspian.xp += xp;

    if (Caspian.xp >= nextLevelUp)
    {
      levelUp();
    }
  }
  
  public static void addDefense(int def)
  {
	  defense+=def;
  }
  
  public static void subDefense(int def)
  {
	  defense-=def;
  }


}
