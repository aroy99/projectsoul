package komorebi.projsoul.gameplay;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Renderable;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.TextHandler;

public class MagicBar implements Renderable {

  int magic;
  int maxMagic;
  double proportion;
  
  TextHandler text;

  int baseMagic = 100;

  static final int BAR_WIDTH = 100;

  /**
   * Creates a new magic bar at maximum capacity
   * @param magic The maximum amount of magic
   */
  public MagicBar(int magic)
  {
    this.magic = magic;
    this.maxMagic = magic;
    
    text = new TextHandler();
  }

  public void update()
  {
    proportion = (double) this.magic/maxMagic;
    
    text.clear();
    text.write(String.valueOf(magic), 127, 207, new EarthboundFont(1));
  }

  public void render()
  {
    Draw.rect(125, 205, (int) (proportion*BAR_WIDTH), 10, 41, 22, 42, 23, 2);

    Draw.rect(125, 204, baseMagic+2, 2, 39, 46, 40, 47, 2);
    Draw.rect(125, 215, baseMagic+2, 2, 39, 46, 40, 47, 2);
    Draw.rect(125, 205, 2, 10, 39, 46, 40, 47, 2);
    Draw.rect(baseMagic+125, 205, 2, 10, 39, 46, 40, 47, 2);
    
    text.render();
  }

  /**
   * Adjusts the amount of magic in left in the magic bar
   * @param dMagic The change in magic (- to decrease magic)
   */
  public void changeMagicBy(int dMagic)
  {
    magic += dMagic;

    if (magic<0) magic = 0;
  }

  /**
   * @param magicNeeded The amount of magic needed to perform an attack/task
   * @return Whether the magic bar currently has enough magic to perform that
   * attack/task
   */
  public boolean hasEnoughMagic(int magicNeeded)
  {
    //TODO Make magic limited again
    return true;
    //return magic>=magicNeeded;
  }
  
  public void addToMaxMagic(int add)
  {
    maxMagic += add;
    magic += add;
  }
}
