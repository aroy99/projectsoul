/**
 * ConnectMap.java    Oct 21, 2016, 9:49:03 AM
 */
package komorebi.projsoul.map;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.swing.JOptionPane;

import komorebi.projsoul.editor.World;
import komorebi.projsoul.editor.modes.ConnectMode;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Renderable;

/**
 * A map file that really doesn't have much
 *
 * @author Aaron Roy
 */
public class ConnectMap implements Renderable{

  private ArrayList<ConnectMap> maps = new ArrayList<ConnectMap>();
  private int[][] tiles;
  private int tx, ty;
  private String filePath;
  private Rectangle area;
  
  /**
   * The different sides a map can be on
   *
   * @author Aaron Roy
   */
  public enum Side{
    DOWN, LEFT, UP, RIGHT;
    
    @Override
    public String toString(){
      switch(this){
        case DOWN:  return "down";
        case LEFT:  return "left";
        case RIGHT: return "right";
        case UP:    return "up";
        default:    return "bleh";
      }
    }
    
    /**
     * Takes in a string and returns its respective Side
     * 
     * @param s The input string
     * @return the corespondent Side, null if not found
     */
    public static Side toEnum(String s){
      for(Side side: values()){
        if(side.toString().equals(s)){
          return side;
        }
      }
      
      return null;
    }
  }
  
  private Side side;
  
  private String name;
  
  public static final int SIZE = 16;         //Width and height of a tile

  
  
