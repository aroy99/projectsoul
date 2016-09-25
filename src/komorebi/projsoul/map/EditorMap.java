/**
 * Map.java    May 30, 2016, 11:32:19 AM
 */

package komorebi.projsoul.map;

import static komorebi.projsoul.engine.KeyHandler.button;
import static komorebi.projsoul.engine.KeyHandler.controlDown;

import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.editor.modes.MoveMode;
import komorebi.projsoul.editor.modes.NPCMode;
import komorebi.projsoul.editor.modes.TileMode;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.KeyHandler.Control;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.script.AreaScript;
import komorebi.projsoul.script.TalkingScript;
import komorebi.projsoul.script.WalkingScript;
import komorebi.projsoul.script.WarpScript;

import org.lwjgl.opengl.Display;

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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Represents a map of tiles
 * 
 * @author Aaron Roy
 */
public class EditorMap implements Playable, Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 3907867851725270089L;
  //Mouse buttons

  //Arrow keys
  private transient boolean up, down, left, right;        //Directions for movement

  //Special commands
  private boolean isSave;              //Save the map
  private boolean isNewSave;        //Saves a new map file
  private boolean isEncSave;        //Saves an encrypted map file
  private boolean isReset;            //Resets tiles position
  private boolean isGrid;              //Turn on/off Grid

  //Map States
  public static boolean grid;                //Whether the grid is on or not
  private static boolean saved = true;


  private static TileList[][] tiles;                //The Map itself
  private boolean[][] collision;


  public static final int SIZE = 16;         //Width and height of a tile

  private ArrayList<NPC> npcs;
  private ArrayList<AreaScript> scripts;

  private static float x, y;       //Current location
  private static float dx, dy;
  private static final float SPEED = 20;

  private String path;  //Path to save the map by default
  private String name;  //Name to save the map by default

  public static final int WIDTH = Display.getWidth();
  public static final int HEIGHT = Display.getHeight();
  
  private TileMode tileMode;
  private MoveMode moveMode;
  private NPCMode npcMode;

  /**
   * The various modes this map can be in
   * 
   * @author Aaron Roy
   */
  public enum Modes{
    TILE, MOVE, NPC;
  }

  private static Modes mode = Modes.TILE;

  /**
   * Creates a new Map of the dimensions col x row
   * @param col number of columns (x)
   * @param row number of rows (y)
   */
  public EditorMap(int col, int row){
    saved = true;
    tiles = new TileList[row][col];
    collision = new boolean[row][col];
    npcs = new ArrayList<NPC>();
    scripts = new ArrayList<AreaScript>();

    Display.setTitle("Project Soul Editor - "+ "Untitled Map");

    for (int i = tiles.length-1; i >= 0; i--) {
      for (int j = 0; j < tiles[0].length; j++) {
        tiles[i][j] = TileList.BLANK;
      }
    }

    Mode.setMap(tiles);
    
    tileMode = new TileMode();
    moveMode = new MoveMode(collision);
    npcMode = new NPCMode(npcs, scripts);
    Mode.setMap(tiles);
    
    tileMode = new TileMode();
    moveMode = new MoveMode(collision);
    npcMode = new NPCMode(npcs, scripts);
  }


  /**
   * Creates a map from a map file, used for the Editor
   * 
   * @param key The location of the map
   * @param name The name of the file
   */
  public EditorMap(String key, String name){
    
    saved = true;

    try {
      BufferedReader reader = new BufferedReader(new FileReader(
          new File(key)));

      int rows = Integer.parseInt(reader.readLine());
      int cols = Integer.parseInt(reader.readLine());

      tiles = new TileList[rows][cols];
      collision = new boolean[rows][cols];
      npcs = new ArrayList<NPC>();
      scripts = new ArrayList<AreaScript>();


      for (int i = 0; i < tiles.length; i++) {
        String[] str = reader.readLine().split(" ");
        int index = 0;
        for (int j = 0; j < cols; j++, index++) {
          if(str[index].equals("")){
            index++;  //pass this token, it's blank
          }
          tiles[i][j] = TileList.getTile(Integer.parseInt(str[index]));
        }
      }

      String s = reader.readLine();

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
      }


      path = key;
      this.name = name;

      Display.setTitle("Project Soul Editor - "+name);

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
        }
      } while ((s=reader.readLine()) != null);


      reader.close();
      
    } catch (IOException | NumberFormatException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, 
          "The file was not found / was corrupt, therefore, the " + 
          "default settings were used");
      tiles = new TileList[10][10];


      for (int i = tiles.length-1; i >= 0; i--) {
        for (int j = 0; j < tiles[0].length; j++) {
          tiles[i][j] = TileList.BLANK;
        }
      }

      KeyHandler.reloadKeyboard();
    }         
    
    
    
    Mode.setMap(tiles);
    
    tileMode = new TileMode();
    moveMode = new MoveMode(collision);
    npcMode = new NPCMode(npcs, scripts);

  }

  @Override
  public void getInput() {

    //Makes sure that up and down / left and right can't be both true
    up =   button(Control.MAP_UP)    && !button(Control.MAP_DOWN);

    down = button(Control.MAP_DOWN)  && 
        !(button(Control.MAP_UP) || controlDown());


    left = button(Control.MAP_LEFT)  && !button(Control.MAP_RIGHT);

    right =button(Control.MAP_RIGHT) && !button(Control.MAP_LEFT);

    isReset = button(Control.RESET_LOC);

    isSave = button(Control.SAVE);

    isNewSave = button(Control.NEW_SAVE);

    //    System.out.println(isSave + ", " + isNewSave);

    isGrid = button(Control.GRID);
        
    Mode.getModeInput();
    tileMode.getInput();
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

    if(KeyHandler.shiftDown()){
      dx *= .1;
      dy *= .1;
    }
        
    if(isReset){
      x = 0;
      y = 0;
    }

    if(button(Control.MOVE_SET)){
      mode = Modes.MOVE;
    }

    switch(mode){
      case MOVE:
        moveMode.update();
        break;
      case NPC:
        npcMode.update();
        break;
      case TILE:
        tileMode.update();
        break;
      default:
        break;
    }
    if(isReset){
      x = 0;
      y = 0;
    }
    
    move(dx, dy);
    
    dx = 0;
    dy = 0;

  }


  /**
   * Saves the map
   */
  public boolean save() {
    PrintWriter writer;

    try {
      if(path.substring(path.length()-4).equals(".map")){
        writer = new PrintWriter(path, "UTF-8");
      }else{
        writer = new PrintWriter(path+".map", "UTF-8");
      }

      writer.println(tiles.length);
      writer.println(tiles[0].length);

      //The map itself
      for (TileList[] tile : tiles) {
        for (TileList t : tile) {
          writer.print(t.getID() + " ");
        }
        writer.println();
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
      
      saved = true;
      writer.close();
      if(name.substring(name.length()-4).equals(".map")){
        Display.setTitle("Project Soul Editor - " + name);
      }else{
        Display.setTitle("Project Soul Editor - " + name + ".map");
      }


      return true;
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      e.printStackTrace();
      return false;
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
      private static final long serialVersionUID = 3881189161552826430L;

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
      //TODO Debug
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

  public int getWidth(){
    return tiles[0].length;
  }

  public int getHeight(){
    return tiles.length;
  }

  public static float getX() {
    return x;
  }

  public String getPath() {
    return path;
  }

  public String getName() {
    return name;
  }

  public static float getY() {
    return y;
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
   * @return Whether the tile is on the map
   */
  public static boolean checkTileInBounds(float x, float y) {
    return x+SIZE > 0 && x < WIDTH-SIZE*8 && y+SIZE > 0 && y < HEIGHT;
  }

  public static TileList[][] getMap(){
    return tiles;
  }


  @Override
  public void render() {
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        if(checkTileInBounds(x+j*SIZE, y+i*SIZE)){
          Draw.rect(x+j*SIZE, y+i*SIZE, SIZE, SIZE, tiles[i][j].getX(), 
              tiles[i][j].getY(), 1);
        }
      }
    }

    
    if(mode == Modes.NPC){
      for (NPC npc: npcs) {
        if(checkTileInBounds(npc.getX(), npc.getY())){
          npc.render();
        }
      }

      for (AreaScript script: scripts) {
        if(checkTileInBounds(script.getX(), script.getY())){
          script.render();
        }
      }
    }
    
    if(grid){
      for (int i = 0; i < tiles.length; i++) {
        for (int j = 0; j < tiles[0].length; j++) {
          if(checkTileInBounds(x+j*SIZE, y+i*SIZE)){
            Draw.rect(x+j*SIZE, y+i*SIZE, SIZE, SIZE, 0, 16, SIZE, 16+SIZE, 2);
          }
        }
      }
    }
    
    switch(mode){
      case MOVE:
        moveMode.render();
        break;
      case NPC:
        npcMode.render();
        break;
      case TILE:
        tileMode.render();
        break;
      default:
        break;
    }

  }

  /**
   * Moves the entire map and all entities contained by it by the specified amount
   * 
   * @param dx pixels to move left/right
   * @param dy pixels to move up/down
   */
  public void move(float dx, float dy) {

    x+=dx;
    y+=dy;

    for(NPC npc:npcs){
      npc.setPixLocation((int)(npc.getX()+dx)+npc.getXTravelled(), 
          (int)(npc.getY()+dy)+npc.getYTravelled());
      npc.update();
    }

    for(AreaScript script:scripts){
      script.setAbsoluteLocation(script.getX()+dx,script.getY()+dy);
    }

  }
  

}

