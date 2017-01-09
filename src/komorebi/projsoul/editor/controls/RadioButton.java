package komorebi.projsoul.editor.controls;

import java.awt.Rectangle;

import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.gameplay.Key;

public class RadioButton extends Switch {
  
  private static final int WIDTH = 20, HEIGHT = 16;
  
  public RadioButton(float x, float y)
  {
    this(x,y,false);
  }
  
  public RadioButton(float x, float y, boolean checked)
  {
    this.x = x;
    this.y = y;
    this.checked = checked;
    visible = true;
    
    area = new Rectangle((int) x, (int) y, WIDTH, HEIGHT);
  }
  
  public void renderOn()
  {
    Draw.drawIfInBounds(Draw.LAYER_MANAGER, x ,y, WIDTH, HEIGHT, 28, 82, 38, 90, 2);
  }
  
  public void renderOff()
  {
    Draw.drawIfInBounds(Draw.LAYER_MANAGER, x ,y, WIDTH, HEIGHT, 16, 82, 26, 90, 2);
  }
  
  
  
}
