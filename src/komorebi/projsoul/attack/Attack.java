package komorebi.projsoul.attack;

import komorebi.projsoul.entities.Face;

public abstract class Attack<T extends AttackInstance> {
  
  public T factory;
  
  public Attack(T factory)
  {
    this.factory = factory;
  }
  
  public abstract void newAttack(float x, float y, float dx, float dy, 
      Face dir, int attack);
 
  
}
