/**
 * Map.java    May 30, 2016, 11:32:19 AM
 */

package komorebi.projsoul.map;

import static komorebi.projsoul.engine.KeyHandler.button;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.opengl.Display;

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.audio.Song;
import komorebi.projsoul.editor.Layer;
import komorebi.projsoul.editor.Layer.LayerType;
import komorebi.projsoul.editor.LayerControl;
import komorebi.projsoul.editor.Sublayer;
import komorebi.projsoul.editor.World;
import komorebi.projsoul.editor.modes.ConnectMode;
import komorebi.projsoul.editor.modes.EventMode;
import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.editor.modes.MoveMode;
import komorebi.projsoul.editor.modes.TileMode;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.KeyHandler.Control;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.entities.SignPost;
import komorebi.projsoul.entities.enemy.Chaser;
import komorebi.projsoul.entities.enemy.Dummy;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.entities.enemy.EnemyType;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.script.AreaScript;
import komorebi.projsoul.script.TalkingScript;
import komorebi.projsoul.script.WalkingScript;
import komorebi.projsoul.script.WarpScript;

/**
 * Represents a map of tiles for use by the Editor
 * 
 * @author Aaron Roy
 */
public class EditorMap implements Playable, Serializable{

  /** I will probably never bother to understand what this does... */
  private static final long serialVersionUID = 3907867851725270089L;

  //Arrow keys
  private transient boolean up, down, left, right;        //Directions for movement

  //Special commands
  private boolean isSave;              //Save the map
  private boolean isNewSave;        //Saves a new map file
  private boolean isEncSave;        //Saves an encrypted map file
  private boolean isReset;            //Resets tiles position
  private boolean isGrid;              //Turn on/off Grid
  private boolean startDragging;                //Starting a group selection
  private boolean isDragging;                   //Is making a group selection
  private boolean isSelection;                  //A selection is active

  //Map States
  public static boolean grid;                //Whether the grid is on or not
  private static boolean saved = true;


  //private static int[][] tiles;                //The Map itself
  private static boolean[][] collision;

  private static String title;                 //The in-game name of this map
  private static Song song;                    //The song this map uses
  private static boolean outside;

  public static final int SIZE = 16;         //Width and height of a tile

  private static ArrayList<NPC> npcs;              //Events
  private static ArrayList<AreaScript> scripts;    //^
  private static ArrayList<Enemy> enemies;         //^
  private static ArrayList<SignPost> signs;        //^

  private static ArrayList<ConnectMap> maps;   //Maps that connect to this one
  private ConnectMap myMap;

  private static float x, y;       //Current location
  private static float dx, dy;
  private static final float SPEED = 20;

  private String path;  //Path to save the map by default
  private String name;  //Name to save the map by default

  public static final int WIDTH = Display.getWidth();
  public static final int HEIGHT = Display.getHeight();

  private TileMode tileMode;
  private MoveMode moveMode;
  private EventMode eventMode;
  private ConnectMode connectMode;

  private static boolean disableUpdate;

  private LayerControl layers;
  private Sublayer curr;

  private static int height, width;

  /**
   * The various modes this map can be in
   * 
   * @author Aaron Roy
   */
  public enum Modes{
    TILE, MOVE, EVENT, CONNECT;
  }

  private static Modes mode = Modes.TILE;

  /**
   * Creates a new Map of the dimensions col x row
   * @param col number of columns (x)
   * @param row number of rows (y)
   */
  public EditorMap(int col, int row){
    saved = true;
    //tiles = new int[row][col];
    collision = new boolean[row][col];
    npcs = new ArrayList<NPC>();
    scripts = new ArrayList<AreaScript>();
    enemies = new ArrayList<Enemy>();
    signs = new ArrayList<SignPost>();
    maps = new ArrayList<ConnectMap>();


    Display.setTitle("Clyde\'s Editor - "+ "Untitled Map");

    /*
    for (int i = tiles.length-1; i >= 0; i--) {
      for (int j = 0; j < tiles[0].length; j++) {
        tiles[i][j] = Draw.BLANK_TILE;
      }
    }*/

    tileMode = new TileMode();
    moveMode = new MoveMode(collision);
    eventMode = new EventMode(npcs, scripts, enemies, signs);

    layers = new LayerControl();

    curr = layers.getFirst();

    //connectMode = new ConnectMode(World.getWorld());
    //Mode.setMap(tiles);

  }


