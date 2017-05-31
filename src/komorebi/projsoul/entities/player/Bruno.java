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

    magic = new MagicBar(maxMagic);
    health = new HUD(maxHealth);

    attack1 = melee;
    
    initializeSprites();
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
