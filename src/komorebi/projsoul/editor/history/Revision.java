package komorebi.projsoul.editor.history;

import java.awt.Rectangle;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.script.TextHandler;

public abstract class Revision {
  
  private static TextHandler text = new TextHandler(Draw.LAYER_MANAGER);
  
  protected Rectangle clickableArea;
  
  public Revision()
  {
    this.clickableArea = new Rectangle(15, 0, 100, 24);
  }
  
  public abstract void undo();
  public abstract void redo();
  public abstract void render();
  
  protected String description;
  
  public String getDescription()
  {
    return description;
  }
  
  public static void showText()
  {
    text.render();
  }
  
  public static TextHandler getTextHandler()
  {
    return text;
  }
  
  public void push(int dy)
  {
    clickableArea.y -= dy;
  }
  
  public int getY()
  {
    return clickableArea.y;
  }
  
  public void setY(int y)
  {
    clickableArea.y = y;
  }
}
