/**
 * SpeechTest.java   May 22, 2017, 7:21:25 PM
 */
package komorebi.projsoul.script.text;

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

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.states.Game;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Shows the text box so you can plan spacing
 *
 * @author Aaron Roy
 */
public class SpeechTest {

  public int scale;
  private BufferedReader read;
  private EarthboundFont font;
  private String currString;

  private static SpeechHandler handler;

  public static final int WIDTH = 256;
  public static final int HEIGHT = 224;

  public static final float ASPECT = (float)WIDTH/HEIGHT;

  public static int oldWidth = WIDTH, oldHeight = HEIGHT;
  public static int renderWidth, renderHeight;


  public static void main(String[] args){
    new SpeechTest().run();
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
        } else if(Game.testLoc == null){
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

    font = new EarthboundFont(1);

    handler = new SpeechHandler();
    currString = JOptionPane.showInputDialog(null, "Please enter text to display.");
    if(currString == null){
      currString = "Lorem ipsum dolor sit amet,\\n"                +
                   "consectetur adipiscing elit. Curabitur\\n"     +
                   "quis nulla pulvinar, congue ipsum\\p"          +
                                                                   
                   "faucibus, malesuada purus.\\n"                 +
                   "Suspendisse volutpat rhoncus nibh,\\n"         +
                   "vel pretium nisl tristique et. Maecenas\\p"    +
                                                                   
                   "ut accumsan urna. Ut lobortis porta\\n"        +
                   "nisl, id interdum enim sollicitudin quis.\\n"  +
                   "Duis rutrum erat nec nibh venenatis,\\p"       +
                                                                   
                   "vel sodales nisi condimentum.\\n"              +
                   "In luctus turpis quis velit malesuada\\n"      +
                   "dictum. Ut finibus enim orci, at lobortis\\p"  +
                                                                   
                   "risus blandit vel. Fusce tincidunt,\\n"        +
                   "metus eu fermentum molestie,\\n"               +
                   "nibh urna placerat ipsum, non laoreet\\p"      +
                                                                   
                   "purus nulla in lacus. Nam a dictum\\n"         +
                   "dolor, eu tristique justo.";
    }
    handler.clear();
    handler.write(currString, font);
    KeyHandler.reloadKeyboard();
  }


  private void getInput(){
    KeyHandler.getInput();

    if (KeyHandler.keyClick(Key.C)) {
      if (handler != null) {
        if (handler.isWaitingOnParagraph()) {
          handler.nextParagraph();
        } else {
          if (!handler.alreadyAsked()) {
            handler.skipScroll();
          } else {
            handler.clear();

          }
        }
      }
    }

  }

  private void update(){
    if(KeyHandler.keyClick(Key.T)){
      String newString = JOptionPane.showInputDialog(null, "Please enter text "+
          "to display.", currString);

      if(newString != null){
        currString = newString;
        handler.clear();
        handler.write(currString, font);
      }
      KeyHandler.reloadKeyboard();
    }
    
    if(KeyHandler.keyClick(Key.S)){
      SpeechHandler.toggleScrolling();
    }

    if(KeyHandler.keyClick(Key.R)){
      handler.clear();
      handler.write(currString, font);
    }
    
    if(KeyHandler.keyClick(Key.P)){
      PortraitSelector selector = new PortraitSelector();
      selector.pack();
      selector.setVisible(true);
      KeyHandler.reloadKeyboard();
    }
    
    if(KeyHandler.keyClick(Key.SLASH)){
      JOptionPane.showMessageDialog(null, "Controls:\n"+
                                          "T: Change text\n"+
                                          "S: Toggle Scrolling On/Off\n"+
                                          "R: Reset Textbox\n"+
                                          "P: Select Portrait");
      KeyHandler.reloadKeyboard();
    }


  }


  private void render(){
    glClear(GL_COLOR_BUFFER_BIT);   //clears the matrix with black
    glLoadIdentity();

    handler.render();

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
  
  /**
   * Allows the user to edit which side a ConnectMap is on
   *
   * @author Aaron Roy
   */
  private class PortraitSelector extends JDialog implements PropertyChangeListener{

    /** It's a mystery what this does.... */
    private static final long serialVersionUID = 1695317195087331220L;
    
    private JOptionPane options;
    private String btnCreate = "Create";
    private String btnCancel = "Cancel";

    private Object[] buttons = {btnCreate, btnCancel};

    private JComboBox<Portrait> portrait = new JComboBox<Portrait>(Portrait.values());
    private Box sideBox = Box.createHorizontalBox();
    {
      sideBox.add(new JLabel("Portrait: "));
      sideBox.add(portrait);
    }
    

    public PortraitSelector(){
      super((Frame)null, true);
      setLocationRelativeTo(null);
      setAlwaysOnTop(true);
      setResizable(false);

      setTitle("Select Portrait");
      

      options = new JOptionPane(sideBox, JOptionPane.PLAIN_MESSAGE, 
          JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);

      addComponentListener(new ComponentAdapter() {
        public void componentShown(ComponentEvent ce){
          portrait.requestFocusInWindow();
        }
      });

      setContentPane(options);
      options.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
      String prop = e.getPropertyName();

      if(e.getSource() == options && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
          JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))){
        Object value = options.getValue();

        if(value == JOptionPane.UNINITIALIZED_VALUE){
          //ignore reset
          return;
        }

        //Reset the JOptionPane's value.
        //If you don't do this, then if the user
        //presses the same button next time, no
        //property change event will be fired.
        options.setValue(JOptionPane.UNINITIALIZED_VALUE);

        //DEBUG Affirm Create button works
        System.out.println("Create works");

        handler.setPortrait((Portrait)portrait.getSelectedItem());
        clearAndHide();
      }
    }

    /** This method clears the dialog and hides it. */
    private void clearAndHide() {
      setVisible(false);
      dispose();
    }

  }


}
