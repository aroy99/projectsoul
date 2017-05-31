/**
 * NPCType.java  Jun 10, 2016, 9:33:03 PM
 */
package komorebi.projsoul.entities;

import komorebi.projsoul.engine.InitializableAnimation;
import komorebi.projsoul.entities.sprites.SpriteSet;

import java.util.ArrayList;

/**
 * 
 * @author Andrew Faulkenberry
 */
public enum NPCType {
  POKEMON(new SpriteSet(
      
      new InitializableAnimation(3,8,16,24,3)
      {
        public void initialize() {
          add(51, 0);
          add(67, 0);
          add(83, 0);
        } 
      
      },
      
      new InitializableAnimation(3,8,16,24,3)
      {
        public void initialize() {
          add(51, 0, true);
          add(67, 0, true);
          add(83, 0, true);
        } 
      },
      
      new InitializableAnimation(3,8,16,24,3)
      {
        public void initialize() {
          add(100, 0);
          add(117, 0);
          add(134, 0);
        } 
      },
      
      new InitializableAnimation(3,8,16,24,3)
      {
        public void initialize() {
          add(1, 0);
          add(18, 0);
          add(35, 0);
        } 
      }
      
      )),
  NESS(new SpriteSet(
      
      new InitializableAnimation(2,8,16,24,4)
      {
        public void initialize() {
          add(51, 0);
          add(68, 0);
        } 
      },
      
      new InitializableAnimation(2,8,16,24,4){
        public void initialize() {
          add(51, 0, true);
          add(68, 0, true);
        }
      },
      
      new InitializableAnimation(2,8,16,24,4)
      {

        public void initialize() {
          add(34, 0);
          add(34, 0, true);
        }
        
      },
      new InitializableAnimation(2,8,16,24,4)
      {
        public void initialize() {
          add(0, 0);
          add(17, 0);
        }
      }
   ));


  private SpriteSet sprites;

  private NPCType(SpriteSet sprites)
  {
    this.sprites = sprites;
  }
  
  public SpriteSet getNewSpriteSet()
  {
    return sprites.duplicate();
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
