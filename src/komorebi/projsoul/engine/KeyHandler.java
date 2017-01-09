/**
 * Keyboard.java Jul 5, 2016, 5:53:57 PM
 */
package komorebi.projsoul.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import komorebi.projsoul.gameplay.Key;

/**
 * Handles key input
 * 
 * @author Andrew Faulkenberry
 * @author Aaron Roy
 */
public class KeyHandler {

  private static boolean[] isKeyDown = new boolean[Keyboard.KEYBOARD_SIZE + 3];
  private static boolean[] wasKeyDown = new boolean[Keyboard.KEYBOARD_SIZE + 3];
  private static boolean[] buffer = new boolean[Keyboard.KEYBOARD_SIZE+3];
    
  public static final char NULL_CHAR = '\r';
  
  private static final int DOUBLE_CLICK_TOLERANCE = 15;
  private static int sinceL, sinceR, sinceS;
  
  private static boolean capsLock, numLock;
  
  /**
   * All of the possible controls that can be used in-game
   * 
   * @author Aaron Roy
   */
  public enum Control{
    UP, DOWN, LEFT, RIGHT, TALK, RUN, ATTACK, SWITCH, MAG_LEFT, MAG_RIGHT, MENU,
    
    MAP_UP, MAP_DOWN, MAP_LEFT, MAP_RIGHT, SAVE, NEW_SAVE, LOAD, NEW, GRID, 
    REVERT_MAP, RESET_LOC, PLAY, 
    TILE, MOVE_SET, EVENT, CONNECT, HEADER;
  }
  
  public static int totalKeys()
  {
    return Keyboard.KEYBOARD_SIZE + 3;
  }
  
  public static char charOf(Key k)
  {
    switch (k)
    {
      case A:
        return 'a';
      case APOSTROPHE:
        return '\'';
      case ASTERISK:
        return '*';
      case B:
        return 'b';
      case BACKSLASH:
        return '\\';
      case C:
        return 'c';
      case CLOSE_BRACKET:
        return ']';
      case COMMA:
        return ',';
      case D:
        return 'd';
      case DASH:
        return '-';
      case DECIMAL:
        return '.';
      case E:
        return 'e';
      case EQUALS:
        return '=';
      case F:
        return 'f';
      case G:
        return 'g';
      case GRAVE:
        return '`';
      case H:
        return 'h';
      case I:
        return 'i';
      case J:
        return 'j';
      case K:
        return 'k';
      case L:
        return 'l';
      case M:
        return 'm';
      case MINUS:
        return '-';
      case N:
        return 'n';
      case O:
        return 'o';
      case OPEN_BRACKET:
        return '[';
      case P:
        return 'p';
      case PAD0:
        return '0';
      case PAD1:
        return '1';
      case PAD2:
        return '2';
      case PAD3:
        return '3';
      case PAD4:
        return '4';
      case PAD5:
        return '5';
      case PAD6:
        return '6';
      case PAD7:
        return '7';
      case PAD8:
        return '8';
      case PAD9:
        return '9';
      case PERIOD:
        return '.';
      case PLUS:
        return '+';
      case Q:
        return 'q';
      case R:
        return 'r';
      case ROW0:
        return '0';
      case ROW1:
        return '1';
      case ROW2:
        return '2';
      case ROW3:
        return '3';
      case ROW4:
        return '4';
      case ROW5:
        return '5';
      case ROW6:
        return '6';
      case ROW7:
        return '7';
      case ROW8:
        return '8';
      case ROW9:
        return '9';
      case S:
        return 's';
      case SEMICOLON:
        return ';';
      case SLASH:
        return '/';
      case SPACE:
        return ' ';
      case T:
        return 't';
      case U:
        return 'u';
      case V:
        return 'v';      
      case W:
        return 'w'; 
      case X:
        return 'x'; 
      case Y:
        return 'y'; 
      case Z:
        return 'z'; 
      default:
        return NULL_CHAR;  
    }
  }

