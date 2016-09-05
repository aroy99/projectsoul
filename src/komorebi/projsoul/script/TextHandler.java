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

  /**
   * Creates a text handler object which will render words in a speaker-bubble style
   */
  public TextHandler()
  {
    words = new ArrayList<Word>();
  }


  public static int getTexX(char c)
  {
    switch (c)
    {
      case' ':case',':case'8':case'D':case'P':case'h':case't':
        return 0;
      case'!':case'-':case'9':case'E':case'Q':case'i':case'u':
        return SCALE;
      case'"':case'.':case':':case'F':case'R':case'j':case'v':
        return SCALE*2;
      case'/':case';':case'G':case'S':case'k':case'w':
        return SCALE*3;
      case'$':case'0':case'H':case'T':case'l':case'x':
        return SCALE*4;
      case'%':case'1':case'=':case'I':case'U':case'a':case'm':case'y':
        return SCALE*5;
      case'2':case'J':case'V':case'b':case'n':case'z':
        return SCALE*6;
      case'\'':case'3':case'?':case'K':case'W':case'c':case'o':case'[':
        return SCALE*7;
      case'(':case'4':case'L':case'X':case'd':case'p':
        return SCALE*8;
      case')':case'5':case'A':case'M':case'Y':case'e':case'q':case']':
        return SCALE*9;
      case'*':case'6':case'B':case'N':case'Z':case'f':case'r':case'~':
        return SCALE*10;
      case'+':case'7':case'C':case'O':case'g':case's':
        return SCALE*11;
      default:
        break;
    }

    return 0;
  }

  public static int getTexY(char c)
  {
    switch(c)
    {
      case' ':case'!':case'"':case'$':case'%':case'\'':case'(':case')':case'*':
      case'+':
        return 0;
      case',':case'-':case'.':case'/':case'0':case'1':case'2':case'3':case'4':
      case'5':case'6':case'7':
        return SCALE;
      case'8':case'9':case':':case';':case'=':case'?':case'A':case'B':case'C':
        return SCALE*2;
      case'D':case'E':case'F':case'G':case'H':case'I':case'J':case'K':case'L':
      case'M':case'N':case'O':
        return SCALE*3;
      case'P':case'Q':case'R':case'S':case'T':case'U':case'V':case'W':case'X':
      case'Y':case'Z':
        return SCALE*4;
      case'a':case'b':case'c':case'd':case'e':case'f':case'g':
        return SCALE*5;
      case'h':case'i':case'j':case'k':case'l':case'm':case'n':case'o':case'p':
      case'q':case'r':case's':
        return SCALE*6;
      case'[':case']':case'~':case't':case'u':case'v':case'w':case'x':case'y':
      case'z':
        return SCALE*7;
      default:
        break;
    }

    return 0;
  }

  public static int getTexSy(char c)
  {
    switch(c)
    {
      case' ':case'!':case'"':case'$':case'%':case'\'':case'(':case')':case'*':case'+':
        return 0;
      case',':case'-':case'.':case'/':case'0':case'1':case'2':case'3':case'4':case'5':
      case'6':case'7':
        return SCALE;
      case'8':case'9':case':':case';':case'=':case'?':case'A':case'B':case'C':
        return SCALE*2;
      case'D':case'E':case'F':case'G':case'H':case'I':case'J':case'K':case'L':case'M':
      case'N':case'O':
        return SCALE*3;
      case'P':case'Q':case'R':case'S':case'T':case'U':case'V':case'W':case'X':case'Y':case'Z':
        return SCALE*4;
      case'a':case'b':case'c':case'd':case'e':case'f':case'g':
        return SCALE*5;
      case'h':case'i':case'j':case'k':case'l':case'm':case'n':case'o':case'p':case'q':
      case'r':case's':
        return SCALE*6;
      case'[':case']':case'~':case't':case'u':case'v':case'w':case'x':case'y':case'z':
        return SCALE*7;
      default:
        break;
    }

    return 0;
  }

  public static int getLength(char c)
  {
    switch (c)
    {
      case'!':case'.':case':':case'I':case'i':case'l':
        return 1;
      case'\'':case',':case'-':case';':case'j':case'[':case']':
      case' ':
        return 2;
      case'\"':case'(':case')':case'*':case'f':case'r':case't':
        return 3;
      case'/':case'0':case'1':case'2':case'3':case'5':case'6':case'7':case'8':
      case'9':case'?':case'E':case'F':case'J':case'L':case'Z':case'b':
      case'c':case'd':case'e':case'g':case'h':case'k':case'n':case'o':
      case'p':case'q':case's':case'u':case'x':case'y':case'z':
        return 4;
      case'$':case'+':case'4':case'=':case'B':case'C':case'D':case'G':
      case'H':case'K':case'N':case'O':case'P':case'Q':case'R':case'S':
      case'T':case'U':case'X':case'Y':case'a':case'v':
        return 5;
      case'A':case'V':case'~':
        return 6;
      case'M':case'W':case'm':case'w':
        return 7;
      case'%':
        return 9;
      default:
        return 0;
    }
  }

  /**
   * 
   * @param s
   * @param x
   * @param y
   * @param fontPt
   */
  public void write(String s, int x, int y, int fontPt)
  {
    words.add(new Word(s, x, y, fontPt));
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
    int size = word.getFontSize();
    char[] letters = word.currentParagraph();
    
    for (int i=0; i < letters.length; i++)
    {

      int under = 0, texUnder = 0;

      if (letters[i]=='g' || letters[i] == 'j' || letters[i] == 'p' ||
          letters[i] == 'q' || letters[i] == 'y')
      {   
        under = size;
        texUnder = 8;
      } 


      Draw.rect(horiz, vert-under, size, size+under, 
          getTexX(letters[i]), getTexY(letters[i]), 
          getTexX(letters[i])+8, getTexY(letters[i]) + 8+texUnder, 5);
      horiz+=(getLength(letters[i])+1);
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
