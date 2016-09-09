/*
 * Main.java           Apr 27, 2016, 8:28:15 PM
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.openal.SoundStore;

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.states.Game;



/**
 * Initializes everything and uses the game handler to start the game
 * 
 * @author Aaron Roy
 * @version 0.0.1.0
 * 
 */
public class Main {

  private GameHandler gamehandler;
  public int scale;
  private BufferedReader read;
  
  public static final int WIDTH = 256;
  public static final int HEIGHT = 224;


  public static void main(String[] args){
    new Main().run();
  }


  /**
   * Runs the game
   */
  private void run() {
    try {
      read = new BufferedReader(
          new FileReader(new File("res/settings")));
      String str;

      while ((str = read.readLine()) != null) {
        if(str.equals("") || str.charAt(0) == '#'){
          continue;
        }
        if(scale == 0){
          scale = Integer.parseInt(str);
        }
        else if(Game.testLoc == null){
          Game.testLoc = str;
        }
        
      }

    } catch (IOException | NumberFormatException e) {
      e.printStackTrace();
      scale = 1;
    }

    initDisplay();
    initGL();

    initGame();
    gameLoop();
    cleanUp();
  }


  /**
   *  Initializes the Display using the Display Class, properly Scaling it
   */
  public void initDisplay(){
    //create display
    try {
      Display.setDisplayMode(new DisplayMode(WIDTH*scale,HEIGHT*scale));
      Display.setTitle("Project Soul");
      Display.create();
      Display.setVSyncEnabled(true);

    } catch (LWJGLException e) {
      e.printStackTrace();
    }
  }

  /**
   *  Creates a new game and initializes the audio
   *  @see GameHandler
   */
  private void initGame(){
    gamehandler = new GameHandler();
    AudioHandler.init();
  }


  private void getInput(){
    gamehandler.getInput();
  }

  private void update(){
    gamehandler.update();
  }


  private void render(){
    glClear(GL_COLOR_BUFFER_BIT);   //clears the matrix with black
    glLoadIdentity();

    gamehandler.render();

    Display.update();   //updates the display with the changes
    Display.sync(60);   //makes up for lost time

  }


  /**
   *  Goes through the game loop, starting the music once
   */
  private void gameLoop(){

    while(!Display.isCloseRequested()){
      getInput();
      update();
      render();
      SoundStore.get().poll(0);

      if (!Keyboard.isCreated())
      {
        try {
          Keyboard.create();
        } catch (LWJGLException e) {
          e.printStackTrace();
        }
      }
      if(Keyboard.isKeyDown(Keyboard.KEY_F4)){
        break;
      }

    }
  }

  /**
   *  Currently Enabled:<br>
   *  -Textures<br>
   *  -Transparency
   *  
   *  <p>Size: 256 x 224
   */
  private void initGL(){
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();              //resets the Matrix
    glOrtho(0,WIDTH,0,HEIGHT,-1,1);     //creates a 3D space
    glMatrixMode(GL_MODELVIEW);
    glEnable(GL_TEXTURE_2D);       //enables Textures
    glEnable (GL_BLEND);

    //Enables transparency
    glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    glClearColor(0,0,0,1);         //sets the clearing color to black

    glDisable(GL_DEPTH_TEST);      //kills off the third dimension
  }

  /**
   *  Destroys the display and keyboard, closing the window
   */
  private void cleanUp(){
    Display.destroy();
    AL.destroy();
    System.exit(0);
  }

  public int getScale(){
    return scale;
  }

  public static Game getGame(){
    return GameHandler.game;
  }


}
