/**
 * Player.java       May 15, 2016, 11:58:06 PM
 */
package komorebi.projsoul.entities.player;

import komorebi.projsoul.attack.Attack;
import komorebi.projsoul.attack.AttackInstance;
import komorebi.projsoul.attack.CircleStrike;
import komorebi.projsoul.attack.FireRingInstance;
import komorebi.projsoul.attack.RingOfFire;
import komorebi.projsoul.attack.WaterBarrier;
import komorebi.projsoul.attack.projectile.ProjectileAttack;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Arithmetic;
import komorebi.projsoul.engine.CollisionDetector;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.Inventory;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Main;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.engine.ThreadHandler.TrackableThread;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.Person;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.entities.sprites.SpriteSet;
import komorebi.projsoul.gameplay.Camera;
import komorebi.projsoul.gameplay.HUD;
import komorebi.projsoul.gameplay.MagicBar;
import komorebi.projsoul.items.Armor;
import komorebi.projsoul.items.CharacterItem;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.map.MapHandler;
import komorebi.projsoul.states.Game;
import komorebi.projsoul.states.State.States;
import komorebi.projsoul.attack.ElementalProperty;

import org.lwjgl.input.Keyboard;

import java.awt.Rectangle;

/**
 * @author Aaron Roy
 * @author Andrew Faulkenberry
 */
public abstract class Player extends Person implements Playable{

  private boolean up;
  private boolean down;
  private boolean left;
  private boolean right;
  private boolean run;
  private boolean pause;
  private boolean guiding;

  public static boolean deathStuff = false;
  private int count;
  CharacterItem items[];


  private boolean dying;
  private boolean dead; 

  public Characters character;
  public ElementalProperty charProperty;
  public boolean isAttacking;

  protected boolean unlocked = true;

  private int framesToGo;
  private boolean hasInstructions;

  int aniSpeed = ANI_SPEED;

  public SpriteSet hurtSprites;
  public Animation deathAni;
  public Animation characterDeathAni;


  private int hurtCount;
  public static int INVINCIBILITY = 60;

  protected boolean invincible;
  private boolean restoreMvmtX;
  private boolean restoreMvmtY;

  private static final float SPEED = 1;

  public Rectangle future;

  public HUD health;

  private TrackableThread waiting;

  protected boolean noContact;

  public Attack<? extends AttackInstance> attack1, attack2, attack3;

  /**
   * @param x x pos, from left
   * @param y y pos from bottom
   */
  public Player(float x, float y) {
    super(x, y, 16, 24);

    restoreMvmtX = true;
    restoreMvmtY = true;

    area = new Rectangle((int) this.x, (int) this.y, 16, 24);
    future = new Rectangle((int) x, (int) y, 16, 24);

    deathAni = new Animation(4,8,16,21,11,false);
    deathAni.add(0, 57);
    deathAni.add(0, 82);
    deathAni.add(0, 103);
    deathAni.add(0, 124);


  }

  protected void initializeSprites()
  {
    sprites = character.getNewWalkingSprites();
    hurtSprites = character.getNewHurtSprites();
  }

  public void getInput(){

    if (unlocked)
    {
      up =    Keyboard.isKeyDown(Keyboard.KEY_UP) && 
          !Keyboard.isKeyDown(Keyboard.KEY_DOWN);
      down =  Keyboard.isKeyDown(Keyboard.KEY_DOWN) && 
          !Keyboard.isKeyDown(Keyboard.KEY_UP);
      left =  Keyboard.isKeyDown(Keyboard.KEY_LEFT) && 
          !Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
      right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && 
          !Keyboard.isKeyDown(Keyboard.KEY_LEFT);

      run = KeyHandler.keyDown(Key.Z);
    }

  }

