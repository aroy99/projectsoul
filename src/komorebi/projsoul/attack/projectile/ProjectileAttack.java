package komorebi.projsoul.attack.projectile;

import komorebi.projsoul.attack.Attack;
import komorebi.projsoul.entities.Face;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A character attack that shoots projectiles
 * 
 * @param <T> Has to extend Projectile
 * @author Andrew Faulkenberry
 */
public class ProjectileAttack<T extends Projectile> extends Attack<T> {
  
  private static ArrayList<Projectile> projs = new ArrayList<Projectile>();
   
  public ProjectileAttack(T factory)
  {
    super(factory);
  }
  
  public static void play() {
    for (Projectile proj: projs)
    {
      proj.play();
    }
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
  @Override
  public T newAttack(float x, float y, float dx, float dy, Face dir, int attack) {
    T ins = (T) factory.build(x, y, dx, dy, dir, attack);
    
    projs.add((T)ins);
    
    return ins;
  }
  
  public static <T extends Projectile> void newAttack(T add)
  { 
    projs.add(add);
  }
  
  
  public static boolean empty()
  {
    return projs.isEmpty();
  }

}
