/**
 * Clyde.java       May 15, 2016, 11:58:06 PM
 */
package komorebi.projsoul.entities;

import java.awt.Rectangle;

import org.lwjgl.input.Keyboard;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Camera;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.script.Execution;
import komorebi.projsoul.script.Lock;
import komorebi.projsoul.states.Game;

/**
 * @author Aaron Roy
 * @author Andrew Faulkenberry
 */
public class Clyde extends Entity implements Playable{

  private boolean up;
  private boolean down;
  private boolean left;
  private boolean right;
  private boolean run;
  private boolean pause;
  private boolean guiding;

  private boolean canMove = true;

  private float dx;
  private float dy;

  private int framesToGo;
  private boolean hasInstructions;

  private Animation upAni;
  private Animation downAni;
  private Animation leftAni;
  private Animation rightAni;

  private static final float SPEED = 1;

  private Face dir = Face.DOWN;    
  private Execution ex;

  private Lock lock;
  
  public Rectangle future;

  /**
   * @param x x pos, from left
   * @param y y pos from bottom
   */
  public Clyde(float x, float y) {
    super(x, y, 16, 24);
    ent = Entities.CLYDE;

    area = new Rectangle((int) x, (int) y, 16, 24);
    future = new Rectangle((int) x, (int) y, 16, 24);

    upAni =    new Animation(4, 8, 16, 24, 0);
    downAni =  new Animation(4, 8, 16, 24, 0);
    leftAni =  new Animation(4, 8, 16, 24, 0);
    rightAni = new Animation(4, 8, 16, 24, 0);

    downAni.add(16,24);
    downAni.add( 0,24);
    downAni.add(16,24);
    downAni.add(32,24);

    upAni.add(16, 0);
    upAni.add( 0, 0);
    upAni.add(16, 0);
    upAni.add(32, 0);

    leftAni.add(48, 0);
    leftAni.add(48,24);
    leftAni.add(48, 0);
    leftAni.add(0, 48, 3);

    rightAni.add(48, 0, true);
    rightAni.add(48,24, true);
    rightAni.add(48, 0, true);
    rightAni.add(0, 48, 3, true);

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

      run = Keyboard.isKeyDown(Keyboard.KEY_X);
    }

  }

  /**
   * @see komorebi.projsoul.engine.Renderable#update()
   */
  @Override
  public void update() {
    
    int aniSpeed = 8;

    if (canMove) {
      
      if (!guiding)
      {
        if(left){
          dx = -SPEED;
          dir = Face.LEFT;
          leftAni.resume();
        }
        if(right){
          dx = SPEED;
          dir = Face.RIGHT;
          rightAni.resume();
        }
        if(!(left || right)){
          dx = 0;
          leftAni.hStop();
          rightAni.hStop();
        }

        if(down){
          dy = -SPEED;
          dir = Face.DOWN;
          downAni.resume();
        }
        if(up){
          dy = SPEED;
          dir = Face.UP;
          upAni.resume();
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
      
      if (blockedByNpc()[0])
      {
        dx = 0;
      } 
      
      if (blockedByNpc()[1])
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

      upAni.setSpeed(aniSpeed);
      downAni.setSpeed(aniSpeed);
      leftAni.setSpeed(aniSpeed);
      rightAni.setSpeed(aniSpeed);
      
      //TODO Debug
      if(!KeyHandler.keyDown(Key.G)){
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

  }

  /**
   * @see komorebi.projsoul.engine.Renderable#render()
   */
  @Override
  public void render() {
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
    
    Draw.rectCam(area.x, area.y, area.width, area.height, 
        220, 4, 221, 5, 6);
   
    
  
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
    return  (int)rx/16;
  }

  public int getTileY(){
    return  (int)ry/16;
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
  
  public void guide(int dx, int dy)
  {
    this.dx = dx;
    this.dy = dy;
    guiding = true;
  }



}