/*
 * Animation.java           Feb 13, 2016
 */

package komorebi.projsoul.engine;

import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.map.EditorMap;

/**
 * Represents a set of pictures
 * 
 * @author Aaron Roy
 * @author Andrew Faulkenberry
 */
public class Animation {

  private int frames;
  private int time;
  private int counter;
  
  private int scale;

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

  private boolean hasCustomFrame;  
  
  /**
   * Creates a playable animation
   * 
   * @param f Max number of frames
   * @param t Time till next frame in frames
   * @param sx size x for the animation 
   *             *used to calculate other tex coordinates too
   * @param sy size y for the animation 
   *             *used to calculate other tex coordinates too
   * @param id see {@link Draw#loadTextures() loadTextures}
   * @param loop Whether the animation should play on loop
   */
  public Animation(int f, int t, float sx, float sy, int id, 
      boolean loop){
    this(f,t,sx,sy,id,loop,1);
  }
  
  public Animation(int f, int t, float sx, float sy, int id, boolean loop,
      int scale){

    
    this.scale = scale;
    onlyOnce = !loop;

    frames = f;
    texx = new int[frames+1];
    texy = new int[frames+1];
    rot = new int[frames+1];
    flipped = new boolean[frames+1];
    time = t;
    texID = id;

    this.sx = new float[frames+1];
    this.sy = new float[frames+1];
    this.offX = new int[frames+1];
    this.offY = new int[frames+1];

    for (int i = 0; i <= f; i++)
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
   * @param id see {@link Draw#loadTextures() loadTextures}
   */
  public Animation(int f, int t, float sx, float sy, int id){
    this(f, t, sx, sy, id, true);
  }


  /**
   * Creates a playable animation
   * 
   * @param f Max number of frames
   * @param t Time till next frame in frames
   * @param id see {@link Draw#loadTextures() loadTextures}
   * @param loop Whether the animation should play on loop
   */
  public Animation(int f, int t, int id, boolean loop){
    this(f,t,id,loop,1);
  }
  
  public Animation(int f, int t, int id, boolean loop, int scale)
  {
    this.scale = scale;
    onlyOnce = !loop;

    frames = f;
    texx = new int[frames+1];
    texy = new int[frames+1];
    rot = new int[frames+1];
    flipped = new boolean[frames+1];
    time = t;
    texID = id;

    sx = new float[f+1];
    sy = new float[f+1];
    this.offX = new int[f+1];
    this.offY = new int[f+1];
  }

  /**
   * Creates a playable animation
   * 
   * @param f Max number of frames
   * @param t Time till next frame in frames
   * @param id see {@link Draw#loadTextures() loadTextures}
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
    add(tx, ty, sx[cAddFrame], sy[cAddFrame], 0,
        0, 0, false);
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
    add(tx, ty, sx[cAddFrame], sy[cAddFrame], 0,
        0, 0, flip);
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
   *                  (+ means right, - means left)
   * @param offY Amount of pixels the frame should be moved when drawn on the screen
   *                  (+ means up, - means down)
   */
  public void add(int tx, int ty, float sx, float sy, int offX, int offY)
  {
    add(tx, ty, sx, sy, offX, offY, 0, false);
  }

  public void add(int tx, int ty, float sx, float sy, int offX, int offY, 
      int rot, boolean flip)
  {
    this.sx[cAddFrame] = sx;
    this.sy[cAddFrame] = sy;
    this.offX[cAddFrame] = offX;
    this.offY[cAddFrame] = offY;

    texx[cAddFrame] = tx;
    texy[cAddFrame] = ty;
    this.rot[cAddFrame] = rot;
    flipped[cAddFrame] = flip;
    cAddFrame++;
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
   *                  (+ means right, - means left)
   * @param offY Amount of pixels the frame should be moved when drawn on the screen
   *                  (+ means up, - means down)
   * @param flip Whether the image is flipped or not
   */
  public void add(int tx, int ty, float sx, float sy, int offX, int offY, boolean flip)
  {
    add(tx, ty, sx, sy, offX, offY, 0, flip);

  }

  /**
   * Adds a frame to the animation, given  the texture coordinates, angle
   * 
   * @param tx X position on the picture, starting from the left         
   * @param ty Y position on the picture, starting from the <i>top</i>   
   * @param rot the rotation of the tile / 90 degrees
   */
  public void add(int tx, int ty, int rot){
    add(tx, ty, sx[cAddFrame], sy[cAddFrame], 0,
        0, rot, false);  }
  
  public void add(int tx, int ty, int sx, int sy, int rot)
  {
    add(tx, ty, sx, sy, 0, 0, rot, false);
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
    add(tx, ty, sx[cAddFrame], sy[cAddFrame], 0,
        0, rot, flip);
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
    add(tx, ty, sx, sy, 0, 0, rot, flip);
  }

  /**
   * Adds a frame to the animation, given  the texture coordinates, angle, and
   * whether it's flipped or not
   * 
   * @param tx X position on the picture, starting from the left         
   * @param ty Y position on the picture, starting from the <i>top</i>   
   * @param sx The horizontal size of the picture
   * @param sy The vertical size of the picture
   */
  public void add(int tx, int ty, int sx, int sy){
    add(tx,ty,sx,sy,0,0,0,false);
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
          Draw.rect(x+offX[currFrame], y+offY[currFrame], sx[currFrame]*scale, sy[currFrame]*scale, texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame], texID);
          break;
        case 1:
          Draw.rect(x+offX[currFrame]+sy[currFrame]*scale, y+offY[currFrame], sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame],
              1, texID);
          break;
        case 2:
          Draw.rect(x+offX[currFrame]+sx[currFrame]*scale, y+offY[currFrame]+sy[currFrame]*scale,
              sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame], 
              2, texID);
          break;
        case 3:
          Draw.rect(x+offX[currFrame], y+offY[currFrame]+sx[currFrame]*scale, sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame], 
              3, texID);
          break;
        default:
          //Do nothing, invalid value
      }
    }else{
      switch(rot[currFrame]){
        case 0:
          Draw.rect(x+offX[currFrame], y+offY[currFrame], sx[currFrame]*scale, sy[currFrame]*scale, texx[currFrame]+(int)sx[currFrame], texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy[currFrame], texID);
          break;
        case 1:
          Draw.rect(x+offX[currFrame]+sy[currFrame]*scale, y+offY[currFrame], sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy[currFrame],
              1, texID);
          break;
        case 2:
          Draw.rect(x+offX[currFrame]+sx[currFrame]*scale, y+offY[currFrame]+sy[currFrame]*scale,
              sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy[currFrame], 
              2, texID);
          break;
        case 3:
          Draw.rect(x+offX[currFrame], y+offY[currFrame]+sx[currFrame]*scale, sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy[currFrame], 
              3, texID);
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
  
  public void playZoom(float x, float y){
    
    
    if(!flipped[currFrame]){
      switch(rot[currFrame]){
        case 0:
          Draw.rectZoom(x+offX[currFrame], y+offY[currFrame], sx[currFrame]*scale, sy[currFrame]*scale, texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame], 0, texID,
              Editor.zoom(), EditorMap.getX(), EditorMap.getY());
          break;
        case 1:
          Draw.rectZoom(x+offX[currFrame]+sy[currFrame]*scale, y+offY[currFrame], sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame],
              1, texID, Editor.zoom(), EditorMap.getX(), EditorMap.getY());
          break;
        case 2:
          Draw.rectZoom(x+offX[currFrame]+sx[currFrame]*scale, y+offY[currFrame]+sy[currFrame]*scale,
              sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame], 
              2, texID, Editor.zoom(), EditorMap.getX(), EditorMap.getY());
          break;
        case 3:
          Draw.rectZoom(x+offX[currFrame], y+offY[currFrame]+sx[currFrame]*scale, sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame], 
              3, texID, Editor.zoom(), EditorMap.getX(), EditorMap.getY());
          break;
        default:
          //Do nothing, invalid value
      }
    }else{
      switch(rot[currFrame]){
        case 0:
          Draw.rectZoom(x+offX[currFrame], y+offY[currFrame], sx[currFrame]*scale, sy[currFrame]*scale, texx[currFrame]+(int)sx[currFrame], texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy[currFrame], texID, Editor.zoom(), 
              EditorMap.getX(), EditorMap.getY());
          break;
        case 1:
          Draw.rectZoom(x+offX[currFrame]+sy[currFrame]*scale, y+offY[currFrame], sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy[currFrame],
              1, texID, Editor.zoom(), EditorMap.getX(), EditorMap.getY());
          break;
        case 2:
          Draw.rectZoom(x+offX[currFrame]+sx[currFrame]*scale, y+offY[currFrame]+sy[currFrame]*scale,
              sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy[currFrame], 
              2, texID, Editor.zoom(), EditorMap.getX(), EditorMap.getY());
          break;
        case 3:
          Draw.rectZoom(x+offX[currFrame], y+offY[currFrame]+sx[currFrame]*scale, sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy[currFrame], 
              3, texID, Editor.zoom(), EditorMap.getX(), EditorMap.getY());
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
  public void playCam(float x, float y) {
        
    if(!flipped[currFrame]){
      switch(rot[currFrame]){
        case 0:
          Draw.rectCam(x+offX[currFrame], y+offY[currFrame], sx[currFrame]*scale, 
              sy[currFrame]*scale, texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame], texID);
          break;
        case 1:
          Draw.rectCam(x+offX[currFrame]+sy[currFrame]*scale, y+offY[currFrame], sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame],
              1, texID);
          break;
        case 2:
          Draw.rectCam(x+offX[currFrame]+sx[currFrame]*scale, y+offY[currFrame]+sy[currFrame]*scale,
              sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame], 
              2, texID);
          break;
        case 3:
          Draw.rectCam(x+offX[currFrame], y+offY[currFrame]+sx[currFrame]*scale, sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame], texy[currFrame], 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame]+(int)sy[currFrame], 
              3, texID);
          break;
        default:
          //Do nothing, invalid value
      }
    }else{
      switch(rot[currFrame]){
        case 0:
          Draw.rectCam(x+offX[currFrame], y+offY[currFrame], sx[currFrame]*scale, sy[currFrame]*scale, texx[currFrame]+(int)sx[currFrame], texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy[currFrame], texID);
          break;
        case 1:
          Draw.rectCam(x+offX[currFrame]+sy[currFrame]*scale, y+offY[currFrame], sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy[currFrame],
              1, texID);
          break;
        case 2:
          Draw.rectCam(x+offX[currFrame]+sx[currFrame]*scale, y+offY[currFrame]+sy[currFrame]*scale,
              sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy[currFrame], 
              2, texID);
          break;
        case 3:
          Draw.rectCam(x+offX[currFrame], y+offY[currFrame]+sx[currFrame]*scale, sx[currFrame]*scale, sy[currFrame]*scale, 
              texx[currFrame]+(int)sx[currFrame], texy[currFrame], 
              texx[currFrame], texy[currFrame]+(int)sy[currFrame], 
              3, texID);
          break;
        default:
          //Do nothing, invalid value
      }
    }

    increment();
  }

  /*
   * Increments the frame counter
   */
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
    if (hasCustomFrame)
    {
      currFrame = frames;
    } else
    {
      currFrame = 0;
    }
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

  /**
   * @return true if the animation is currently playing, else false
   */
  public boolean playing()
  {
    return playing;
  }

  /**
   * @return True if the animation is on its last frame, else false
   */
  public boolean lastFrame()
  {
    return (currFrame+1 == frames);
  }

  public void setStopFrame(){

  }

  public void reset()
  {
    currFrame = 0;
  }

  public int currentFrame()
  {
    return currFrame;
  }

  public float getCurrSX()
  {
    return sx[currFrame];
  }

  public float getCurrSY()
  {
    return sy[currFrame];
  }

  public float getCurrOffX()
  {
    return offX[currFrame];
  }

  public float getCurrOffY()
  {
    return offY[currFrame];
  }

  public void setPausedFrame(int tx, int ty){
    setPausedFrame(tx, ty, 0, false);
  }

  /**
   * Adds a frame to the animation, given  the texture coordinates, and
   * whether it's flipped or not
   * 
   * @param tx X position on the picture, starting from the left         
   * @param ty Y position on the picture, starting from the <i>top</i>   
   * @param flip whether to flip the image or not
   */
  public void setPausedFrame(int tx, int ty, boolean flip){
    setPausedFrame(tx, ty, 0, flip);
  }


  /**
   * Adds a frame to the animation, given  the texture coordinates, angle
   * 
   * @param tx X position on the picture, starting from the left         
   * @param ty Y position on the picture, starting from the <i>top</i>   
   * @param rot the rotation of the tile / 90 degrees
   */
  public void setPausedFrame(int tx, int ty, int rot){
    setPausedFrame(tx, ty, rot, false);
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
  public void setPausedFrame(int tx, int ty, int rot, boolean flip){
    texx[frames] = tx;
    texy[frames] = ty;
    this.rot[frames] = rot;
    flipped[frames] = flip;

    hasCustomFrame = true;
  }



  /**
   * Adds a frame to the animation, given  the texture coordinates, angle, and
   * whether it's flipped or not
   * 
   * @param tx X position on the picture, starting from the left         
   * @param ty Y position on the picture, starting from the <i>top</i>   
   * @param sx The horizontal size of the picture
   * @param sy The vertical size of the picture
   */
  public void setPausedFrame(int tx, int ty, int sx, int sy){
    setPausedFrame(tx,ty,sx,sy,0,false);
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
  public void setPausedFrame(int tx, int ty, int sx, int sy, int rot, boolean flip){    
    this.sx[frames] = sx;
    this.sy[frames] = sy;
    setPausedFrame(tx, ty, rot, flip);
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
   *         (+ means right, - means left)
   * @param offY Amount of pixels the frame should be moved when drawn on the screen
   *         (+ means up, - means down)
   */
  public void setPausedFrame(int tx, int ty, float sx, float sy, int offX, int offY)
  {
    this.sx[frames] = sx;
    this.sy[frames] = sy;
    this.offX[frames] = offX;
    this.offY[frames] = offY;

    setPausedFrame(tx, ty, 0, false);

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
   *         (+ means right, - means left)
   * @param offY Amount of pixels the frame should be moved when drawn on the screen
   *         (+ means up, - means down)
   * @param flip Whether the image is flipped or not
   */
  public void setPausedFrame(int tx, int ty, float sx, float sy, int offX, int offY, boolean flip)
  {
    this.sx[frames] = sx;
    this.sy[frames] = sy;
    this.offX[frames] = offX;
    this.offY[frames] = offY;

    setPausedFrame(tx, ty, 0, flip);

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
   *         (+ means right, - means left)
   * @param offY Amount of pixels the frame should be moved when drawn on the screen
   *         (+ means up, - means down)
   * @param flip Whether the image is flipped or not
   */
  public void setPausedFrame(int tx, int ty, float sx, float sy, int offX, 
      int offY, int rot, boolean flip){
    this.sx[frames] = sx;
    this.sy[frames] = sy;
    this.offX[frames] = offX;
    this.offY[frames] = offY;

    texx[frames] = tx;
    texy[frames] = ty;
    this.rot[frames] = rot;
    flipped[frames] = flip;

    hasCustomFrame = true;
  }

  /**
   * Creates a new animation that is the flipped version of the input
   * 
   * @return The same animation, flipped on the X-axis
   */
  public Animation getFlipped(){
    Animation returnee = new Animation(frames, time, texID, !onlyOnce);

    for(int j = 0; j < frames; j++){
      if(offX[j] != 0){
        returnee.add(texx[j], texy[j], sx[j], sy[j], 
            offX[j]+2, offY[j], rot[j], !flipped[j]);
      }else{
        returnee.add(texx[j], texy[j], sx[j], sy[j], 
            offX[j], offY[j], rot[j], !flipped[j]);
      }
    }

    if(hasCustomFrame){
      if(offX[frames] != 0){
        returnee.setPausedFrame(texx[frames], texy[frames], sx[frames], sy[frames], 
            offX[frames]+2, offY[frames], !flipped[frames]);
      }else{
        returnee.setPausedFrame(texx[frames], texy[frames], sx[frames], sy[frames], 
            offX[frames], offY[frames], !flipped[frames]);
      }
      returnee.setPausedFrame(texx[frames], texy[frames], 
          rot[frames], !flipped[frames]);
    }
    return returnee;
  }
  
  public Animation duplicate()
  {
    Animation duplicate = new Animation(frames, time, texID);
    
    for (int i = 0; i < frames; i++)
    {
      duplicate.add(texx[i], texy[i], sx[i], sy[i], offX[i],
          offY[i], rot[i], flipped[i]);
    }
    
    return duplicate;
  }
}
