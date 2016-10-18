/**
 * Map.java    May 30, 2016, 11:32:19 AM
 */

package komorebi.projsoul.map;

import static komorebi.projsoul.engine.Main.HEIGHT;
import static komorebi.projsoul.engine.Main.WIDTH;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import komorebi.projsoul.engine.Camera;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.entities.Bruno;
import komorebi.projsoul.entities.Caspian;
import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.entities.Chaser;
import komorebi.projsoul.entities.Enemy;
import komorebi.projsoul.entities.Flannery;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.entities.Player;
import komorebi.projsoul.entities.Sierra;
import komorebi.projsoul.entities.SignPost;
import komorebi.projsoul.script.AreaScript;
import komorebi.projsoul.script.Script;
import komorebi.projsoul.script.TalkingScript;
import komorebi.projsoul.script.WalkingScript;
import komorebi.projsoul.script.WarpScript;


/**
 * Represents a map of tiles
 * 
 * @author Aaron Roy
 * @version 
 */
public class Map implements Playable{

  private TileList[][] tiles;                //The Map itself
  private boolean[][] collision;

  public static final int SIZE = 16;  //Width and height of a tile

  private ArrayList<NPC> npcs;
  private ArrayList<AreaScript> scripts;
  private ArrayList<SignPost> signs;
  
  private static Player play;
  
  private static Caspian caspian;
  private static Flannery flannery;
  private static Sierra sierra;
  private static Bruno bruno;

  //TODO Debug
  private ArrayList<Enemy> enemies = new ArrayList<Enemy>();

  //Debug
  private boolean isHitBox;
  private boolean isGrid;



  /**
   * Creates a new Map of the dimensions col x row <br>
   * Really shouldn't be used anymore
   * @param col number of columns (x)
   * @param row number of rows (y)
   */
  @Deprecated
  public Map(int col, int row){

    tiles = new TileList[row][col];
    //npcs = new NPC[row][col];
    //scripts = new AreaScript[row][col];

    for (int i = tiles.length-1; i >= 0; i--) {
      for (int j = 0; j < tiles[0].length; j++) {
        tiles[i][j] = TileList.BLANK;
      }
    }

  }


  /**
   * Creates a map from a map file, used for the game
   * 
   * @param key The location of the map
   */
  public Map(String key){
    
    try {
      BufferedReader reader = new BufferedReader(new FileReader(
          new File(key)));
      int rows = Integer.parseInt(reader.readLine());
      int cols = Integer.parseInt(reader.readLine());

      
      //instantiates the necessary arrays and arraylists
      tiles = new TileList[rows][cols];
      collision = new boolean[rows][cols];
      npcs = new ArrayList<NPC>();
      scripts = new ArrayList<AreaScript>();
      signs = new ArrayList<SignPost>();
      
      for (int i = 0; i < tiles.length; i++) {
        String[] str = reader.readLine().split(" ");
        int index = 0;
        for (int j = 0; j < cols; j++, index++) {
          if(str[index].equals("")){
            index++;  //pass this token, it's blank
          }
          //reads the tile makeup of the map
          tiles[i][j] = TileList.getTile(Integer.parseInt(str[index]));


          //TODO This number will eventually have to be changed, as will the string
          if (tiles[i][j]==TileList.getTile(15))
          {
            signs.add(new SignPost(j, i, "First line\\nSecond line\\pNew paragraph"));
          }

          collision[i][j] = true;
        }
      }


      String s = reader.readLine();

      for (int i = 0; i < tiles.length; i++) {
        if(s == null || s.startsWith("npc")){
          break;
        }
        if(i != 0){
          s = reader.readLine();
        }
        String[] str = s.split(" ");
        int index = 0;
        for (int j = 0; j < cols; j++, index++) {
          if(str[index].equals("")){
            index++;  //pass this token, it's blank
          }
          collision[i][j]=str[index].equals("0")?true : false;
        }
      }

      do
      {
        if(s == null){
          break;
        }
        if (s.startsWith("npc"))
        {
          s = s.replace("npc ", "");
          String[] split = s.split(" ");

          int arg0 = Integer.parseInt(split[2]);
          int arg1 = Integer.parseInt(split[1]);
          NPC n;
          npcs.add(n=new NPC(split[0], arg0*16, arg1*16,  NPCType.toEnum(split[3])));

          n.setWalkingScript(new WalkingScript(split[4], n));
          n.setTalkingScript(new TalkingScript(split[5], n));
        } else if (s.startsWith("script"))
        {
          s = s.replace("script ", "");
          String[] split = s.split(" ");

          int arg0 = Integer.parseInt(split[2]);
          int arg1 = Integer.parseInt(split[1]);

          scripts.add(new AreaScript(split[0], arg0, arg1, false, 
              findNPC(split[3])));
        } else if (s.startsWith("warp"))
        {
          s = s.replace("warp ", "");
          String[] split = s.split(" ");

          int arg0 = Integer.parseInt(split[2]);
          int arg1 = Integer.parseInt(split[1]);

          scripts.add(new WarpScript(split[0], arg1, arg0, false));          
        }
      } while ((s=reader.readLine()) != null);

      for (Script script: scripts)
      {
        script.read();
      }

      for (NPC npc: npcs)
      {
        npc.getWalkingScript().read();
        npc.getTalkingScript().read();
      }

      reader.close();
      
      caspian = new Caspian(tiles[0].length/2*16,0);
      flannery = new Flannery(tiles[0].length/2*16,0);
      sierra = new Sierra(tiles[0].length/2*16,0);
      bruno = new Bruno(tiles[0].length/2*16,0);
      
      play = caspian;
      
      Camera.center(play.getX(), play.getY(), tiles[0].length*16, tiles.length*16);

    } catch (IOException | NumberFormatException e) {
      e.printStackTrace();
    }
    
    enemies.add(new Chaser(100, 100, 16, 21, 100));

  }


