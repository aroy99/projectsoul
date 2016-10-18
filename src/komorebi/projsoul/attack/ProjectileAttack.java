package komorebi.projsoul.attack;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.entities.Face;

public class ProjectileAttack extends Attack {

  private Rectangle hitBox;
  
  private ArrayList<FireBall> projs = new ArrayList<FireBall>();

  private Characters character;
  
  private boolean playing;
  
  public ProjectileAttack(Characters c)
  {
    switch (c)
    {
      case FLANNERY:
        
        
        break;
      default:
        break;
    }
  }

  public void newAttack(float x, float y, float dx, float dy, Face dir,
      int attack)
  {
    projs.add(new FireBall(x,y,dx,dy,dir,attack));
    
    playing = true;
  }
  
  public void play() {
    for (FireBall fire: projs)
    {
      fire.play();
    }
  }
  
  public boolean isActive()
  {
    return playing;
  }
  
  public void update()
  {
    for (FireBall fire: projs)
    {
      fire.update();
    }
    
    for (Iterator<FireBall> it = projs.iterator(); it.hasNext();)
    {      
      FireBall fire = it.next();
      if (fire.destroyed())
      {
        it.remove();
      }

    }
  }
}
