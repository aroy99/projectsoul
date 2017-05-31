package komorebi.projsoul.attack;

import komorebi.projsoul.engine.Arithmetic;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.map.Map;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The actual ring of fire attack
 *
 * @author Andrew Faulkenberry
 */
public class RingOfFire extends Attack<FireRingInstance> {

  private static ArrayList<FireRingInstance> rings = new ArrayList<FireRingInstance>();
  
  private float x, y, dx, dy, tarX, tarY;
  private boolean left, right, up, down;
  private boolean aiming;
  
  private static final int CENTER_X = 4;
  private static final int SIZE_X = 7;
  private static final int RANGE = 80;
  private static final float SPEED_X = (float) Math.sqrt(2);
  
  public RingOfFire(FireRingInstance factory) {
    super(factory);
  }

  @Override
  public FireRingInstance newAttack(float x, float y, float dx, float dy, Face dir,
      int attack) {
    FireRingInstance ins = (FireRingInstance) factory.build(this.x, this.y, 
        this.dx, this.dy, dir, attack);
    
    rings.add(ins);
    
    aiming = false;

    return ins;
  }
  
  public void getInput()
  {
    aiming = true;
    
    left = KeyHandler.keyDown(Key.LEFT);
    right = KeyHandler.keyDown(Key.RIGHT);
    up = KeyHandler.keyDown(Key.UP);
    down = KeyHandler.keyDown(Key.DOWN);
  }
  
  public void update()
  {
    dx = 0;
    dy = 0;
    
    if (left && !right && !up && !down)
    {
      dx = -SPEED_X;
    } else if (right && !left && !up && !down)
    {
      dx = SPEED_X;
    } else if (left && !right)
    {
      dx = -1;
    } else if (right && !left)
    {
      dx = 1;
    }
    
    if (up && !down && !right && !left)
    {
      dy = SPEED_X;
    } else if (down && !up && !right && !left)
    {
      dy = -SPEED_X;
    } else if (up && !down)
    {
      dy = 1;
    } else if (down && !up)
    {
      dy = -1;
    }
    
    if (Arithmetic.distanceBetween(x, y, tarX, tarY) > RANGE)
    {
      float[] get = Arithmetic.coordinatesAt(tarX, tarY, RANGE, 
          Arithmetic.angleOf(x, y, tarX, tarY));
      x = get[0];
      y = get[1];
    }
    
    if (Arithmetic.distanceBetween(x+dx,y+dy,tarX,tarY) > RANGE)
    {
      double ang;
      switch (Arithmetic.quadrantOf(x, y, tarX, tarY))
      {
        case 1:
          ang = Arithmetic.angleOf(x, y, tarX, tarY) - 90;

          if (up && !right)
          {
            ang += 180;
            dx = (float) (Math.cos(ang * (Math.PI / 180)) * SPEED_X);
            dy = (float) (Math.sin(ang * (Math.PI / 180)) * SPEED_X);
          } else if (right && !up)
          {
            dx = (float) (Math.cos(ang * (Math.PI / 180)) * SPEED_X);
            dy = (float) (Math.sin(ang * (Math.PI / 180)) * SPEED_X);
          }
          break;
        case 2:
          ang = 90 + Arithmetic.angleOf(x, y, tarX, tarY);
          
          if (up && !left)
          {
            ang-=180;
            dx = (float) (Math.cos(ang * (Math.PI / 180)) * SPEED_X);
            dy = (float) (Math.sin(ang * (Math.PI / 180)) * SPEED_X);
          } else if (left && !up)
          {
            dx = (float) (Math.cos(ang * (Math.PI / 180)) * SPEED_X);
            dy = (float) (Math.sin(ang * (Math.PI / 180)) * SPEED_X);
          } 
          break;
        case 3:
          ang = 90 + Arithmetic.angleOf(x, y, tarX, tarY);
          
          if (down && !left)
          {
            dx = (float) (Math.cos(ang * (Math.PI / 180)) * SPEED_X);
            dy = (float) (Math.sin(ang * (Math.PI / 180)) * SPEED_X);
          } else if (left && !down)
          {
            ang += 180;
            dx = (float) (Math.cos(ang * (Math.PI / 180)) * SPEED_X);
            dy = (float) (Math.sin(ang * (Math.PI / 180)) * SPEED_X);
          }
          break;
        case 4:
          ang = Arithmetic.angleOf(x, y, tarX, tarY) + 90;
          
          if (right && !down)
          {
            dx = (float) (Math.cos(ang * (Math.PI / 180)) * SPEED_X);
            dy = (float) (Math.sin(ang * (Math.PI / 180)) * SPEED_X);
          } else if (down && !right)
          {
            ang -= 180;
            dx = (float) (Math.cos(ang * (Math.PI / 180)) * SPEED_X);
            dy = (float) (Math.sin(ang * (Math.PI / 180)) * SPEED_X);
          }
          break;
        case 0:
          if (up && !down)
          {
            dy = (float) SPEED_X;
            dx = 0;
          } else if (down && !up)
          {
            dy = (float) -SPEED_X;
            dx = 0;
          } else
          {
            dx = 0;
            dy = 0;
          }
          break;
        case -1:
          if (left && !right)
          {
            dx = (float) -SPEED_X;
            dy = 0;
          } else if (right && !left)
          {
            dx = (float) SPEED_X;
            dy = 0;
          } else
          {
            dx = 0;
            dy = 0;
          }
          break;
        default:
          break;
      }
     
    }
            
    x += dx;
    y += dy;
  }
  
  public void render()
  {    
    if (aiming) {
      Draw.rectCam(x-CENTER_X, y-CENTER_X, SIZE_X, SIZE_X, 831, 70, 838, 77, 12);
    }
  }
  
  public static void updateAll()
  {    
    for (FireRingInstance ring: rings)
    {
      ring.update();
    }
    
    for (Iterator<FireRingInstance> it = rings.iterator(); it.hasNext();)
    {
      FireRingInstance ring = it.next();
      if (ring.destroyed())
      {
        it.remove();
      }
    }
  }
  
  public static void play()
  {
    for (FireRingInstance ring: rings)
    {
      ring.render();
    }
  }
  
  public boolean isAiming()
  {
    return aiming;
  }
  
  public void newXMark(float x, float y)
  {
    this.x = x;
    this.y = y;
    this.tarX = x;
    this.tarY = y;
  }
  
  public static boolean ringsExist()
  {
    return !rings.isEmpty();
  }
  
  public static ArrayList<FireRingInstance> allInstances()
  {
    return rings;
  }

}