  /* (non-Javadoc)
   * @see komorebi.clyde.engine.Playable#getInput()
   */
  @Override
  public void getInput() {

    play.getInput();

    // TODO Debug
    if(KeyHandler.keyClick(Key.H)){
      isHitBox = !isHitBox;
    }

    if(KeyHandler.keyClick(Key.G)){
      isGrid = !isGrid;
    }
  }


  /* (non-Javadoc)
   * @see komorebi.clyde.engine.Renderable#update()
   */
  @Override
  public void update() {

    play.update();

    for (Enemy enemy: enemies)
    { 
      if (play instanceof Caspian)
      {
        enemy.updateHits(((Caspian) play).getAttackHitBox().intersects(enemy.getHitBox()) 
            && play.isAttacking(), (int) (25*(play.getAttack()/Player.MEAN_STAT)), play.getDirection());
      }
      enemy.update();

    }

    for (NPC npc: npcs) {
      if (npc != null) 
      {
        npc.update();

        if (npc.isApproached(play.getArea(), play.getDirection()) && 
            KeyHandler.keyClick(Key.C))
        {
          //TODO Debug
          npc.turn(play.getDirection().opposite());
          npc.approach();
        }

        if (!npc.started())
        {
          npc.runWalkingScript();
        }

      }
    }

    for (AreaScript script: scripts)
    {      
      if (script.isLocationIntersected(play.getTileX(), play.getTileY()) 
          && !script.hasRun()) {
        script.run();
      }
    }


    for (SignPost sign: signs)
    { 
      if (sign.isApproached(play.getArea(), play.getDirection())
          && KeyHandler.keyClick(Key.C))
      {
        sign.show();
      }
    }
    
    if (KeyHandler.keyClick(Key.SPACE))
    {
      switchPlayer();
    }

    //Removes all dead enemies from the computer's memory
    cleanUp();
  }

