/**
 * Editor.java    May 16, 2016, 10:03:58 PM
 */
package komorebi.projsoul.editor;

import static komorebi.projsoul.engine.KeyHandler.button;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.input.Mouse;

import komorebi.projsoul.audio.Song;
import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.KeyHandler.Control;
import komorebi.projsoul.engine.MainE;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.map.EditorMap;


/**
 * Represents the level editor
 * 
 * @author Aaron Roy
 */
public class Editor implements Playable{

  private boolean isLoad;  //Whether to load a new map or not
  private boolean isNew;    //Whether to create a new map or not
  private boolean isRevert;//Reset the map

  private static float zoom = 1;
  private static float zoomOffX, zoomOffY;
  private static final int MAX_ZOOM = 3;
  private static final float MIN_ZOOM = 0.5f;
  private static EditorMap map;
  private static Buttons buttons;
  
  public static float dWheel;

  /**
   * Creates a new editor with a map that is 20*20
   */
  public Editor(){
    map = new EditorMap("res/maps/"+MainE.testLoc, MainE.testLoc);
    //    map = new EditorMap(20, 20);

    buttons = new Buttons(map);
  }


  @Override
  public void getInput() {
    KeyHandler.getInput();
    //        if(Display.wasResized())resize();
    if(KeyHandler.keyClick(Key.P)) {
      testGame();
    }

    float mx, my;



    dWheel = Mouse.getDWheel();    
    if (KeyHandler.controlDown()) 
    {    
      boolean zoomed = false;

      mx = Mouse.getX()/MainE.getScale();  
      my = Mouse.getY()/MainE.getScale(); 
      float prevZoom = zoom;              

      if (dWheel>0 || KeyHandler.keyClick(Key.PLUS))
      {
        zoom+=0.5;
        if (zoom > MAX_ZOOM)
        {
          zoom = MAX_ZOOM;
        }
        zoomed = true;
      } else if (dWheel<0 || KeyHandler.keyClick(Key.MINUS))
      {
        zoom-=0.5;
        if (zoom < MIN_ZOOM)
        {
          zoom = MIN_ZOOM;
        }
        zoomed = true;
      }

      if (KeyHandler.keyClick(Key.PAD0) || KeyHandler.keyClick(Key.ROW0))
      {
        zoom = 1;
        zoomed = true;
      }
      
      if (zoomed)
      {
        float ex  = EditorMap.getX(), ey = EditorMap.getY();
        float newex = mx - (mx - ex) * (zoom/prevZoom);
        float newey = my - (my - ey) * (zoom/prevZoom);

        map.move(newex - ex, newey - ey);
      }

    }

    isLoad = button(Control.LOAD);

    isRevert = button(Control.REVERT_MAP);

    isNew =  button(Control.NEW);

    map.getInput();
    buttons.getInput();
  }

  @Override
  public void update() {
    if(isLoad){
      loadMap();
    }

    if(isRevert){
      revertMap();
    }

    if(isNew){
      newMap();
    }

    map.update();
    buttons.update();
  }

  @Override
  public void render() {
    map.render();
    buttons.render();
    Mode.renderStatus();
  }


  /**
   * Creates the game in a new window
   */
  public static void testGame() {
    Runtime runTime = Runtime.getRuntime();
    try {
      runTime.exec("java -jar \"RealGame v 0.2.jar\"");
    } catch (IOException e) {
      e.printStackTrace();
    }    
  }


  /**
   * Creates a new map, asking if the user wants to save or not
   */
  public static void newMap(){
    if(requestSave()){
      NewMapDialog dialog = new NewMapDialog();
      dialog.pack();
      dialog.setVisible(true);

      int width = dialog.getActWidth();
      int height = dialog.getActHeight();

      if(width != 0 && height != 0){
        map = new EditorMap(width, height);
        map.setTitle(dialog.getTitleText());
        map.setSong(dialog.getSong());
        map.setOutside(dialog.getOutside());

        buttons.setMap(map);
      }

      KeyHandler.reloadKeyboard();
    }

  }


  /**
   * Loads a map, asking the user if they want to save first
   */
  public static void loadMap(){
    if(requestSave()){
      JFileChooser chooser = new JFileChooser("res/maps/");
      FileNameExtensionFilter filter = new FileNameExtensionFilter(
          "Map Files (.map)", "map");
      chooser.setFileFilter(filter);
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setDialogTitle("Enter the name of the map to load");
      int returnee = chooser.showOpenDialog(null);

      KeyHandler.reloadKeyboard();

      if(returnee == JFileChooser.APPROVE_OPTION){
        map = new EditorMap(chooser.getSelectedFile().getAbsolutePath(), 
            chooser.getSelectedFile().getName());
        buttons.setMap(map);
      }
    }

  }

  /**
   * Loads a new map at the selected location
   * 
   * @param name The name of the map to load (will assume in res/map)
   * @param x The x in pixels
   * @param y The y in pixels
   */
  public static void loadMap(String name, float x, float y){
    if(requestSave()){
      map = new EditorMap("res/maps/" + name + ".map", 
          name + ".map");
      map.setLocation(x, y);
      buttons.setMap(map);
    }

  }


