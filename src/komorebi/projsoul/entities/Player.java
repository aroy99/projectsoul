/**
 * Clyde.java       May 15, 2016, 11:58:06 PM
 */
package komorebi.projsoul.entities;

import java.awt.Rectangle;

import org.lwjgl.input.Keyboard;

import komorebi.projsoul.attack.MeleeAttack;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Camera;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MagicBar;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.script.Execution;
import komorebi.projsoul.script.Lock;
import komorebi.projsoul.states.Game;
import komorebi.projsoul.engine.HUD;


/**
 * @author Aaron Roy
 * @author Andrew Faulkenberry
 */
public class Player extends Entity implements Playable{

  private boolean up;
  private boolean down;
  private boolean left;
  private boolean right;
  private boolean run;
  private boolean pause;
  private boolean guiding;
 
  
  //private int health;

  private boolean isAttacking;
  public static boolean isHit, wasHit;
  private boolean dying;
  private boolean dead; 
  private int hitCounter;
  
  private boolean canMove = true;

  private float dx;
  private float dy;

  private int framesToGo;
  private boolean hasInstructions;

  private Animation upAni;
  private Animation downAni;
  private Animation leftAni;
  private Animation rightAni;

  private Animation hurtLeftAni;
  private Animation hurtRightAni;
  private Animation hurtUpAni;
  private Animation hurtDownAni;
  private int hurtCount;

  private Rectangle area;
  private boolean invincible, restoreMvmtX, restoreMvmtY;

  private static final float SPEED = 1;

  private Face dir = Face.DOWN;    
  private Execution ex;

  private Lock lock;

  public Rectangle future;

  private MeleeAttack melee;
  
  private MagicBar magic;
  private Animation hitAni;
  private Animation deathAni;
  private Face hitDirection;

  /**
   * @param x x pos, from left
   * @param y y pos from bottom
   */
  public Player(float x, float y) {
    super(x, y, 16, 24);
    ent = Entities.CLYDE;
    
    hitAni = new Animation(2,8,16,21,11);
    hitAni.add(0, 0);
    hitAni.add(0, 22);

    deathAni = new Animation(4,8,16,21,11,false);
    deathAni.add(0, 57);
    deathAni.add(0, 82);
    deathAni.add(0, 103);
    deathAni.add(0, 124);
    
    restoreMvmtX = true;
    restoreMvmtY = true;

    area = new Rectangle((int) x, (int) y, 16, 24);
    future = new Rectangle((int) x, (int) y, 16, 24);

    upAni =    new Animation(6, 8, 11);
    downAni =  new Animation(6, 8, 11);
    leftAni =  new Animation(6, 8, 11);
    rightAni = new Animation(6, 8, 11);

    hurtUpAni = new Animation(2,8,16,35,11);
    hurtDownAni = new Animation(2,8,16,34,11);
    hurtRightAni = new Animation(2,8,14,33,11);
    hurtLeftAni = new Animation(2,8,14,33,11);

    downAni.add(8,162,16,34);
    downAni.add(28,164,17,32);
    downAni.add(49,161,18,35);
    downAni.add(71,162,16,34);
    downAni.add(91,164,17,32);
    downAni.add(112,161,18,35);

    upAni.add(8,204,16,35);
    upAni.add(28,207,18,32);
    upAni.add(50,206,18,33);
    upAni.add(71,204,16,35);
    upAni.add(91,207,18,32);
    upAni.add(113,206,18,33);

    rightAni.add(3,247,21,32);
    rightAni.add(30,246,14,33);
    rightAni.add(52,245,14,34);
    rightAni.add(72,247,22,32);
    rightAni.add(99,246,14,33);
    rightAni.add(120,245,15,34);

    leftAni.add(3,247,21,32,0,true);
    leftAni.add(30,246,14,33,0,true);
    leftAni.add(52,245,14,34,0,true);
    leftAni.add(72,247,22,32,0,true);
    leftAni.add(99,246,14,33,0,true);
    leftAni.add(120,245,15,34,0,true);

    hurtUpAni.add(8,204);
    hurtUpAni.add(141, 205);

    hurtDownAni.add(8, 162);
    hurtDownAni.add(141, 163);

    hurtRightAni.add(30, 246);
    hurtRightAni.add(141, 246);

    hurtLeftAni.add(30, 246, true);
    hurtLeftAni.add(141, 246, true);


    melee = new MeleeAttack();
    magic = new MagicBar(200);
    

  }

