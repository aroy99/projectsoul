/**
 * SpeechHandler.java    Jul 26, 2016, 10:54:08 AM
 */
package komorebi.projsoul.script.text;

import komorebi.projsoul.engine.Draw;

/**
 * 
 * @author Andrew Faulkenberry
 */
public class SpeechHandler extends TextHandler {

  private boolean alreadyAsked;
  private boolean hasChoice;
  
  private int scrollIndex;
  private int buffer;
  
  private static int speed = 3;
  private static boolean scrolling = false;
  
  private boolean delayed;
  private int delayIndex;

  private int dotCount;
  private boolean dots;
    
  private String[] options;
  
  private String answerToQuestion;


  /**
   * Creates a SpeechHandler object
   */
  public SpeechHandler()
  {
    super();
  }

  /**
   * Sets the speed for ALL SpeechHandlers in the game
   * @param speed 1 = slowest ... 3 = fastest
   */
  public static void setSpeed(int speed)
  {
    scrolling = true;
    SpeechHandler.speed = speed;
  }

  /**
   * Sets whether SpeechHandlers scroll (true) or all the text appears at once 
   * (false)
   * @param b Whether SpeechHandlers scroll
   */
  public static void setScrolling(boolean b)
  {
    scrolling = b;
  }

  public void setAskMode(boolean askMode)
  {
    hasChoice = askMode;
  }
  /**
   * Renders the text and speech box on screen
   */
  public void render()
  {
    if (!words.isEmpty())
    {
      Draw.rect(15, 15, 220, 59, 0, 0, 220, 59, 6);
    }
    
    if (hasChoice)
    {
      if (!alreadyAsked)
      {
        scrollingRender(words.get(0));
      } else {        
        for (Word word: words)
        {
          super.render(word);
        }
      }
    } else if (!words.isEmpty())
    { 
      scrollingRender(words.get(0));
    }

    if (dots) //Draws the "I'm waiting" ellipses animation . . . 
    {
      if (dotCount>=10 && dotCount<50)
      {
        Draw.rect(210, 25, 1, 1, 1, 0, 2, 1, 6);
      }

      if (dotCount>=20 && dotCount<60)
      {
        Draw.rect(215, 25, 1, 1, 1, 0, 2, 1, 6);
      }

      if (dotCount>=30 && dotCount<70)
      {
        Draw.rect(220, 25, 1, 1, 1, 0, 2, 1, 6);
      }

      dotCount++;
      if (dotCount>=80)
      {
        dotCount=0;
      }
    }

  }

  /**
   * Renders a word letter by letter, creating a scrolling effect
   * @param word The Word object to be rendered
   */
  private void scrollingRender(Word word)
  {

    int horiz = word.getX();
    int vert = word.getY();
    int size = word.getFont().getFontPoint()*word.getFont().getScale();
    char[] letters = word.currentParagraph();

    int ohor = horiz;
    int scroll;
    
    if (scrolling)
    {
      scroll = scrollIndex;
    } else
    {
      scroll = word.currentParagraph().length;
    }

    for (int i=0; i < scroll; i++)
    {

      //Escape sequence \n
      if (letters[i]=='\\')
      {
        if (letters[i+1]=='n')
        {
          vert-=size*2;
          horiz = ohor;
          i++;
          continue; 
        } else if (letters[i+1]=='d')
        {
          i++;
          continue;
        }
      }

      //Escape sequence \d
      if (letters[scroll-1]=='\\' && letters[scroll]=='d')
      {
        if (!delayed)
        {
          delayed = true;
          delayIndex = 10;
        }
      }

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
    
    /* Signifies that the scrolling is done, so if the user clicks "C",
     * it will go to the next paragraph
     */
    if (scrollIndex==word.currentParagraph().length)
    {
      alreadyAsked = true;
      if (word.hasNext())
      {
        dots = true;
      }
    }

    if (!dots && !delayed) //Increments the scroll index
    {
      if (scrollIndex<letters.length)
      {
        buffer++;
        if (buffer>=speed)
        {
          scrollIndex++;
          buffer=0;
        }
      } else if (hasChoice){
        scrollIndex = 0;
        alreadyAsked = true;
      }
    } else if (delayed) //For dramatic effect . . . 
    {
      delayIndex--;

      if (delayIndex<=0)
      {
        delayed=false;
        buffer++;
        if (buffer>=speed)
        {
          scrollIndex++;
          buffer=0;
        }
      }
    }


  }

  /**
   * Clears all data in the object
   */
  public void clear()
  {    
    super.clear();
    scrollIndex = 0;
    hasChoice = false;
    alreadyAsked = false;
  }

  /**
   * Writes a String for to be shown in a speech box to the object's memory
   */
  public void write(String s, int x, int y, Font font)
  {
    super.write(s,x,y,font);
  }

  /**
   * Will skip the scrolling text, showing all text up until the next 
   * paragraph marker (\p)
   */
  public void skipScroll()
  {
    alreadyAsked = true;
    scrollIndex = words.get(0).currentParagraph().length;
  }

  public boolean alreadyAsked()
  {
    return alreadyAsked;
  }

  public boolean isWaitingOnParagraph()
  {
    return dots;
  }

  /**
   * Goes to the next paragraph, or the next grouping of text between the 
   * previous and next paragraph marker (\p)
   */
  public void nextParagraph()
  {
    
    words.get(0).nextParagraph();
    dots = false;
    if (scrolling) 
    {
      scrollIndex =0;
    }
    dotCount = 0;
    
    alreadyAsked = false;

  }

  /**
   * Creates a String object from a given char array
   * @param array The char array to be converted
   * @return A string containing all the chars in the char array, in order of
   * their appearance in the array
   */
  public static String charToString(char[] array)
  {
    String s = "";
    for (char c: array)
    {
      s = s + c;
    }

    return s;
  }
  
  public void setOptions(String[] args)
  {
    options = args;
  }
  
  public int maxOptionIndex()
  {
    return options.length - 1;
  }
  
  public void branch(int i)
  {
    answerToQuestion = options[i];
  }
  
  public String getAnswer()
  {
    return answerToQuestion;
  }
}
