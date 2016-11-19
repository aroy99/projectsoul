/**
 * EnemyType.java	   Oct 4, 2016, 9:34:07 AM
 */
package komorebi.projsoul.entities;

import java.util.ArrayList;

/**
 * Contains all of the enemies in the game
 *
 * @author Aaron Roy
 */
public enum EnemyType {
  SATURN(16, 21);
  
  
  private int sx, sy;
  
  /**
   * Creates a new EnemyType
   */
  private EnemyType(int sx, int sy) {
    this.sx = sx;
    this.sy = sy;
  }
  
  /**
   * Takes in a string and returns its respective EnemyType
   * 
   * @param s The input string
   * @return the corespondent EnemyType, null if not found
   */
  public static EnemyType toEnum(String s){
    switch (s){
      case "SATURN":case "Mr. Saturn":
        return EnemyType.SATURN;
      default:
        return null;
    }
  }
  
  @Override
  public String toString(){
    switch(this){
      case SATURN: return "SATURN";
      default:      return "bleh";
    }
  }

  /**
   * @return all of the strings in this enum
   */
  public static ArrayList<String> allStrings(){
    ArrayList<String> a = new ArrayList<String>();
    for(EnemyType type : values()){
      a.add(type.toString());
    }
    
    return a;
  }

  public int getSX() {
    return sx;
  }

  public int getSY() {
    return sy;
  }
  
  

  
}
