package komorebi.projsoul.attack;

import komorebi.projsoul.entities.Face;

public interface AttackInstance {

  public AttackInstance build(float x, float y, float dx, float dy,
      Face dir, int attack);
  
  /**
   * Updates this objects variables based on the input and game logic
   */
  public void update();
  
}
