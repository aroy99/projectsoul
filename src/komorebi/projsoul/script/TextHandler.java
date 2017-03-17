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
public class TextHandler{
   
  public static final int SCALE = 16;

  public ArrayList<Word> words;
  private static MenuFont defFont = new MenuFont(1);

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
    
    Font font = word.getFont();
    
    int size = font.getFontPoint()*font.getScale();
    char[] letters = word.currentParagraph();
    
    for (int i=0; i < letters.length; i++)
    {

      int under = 0, texUnder = 0;

      under = font.getTexUnder(letters[i])*font.getScale();
      if(under != 0){
        texUnder = font.getTexUnder(letters[i]);
      }


      Draw.rect(horiz, vert-under, size, size+under, 
          font.getTexX(letters[i]), font.getTexY(letters[i]), 
          font.getTexX(letters[i])+font.getFontPoint(), 
          font.getTexY(letters[i]) + font.getFontPoint()+texUnder, 
          font.getTexture());
      horiz+=(font.getLength(letters[i])+
          size/(font.getFontPoint()/font.getScale()));
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
