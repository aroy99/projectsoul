/**
 * TextHandler.java  Jun 12, 2016, 1:48:14 PM
 */
package komorebi.projsoul.script;

import java.util.ArrayList;

import komorebi.projsoul.engine.Draw;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class TextHandler {
   
  public static final int SCALE = 16;

  public ArrayList<Word> words;
  private static EarthboundFont defFont = new EarthboundFont(1);

  /**
   * Creates a text handler object which will render words
   */
  public TextHandler()
  {
    words = new ArrayList<Word>();
  }

  /**
   * 
   * @param s
   * @param x
   * @param y
   * @param fontPt
   */
  public void write(String s, int x, int y, Font font)
  {
    words.add(new Word(s, x, y, font));
  }
  
  public void write(String s, int x, int y)
  {
    words.add(new Word(s, x, y, defFont));
  }
  
  public void writeMindLength(String s, int x, int y, Font font, int maxLength)
  {
    if (pixLengthOf(s, font) < maxLength)
    {
      write(s, x, y, font);
    } else {
      int subIndex = s.length()-1;
      
      while (pixLengthOf(s.substring(0, subIndex) + "...", font) > maxLength)
      {
        subIndex--;
      }
      
      write(s.substring(0, subIndex) + "...", x, y, font);
    }
  }
  
  public void writeMindLength(String s, int x, int y, int maxLength)
  {
    writeMindLength(s, x, y, defFont, maxLength);
  }
  
  private int pixLengthOf(String str, Font font)
  {
    int num = 0;
    
    for (int i = 0; i < str.length(); i++)
    {
      num += font.getLength(str.charAt(i)) + 1;
    }
    
    return num;
  }

  /**
   * Renders the text in a style specified by the text handler's attributess
   */
  public void render()
  {

    for (Word word: words)
    {
      render(word);
    }
    
  }

  public void render(Word word)
  {
    int horiz = word.getX();
    int vert = word.getY();
    int size = word.getFont().getFontPoint()*word.getFont().getScale();
    char[] letters = word.currentParagraph();
    
    for (int i=0; i < letters.length; i++)
    {

      int under = 0, texUnder = 0;

      if (letters[i]=='g' || letters[i] == 'j' || letters[i] == 'p' ||
          letters[i] == 'q' || letters[i] == 'y')
      {   
        under = size;
        texUnder = word.getFont().getFontPoint();
      } 


      Draw.rect(horiz, vert-under, size, size+under, 
          word.getFont().getTexX(letters[i]), word.getFont().getTexY(letters[i]), 
          word.getFont().getTexX(letters[i])+word.getFont().getFontPoint(), 
          word.getFont().getTexY(letters[i]) + word.getFont().getFontPoint()+texUnder, 
          word.getFont().getTexture());
      horiz+=(word.getFont().getLength(letters[i])+1);
    }
  }




  /**
   * Clears the text handler's memory
   */
  public void clear()
  {
    words.clear();
  }
  
  public void replace(String erase, String replace)
  {
    for (Word word: words)
    {
      if (word.getString().equals(erase))
      {
        word.setString(replace);
      }
    }
  }
 
  
}
