/**
 * Location.java	   Apr 17, 2017, 9:38:36 AM
 */
package komorebi.projsoul.ai;

/**
 * Represents a Location on a grid with an X and Y
 *
 * @author Aaron Roy
 */
public class Location{
  public final int x;
  public final int y;
  public Location(int nx, int ny){
    x = nx;
    y = ny;
  }
  
  @Override
  public boolean equals(Object chec){
    if(chec instanceof Location){
      Location check = (Location)chec;
      return check.x == x && check.y == y;
    }
    return false;
  }
  
  public int hashCode(){
    return 31*x+y;
  }
  
  public String toString(){
    return x + ", " + y;
  }
}