  /**
   * @see komorebi.projsoul.engine.Playable#update()
   */
  public void getInput(){

    if (canMove)
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

    int aniSpeed = 8;

    if (canMove) {

      if (!guiding && restoreMvmtX && restoreMvmtY)
      {
        if(left){
          dx = -SPEED;

          if (!isAttacking)
          {
            dir = Face.LEFT;
            leftAni.resume();
          }
        }
        if(right){
          dx = SPEED;
          if (!isAttacking)
          {
            dir = Face.RIGHT;
            rightAni.resume();
          }
        }
        if(!(left || right)){
          dx = 0;
          leftAni.hStop();
          rightAni.hStop();
        }

        if(down){
          dy = -SPEED;
          if (!isAttacking)
          {
            dir = Face.DOWN;
            downAni.resume();
          }
        }
        if(up){
          dy = SPEED;
          if (!isAttacking)
          {
            dir = Face.UP;
            upAni.resume();
          }
        }

        if(!(up || down)){
          dy = 0;
          downAni.hStop();
          upAni.hStop();
        }

        if(run){
          dx *=2;
          dy *=2;
          aniSpeed /=2;
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

      /*
      if((up && (left || right)) || (down && (left || right))){
        dx *= Math.sqrt(2)/2;
        dy *= Math.sqrt(2)/2;
        speed = (int)Math.round(speed / (Math.sqrt(2)/2));
      }
       */

      if (isAttacking && !melee.playing())
      {
        isAttacking = false;
      }
      
      if (KeyHandler.keyClick(Key.X) && !isAttacking && magic.hasEnoughMagic(29))
      {        
        upAni.hStop();
        downAni.hStop();
        leftAni.hStop();
        rightAni.hStop();

        isAttacking = true;
        melee.newAttack(dir);
        
        magic.changeMagicBy(-10);
      }

      if (!isAttacking)
      {
        melee.setDirection(dir);
      } 

      melee.update((int) x, (int) y);

      upAni.setSpeed(aniSpeed);
      downAni.setSpeed(aniSpeed);
      leftAni.setSpeed(aniSpeed);
      rightAni.setSpeed(aniSpeed);

      if (invincible)
      {
        hurtCount--;

        if (!restoreMvmtX)
        {
          if (Math.abs(dx)<=0.5 && Math.abs(dx)>=0)
          {
            dx = 0;
            restoreMvmtX = true;
          }
          if (dx>0) dx-=0.5;
          if (dx<0) dx+=0.5;
        }
        
        if (!restoreMvmtY)
        {
          if (Math.abs(dy)<=0.5 && Math.abs(dy)>=0)
          {
            dy = 0;
            restoreMvmtY = true;
          }
          if (dy>0) dy-=0.5;
          if (dy<0) dy+=0.5;
        }
       
        if (hurtCount<=0)
        {
          invincible = false;

          switch (dir)
          {
            case DOWN:
              hurtDownAni.hStop();
              downAni.resume();
              break;
            case LEFT:
              hurtLeftAni.hStop();
              leftAni.resume();
              break;
            case RIGHT:
              hurtRightAni.hStop();
              rightAni.resume();
              break;
            case UP:
              hurtUpAni.hStop();
              upAni.resume();
              break;
            default:
              break;

          }
        }
      }

      //TODO Debug
      if(!KeyHandler.keyDown(Key.G)){
        Game.getMap().guidePlayer(x, y, dx, dy);
        boolean[] col = Game.getMap().checkCollisions(x,y,dx,dy);

        if(!col[0] || !col[2]){
          dy=0;
          dx*=.75f;
        }
        if(!col[1] || !col[3]){
          dx=0;
          dy*=.75f;
        }
      }

      if(KeyHandler.keyClick(Key.R)){
        x = 100;
        y = 100;
        Camera.center(x, y);
      }

    }else {
      upAni.hStop();
      downAni.hStop();
      leftAni.hStop();
      rightAni.hStop();
    }

    Camera.move(dx, dy);
    x += dx;
    y += dy;

    area.x += dx;
    area.y += dy;

    if (hasInstructions)
    {
      if (dx!=0) 
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

    //TODO Debug
    if(KeyHandler.keyClick(Key.L)){
      System.out.println("x: "+x+", y: "+y);
    }

    if (hasInstructions&&framesToGo<=0)
    {
      hasInstructions=false;
      dx=0;
      dy=0;
      left = false;
      right = false;
      down = false;
      up = false;
      lock.resumeThread();
    }

    area.x = (int) x;
    area.y = (int) y;

    future.x = (int) x;
    future.y = (int) y;

    guiding = false;
    
    magic.update();
    
    if (dying && deathAni.lastFrame())
    {
      dead = true;
    } else if (dying)
    {
      dx = 0;
      dy = 0;
    }

    /*if (invincible)
    {
      hitCounter--;
      if (dx>0) dx--;
      if (dx<0) dx++;
      if (dy>0) dy--;
      if (dy<0) dy++;
    }

    if (hitCounter<=0)
    {
      hitAni.hStop();
      invincible = false;
    }*/

    if (isHit && !wasHit && !invincible)
    {
      HUD.health-=25;

      //Kills the enemy
      if (HUD.health<=0)
      {
        deathAni.resume();
        dying = true;
      } /*else
      {
        //Knocks back the enemy
        invincible = true;
        hitCounter = 50;
        hitAni.resume();

        switch (hitDirection)
        {
          case DOWN:
            dx = 0;
            dy = -5;
            //break;
          case LEFT:
            dx = -5;
            dy = 0;
            //break;
          case RIGHT:
            dx = 5;
            dy = 0;
            //break;
          case UP:
            dx = 0;
            dy = 5;
            //break;
          default:
            //break;

        }
      }*/
    }

    
  }

  /**
   * @see komorebi.projsoul.engine.Renderable#render()
   */
  @Override
  public void render() {

    if (!invincible)
    {
      if (isAttacking)
      {
        melee.play(x, y);
      } else
      {
        switch (dir) {
          case DOWN:
            downAni.playCam(x,y);
            break;
          case UP:
            upAni.playCam(x,y);
            break;
          case LEFT:
            leftAni.playCam(x,y);
            break;
          case RIGHT:
            rightAni.playCam(x,y);
            break;
          default:
            break;
        }
      }
    } else
    {
      switch (dir)
      {
        case DOWN:
          hurtDownAni.playCam(x, y);
          break;
        case LEFT:
          hurtLeftAni.playCam(x, y);
          break;
        case RIGHT:
          hurtRightAni.playCam(x, y);
          break;
        case UP:
          hurtUpAni.playCam(x, y);
          break;
        default:
          break;

      }
    }
    
  }

  public void pause(int frames, Lock lock)
  {
    framesToGo = frames;

    pause = true;
    hasInstructions = true;

    this.lock = lock;
    this.lock.pauseThread();
  }

  public void walk(Face dir, int tiles)
  {

    hasInstructions=true;
    framesToGo = tiles*16;
    //isMoving=true;
    //isRunning=false;
    this.dir = dir;

    switch (dir)
    {
      case DOWN:
        down = true;
        break;
      case LEFT:
        left = true;
        break;
      case RIGHT:
        right = true;
        break;
      case UP:
        up = true;
        break;
      default:
        break;
    }

  }

  public void walk(Face dir, int tiles, Execution ex)
  {
    walk(dir, tiles);
    this.ex = ex;
    this.ex.getLock().pauseThread();    
  }

  public void walk(Face dir, int tiles, Lock lock)
  {
    this.lock = lock;
    walk(dir,tiles);
    this.lock.pauseThread();
  }

  public void align(Face dir, Lock lock)
  {
    this.lock = lock;
    hasInstructions=true;

    this.dir = dir;

    switch (dir)
    {
      case DOWN:
        framesToGo = (int) this.ry - 16*getTileY();
        down = true;
        break;
      case LEFT:
        framesToGo = (int) this.rx - 16*getTileX();
        left = true;
        break;
      case RIGHT:
        framesToGo = (int) (16*getTileX() + 16 - this.rx);
        right = true;
        break;
      case UP:
        framesToGo = (int) (16*getTileY() + 16 - this.ry);
        up = true;
        break;
      default:
        break;
    }

    this.lock.pauseThread();
  }

  public void align(NPC npc, Lock lock)
  {
    Rectangle r = npc.intersectedHitbox(area);
    goToPixX(r.x, lock);
    goToPixY(r.y, lock);
    dir = npc.faceMe(area);
  }

  public void goToPixX(int goTo, Lock lock)
  {
    int distance = goTo - (int) x;
    framesToGo = Math.abs(distance);
    hasInstructions = true;

    if (distance<0)
    {
      left = true;
      dir = Face.LEFT;
      dx = -1;
    } else if (distance>0)
    {
      right = true;
      dir = Face.RIGHT;
      dx = 1;
    }

    this.lock = lock;
    lock.pauseThread();
  }

  public void goToPixY(int goTo, Lock lock)
  {

    int distance = goTo - (int) y;
    framesToGo = Math.abs(distance);
    hasInstructions = true;

    if (distance<0)
    {
      down = true;
      dir = Face.DOWN;
      dy = -1;
    } else if (distance>0)
    {
      up = true;
      dir = Face.UP;
      dy = 1;
    }

    this.lock = lock;
    lock.pauseThread();
  }

  public void turn(Face dir)
  {
    this.dir = dir;
  }

  public void lock(){
    canMove=false;
  }

  public void unlock(){
    canMove=true;
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

  public void goTo(boolean horizontal, int tx, Lock lock)
  {
    System.out.println(tx*16 + ", x = " + x);


    if (horizontal)
    {
      if (x>tx*16)
      {
        align(Face.LEFT, lock);
        walk(Face.LEFT, getTileX()-tx);
      } else if (rx<tx*16)
      {
        align(Face.RIGHT, lock);
        walk(Face.RIGHT, tx-getTileX(), lock);
      }
    } else
    {
      if (y>tx*16)
      {
        align(Face.DOWN, lock);
        walk(Face.DOWN, getTileY()-tx, lock);
      } else if (ry<tx*16)
      {
        align(Face.UP, lock);
        walk(Face.UP, tx-getTileY(), lock);
      }
    }

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

    for (NPC npc: Game.getMap().getNPCs())
    {
      if (npc.getArea().intersects(future))
      {
        get[0] = true;
      }
    }

    future.x -= dx;
    future.y += dy;

    for (NPC npc: Game.getMap().getNPCs())
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

    for (Enemy enemy: Game.getMap().getEnemies())
    {
      if (enemy.getHitBox().intersects(future))
      {
        get[0] = true;
      }
    }

    future.x -= dx;
    future.y += dy;

    for (Enemy enemy: Game.getMap().getEnemies())
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

  public Rectangle getAttackHitBox()
  {
    return melee.getHitBox();
  }


  public void setDelta(float dx, float dy)
  {
    this.dx = dx;
    this.dy = dy;
  }

  public void overrideImproperMovements()
  {
    if (x+dx<0)
    {
      x = 0;
      dx = 0;
    } else if (x+dx>Game.getMap().getWidth()*16 - sx)
    {
      dx = 0;
      x = Game.getMap().getHeight() * 16 - sx;
    }

    if (y+dy<0)
    {
      dy = 0;
      y = 0;
    } else if (y+dy>Game.getMap().getHeight()*16 - sy)
    {
      dy = 0;
      y = Game.getMap().getHeight() * 16 - sy;
    }
  }

  public Rectangle getHitBox()
  {
    return area;
  }

  public void inflictPain(float dx, float dy)
  {
    invincible = true;
    restoreMvmtX = false;
    restoreMvmtY = false;
    
    hurtCount = 40;
    
    HUD.health-=25;

    switch (dir)
    {
      case DOWN:
        hurtDownAni.resume();
        break;
      case LEFT:
        hurtLeftAni.resume();
        break;
      case RIGHT:
        hurtRightAni.resume();
        break;
      case UP:
        hurtUpAni.resume();
        break;
      default:
        break;

    }

    this.dx = dx;
    this.dy = dy;
  }

  public boolean invincible()
  {
    return invincible;
  }
  
  public MagicBar magicBar()
  {
    return magic;
  }

}