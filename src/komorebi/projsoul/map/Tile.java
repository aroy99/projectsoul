/**
 * Tile.java  May 18, 2016, 8:42:07 PM
 */
package komorebi.projsoul.map;

import org.lwjgl.opengl.Display;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Renderable;

/**
 * Use a TileList[][] array instead, it's way more efficient!
 * 
 * @author Aaron Roy
 */
@Deprecated
public class Tile implements Renderable{

  private static boolean grid;        //Whether the grid is on or not

  private float x, y;                 //Location of the tile
  private float dx,dy;                //Amount tile will move each frame
  public static final int SIZE = 16;  //Width and height of a tile
  private TileList type;              //This tile's type
  private static final int WIDTH = Display.getWidth();
  private static final int HEIGHT = Display.getHeight();

  

  /**
   * Creates a tile at the current coordinates
   * 
   * @param tx x will become this*16
   * @param ty y will become this*16
   * @param t the type of this tile
   */
  public Tile(int tx,int ty, TileList t){
    x = tx*SIZE;
    y = ty*SIZE;
    type = t;
  }

  /**
   * Creates a tile at the current coordinates
   * 
   * @param x x will become this
   * @param y y will become this
   * @param t the type of this tile
   * @param dummy Dummy variable to differentiate this from the other
   */
  public Tile(float x,float y, TileList t, boolean dummy){
    this.x = x;
    this.y = y;
    type = t;
  }


  /**
   * Currently has a lot of test tiles (all tests start with x)
   */
  public void render(){
    if(inBounds()){
      int texx = type.getX(), texy = type.getY();
      
      Draw.rect(x,y, SIZE, SIZE, texx, texy, texx+SIZE, texy+SIZE, 1);
      if(grid){
        Draw.rect(x, y, SIZE, SIZE, 0, 16, SIZE, 16+SIZE, 2);
      }
    }
  }


  public TileList getType(){
    return type;
  }


  public void setType(TileList newT){
    type = newT;
  }

  public void move(float dx, float dy){
    this.dx = dx;
    this.dy = dy;
  }

  public float getX(){
    return x;
  }

  public float getY(){
    return y;
  }

  public String toString(){
    return type.toString() + ", X: " + x + ", Y: " + y;
  }

  /* (non-Javadoc)
   * @see komorebi.clyde.engine.Renderable#update()
   */
  @Override
  public void update() {
    x += dx;
    y += dy;
  }

  /**
   * Sets the location to the specified x and y
   * 
   * @param x the x, duh
   * @param y the y, duh
   */
  public void setLoc(float x, float y){
    this.x = x;
    this.y = y;
  }

  /**
   * Swtiches the state of the grid of every tile
   */
  public static void changeGrid(){
    grid = !grid;
  }  
  
  /**
   * @return Whether the tile is on the map
   */
  private boolean inBounds() {
    return x+32 > 0 && x < WIDTH && y+32 > 0 && y < HEIGHT;
  }

  
}