  /**
   * Creates a map from a map file, used for the Editor
   * 
   * @param key The location of the map
   * @param name The name of the file
   */
  public EditorMap(String key, String name){

    layers = new LayerControl();
    
    saved = true;

    npcs = new ArrayList<NPC>();
    scripts = new ArrayList<AreaScript>();
    enemies = new ArrayList<Enemy>();
    signs = new ArrayList<SignPost>();

    maps = new ArrayList<ConnectMap>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(
          new File(key)));
      
      BufferedReader subReader = new BufferedReader(new FileReader(
          new File(key.substring(0, key.indexOf("maps/")+5) + 
              "data/" + 
              key.substring(key.indexOf("maps/")+5, key.indexOf(".map")) +
              ".edt")));

      int rows = Integer.parseInt(reader.readLine());
      int cols = Integer.parseInt(reader.readLine());

      height = rows;
      width = cols;

      layers.clear();

      //tiles = new int[rows][cols];
      collision = new boolean[rows][cols];


      reader.mark(50);

      String test = reader.readLine();

      if(!test.substring(0, 1).matches("\\d")){
        title = test;
        song = Song.getSong(reader.readLine());
        if(song == null){
          song = Song.NONE;
        }
        outside = Integer.parseInt(reader.readLine()) == 1?true : false;
      }else{
        reader.reset();
      }
      
      String read;
      Sublayer edit = null;
      int layerNum = -1;
      
      while ((read = subReader.readLine())!=null)
      {
        System.out.println(read + ", " + read.startsWith("~"));
        
        if (read.startsWith("#"))
          layerNum++;
        else if (read.startsWith("~"))
        {
          edit = new Sublayer(0, LayerType.layerNumber(layerNum), 
              read.replace("~", ""));
          edit.setTiles(new int[height][width]);
          layers.getLayers()[layerNum].getSubs().add(edit);
        } else
        {
          String[] split = read.split(" ");
          int i = height - 1, j = 0;
          
          for (String term: split)
          {
            if (term.isEmpty())
              continue;
            
            int times;
            if (term.contains("^"))
            {
              times = Integer.valueOf(term.substring(term.indexOf("^") + 1));
              term = term.substring(0, term.indexOf("^"));
            }
            else
              times = 1;
              
            for (int repeat = 0; repeat < times; repeat++)
            {
              System.out.println(edit);              
              
              edit.getTiles()[i][j] = Integer.valueOf(term);
              j++;
              if (j >= width)
              {
                j = 0;
                i--;
              }
            }
          }
          
        }
      }

      /*
      for (int i = 0; i < tiles.length; i++) {
        String[] str = reader.readLine().split(" ");
        int index = 0;
        for (int j = 0; j < cols; j++, index++) {
          if(str[index].equals("")){
            index++;  //pass this token, it's blank
          }
          tiles[i][j] = Integer.parseInt(str[index]);
        }
      }*/

      String s = reader.readLine();

      /*
      for (int i = 0; i < tiles.length; i++) {
        if(s == null || s.startsWith("npc")){
          break;
        }
        if(i != 0){
          s = reader.readLine();
        }
        String[] str = s.split(" ");
        int index = 0;
        for (int j = 0; j < cols; j++, index++) {
          if(str[index].equals("")){
            index++;  //pass this token, it's blank
          }
          collision[i][j]=str[index].equals("1")?true : false;
        }
      }*/


      path = key;
      this.name = name;

      Display.setTitle("Clyde\'s Editor - "+name);

