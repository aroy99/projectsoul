/**
 * NPCType.java  Jun 10, 2016, 9:33:03 PM
 */
package komorebi.projsoul.entities;

import komorebi.projsoul.engine.InitializableAnimation;
import komorebi.projsoul.entities.sprites.NPCLoader;
import komorebi.projsoul.entities.sprites.SpriteSet;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * 
 * @author Andrew Faulkenberry
 */
public enum NPCType {
  POKEMON(new SpriteSet(
      //Left
      new InitializableAnimation(3,8,16,24,3)
      {
        public void initialize() {
          add(51, 0);
          add(67, 0);
          add(83, 0);
        } 

      },
      //Right
      new InitializableAnimation(3,8,16,24,3)
      {
        public void initialize() {
          add(51, 0, true);
          add(67, 0, true);
          add(83, 0, true);
        } 
      },
      //Up
      new InitializableAnimation(3,8,16,24,3)
      {
        public void initialize() {
          add(100, 0);
          add(117, 0);
          add(134, 0);
        } 
      },
      //Down
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
      //Left
      new InitializableAnimation(2,8,16,24,4)
      {
        public void initialize() {
          add(51, 0);
          add(68, 0);
        } 
      },
      //Right
      new InitializableAnimation(2,8,16,24,4){
        public void initialize() {
          add(51, 0, true);
          add(68, 0, true);
        }
      },
      //Up
      new InitializableAnimation(2,8,16,24,4)
      {

        public void initialize() {
          add(34, 0);
          add(34, 0, true);
        }

      },
      //Down
      new InitializableAnimation(2,8,16,24,4)
      {
        public void initialize() {
          add(0, 0);
          add(17, 0);
        }
      }
      )), 
  GUARD(new SpriteSet(
      //Left
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("guard_13"), 2, 0);
          add(NPCLoader.SPRITES.get("guard_14"), 1, 0);
          add(NPCLoader.SPRITES.get("guard_15"), 2, 0);
          add(NPCLoader.SPRITES.get("guard_16"), 2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("guard_01"), 2, 0);
        } 
      },
      //Right
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("guard_09"), 3, 0);
          add(NPCLoader.SPRITES.get("guard_10"), 4, 0);
          add(NPCLoader.SPRITES.get("guard_11"), 2, 0);
          add(NPCLoader.SPRITES.get("guard_12"), 4, 0);

          setPausedFrame(NPCLoader.SPRITES.get("guard_03"), 3, 0);
        } 
      },
      //Up
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("guard_17"), 0, 0);
          add(NPCLoader.SPRITES.get("guard_18"), -1, 0);
          add(NPCLoader.SPRITES.get("guard_19"), -1, 0);
          add(NPCLoader.SPRITES.get("guard_20"), -1, 0);

          setPausedFrame(NPCLoader.SPRITES.get("guard_04"), -1, 0);
        } 
      },
      //Down
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("guard_05"), -1, 0);
          add(NPCLoader.SPRITES.get("guard_06"), 2, 0);
          add(NPCLoader.SPRITES.get("guard_07"), 2, 0);
          add(NPCLoader.SPRITES.get("guard_08"), 2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("guard_02"), 1, 0);
        } 
      }
      )
      ),
  MAYOR(
      NESS.getNewSpriteSet()
      ),
  CELIA(
      NESS.getNewSpriteSet()
      ),
  KID_BOY(
      NESS.getNewSpriteSet()
      ),
  KID_GIRL(
      NESS.getNewSpriteSet()
      ),
  PEASANT(
      NESS.getNewSpriteSet()
      ),
  DOCTOR(new SpriteSet(
      //Left
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("doctor_13"), 3, 0);
          add(NPCLoader.SPRITES.get("doctor_14"), -1, 0);
          add(NPCLoader.SPRITES.get("doctor_15"), 3, 0);
          add(NPCLoader.SPRITES.get("doctor_16"), -2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("doctor_01"), 3, 0);
        } 
      },
      //Right
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("doctor_09"), 0, 0);
          add(NPCLoader.SPRITES.get("doctor_10"), 3, 0);
          add(NPCLoader.SPRITES.get("doctor_11"), 1, 0);
          add(NPCLoader.SPRITES.get("doctor_12"), 3, 0);

          setPausedFrame(NPCLoader.SPRITES.get("doctor_03"), 3, 0);
        } 
      },
      //Up
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("doctor_17"), -1, 0);
          add(NPCLoader.SPRITES.get("doctor_18"), 1, 1);
          add(NPCLoader.SPRITES.get("doctor_19"), 2, 0);
          add(NPCLoader.SPRITES.get("doctor_20"), 1, 1);

          setPausedFrame(NPCLoader.SPRITES.get("doctor_04"), 1, 0);
        } 
      },
      //Down
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("doctor_05"), -1, 0);
          add(NPCLoader.SPRITES.get("doctor_06"), 1, 1);
          add(NPCLoader.SPRITES.get("doctor_07"), 1, 0);
          add(NPCLoader.SPRITES.get("doctor_08"), 1, 1);

          setPausedFrame(NPCLoader.SPRITES.get("doctor_02"), 1, 0);
        } 
      }
      )
      ),
  PROFESSOR(new SpriteSet(
      //Left
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("professor_13"), 3, 0);
          add(NPCLoader.SPRITES.get("professor_14"), -1, 0);
          add(NPCLoader.SPRITES.get("professor_15"), 3, 0);
          add(NPCLoader.SPRITES.get("professor_16"), -2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("professor_01"), 3, 0);
        } 
      },
      //Right
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("professor_09"), 1, 0);
          add(NPCLoader.SPRITES.get("professor_10"), 4, 0);
          add(NPCLoader.SPRITES.get("professor_11"), 1, 0);
          add(NPCLoader.SPRITES.get("professor_12"), 4, 0);

          setPausedFrame(NPCLoader.SPRITES.get("professor_03"), 4, 0);
        } 
      },
      //Up
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("professor_17"), -1, 0);
          add(NPCLoader.SPRITES.get("professor_18"), 1, 1);
          add(NPCLoader.SPRITES.get("professor_19"), 2, 0);
          add(NPCLoader.SPRITES.get("professor_20"), 1, 1);

          setPausedFrame(NPCLoader.SPRITES.get("professor_04"), 1, 0);
        } 
      },
      //Down
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("professor_05"), -1, 0);
          add(NPCLoader.SPRITES.get("professor_06"), 1, 1);
          add(NPCLoader.SPRITES.get("professor_07"), 1, 0);
          add(NPCLoader.SPRITES.get("professor_08"), 1, 1);

          setPausedFrame(NPCLoader.SPRITES.get("professor_02"), 1, 0);
        } 
      }
      )
          ),
  BANKTELLER(new SpriteSet(
      //Left
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("bank_teller_13"), 3, 0);
          add(NPCLoader.SPRITES.get("bank_teller_14"), -1, 0);
          add(NPCLoader.SPRITES.get("bank_teller_15"), 3, 0);
          add(NPCLoader.SPRITES.get("bank_teller_16"), -2, 0);

          setPausedFrame(NPCLoader.SPRITES.get("bank_teller_01"), 3, 0);
        } 
      },
      //Right
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("bank_teller_09"), 1, 0);
          add(NPCLoader.SPRITES.get("bank_teller_10"), 4, 0);
          add(NPCLoader.SPRITES.get("bank_teller_11"), 1, 0);
          add(NPCLoader.SPRITES.get("bank_teller_12"), 4, 0);

          setPausedFrame(NPCLoader.SPRITES.get("bank_teller_03"), 4, 0);
        } 
      },
      //Up
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("bank_teller_17"), 0, 0);
          add(NPCLoader.SPRITES.get("bank_teller_18"), 1, 1);
          add(NPCLoader.SPRITES.get("bank_teller_19"), 2, 0);
          add(NPCLoader.SPRITES.get("bank_teller_20"), 1, 1);

          setPausedFrame(NPCLoader.SPRITES.get("bank_teller_04"), 1, 0);
        } 
      },
      //Down
      new InitializableAnimation(4,16,16)
      {
        public void initialize() {
          add(NPCLoader.SPRITES.get("bank_teller_05"), 0, 0);
          add(NPCLoader.SPRITES.get("bank_teller_06"), 1, 1);
          add(NPCLoader.SPRITES.get("bank_teller_07"), 1, 0);
          add(NPCLoader.SPRITES.get("bank_teller_08"), 1, 1);

          setPausedFrame(NPCLoader.SPRITES.get("bank_teller_02"), 1, 0);
        } 
      }
      )
      );

  private SpriteSet sprites;

  private NPCType(SpriteSet sprites)
  {
    this.sprites = sprites;
  }

  public SpriteSet getNewSpriteSet()
  {
    return sprites.duplicate();
  }

  /**
   * Takes in a string and returns its respective NPCType
   * 
   * @param s The input string
   * @return the corespondent NPCType
   */
  public static NPCType toEnum(String s){
    for(NPCType type: NPCType.values()){
      if(type.toString().equals(s)){
        return type;
      }
    }
    
    throw new InvalidParameterException("NPC Type " + s + " is not a valid type!");
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
