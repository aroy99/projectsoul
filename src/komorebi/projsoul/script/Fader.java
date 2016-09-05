/**
 * Fader.java    Jun 26, 2016, 3:54:54 PM
 */

package komorebi.projsoul.script;

import komorebi.projsoul.engine.Draw;


/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class Fader {

  private static int faderIndex;
  private static boolean isFadingOut, isFadingIn;

  private static Execution instructor;
  private static Lock lock;
  
  @Deprecated
  /**
   * Fades the screen out
   * @param ex The thread on which the method is called
   */
  public static void fadeOut(Execution ex)
  {
    isFadingOut = true;
    instructor = ex;
    instructor.getLock().pauseThread();

  }
  
  public static void fadeOut(Lock lock)
  {
    isFadingOut=true;
    Fader.lock = lock;
    Fader.lock.pauseThread();
  }
  
  public static void fadeIn(Lock lock)
  {
    isFadingIn=true;
    Fader.lock = lock;
    Fader.lock.pauseThread();
  }

  @Deprecated
  /**
   * Fades the screen in
   * @param ex The thread on which the method is called
   */
  public static void fadeIn(Execution ex)
  {

    isFadingIn=true;

    instructor = ex;
    instructor.getLock().pauseThread();

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
