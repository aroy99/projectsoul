package komorebi.projsoul.editor.controls;

import java.awt.Rectangle;

import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.gameplay.Key;

public class ExpandArrow {
  
  private float x, y;
  private Rectangle area;
  private boolean visible;
  private boolean pointDown;
  
  private static final int HEIGHT = 14, WIDTH = 22;
  
  public ExpandArrow(float x, float y)
  {
    this.x = x;
    this.y = y;
    visible = true;
    area = new Rectangle((int) x, (int) y, WIDTH, HEIGHT);
    pointDown = true;
  }
  
  public void update()
  {
    if (KeyHandler.keyClick(Key.LBUTTON) && area.contains(Mode.getFloatMouseX(),
        Mode.getFloatMouseY()))
    {
      fireClickEvent();
    }
  }
  
  public void render()
  {
    if (visible)
    {
      if (pointDown)
      {
        Draw.drawIfInBounds(Draw.LAYER_MANAGER, x, y, 
            WIDTH, HEIGHT, 0, 82, 11, 89, 2);
      } else
      {
        Draw.drawIfInBounds(Draw.LAYER_MANAGER, x+WIDTH, y+HEIGHT, 
            WIDTH, HEIGHT, 0, 82, 11, 89, 2, 2);
      }
    }
  }
  
  public boolean isVisible()
  {
    return visible;
  }
  
  public void setVisible(boolean b)
  {
    visible = b;
  }
  
  private void fireClickEvent()
  {
    pointDown = !pointDown;
    click();
  }
  
  public void click() {};
  
  public void push(float num)
  {
    y-=num;
    area.setLocation((int) x, (int) y);
  }
  
  public boolean pointsDown()
  {
    return pointDown;
  }

}
