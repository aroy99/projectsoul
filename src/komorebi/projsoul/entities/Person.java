package komorebi.projsoul.entities;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Main;
import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.script.Lock;
import komorebi.projsoul.script.tasks.MovementTask;
import komorebi.projsoul.script.tasks.Task;
import komorebi.projsoul.script.tasks.Task.Precedence;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.SpeechHandler;
import komorebi.projsoul.script.tasks.TimedTask;
import komorebi.projsoul.script.tasks.ToDoList;

public abstract class Person extends Entity {

  public enum ActionState {
    WALKING,
    JOGGING,
    PAUSED,
    NONE;
  }

  private static final int PIXELS_PER_STEP = 16;

  protected SpeechHandler text;

  private float dx, dy;
  private boolean interrupted;

  protected Animation rightAni, leftAni, downAni, upAni;
  protected Animation currentAni;

  private ToDoList toDoList;

  public Person(float x, float y, int sx, int sy) {
    super(x, y, sx, sy);

    toDoList = new ToDoList();
  }

  public abstract boolean canMove(float dx, float dy);

  public void update()
  {    
    stopMoving();
    workOnToDoList();
    updateAnimation();
    updatePosition();
  }
  
  private void updateAnimation()
  {
    if (moving())
      currentAni.setSpeed(4 / Math.abs((int) movingVelocity()));
    else
      currentAni.hStop();
  }
  
  private boolean moving()
  {
    return dx != 0 || dy != 0;
  }
  
  private float movingVelocity()
  {
    if (dx != 0)
      return dx;
    return dy;
  }
  
  private void updatePosition()
  {
    x+=dx;
    y+=dy;

    area.x += dx;
    area.y += dy;
  }

  private void stopMoving()
  {
    dx = dy = 0;
  }

  private void workOnToDoList()
  {
    if (toDoList.hasTasks() && (!interrupted || 
        toDoList.next().hasHighPrecedence()))
    {    
      Task task;
      TimedTask timed;
      task = toDoList.next();

      if (task instanceof TimedTask)
      {
        timed = (TimedTask) task;
        
        if (task instanceof MovementTask)
        {
          if (canMoveAsSpecifiedByTask((MovementTask) task))
          {
            setVelocities((MovementTask) task);
            timed.decrement();
          }
        } else
        {
          timed.decrement();
        }
      }
    }
    
    toDoList.clean();
  }

  private boolean canMoveAsSpecifiedByTask(MovementTask task)
  {
    float potentialDx = task.getDx();
    float potentialDy = task.getDy();

    return canMove(potentialDx, potentialDy);
  }

  private void setVelocities(MovementTask task)
  {
    dx = task.getDx();
    dy = task.getDy();
  }

  public void walk(Face dir, Lock lock)
  {
    Precedence precedence = ThreadHandler.currentThread().precedence();
    
    TimedTask walk = new MovementTask(ActionState.WALKING, precedence, 
        PIXELS_PER_STEP, dir, lock);
            
    turn(dir);
    currentAni.resume();
    
    toDoList.add(walk);
    walk.lock();
  }

  public void jog(Face dir, Lock lock)
  {
    Precedence precedence = ThreadHandler.currentThread().precedence();
    
    TimedTask jog = new MovementTask(ActionState.JOGGING, precedence, 
        PIXELS_PER_STEP, dir, lock);
        
    turn(dir);
    currentAni.resume();
    
    toDoList.add(jog);
    jog.lock();
  }

  public void pause(int pauseFor, Lock lock)
  {    
    Precedence precedence = ThreadHandler.currentThread().precedence();

    TimedTask pause = new TimedTask(ActionState.PAUSED, precedence, pauseFor, lock);
    toDoList.add(pause);
    
    pause.lock();
  }

  public void turn(Face dir)
  {
    switch (dir)
    {
      case DOWN:
        currentAni = downAni;
        break;
      case LEFT:
        currentAni = leftAni;
        break;
      case RIGHT:
        currentAni = rightAni;
        break;
      case UP:
        currentAni = upAni;
        break;
      default:
        break;
    }
  }

  public void render()
  {
    currentAni.playCam(x, y);
    text.render();

  }

  public void say(String s, Lock lock)
  {
    text.write(s, 20, 58, new EarthboundFont(1));
    Main.getGame().setSpeaker(text);
    text.setAndLock(lock);
  }
  
  public void interrupt()
  {
    interrupted = true;
  }
  
  public void letContinue()
  {
    interrupted = false;
  }
}
