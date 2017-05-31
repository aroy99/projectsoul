package komorebi.projsoul.entities.player;

import komorebi.projsoul.gameplay.HUD;
import komorebi.projsoul.gameplay.MagicBar;

public class Sierra extends Player {

  public static int attack = 50, defense = 45, 
      maxHealth = 45, maxMagic = 60;    
  public static int level = 1, xp = 0, nextLevelUp = 10;

  public Sierra(float x, float y) {

    super(x,y);

    character = Characters.SIERRA;

    magic = new MagicBar(maxMagic);
    health = new HUD(maxHealth);
    
    initializeSprites();
   }

  @Override
  public void renderAttack() {
    // TODO Auto-generated method stub

  }

  @Override
  public void levelUp() {

    level++;

    Sierra.xp-=nextLevelUp;
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
    Sierra.xp += xp;

    if (Sierra.xp >= nextLevelUp)
    {
      levelUp();
    }
  }
}