      do
      {
        if(s == null){
          break;
        }

        if (s.startsWith("npc"))
        {
          s = s.replace("npc ", "");
          String[] split = s.split(" ");

          int arg0 = Integer.parseInt(split[1]);
          int arg1 = Integer.parseInt(split[2]);

          NPC addee = new NPC(split[0], x+arg0*16, y+arg1*16, NPCType.toEnum(split[3]));


          addee.setWalkingScript(
              new WalkingScript(split[4], addee));
          addee.setTalkingScript(
              new TalkingScript(split[5], addee));

          npcs.add(addee);

        } else if (s.startsWith("script"))
        {
          s = s.replace("script ", "");
          String[] split = s.split(" ");

          int arg0 = Integer.parseInt(split[1]);
          int arg1 = Integer.parseInt(split[2]);

          scripts.add(new AreaScript(split[0], x+arg0*16, y+arg1*16, 
              false, findNPC(split[3])));

        } else if (s.startsWith("warp"))
        {
          s = s.replace("warp ", "");
          String[] split = s.split(" ");

          int arg0 = Integer.parseInt(split[1]);
          int arg1 = Integer.parseInt(split[2]);

          scripts.add(new WarpScript(split[0], x+arg0*16, y+arg1*16, false));
        } else if (s.startsWith("enemy")){
          s = s.replace("enemy ", "");
          String[] split = s.split(" ");

          int arg0 = Integer.parseInt(split[0]);
          int arg1 = Integer.parseInt(split[1]);

          switch(split[3]){
            case "none":
              enemies.add(new Dummy(x+arg0*16, y+arg1*16, EnemyType.toEnum(split[2]), 1));
              break;
            case "chaser":
              enemies.add(new Chaser(x+arg0*16, y+arg1*16, EnemyType.toEnum(split[2]),
                  Integer.parseInt(split[4])));
              break;
            default:
              System.out.println("This shouldn't happen!");
              break;
          }
        } else if (s.startsWith("sign")){
          s = s.replace("sign ", "");
          String[] split = s.split(" ", 3);

          int arg0 = Integer.parseInt(split[0]);
          int arg1 = Integer.parseInt(split[1]);

          signs.add(new SignPost(x+arg0*16, y+arg1*16, split[2]));

        } /*else if (s.startsWith("connect")){
          s = s.replace("connect ", "");
          String[] split = s.split(" ");


          ConnectMap newMap = (new ConnectMap("res/maps/"+split[0]+".map", split[0], 
              Side.toEnum(split[1])));

          newMap.setLoc(x+Integer.parseInt(split[2])*SIZE, y+Integer.parseInt(split[3])*SIZE);

          maps.add(newMap);
        }*/

      } while ((s=reader.readLine()) != null);


