/**
 * ProjectileType.java    Jan 3, 2017, 9:14:38 AM
 */
package komorebi.projsoul.attack.projectile;

import komorebi.projsoul.engine.Animation;

/**
 * Represents one of the projectile attack animations
 *
 * @author Aaron Roy
 */
public enum ProjectileType {
  WATER, FIRE, AIR;


  /**
   * Takes in a string and returns its respective ProjectileType
   * 
   * @param s The input string
   * @return the corespondent ProjectileType, null if not found
   */
  public static ProjectileType toEnum(String s){
    switch (s.toLowerCase()){
      case "water":
        return WATER;
      case "fire":
        return FIRE;
      case "air":
        return AIR;
      default:
        return null;
    }
  }
  
  /**
   * Gives an array of animations based on the ProjectileType
   * 
   * @param type The animation to get
   * @return The corresponding animations
   */
  public static Animation[] getAni(ProjectileType type){
    Animation[] ani = new Animation[4];
    
    switch (type) {
      case FIRE:
        for(int i=0; i < 4;i+=2){
          ani[i] = new Animation(4,8,12,false);
        }
        
        //UP
        ani[0].add(810,2,11,8,1,false);
        ani[0].add(827,1,15,9,1,false);
        ani[0].add(848,1,18,9,1,false);
        ani[0].add(874,0,20,10,1,false);
        ani[0].setPausedFrame(874,0,20,10,1,false);
        //DOWN
        ani[1] = ani[0].getFlipped();
        //RIGHT
        ani[2].add(810,2,11,8);
        ani[2].add(827,1,15,9);
        ani[2].add(848,1,18,9);
        ani[2].add(874,0,20,10);
        ani[2].setPausedFrame(874,0,20,10);
        //LEFT
        ani[3] = ani[2].getFlipped();

        break;
      case WATER:
        
        for(int i=0; i < 4;i+=2){
          ani[i] = new Animation(4,8,12,false);
        }
        //UP  
        ani[0].add(811, 13, 10, 9, 1, false);
        ani[0].add(828, 12, 14, 9, 1, false);
        ani[0].add(851, 13, 14, 9, 1, false);
        ani[0].add(875, 13, 19, 9, 1, false);
        ani[0].setPausedFrame(875, 13, 19, 9, 1, false);
        //DOWN
        ani[1] = ani[0].getFlipped();
        //RIGHT
        ani[2].add(811, 13, 10, 9);
        ani[2].add(828, 12, 14, 9);
        ani[2].add(851, 13, 14, 9);
        ani[2].add(875, 13, 19, 9);
        ani[2].setPausedFrame(875, 13, 19, 9);
        //LEFT
        ani[3] = ani[2].getFlipped();


        break;
        
      case AIR:
        
        for(int i=0; i < 4;i+=2){
          ani[i] = new Animation(5,4,12,false);
        }
        //UP  
        ani[0].add(795, 574, 8, 43, 1, false);
        ani[0].add(804, 574, 7, 43, 1, false);
        ani[0].add(813, 574, 6, 43, 1, false);
        ani[0].add(820, 574, 5, 43, 1, false);
        ani[0].add(827, 574, 6, 43, 1, false);
        ani[0].setPausedFrame(827, 574, 6, 43, 1, false);
        //DOWN
        ani[1] = ani[0].getFlipped();
        //RIGHT
        ani[2].add(795, 574, 8, 43);
        ani[2].add(804, 574, 7, 43);
        ani[2].add(813, 574, 6, 43);
        ani[2].add(820, 574, 5, 43);
        ani[2].add(827, 574, 6, 43);
        ani[2].setPausedFrame(827, 574, 6, 43);
        //LEFT
        ani[3] = ani[2].getFlipped();


        break;

      default:
        break;
    }
    
    return ani;
  }

}
