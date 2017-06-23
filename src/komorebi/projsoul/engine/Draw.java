/*
 * Draw.java    June 2, 2016, 7:42:38 PM
 */

package komorebi.projsoul.engine;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import komorebi.projsoul.map.Map;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import komorebi.projsoul.gameplay.Camera;

/**
 * Draws stuff. :D
 *
 * @author Aaron Roy
 */
public class Draw {

  /** To ensure rotations can only happen in multiples of 90 degrees.*/
  private static final int RIGHT_ANGLE = 90;

  /** The roundness of the circle */
  private static final int NUM_PIZZA_SLICES = 30;

  public static final int BLANK_TILE = 0;
  public static final Rectangle LAYER_MANAGER = new Rectangle(0, 0, 12*16, 
      34*16-8);
  public static final Rectangle FULL_SCREEN =
      new Rectangle(0, 0, MainE.WIDTH*MainE.scale, MainE.HEIGHT*MainE.scale);

  private static final int SPREADSHEET_SIZE = 256;
  private static final int SPREADSHEET_ROW = 16;

  /** Holds all of the textures for this class.*/
  private static Texture[] tex = new Texture[22];

  private static ArrayList<Texture> sheets = new ArrayList<Texture>();

  /** Determines whether textures are loaded.*/
  private static boolean texLoaded;

