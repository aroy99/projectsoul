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
  private float[] sx;
  private float[] sy;
  private int[] offX;
  private int[] offY;

  private int[] rot;
  private boolean[] flipped;
  private int currFrame;
  private int cAddFrame;
  private int texID;
  private boolean playing = true;
  private boolean onlyOnce = false;

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
   * @param loop Whether the animation should play on loop
   */
  public Animation(int f, int t, float sx, float sy, int id, boolean loop){
    
    onlyOnce = !loop;
    
    frames = f;
    texx = new int[frames];
    texy = new int[frames];
    rot = new int[frames];
    flipped = new boolean[frames];
    time = t;
    texID = id;

    this.sx = new float[f];
    this.sy = new float[f];
    this.offX = new int[f];
    this.offY = new int[f];

    for (int i = 0; i < f; i++)
    {
      this.sx[i] = sx;
      this.sy[i] = sy;
    }

  }
  
  

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
    this(f, t, sx, sy, id, true);
  }


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
  public Animation(int f, int t, int id, boolean loop){
    
    onlyOnce = !loop;
    
    frames = f;
    texx = new int[frames];
    texy = new int[frames];
    rot = new int[frames];
    flipped = new boolean[frames];
    time = t;
    texID = id;

    sx = new float[f];
    sy = new float[f];
    this.offX = new int[f];
    this.offY = new int[f];
  }
  
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
  public Animation(int f, int t, int id){
    this(f,t,id,true);
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
   * Adds a frame to the animation, given the texture cooridantes, and the
   * size of the sprite in the frame
   * 
   * @param tx X position on the picture, starting from the left
   * @param ty Y position on the picture, starting from the top
   * @param sx Horizontal size of the sprite on the picture
   * @param sy Veritcal size of the sprite on the picture
   * @param offX Amount of pixels the frame should be moved when drawn on the screen
   * (+ means right, - means left)
   * @param offY Amount of pixels the frame should be moved when drawn on the screen
   * (+ means up, - means down)
   */
  public void add(int tx, int ty, float sx, float sy, int offX, int offY)
  {
    this.sx[cAddFrame] = sx;
    this.sy[cAddFrame] = sy;
    this.offX[cAddFrame] = offX;
    this.offY[cAddFrame] = offY;

    add(tx, ty, 0, false);

  }
  
  /**
   * Adds a frame to the animation, given the texture cooridantes, and the
   * size of the sprite in the frame
   * 
   * @param tx X position on the picture, starting from the left
   * @param ty Y position on the picture, starting from the top
   * @param sx Horizontal size of the sprite on the picture
   * @param sy Veritcal size of the sprite on the picture
   * @param offX Amount of pixels the frame should be moved when drawn on the screen
   * (+ means right, - means left)
   * @param offY Amount of pixels the frame should be moved when drawn on the screen
   * (+ means up, - means down)
   */
  public void add(int tx, int ty, float sx, float sy, int offX, int offY, boolean flip)
  {
    this.sx[cAddFrame] = sx;
    this.sy[cAddFrame] = sy;
    this.offX[cAddFrame] = offX;
    this.offY[cAddFrame] = offY;

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
   * Adds a frame to the animation, given  the texture coordinates, angle, and
   * whether it's flipped or not
   * 
   * @param tx X position on the picture, starting from the left         
   * @param ty Y position on the picture, starting from the <i>top</i>   
   * @param rot the rotation of the tile / 90 degrees
   * @param flip whether to flip the image or not
   */
  public void add(int tx, int ty, int sx, int sy, int rot, boolean flip){
    this.sx[cAddFrame] = sx;
    this.sy[cAddFrame] = sy;
    add(tx, ty, rot, flip);
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
  public void add(int tx, int ty, int sx, int sy){
    add(tx,ty,sx,sy,0,false);
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
          Draw.rect(x+offX[currFrame], y+offY[currFrame], sx[currFrame], 
              sy[currFrame], texx[currFrame], 
              texy[currFrame], texx[currFrame]+(int) sx[currFrame], 
              texy[currFrame]+(int) sy[currFrame], texID);
          break;
        case 1:
          Draw.rect(x+sx[currFrame]+offX[currFrame], y+offY[currFrame], sy[currFrame], sx[currFrame], texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int) sy[currFrame], texy[currFrame]+(int)sx[currFrame], 
              1, texID);
          break;
        case 2:
          Draw.rect(x+sx[currFrame]+offX[currFrame], y+sy[currFrame]+offY[currFrame], sx[currFrame], sy[currFrame],
              texx[currFrame], texy[currFrame], texx[currFrame]+(int)sx[currFrame],
              texy[currFrame]+ (int)sy[currFrame], 2, texID);
          break;
        case 3:
          Draw.rect(x+offX[currFrame], y+sy[currFrame]+offY[currFrame], sy[currFrame], sx[currFrame], 
              texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sy[currFrame], texy[currFrame]+(int) sx[currFrame],
              3, texID);
          break;
        default:
          //Do nothing, invalid value
      }
    }else{
      switch(rot[currFrame]){
        case 0:
          Draw.rect(x+offX[currFrame], y+offY[currFrame], sx[currFrame], sy[currFrame], texx[currFrame]+(int)sx[currFrame], 
              texy[currFrame], texx[currFrame], texy[currFrame]+(int)sy[currFrame], texID);
          break;
        case 1:
          break;
        case 2:
          break;
        case 3:
          Draw.rect(x+offX[currFrame], y+sy[currFrame]+offY[currFrame], sy[currFrame], sx[currFrame], texx[currFrame],
              texy[currFrame]+(int)sx[currFrame], 
              texx[currFrame]+(int)sy[currFrame], texy[currFrame], 3, texID);
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
          Draw.rectCam(x+offX[currFrame], y+offY[currFrame], sx[currFrame], sy[currFrame], texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame], texID);
          break;
        case 1:
          Draw.rectCam(x+sx[currFrame]+offX[currFrame], y+offY[currFrame], sy[currFrame], sx[currFrame], 
              texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sy[currFrame], texy[currFrame]+(int)sx[currFrame],
              1, texID);
          break;
        case 2:
          Draw.rectCam(x+sx[currFrame]+offX[currFrame], y+sy[currFrame]+offY[currFrame], sx[currFrame], sy[currFrame], 
              texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame], 
              2, texID);
          break;
        case 3:
          Draw.rectCam(x+offX[currFrame], y+sy[currFrame]+offY[currFrame], sy[currFrame], sx[currFrame], 
              texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sy[currFrame], texy[currFrame]+(int)sx[currFrame], 
              3, texID);
          break;
        default:
          //Do nothing, invalid value
      }
    }else{
      switch(rot[currFrame]){
        case 0:
          Draw.rectCam(x+offX[currFrame], y+offY[currFrame], sx[currFrame], sy[currFrame], texx[currFrame]+
              (int)sx[currFrame], texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy[currFrame], texID);
          break;
        case 1:
          break;
        case 2:
          break;
        case 3:
          Draw.rectCam(x+offX[currFrame], y+sy[currFrame]+offY[currFrame], sy[currFrame], sx[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sx[currFrame], 
              texx[currFrame]+(int)sy[currFrame], texy[currFrame], 3, texID);
          break;
        default:
          //Do nothing, invalid value
      }
    }

    increment();
  }

  public void increment()
  {
    if(playing){
      counter++;
      if(counter > time){
        counter = 0;
        currFrame++;
        if(currFrame > frames-1){
          if (onlyOnce)
          {
            hStop();
          } else
          {
            currFrame = 0;
          }
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

  public int getLWJGLFrames()
  {
    return time*frames;
  }
  
  public boolean playing()
  {
    return playing;
  }
  
  public boolean lastFrame()
  {
    return (currFrame+1==frames);
  }


}
