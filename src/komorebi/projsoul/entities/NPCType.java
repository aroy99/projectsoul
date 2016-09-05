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
   * @return all of the strings in this enum
   */
  public static ArrayList<String> allStrings(){
    ArrayList<String> a = new ArrayList<String>();
    a.add("POKEMON");
    a.add("NESS");

    return a;
  }

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
}
