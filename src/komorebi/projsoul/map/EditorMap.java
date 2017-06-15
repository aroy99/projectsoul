/**
 * Map.java    May 30, 2016, 11:32:19 AM
 */

package komorebi.projsoul.map;

import static komorebi.projsoul.engine.KeyHandler.button;

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.audio.Song;
import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.editor.Layer;
import komorebi.projsoul.editor.LayerControl;
import komorebi.projsoul.editor.LayerType;
import komorebi.projsoul.editor.Sublayer;
import komorebi.projsoul.editor.controls.TabControl;
import komorebi.projsoul.editor.history.HistoryTab;
import komorebi.projsoul.editor.history.OpenRevision;
import komorebi.projsoul.editor.history.Revision;
import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.editor.modes.MoveMode;
import komorebi.projsoul.editor.modes.MoveMode.Permission;
import komorebi.projsoul.editor.modes.TileMode;
import komorebi.projsoul.editor.modes.connect.ConnectMode;
import komorebi.projsoul.editor.modes.event.AreaScriptEvent;
import komorebi.projsoul.editor.modes.event.EnemyEvent;
import komorebi.projsoul.editor.modes.event.EventMode;
import komorebi.projsoul.editor.modes.event.NPCEvent;
import komorebi.projsoul.editor.modes.event.SignPostEvent;
import komorebi.projsoul.editor.modes.event.WarpScriptEvent;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.KeyHandler.Control;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.entities.enemy.EnemyAI;
import komorebi.projsoul.entities.enemy.EnemyType;
import komorebi.projsoul.script.utils.ClassUtils;

