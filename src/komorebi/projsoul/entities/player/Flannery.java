package komorebi.projsoul.entities.player;

import static komorebi.projsoul.engine.KeyHandler.button;
import komorebi.projsoul.attack.ElementalProperty;
import komorebi.projsoul.attack.FireRingInstance;
import komorebi.projsoul.attack.RingOfFire;
import komorebi.projsoul.attack.projectile.FireBall;
import komorebi.projsoul.attack.projectile.ProjectileAttack;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.KeyHandler.Control;
import komorebi.projsoul.gameplay.HUD;
import komorebi.projsoul.gameplay.MagicBar;

/**
 * The fiery chick Flannery, which there can be only one of
 *
 * @author Andrew Faulkenberry
 * @author Aaron Roy
 */
public class Flannery extends Player {

  private ProjectileAttack<FireBall> projectile;
  private RingOfFire ring;
  
  
  private Animation leftThrow;
  private Animation rightThrow;
  private Animation upThrow;
  private Animation downThrow;

  private Animation currentAnimation;

  //Stats
  public static int    attack = 60,  defense = 50, 
                    maxHealth = 50, maxMagic = 40, money = 0;; 
  public static int level = 1, xp = 0, nextLevelUp = 10;
  
  //Magic costs
  public static final int PROJ_COST = -5;
  public static final int RING_COST = -8;


  /**
   * Creates Flannery
   * 
   * @param x X pixel location
   * @param y Y pixel location
   */
  public Flannery(float x, float y) {
    super(x,y);
    charProperty = ElementalProperty.FIRE;
    character = Characters.FLANNERY;
    
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

    characterDeathAni = new Animation(2,30,12,false);
    characterDeathAni.add(101,6,15,32,1,false);
    
    health = new HUD(maxHealth, money, maxMagic);

    projectile = new ProjectileAttack<FireBall>(new FireBall());
    ring = new RingOfFire(new FireRingInstance());
    
    attack1 = projectile;
    attack2 = ring;
    
    initializeSprites();
       
  }

  public void update()
  {
    super.update();

    if (isAttacking && attack1 == projectile && !currentAnimation.playing())
    {
      isAttacking = false;
      currentAnimation = null;
    }

    if (button(Control.ATTACK) && !isAttacking && health.hasEnoughMagic(
        5))
    {              
      if (attack1 == projectile)
      {
        sprites.stopCurrent();

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
        health.changeMagicBy(PROJ_COST);
        
        attack1.newAttack(x,y,aDx,aDy,dir,attack);
      }
      
      if (attack1 == ring)
      {
        ring.newXMark(x+8, y+16);
      }
    }
    
    if (KeyHandler.keyDown(Key.X) && !isAttacking && health.hasEnoughMagic(
        10) && attack1 == ring)
    {
      unlocked = false;
      
      dx = 0;
      dy = 0;
      
      ring.getInput();
      ring.update();
    } else if (KeyHandler.keyRelease(Key.X) && !isAttacking && health.hasEnoughMagic(
        8) && attack1 == ring)
    {
      ring.newAttack(0, 0, 0, 0, dir, attack);
      unlocked = true;
      health.changeMagicBy(RING_COST);
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
    nextLevelUp = getRequiredExp(level);

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
  @Override
  public void giveXP(int xp) {
    Flannery.xp += xp;
    
    while (Flannery.xp >= nextLevelUp)
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