  /**
   * Loads textures
   * 
   * <p><u>Current:</u><br>
   *    0:  Terra<br>
   *    1:  Pokemon Tiles<br>
   *    2:  Selector/Grid/Debug<br>
   *    3:  Ash Ketchum<br>
   *    4:  Ness<br>
   *    5:  Earthbound Font<br>
   *    6:  Textbox<br>
   *    7:  Picker<br>
   *    8:  Fader<br>
   *    9:  Clyde's Tiles<br>
   *    10: Miscellaneous Items<br>
   *    11: Fillers for Project Soul<br>
   *    12: Other Filler Characters<br>
   *    13: You Ded Screen<br>
   *    14: Menu Font<br>
   *    15: Portraits<br>
   *    16: Justin, Mike, and Anthony's NPCs<br>
   *    17: Cuad's Title Screen<br>
   *    18: Currency Numbers<br>
   *    19: Bank Box<br>
   *    20: Up Arrow<br>
   *    21: Anthony's HP Bar<br>
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
          new File("res/MenuFont.png")));
      tex[15] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/Portraits.png")));
      tex[16] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/compressed_spritesheet.png")));
      tex[17] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/Title-Screen-Cuads.png")));
      tex[18] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/fontnumbers-currency.png")));
      tex[19] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/NumberBox.png")));
      tex[20] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/Arrow.png")));
      tex[21] = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/hp_bar.png")));


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
        tex[texID].bind();
      }

      int imgX = tex[texID].getImageWidth();
      int imgY = tex[texID].getImageHeight();

      //TODO Investigate performance benefits
      //      if(prevTexID != texID){
      tex[texID].bind();
      //      }

      //      prevTexID = texID;

      glTranslatef((int)x, (int)y, 0);
      glRotatef(angle * RIGHT_ANGLE, 0.0f, 0.0f, 1.0f);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

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
   * @param texture Some custom texture
   */
  public static void rect(float x, float y, float sx, float sy, int texx, 
      int texy, int texsx, int texsy, int angle, Texture texture) {
    glPushMatrix();
    {
      if (!texLoaded) {
        loadTextures();
        texLoaded = true;
      }

      int imgX = texture.getImageWidth();
      int imgY = texture.getImageHeight();

      texture.bind();
      
      glTranslatef((int)x, (int)y, 0);
      glRotatef(angle * RIGHT_ANGLE, 0.0f, 0.0f, 1.0f);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

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

  public static void rect(float x, float y, float sx, float sy, int texx,
      int texy, int texsx, int texsy, Texture texture)
  {
    rect(x, y, sx, sy, texx, texy, texsx, texsy,
        0, texture);
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
        sx, sy, texx, texy, texsx, texsy, angle, tex[texID]);
  }

  public static void rectCam(float x, float y, float sx, float sy, int texx,
      int texy, int texsx, int texsy, Texture texture)
  {
    rect(x-Camera.getX(), y-Camera.getY(), sx, sy, texx, texy, texsx, texsy,
        0, texture);
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


  public static void addSpreadsheetTexture(int png) throws IOException
  {
    try
    {
      Texture t = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/spreadsheets/"+png+".png")));
      sheets.add(t);
    } catch (IOException e)
    {
      throw new IOException();
    }
  }
  public static void tile(float x, float y, int texX, int texY, int texID)
  {
    Draw.rect(x, y, 16, 16, texX, texY, texX+Map.SIZE, texY+Map.SIZE, sheets.get(texID));
  }

  public static void tileCam(float x, float y, int texX, int texY, int texID)
  {
    Draw.tile(x-Camera.getX(), y-Camera.getY(), texX, texY, texID);
  }
  
  /**
   * Draws a sprite that loops around the screen (good for map border tiles).
   * 
   * @param x the X position on the screen, starting from the left           
   * @param y the Y position on the screen, starting from the <i>bottom</i>  
   * @param sx the width                                                     
   * @param sy the height                                                    
   * @param texx X position on the picture, starting from the left           
   * @param texy Y position on the picture, starting from the <i>top</i>     
   * @param texID see {@link Draw#loadTextures() loadTextures}
   */
  public static void tileScroll(float x, float y, float sx, float sy, int texx, 
      int texy, int texID) {
    tile(x-Camera.getX()%Map.SIZE, y-Camera.getY()%Map.SIZE, texx, texy, texID);
  }

  public static int getTexX(int id)
  {    
    return (id % (SPREADSHEET_SIZE) % SPREADSHEET_ROW) * SPREADSHEET_ROW;
  }

  public static int getTexY(int id)
  {
    return ((id % SPREADSHEET_SIZE) / SPREADSHEET_ROW) * SPREADSHEET_ROW;
  }

  /**
   * @param id The number of the spreadsheet
   * @return The correct texture for the spreadsheet
   */
  public static int getTexture(int id)
  {
    if (id == -1) {
      return -1;
    }
    return id / SPREADSHEET_SIZE;
  }



  /**
   * Creates an approximated circle at the specified point
   * 
   * @param x the X position on the screen, starting from the left
   * @param y the Y position on the screen, starting from the <i>bottom</i>
   * @param radius The radius of the circle in pixels
   * @param r Red, max 255
   * @param g Green, max 255
   * @param b Blue, max 255
   * @param a Alpha value (Transparency), max 255
   */
  public static void circ(float x, float y, float radius, 
      float r, float g, float b, float a){
    glPushMatrix();
    {
      glTranslatef((int)x, (int)y, 0);
      glScalef((int)radius, (int)radius, 0);
      glDisable(GL_TEXTURE_2D);


      glBegin(GL_TRIANGLE_FAN);
      {
        glColor4f(r/255,g/255,b/255,a/255);
        glVertex2f(0, 0);
        for(int i = 0; i <= NUM_PIZZA_SLICES; i++){
          double angle = Math.PI * 2 * i/NUM_PIZZA_SLICES;
          glVertex2f((float)Math.cos(angle), (float)Math.sin(angle));
        }
        glColor3f(1, 1, 1);

      }
      glEnd();

      glEnable(GL_TEXTURE_2D);

    }
    glPopMatrix();
  }

  public static void circCam(float x, float y, float radius, 
      float r, float g, float b, float a){
    circ(x-Camera.getX(), y-Camera.getY(), radius, r, g, b, a);
  }

  public static void readSpreadsheets()
  {
    int texNum = 0;
    boolean hasFiles = true;

    while (hasFiles)
    {
      try 
      {
        addTexture(texNum);
        texNum++;
      } catch (IOException e)
      {
        hasFiles = false;
      }
    }
  }

  public static void addTexture(int png) throws IOException
  {
    try
    {
      Texture t = TextureLoader.getTexture("PNG", new FileInputStream(
          new File("res/spreadsheets/"+png+".png")));
      sheets.add(t);
    } catch (IOException e)
    {
      throw new IOException();
    }
  }

  public static int getNumberOfSpreadsheets()
  {
    return sheets.size();
  }

  public static void drawIfInBounds(Rectangle r, float x, float y, 
      float sx, float sy, int texx, int texy, int texsx, int texsy,
      int rot, int texId)
  {
    Rectangle obj = new Rectangle((int) x, (int) y, (int) sx, (int) sy);
    if (r.contains(obj))
    {
      Draw.rect(x, y, sx, sy, texx, texy, texsx, texsy, rot, texId);
    } else if (r.intersects(obj))
    {
      int scale = (int) sx / (texsx - texx);

      float drawX = Math.max(r.x, x);
      float drawY = Math.max(r.y, y);
      float drawMaxX = Math.min(r.x+r.width, x+sx);
      float drawMaxY = Math.min(r.y+r.height, y+sy);

      if ((int) drawY % 2 != 0 && (int) drawMaxY % 2 == 0)
      {
        drawY--;
      } else if ((int) drawY % 2 == 0 && (int) drawMaxY % 2 != 0)
      {
        drawMaxY++;
      }

      if ((int) drawX % 2 != 0 && (int) drawMaxX % 2 == 0)
      {
        drawX--;
      } else if ((int) drawX % 2 == 0 && (int) drawMaxX % 2 != 0)
      {
        drawMaxX++;
      }      
      float drawSx = (drawMaxX - drawX);
      float drawSy = (drawMaxY - drawY);

      int topOff = (int) ((y+sy-drawMaxY) / scale);
      int botOff = (int) ((drawY - y) / scale);
      int rightOff = (int) ((x+sx-drawMaxX) / scale);
      int leftOff = (int) ((drawX - x) / scale);

      Draw.rect(drawX, drawY, drawSx, drawSy, texx + leftOff,
          texy + topOff, texsx - rightOff, texsy -
          botOff, rot, texId);

    }
  }

  public static void drawIfInBounds(Rectangle r, float x, float y, 
      float sx, float sy, int texx, int texy, int texsx, int texsy,
      int texId)
  {
    drawIfInBounds(r, x, y, sx, sy, texx, texy, texsx, texsy, 0, texId);
  }

  public static void tileZoom(float x, float y, int texx, int texy, 
      int texID, float zoom, float pivx, float pivy)
  {
    rect((x-pivx)*zoom+pivx, (y-pivy)*zoom+pivy, 16*zoom, 16*zoom, texx, texy, 
        texx+16, texy+16, sheets.get(texID));
  }

  public static void rectZoom(float x, float y, float sx, float sy,
      int texx, int texy, int texsx, int texsy, int texID, float zoom,
      float ex, float ey)
  { 
    rect((x-ex)*zoom+ex, (y-ey)*zoom+ey, sx*zoom, sy*zoom, 
        texx, texy, texsx, texsy, texID);

    /*rectZoom(x, y, sx, sy, texx, texy, texsx, texsy, 0, texID, zoom,
        ex, ey);*/

  }

  public static void rectZoom(float x, float y, float sx, float sy,
      int texx, int texy, int texsx, int texsy, int rot, int texID, float zoom,
      float ex, float ey)
  { 
    rect((x-ex)*zoom+ex, (y-ey)*zoom+ey, sx*zoom, sy*zoom, 
        texx, texy, texsx, texsy, rot, texID);
  }}