  /**
   * @see komorebi.projsoul.engine.Renderable#update()
   */
  @Override
  public void update() {

    super.update();

    health.update();
    if(health.health<=0)
    {
      deathStuff = true;
      if(MapHandler.allPlayersDead())
      {
        GameHandler.switchState(States.DEATH);
      }
    }


    int aniSpeed = 8;
    if(!deathStuff){

      if (unlocked) {

        if (!guiding && restoreMvmtX && restoreMvmtY)
        {
          if(left){
            dx = -SPEED;

            if (!isAttacking)
            {
              turn(Face.LEFT);
              resumeAnimationIfStopped();
            }
          }
          if(right){
            dx = SPEED;
            if (!isAttacking)
            {
              turn(Face.RIGHT);
              resumeAnimationIfStopped();
            }
          }

          if(!(left || right)){
            dx = 0;
          }

          if(down){
            dy = -SPEED;
            if (!isAttacking)
            {
              turn(Face.DOWN);
              resumeAnimationIfStopped();
            }
          }
          if(up){
            dy = SPEED;
            if (!isAttacking)
            {
              turn(Face.UP);
              resumeAnimationIfStopped();
            }
          }

          if(!(up || down)){
            dy = 0;
          }

          if(run){
            dx *=2;
            dy *=2;
            aniSpeed = ANI_SPEED/2;
          }else{
            aniSpeed = ANI_SPEED;
          }

          if (!moving())
          {
            sprites.stopCurrent();
            x = (float) Math.floor(x);
            y = (float) Math.floor(y);
          }
        }

        if (blockedByNpc()[0] || blockedByEnemy()[0])
        {
          dx = 0;
        } 

        if (blockedByNpc()[1] || blockedByEnemy()[1])
        {
          dy = 0;
        }
      }

      /*
      if((up && (left || right)) || (down && (left || right))){
        dx *= Math.sqrt(2)/2;
        dy *= Math.sqrt(2)/2;
        aniSpeed = (int)Math.round(aniSpeed / (Math.sqrt(2)/2));
      }
       */



      sprites.setAniSpeed(aniSpeed);

      if (!deathStuff && invincible)
      {
        hurtCount--;

        if (!restoreMvmtX)
        {
          if (Math.abs(dx) <= 0.5 && Math.abs(dx) >= 0)
          {
            dx = 0;
            restoreMvmtX = true;
          }
          if (dx > 0){
            dx-=0.5;
          }
          if (dx < 0){
            dx+=0.5;
          }
        }

        if (!restoreMvmtY)
        {
          if (Math.abs(dy) <= 0.5 && Math.abs(dy) >= 0)
          {
            dy = 0;
            restoreMvmtY = true;
          }
          if (dy > 0){
            dy-=0.5;
          }
          if (dy < 0){
            dy+=0.5;
          }
        }

        if (hurtCount <= 0)
        {
          invincible = false;

          hurtSprites.stopCurrent();
        }
      }

      //DEBUG God Mode
      if(!KeyHandler.keyDown(Key.G)){

        //Guide only if the player is not going diagonally
        if((up || down) ^ (left || right)){
          CollisionDetector.guidePlayer(x, y, dx, dy);
        }
        boolean[] col = CollisionDetector.checkCollisions(x,y,dx,dy);

        if(!col[0] || !col[2]){
          dy=0;
          dx*=.75f;
        }
        if(!col[1] || !col[3]){
          dx=0;
          dy*=.75f;
        }
      }

      //DEBUG Reset location R
      if(KeyHandler.keyClick(Key.R)){
        x = 100;
        y = 100;
        Camera.center(x, y);
      }

    }else {
      sprites.stopCurrent();
    }

    overrideImproperMovements();

    Camera.move(dx, dy);
    x += dx;
    y += dy;

    area.x += dx;
    area.y += dy;

    if (hasInstructions)
    {
      if (dx != 0) 
      {
        framesToGo-=Math.abs(dx);
      } else if (dy != 0)
      {
        framesToGo-=Math.abs(dy);
      } else if (pause)
      {
        framesToGo--;
      }
    }

    //DEBUG Print Location L
    if(KeyHandler.keyClick(Key.L)){
      System.out.println("x: "+x+", y: "+y);
    }

    area.x = (int) x;
    area.y = (int) y;

    future.x = (int) x;
    future.y = (int) y;

    guiding = false;

    health.update();

    ProjectileAttack.update();
    RingOfFire.updateAll();

    CircleStrike aoe = Sierra.aoe.getAttackInstance();
    if(aoe != null && aoe.playing()){
      aoe.update();
    }

    WaterBarrier barr = Caspian.support.getAttackInstance();
    if(barr != null && barr.playing()){
      barr.update();
    }

    //DEBUG: Manual-level up
    if (KeyHandler.controlDown() && KeyHandler.keyClick(Key.PLUS))
    {
      levelUp();
    }

    //DEBUG Stat Dump
    if (KeyHandler.controlDown() && KeyHandler.keyClick(Key.S))
    {
      dumpStats();
    }

    if (KeyHandler.keyClick(Key.A))
    {
      isAttacking = false;
      switchAttack(false);
    } else if (KeyHandler.keyClick(Key.S))
    {
      isAttacking = false;
      switchAttack(true);
    }
  }