  /**
   * Renders the map on-screen
   */
  @Override
  public void render() {
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        if(checkTileInBounds(j*SIZE, i*SIZE)){
          Draw.rectCam((int)j*SIZE, (int)i*SIZE, SIZE, SIZE, 
              tiles[i][j].getX(), tiles[i][j].getY(), 1);

          //TODO Debug
          if(isGrid){
            Draw.rectCam((int)j*SIZE, (int)i*SIZE, SIZE, SIZE, 
                0, 16, SIZE, 16+SIZE, 2);
          }

        }
      }
    }



    for (NPC npc: npcs) {
      if (npc != null) 
      {
        npc.render();
      }
    }



    //TODO Debug
    if (isHitBox) {
      for (int i = 0; i < collision.length; i++) {
        for (int j = 0; j < collision[0].length; j++) {
          if(checkTileInBounds(j*SIZE, i*SIZE) && !collision[i][j]){
            Draw.rectCam((int)j*SIZE, (int)i*SIZE, SIZE, SIZE, 
                16, 16, 16, 16, 2);
          }        
        }
      }
    }

    play.render();

    for (Enemy enemy: enemies)
    {
      enemy.render();
    }

    for (SignPost sign: signs)
    {
      sign.render();
    }
    //TODO Debug
    if(isHitBox){
      Draw.rectCam((int)play.getX(), (int)play.getY(), 16, 16, 18, 16, 18, 16, 2);
    }
  }

  /**
   * 
   * @param s
   * @return
   */
  public NPC findNPC(String s)
  {

    for (NPC npc: npcs) {
      if (npc != null)
        if (npc.getName().equals(s)) return npc;
    }


    return null;
  }

  public AreaScript getScript(String s)
  {
    for (AreaScript scr: scripts)
    {
      if (scr!=null)
      {
        if (scr.getName().equals(s)) return scr;
      }
    }

    return null;
  }

  /**
   * @return Whether the tile is on the screen
   */
  private boolean checkTileInBounds(float x, float y) {
    x -= Camera.getX();
    y -= Camera.getY();

    return x+32 > 0 && x < WIDTH && y+32 > 0 && y < HEIGHT;
  }

  /**
   * Checks the collisions between all four points of the character
   * 
   * @param x Clyde's X
   * @param y Clyde's Y
   * @param dx Delta x of Clyde
   * @param dy Delta y of Clyde
   * @return {Never, Eat, Slimy, Worms}
   */
  public boolean[] checkCollisions(float x, float y, float dx, float dy){
    //Speed affected
    int x1 = (int)((x-16+dx)/16)+1; //Left
    int y1 = (int)((y-16+dy)/16)+1; //Bottom

    int bufX = Math.abs(x1*16 - (int) (x +dx));

    int x2 = (int)((x-1+dx)/16)+1;  //Right
    int y2 = (int)((y-1+dy)/16)+1;  //Top

    //Speed Unaffected
    int x3 = (int)((x-16)/16)+1; //Left
    int y3 = (int)((y-16)/16)+1; //Bottom

    int x4 = (int)((x-1)/16)+1;  //Right
    int y4 = (int)((y-1)/16)+1;  //Top

    boolean[] ret = new boolean[4];
        
    ret[1] = x2 < collision[0].length; //East
    ret[3] = x1-1 >= 0; //West
    ret[0] = y2 < collision.length; //North
    ret[2] = y1-1 >= 0; //South

    ret[0] = ret[0] && collision[y2][x3] && collision[y2][x4];  //North
    ret[2] = ret[2] && collision[y1][x3] && collision[y1][x4];  //South

    ret[1] = ret[1] && collision[y3][x2] && collision[y4][x2];  //East
    ret[3] = ret[3] && collision[y3][x1] && collision[y4][x1];  //West

    //TODO Debug
    if(KeyHandler.keyClick(Key.Q)){
      System.out.println(x1 + ", " + x2 + ", " + y1 + ", " + y2);
      System.out.println("dx: " + dx + ", dy: " + dy + "\n" + 
          collision[y2][x1]+ ", " +collision[y2][x2]+ ", \n" +
          collision[y1][x1]+ ", " +collision[y1][x2]);
      System.out.println("Never: " + ret[0] + ", Eat: " + ret[1] + 
          ", Slimy: " + ret[2] + ", Worms: " + ret[3]);
    }

    if (KeyHandler.keyClick(Key.B))
    {
      System.out.println(collision[y2][x3]);
      System.out.println(collision[y2][x4]);
      System.out.println(bufX);
    }

    return ret;
  }
  
  public void guidePlayer(float x, float y, float dx, float dy)
  {
    
    
      //Speed affected
      int x1 = (int)((x-16+dx)/16)+1; //Left
      int y1 = (int)((y-16+dy)/16)+1; //Bottom

      int bufX = Math.abs(x1*16 - (int) (x +dx));
      int bufY = Math.abs(y1*16 - (int) (y +dy));      
      int x2 = (int)((x-1+dx)/16)+1;  //Right
      int y2 = (int)((y-1+dy)/16)+1;  //Top

      //Speed Unaffected
      int x3 = (int)((x-16)/16)+1; //Left
      int y3 = (int)((y-16)/16)+1; //Bottom

      int x4 = (int)((x-1)/16)+1;  //Right
      int y4 = (int)((y-1)/16)+1;  //Top

      boolean[] ret = new boolean[4];
          
      ret[1] = x2 < collision[0].length; //East
      ret[3] = x1-1 >= 0; //West
      ret[0] = y2 < collision.length; //North
      ret[2] = y1-1 >= 0; //South

      
       if (ret[0] && (collision[y2][x3] ^ collision[y2][x4]))
       {
         //Player moving up
         if (collision[y2][x3] && (16 - bufX) >=10)
         {
           play.guide(-1, 0);
         } else if (collision[y2][x4] && bufX >= 10) {
           play.guide(1, 0);
         }
       } else if (ret[2] && (collision[y1][x3] ^ collision[y1][x4]))
       {
         //Player moving down
         if (collision[y1][x3] && (16 - bufX) >= 10)
         {
           play.guide(-1, 0);
         } else if (collision[y1][x4] && bufX>=10)
         {
           play.guide(1, 0);
         }
       } else if (ret[1] && (collision[y3][x2] ^ collision[y4][x2]))
       {
         //Player moving right
         if (collision[y3][x2] && (bufY <= 6))
         {
           play.guide(0, -1);
         } else if (collision[y4][x2] && (16 - bufY) <= 6)
         {
           play.guide(0, 1);
         }
         
       } else if (ret[3] && (collision[y3][x1] ^ collision[y4][x1]))
       {
         //Player moving left
         if (collision[y3][x1] && (bufY <= 6))
         {
           play.guide(0, -1);
         } else if (collision[y4][x1] && (16 - bufY) <= 6)
         {
           play.guide(0, 1);
         }
       }
     
  }

  public void setCollision(int x, int y, boolean tf)
  {
    collision[y][x] = tf;
  }
  /**
   * Returns two booleans based on if the map should move forward or not
   * 
   * @param x The x of the camera
   * @param y The y of the camera
   * @param dx The delta x of the camera
   * @param dy The delta y of the camera
   * @return {X, Y}
   */
  public boolean[] checkBoundaries(float x, float y, float dx, float dy){
    boolean[] ret = new boolean[2];

    //Entire Map < Screen -> Map is centered to screen, Camera Doesn't scroll
    //Map in one dimension > Camera -> Center to clyde in that dimension, Scroll in that dimension until the edge
    //If clyde is not centered and Map > Screen -> don't move in that dimension until he is


    ret[0] = tiles[0].length*16 > WIDTH && x+dx >= 0 && x+dx+WIDTH < tiles[0].length*16;

    //Left
    if(dx < 0){
      ret[0] = ret[0] && play.getX()-x < WIDTH/2-8 || x + WIDTH > tiles[0].length*16;

    }else if(dx > 0){ //Right
      ret[0] = ret[0] && play.getX()-x > WIDTH/2-8 || x < 0;
    }

    ret[1] = y+dy >= 0 && y+dy+HEIGHT < tiles.length*16 && 
        !(tiles.length*16 < HEIGHT);

    //Bottom
    if(dy < 0){
      ret[1] = ret[1] && play.getY()-y < HEIGHT/2-12 || y + HEIGHT > tiles.length*16;

    }else if(dy > 0){ //Top
      ret[1] = ret[1] && play.getY()-y > HEIGHT/2-12 || y < 0;
    }

    return ret;
  }

  public int getWidth(){
    return tiles[0].length;
  }

  public int getHeight(){
    return tiles.length;
  }

  public void setTile(TileList tile, int x, int y)
  {
    tiles[x][y] = tile;
  }

  public static Player getPlayer()
  {
    return play;
  }

  public ArrayList<NPC> getNPCs()
  {
    return npcs;
  }

  public boolean blockedByEnemy(Rectangle rectangle)
  {
    for (Enemy enemy: enemies)
    {
      if (enemy.getHitBox().intersects(rectangle))
        return true;
    }

    return false;
  }

  public ArrayList<Enemy> getEnemies()
  {
    return enemies;
  }

  public void cleanUp()
  {
    for (Iterator<Enemy> it = enemies.iterator(); it.hasNext();)
    {      
      Enemy enemy = it.next();
      if (enemy.dead())
      {
        it.remove();
      }

    }
  }
  
  public void switchPlayer()
  {
    switch (play.getCharacter())
    {
      case CASPIAN:
        flannery.setLocation(caspian.getX(), caspian.getY());
        play = flannery;
        break;
      case FLANNERY:
        sierra.setLocation(flannery.getX(), flannery.getY());
        play = sierra;
        break;
      case SIERRA:
        bruno.setLocation(sierra.getX(), sierra.getY());
        play = bruno;
        break;
      case BRUNO:
        caspian.setLocation(bruno.getX(), bruno.getY());
        play = caspian;
        break;
      default:
        break;
    }
  }
  
  public static Characters currentPlayer()
  {
    return play.getCharacter();
  }
  

}

