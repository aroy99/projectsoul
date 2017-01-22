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

  /**
   * Creates Caspian
   * 
   * @param x X pixel location
   * @param y Y pixel location
   */
  public Caspian(float x, float y) {
    super(x, y);

    character = Characters.CASPIAN;

    upAni =    new Animation(6, 8, 11);
    downAni =  new Animation(6, 8, 11);
    rightAni = new Animation(6, 8, 11);

    hurtUpAni =    new Animation(2,8,16,35,11);
    hurtDownAni =  new Animation(2,8,16,34,11);
    hurtRightAni = new Animation(2,8,14,33,11);
    hurtLeftAni =  new Animation(2,8,14,33,11);

    downAni.add(8,162,16,34);
    downAni.add(28,164,17,32);
    downAni.add(49,161,18,35);
    downAni.add(71,162,16,34);
    downAni.add(91,164,17,32);
    downAni.add(112,161,18,35);
    
    downAni.setPausedFrame(166, 162, 16, 35);

    upAni.add(8,204,16,35);
    upAni.add(28,207,18,32);
    upAni.add(50,206,18,33);
    upAni.add(71,204,16,35);
    upAni.add(91,207,18,32);
    upAni.add(113,206,18,33);
    
    upAni.setPausedFrame(166, 207, 16, 33);

    rightAni.add(3,247,21,32);
    rightAni.add(30,246,14,33);
    rightAni.add(52,245,14,34);
    rightAni.add(72,247,22,32);
    rightAni.add(99,246,14,33);
    rightAni.add(120,245,15,34);

    rightAni.setPausedFrame(166,245,14,34);

    leftAni = rightAni.getFlipped();
    
    hurtUpAni.add(8,204);
    hurtUpAni.add(141, 205);

    hurtDownAni.add(8, 162);
    hurtDownAni.add(141, 163);

    hurtRightAni.add(30, 246);
    hurtRightAni.add(141, 246);

    hurtLeftAni.add(30, 246, true);
    hurtLeftAni.add(141, 246, true);

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
    
    if (button(Control.ATTACK) && !isAttacking && magic.hasEnoughMagic(2))
    {        

      isAttacking = true;

      int aDx = 0, aDy = 0;

      if (attack1 == melee)
      {
        upAni.hStop();
        downAni.hStop();
        leftAni.hStop();
        rightAni.hStop();

        magic.changeMagicBy(SWORD_COST);
        
        attack1.newAttack(x,y,aDx,aDy,dir,attack);
      } else if (attack1 == proj)
      {        
        //TODO Refactor, since this is ugly
        float sideX = 0.5f*PROJ_SPEED, sideY = 0.87f*PROJ_SPEED;
        
        switch (dir)
        {
          case DOWN:
            aDy = -PROJ_SPEED;
            sideX = 0.5f*PROJ_SPEED;
            sideY = 0.87f*PROJ_SPEED;
            
            attack1.newAttack(x,y,-sideX,-sideY,dir,attack);
            attack1.newAttack(x,y, sideX,-sideY,dir,attack);
            break;
          case LEFT:
            aDx = -PROJ_SPEED;
            sideX = 0.87f*PROJ_SPEED;
            sideY = 0.5f*PROJ_SPEED; 

            attack1.newAttack(x,y,-sideX,-sideY,dir,attack);
            attack1.newAttack(x,y,-sideX, sideY,dir,attack);
            break;
          case RIGHT:
            aDx = PROJ_SPEED;
            sideX = 0.87f*PROJ_SPEED;
            sideY = 0.5f*PROJ_SPEED;

            attack1.newAttack(x,y, sideX,-sideY,dir,attack);
            attack1.newAttack(x,y, sideX, sideY,dir,attack);
            break;
          case UP:
            aDy = PROJ_SPEED;
            sideX = 0.5f*PROJ_SPEED;
            sideY = 0.87f*PROJ_SPEED;

            attack1.newAttack(x,y,-sideX, sideY,dir,attack);
            attack1.newAttack(x,y, sideX, sideY,dir,attack);
            break;
          default:
            break;          
        }
        index = dir.getFaceNum();
        castAni[index].resume();
        
        magic.changeMagicBy(PROJ_COST);
        
        attack1.newAttack(x,y,aDx,aDy,dir,attack);
      } else if(attack1 == support){
        dx = 0;
        dy = 0;
        
        upAni.hStop();
        downAni.hStop();
        leftAni.hStop();
        rightAni.hStop();

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
