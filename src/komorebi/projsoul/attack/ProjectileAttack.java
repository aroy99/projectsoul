package komorebi.projsoul.attack;

import java.util.ArrayList;
import java.util.Iterator;

import komorebi.projsoul.entities.Face;

public class ProjectileAttack<T extends Projectile> extends Attack<T> {
  
  private static ArrayList<Projectile> projs = new ArrayList<Projectile>();
  
  private boolean playing;
 
  public ProjectileAttack(T factory)
  {
    super(factory);
  }

  public void newAttack(T add)
  { 
    projs.add(add);
    playing = true;
  }
  
  public static void play() {
    for (Projectile proj: projs)
    {
      proj.play();
    }
  }
  
  public boolean isActive()
  {
    return playing;
  }
  
  public static void update()
  {
    for (Projectile proj: projs)
    {
      proj.update();
    }
    
    for (Iterator<Projectile> it = projs.iterator(); it.hasNext();)
    {      
      Projectile proj = it.next();
      if (proj.destroyed())
      {
        it.remove();
      }

    }
  }

  @SuppressWarnings("unchecked")
  public void newAttack(float x, float y, float dx, float dy, Face dir,
      int attack) {
      projs.add((T) factory.build(x, y, dx, dy, dir, attack));
  }
  
  
  public static boolean empty()
  {
    return projs.isEmpty();
  }

}
