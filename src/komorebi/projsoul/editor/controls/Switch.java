package komorebi.projsoul.editor.controls;

import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;

import java.awt.Rectangle;

public abstract class Switch {
  
  
  protected float x, y;
  protected boolean checked;
  protected boolean visible;
  protected Rectangle area;

  public void update()
  {
    if (KeyHandler.keyClick(Key.LBUTTON) && area.contains(Mode.getFloatMouseX(),
        Mode.getFloatMouseY()))
    {
      fireClickEvent();
    }
  }
  
  public abstract void renderOff();
  public abstract void renderOn();

 
  public void render()
  {
    if (visible)
    {
      if (checked)
      {
        renderOn();
      } else
      {
        renderOff();
      }
    }
  }
  
  private void fireClickEvent()
  {
    checked = !checked;
    click();
  }
  
  public void click() {}
    
  public boolean isVisible()
  {
    return visible;
  }
  
  public void setVisible(boolean b)
  {
    visible = b;
  }
  
  public boolean isChecked()
  {
    return checked;
  }
  
  public void setChecked(boolean b)
  {
    checked = b;
  }
  
  public void push(float num)
  {
    y-=num;
    area.setLocation((int) x, (int) y);
  }
  
  public void setLocation(int x, int y)
  {
    this.x = x;
    this.y = y;
    
    area.setLocation(x, y);
  }
}
