package komorebi.projsoul.attack;

import komorebi.projsoul.entities.Face;

public class MeleeAttack<T extends Melee> extends Attack<T> {
    
  T melee;
  
  /**
   * Creates an instance of a close-combat attack with rectangle-based
   * hit detection
   */
  public MeleeAttack(T factory)
  {    
    super(factory);
  }

  /**
   * Indicates whether the current attack animation is currently playing
   * @return true if the attack animation is still playing, false if not
   */
  public boolean playing()
  {
    return melee.playing();
  }

  /**
   * Starts a new attack, begins the animation
   * @param dir The direction the player is facing when he/she starts the attack
   */


  /**
   * Starts a new attack, begins the animation
   * @param dir The direction the player is facing when he/she starts the attack
   */
  public void newAttack(T add)
  {
    melee = add;
  }
  
  /**
   * Switches the current direction of the attack
   * @param dir The new direction of the attack
   */
  public void setDirection(Face dir)
  {
    melee.setDirection(dir);
  }
  
  /**
   * Updates the location of the attack's hitbox based on the current direction
   * of the attack
   * @param x The player's x location
   * @param y The player's y location
   */
  public void update(float x, float y)
  {
      melee.update((int) x, (int) y);
  }
  
  public void update()
  {
    melee.update();
  }
  
  public T getAttackInstance()
  {
    return melee;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void newAttack(float x, float y, float dx, float dy, Face dir,
      int attack) {
      melee = (T) factory.build(x, y, dx, dy, dir, attack);
  }

  
  



}

