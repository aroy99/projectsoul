package komorebi.projsoul.editor.controls;

import java.awt.Rectangle;

import komorebi.projsoul.engine.Draw;

public class CheckBox extends Switch {

  private static final int WIDTH = 22, HEIGHT = 18;
  
  public CheckBox(float x, float y)
  {
    this(x,y,false);
  }
  
  public CheckBox(float x, float y, boolean checked)
  {
    this.x = x;
    this.y = y;
    this.checked = checked;
    visible = true;
    
    area = new Rectangle((int) x, (int) y, HEIGHT, WIDTH);
  }
  @Override
  public void renderOff() {
    Draw.drawIfInBounds(Draw.LAYER_MANAGER, x, y, WIDTH, HEIGHT, 40, 106, 51, 115, 2);
    
  }

  @Override
  public void renderOn() {
    Draw.drawIfInBounds(Draw.LAYER_MANAGER, x, y, WIDTH, HEIGHT, 52, 106, 63, 115, 2);
    
  }

}
