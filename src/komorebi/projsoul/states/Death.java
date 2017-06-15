package komorebi.projsoul.states;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.map.MapHandler;

public class Death extends State
{
  public static boolean playable = true;
  public static boolean death;

  public Death()
  {

  }
  @Override
  public void getInput() 
  {

  }

  @Override
  public void update()
  {
    if (MapHandler.allPlayersDead())
    {
      playable = false;
      death = true;

    }	
  }
  @Override
  public void render()
  {
    Draw.rect((float)0, (float)0, (float)256, (float)244, 0, 0, 256, 244, 0, 13);
  }
}









