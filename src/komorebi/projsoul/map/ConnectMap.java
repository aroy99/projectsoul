/**
 * ConnectMap.java    Oct 21, 2016, 9:49:03 AM
 */
package komorebi.projsoul.map;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Renderable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * A map file that really doesn't have much
 *
 * @author Aaron Roy
 */
public class ConnectMap implements Renderable{

  private TileList[][] tiles;
  private float x, y;
  
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
   * Creates a map based on the map file given
   * 
   * @param key The location of the map
   * @param name The name of the map (used when saving)
   * @param s The side the map is on in respect to the parent EditorMap
   */
  public ConnectMap(String key, String name, Side s){
    try {
      BufferedReader reader = new BufferedReader(new FileReader(
          new File(key)));

      int rows = Integer.parseInt(reader.readLine());
      int cols = Integer.parseInt(reader.readLine());

      tiles = new TileList[rows][cols];
      
      reader.mark(50);

      String test = reader.readLine();

      if(!test.substring(0, 1).matches("\\d")){
        reader.readLine();
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
          tiles[i][j] = TileList.getTile(Integer.parseInt(str[index]));
        }
      }
      
      this.name = name;
      side = s;
      
      reader.close();

    }catch (IOException | NumberFormatException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, 
          "The file was not found / was corrupt, therefore, the " + 
          "default settings were used. Please remove this map as it is invalid");
      tiles = new TileList[10][10];


      for (int i = tiles.length-1; i >= 0; i--) {
        for (int j = 0; j < tiles[0].length; j++) {
          tiles[i][j] = TileList.BLANK;
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
        if(EditorMap.checkTileInBounds(x+j*SIZE, y+i*SIZE)){
          Draw.rect(x+j*SIZE, y+i*SIZE, SIZE, SIZE, tiles[i][j].getX(), 
              tiles[i][j].getY(), 1);
          if(EditorMap.grid){
            Draw.rect(x+j*SIZE, y+i*SIZE, SIZE, SIZE, 0, 16, SIZE, 16+SIZE, 2);
          }
        }
      }
    }
  }
  
  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }
  
  /**
   * @return The x position of the map relative to the parent map in tiles
   */
  public int getTileX(){
    return (int)(x - EditorMap.getX())/16;
  }
  
  /**
   * @return The y position of the map relative to the parent map in tiles
   */
  public int getTileY(){
    return (int)(y - EditorMap.getY())/16;
  }
  
  /**
   * Moves the map to the specified pixel location
   * 
   * @param nx The x to move to
   * @param ny The y to move to
   */
  public void setLoc(float nx, float ny){
    x = nx;
    y = ny;
  }
    
  /**
   * Moves the map to a new x or y depending on its side
   * 
   * @param tx The tile x to try
   * @param ty The tile y to try
   */
  public void setTileLocation(int tx, int ty){
    if(side == Side.UP | side == Side.DOWN){
      x = tx*16+EditorMap.getX();
    }else if(side == Side.LEFT | side == Side.RIGHT){
      y = ty*16+EditorMap.getY();
    }

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

  
}
