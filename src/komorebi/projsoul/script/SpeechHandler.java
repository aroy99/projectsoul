/**
 * SpeechHandler.java		Jul 26, 2016, 10:54:08 AM
 */
package komorebi.projsoul.script;

import komorebi.projsoul.engine.Draw;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class SpeechHandler extends TextHandler {

  private boolean hasChoice;
  private int pickerIndex;
  private boolean alreadyAsked;
  
  private int scrollIndex;
  private int buffer;
  
  private static int speed;
  private static boolean scrolling;
  
  private boolean delayed;
  private int delayIndex;

  private int dotCount;
  private boolean dots;
  
  private Lock lock;
  
  private String[] options;
  
  private String answerToQuestion;


  public SpeechHandler(boolean b)
  {
    super();
    scrolling = b;
    speed = 1;
  }

  public static void setSpeed(int speed)
  {
    scrolling = true;
    SpeechHandler.speed = speed;
  }

  public static void setScrolling(boolean b)
  {
    scrolling = b;
  }

  @Override
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
        
        int x = 0, y= 0;
        switch (pickerIndex)
        {
          case 1: x = 20; y = 40; break;
          case 2: x = 90; y = 40; break;
          case 3: x = 20; y = 22; break;
          case 4: x = 90; y = 22; break;
          default: break;
        }

        Draw.rect(x, y, 8, 8, 0, 0, 8, 8, 7);
      }


    } else if (!words.isEmpty())
    { 
      scrollingRender(words.get(0));
    }

    if (dots)
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

  private void scrollingRender(Word word)
  {

    int horiz = word.getX();
    int vert = word.getY();
    int size = word.getFontSize();
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
        texUnder = 8;
      }


      Draw.rect(horiz, vert-under, size, size+under, 
          getTexX(letters[i]), getTexY(letters[i]), 
          getTexX(letters[i])+8, getTexY(letters[i]) + 8+texUnder, 5);
      horiz+=(getLength(letters[i])+1);
    }
    
    if (scrollIndex==word.currentParagraph().length)
    {
      alreadyAsked = true;
      if (word.hasNext())
      {
        dots = true;
      }
    }

    if (!dots && !delayed)
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
    } else if (delayed)
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
   * Draws the arrow pointing to one of two text-based choices
   * @param option Which option the arrow should correspond to, where 1 represents the left option and 2 represents the right option
   */
  public void drawPicker(int option)
  {
    pickerIndex = option;
    hasChoice = true;
  }

  public int getPickerIndex()
  {
    return pickerIndex;
  }

  public void setPickerIndex(int option)
  {
    pickerIndex = option;
  }

  @Override
  public void clear()
  {    
    super.clear();
    scrollIndex = 0;
    hasChoice = false;
    alreadyAsked = false;
  }

  @Override
  public void write(String s, int x, int y, int fontPt)
  {
    super.write(s,x,y,fontPt);
  }

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

  public void nextParagraph()
  {
    
    words.get(0).nextParagraph();
    dots = false;
    if (scrolling) 
    {
      scrollIndex =0;
    }
    dotCount = 0;

  }

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
  
  public void setLockAndPause(Lock lock)
  {
    this.lock = lock;
    lock.pauseThread();
  }
  
  public void branch(int i)
  {
    answerToQuestion = options[i];
    this.lock.resumeThread();
  }
  
  public String getAnswer()
  {
    return answerToQuestion;
  }

}
