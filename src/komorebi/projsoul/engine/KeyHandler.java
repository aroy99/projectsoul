/**
 * Keyboard.java Jul 5, 2016, 5:53:57 PM
 */
package komorebi.projsoul.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Handles key input
 * 
 * @author Andrew Faulkenberry
 * @author Aaron Roy
 */
public class KeyHandler {

  private static boolean[] isKeyDown = new boolean[Keyboard.KEYBOARD_SIZE + 3];
  private static boolean[] wasKeyDown = new boolean[Keyboard.KEYBOARD_SIZE + 3];
  private static boolean[] buffer = new boolean[Keyboard.KEYBOARD_SIZE + 3];
    
  /**
   * All of the possible controls that can be used in-game
   * 
   * @author Aaron Roy
   */
  public enum Control{
    UP, DOWN, LEFT, RIGHT, TALK, MENU, 
    
    MAP_UP, MAP_DOWN, MAP_LEFT, MAP_RIGHT, SAVE, NEW_SAVE, LOAD, NEW, GRID, 
    REVERT_MAP, RESET_LOC, PLAY, MOVE_SET, NPC;
  }  
  
  public static int totalKeys()
  {
    return Keyboard.KEYBOARD_SIZE + 3;
  }

  /**
   * Gets input from all of the keys and mouse
   */
  public static void getInput()
  {

    for (int i=0; i < Keyboard.KEYBOARD_SIZE; i++)
    {
      wasKeyDown[i]=isKeyDown[i];
      isKeyDown[i]=Keyboard.isKeyDown(i);

      if (buffer[i] && !isKeyDown[i])
      {
        buffer[i] = false;
      }
    }
    for (int i=Keyboard.KEYBOARD_SIZE; i < Keyboard.KEYBOARD_SIZE+3; i++)
    {
      wasKeyDown[i]=isKeyDown[i];
      isKeyDown[i]=Mouse.isButtonDown(i-Keyboard.KEYBOARD_SIZE);

      if (buffer[i] && !isKeyDown[i])
      {
        buffer[i] = false;
      }
    }

    
  }

  /**
   * Returns a boolean value which is true only at the first instant a key is pressed
   * @param k The Key value to be tested
   * @return Whether the inputted key value was just clicked
   */
  public static boolean keyClick(Key k)
  {
    if (isKeyDown[k.getGLKey()] && !wasKeyDown[k.getGLKey()] && !buffer[k.getGLKey()])
    {
      buffer[k.getGLKey()] = true;
      return true;
    }
    return false;
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
      case UP:    return keyDown(Key.UP);
      case DOWN:  return keyDown(Key.DOWN);
      case LEFT:  return keyDown(Key.LEFT);
      case RIGHT: return keyDown(Key.RIGHT);
      case TALK:  return keyClick(Key.Z);
      case MENU:  return keyClick(Key.ENTER);
             
      case MAP_DOWN:   return keyDown(Key.DOWN)  || keyDown(Key.S) && !keyDown(Key.CTRL);
      case MAP_LEFT:   return keyDown(Key.LEFT)  || keyDown(Key.A);
      case MAP_RIGHT:  return keyDown(Key.RIGHT) || keyDown(Key.D);
      case MAP_UP:     return keyDown(Key.UP)    || keyDown(Key.W);
      case SAVE:       return !keyDown(Key.SHIFT) && keyDown(Key.CTRL) && keyClick(Key.S);
      case NEW_SAVE:   return keyDown(Key.SHIFT) && keyDown(Key.CTRL) && keyClick(Key.S);
      case LOAD:       return keyDown(Key.CTRL)  && keyClick(Key.L);
      case NEW:        return keyDown(Key.CTRL)  && keyClick(Key.N);
      case REVERT_MAP: return keyDown(Key.CTRL)  && keyClick(Key.R);
      case RESET_LOC:  return !keyDown(Key.CTRL) && keyClick(Key.R);
      case GRID:       return keyDown(Key.CTRL)  && keyClick(Key.G);
      case PLAY:       return keyDown(Key.CTRL)  && keyClick(Key.P);
      case MOVE_SET:   return keyDown(Key.CTRL)  && keyClick(Key.M);
      case NPC:        return keyDown(Key.CTRL)  && keyClick(Key.C);

      default:         return false;
      
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

}
