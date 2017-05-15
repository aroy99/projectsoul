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

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.audio.Song;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.entities.SignPost;
import komorebi.projsoul.entities.XPObject;
import komorebi.projsoul.entities.enemy.Chaser;
import komorebi.projsoul.entities.enemy.Dummy;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.entities.enemy.EnemyType;
import komorebi.projsoul.entities.player.Bruno;
import komorebi.projsoul.entities.player.Caspian;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.entities.player.Flannery;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.entities.player.Sierra;
import komorebi.projsoul.gameplay.Camera;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.script.utils.AreaScript;


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
  private static final float TOLERANCE = 0.5f;

  private ArrayList<NPC> npcs;
  private ArrayList<AreaScript> scripts;
  private ArrayList<SignPost> signs;
  private ArrayList<XPObject> xpObj;

  private static Player play;

  private static Caspian caspian;
  private static Flannery flannery;
  private static Sierra sierra;
  private static Bruno bruno;
  private ArrayList<Enemy> enemies;

  private String title;                 //The in-game name of this map
  private Song song;                    //The song this map uses
  private boolean outside;


  //DEBUG Map Debug variables
  public static boolean isHitBox;
  public static boolean isGrid;

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
    this(key, true);
  }
  
  public static Map createMapAndTransferPlayers(String name,
      Map map)
  {
    Map newMap = new Map(name, false);
    newMap.transferPlayers(map);
    
    return newMap;
  }
  
  private Map(String key, boolean initializePlayers)
  {
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
      xpObj = new ArrayList<XPObject>();
      enemies = new ArrayList<Enemy>();

      reader.mark(50);

      String test = reader.readLine();

      if(!test.substring(0, 1).matches("\\d")){
        title = test;
        song = Song.getSong(reader.readLine());
        if(song == null){
          song = Song.NONE;
        }
        outside = Integer.parseInt(reader.readLine()) == 1?true : false;
      }else{
        reader.reset();
      }

      //DEBUG Text
      System.out.println("Title: " + title);
      System.out.println("Song: " + song);
      System.out.println(outside);
      AudioHandler.play(song);

      for (int i = 0; i < tiles.length; i++) {
        String[] str = reader.readLine().split(" ");
        int index = 0;
        for (int j = 0; j < cols; j++, index++) {
          if(str[index].equals("")){
            index++;  //pass this token, it's blank
          }
          //reads the tile makeup of the map
          tiles[i][j] = TileList.getTile(Integer.parseInt(str[index]));
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

          n.setWalkingScript(split[4]);
          n.setTalkingScript(split[5]);
        } else if (s.startsWith("script"))
        {
          s = s.replace("script ", "");
          String[] split = s.split(" ");

          int arg0 = Integer.parseInt(split[2]);
          int arg1 = Integer.parseInt(split[1]);

          AreaScript areaScript = new AreaScript(split[0], arg0, arg1);
          areaScript.executeUpon(findNPC(split[3]));

          scripts.add(areaScript);
        } else if (s.startsWith("warp"))
        {
          s = s.replace("warp ", "");
          String[] split = s.split(" ");

          int arg0 = Integer.parseInt(split[2]);
          int arg1 = Integer.parseInt(split[1]);

          scripts.add(new AreaScript(split[0], arg1, arg0));          
        } else if (s.startsWith("enemy")){
          s = s.replace("enemy ", "");
          String[] split = s.split(" ");

          int arg0 = Integer.parseInt(split[0]);
          int arg1 = Integer.parseInt(split[1]);

          switch(split[3]){
            case "none":
              enemies.add(new Dummy(arg0*16, arg1*16, EnemyType.toEnum(split[2]), 1));
              break;
            case "chaser":
              enemies.add(new Chaser(arg0*16, arg1*16, EnemyType.toEnum(split[2]),
                  Integer.parseInt(split[4])));
              break;
            default:
              System.out.println("This shouldn't happen!");
              break;
          }
        } else if (s.startsWith("sign")){
          s = s.replace("sign ", "");
          String[] split = s.split(" ", 3);

          int arg0 = Integer.parseInt(split[0]);
          int arg1 = Integer.parseInt(split[1]);

          signs.add(new SignPost(arg0*16, arg1*16, split[2]));

        }
      } while ((s=reader.readLine()) != null);

      reader.close();

      if (initializePlayers)
      {
        caspian = new Caspian(tiles[0].length/2*16,0);
        flannery = new Flannery(tiles[0].length/2*16,0);
        sierra = new Sierra(tiles[0].length/2*16,0);
        bruno = new Bruno(tiles[0].length/2*16,0);

        play = caspian;
        
        Camera.center(play.getX(), play.getY(), tiles[0].length*16, tiles.length*16);
      }
     

    } catch (IOException | NumberFormatException e) {
      e.printStackTrace();
    }

    startNPCs();
  }

  private void startNPCs()
  {
    for (NPC npc: npcs)
    {
      npc.runWalkingScript();
    }
  }
  
  public void transferPlayers(Map map)
  {
    for (Characters c: Characters.values())
    {
      setPlayer(map.getPlayer(c), c);
    }
  }
  
  public void setPlayer(Player p, Characters c)
  {
    switch (c)
    {
      case BRUNO:
        bruno = (Bruno) p;
        break;
      case CASPIAN:
        caspian = (Caspian) p;
        break;
      case FLANNERY:
        flannery = (Flannery) p;
        break;
      case SIERRA:
        sierra = (Sierra) p;
        break;
      default:
        break;
      
    }
  }
  
  public Player getPlayer(Characters c)
  {
    switch (c)
    {
      case BRUNO:
        return bruno;
      case CASPIAN:
        return caspian;
      case FLANNERY:
        return flannery;
      case SIERRA:
        return sierra;
      default:
        return play;
      
    }
  }


  /* (non-Javadoc)
   * @see komorebi.clyde.engine.Playable#getInput()
   */
  @Override
  public void getInput() {

    play.getInput();

    // DEBUG Special map functions
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
      enemy.update();
    }
    
    for (NPC npc: npcs) {

      npc.update();
      
      if (npc.isApproached(play.getArea(), play.getDirection())
          && KeyHandler.firstKeyClick(Key.C))
      {                
        npc.turn(play.getDirection().opposite());
        npc.approach();
      }

    }

    for (AreaScript script: scripts)
    {      
      if (script.isLocationIntersected(play.getTileX(), play.getTileY()))
        ThreadHandler.newThread(script.getName(), null, play);
    }

    for (SignPost sign: signs)
    { 
      if (sign.isApproached(play.getArea(), play.getDirection()) &&
          KeyHandler.keyClick(Key.C))
      {
        sign.show();
      }
    }

    if (KeyHandler.keyClick(Key.SPACE) && !play.isLocked())
    {
      switchPlayer();
    }

    for (XPObject xp: xpObj)
    {
      if (xp.withinRadius(play.getHitBox()))
      {
        xp.guide(play.getX(), play.getY());
      } else
      {
        xp.setSpeed(0, 0);
      }

      xp.update();

      if (play.getHitBox().intersects(xp.getHitBox()))
      {
        xp.eat();
      }
    }

    for (Iterator<XPObject> it = xpObj.iterator(); it.hasNext();)
    {      
      XPObject xp = it.next();
      if (xp.destroyed())
      {
        it.remove();
      }

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

          //DEBUG Grid
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



    //DEBUG Show Hitboxes
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

    for (XPObject xp: xpObj)
    {
      xp.render();
    }

    //DEBUG Draw Player hitbox
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

    //DEBUG Show collision values
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
    if(outside){
      return new boolean[]{true, true, true};
    }

    boolean[] ret = new boolean[2];

    //Entire Map < Screen -> Map is centered to screen, Camera Doesn't scroll
    //Map in one dimension > Camera -> Center to clyde in that dimension, 
    //  Scroll in that dimension until the edge
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

  public void move(float dx, float dy)
  {
    for (NPC npc: npcs)
    {
      npc.move(dx, dy);
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

  public void addXPObject(XPObject xp)
  {
    xpObj.add(xp);
  }

  /**
   * Calculates the distance between the enemy and the player
   * @param x The x of the enemy
   * @param y The y of the enemy
   * @param tarX The target X (i.e., the x of the player)
   * @param tarY The target Y (i.e., the y of the player)
   * @return The distance, as a double
   */
  public static double distanceBetween(float x, float y, float tarX, float tarY)
  {
    return Math.sqrt(Math.pow((x-tarX), 2) + Math.pow((y-tarY), 2));
  }

  public static double angleOf(float x, float y, float tarX, float tarY)
  {
    float triX = x - tarX, triY = y - tarY;
    double ret = Math.atan(triY / triX)* (180 / Math.PI);

    if (triX < 0 && triY > 0)
    {
      ret+=180;
    } else if (triX < 0 && triY < 0)
    {
      ret-=180;
    }

    return ret;
  }

  public static int quadrantOf(float x, float y, float tarX, float tarY)
  {
    double angle = angleOf(x,y,tarX,tarY);

    if (angle > 0.5 && angle < 89.5)
    {
      return 1;
    } else if (angle > 90.5 && angle < 179.5)
    {
      return 2;
    } else if (angle > -179.5 && angle < -90.5)
    {
      return 3;
    } else if (angle > -89.5 && angle < -0.5)
    {
      return 4;
    } else if (Math.abs(angle)<TOLERANCE || Math.abs(angle-180)<TOLERANCE 
        || Math.abs(angle+180)<TOLERANCE)
    {
      return 0;
    } else
    {
      return -1;
    }
  }

  public void giveXP(Characters c, int xp)
  {
    switch (c)
    {
      case CASPIAN:
        caspian.giveXP(xp);
        break;
      case FLANNERY:
        flannery.giveXP(xp);
        break;
      case SIERRA:
        sierra.giveXP(xp);
        break;
      case BRUNO:
        bruno.giveXP(xp);
        break;
    }
  }

  public static float[] coordinatesAt(float cx, float cy, float dist, double ang)
  {
    float[] ret  = new float[2];

    ret[0] = (float) (cx + Math.cos(ang*(Math.PI / 180))*dist);
    ret[1] = (float) (cy + Math.sin(ang*(Math.PI / 180))*dist);

    return ret;
  }

  public static boolean allPlayersDead()
  {
    return (caspian.getHealth()<=0 && flannery.getHealth()<=0 && sierra.getHealth()<=0
        && bruno.getHealth()<=0);
  }

  public Song getSong(){
    return song;
  }

  public void setSong(Song newSong){
    song = newSong;
  }

  public boolean isOutside(){
    return outside;
  }

  public String getTitle(){
    return title;
  }

  public boolean willIntersectNPCs(Rectangle future)
  {
    for (NPC npc: npcs)
      if (future.intersects(npc.getArea()))
        return true;

    return false;
  }
  
  public void addNPC(NPC npc)
  {
    npcs.add(npc);
  }


}