import org.lwjgl.opengl.Display;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

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
  private static Permission[][][] collision;

  private static String title;                 //The in-game name of this map
  private static Song song;                    //The song this map uses
  private static boolean outside;

  public static final int SIZE = 16;         //Width and height of a tile

  private static ArrayList<NPCEvent> npcs;              //Events
  private static ArrayList<AreaScriptEvent> scripts;    //^
  private static ArrayList<EnemyEvent> enemies;         //^
  private static ArrayList<SignPostEvent> signs;        //^
  private static ArrayList<WarpScriptEvent> warps;      //^

  private static ArrayList<ConnectMap> maps;   //Maps that connect to this one
  private ConnectMap myMap;

  private static float x, y;       //Current location
  private static float dx, dy;
  private static final float SPEED = 20;

  private File mapFile; //File map data is saved in
  private File editorFile; //File editor data is saved in
  private String name;  //Name to save the map by default

  public static final int WIDTH = Display.getWidth();
  public static final int HEIGHT = Display.getHeight();

  private TileMode tileMode;
  private MoveMode moveMode;
  private EventMode eventMode;
  private ConnectMode connectMode;

  private static boolean disableUpdate;

  private TabControl tabs;
  private LayerControl layers;
  private HistoryTab history;
  
  private Sublayer curr;

  private static int height, width;
  
  private static final File MAP_FOLDER = new File("res/maps/");
  private static final File MAP_DATA = new File("res/maps/data/");

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
    collision = new Permission[MoveMode.NUM_MOVEMENT_LAYERS][row][col];

    for (int l = 0; l < collision.length; l++)
      for (int i = 0; i < collision[l].length; i++)
      {
        for (int j = 0; j < collision[l][i].length; j++)
        {
          collision[l][i][j] = Permission.TRUE;
        }
      }

    npcs = new ArrayList<NPCEvent>();
    scripts = new ArrayList<AreaScriptEvent>();
    enemies = new ArrayList<EnemyEvent>();
    signs = new ArrayList<SignPostEvent>();
    warps = new ArrayList<WarpScriptEvent>();
    maps = new ArrayList<ConnectMap>();


    Display.setTitle("Clyde\'s Editor - "+ "Untitled Map");

    /*
    for (int i = tiles.length-1; i >= 0; i--) {
      for (int j = 0; j < tiles[0].length; j++) {
        tiles[i][j] = Draw.BLANK_TILE;
      }
    }*/

    layers = new LayerControl();
    history = new HistoryTab();
    tabs = new TabControl();
    
    tabs.addTab(layers);
    tabs.addTab(history);
    tabs.setCurrTab(0);

    curr = layers.getFirst();

    tileMode = new TileMode(curr.getTiles());
    moveMode = new MoveMode(collision);
    eventMode = new EventMode(npcs, scripts, warps, enemies, signs);

    
    //connectMode = new ConnectMode(World.getWorld());
    //Mode.setMap(tiles);
    history.addRevision(new OpenRevision(name));

  }


  /**
   * Creates a map from a map file, used for the Editor
   * 
   * @param key The location of the map (will be assumed to be in res/maps/)
   * @param name The name of the file
   */
  public EditorMap(String key){

    layers = new LayerControl();
    history = new HistoryTab();
    tabs = new TabControl();
    
    tabs.addTab(layers);
    tabs.addTab(history);
    tabs.setCurrTab(0);

    saved = true;

    npcs = new ArrayList<NPCEvent>();
    scripts = new ArrayList<AreaScriptEvent>();
    enemies = new ArrayList<EnemyEvent>();
    signs = new ArrayList<SignPostEvent>();
    warps = new ArrayList<WarpScriptEvent>();

    maps = new ArrayList<ConnectMap>();

    try {
      
      defineSavePath(key);
      
      BufferedReader reader = new BufferedReader(new FileReader(
          mapFile));
      
      BufferedReader subReader = new BufferedReader(new FileReader(
          editorFile));

      int rows = Integer.parseInt(reader.readLine());
      int cols = Integer.parseInt(reader.readLine());

      height = rows;
      width = cols;

      layers.clear();

      //tiles = new int[rows][cols];
      collision = new Permission[MoveMode.NUM_MOVEMENT_LAYERS]
          [rows][cols];


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
        if (read.startsWith("#")) {
          layerNum++;
        } else if (read.startsWith("~"))
        {
          edit = new Sublayer(LayerType.layerNumber(layerNum), 
              read.replace("~", ""));
          edit.setTiles(new int[height][width]);
          layers.getLayers()[layerNum].getSubs().add(edit);
        } else
        {
          String[] split = read.split(" ");
          int i = height - 1, j = 0;

          for (String term: split)
          {
            if (term.isEmpty()) {
              continue;
            }

            int times;
            if (term.contains("^"))
            {
              times = Integer.valueOf(term.substring(term.indexOf("^") + 1));
              term = term.substring(0, term.indexOf("^"));
            } else {
              times = 1;
            }

            for (int repeat = 0; repeat < times; repeat++)
            {
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


      while (!(read = reader.readLine()).equals("--movement permissions"))
      {
      }
      
      read = reader.readLine();


      int i = height - 1, j = 0;

      String[] terms = read.split(" ");

      for (String term: terms)
      {
        int repetitions;

        if (term.contains("^"))
        {
          repetitions = Integer.parseInt(term.substring(
              term.indexOf("^")+1));
          term = term.substring(0, term.indexOf("^"));
        } else
        {
          repetitions = 1;
        }

        for (int rep = 0; rep < repetitions; rep++)
        {
          char[] chars = term.toCharArray();

          if(chars.length == MoveMode.NUM_MOVEMENT_LAYERS){
            for (int l = 0; l < MoveMode.NUM_MOVEMENT_LAYERS; l++)
            {
              collision[l][i][j] = Permission.interpret(chars[l]);
            }
          }

          j++;
          if (j >= width)
          {
            j = 0;
            i--;
          }

        }


      }


      this.name = key + ".map";

      Display.setTitle("Project Soul Editor - "+name);

      do
      {
        if(read == null){
          break;
        }

        if (read.startsWith("npc"))
        {
          read = read.replace("npc ", "");
          String[] split = read.split(" ");

          /*split[0] = name     split[1] = x
            split[2] = y        split[3] = sprite
            split[4] = walking  split[5] = talking*/

          NPCEvent addee = new NPCEvent(NPCType.toEnum(split[3]), split[0],
              split[4], split[5]);

          addee.setTileLocation(Integer.parseInt(split[1]), 
              Integer.parseInt(split[2]));
          npcs.add(addee);

        } else if (read.startsWith("script"))
        {
          read = read.replace("script ", "");
          String[] split = read.split(" ");

          AreaScriptEvent addee;

          //split[0] = name   split[1] = x
          //split[2] = y      split[3] = repeat
          //split[4] = npc

          if (split.length == 4) //no npc
          {
            addee = new AreaScriptEvent(split[0], Boolean.valueOf(split[3]),
                "");
          } else
          {
            addee = new AreaScriptEvent(split[0], Boolean.valueOf(split[3]),
                split[4]);
          }

          addee.setTileLocation(Integer.parseInt(split[1]), 
              Integer.parseInt(split[2]));

          scripts.add(addee);

        } else if (read.startsWith("warp"))
        {
          read = read.replace("warp ", "");
          String[] split = read.split(" ");

          WarpScriptEvent addee = new WarpScriptEvent(split[0],
              Integer.parseInt(split[3]), Integer.parseInt(split[4]));

          addee.setTileLocation(Integer.parseInt(split[1]), 
              Integer.parseInt(split[2]));


          //split[0] = map    //split[1] = x
          //split[2] = y      //split[3] = newX
          //split[4] = newY

          warps.add(addee);
        } else if (read.startsWith("enemy")){
          read = read.replace("enemy ", "");
          String[] split = read.split(" ");

          //enemy 16 16 SATURN chaser 3
          //split[0] = x      split[1] = y
          //split[2] = sprite split[3] = AI
          //split[4] = radius

          EnemyEvent addee = new EnemyEvent(EnemyType.toEnum(split[2]),
              EnemyAI.toEnum(split[3]), Integer.parseInt(split[4]));
          addee.setTileLocation(Integer.parseInt(split[0]), 
              Integer.parseInt(split[1]));


        } else if (read.startsWith("sign")){
          read = read.replace("sign ", "");
          String[] split = read.split(" ", 3);

          //sign 2 18 This is a forest

          SignPostEvent addee = new SignPostEvent(split[2]);
          addee.setTileLocation(Integer.parseInt(split[0]), 
              Integer.parseInt(split[1]));

          signs.add(addee);

        } /*else if (s.startsWith("connect")){
          s = s.replace("connect ", "");
          String[] split = s.split(" ");
          ConnectMap newMap = (new ConnectMap("res/maps/"+split[0]+".map", split[0], 
              Side.toEnum(split[1])));
          newMap.setLoc(x+Integer.parseInt(split[2])*SIZE, y+Integer.parseInt(split[3])*SIZE);
          maps.add(newMap);
        }*/

      } while ((read=reader.readLine()) != null);


      reader.close();

    } catch (IOException | NumberFormatException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, 
          "The file was not found, was corrupt, or used an outdated map format, " +
          "therefore, some things might not be right");

      KeyHandler.reloadKeyboard();
      collision = new Permission[3][10][10];
      for (int l = 0; l < collision.length; l++)
      {
        for (int i = 0; i < collision[l].length; i++)
        {
          for (int j = 0; j < collision[l][i].length; j++)
          {
            collision[l][i][j] = Permission.TRUE;
          }
        }
      }

      height = width = 10;

      for (Layer l: layers.getLayers())
      {
        for (Sublayer s: l.getSubs())
        {
          s.setTiles(new int[height][width]);
        }
      }

    }
    curr = layers.getFirst();
    
    tileMode = new TileMode(curr.getTiles());
    moveMode = new MoveMode(collision);
    eventMode = new EventMode(npcs, scripts, warps, enemies, signs);

    //myMap = new ConnectMap(key);


    Mode.setSize(width, height);

    for (Layer l: layers.getLayers())
    {
      l.alignSublayers();
    }

    connectMode = new ConnectMode();
    
    history.addRevision(new OpenRevision(name));
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
      if(mapFile == null){
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
    
    curr.getRadioButton().setChecked(true);
    tabs.update();
    
    history.updateBackground();

  }


  @Override
  public void render() {
    if (mode != Modes.CONNECT)
    {      
      int widthInBounds = (Math.max((int) (Math.min(x+width*16, WIDTH) - x)/16, 0));
      
      Draw.rectZoom(x, y, widthInBounds*16, height*16, 98, 105, 99, 106, 2, 
          Editor.zoom(), EditorMap.getX(), EditorMap.getY());

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

    tabs.render();

    if (layers.isTabbed() && layers.layerContaining(curr)!=null && 
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
            Draw.rectZoom(x+j*SIZE, y+i*SIZE, SIZE, SIZE, 0, 16, SIZE, 16+SIZE, 2,
                Editor.zoom(), EditorMap.getX(), EditorMap.getY());
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
      PrintWriter mapWriter, editorWriter;
      try {

        mapWriter = new PrintWriter(mapFile);
        editorWriter = new PrintWriter(editorFile);

        mapWriter.println(width);
        mapWriter.println(height);

        mapWriter.println(title);
        mapWriter.println(song);
        mapWriter.println(outside?1 : 0);

        Sublayer[] flats = layers.getFlattenedLayers();

        for (int layer = 0; layer < flats.length; layer++)
        {
          mapWriter.println("#" + LayerType.layerNumber(layer).toString());

          if (flats[layer] == null)
          {
            mapWriter.println("0^"+(height*width)+" ");
          } else
          {
            mapWriter.println(condenseData(flats[layer].getTiles()));
          }
        }



        for (int layer = 0; layer < layers.getLayers().length; layer++)
        {
          editorWriter.println("#" + LayerType.layerNumber(layer).toString());

          for (Sublayer s: layers.getLayers()[layer].getSubs())
          {
            editorWriter.println("~" + s.getTextField().getText());
            editorWriter.println(condenseData(s.getTiles()));
          }
        }

        mapWriter.println("--movement permissions\n" + 
            condenseData(collision));

        //The NPCs
        for(NPCEvent npc: npcs){
          mapWriter.println(npc.toString());
        }

        //The Scripts and Warps
        for(AreaScriptEvent script: scripts){
          mapWriter.println(script.toString());
        }

        for(WarpScriptEvent warp: warps)
        {
          mapWriter.println(warp.toString());
        }

        for(EnemyEvent enemy: enemies){
          mapWriter.println(enemy.toString());
        }

        for(SignPostEvent sign:signs){
          mapWriter.println(sign.toString());
        }

        for(ConnectMap map: maps){
          mapWriter.println("connect " + map.getName() + " " + map.getSide() + " " +
              map.getTileX() + " " + map.getTileY());
        }

        saved = true;
        mapWriter.close();
        editorWriter.close();
        if(name.length() > 4 && name.substring(name.length()-4).equals(".map")){
          Display.setTitle("Clyde\'s Editor - " + name);
        }else{
          Display.setTitle("Clyde\'s Editor - " + name + ".map");
        }


        return true;
      } catch (FileNotFoundException e) {
        e.printStackTrace();
        return false;
      }
    } else
    {
      return connectMode.saveMyMaps();

    }
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

      String mapName = chooser.getSelectedFile().getName();
      
      System.out.println(mapName);
      
      try
      {
        defineSavePath(mapName.replace(".map", ""));
      } catch (FileNotFoundException e)
      {
        System.out.println("This shouldn't happen");
        e.printStackTrace();
      }
      name = chooser.getSelectedFile().getName();

      return save();
    }

    return false;
  }


  /**
   * Finds the NPC with the requested name
   * 
   * @param s The name to find
   * @return The NPC in question, null if not found
   */
  public NPCEvent findNPC(String s)
  {
    for (NPCEvent npc: npcs) {
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
  public AreaScriptEvent getScript(String s)
  {
    for (AreaScriptEvent scr: scripts)
    {
      if (scr.getScript().equals(s)){
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
        return x+SIZE*Editor.zoom() > 0 && x < WIDTH-SIZE*8 && 
            y+SIZE*Editor.zoom() > 0 && y < HEIGHT;
      default:
        return x+SIZE*Editor.zoom() > 0 && x < WIDTH+SIZE && 
            y+SIZE*Editor.zoom() > 0 && y < HEIGHT;
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


      for(NPCEvent npc:npcs){
        npc.setPixLocation((int)(npc.getX()+dx), (int)(npc.getY()+dy));
      }

      for(AreaScriptEvent script:scripts){
        script.setPixLocation((int)(script.getX()+dx),(int)(script.getY()+dy));
      }

      for (EnemyEvent enemy:enemies) {
        enemy.setPixLocation((int)(enemy.getX()+dx),(int)(enemy.getY()+dy));
      }

      for (SignPostEvent sign:signs) {
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
            Permission[][][] newCol = new Permission
                [MoveMode.NUM_MOVEMENT_LAYERS][height][width];
            for (int l = 0; l < MoveMode.NUM_MOVEMENT_LAYERS; l++)
            {
              for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                  if(i < collision.length && j < collision[0].length){
                    //newMap[i][j] = tiles[i][j];
                    newCol[l][i][j] = collision[l][i][j];
                  }
                  else{
                    newMap[i][j] = Draw.BLANK_TILE;
                    newCol[l][i][j] = Permission.TRUE;
                  }
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

    if (prevCount == 1)
    {
      string += prev + " ";
    } else
    {
      string += prev + "^" + prevCount + " ";
    }
    return string;
  }

  public static String condenseData(Permission[][][] perms)
  {
    String string = "";
    String prev = "", curr;
    int prevCount = 1;

    for (int i = perms[0].length - 1; i >= 0; i--)
    {
      for (int j = 0; j < perms[0][i].length; j++)
      {
        curr = "";

        for (int l = 0; l < perms.length; l++)
        {
          curr+=perms[l][j][i].asChar();
        }

        if (!prev.equals("") && !prev.equals(curr))
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

        if (prev.equals(curr))
        {
          prevCount++;
        }

        prev = curr;

      }
    }

    if (prevCount == 1)
    {
      string += prev + " ";
    } else
    {
      string += prev + "^" + prevCount + " ";
    }
    return string;
  }
  
  public void addRevision(Revision revision)
  {
    history.clearBelowCurrent();
    history.addRevision(revision);
  }
  
  public MoveMode getMoveMode()
  {
    return moveMode;
  }
  
  public boolean doesConnectModeHaveWorld()
  {
    return connectMode.hasWorld();
  }
  
  public void defineSavePath(String fileNameNoExtension) throws
                                                          FileNotFoundException
  {
    try
    {
      mapFile = ClassUtils.findFile(MAP_FOLDER, fileNameNoExtension + ".map");
      editorFile = ClassUtils.findFile(MAP_DATA, fileNameNoExtension + ".edt");
    } catch (FileNotFoundException e)
    {
      throw e;
    }
  }
  
  /**
   * @return The name of the map file (sans file extension)
   */
  public String getSimpleName()
  {
    return name.replace(".map", "");
  }
 
  public boolean hasPath()
  {
    return mapFile != null && editorFile != null;
  }
}