      reader.close();

    } catch (IOException | NumberFormatException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, 
          "The file was not found, was corrupt, or used an outdated map format, " +
          "therefore, some things might not be right");

      KeyHandler.reloadKeyboard();
      collision = new boolean[10][10];
      height = width = 10;
      
      for (Layer l: layers.getLayers())
      {
        for (Sublayer s: l.getSubs())
        {
          s.setTiles(new int[height][width]);
        }
      }
    
    }

    tileMode = new TileMode();
    moveMode = new MoveMode(collision);
    eventMode = new EventMode(npcs, scripts, enemies, signs);

    //myMap = new ConnectMap(key);

    curr = layers.getFirst();

    Mode.setSize(width, height);

    for (Layer l: layers.getLayers())
    {
      l.alignSublayers();
    }

    try
    {
      connectMode = new ConnectMode(World.findWorldContainingMap(key));
    } catch (NoSuchElementException e)
    {
      connectMode = new ConnectMode();
    }
    //Mode.setMap(tiles);
  }

  @Override
  public void getInput() {

    //Makes sure that up and down / left and right can't be both true
    up =   button(Control.MAP_UP)    && !button(Control.MAP_DOWN);

    down = button(Control.MAP_DOWN)  && 
        !(button(Control.MAP_UP) || KeyHandler.keyDown(Key.CTRL));


    left = button(Control.MAP_LEFT)  && !button(Control.MAP_RIGHT);

    right =button(Control.MAP_RIGHT) && !button(Control.MAP_LEFT);

    isReset = button(Control.RESET_LOC);

    isSave = button(Control.SAVE);

    isNewSave = button(Control.NEW_SAVE);

    //    System.out.println(isSave + ", " + isNewSave);


    Mode.getModeInput();
    tileMode.getInput();
    isGrid = button(Control.GRID);
  }

  @Override
  public void update(){
    //Resets tiles to default position
    if(isSave){
      if(path == null){
        newSave();
      }else{
        save();
      }
    }

    if(isNewSave){
      newSave();
    }


    if(isGrid){
      changeGrid();
    }

    if(up){
      dy =  SPEED;
    }
    if(down){
      dy = -SPEED;
    }
    if(right){
      dx =  SPEED;
    }
    if(left){
      dx = -SPEED;
    }


    if(button(Control.TILE)){
      mode = Modes.TILE;
    }
    if(button(Control.MOVE_SET)){
      mode = Modes.MOVE;
    }
    if(button(Control.EVENT)){
      mode = Modes.EVENT;
    }
    if(button(Control.CONNECT)){
      mode = Modes.CONNECT;
    }

    if(button(Control.HEADER)){
      editMapHeader();
    }

    switch(mode){
      case MOVE:
        moveMode.update();
        break;
      case EVENT:
        eventMode.update();
        break;
      case TILE:
        tileMode.update();
        break;
      case CONNECT:
        connectMode.update();
        break;
      default:
        break;
    }
    if(isReset){
      dx = -x;
      dy = -y;
    }

    if (!disableUpdate)
    {
      move(dx, dy);
    }

    dx = 0;
    dy = 0;

    layers.update();

  }


  @Override
  public void render() {
    if (mode!=Modes.CONNECT)
    {
      Draw.rect(x, y, width*16, height*16, 98, 105, 99, 106, 2);

      for (Layer l: layers.getLayers())
      {
        for (int i = l.getSubs().size() - 1; i >= 0; i--)
        {
          if (l.getSubs().get(i).getRadioButton().isChecked())
          {
            l.getSubs().get(i).showTiles();
          }
        }
      }

    }

    switch(mode){
      case MOVE:
        moveMode.render();
        break;
      case EVENT:
        eventMode.render();
        break;
      case TILE:
        tileMode.render();
        break;
      case CONNECT:
        connectMode.render();
        break;
      default:
        break;
    }

    layers.render();

    if (layers.layerContaining(curr)!=null && 
        !layers.layerContaining(curr).getExpandArrow().pointsDown())
      Draw.rect(6, curr.getY(), 8, 14, 27, 108, 31, 115, 2);

  }

  /**
   * Renders the grid of the map
   */
  public static void renderGrid(){
    if(grid){

      for (int i = 0; i < collision.length; i++) {
        for (int j = 0; j < collision[0].length; j++) {
          if(checkTileInBounds(x+j*SIZE, y+i*SIZE)){
            Draw.rect(x+j*SIZE, y+i*SIZE, SIZE, SIZE, 0, 16, SIZE, 16+SIZE, 2);
          }
        }
      }
    }
  }

  /**
   * Creates a new map header dialog
   */
  public static void editMapHeader(){
    EditMapHeader header = new EditMapHeader();
    header.pack();
    header.setVisible(true);
    setUnsaved();
  }


  /**
   * Saves the map
   */
  public boolean save() {

    if (mode != Modes.CONNECT)
    {
      PrintWriter writer, subWriter;
      String subpath;

      try {
        if(!path.substring(path.length()-4).equals(".map")){
          path = path + ".map";
        }
                
        subpath = path.substring(0, path.indexOf("maps\\")+5) + 
            "data/" + 
            path.substring(path.indexOf("maps\\")+5, path.indexOf(".map")) +
            ".edt";

        writer = new PrintWriter(path, "UTF-8");
        subWriter = new PrintWriter(subpath, "UTF-8");
        
        writer.println(collision.length);
        writer.println(collision[0].length);

        writer.println(title);
        writer.println(song);
        writer.println(outside?1 : 0);

        Sublayer[] flats = layers.getFlattenedLayers();

        for (int layer = 0; layer < flats.length; layer++)
        {
          writer.println("#" + LayerType.layerNumber(layer).toString());

          if (flats[layer] == null)
          {
            writer.println("0^"+(height*width)+" ");
          } else
          {
            writer.println(condenseData(flats[layer].getTiles()));
          }
        }
        
        
        
        for (int layer = 0; layer < layers.getLayers().length; layer++)
        {
          subWriter.println("#" + LayerType.layerNumber(layer).toString());
          
          for (Sublayer s: layers.getLayers()[layer].getSubs())
          {
            subWriter.println("~" + s.getTextField().getText());
            subWriter.println(condenseData(s.getTiles()));
          }
        }





        //The collision
        for (boolean[] tile : collision) {
          for (boolean t : tile) {
            writer.print((t?1 : 0) + " ");
          }
          writer.println();
        }

        //The NPCs
        for(NPC npc: npcs){
          writer.println("npc " + npc.getName() + " " + npc.getOrigTX() + 
              " " + npc.getOrigTY() + " " + npc.getType() + " " + 
              npc.getWalkingScript().getScript() + " " + npc.getTalkingScript().getScript());
        }

        //The Scripts and Warps
        for(AreaScript script: scripts){
          if(script instanceof WarpScript){
            writer.println("warp " + ((WarpScript)script).getMap() + " " + 
                script.getOrigTX() + " " + script.getOrigTY());
          }else{
            writer.println("script " + script.getName() + " " + script.getOrigTX() + 
                " " + script.getOrigTY() + 
                (script.hasNPC()?" " + script.getNPC().getName() : ""));
          }
        }

        for(Enemy enemy:enemies){
          writer.println("enemy " + enemy.getOrigTX() + 
              " " + enemy.getOrigTY() + " " + enemy.getType() + " " + 
              enemy.getBehavior() + 
              (enemy instanceof Chaser?" " +  ((Chaser)enemy).getOriginalRadius() : ""));
        }

        for(SignPost sign:signs){
          writer.println("sign " + sign.getOrigTX() + 
              " " + sign.getOrigTY() + " " + sign.getText());
        }

        for(ConnectMap map: maps){
          writer.println("connect " + map.getName() + " " + map.getSide() + " " +
              map.getTileX() + " " + map.getTileY());
        }

        saved = true;
        writer.close();
        subWriter.close();
        if(name.substring(name.length()-4).equals(".map")){
          Display.setTitle("Clyde\'s Editor - " + name);
        }else{
          Display.setTitle("Clyde\'s Editor - " + name + ".map");
        }


        return true;
      } catch (FileNotFoundException | UnsupportedEncodingException e) {
        e.printStackTrace();
        return false;
      }
    } else
    {
      return connectMode.saveMyMaps();

    }



  }

  public boolean encryptedSave(String path, String name)
  {
    try
    {
      String loc = path;
      if (!loc.substring(loc.length()-5).equals(".mapx"))
      {
        loc = loc + ".mapx";
      }
      FileOutputStream fileOut = new FileOutputStream(loc);
      ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
      objOut.writeObject(this);

      objOut.close();
      fileOut.close();

      return true;

    } catch (IOException e)
    {
      e.printStackTrace();
    }

    return false;
  }


  /**
   * Saves the Map file
   * 
   * @return true if the save completed successfully, false if not
   */
  public boolean newSave() {

    JFileChooser chooser = new JFileChooser("res/maps/"){
      /**
       * I don't know what this does, but it does something...
       */
      private static final long serialVersionUID = 3881189161552826430L;

      @Override
      public void approveSelection(){
        File f = getSelectedFile();
        if(f.exists() && getDialogType() == SAVE_DIALOG){
          int result = JOptionPane.showConfirmDialog(this,
              "The file exists, overwrite?",
              "Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
          switch(result){
            case JOptionPane.YES_OPTION:
              super.approveSelection();
              return;
            case JOptionPane.NO_OPTION:
              return;
            case JOptionPane.CLOSED_OPTION:
              return;
            case JOptionPane.CANCEL_OPTION:
              cancelSelection();
              return;
            default:
              return;
          }
        }
        super.approveSelection();
      }
    };

    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Map Files", "map");
    chooser.setFileFilter(filter);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setDialogTitle("Enter the name of the map to save");
    int returnee = chooser.showSaveDialog(null);

    KeyHandler.reloadKeyboard();

    if(returnee == JFileChooser.APPROVE_OPTION){

      path = chooser.getSelectedFile().getAbsolutePath();
      name = chooser.getSelectedFile().getName();

      return save();
    }

    return false;
  }

  public boolean newEncryptedSave() {

    JFileChooser chooser = new JFileChooser("res/maps/"){
      /**
       * Don't feel too bad Aaron, I have no clue either
       */
      private static final long serialVersionUID = 3881189165152826430L;

      @Override
      public void approveSelection(){
        File f = getSelectedFile();
        if(f.exists() && getDialogType() == SAVE_DIALOG){
          int result = JOptionPane.showConfirmDialog(this,
              "The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
          switch(result){
            case JOptionPane.YES_OPTION:
              super.approveSelection();
              return;
            case JOptionPane.NO_OPTION:
              return;
            case JOptionPane.CLOSED_OPTION:
              return;
            case JOptionPane.CANCEL_OPTION:
              cancelSelection();
              return;
            default:
              return;
          }
        }
        super.approveSelection();
      }
    };

    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Map Files", "mapx");
    chooser.setFileFilter(filter);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setDialogTitle("Enter the name of the map to save");
    int returnee = chooser.showSaveDialog(null);
    /*
    Editor.reloadKeyboard();

    if(returnee == JFileChooser.APPROVE_OPTION){

      savePath = chooser.getSelectedFile().getAbsolutePath();
      saveName = chooser.getSelectedFile().getName();

      return encryptedSave(savePath, saveName);
    }

    return false;
     */
    return false;
  }


  /**
   * Finds the NPC with the requested name
   * 
   * @param s The name to find
   * @return The NPC in question, null if not found
   */
  public NPC findNPC(String s)
  {
    for (NPC npc: npcs) {
      if (npc.getName().equals(s)){
        return npc;
      }
    }

    return null;
  }

  /**
   * Finds the Script with the requested name
   * 
   * @param s The name to find
   * @return The Script in question, null if not found
   */
  public AreaScript getScript(String s)
  {
    for (AreaScript scr: scripts)
    {
      //DEBUG Print out Script name
      System.out.println(scr.getName());
      if (scr.getName().equals(s)){
        return scr;

      }
    }

    return null;
  }

  /**
   * Swtiches the state of the grid of every tile
   */
  public static void changeGrid(){
    grid = !grid;
  }  

  public static int getWidth(){
    return width;
  }

  public static int getHeight(){
    return height;
  }

  public static float getX() {
    return x;
  }

  public static float getY() {
    return y;
  }

  public void setLocation(float x, float y){
    move(x-EditorMap.x, y-EditorMap.y);
  }

  public String getPath() {
    return path;
  }


  public String getName() {
    return name;
  }


  public String getTitle(){
    return title;
  }

  public void setTitle(String newTitle){
    title = newTitle;
  }

  public Song getSong(){
    return song;
  }

  public void setSong(Song newSong){
    song = newSong;
  }

  public boolean isOutside(){
    return outside;
  }

  public void setOutside(boolean isOutside){
    outside = isOutside;
  }

  public static boolean wasSaved(){
    return saved;
  }

  /**
   * Creates the asterisk next to the name indicating the map has changed
   */
  public static void setUnsaved(){
    saved = false;
    if(Display.getTitle().charAt(Display.getTitle().length()-1) != '*'){
      Display.setTitle(Display.getTitle()+"*");
    }
  }

  /**
   * Changes the mode of the editor
   * 
   * @param newMode The mode to switch to
   */
  public static void setMode(Modes newMode){
    mode = newMode;
  }

  /**
   * @param x The x to check in pixels
   * @param y The y to check in pixels
   * 
   * @return Whether the tile is on the map
   */
  public static boolean checkTileInBounds(float x, float y) {
    switch (mode) {
      case TILE:
        return x+SIZE > 0 && x < WIDTH-SIZE*8 && y+SIZE > 0 && y < HEIGHT;
              default:
                return x+SIZE > 0 && x < WIDTH+SIZE && y+SIZE > 0 && y < HEIGHT;
    }
  }

  /*
  public static int[][] getMap(){
    //return tiles;
  }*/

  public static int getPxHeight(){
    return collision.length*SIZE;
  }

  public static int getPxWidth(){
    return collision[0].length*SIZE;
  }


  public static Modes getMode(){
    return mode;
  }
  /**
   * Moves the entire map and all entities contained by it by the specified amount
   * 
   * @param dx pixels to move left/right
   * @param dy pixels to move up/down
   */
  public void move(float dx, float dy) {

    if (mode!=Modes.CONNECT)
    {
      x+=dx;
      y+=dy;

      for(NPC npc:npcs){
        npc.setPixLocation((int)(npc.getX()+dx), (int)(npc.getY()+dy));
        npc.update();
      }

      for(AreaScript script:scripts){
        script.setPixLocation((int)(script.getX()+dx),(int)(script.getY()+dy));
      }

      for (Enemy enemy:enemies) {
        enemy.setPixLocation((int)(enemy.getX()+dx),(int)(enemy.getY()+dy));
      }

      for (SignPost sign:signs) {
        sign.setPixLocation((int)(sign.getX()+dx),(int)(sign.getY()+dy));
      }

      for(ConnectMap map:maps){
        map.setLoc(map.getX()+dx, map.getY()+dy);
      }
    } else
    {
      connectMode.move(dx, dy);
    }


  }

  /**
   * Allows the user to edit the width, height, title, and song of maps
   *
   * @author Aaron Roy
   */
  public static class EditMapHeader extends JDialog implements ActionListener,
  PropertyChangeListener{

    /** Not planning to know what this does... */
    private static final long serialVersionUID = 5657211675232029800L;

    JOptionPane options;
    String btnCreate = "Create";
    String btnCancel = "Cancel";

    Object[] buttons = {btnCreate, btnCancel};

    JTextField newTitle = new JTextField(10);

    Box titleBox = Box.createHorizontalBox();
    {
      titleBox.add(new JLabel("Displayed Name:  "));
      titleBox.add(newTitle);
    }

    JTextField newWidth = new JTextField(3);
    JTextField newHeight = new JTextField(3);

    Box dimensions = Box.createHorizontalBox();
    {
      dimensions.add(new JLabel("Width:  "));
      dimensions.add(newWidth);
      dimensions.add(Box.createHorizontalStrut(30));
      dimensions.add(new JLabel("Height:  "));
      dimensions.add(newHeight);
    }

    JComboBox<Song> songs = new JComboBox<Song>(Song.values());
    JButton play = new JButton("Play");
    JButton stop = new JButton("Stop");

    Box songBox = Box.createHorizontalBox();
    {
      songBox.add(new JLabel("Song: "));
      songBox.add(songs);
      songBox.add(Box.createGlue());
      songBox.add(play);
      songBox.add(stop);
    }
    JCheckBox isOutside = new JCheckBox("Is this map outside?");

    Object[] fullContent = {
        titleBox,   Box.createVerticalStrut(5),
        dimensions, Box.createVerticalStrut(5),
        songBox,    Box.createVerticalStrut(5),
        isOutside
    };

    /**
     * Creates a new map header dialog
     */
    public EditMapHeader(){
      super((Frame)null, false);
      setLocationRelativeTo(null);
      setAlwaysOnTop(true);
      setResizable(false);

      setTitle("Edit Map Header");

      newWidth.setText(Integer.toString(collision[0].length));
      newHeight.setText(Integer.toString(collision.length));
      newTitle.setText(title);
      songs.setSelectedItem(song);
      isOutside.setSelected(outside);

      options = new JOptionPane(fullContent, JOptionPane.PLAIN_MESSAGE,
          JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);

      setContentPane(options);

      options.addPropertyChangeListener(this);

      play.addActionListener(this);
      stop.addActionListener(this);

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

        if(btnCreate.equals(value)){
          if(!newTitle.equals("") && checkNum(newWidth) && checkNum(newHeight) &&
              songs.getSelectedItem() != null){
            title = newTitle.getText();
            song = (Song)songs.getSelectedItem();
            outside = isOutside.isSelected();

            int width = Integer.parseInt(newWidth.getText());
            int height = Integer.parseInt(newHeight.getText());

            int[][] newMap = new int[height][width];
            boolean[][] newCol = new boolean[height][width];
            for(int i = 0; i < height; i++){
              for(int j = 0; j < width; j++){
                if(i < collision.length && j < collision[0].length){
                  //newMap[i][j] = tiles[i][j];
                  newCol[i][j] = collision[i][j];
                }
                else{
                  newMap[i][j] = Draw.BLANK_TILE;
                  newCol[i][j] = true;
                }
              }
            }

            //tiles = newMap;
            collision = newCol;

            clearAndHide();
          }
          else{
            complainIncomplete();
          }
        }else{
          clearAndHide();
        }
      }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if(e.getSource() == options){
        options.setValue(btnCreate);
      }
      if(e.getSource() == play && songs.getSelectedItem() != null){
        AudioHandler.play((Song)songs.getSelectedItem());
      }
      if(e.getSource() == stop){
        AudioHandler.stop();
      }
    }

    /** This method clears the dialog and hides it. */
    public void clearAndHide() {
      newTitle.setText(null);
      newWidth.setText(null);
      newHeight.setText(null);
      songs.setSelectedItem(null);
      isOutside.setSelected(false);

      setVisible(false);
      dispose();
    }

    public boolean checkNum(JTextField text){
      return text.getText().matches("[1-9]\\d{0,2}?");
    }

    /**
     * Complains about problems in input
     */
    public void complainIncomplete(){
      JOptionPane.showMessageDialog(this, "Sorry, make sure every field is " +
          "filled in and formatted correctly (numbers should be between 1-128)", 
          "Please Try Again", JOptionPane.ERROR_MESSAGE);
    }


  }

  public int getUniqueTiles()
  {
    ArrayList<Integer> has = new ArrayList<Integer>();

    /*
    for (int[] row: tiles)
    {
      for (int i: row)
      {
        if (!has.contains(i))
        {
          has.add(i);
        }
      }
    }*/

    return has.size();
  }

  public ArrayList<ConnectMap> getConnectMaps()
  {
    return maps;
  }

  public static void canUpdate(boolean b)
  {
    disableUpdate = !b;
  }

  public LayerControl getLayerControl()
  {
    return layers;
  }

  public Sublayer currentSublayer()
  {
    return curr;
  }

  public void setCurrentSublayer(Sublayer s)
  {
    curr = s;
  }
  
  public static String condenseData(int[][] tiles)
  {
    String string = "";
    int prev = -1, prevCount = 1;

    for (int i = tiles.length - 1; i >= 0; i--)
    {
      for (int j = 0; j <tiles[i].length; j++)
      {
        if (prev != -1 && prev != tiles[i][j])
        {
          if (prevCount == 1)
          {
            string += prev + " ";
          } else
          {
            string += prev + "^" + prevCount + " ";
          }

          prevCount=1;
        }

        if (prev == tiles[i][j])
        {
          prevCount++;
        }

        prev = tiles[i][j];

      }
    }

    string+=prev + "^" + prevCount + " ";
    return string;
  }

}