  private void resumeAnimationIfStopped()
  {
    if (sprites.isCurrentStopped())
    {
      sprites.resumeCurrent();
    }
  }


  //TODO Use inheritance with this, since doing this with static variables is a drag
  public abstract void levelUp();

  private void dumpStats() {
    for (Characters c: Characters.values()){
      System.out.println(c + ": ");
      System.out.println("Att: " + Player.getAttack(c) + 
          "\tDef: " + Player.getDefense(c));
      System.out.println("Mag: " + Player.getMaxMagic(c)+ 
          "\tHth: " + Player.getMaxHealth(c));
      System.out.println("XP: " + Player.getXP(c) + " / " + 
          Player.getXPToNextLevel(c) + "\tLevel " + Player.getLevel(c) + "\n");
    }
  }

  /**
   * @see komorebi.projsoul.engine.Renderable#render()
   */
  @Override
  public void render() {
    if(!deathStuff){
      if (!invincible)
      {
        if (!isAttacking) {
          playWalk();
        } else
        {
          renderAttack();
        }
      } else
      {
        hurtSprites.renderCurrent(x, y);
      }

      ProjectileAttack.play();
      RingOfFire.play();

      CircleStrike aoe = Sierra.aoe.getAttackInstance();
      if(aoe != null && aoe.playing()){
        aoe.play();
      }

      WaterBarrier barr = Caspian.support.getAttackInstance();
      if(barr != null && barr.playing()){
        barr.play();
      }
    }
    else
    {
      characterDeathAni.playCam(Map.getPlayer().getX(),Map.getPlayer().getY());
      //This still bombs it out, possible bug with frames? But why collision?
      //Because it returns true, it tries to switch characters outside the bounds of the map.
      //If I put a stop before it checks, no crash but the character still flies off the screen.
      //characterDeathAni.stop();
      //if(characterDeathAni.lastFrame())
      {
        refreshInventory();
        for(CharacterItem item: items)
        {
          if(item instanceof Armor)
          {
            Armor a = (Armor)item;
            if(a.equipped)
            {
              if(a.getEquippedCharacter() == MapHandler.currentPlayer())a.unequip();
            }
          }

        }
        MapHandler.switchPlayer();
        deathStuff = false;  
      }

    }

  }



  public void align(Face dir, NPC npc)
  {
    waiting = ThreadHandler.currentThread();
    hasInstructions=true;

    Rectangle r = npc.getSurroundingRectangle(dir);

    goToPixX(r.x);
    goToPixY(r.y);

  }

  public void align(NPC npc)
  {
    waiting = ThreadHandler.currentThread();

    Rectangle r = npc.intersectedHitbox(area);

    goToPixX(r.x);
    goToPixY(r.y);

    dir = npc.faceMe(area);
    turn(dir);
  }

  public void lock(){
    unlocked=false;
  }

  public void unlock(){
    unlocked=true;
  }

  public int getTileX(){
    return  (int) (x/16);
  }

  public int getTileY(){
    return  (int) (y/16);
  }

  public Face getDirection(){
    return dir;
  }



  public void stop()
  {
    dx=0;
    dy=0;
  }

  public Rectangle getArea()
  {
    return area;
  }

