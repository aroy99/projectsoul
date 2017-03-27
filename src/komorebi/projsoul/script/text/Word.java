/**
 * Word.java		Aug 21, 2016, 4:28:33 PM
 */
package komorebi.projsoul.script.text;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class Word {
  
  String string;
  char[][] paragraphs;
  int x, y, ptSize;
  
  Font font;
  
  private int currentParagraph;
  
  public Word(String s, int x, int y, Font font)
  {
    string = s;    
    String[] array = s.split("\\\\p");
    
    paragraphs = new char[array.length][];
    
    for (int i = 0; i< array.length; i++)
    {
      paragraphs[i] = array[i].toCharArray();
    }
    
    this.x = x;
    this.y = y;
    
    this.font = font;
  }
  
  public void nextParagraph()
  {
    currentParagraph++;
  }
  
  public char[] currentParagraph()
  {
    return paragraphs[currentParagraph];
  }
  
  public int getX()
  {
    return x;
  }
  
  public int getY()
  {
    return y;
  }
  
  public int getFontSize()
  {
    return ptSize;
  }
  
  public String getString()
  {
    return string;
  }
  
  public void setString(String s)
  {
    string = s;
  }
  
  public boolean hasNext()
  {
    return currentParagraph < paragraphs.length - 1;
  }
  
  public Font getFont()
  {
    return font;
  }
}