  /**
   * Gets input from all of the keys and mouse
   */
  public static void getInput()
  {

    for (int i=0; i < Keyboard.KEYBOARD_SIZE; i++)
    {
      buffer[i] = false;
      wasKeyDown[i]=isKeyDown[i];
      isKeyDown[i]=Keyboard.isKeyDown(i);

    }
    for (int i=Keyboard.KEYBOARD_SIZE; i < Keyboard.KEYBOARD_SIZE+3; i++)
    {
      buffer[i] = false;
      wasKeyDown[i]=isKeyDown[i];
      isKeyDown[i]=Mouse.isButtonDown(i-Keyboard.KEYBOARD_SIZE);
    }
    
    if (keyRelease(Key.LBUTTON))
    {
      sinceL = DOUBLE_CLICK_TOLERANCE;
    }
    
    if (keyRelease(Key.RBUTTON))
    {
      sinceR = DOUBLE_CLICK_TOLERANCE;
    }
    
    if (keyRelease(Key.MBUTTON))
    {
      sinceS = DOUBLE_CLICK_TOLERANCE;
    }
    
    if (sinceL > 0)
    {
      sinceL--;
    }
    
    if (sinceR > 0)
    {
      sinceR--;
    }
    
    if (sinceS > 0)
    {
      sinceS--;
    }
    
    if (isKeyDown[Keyboard.KEY_NUMLOCK] && !wasKeyDown[Keyboard.KEY_NUMLOCK])
    {
      numLock = !numLock;
    }
    
    if (isKeyDown[Keyboard.KEY_CAPITAL] && !wasKeyDown[Keyboard.KEY_CAPITAL])
    {
      capsLock = !capsLock;
    }
    
    
  }
  
  /**
   * Returns a boolean value which is true only at the first instant a key is pressed
   * @param k The Key value to be tested
   * @return Whether the inputted key value was just clicked
   */
  public static boolean keyClick(Key k)
  {
    if (isKeyDown[k.getGLKey()] && !wasKeyDown[k.getGLKey()])
    {
      return true;
    }
    return false;
  }
  
  public static boolean bufferedKeyClick(Key k)
  {
    if (isKeyDown[k.getGLKey()] && !wasKeyDown[k.getGLKey()] && 
        !buffer[k.getGLKey()])
    {
      return true;
    }
    return false;
  }
  
  public static void tempDisable(Key k)
  {
    buffer[k.getGLKey()] = true;
  }
  
  public static boolean keyRelease(Key k)
  {
    return (!isKeyDown[k.getGLKey()] && wasKeyDown[k.getGLKey()]);

  }

  /**
   * Returns a boolean value which is true if the specified key is down
   * @param k The key value to be tested
   * @return Whether the inputted key value is currently down
   */
  public static boolean keyDown(Key k)
  {
    return (isKeyDown[k.getGLKey()]);
  }
  
