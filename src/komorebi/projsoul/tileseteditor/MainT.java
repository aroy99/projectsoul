/**
 * MainT.java		Jul 28, 2016, 2:30:13 PM
 */
package komorebi.projsoul.tileseteditor;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.openal.SoundStore;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.KeyHandler;


/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class MainT {

  /**
   * Use this if you want to run the editor
   * 
   * @author Aaron Roy
   * @version 0.0.1.0
   */

  public static int scale = 2;
  public static boolean grid;

  private static boolean running = true;
  private static TileSetEditor edit = new TileSetEditor();

  /**
   * Starts the program, reading an int from settings and using it for the scale.
   * @param args not used
   */
  public static void main(String[] args) {

    initDisplay();
    initGl();

    initGame();
    gameLoop();
    cleanUp();
  }


  /**
   *  Initializes the Display using the Display Class, properly Scaling it.
   */
  public static void initDisplay() {
    //create display
    try {
      Display.setDisplayMode(new DisplayMode(272 * scale, 256 * scale));
      Display.setTitle("Clyde\'s Tile Editor");
      //      Display.setResizable(true);
      Display.create();
      Display.setVSyncEnabled(true);

    } catch (LWJGLException ex) {
      ex.printStackTrace();
    }
  }

  /**
   *  Creates a new game and initializes the audio.
   *  @see GameHandler
   */
  private static void initGame() {

  }


  private static void getInput() {
    KeyHandler.getInput();
    edit.getInput();
  }

  private static void update() {
    edit.update();
  }


  private static void render() {
    glClear(GL_COLOR_BUFFER_BIT);   //clears the matrix with black
    glLoadIdentity();

    Draw.rect(0, 0, 128, 256, 220, 12, 221, 13, 6); //Magenta palette background
    edit.render();
    if (grid) Grid.render();

    Display.update();   //updates the display with the changes
    Display.sync(60);   //makes up for lost time

  }


  /**
   *  Goes through the game loop, starting the music once.
   */
  private static void gameLoop() {

    while (running) {
      getInput();
      update();
      render();
      SoundStore.get().poll(0);

      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || 
          Display.isCloseRequested()) {

        running = !edit.closeFile();

      }

    }
  }

  /**
   *  Currently Enabled:<br>
   *  -Textures<br>
   *  -Transparency
   *  
   *  <p>Size: 256 x 224.
   */
  private static void initGl() {
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();              //resets the Matrix
    glOrtho(0,272,0,256,-1,1);     //creates a 3D space
    glMatrixMode(GL_MODELVIEW);
    glEnable(GL_TEXTURE_2D);       //enables Textures
    glEnable(GL_BLEND);

    //Enables transparency
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    glClearColor(246f / 255,246f / 255,246f / 255,1);//sets the clearing color to black

    glDisable(GL_DEPTH_TEST);      //kills off the third dimension
  }

  /**
   *  Destroys the display and keyboard, closing the window.
   */
  private static void cleanUp() {
    Display.destroy();
    AL.destroy();
  }

  public static int getScale() {
    return scale;
  }

  public static void switchGrid()
  {
    grid = !grid;
  }
}
