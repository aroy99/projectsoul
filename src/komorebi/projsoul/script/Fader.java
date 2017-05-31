/**
 * Fader.java    Jun 26, 2016, 3:54:54 PM
 */

package komorebi.projsoul.script;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.engine.ThreadHandler.TrackableThread;


/**
 * Use this to fade the screen out, static only
 * 
 * @author Andrew Faulkenberry
 */
public class Fader {

  private static int faderIndex;
  private static boolean isFadingOut, isFadingIn;

  private static TrackableThread waiting;
  
  //Makes it impossible to instantiate this class
  private Fader(){}
  
  /**
   * Gradually fades out the screen by rendering a black rectangle that 
   * gradually becomes opaque
   * @param lock The lock that pauses the thread waiting for the screen to fade
   *         out
   */
  public static void fadeOut()
  {
    isFadingOut=true;
    waiting = ThreadHandler.currentThread();
    ThreadHandler.lockCurrentThread();
  }
  
  /**
   * Gradually fades in the screen by rendering a black rectangle that gradually
   * becomes transparent
   * @param lock The lock that pauses the thread waiting for the screen to fade
   *         in
   */
  public static void fadeIn()
  {
    isFadingIn=true;
    waiting = ThreadHandler.currentThread();
    ThreadHandler.lockCurrentThread();
  }

  /**
   * Updates the static Fader class
   */
  public static void update()
  {

    if (isFadingOut) 
    {
      faderIndex++;
      if (faderIndex > 16) 
      {
        isFadingOut=false;
        waiting.unlock();
      }
    }


    if (isFadingIn) 
    {
      faderIndex--;
      if (faderIndex <= 0) 
      {
        isFadingIn=false;
        waiting.unlock();
      }
    }


  }

  public static void render()
  {
    Draw.rect(0, 0, 284, 224, faderIndex, 0, faderIndex+1, 1, 8);
  }
}
