package komorebi.projsoul.editor.script;

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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.script.commands.keywords.Keywords;
import komorebi.projsoul.script.decision.Flags;
import komorebi.projsoul.script.utils.ScriptDatabase;
import komorebi.projsoul.states.Game;

public class TestScripts {

  private GameHandler gamehandler;
  public int scale;

  public static final int WIDTH = 256;
  public static final int HEIGHT = 224;

  private static final String DEFAULT_MAP = "ScriptTestingMap.map";
  private static final File DEFAULT_MAP_FILE = new File(
      "res/maps/" + DEFAULT_MAP);

  public static void main(String[] args){
    
    System.out.println("START PRINTING ARGS");
    
    for (String arg: args)
    {
      System.out.println(arg);
    }
    
    System.out.println("STOP PRINTING ARGS");

    
    if (args.length != 2)
    {
      args = new String[2];
      args[0] = "do_nothing";
      args[1] = "do_nothing";
    }
    
    new TestScripts().run(args);
  }

  private static void addTestNPCToDefaultMap(String walkScript,
      String talkScript)
  {
    try {
      PrintWriter write = new PrintWriter(new FileWriter(
          DEFAULT_MAP_FILE, true));

      write.println("npc testMe 12 10 POKEMON " + walkScript + " " + 
          talkScript);

      write.close();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static void removeTestNPCFromDefaultMap()
  {
    File temp = new File("res/maps/temp0x0");

    try {
      BufferedReader read = new BufferedReader(
          new FileReader(DEFAULT_MAP_FILE));
      PrintWriter write = new PrintWriter(new FileWriter(
          temp, false));


      String line;

      while ((line = read.readLine()) != null)
      {
        if (!line.startsWith("npc testMe"))
          write.println(line);
      }

      read.close();
      write.close();
      
      DEFAULT_MAP_FILE.delete();
      temp.renameTo(DEFAULT_MAP_FILE);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Runs the game
   */
  private void run(String[] args) {

    addTestNPCToDefaultMap(args[0], args[1]);

    scale = 2;
    Game.testLoc = DEFAULT_MAP;

    initDisplay();
    initGL();

    initGame();
    
    removeTestNPCFromDefaultMap();

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
   *  @see AudioHandler
   */
  private void initGame(){

    initScripts();

    gamehandler = new GameHandler();
    AudioHandler.init();
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

    ScriptDatabase.loadScripts();
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
      //SoundStore.get().poll(0);

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
