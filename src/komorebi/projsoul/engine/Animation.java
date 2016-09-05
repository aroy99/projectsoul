/*
 * Animation.java           Feb 13, 2016
 */

package komorebi.projsoul.engine;

/**
 * Represents a set of pictures
 * 
 * @author Aaron Roy
 */
public class Animation {

  private int frames;
  private int time;
  private int counter;
  private int[] texx;
  private int[] texy;
  private int[] rot;
  private boolean[] flipped;
  private int currFrame;
  private int cAddFrame;
  private float sx,sy;
  private int texID;
  private boolean playing = true;

  /**
   * Creates a playable animation
   * 
   * @param f Max number of frames
   * @param t Time till next frame in frames
   * @param sx size x for the animation 
   *             *used to calculate other tex coordinates too
   * @param sy size y for the animation 
   *             *used to calculate other tex coordinates too
   * @param id The Texture ID
   */
  public Animation(int f, int t, float sx, float sy, int id){
    frames = f;
    texx = new int[frames];
    texy = new int[frames];
    rot = new int[frames];
    flipped = new boolean[frames];
    time = t;
    texID = id;
    this.sx = sx;
    this.sy = sy;
  }

  /**
   * Adds a frame to the animation, given  the texture coordinates
   * 
   * @param tx X position on the picture, starting from the left         
   * @param ty Y position on the picture, starting from the <i>top</i>   
   */
  public void add(int tx, int ty){
    add(tx, ty, 0, false);
  }

  /**
   * Adds a frame to the animation, given  the texture coordinates, and
   * whether it's flipped or not
   * 
   * @param tx X position on the picture, starting from the left         
   * @param ty Y position on the picture, starting from the <i>top</i>   
   * @param flip whether to flip the image or not
   */
  public void add(int tx, int ty, boolean flip){
    add(tx, ty, 0, flip);
  }

  /**
   * Adds a frame to the animation, given  the texture coordinates, angle
   * 
   * @param tx X position on the picture, starting from the left         
   * @param ty Y position on the picture, starting from the <i>top</i>   
   * @param rot the rotation of the tile / 90 degrees
   */
  public void add(int tx, int ty, int rot){
    add(tx, ty, rot, false);
  }

  /**
   * Adds a frame to the animation, given  the texture coordinates, angle, and
   * whether it's flipped or not
   * 
   * @param tx X position on the picture, starting from the left         
   * @param ty Y position on the picture, starting from the <i>top</i>   
   * @param rot the rotation of the tile / 90 degrees
   * @param flip whether to flip the image or not
   */
  public void add(int tx, int ty, int rot, boolean flip){
    texx[cAddFrame] = tx;
    texy[cAddFrame] = ty;
    this.rot[cAddFrame] = rot;
    flipped[cAddFrame] = flip;
    cAddFrame++;
  }

  /**
   * Plays the animation at the specified location
   * 
   * @param x x location for the animation
   * @param y y location for the animation
   */
  public void play(float x, float y){
    if(!flipped[currFrame]){
      switch(rot[currFrame]){
        case 0:
          Draw.rect(x, y, sx, sy, texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx, texy[currFrame]+(int)sy, texID);
          break;
        case 1:
          Draw.rect(x+sx, y, sy, sx, texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sy, texy[currFrame]+(int)sx, 1, texID);
          break;
        case 2:
          Draw.rect(x+sx, y+sy, sx, sy, texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx, texy[currFrame]+(int)sy, 2, texID);
          break;
        case 3:
          Draw.rect(x, y+sy, sy, sx, texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sy, texy[currFrame]+(int)sx, 3, texID);
          break;
        default:
          //Do nothing, invalid value
      }
    }else{
      switch(rot[currFrame]){
        case 0:
          Draw.rect(x, y, sx, sy, texx[currFrame]+(int)sx, texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy, texID);
          break;
        case 1:
          break;
        case 2:
          break;
        case 3:
          Draw.rect(x, y+sy, sy, sx, texx[currFrame], texy[currFrame]+(int)sx, 
              texx[currFrame]+(int)sy, texy[currFrame], 3, texID);
          break;
        default:
          //Do nothing, invalid value
      }
    }

    if(playing){
      counter++;
      if(counter > time){
        counter = 0;
        currFrame++;
        if(currFrame > frames-1){
          currFrame = 0;
        }
      }
    }
  }
  
  /**
   * Plays the animation at the specified location, minding the Camera
   * 
   * @param x x location for the animation
   * @param y y location for the animation
   */
  public void playCam(float x, float y){
    if(!flipped[currFrame]){
      switch(rot[currFrame]){
        case 0:
          Draw.rectCam(x, y, sx, sy, texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx, texy[currFrame]+(int)sy, texID);
          break;
        case 1:
          Draw.rectCam(x+sx, y, sy, sx, texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sy, texy[currFrame]+(int)sx, 1, texID);
          break;
        case 2:
          Draw.rectCam(x+sx, y+sy, sx, sy, texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx, texy[currFrame]+(int)sy, 2, texID);
          break;
        case 3:
          Draw.rectCam(x, y+sy, sy, sx, texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sy, texy[currFrame]+(int)sx, 3, texID);
          break;
        default:
          //Do nothing, invalid value
      }
    }else{
      switch(rot[currFrame]){
        case 0:
          Draw.rectCam(x, y, sx, sy, texx[currFrame]+(int)sx, texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy, texID);
          break;
        case 1:
          break;
        case 2:
          break;
        case 3:
          Draw.rectCam(x, y+sy, sy, sx, texx[currFrame], texy[currFrame]+(int)sx, 
              texx[currFrame]+(int)sy, texy[currFrame], 3, texID);
          break;
        default:
          //Do nothing, invalid value
      }
    }

    if(playing){
      counter++;
      if(counter > time){
        counter = 0;
        currFrame++;
        if(currFrame > frames-1){
          currFrame = 0;
        }
      }
    }
  }



  /**
   * Stops the animation, keeping the frame
   */
  public void stop(){
    playing = false;
  }

  /**
   * Stops the animation and resets the frame
   */
  public void hStop(){
    playing  = false;
    currFrame = 0;
  }

  /**
   * Resumes the animation
   */
  public void resume(){
    playing  = true;
  }
  /**
   * Sets the speed of the animation
   * @param speed speed in frames
   */
  public void setSpeed(int speed){
    time = speed;
  }


}