  /**
   * Asks the player if they want to save the map
   */
  public static boolean requestSave() {
    boolean continyu = true;
    if(!EditorMap.wasSaved()){

      int returnee = JOptionPane.showConfirmDialog(null, "Would you like to save?");

      switch(returnee){
        case JFileChooser.APPROVE_OPTION:
          if(map.getPath() == null){
            continyu = map.newSave();
          }else{
            continyu = map.save();
          }
          break;
        case JFileChooser.CANCEL_OPTION:
          continyu = true;
          break;
        default:
          continyu = false;
          break;
      }
    }

    KeyHandler.reloadKeyboard();

    return continyu;    
  }


  /**
   * Reverts the map back to it was before the last save
   */
  public static void revertMap(){
    if(JOptionPane.showConfirmDialog(null, "Are you sure you want to go back " +
        "to your last save? Your work will be lost!") == 
        JOptionPane.YES_OPTION){
      if(map.getPath() != null){
        map = new EditorMap(map.getPath(), map.getName());
        buttons.setMap(map);
      }else{
        map = new EditorMap(map.getWidth(),map.getHeight());
        buttons.setMap(map);
      }
    }
  }

  /**
   * @return true if the current map wasn't edited without saving, false if not
   */
  public static boolean wasSaved(){
    return EditorMap.wasSaved();
  }

  /**
   * The Dialog that is created when Ctrl-N is pressed
   * 
   * @author Aaron Roy
   */
  private static class NewMapDialog extends JDialog implements ActionListener, 
  PropertyChangeListener{
    /**
     * I don't know what this does, but it does something...
     */
    private static final long serialVersionUID = -7199179946667631093L;

    JTextField title = new JTextField(10);

    Box titleBox = Box.createHorizontalBox();
    {
      titleBox.add(new JLabel("Displayed Name:  "));
      titleBox.add(title);
    }

    JTextField width = new JTextField(3);
    JTextField height = new JTextField(3);

    Box dimensions = Box.createHorizontalBox();
    {
      dimensions.add(new JLabel("Width:  "));
      dimensions.add(width);
      dimensions.add(Box.createHorizontalStrut(30));
      dimensions.add(new JLabel("Height:  "));
      dimensions.add(height);
    }

    JComboBox<Song> songs = new JComboBox<Song>(Song.values());

    Box songBox = Box.createHorizontalBox();
    {
      songBox.add(new JLabel("Song: "));
      songBox.add(songs);
      songBox.add(Box.createGlue());
    }
    JCheckBox isOutside = new JCheckBox("Is this map outside?");

    Object[] contents = {
        titleBox,   Box.createVerticalStrut(5),
        dimensions, Box.createVerticalStrut(5),
        songBox,    Box.createVerticalStrut(5),
        isOutside
    };

    String entWidth, entHeight;
    int actWidth, actHeight;
    JOptionPane options;
    String btnCreate = "Create";
    String btnCancel = "Cancel";

    public NewMapDialog(){
      super((Frame)null, true);
      setTitle("Create a New Map");
      setLocationRelativeTo(null);
      setAlwaysOnTop(true);
      setResizable(false);

      Object[] buttons = {btnCreate, btnCancel};

      options = new JOptionPane(contents, JOptionPane.PLAIN_MESSAGE, 
          JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);

      addComponentListener(new ComponentAdapter() {
        public void componentShown(ComponentEvent ce) {
          title.requestFocusInWindow();
        }
      });

      setContentPane(options);

      width.addActionListener(this);
      height.addActionListener(this);

      options.addPropertyChangeListener(this);


    }

    public boolean getOutside() {
      return isOutside.isSelected();
    }

    public Song getSong() {
      return (Song)songs.getSelectedItem();
    }

    public String getTitleText() {
      return title.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if(e.getSource() == options){
        options.setValue(btnCreate);
      }
    }


    @Override
    public void propertyChange(PropertyChangeEvent e) {
      String prop = e.getPropertyName();

      if(e.getSource() == options &&
          (JOptionPane.VALUE_PROPERTY.equals(prop) ||
              JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
        Object value = options.getValue();

        if (value == JOptionPane.UNINITIALIZED_VALUE) {
          //ignore reset
          return;
        }

        //Reset the JOptionPane's value.
        //If you don't do this, then if the user
        //presses the same button next time, no
        //property change event will be fired.
        options.setValue(JOptionPane.UNINITIALIZED_VALUE);

        if(btnCreate.equals(value)){          
          entWidth = width.getText();
          entHeight = height.getText();

          if(entWidth.matches("[1-9]\\d{0,2}?") && 
              entHeight.matches("[1-9]\\d{0,2}?")){
            int tWidth = Integer.parseInt(entWidth);
            int tHeight = Integer.parseInt(entHeight);

            if(tWidth <= 128 && tHeight <= 128){
              actWidth = tWidth;
              actHeight = tHeight;

              if(actWidth > 0 && actHeight > 0){

              }

              clearAndHide();
            }else{
              complain();
            }
          }else{
            complain();
          }

        }else{
          clearAndHide();
        }
      }
    }



    public int getActWidth(){
      System.out.println(actWidth);
      return actWidth;
    }

    public int getActHeight(){
      return actHeight;
    }


    private void complain(){
      JOptionPane.showMessageDialog(this, "Sorry, make sure your numbers are between 1 and 128", 
          "Please Try Again", JOptionPane.ERROR_MESSAGE);
    }

    /** This method clears the dialog and hides it. */
    public void clearAndHide() {
      width.setText(null);
      height.setText(null);
      setVisible(false);
      dispose();
    }

  }

  public static EditorMap getMap()
  {
    return map;
  }

  public static float zoom()
  {
    return zoom;
  }

}


