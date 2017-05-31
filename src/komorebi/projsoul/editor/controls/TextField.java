package komorebi.projsoul.editor.controls;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.TextHandler;

public class TextField {

  private String stringAtBeginningEdit;
  private String string;
  private int x, y;
  private TextHandler text;
  private int blinker;
  
  private int cursor;
  private int visMin, visMax;
  
  private int keyDown;
  
  private static final int CURSOR_TIME = 50;
  private static final int CURSOR_MOD = 5;
  private boolean isFocused;
  private static final int PENCIL_TIME = 50;
  private int arbCount;
    
  public TextField(int x, int y)
  {
    blinker = 0;
    this.x = x;
    this.y = y;
    text = new TextHandler(Draw.LAYER_MANAGER);
  }
  
  public void setLocation(int x, int y)
  {
    this.x = x;
    this.y = y;
    updateText();
  }
  
  public boolean isFocused()
  {
    return isFocused;
  }
  
  public String getText()
  {
    return string;
  }
  
  public void setFocused(boolean b)
  {
    isFocused = b;
    
    if (!isFocused)
    {
      
      text.clear();
      text.writeMindLength(string, 
        x, y, new EarthboundFont(2), 90);
      
      
    } else
    {
      stringAtBeginningEdit = string + "";
      
      text.clear();
      visMax = string.length();
      visMin = visMax-1;
      
      while (visMin >= 0 && text.pixLengthOf(string.substring(visMin, visMax),
          new EarthboundFont(2)) < 90)
       {
          visMin--;
       }
      
      visMin++;      
      updateText();
    }
  }
  
  public void setText(String str)
  {
    this.stringAtBeginningEdit = str + "";
    this.string = str;
    
    visMin = 0;
    visMax = string.length();
    updateText();
  }
  
  public void push(float dy)
  {
    y -= dy;
    updateText();
  }
  
  public void update()
  {    
    blinker++;
    
    if (blinker>=CURSOR_TIME*2)
    {
      blinker = 0;
    }
    
    if (KeyHandler.keyDown(Key.LEFT) && cursor > 0)
    {
      if (KeyHandler.keyClick(Key.LEFT))
      {
        keyDown = 0;
      }
      
      if (keyDown % CURSOR_MOD == 0)
      {
        cursor--;
        blinker = 0;
        
        updateText();
       
      }
      
      keyDown++;
    }
    
    if (KeyHandler.keyDown(Key.RIGHT) && cursor < string.length())
    {
      if (KeyHandler.keyClick(Key.RIGHT))
      {
        keyDown = 0;
      }
      
      if (keyDown % CURSOR_MOD == 0)
      {
        cursor++;
        updateText();
        blinker = 0;
      }
      
      keyDown++;    
     }
    
    if (KeyHandler.keyDown(Key.BACKSPACE) && cursor > 0)
    {
      if (KeyHandler.keyClick(Key.BACKSPACE))
      {
        keyDown = 0;
      }
      
      if (keyDown % CURSOR_MOD == 0)
      {
        setString(string.substring(0, cursor-1) + string.substring(cursor, 
            string.length()));
        cursor--;
        if (visMin > 0)
        {
          visMin--;
        }
        updateText();

      }
      
      keyDown++;    
     }
    
    if (KeyHandler.keyDown(Key.DEL) && cursor < string.length())
    {
      if (KeyHandler.keyClick(Key.DEL))
      {
        keyDown = 0;
      }
      
      if (keyDown % CURSOR_MOD == 0)
      {
        setString(string.substring(0, cursor) + string.substring(cursor+1, 
            string.length()));
        updateText();
      }
      
      keyDown++;
    }
    
    
    for (Key k: Key.values())
    {
      if (KeyHandler.keyClick(k))
      {
        char c = KeyHandler.charOf(k);
       
        if (c != KeyHandler.NULL_CHAR)
        {
          if (KeyHandler.shiftDown() || KeyHandler.capsLocked()) 
          {
            if (Character.isAlphabetic(c))
            {
              c = Character.toUpperCase(c);
            } else if (!KeyHandler.capsLocked())
            {
              c = KeyHandler.keyWithShift(k);
            }
          }
         
          setString(string.substring(0, cursor) + c + 
              string.substring(cursor, string.length()));
          cursor++;
          
          
          updateText();
        }
      }
    }
    
    arbCount++;
    if (arbCount>=PENCIL_TIME*2)
    {
      arbCount = 0;
    }
  }
  
  public boolean showPencil()
  {
    return arbCount < PENCIL_TIME;
  }
  
  public void render()
  {
    text.render();
    if (isFocused && blinker < CURSOR_TIME)
    {
      Draw.drawIfInBounds(Draw.LAYER_MANAGER, x+text.pixLengthOf(string.substring(visMin, cursor), 
          new EarthboundFont(2)), y, 
        2, 16, 111, 82, 112, 90, 2);
    }
  }
  
  private void updateText()
  {
    text.clear();    
    
    if (cursor > visMax)
    {
      visMax++;
    }
    
    if (visMax > string.length())
    {
      visMax = string.length();
    }
    
    if (cursor < visMin)
    {
      visMin--;
    }
   
    while (text.pixLengthOf(string.substring(visMin, visMax), 
        new EarthboundFont(2)) > 90)
    {
      if (cursor >= visMax)
      {
        visMin++;
      } else {
        visMax--;
      }
    }
    text.write(string.substring(visMin, visMax), 
        x, y, new EarthboundFont(2));
  }
  
  public boolean wasChanged()
  {
    return !stringAtBeginningEdit.equals(string);
  }
  
  public void setUnchanged()
  {
    stringAtBeginningEdit = string + "";
  }
  
  private void setString(String newStr)
  {
    string = newStr;
  }
  
  public String getPreviousText()
  {
    return stringAtBeginningEdit;
  }

}
