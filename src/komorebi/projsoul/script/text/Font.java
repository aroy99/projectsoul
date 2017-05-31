package komorebi.projsoul.script.text;

/**
 * Represents a font to be displayed ingame
 *
 * @author Andrew Faulkenberry
 */
public abstract class Font {

  public int scale;
  
  public Font(int scale)
  {
    this.scale = scale;
  }
  
  //TODO Fix class hierarchy and DOCUMENT
  public abstract int getTexX(char c);
  public abstract int getTexY(char c);
  public abstract int getTexUnder(char c);
  public abstract int getLength(char c);
  
  /**
   * @param c1 First character to kern
   * @param c2 Second character to kern
   * @return The space between the characters, default 1
   */
  public abstract int getKerning(char c1, char c2);
  
  public abstract int getFontPoint();
  public abstract int getTexture();
  
  
  public int getScale()
  {
    return scale; 
  }
}
