/**
 * Portrait.java   May 23, 2017, 9:12:06 AM
 */
package komorebi.projsoul.script.text;

import komorebi.projsoul.engine.Draw;

import java.util.Random;

/**
 * Holds the coordinates for all of the portraits
 *
 * @author Aaron Roy
 */
public enum Portrait {
  NONE, PRIDE, YOUNG_CASPIAN, KID_BOY, TAMAKO, FILL3;
  
  private static final int LEFT_X = 20, RIGHT_X = 190;
  private static final int PORTRAIT_Y_OFFSET = 6;
  private static final int COLUMNS = 3;
  private static final int WIDTH = 40, HEIGHT = 48;
  
  int tx, ty;
  boolean isLeft = false;
  
  private static final Random GEN = new Random();
  
  static{
    for(int i = 0; i < Portrait.values().length; i++){
      Portrait p = Portrait.values()[i];
      p.tx = i%COLUMNS*WIDTH;
      p.ty = (i/3)*HEIGHT;
    }
    
  }
  
  /**
   * Sets this portrait to the left or right side
   * 
   * @param left True for left, false for right
   */
  public void setLeft(boolean left){
    this.isLeft = left;
  }
  
  public void render(){
    if (this != NONE) {
      int x;
      if(isLeft){
        x = LEFT_X;
      }else{
        x = RIGHT_X;
      }
      Draw.rect(x, SpeechHandler.getY()+PORTRAIT_Y_OFFSET, WIDTH, HEIGHT, tx, ty, 15);
    }
  }

  /**
   * @return Whether the portrait is on the left or right
   */
  public boolean isLeft() {
    return isLeft;
  }

  /**
   * @return a Random portrait
   */
  public static Portrait random() {
    return Portrait.values()[GEN.nextInt(5) + 1];
  }
}
