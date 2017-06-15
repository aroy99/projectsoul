/*
 * Main.java           Apr 27, 2016, 8:28:15 PM
 */

package komorebi.projsoul.engine;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

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
import static org.lwjgl.opengl.GL11.glViewport;

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.script.commands.keywords.Keywords;
import komorebi.projsoul.script.decision.Flags;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.TextHandler;
import komorebi.projsoul.states.Game;

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
import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.audio.Song;
import komorebi.projsoul.states.Game;
import komorebi.projsoul.states.State.States;
import komorebi.projsoul.engine.Save;

/**
 * Initializes everything and uses the game handler to start the game
 * 
 * @author Aaron Roy
 * @version 0.0.1.0
 * 
 */
public class Main {

  private GameHandler gamehandler;
  public static int scale=1;
  //private BufferedReader read;
  private BufferedReader readSave;
  
  private static TextHandler handler;
  private static long lastFrame, lastFPS;
  private static int fps;
  
  public static final int WIDTH = 256;
  public static final int HEIGHT = 224;
  
  public static final float ASPECT = (float)WIDTH/HEIGHT;
  
  public static int oldWidth = WIDTH, oldHeight = HEIGHT;
  public static int renderWidth, renderHeight;
  

  public static void main(String[] args){
    new Main().run();
  }


  /**
   * Runs the game
   */
  private void run() {
  /* reads from the settings file 
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
        } else if(Game.testLoc == null){
          Game.testLoc = str;
        }
        
      }

    } catch (IOException | NumberFormatException e) {
      e.printStackTrace();
      scale = 1;
    }
   /* Reads from your save file
    //*/
   ///* Reads from the SettingS file
    try{
      readSave = new BufferedReader(new FileReader(new File("res/settingsS")));
      String str;

      while ((str = readSave.readLine()) != null) {
        if(str.equals("") || str.charAt(0) == '#'){
          continue;
        }
        if(Game.saveLoc == null){
          Game.saveLoc = str;
          Save savey = new Save(Game.saveLoc);  
        }
        /*
        if(numberOfSaves==0 && ){

        }*/

      }}catch (IOException | NumberFormatException e) {
      e.printStackTrace();
      scale = 1;
    }
    initDisplay();

    initGL();


    initGame();

    GameHandler.switchState(States.MENU);
	  AudioHandler.play(Song.CHAOS, true);
    gameLoop();
    cleanUp();
  }


  /**
   *  Initializes the Display using the Display Class, properly Scaling it
   */
  public void initDisplay(){
    //create display
    try {
      renderWidth = WIDTH*scale;
      renderHeight = HEIGHT*scale;
      Display.setDisplayMode(new DisplayMode(WIDTH*scale,HEIGHT*scale));
      Display.setTitle("Project Soul");
      Display.create();
      Display.setVSyncEnabled(true);
//      Display.setResizable(true);

    } catch (LWJGLException e) {
      e.printStackTrace();
    }
  }

  /**
   *  Creates a new game and initializes the audio
   *  @see GameHandler
   *  @see AudioHandler
   */
  private void initGame(){

    Draw.readSpreadsheets();
    initScripts();

    gamehandler = new GameHandler();
    AudioHandler.init();
    
    getDelta();          // call once before loop to initialise lastFrame
    lastFPS = getTime(); // call before loop to initialise fps timer
    
    handler = new TextHandler();
  }
  
  private void initScripts()
  {
    Flags.loadFlags();
    
    try
    {
      Keywords.loadKeywords();
    } catch (Exception e)
    {
      e.printStackTrace();
      System.exit(1);
    }
  }


  private void getInput(){
    gamehandler.getInput();
  }

  private void update(int delta){
    if(Display.wasResized()){
      System.out.println("HAI HO");
      resizeHandler(Display.getWidth(), Display.getHeight());
    }
    if(Keyboard.isKeyDown(Keyboard.KEY_0)){
      try {
        Display.setDisplayMode(new DisplayMode(WIDTH*scale,HEIGHT*scale));
        Display.setFullscreen(false);

        glViewport(0, 0, WIDTH*scale, HEIGHT*scale);
      } catch (LWJGLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
      try {
        Display.setFullscreen(true);
        Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
        resizeHandler(Display.getWidth(), Display.getHeight());
      } catch (LWJGLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    
    gamehandler.update();
    updateFPS(delta);
  }



private void render(){
    glClear(GL_COLOR_BUFFER_BIT);   //clears the matrix with black
    glLoadIdentity();

    gamehandler.render();
    handler.render();

    Display.update();   //updates the display with the changes
    Display.sync(60);   //makes up for lost time

  }


  /**
   *  Goes through the game loop, starting the music once
   */
  private void gameLoop(){

    while(!Display.isCloseRequested()){
      int delta = getDelta();
      
      getInput();
      update(delta);
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
      handler.write("FPS: " + fps, 1, 1, new EarthboundFont(1));
      fps = 0; //reset the FPS counter
      lastFPS += 1000; //add one second
    }
    fps++;
  }
  
  private void resizeHandler(int windowWidth, int windowHeight){
    if(renderWidth < windowWidth && windowHeight == oldHeight){
      //Recenter the screen
      int offX = (windowWidth - renderWidth)/2;
      glViewport(offX, 0, renderWidth, windowHeight);
    }
    
//    glViewport(0, 0, windowWidth, windowHeight);
    oldWidth = windowWidth;
    oldHeight = windowHeight;    
  }



}
