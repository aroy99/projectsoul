package komorebi.projsoul.entities.player;

import static komorebi.projsoul.engine.KeyHandler.button;

import komorebi.projsoul.attack.CircleStrike;
import komorebi.projsoul.attack.SingleAttack;
import komorebi.projsoul.attack.projectile.AirSlash;
import komorebi.projsoul.attack.projectile.ProjectileAttack;
import komorebi.projsoul.attack.ElementalProperty;
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
  private static final int PROJ_COST = -8;
  private static final int PROJ_COOLDOWN = 30;
  
  public static final ProjectileAttack<AirSlash> proj = 
                                new ProjectileAttack<AirSlash>(new AirSlash());
  public static final SingleAttack<CircleStrike> aoe = 
                                new SingleAttack<CircleStrike>(new CircleStrike());
  
  private Animation[] castAni = new Animation[4];
  
  private int index;
  private int projCounter = PROJ_COOLDOWN;
  
  /**
   * Creates Sierra
   * 
   * @param x X pixel location
   * @param y Y pixel location
   */
  public Sierra(float x, float y) {

    super(x,y);
    charProperty = ElementalProperty.WIND;
    character = Characters.SIERRA;

    magic = new MagicBar(maxMagic);
    health = new HUD(maxHealth);
    
    attack1 = proj;
    attack2 = aoe;
    
    
    initializeSprites();
    
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
    
   }

  @Override
  public void renderAttack() {
    if (attack1 == aoe || attack1 == proj) {
      castAni[index].playCam(x, y);
    }
  }
  
  @Override
  public void update(){
    super.update();
    
    if(isAttacking){
      if(attack1 == aoe){
        
        if(!aoe.playing()){
          isAttacking = false;
//          canMove = true;
        }
      }else if(attack1 == proj){
        projCounter--;
        
        if(projCounter < 0){
          isAttacking = false;
          projCounter = PROJ_COOLDOWN;
        }
      }
    }
    
    if (button(Control.ATTACK) && !isAttacking){        
      if (attack1 == aoe  && magic.hasEnoughMagic(-CIRCLE_COST)){
        isAttacking = true;
        //      canMove = false;
        dx = 0;
        dy = 0;
        
        sprites.stopCurrent();
        
        magic.changeMagicBy(CIRCLE_COST);
        index = dir.getFaceNum();
        castAni[index].resume();

        attack1.newAttack(x+castAni[index].getCurrSX()/2+castAni[index].getCurrOffX(),
            y+castAni[index].getCurrSY()/2+castAni[index].getCurrOffY(),
            21.5f,40,dir,attack);
        
      }else if(attack1 == proj){
        int aDx = 0, aDy = 0;
        float aX = x, aY = y;
        
        isAttacking = true;

        switch (dir)
        {
          case DOWN:
            aDy = -3;
            aX = x-43/2+sx/2;
            break;
          case LEFT:
            aDx = -3;
            aY = y-43/2+sy/2;
            break;
          case RIGHT:
            aDx = 3;
            aY = y-43/2+sy/2;
            break;
          case UP:
            aDy = 3;
            aX = x-43/2+sx/2;
            break;
          default:
            break;          
        }

        sprites.stopCurrent();
        
        index = dir.getFaceNum();

        castAni[index].resume();
        
        magic.changeMagicBy(PROJ_COST);
        attack1.newAttack(aX,aY,aDx,aDy,dir,attack*2);

      }
    }

  }
  
  public void render(){
    
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
