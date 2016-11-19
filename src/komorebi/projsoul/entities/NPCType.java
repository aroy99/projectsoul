/**
 * NPCType.java  Jun 10, 2016, 9:33:03 PM
 */
package komorebi.projsoul.entities;

import java.util.ArrayList;

/**
 * 
 * @author Andrew Faulkenberry
 */
public enum NPCType {
  POKEMON,
  NESS;

  /**
   * Takes in a string and returns its respective NPCType
   * 
   * @param s The input string
   * @return the corespondent NPCType
   */
  public static NPCType toEnum(String s){
    switch (s){
      case "POKEMON":
        return NPCType.POKEMON;

      case "NESS":
        return NPCType.NESS;

      default:
        return null;
    }
  }
  
  @Override
  public String toString(){
    switch(this){
      case POKEMON: return "POKEMON";
      case NESS:    return "NESS";
      default:      return "bleh";
    }
  }

  /**
   * @return all of the strings in this enum
   */
  public static ArrayList<String> allStrings(){
    ArrayList<String> a = new ArrayList<String>();
    for(NPCType type : values()){
      a.add(type.toString());
    }
    
    return a;
  }
}
