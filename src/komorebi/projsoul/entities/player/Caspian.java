package komorebi.projsoul.entities.player;

import static komorebi.projsoul.engine.KeyHandler.button;

import komorebi.projsoul.attack.MeleeAttack;
import komorebi.projsoul.attack.SingleAttack;
import komorebi.projsoul.attack.WaterBarrier;
import komorebi.projsoul.attack.WaterSword;
import komorebi.projsoul.attack.projectile.ProjectileAttack;
import komorebi.projsoul.attack.projectile.WaterKunai;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.KeyHandler.Control;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.gameplay.HUD;
import komorebi.projsoul.gameplay.MagicBar;
import komorebi.projsoul.map.MapHandler;
import komorebi.projsoul.states.Game;

import java.awt.Rectangle;

/**
 * The water fighter Caspian, which there can be only one of
 *
 * @author Andrew Faulkenberry
 * @author Aaron Roy
 */
public class Caspian extends Player {

  //Stats
  public static int attack = 50,  defense = 50, 
                 maxHealth = 50, maxMagic = 50;
  public static int level = 1, xp = 0, nextLevelUp = 10;
  
  //Magic cost
  public static final int SWORD_COST = -2;
  public static final int PROJ_COST = -3;
  public static final int SUPP_COST = -10;
  
  //Other constants
  private static final int PROJ_SPEED = 3;
  private static final int SUPP_COOLDOWN = 50;

  public static final MeleeAttack<WaterSword>  melee = 
                                  new MeleeAttack<WaterSword>(new WaterSword());     
  public static final ProjectileAttack<WaterKunai> proj = 
                                  new ProjectileAttack<WaterKunai>(new WaterKunai()); 
  public static final SingleAttack<WaterBarrier> support = 
                                  new SingleAttack<WaterBarrier>(new WaterBarrier());
  private Animation[] castAni = new Animation[4];
  
  private int index;
  
  private int suppCounter = SUPP_COOLDOWN;

  private Animation currentAnimation; 
  private Animation currentThrowAni;

  /**
   * Creates Caspian
   * 
   * @param x X pixel location
   * @param y Y pixel location
   */
  public Caspian(float x, float y) {
    super(x, y);

    character = Characters.CASPIAN;

    for(int i = 0; i < 3; i++){
      castAni[i] = new Animation(3, 4, 11, false);
    }
    
    castAni[0].add(50,206,18,33);
    castAni[0].add(50,206,18,33);
    castAni[0].add(50,206,18,33);

    castAni[1].add(49,161,18,35);
    castAni[1].add(49,161,18,35);
    castAni[1].add(49,161,18,35);

    castAni[2].add(52,245,14,34);
    castAni[2].add(52,245,14,34);
    castAni[2].add(52,245,14,34);

    castAni[3] = castAni[2].getFlipped();
    
    magic = new MagicBar(maxMagic);
    health = new HUD(maxHealth);

    attack1 = melee;
    attack2 = proj; 
    attack3 = support;
    
    initializeSprites();
  }

  @Override
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

        for (Enemy enemy: MapHandler.getEnemies())
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
        if (!castAni[index].playing())
        {
          isAttacking = false;
        }

      } else if (attack1 == support){
        dx = 0; dy = 0;
        
        if (!castAni[index].playing())
        {
          suppCounter--;
          
          if(suppCounter < 0){
            isAttacking = false;
            suppCounter = SUPP_COOLDOWN;
          }
        }
      }
    }

    //System.out.println(button(Control.ATTACK) +" and "+ !isAttacking +" and "+ magic.hasEnoughMagic(10));
    
    if (button(Control.ATTACK) && !isAttacking && magic.hasEnoughMagic(2))
    {        

      isAttacking = true;

      int aDx = 0, aDy = 0;

      if (attack1 == melee)
      {
        sprites.stopCurrent();

        magic.changeMagicBy(SWORD_COST);
        
        attack1.newAttack(x,y,aDx,aDy,dir,attack);
      } else if (attack1 == proj)
      {        
        switch (dir)
        {
          case DOWN:  aDy = -PROJ_SPEED; break;
          case LEFT:  aDx = -PROJ_SPEED; break;
          case RIGHT: aDx =  PROJ_SPEED; break;
          case UP:    aDy =  PROJ_SPEED; break;
          default:
            break;          
        }
        
        index = dir.getFaceNum();
        castAni[index].resume();
        
        magic.changeMagicBy(PROJ_COST);
        
        attack1.newAttack(x,y + castAni[index].getCurrSY()/2+
            castAni[index].getCurrOffY(),aDx,aDy,dir,attack);
      } else if(attack1 == support){
        dx = 0;
        dy = 0;
        
        sprites.stopCurrent();

        magic.changeMagicBy(SUPP_COST);
        
        index = dir.getFaceNum();
        castAni[index].resume();
        
        attack1.newAttack(x+castAni[index].getCurrSX()/2+
            castAni[index].getCurrOffX(), y +
            castAni[index].getCurrSY()/2+castAni[index].getCurrOffY(),
            21, 0,dir,attack);
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
    } else if (attack1 == proj || attack1 == support)
    {
      castAni[index].playCam(x, y);
    }

  }

  @Override
  public void levelUp() {

    level++;

    Caspian.xp-=nextLevelUp;
    nextLevelUp = getRequiredExp(level);

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

    while (Caspian.xp >= nextLevelUp)
    {
      levelUp();
    }
  }


}
