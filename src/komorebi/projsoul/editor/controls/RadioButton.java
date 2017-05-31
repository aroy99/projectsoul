package komorebi.projsoul.editor.controls;

import komorebi.projsoul.engine.Draw;

import java.awt.Rectangle;

public class RadioButton extends Switch {
  
  private static final int WIDTH = 20, HEIGHT = 16;
  private Rectangle drawBounds;
  
  public RadioButton(float x, float y)
  {
    this(x,y,false, Draw.LAYER_MANAGER);
  }
  
  public RadioButton(float x, float y, boolean checked, Rectangle bounds)
  {
    this.x = x;
    this.y = y;
    this.checked = checked;
    visible = true;
    
    area = new Rectangle((int) x, (int) y, WIDTH, HEIGHT);
    drawBounds = bounds;
  }
  
  public RadioButton(float x, float y, boolean checked)
  {
    this(x, y, checked, Draw.LAYER_MANAGER);
  }
  
  public void renderOn()
  {
    Draw.drawIfInBounds(drawBounds, x ,y, WIDTH, HEIGHT, 28, 82, 38, 90, 2);
  }
  
  public void renderOff()
  {
    Draw.drawIfInBounds(drawBounds, x ,y, WIDTH, HEIGHT, 16, 82, 26, 90, 2);
  }
  
  
  
}