  public boolean[] blockedByNpc()
  {
    boolean[] get = new boolean[2];

    future.x += dx;

    for (NPC npc: MapHandler.getActiveMap().getNPCs())
    {
      if (npc.getArea().intersects(future))
      {
        get[0] = true;
      }
    }

    future.x -= dx;
    future.y += dy;

    for (NPC npc: MapHandler.getActiveMap().getNPCs())
    {
      if (npc.getArea().intersects(future))
      {
        get[1] = true;
      }
    }

    future.y -= dy;

    return get;

  }

  public boolean[] blockedByEnemy()
  {
    boolean[] get = new boolean[2];

    future.x += dx;

    for (Enemy enemy: MapHandler.getEnemies())
    {
      if (enemy.getHitBox().intersects(future))
      {
        get[0] = true;
      }
    }

    future.x -= dx;
    future.y += dy;

    for (Enemy enemy: MapHandler.getEnemies())
    {
      if (enemy.getHitBox().intersects(future))
      {
        get[1] = true;
      }
    }

    future.y -= dy;

    return get;

  }

  public void guide(int dx, int dy)
  {    
    this.dx = dx;
    this.dy = dy;
    guiding = true;
  }

  public boolean isAttacking()
  {
    return isAttacking;
  }



  public void setDelta(float dx, float dy)
  {
    this.dx = dx;
    this.dy = dy;
  }

  public void overrideImproperMovements()
  {
    if(!MapHandler.isOutside()){
      if (x + dx < 0) {
        x = 0;
        dx = 0;
      } else if (x + dx > MapHandler.getActiveMap().getTileWidth() * 16 - sx) {
        dx = 0;
        x = MapHandler.getActiveMap().getTileHeight() * 16 - sx;
      }

      if (y + dy < 0) {
        dy = 0;
        y = 0;
      } else if (y + dy > MapHandler.getActiveMap().getTileHeight() * 16 - sy) {
        dy = 0;
        y = MapHandler.getActiveMap().getTileHeight() * 16 - sy;
      }
    }

    for (FireRingInstance ring: RingOfFire.allInstances())
    {
      if (ring.intersectsCirc(new Rectangle((int)(x+dx),(int)(y+dy),sx,sy)))
      {
        float[] center = ring.getCenter();
        double ang = Arithmetic.angleOf(x, y, center[0], center[1]);

        if (ring.inRing(new Rectangle((int)(x+dx),(int)(y+dy),sx,sy)))
        {
          ang -= 180;
        }
        float chgx = (float) Math.cos(ang * (Math.PI/180)) * 5;
        float chgy = (float) Math.sin(ang * (Math.PI/180)) * 5;

        if (this instanceof Flannery) {
          inflictPain(0, chgx, chgy, Enemy.emyProperty);
        } else {
          inflictPain(ring.getDamage(), chgx, chgy, Enemy.emyProperty);
        }
      }
    }
  }

  public Rectangle getHitBox()
  {
    return area;
  }

  //modified inflictPain method that takes in an attack's effectiveness to determine damage
  public void inflictPain(int attack, float dx, float dy, ElementalProperty emyProperty)
  {
    invincible = true;
    restoreMvmtX = false;
    restoreMvmtY = false;

    hurtCount = INVINCIBILITY;

    hurtSprites.turn(dir);
    hurtSprites.resumeCurrent();

    //DEBUG Velocity
    System.out.format("Velocity = %f, %f\n", dx, dy);

    attack = (int) (attack*charProperty.findEffectiveness(emyProperty,charProperty)+1);

    if (attack - getDefense(character)/2 > 0)
    {
      health.health -= (int) (attack - (getDefense(character)/2));
    }else if(attack < 0){
      health.health--;
    }

    refreshInventory();
    for(CharacterItem item: items)
    {
      if(item instanceof Armor)
      {
        Armor a = (Armor)item;
        if(a.equipped)
        {
          if(a.getEquippedCharacter() == MapHandler.currentPlayer())a.damageArmor(11);
        }
      }

    }

    //Kills the player
    if (health.health <= 0)
    {
      deathAni.resume();
      dying = true;

      //DEBUG Kill teh player
      System.out.println(character.getNameFormatted() + " ded");
    }

    this.dx = dx;
    this.dy = dy;
  }

