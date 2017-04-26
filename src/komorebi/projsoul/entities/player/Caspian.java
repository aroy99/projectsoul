package komorebi.projsoul.entities.player;

import static komorebi.projsoul.engine.KeyHandler.button;

import komorebi.projsoul.attack.MeleeAttack;
import komorebi.projsoul.attack.ProjectileAttack;
import komorebi.projsoul.attack.WaterKunai;
import komorebi.projsoul.attack.WaterSword;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.KeyHandler.Control;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.gameplay.HUD;
import komorebi.projsoul.gameplay.MagicBar;
import komorebi.projsoul.states.Game;

import java.awt.Rectangle;

public class Caspian extends Player {

  public static int attack = 50, defense = 50, 
      maxHealth = 50, maxMagic = 50;
  public static int level = 1, xp = 0, nextLevelUp = 10;

  private MeleeAttack<WaterSword> melee;
  private ProjectileAttack<WaterKunai> proj;

  private Animation leftThrow;
  private Animation rightThrow;
  private Animation upThrow;
  private Animation downThrow;

  private Animation currentThrowAni;

  public Caspian(float x, float y) {
    super(x, y);

    character = Characters.CASPIAN;

    upThrow = new Animation(3,8,11,false);
    downThrow = new Animation(3,8,11,false);
    rightThrow = new Animation(3,8,11,false);
    leftThrow = new Animation(3,8,11,false);

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

    melee = new MeleeAttack<WaterSword>(new WaterSword());
    proj = new ProjectileAttack<WaterKunai>(new WaterKunai());

    magic = new MagicBar(maxMagic);
    health = new HUD(maxHealth);

    attack1 = melee;
    attack2 = proj; 
    
    initializeSprites();
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
        if (!currentThrowAni.playing())
        {
          isAttacking = false;
        }

      }
    }

    if (button(Control.ATTACK) && !isAttacking && magic.hasEnoughMagic(
        10))
    {        

      isAttacking = true;

      int aDx = 0, aDy = 0;

      if (attack1 == melee)
      {
        sprites.stopCurrent();

        magic.changeMagicBy(-10);
      } else if (attack1 == proj)
      {        
        switch (dir)
        {
          case DOWN:
            aDy = -3;
            currentThrowAni = downThrow;
            break;
          case LEFT:
            aDx = -3;
            currentThrowAni = leftThrow;
            break;
          case RIGHT:
            aDx = 3;
            currentThrowAni = rightThrow;
            break;
          case UP:
            aDy = 3;
            currentThrowAni = upThrow;
            break;
          default:
            break;          
        }

        currentThrowAni.resume();
        
        magic.changeMagicBy(-10);
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
      currentThrowAni.playCam(x, y);
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

    magic.addToMaxMagic(nMag);
    health.addToMaxHealth(nHth);
  }

  @Override
  public void giveXP(int xp) {
    Caspian.xp += xp;

    if (Caspian.xp >= nextLevelUp)
    {
      levelUp();
    }
  }


}
