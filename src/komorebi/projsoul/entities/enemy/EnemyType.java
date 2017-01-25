/**
 * EnemyType.java	   Oct 4, 2016, 9:34:07 AM
 */
package komorebi.projsoul.entities.enemy;

import komorebi.projsoul.engine.Animation;

import java.util.ArrayList;

import javax.print.attribute.standard.RequestingUserName;

/**
 * Contains all of the enemies in the game
 *
 * @author Aaron Roy
 */
public enum EnemyType {
  SATURN(16, 21), EVIL_SATURN(16,21), SMILIN_SATURN(16,21), BLUE_DETRAL(16, 16);
  
  
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
      case "SATURN":case "Mr. Saturn":            return SATURN;
      case "EVIL_SATURN":case "Evil Saturn":      return EVIL_SATURN;
      case "SMILIN_SATURN":case "Smilin' Saturn": return SMILIN_SATURN;
      case "BLUE_DETRAL":case "Blue Detral":      return BLUE_DETRAL;
      default: return null;
    }
  }
  
  /**
   * @param type The enemy you want the animation for
   * @return An animation for the enemy
   */
  public static Animation getAni(EnemyType type){
    Animation ani;
    int tx, ty;
    
    switch(type){
      case EVIL_SATURN:   tx = 174; ty =   0; break;
      case SATURN:        tx =   0; ty =   0; break;
      case SMILIN_SATURN: tx = 191; ty =   0; break;
      case BLUE_DETRAL:   tx = 223; ty =   0; break;
      default: tx = 0; ty = 0;      
    }
    
    ani = new Animation(2, 30, type.getSX(), type.getSY(), 11);
    
    ani.add(tx, ty);
    ani.add(tx, ty, true);
        
    return ani;
  }
  
  @Override
  public String toString(){
    switch(this){
      case SATURN:        return "Mr. Saturn";
      case EVIL_SATURN:   return "Evil Saturn";
      case SMILIN_SATURN: return "Smilin' Saturn";
      case BLUE_DETRAL:   return "Blue Detral";
      default:            return "bleh";
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
