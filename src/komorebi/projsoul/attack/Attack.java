package komorebi.projsoul.attack;

import komorebi.projsoul.entities.Face;

/**
   * Renders the attack animation on-screen
   * @param x The x location at which the attack is occuring
   * @param y The y location at which the attack is occuring
   */

public abstract class Attack<T extends AttackInstance> {
  
  public T factory;
  
  public Attack(T factory)
  {
    this.factory = factory;
  }
  
  public abstract void newAttack(float x, float y, float dx, float dy, 
      Face dir, int attack);
  
}
