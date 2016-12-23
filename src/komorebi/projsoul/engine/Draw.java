/*
 * Draw.java    June 2, 2016, 7:42:38 PM
 */

package komorebi.projsoul.engine;

import java.io.File;
import java.io.FileInputStream;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 * Draws stuff. :D
 *
 * @author Aaron Roy
 */
public class Draw {

  /** To ensure rotations can only happen in multiples of 90 degrees.*/
  private static final int RIGHT_ANGLE = 90;

  /** Holds all of the textures for this class.*/
  private static Texture[] tex = new Texture[15];

  /** Determines whether textures are loaded.*/
  private static boolean texLoaded;

  /**
   * Loads textures
   * 
   * <p><u>Current:</u><br>
   *    0: Terra<br>
   *    1: Pokemon Tiles<br>
   *    2: Selector/Grid<br>
   *    3: Ash Ketchum<br>
   *    4: Ness<br>
   *    5: Earthbound Font<br>
   *    6: Textbox<br>
   *    7: Picker<br>
   *    8: Fader<br>
   *    9: Clyde's Tiles<br>
   *    10: Miscellaneous Items<br>
   *    11: Fillers for Project Soul<br>
   */
  public static void loadTextures() {
    try {
      tex[0] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/Terra.png")));
      tex[1] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/PokeTiles.png")));
      tex[2] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/EditorSheet.png")));
      tex[3] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/NPCFiller.png")));
      tex[4] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/NPCFiller2.png")));
      tex[5] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/FillerFont.png")));
      tex[6] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/Textfield2.png")));
      tex[7] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/Picker.png")));
      tex[8] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/Fader.png")));
      tex[9] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/ClydeTiles.png")));
      tex[10] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/Items.png")));
      tex[11] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/Fillers.png")));
      tex[12] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/Shadow.png")));
      tex[13] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/Death.png")));
      tex[14] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/fontnumbers-currency.png")));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Draws a sprite on the screen from the specified image, assumed the texsx
   * and texsy are the same as sx and sy
   * 
   * @param x the X position on the screen, starting from the left         
   * @param y the Y position on the screen, starting from the <i>bottom</i>
   * @param sx the width                                                   
   * @param sy the height                                                  
   * @param texx X position on the picture, starting from the left         
   * @param texy Y position on the picture, starting from the <i>top</i>   
   * @param texID see {@link Draw#loadTextures() loadTextures}
   */
  public static void rect(float x, float y, float sx, float sy, int texx, 
      int texy, int texID) {
    rect(x, y, sx, sy, texx, texy, texx + (int)sx, texy + (int)sy, texID);
  }

  /**
   * Draws a sprite on the screen from the specified image, no rotation
   * 
   * @param x the X position on the screen, starting from the left         
   * @param y the Y position on the screen, starting from the <i>bottom</i>
   * @param sx the width                                                   
   * @param sy the height                                                  
   * @param texx X position on the picture, starting from the left         
   * @param texy Y position on the picture, starting from the <i>top</i>   
   * @param texsx end X position on the picture, starting from the left      
   * @param texsy end Y position on the picture, starting from the <i>top</i>
   * @param texID see {@link Draw#loadTextures() loadTextures}
   */
  public static void rect(float x, float y, float sx, float sy, int texx, 
      int texy, int texsx, int texsy, int texID) {
    rect(x, y, sx, sy, texx, texy, texsx, texsy, 0, texID);
  }

  /**
   * Draws a sprite on the screen from the specified image, with rotation.
   * 
   * @param x the X position on the screen, starting from the left           
   * @param y the Y position on the screen, starting from the <i>bottom</i>  
   * @param sx the width                                                     
   * @param sy the height                                                    
   * @param texx X position on the picture, starting from the left           
   * @param texy Y position on the picture, starting from the <i>top</i>     
   * @param texsx end X position on the picture, starting from the left      
   * @param texsy end Y position on the picture, starting from the <i>top</i>
   * @param angle the rotation of the tile / 90 degrees
   * @param texID see {@link Draw#loadTextures() loadTextures}
   */
  public static void rect(float x, float y, float sx, float sy, int texx, 
      int texy, int texsx, int texsy, int angle, int texID) {
    glPushMatrix();
    {
      if (!texLoaded) {
        loadTextures();
        texLoaded = true;
      }

      //Change Death png that has to be 256 by 256 (DONE)
      int imgX = tex[texID].getImageWidth();
      int imgY = tex[texID].getImageHeight();

      glTranslatef((int)x, (int)y, 0);
      glRotatef(angle * RIGHT_ANGLE, 0.0f, 0.0f, 1.0f);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
      tex[texID].bind();

      glBegin(GL_QUADS);
      {
        glTexCoord2f((float) texx / imgX, (float) texsy / imgY);
        glVertex2f(0, 0);

        glTexCoord2f((float) texx / imgX, (float) texy / imgY);
        glVertex2f(0, (int) sy);

        glTexCoord2f((float) texsx / imgX, (float) texy / imgY);
        glVertex2f((int) sx, (int) sy);

        glTexCoord2f((float) texsx / imgX, (float) texsy / imgY);
        glVertex2f((int) sx, 0);
      }
      glEnd();
    }

    glPopMatrix();

  }
  
  /**
   * Draws a camera fixed sprite on the screen from the specified image, with rotation.
   * 
   * @param x the X position on the screen, starting from the left           
   * @param y the Y position on the screen, starting from the <i>bottom</i>  
   * @param sx the width                                                     
   * @param sy the height                                                    
   * @param texx X position on the picture, starting from the left           
   * @param texy Y position on the picture, starting from the <i>top</i>     
   * @param texsx end X position on the picture, starting from the left      
   * @param texsy end Y position on the picture, starting from the <i>top</i>
   * @param angle the rotation of the tile / 90 degrees
   * @param texID see {@link Draw#loadTextures() loadTextures}
   */
  public static void rectCam(float x, float y, float sx, float sy, int texx, 
      int texy, int texsx, int texsy, int angle, int texID) {
    rect(x-Camera.getX(), y-Camera.getY(), 
        sx, sy, texx, texy, texsx, texsy, angle, texID);
  }
  
  /**
   * Draws a camera fixed sprite on the screen from the specified image, assumed the texsx
   * and texsy are the same as sx and sy
   * 
   * @param x the X position on the screen, starting from the left         
   * @param y the Y position on the screen, starting from the <i>bottom</i>
   * @param sx the width                                                   
   * @param sy the height                                                  
   * @param texx X position on the picture, starting from the left         
   * @param texy Y position on the picture, starting from the <i>top</i>   
   * @param texID see {@link Draw#loadTextures() loadTextures}
   */
  public static void rectCam(float x, float y, float sx, float sy, int texx, 
      int texy, int texID) {
    rectCam(x, y, sx, sy, texx, texy, texx + (int)sx, texy + (int)sy, texID);
  }

  /**
   * Draws a camera fixed sprite on the screen from the specified image, no rotation
   * 
   * @param x the X position on the screen, starting from the left         
   * @param y the Y position on the screen, starting from the <i>bottom</i>
   * @param sx the width                                                   
   * @param sy the height                                                  
   * @param texx X position on the picture, starting from the left         
   * @param texy Y position on the picture, starting from the <i>top</i>   
   * @param texsx end X position on the picture, starting from the left      
   * @param texsy end Y position on the picture, starting from the <i>top</i>
   * @param texID see {@link Draw#loadTextures() loadTextures}
   */
  public static void rectCam(float x, float y, float sx, float sy, int texx, 
      int texy, int texsx, int texsy, int texID) {
    rectCam(x, y, sx, sy, texx, texy, texsx, texsy, 0, texID);
  }

}
