package komorebi.projsoul.attack;

import komorebi.projsoul.entities.Face;
import komorebi.projsoul.attack.ElementalProperty;

public interface AttackInstance {

  public AttackInstance build(float x, float y, float dx, float dy,
      Face dir, int attack);
  
  public void update();
}