  public boolean invincible()
  {
    return invincible;
  }

  public Characters getCharacter()
  {
    return character;
  }

  public void setLocation(float x, float y)
  {
    this.x = x;
    this.y = y;
  }

  public void renderHUD()
  {
    health.render();
  }

  public abstract void renderAttack();

  public static int getAttack(Characters c)
  {
    switch (c)
    {
      case CASPIAN:
        return Caspian.attack;
      case FLANNERY:
        return Flannery.attack;
      case SIERRA:
        return Sierra.attack;
      case BRUNO:
        return Bruno.attack;
      default:
        break;
    }

    return 0;
  }

  public static int getDefense(Characters c)
  {
    switch (c)
    {
      case CASPIAN:
        return Caspian.defense;
      case FLANNERY:
        return Flannery.defense;
      case SIERRA:
        return Sierra.defense;
      case BRUNO:
        return Bruno.defense;
      default:
        break;
    }

    return 0;
  }

  public static int getMaxMagic(Characters c)
  {
    switch (c)
    {
      case CASPIAN:
        return Caspian.maxMagic;
      case FLANNERY:
        return Flannery.maxMagic;
      case SIERRA:
        return Sierra.maxMagic;
      case BRUNO:
        return Bruno.maxMagic;
      default:
        break;
    }

    return 0;  
  }

  public static int getMaxHealth(Characters c)
  {
    switch (c)
    {
      case CASPIAN:
        return Caspian.maxHealth;
      case FLANNERY:
        return Flannery.maxHealth;
      case SIERRA:
        return Sierra.maxHealth;
      case BRUNO:
        return Bruno.maxHealth;
      default:
        break;
    }

    return 0;  
  }

  public static int getXP(Characters c)
  {
    switch (c)
    {
      case CASPIAN:
        return Caspian.xp;
      case FLANNERY:
        return Flannery.xp;
      case SIERRA:
        return Sierra.xp;
      case BRUNO:
        return Bruno.xp;
      default:
        break;
    }

    return 0;  
  }

  public static int getXPToNextLevel(Characters c)
  {
    switch (c)
    {
      case CASPIAN:
        return Caspian.nextLevelUp;
      case FLANNERY:
        return Flannery.nextLevelUp;
      case SIERRA:
        return Sierra.nextLevelUp;
      case BRUNO:
        return Bruno.nextLevelUp;
      default:
        break;
    }

    return 0;  
  }

  public static int getLevel(Characters c)
  {
    switch (c)
    {
      case CASPIAN:
        return Caspian.level;
      case FLANNERY:
        return Flannery.level;
      case SIERRA:
        return Sierra.level;
      case BRUNO:
        return Bruno.level;
      default:
        break;
    }

    return 0;  
  }

  public abstract void giveXP(int xp);

  public void switchAttack(boolean fwd)
  {
    Attack<? extends AttackInstance> temp = attack1;

    if (fwd)
    {
      attack1 = attack2;
      attack2 = attack3;
      attack3 = temp;
    } else
    {
      attack1 = attack3;
      attack3 = attack2;
      attack2 = temp;
    }
  }

  public void playWalk()
  {
    sprites.renderCurrent(x, y);
  }

  public int getHealth()
  {
    return health.getHealth();
  }

  public int getRequiredExp(int level){
    return (int)(level*level + 10*level + 10);
  }


  public boolean canMove(float dx, float dy)
  {
    future = new Rectangle((int) (x + dx), (int) (y + dy), 
        sx, sy);

    return !MapHandler.getActiveMap().willIntersectNPCs(future);
  }

  public boolean isLocked()
  {
    return !unlocked;
  }

  public void refreshInventory()
  {
    count = 0;
    items = new CharacterItem[Inventory.numOfItems];
    for(CharacterItem item: Inventory.items)
    {
      items[count] = item;
      count++;
    }   
  }
  
  public HUD getCharacterHUD(Characters c)
  {
    return health;  
  }

}