package komorebi.projsoul.entities.player;

import static komorebi.projsoul.engine.KeyHandler.button;

import komorebi.projsoul.attack.CircleStrike;
import komorebi.projsoul.attack.SingleAttack;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.KeyHandler.Control;
import komorebi.projsoul.gameplay.HUD;
import komorebi.projsoul.gameplay.MagicBar;

/**
 * The airy orphan Sierra, which there can be only one of
 *
 * @author Andrew Faulkenberry
 * @author Aaron Roy
 */
public class Sierra extends Player {

  public static int attack = 50,  defense = 45, 
                 maxHealth = 45, maxMagic = 60;    
  public static int level = 1, xp = 0, nextLevelUp = 10;
  
  //Magic Cost
  public static final int CIRCLE_COST = -5;
  
  private SingleAttack<CircleStrike> aoe;
  
  private Animation[] castAni = new Animation[4];
  
  private int index;
  
  /**
   * Creates Sierra
   * 
   * @param x X pixel location
   * @param y Y pixel location
   */
  public Sierra(float x, float y) {

    super(x,y);

    character = Characters.SIERRA;

    upAni =    new Animation(6, 8, 12);
    downAni =  new Animation(6, 8, 12);
    rightAni = new Animation(6, 8, 12);

    hurtUpAni = new Animation(2,8,14,32,12);
    hurtDownAni = new Animation(2,8,16,32,12);
    hurtRightAni = new Animation(2,8,24,31,12);

    upAni.add(633,237,14,33);
    upAni.add(652,238,13,32);
    upAni.add(668,237,16,33);
    upAni.add(687,237,14,33);
    upAni.add(706,238,13,32);
    upAni.add(726,237,16,33);
    upAni.setPausedFrame(584, 238, 12, 32);

    downAni.add(631,192,15,35);
    downAni.add(650,194,14,33);
    downAni.add(668,192,16,35);
    downAni.add(688,192,15,35);
    downAni.add(707,194,14,33);
    downAni.add(724,192,16,35);
    downAni.setPausedFrame(566, 194, 13, 33);

    rightAni.add(626,279,18,31);
    rightAni.add(648,278,17,32);
    rightAni.add(669,278,18,32);
    rightAni.add(688,280,18,30);
    rightAni.add(709,278,18,32);
    rightAni.add(730,278,19,32);
    rightAni.setPausedFrame(559, 278, 16, 32);
    
    leftAni = rightAni.getFlipped();

    hurtUpAni.add(704,561);
    hurtUpAni.add(754,645);

    hurtDownAni.add(701, 681);
    hurtDownAni.add(751, 682);

    hurtRightAni.add(887,442);
    hurtRightAni.add(914,442);
    
    hurtLeftAni = hurtRightAni.getFlipped();
    
    for(int i = 0; i < 4; i++){
      castAni[i] = new Animation(2, 8, 12);
    }
    
    //UP
    castAni[0].add(631, 864, 18, 32, -4, 0);
    castAni[0].add(653, 864, 18, 32, -4, 0);
    
    //DOWN
    castAni[1].add(591, 860, 16, 36, -2, -2);
    castAni[1].add(611, 859, 16, 37, -2, -2);

    
    //RIGHT
    castAni[2].add(674, 864, 30, 32, -5, 0);
    castAni[2].add(707, 864, 29, 32, -4, 0);
    
    //LEFT
    castAni[3].add(674, 864, 30, 32, -8, 0, true);
    castAni[3].add(707, 864, 29, 32, -8, 0, true);

    aoe = new SingleAttack<CircleStrike>(new CircleStrike());
    
    magic = new MagicBar(maxMagic);
    health = new HUD(maxHealth);
    
    attack1 = aoe;
    
  }

  @Override
  public void renderAttack() {
    if (attack1 == aoe) {
      castAni[index].playCam(x, y);
    }
  }
  
  @Override
  public void update(){
    super.update();
    
    if(isAttacking){
      if(attack1 == aoe){
        aoe.update();
        
        if(!aoe.playing()){
          isAttacking = false;
//          canMove = true;
        }
      }
    }
    
    if (button(Control.ATTACK) && !isAttacking && magic.hasEnoughMagic(-CIRCLE_COST))
    {        

      isAttacking = true;
//      canMove = false;
      dx = 0;
      dy = 0;

      if (attack1 == aoe)
      {
        upAni.hStop();
        downAni.hStop();
        leftAni.hStop();
        rightAni.hStop();

        magic.changeMagicBy(CIRCLE_COST);
        index = dir.getFaceNum();
        
        attack1.newAttack(x+castAni[index].getCurrSX()/2+castAni[index].getCurrOffX(),
            y+castAni[index].getCurrSY()/2+castAni[index].getCurrOffY(),
            21.5f,40,dir,attack);
        
      }
    }

  }
  
  public void render(){
    if(isAttacking){
      aoe.getAttackInstance().play();
    }
    super.render();
  }

  @Override
  public void levelUp() {

    level++;

    Sierra.xp-=nextLevelUp;
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
    Sierra.xp += xp;

    while (Sierra.xp >= nextLevelUp)
    {
      levelUp();
    }
  }
}
