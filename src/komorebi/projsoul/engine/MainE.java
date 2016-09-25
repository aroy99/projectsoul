/*
 * MainE.java        May 22, 2016, 2:15:58 PM
 */

package komorebi.projsoul.engine;

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

import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.script.TextHandler;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.openal.SoundStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Use this if you want to run the editor
 * 
 * @author Aaron Roy
 */
public class MainE {

  public static Editor edit;
  public static int scale = 1;
  private static BufferedReader read;

  private static TextHandler handler;
  private static long lastFrame, lastFPS;
  private static int fps;
  
  private static boolean running = true;
  
  public static final int WIDTH = 800;
  public static final int HEIGHT = 608;

  /**
   * Starts the program, reading an int from settings and using it for the scale.
   * @param args not used
   */
  public static void main(String[] args) {

    try {
      read = new BufferedReader(
          new FileReader(new File("res/settings")));
      String str;

      while ((str = read.readLine()) != null) {
        if(str.equals("") || str.charAt(0) == '#'){
          continue;
        }
        if (scale == 0) {
          scale = Integer.parseInt(str);
        }
      }

    } catch (IOException ex) {
      ex.printStackTrace();
      scale = 1;
    } catch (NumberFormatException ex) {
      ex.printStackTrace();
      scale = 1;
    }

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
      Display.setDisplayMode(new DisplayMode(WIDTH * scale, HEIGHT * scale));
      Display.setTitle("Project Soul Editor");
      //    Display.setResizable(true);
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
    edit = new Editor();
    //AudioHandler.init();

    getDelta();          // call once before loop to initialise lastFrame
    lastFPS = getTime(); // call before loop to initialise fps timer

    handler = new TextHandler();

  }


  private static void getInput() {
    edit.getInput();
  }

  private static void update(int delta) {
    edit.update();
    updateFPS(delta); // update FPS Counter
  }


  private static void render() {
    glClear(GL_COLOR_BUFFER_BIT);   //clears the matrix with black
    glLoadIdentity();

    edit.render();
    handler.render();

    Display.update();   //updates the display with the changes
    Display.sync(60);   //makes up for lost time

  }


  /**
   *  Goes through the game loop, starting the music once.
   */
  private static void gameLoop() {

    while (running) {
      int delta = getDelta();

      getInput();
      update(delta);
      render();
      SoundStore.get().poll(0);

      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || 
          Display.isCloseRequested()) {
        if(Editor.requestSave()){
          running = false;
        }
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
    glOrtho(0,WIDTH,0,HEIGHT,-1,1);     //creates a 3D space
    glMatrixMode(GL_MODELVIEW);
    glEnable(GL_TEXTURE_2D);       //enables Textures
    glEnable(GL_BLEND);

    //Enables transparency
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    //sets the clearing color to light gray
    glClearColor(246f / 255,246f / 255,246f / 255,1);

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

  private static long getTime(){
    return System.currentTimeMillis();
  }

  private static int getDelta(){
    long time = getTime();
    int delta = (int)(time - lastFrame);
    lastFrame = time;

    return delta;
  }

  /**
   * Calculate the FPS and set it in the title bar
   */
  private static void updateFPS(int delta) {
    if (getTime() - lastFPS > 1000) {
      handler.clear();
      handler.write("FPS: " + fps, 1, 10, 8);
      handler.write("Delta: " + delta, 1, 1, 8);
      fps = 0; //reset the FPS counter
      lastFPS += 1000; //add one second
    }
    fps++;
  }

}