  /**
   * Creates a map based on the map file given, in reference to a world
   * 
   * @param key The location of the map
   */
  public ConnectMap(World world, String filePath, int x, int y)
  {
    
    area = new Rectangle();

    if (!world.contains(this))
    {
      world.addMap(this);
    }
    
    setTileLocation(x, y);
    this.filePath = filePath;    
    
    try {
      BufferedReader reader = new BufferedReader(new FileReader(
          new File(filePath)));

      int rows = Integer.parseInt(reader.readLine());
      int cols = Integer.parseInt(reader.readLine());

      tiles = new int[rows][cols];
      setSize(tiles[0].length, tiles.length);
      
      this.name = reader.readLine();
      String test = reader.readLine();
      
      if(!test.substring(0, 1).matches("\\d")){
        reader.readLine();
      }else{
        reader.reset();
      }
      
      for (int i = 0; i < tiles.length; i++) {
        String[] str = reader.readLine().split(" ");
        int index = 0;
        for (int j = 0; j < cols; j++, index++) {
          if(str[index].equals("")){
            index++;  //pass this token, it's blank
          }
          tiles[i][j] = Integer.parseInt(str[index]);
        }
      }  
      
      while ((test=reader.readLine())!=null)
      {
        if (test.startsWith("connect")){
          test = test.replace("connect ", "");
          String[] split = test.split(" ");
          
          try
          {
            this.maps.add(world.getMap("res/maps/"+split[0]+".map"));
          } catch (NoSuchElementException e)
          {
            ConnectMap newMap = (new ConnectMap(world, "res/maps/"+split[0]+".map", 
                 x+Integer.parseInt(split[2]), 
                y+Integer.parseInt(split[3])));            
            maps.add(newMap);
          }
        }
      }
      
      
      reader.close();

    }catch (IOException | NumberFormatException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, 
          "The file was not found / was corrupt, therefore, the " + 
          "default settings were used. Please remove this map as it is invalid");
      tiles = new int[10][10];


      for (int i = tiles.length-1; i >= 0; i--) {
        for (int j = 0; j < tiles[0].length; j++) {
          tiles[i][j] = Draw.BLANK_TILE;
        }
      }

      KeyHandler.reloadKeyboard();
    }
    
  }
  
  @Override
  public void update(){
    //There is really nothing to update
  }
  
  @Override
  public void render(){    
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        if(EditorMap.checkTileInBounds(tx*SIZE+j*SIZE+ConnectMode.getX(), 
            ty*SIZE+i*SIZE+ConnectMode.getY())){
          Draw.tile(tx*SIZE+j*SIZE+ConnectMode.getX(), ty*SIZE+i*SIZE+ConnectMode.getY(),
              Draw.getTexX(tiles[i][j]), Draw.getTexY(tiles[i][j]), Draw.getTexture(tiles[i][j]));
          if(EditorMap.grid){
            Draw.rect(tx*SIZE+j*SIZE+ConnectMode.getX(), ty*SIZE+i*SIZE+ConnectMode.getY(), 
                SIZE, SIZE, 0, 16, SIZE, 16+SIZE, 2);
          }
        }
      }
    }
  }
  
  public ConnectMap(String key)
  {
    this.filePath = key;    
    
    area = new Rectangle();
    
    try {
      BufferedReader reader = new BufferedReader(new FileReader(
          new File(filePath)));

      int rows = Integer.parseInt(reader.readLine());
      int cols = Integer.parseInt(reader.readLine());

      tiles = new int[rows][cols];
      setSize(tiles[0].length, tiles.length);
      
      this.name = reader.readLine();
      String test = reader.readLine();
      
      if(!test.substring(0, 1).matches("\\d")){
        reader.readLine();
      }else{
        reader.reset();
      }
      
      for (int i = 0; i < tiles.length; i++) {
        String[] str = reader.readLine().split(" ");
        int index = 0;
        for (int j = 0; j < cols; j++, index++) {
          if(str[index].equals("")){
            index++;  //pass this token, it's blank
          }
          tiles[i][j] = Integer.parseInt(str[index]);
        }
      }
      
      reader.close();
      
      

    }catch (IOException | NumberFormatException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, 
          "The file was not found / was corrupt, therefore, the " + 
          "default settings were used. Please remove this map as it is invalid");
      tiles = new int[10][10];


      for (int i = tiles.length-1; i >= 0; i--) {
        for (int j = 0; j < tiles[0].length; j++) {
          tiles[i][j] = Draw.BLANK_TILE;
        }
      }

      KeyHandler.reloadKeyboard();
    }
  }
  
  public float getX() {
    return tx*16;
  }

  public float getY() {
    return ty*16;
  }
  
  /**
   * @return The x position of the map relative to the parent map in tiles
   */
  public int getTileX(){
    return tx;
  }
  
  /**
   * @return The y position of the map relative to the parent map in tiles
   */
  public int getTileY(){
    return ty;
  }
  
  /**
   * Moves the map to the specified pixel location
   * 
   * @param nx The x to move to
   * @param ny The y to move to
   */
  public void setLoc(float nx, float ny){
    tx = (int) nx/16;
    ty = (int) ny/16;
  }
    
  /**
   * Moves the map to a new x or y dependidng on its side
   * 
   * @param tx The tile x to try
   * @param ty The tile y to try
   */
  public void setTileLocation(int tx, int ty){
      this.tx = tx;
      this.ty = ty;
      area.setLocation(tx, ty);
  }

  
  public Side getSide(){
    return side;
  }

  public void setSide(Side side) {
    this.side = side;
  }
  
  public String getName(){
    return name;
  }
  
  public int getPxHeight(){
    return tiles.length*EditorMap.SIZE;
  }
  
  public int getPxWidth(){
    return tiles[0].length*EditorMap.SIZE;
  }
  
  public boolean equals(ConnectMap connect)
  {
    return (connect.getFilePath().equals(filePath));
  }
  
  public boolean matches(String s) {
    return (s.equals(filePath));
  }
  
  public String getFilePath()
  {
    return filePath;
  }
  
  public boolean hasNewMaps(ArrayList<ConnectMap> arr)
  {    
    for (ConnectMap c: maps)
    {     
      
      if (!arr.contains(c))
      {
        return true;
      }
    }
    
    return false;
  }
  
  public void gather(ArrayList<ConnectMap> arr)
  {
    
    for (ConnectMap c: maps)
    {
      //System.out.println(this.getFilePath() + " connects to " + c.getFilePath());
      
      if (!arr.contains(c))
      {
        arr.add(c);
      }
      
      if (c.hasNewMaps(arr))
      {
        c.gather(arr);
      }
    }
  }
  
  public ArrayList<ConnectMap> map()
  {    
    ArrayList<ConnectMap> collection = new ArrayList<ConnectMap>();
    collection.add(this);
    this.gather(collection);
    return collection;
  }
  
  public int getWidth()
  {
    return tiles[0].length;
  }
  
  public int getHeight()
  {
    return tiles.length;
  }
  
  public Rectangle getArea()
  {
    return area;
  }
  
  private void setSize(int width, int height)
  {
    area.setSize(width, height);
  }
  
}