  /**
   * @param c The control to survey
   * @return If the requested button was pressed
   */
  public static boolean button(Control c){
    switch(c){
      case UP:        return keyDown(Key.UP);
      case DOWN:      return keyDown(Key.DOWN);
      case LEFT:      return keyDown(Key.LEFT);
      case RIGHT:     return keyDown(Key.RIGHT);
      case TALK:      return keyClick(Key.C);
      case ATTACK:    return keyClick(Key.X);
      case MAG_LEFT:  return keyClick(Key.A);
      case MAG_RIGHT: return keyClick(Key.S);
      case RUN:       return keyDown(Key.Z);
      case SWITCH:    return keyClick(Key.SPACE);
      case MENU:      return keyClick(Key.ENTER);

      case MAP_DOWN:   return keyDown(Key.DOWN)  || keyDown(Key.S) && !keyDown(Key.CTRL);
      case MAP_LEFT:   return keyDown(Key.LEFT)  || keyDown(Key.A);
      case MAP_RIGHT:  return keyDown(Key.RIGHT) || keyDown(Key.D);
      case MAP_UP:     return keyDown(Key.UP)    || keyDown(Key.W);
      case SAVE:       return !shiftDown()       && controlDown()  && keyClick(Key.S);
      case NEW_SAVE:   return shiftDown()        && controlDown()  && keyClick(Key.S);
      case LOAD:       return controlDown()      && keyClick(Key.L);
      case NEW:        return controlDown()      && keyClick(Key.N);
      case REVERT_MAP: return controlDown()      && keyClick(Key.R);
      case RESET_LOC:  return !controlDown()     && keyClick(Key.R);
      case GRID:       return controlDown()      && keyClick(Key.G);
      case PLAY:       return controlDown()      && keyClick(Key.P);
      case TILE:       return controlDown()      && keyClick(Key.ROW1);
      case MOVE_SET:   return controlDown()      && keyClick(Key.ROW2);
      case EVENT:      return controlDown()      && keyClick(Key.ROW3);
      case CONNECT:    return controlDown()      && keyClick(Key.ROW4);
      case HEADER:     return controlDown()      && keyClick(Key.ROW5);
      
      default: return false;
    }
  }

  /**
   * Resets the KeyHandler
   */
  public static void reset()
  {
    for (int i=0; i < Keyboard.KEYBOARD_SIZE; i++)
    {
      wasKeyDown[i]=false;
      isKeyDown[i]=false;
    }
  }
  
  /**
   * Designates whether either of the two control keys is down
   * @return Left-control or right-control is down
   */
  public static boolean controlDown()
  {
    return (isKeyDown[Keyboard.KEY_LCONTROL]) || (isKeyDown[Keyboard.KEY_RCONTROL]);
  }

  /**
   * Designates whether either of the two shift keys is down
   * @return Left-shift or right-shift is down
   */
  public static boolean shiftDown()
  {
    return (isKeyDown[Keyboard.KEY_LSHIFT]) || (isKeyDown[Keyboard.KEY_RSHIFT]);
  }
  
  /**
   * For some stupid reason the keys stick when JDialogs are opened, so this
   * method resets the Keyboard by destroying and creating it
   */
  public static void reloadKeyboard(){
    Keyboard.destroy();
    try {
      Keyboard.create();
    } catch (LWJGLException e) {
      e.printStackTrace();
    }

  }
  
  public static char keyWithShift(Key k)
  {
    switch (k)
    {
      case ROW1:
        return '!';
      case ROW2:
        return '@';
      case ROW3:
        return '#';
      case ROW4:
        return '$';
      case ROW5:
        return '%';
      case ROW6:
        return '^';
      case ROW7:
        return '&';
      case ROW8:
        return '*';
      case ROW9:
        return '(';
      case ROW0:
        return ')';
      case DASH:
        return '_';
      case EQUALS:
        return '+';
      case GRAVE:
        return '~';
      case OPEN_BRACKET:
        return '{';
      case CLOSE_BRACKET:
        return '}';
      case BACKSLASH:
        return '|';
      case SEMICOLON:
        return ':';
      case APOSTROPHE:
        return '\"';
      case COMMA:
        return '<';
      case PERIOD:
        return '>';
      case SLASH:
        return '?';
       default:
         return charOf(k);
    }
  }
  
  public static boolean capsLocked()
  {
    return capsLock;
  }
  
  public static boolean numLocked()
  {
    return numLock;
  }
  
  public static boolean doubleClick(Key mouseKey)
  {
    switch (mouseKey)
    {
      case LBUTTON:
        if (keyClick(mouseKey) && sinceL > 0)
        {
          sinceL = 0;
          return true;
        }
        return false;
      case RBUTTON:
        if (keyClick(mouseKey) && sinceR > 0)
        {
          sinceR = 0;
          return true;
        }
        return false;
      case MBUTTON:
        if (keyClick(mouseKey) && sinceS > 0)
        {
          sinceS = 0;
          return true;
        }
        return false;
      default:
        return false;
    }
  }

}
