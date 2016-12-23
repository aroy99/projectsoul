package komorebi.projsoul.entities;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.HUD;
import komorebi.projsoul.engine.MagicBar;

public class Sierra extends Player {

  public static int attack = 50, defense = 45, 
      maxHealth = 45, maxMagic = 60, money = 0;    
  public static int level = 1, xp = 0, nextLevelUp = 10;

  public Sierra(float x, float y) {

    super(x,y);

    character = Characters.SIERRA;

    upAni =    new Animation(6, 8, 12);
    downAni =  new Animation(6, 8, 12);
    leftAni =  new Animation(6, 8, 12);
    rightAni = new Animation(6, 8, 12);

    hurtUpAni = new Animation(2,8,14,32,12);
    hurtDownAni = new Animation(2,8,16,32,12);
    hurtRightAni = new Animation(2,8,24,31,12);
    hurtLeftAni = new Animation(2,8,22,28,12);
    characterDeathAni = new Animation(3,45,12,false);

    downAni.add(631,192,15,35);
    downAni.add(650,194,14,33);
    downAni.add(668,192,16,35);
    downAni.add(688,192,15,35);
    downAni.add(707,194,14,33);
    downAni.add(724,192,16,35);

    upAni.add(633,237,14,33);
    upAni.add(652,238,13,32);
    upAni.add(668,237,16,33);
    upAni.add(687,237,14,33);
    upAni.add(706,238,13,32);
    upAni.add(726,237,16,33);

    rightAni.add(626,279,18,31);
    rightAni.add(648,278,17,32);
    rightAni.add(669,278,18,32);
    rightAni.add(688,280,18,30);
    rightAni.add(709,278,18,32);
    rightAni.add(730,278,19,32);

    leftAni.add(626,279,18,31,0,true);
    leftAni.add(648,278,17,32,0,true);
    leftAni.add(669,278,18,32,0,true);
    leftAni.add(688,280,18,30,0,true);
    leftAni.add(709,278,18,32,0,true);
    leftAni.add(730,278,19,32,0,true);

    hurtUpAni.add(704,561);
    hurtUpAni.add(754,645);

    hurtDownAni.add(701, 681);
    hurtDownAni.add(751, 682);

    hurtRightAni.add(887,442);
    hurtRightAni.add(914,442);

    hurtLeftAni.add(887,442,true);
    hurtLeftAni.add(914,442,true);
    
    characterDeathAni.add(582,615,20,24,0,false);
    characterDeathAni.add(807,529,42,14,0,false);

    magic = new MagicBar(maxMagic);
    health = new HUD(maxHealth, money);
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

  public void giveXP(int xp) {
    Sierra.xp += xp;

    if (Sierra.xp >= nextLevelUp)
    {
      levelUp();
    }
  }
}
