/**
 * SingleAttack.java    Jan 11, 2017, 9:12:24 AM
 */
package komorebi.projsoul.attack;

import komorebi.projsoul.entities.Face;

/**
 * A class that represents an attack that may only have one instance
 *
 * @param <T> Has to be a SingleInstance
 *
 * @author Aaron Roy
 */
public class SingleAttack<T extends SingleInstance> extends Attack<T>{
  T attack;
  
  public SingleAttack(T factory){
    super(factory);
  }
    
  public void update(){
    attack.update();
  }
  
  /**
   * Indicates whether the current attack animation is currently playing
   * @return true if the attack animation is still playing, false if not
   */
  public boolean playing()
  {
    return attack.playing();
  }
  
  public T getAttackInstance()
  {
    return attack;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public T newAttack(float x, float y, float dx, float dy, Face dir,
      int attack){
    T ins = (T) factory.build(x, y, dx, dy, dir, attack);
    this.attack =  ins;
    return ins;
  }

  /**
   * Starts a new attack, begins the animation
   * 
   * @param add The direction the player is facing when he/she starts the attack
   */
  public void newAttack(T add)
  {
    attack = add;
  }
}
