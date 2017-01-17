/**
 * Fader.java    Jun 26, 2016, 3:54:54 PM
 */

package komorebi.projsoul.script;

import komorebi.projsoul.engine.Draw;


/**
 * Use this to fade the screen out, static only
 * 
 * @author Andrew Faulkenberry
 */
public class Fader {

  private static int faderIndex;
  private static boolean isFadingOut, isFadingIn;

  private static Lock lock;
  
  //Makes it impossible to instantiate this class
  private Fader(){}
  
  /**
   * Gradually fades out the screen by rendering a black rectangle that 
   * gradually becomes opaque
   * @param lock The lock that pauses the thread waiting for the screen to fade
   *         out
   */
  public static void fadeOut(Lock lock)
  {
    isFadingOut=true;
    Fader.lock = lock;
    Fader.lock.pauseThread();
  }
  
  /**
   * Gradually fades in the screen by rendering a black rectangle that gradually
   * becomes transparent
   * @param lock The lock that pauses the thread waiting for the screen to fade
   *         in
   */
  public static void fadeIn(Lock lock)
  {
    isFadingIn=true;
    Fader.lock = lock;
    Fader.lock.pauseThread();
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
        lock.resumeThread();
      }
    }


    if (isFadingIn) 
    {
      faderIndex--;
      if (faderIndex <= 0) 
      {
        isFadingIn=false;
        lock.resumeThread();
      }
    }


  }

  public static void render()
  {
    Draw.rect(0, 0, 284, 224, faderIndex, 0, faderIndex+1, 1, 8);
  }
}
