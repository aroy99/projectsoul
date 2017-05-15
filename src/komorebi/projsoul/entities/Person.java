package komorebi.projsoul.entities;

import komorebi.projsoul.engine.Main;
import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.entities.player.Caspian;
import komorebi.projsoul.entities.sprites.SpriteSet;
import komorebi.projsoul.script.tasks.MovementTask;
import komorebi.projsoul.script.tasks.Task;
import komorebi.projsoul.script.tasks.Task.Precedence;
import komorebi.projsoul.script.tasks.TimedTask;
import komorebi.projsoul.script.tasks.ToDoList;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.SpeechHandler;

public abstract class Person extends Entity {

  public enum ActionState {
    WALKING,
    JOGGING,
    PAUSED,
    NONE;
  }

  private static final int PIXELS_PER_STEP = 16;

  protected SpeechHandler text;

  protected float dx, dy;
  private boolean interrupted;

  protected SpriteSet sprites;

  private ToDoList toDoList;
  protected Face dir;

  public Person(float x, float y, int sx, int sy) {
    super(x, y, sx, sy);

    toDoList = new ToDoList();
  }

  public abstract boolean canMove(float dx, float dy);

  public void update()
  {  
    stopMoving();
    
    if (toDoList.hasTasks())
    {
      workOnToDoList();
      updateAnimation();
      updatePosition();
    }
    
    stopMoving();
  }

  private void updateAnimation()
  {
    if (moving())
      sprites.setAniSpeed(4 / Math.abs((int) movingVelocity()));
    else 
    {
      if (!sprites.isCurrentStopped())
      {
        sprites.stopCurrent();
      }
    }


  }

  protected boolean moving()
  {    
    return dx != 0.0 || dy != 0.0;
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

  public void walk(Face dir)
  {
    Precedence precedence = ThreadHandler.currentThread().precedence();

    TimedTask walk = new MovementTask(ActionState.WALKING, precedence, 
        PIXELS_PER_STEP, dir);

    turn(dir);
    sprites.resumeCurrent();

    toDoList.add(walk);
    ThreadHandler.lockCurrentThread();
  }

  public void jog(Face dir)
  {
    Precedence precedence = ThreadHandler.currentThread().precedence();

    TimedTask jog = new MovementTask(ActionState.JOGGING, precedence, 
        PIXELS_PER_STEP, dir);

    turn(dir);
    sprites.resumeCurrent();

    toDoList.add(jog);
    ThreadHandler.lockCurrentThread();
  }

  public void pause(int pauseFor)
  {    
    Precedence precedence = ThreadHandler.currentThread().precedence();

    TimedTask pause = new TimedTask(ActionState.PAUSED, precedence, pauseFor);
    toDoList.add(pause);

    ThreadHandler.lockCurrentThread();
  }

  public void turn(Face dir)
  {
    sprites.turn(dir);
    this.dir = dir;
  }

  public void render()
  {
    sprites.renderCurrent(x, y);
  }

  public void say(String s)
  {
    text.clear();
    text.write(s, 20, 58, new EarthboundFont(1));
    Main.getGame().setSpeaker(text);
  }

  public void interrupt()
  {
    interrupted = true;
  }

  public void letContinue()
  {
    interrupted = false;
  }

  public void goToPixX(int goTo)
  {
    int distance = goTo - (int) x;

    if (distance == 0)
      return;
    
    Face direction = (distance > 0 ? Face.RIGHT : Face.LEFT);

    TimedTask walk = new MovementTask(ActionState.WALKING, 
        ThreadHandler.currentThread().precedence(),
        Math.abs(distance), direction);

    turn(direction);

    sprites.resumeCurrent();
    toDoList.add(walk);
    ThreadHandler.lockCurrentThread();
  }

  public void goToPixY(int goTo)
  {
    int distance = goTo - (int) y;

    if (distance == 0)
      return;

    Face direction = (distance > 0 ? Face.UP : Face.DOWN);

    TimedTask walk = new MovementTask(ActionState.WALKING, 
        ThreadHandler.currentThread().precedence(),
        Math.abs(distance), direction);

    if (distance<0)
    {
      turn(Face.DOWN);
    } else if (distance>0)
    {
      turn(Face.UP);
    }    

    sprites.resumeCurrent();
    toDoList.add(walk);
    ThreadHandler.lockCurrentThread();

  }
}
