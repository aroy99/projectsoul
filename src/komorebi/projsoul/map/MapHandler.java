/**
 * MapHandler.java    Mar 20, 2017, 9:12:43 AM
 */
package komorebi.projsoul.map;

import static komorebi.projsoul.engine.Main.HEIGHT;
import static komorebi.projsoul.engine.Main.WIDTH;
import static komorebi.projsoul.map.Map.SIZE;

import komorebi.projsoul.ai.Location;
import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.audio.Song;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Main;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.entities.SignPost;
import komorebi.projsoul.entities.XPObject;
import komorebi.projsoul.entities.enemy.Chaser;
import komorebi.projsoul.entities.enemy.Dummy;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.entities.enemy.EnemyType;
import komorebi.projsoul.entities.enemy.Shooter;
import komorebi.projsoul.entities.enemy.SmartEnemy;
import komorebi.projsoul.entities.enemy.Tackler;
import komorebi.projsoul.entities.player.Bruno;
import komorebi.projsoul.entities.player.Caspian;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.entities.player.Flannery;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.entities.player.Sierra;
import komorebi.projsoul.gameplay.Camera;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.script.AreaScript;
import komorebi.projsoul.script.EarthboundFont;
import komorebi.projsoul.script.Script;
import komorebi.projsoul.script.TalkingScript;
import komorebi.projsoul.script.TextHandler;
import komorebi.projsoul.script.WalkingScript;
import komorebi.projsoul.script.WarpScript;
import komorebi.projsoul.script.Word;

import org.junit.Ignore;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Loads and Handles Maps
 *
 * @author Aaron Roy
 */
public class MapHandler {
  
  private static Player play;
  
  private static Caspian caspian;
  private static Flannery flannery;
  private static Sierra sierra;
  private static Bruno bruno;
  
  private static ArrayList<Enemy>  enemies = new ArrayList<Enemy>();     
  private static ArrayList<XPObject> xpObj = new ArrayList<XPObject>();
  
  private static Map activeMap;
  private static ArrayList<Map> borderMaps;
  
  private static TextHandler currMap;
  
  private static final int MAX_DISPLAY_COUNT = 120;
  private static final int MAX_DISPLAY_Y = 15;

  private static int mapDisplayCount = MAX_DISPLAY_COUNT;
  private static int displayY = MAX_DISPLAY_Y;
  
  //The collision map runs on a different coordinate system than the rest of
  //the map. 0,0 on the collision map is equal to 
  //0 - the leftmost border map, 0 - the bottommost border map
  private static boolean[][] collision;
  
  //The offset between the map and collision map, in tiles
  private static int lowX, lowY;
  private static int highX, highY;

  
  private static boolean isOutside;
  
  private static final float TOLERANCE = 0.5f;
  
  private static TileList[][] border = {{TileList.dBLANK, TileList.dBLANK},
                                        {TileList.dBLANK, TileList.dBLANK}};
  
  public static boolean isHitBox;
    
  private MapHandler(){}
  
  public static void initialize(String firstMap){
    activeMap = loadMap(firstMap, 0, 0);
    
    caspian = new Caspian(0,0);
    flannery = new Flannery(0,0);
    sierra = new Sierra(0,0);
    bruno = new Bruno(0,0);
    
    play = caspian;
    
    borderMaps = new ArrayList<Map>();
    
    for(Entry<String, Location> entry: activeMap.getBorders().entrySet()){
      borderMaps.add(loadMap(entry.getKey(), entry.getValue().x, entry.getValue().y));
    }
    
    Camera.center(play.getX(), play.getY(), 
        activeMap.getTileWidth(), activeMap.getTileHeight());
    
    createCollision();
       
    currMap = new TextHandler();
    currMap.write(MapHandler.getActiveMap().getTitle(), 5, Main.HEIGHT-13);
    
  }

  private static void createCollision() {
    lowX = activeMap.getTileOffsetX();
    lowY = activeMap.getTileOffsetY();
    highX = lowX + activeMap.getTileWidth();
    highY = lowY + activeMap.getTileHeight();
    
    for(Map map : borderMaps){
      if(map.getTileOffsetX() < lowX){
        lowX = map.getTileOffsetX();
      }
      if(map.getTileOffsetY() < lowY){
        lowY = map.getTileOffsetY();
      }
      
      if(map.getTileOffsetX()+map.getTileWidth() > highX){
        highX = map.getTileOffsetX()+map.getTileWidth();
      }
      if(map.getTileOffsetY()+map.getTileHeight() > highY){
        highY = map.getTileOffsetY()+map.getTileHeight();
      }
    }
    
    System.out.format("Low: %d, %d; High: %d, %d\n", lowX, lowY, highX, highY);
    
    collision = new boolean[highY-lowY][highX-lowX];
    
    for (int i = 0; i < collision.length; i++) {
      for (int j = 0; j < collision[0].length; j++) {
        collision[i][j] = false;
      }
    }
    
    for(Map map : borderMaps){
      addToCollision(map);
    }
    addToCollision(activeMap);
    
    for(Enemy e: enemies){
      if(e instanceof SmartEnemy){
        ((SmartEnemy)e).initGrid();
      }
    }
  }

  private static void addToCollision(Map map) {
    boolean[][] col = map.getCollision();
    
    int lowXBound = map.getTileOffsetX() - lowX;
    int lowYBound = map.getTileOffsetY() - lowY;
    
    int highXBound = map.getTileOffsetX()+map.getTileWidth()  - lowX;
    int highYBound = map.getTileOffsetY()+map.getTileHeight() - lowY;
    
    for(int i = lowYBound; i < highYBound; i++){
      for(int j = lowXBound; j < highXBound; j++){
        collision[i][j] = col[i-lowYBound][j-lowXBound];
      }
    }
  }
  
  public static Map loadMap(String key, int offX, int offY){
    try {
      
      //DEBUG Printing maps as they load
      System.out.format("%s, %d, %d\n", key, offX, offY);
      
      int currOffX = 0; 
      int currOffY = 0;
           
      BufferedReader reader = new BufferedReader(new FileReader(
          new File("res/maps/" + key)));
      int rows = Integer.parseInt(reader.readLine());
      int cols = Integer.parseInt(reader.readLine());

      
      String title;                 //The in-game name of this map
      Song song;                    //The song this map uses
            
      //instantiates the necessary arrays and arraylists
      TileList[][] tiles = new TileList[rows][cols];
      boolean[][] collision = new boolean[rows][cols];
      ArrayList<NPC> npcs = new ArrayList<NPC>();
      ArrayList<AreaScript> scripts = new ArrayList<AreaScript>();
      ArrayList<SignPost> signs = new ArrayList<SignPost>();
            
      HashMap<String, Location> borderLocs = new HashMap<>();
      
      title = reader.readLine();
      song = Song.getSong(reader.readLine());
      if(song == null){
        song = Song.NONE;
      }
      isOutside = Integer.parseInt(reader.readLine()) == 1?true : false;
      
      //DEBUG Text
//      System.out.println("Title: " + title);
//      System.out.println("Song: " + song);
//      System.out.println(isOutside);
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
      
      do {
        if (s == null) {
          break;
        }
        if (s.startsWith("npc")) {
          s = s.replace("npc ", "");
          
          createNewNPC(npcs, s, currOffX + offX, currOffY + offY);
          
        } else if (s.startsWith("script")) {
          s = s.replace("script ", "");
          String[] split = s.split(" ");

          int arg0 = Integer.parseInt(split[2]) + currOffX + offX;
          int arg1 = Integer.parseInt(split[1]) + currOffY + offY;

          scripts.add(new AreaScript(split[0], arg0, arg1, false,
              findNPC(npcs, split[3])));
        } else if (s.startsWith("warp")) {
          s = s.replace("warp ", "");
          String[] split = s.split(" ");

          int arg0 = Integer.parseInt(split[2]) + currOffX + offX;
          int arg1 = Integer.parseInt(split[1]) + currOffY + offY;

          scripts.add(new WarpScript(split[0], arg1, arg0, false));
        } else if (s.startsWith("enemy")) {
          s = s.replace("enemy ", "");

          createNewEnemy(s, currOffX + offX, currOffY + offY);

        } else if (s.startsWith("sign")) {
          s = s.replace("sign ", "");
          String[] split = s.split(" ", 3);

          int arg0 = Integer.parseInt(split[0]) + currOffX + offX;
          int arg1 = Integer.parseInt(split[1]) + currOffY + offY;

          signs.add(new SignPost(arg0 * 16, arg1 * 16, split[2]));

        } else if (s.startsWith("connect")) {
          s = s.replace("connect ", "");
          String[] split = s.split(" ");

          int offsetX = Integer.parseInt(split[2]);
          int offsetY = Integer.parseInt(split[3]);
          
          borderLocs.put(split[0] + ".map", new Location(offsetX, offsetY));
        }
      } 
      while ((s = reader.readLine()) != null);

      
      for (Script script : scripts) {
        script.read();
      }

      for (NPC npc : npcs) {
        npc.getWalkingScript().read();
        npc.getTalkingScript().read();
      }

      reader.close();
      
      Map returnee = new Map(tiles, collision, npcs, scripts, signs, title, song);
      returnee.setBorders(borderLocs);
      returnee.setOffset(offX, offY);
      returnee.setAddress(key);
      
      return returnee;

    } catch (IOException | NumberFormatException e) {
      e.printStackTrace();
    }
    
    return null;
  }

  private static void createNewNPC(ArrayList<NPC> npcs, String s, int offX, int offY) {
    String[] split = s.split(" ");

    int arg0 = Integer.parseInt(split[1]) + offX;
    int arg1 = Integer.parseInt(split[2]) + offY;
    NPC n;
    npcs.add(n = new NPC(split[0], arg0 * 16, arg1 * 16,
        NPCType.toEnum(split[3])));

    n.setWalkingScript(new WalkingScript(split[4], n));
    n.setTalkingScript(new TalkingScript(split[5], n));
  }

  private static void createNewEnemy(String s, int offX, int offY) {
    String[] split = s.split(" ");

    int arg0 = Integer.parseInt(split[0]) + offX;
    int arg1 = Integer.parseInt(split[1]) + offY;
    
    switch(split[3]){
      case "none":
        enemies.add(new Dummy(arg0*16, arg1*16, EnemyType.toEnum(split[2]), 1));
        break;
      case "chaser":
        enemies.add(new Chaser(arg0*16, arg1*16, EnemyType.toEnum(split[2]),
            Integer.parseInt(split[4])));
        break;
      case "smart":
        enemies.add(new SmartEnemy(arg0*16, arg1*16, EnemyType.toEnum(split[2]),
            Integer.parseInt(split[4])));
        break;
      case "shooter":
        enemies.add(new Shooter(arg0*16, arg1*16, EnemyType.toEnum(split[2]),
            Integer.parseInt(split[4])));
        break;
      case "tackler":
        enemies.add(new Tackler(arg0*16, arg1*16, EnemyType.toEnum(split[2]),
            Integer.parseInt(split[4])));
        break;

      default:
        System.out.format("Enemy %s is invalid\n", s);
        break;
    }
  }
  
  /**
   * Finds the npc with the specified name
   * 
   * @param npcs The list to search
   * @param s The name of the NPC
   * @return The NPC if found, null if not
   */
  private static NPC findNPC(ArrayList<NPC> npcs, String s) {

    for (NPC npc : npcs) {
      if (npc != null) {
        if (npc.getName().equals(s)) {
          return npc;
        }
      }
    }

    return null;
  }
  
  public static void getInput() {

    activeMap.getInput();
    
    play.getInput();

    // DEBUG Special map functions
    if(KeyHandler.keyClick(Key.H)){
      isHitBox = !isHitBox;
    }
  }

  
  public static void update(){
    activeMap.update();
    play.update();
        
    for (Enemy enemy : enemies) {
      enemy.update();
    }
    
    if (KeyHandler.keyClick(Key.SPACE))
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

    if(KeyHandler.keyClick(Key.F)){
      System.out.println(findEntity(play.getHitBox()));
    }
    
    if(KeyHandler.keyClick(Key.B)){
      for(String s: activeMap.getBorders().keySet()){
        System.out.println("Border: " + s);
      }
    }
    
    String currMapAddress = findEntity(play.getHitBox());
    
    if(!activeMap.getAddress().equals(currMapAddress) && currMapAddress !=  null){
      for(int i = 0; i < borderMaps.size(); i++){
        Map newMap = borderMaps.get(i);
        
        if(newMap.getAddress().equals(currMapAddress)){
          for(Enemy enemy: enemies){
            if(!findEntity(enemy.getHitBox()).equals(currMapAddress) &&
                !findEntity(enemy.getHitBox()).equals(activeMap.getAddress())){
              enemy.kill();
              System.out.println("Killed "+ enemy.getType());
            }
          }
          
          Map temp = activeMap;
          activeMap = newMap;
          
          for(Map map: borderMaps){
            if(map != temp){
              map.cleanUp();
            }
          }
          
          borderMaps.clear();
          borderMaps.add(temp);
          
          loadNeighbors();
          createCollision();
          
          mapDisplayCount = MAX_DISPLAY_COUNT;
          displayY = MAX_DISPLAY_Y;
          
          System.out.println("Welcome to " + activeMap.getAddress() + "!");
          break;
        }
      }
    }
    
    //Removes all dead enemies from the computer's memory
    cleanUp();

  }
 
  /**
   * Loads the neighbors of the activeMap
   */
  private static void loadNeighbors() {
    int currOffX = activeMap.getTileOffsetX();
    int currOffY = activeMap.getTileOffsetY();
    
    outer:
      for(Entry<String, Location> entry: activeMap.getBorders().entrySet()){
        for(Map map: borderMaps){
          if(map.getAddress().equals(entry.getKey())){
            System.out.println("Skipped " + map.getAddress());
            continue outer;
          }
        }
        borderMaps.add(loadMap(entry.getKey(), currOffX + entry.getValue().x, 
            currOffY + entry.getValue().y));
        
        System.out.println("LOOK AT ME: " + entry.getKey());
      }
  }

  /**
   * @return The map the player is on
   */
  private static String findEntity(Rectangle hitbox) {
    if(activeMap.getRectangle().intersects(hitbox)){
      return activeMap.getAddress();
    }
    
    for(Map map: borderMaps){
      if(map.getRectangle().intersects(hitbox)){
        return map.getAddress();
      }
    }
    
    return null;
  }

  public static void render(){
    if(isOutside){
      drawBorder();
    }
    
    if(KeyHandler.keyClick(Key.O)){
      isOutside = !isOutside;
    }
        
    for(Map map: borderMaps){
      map.render();
    }
    
    activeMap.render();
    
    play.render();

    for (Enemy enemy: enemies)
    {
      enemy.render();
    }

    for (XPObject xp: xpObj)
    {
      xp.render();
    }
    
    //DEBUG Draw Player hitbox
    if(isHitBox){
      renderHitboxes();
    }

    play.renderHUD();
    
    if(mapDisplayCount > 0 || displayY > 0){
      Draw.rect(2, Main.HEIGHT-displayY, 100, 12, 1, 1, 2, 2, 6);
      currMap.render(new Word(activeMap.getTitle(), 
          5, Main.HEIGHT-displayY+2, new EarthboundFont(1)));
      mapDisplayCount--;
      
      if(mapDisplayCount < 0){
        displayY--;
      }
    }
    
  }
  
  private static void drawBorder() {
    
    for (int i = -2; i < 16; i++) {
      for (int j = -2; j < 18; j++) {
        if(checkBorderTileNotInMap(j*SIZE, i*SIZE)){
          Draw.rectScroll(j*SIZE, i*SIZE, SIZE, SIZE, 
              border[Math.abs(i%2)][Math.abs(j%2)].getX(), 
              border[Math.abs(i%2)][Math.abs(j%2)].getY(), 1);

          //DEBUG Grid
          if(Map.isGrid){
            Draw.rectCam(j*SIZE, i*SIZE, SIZE, SIZE, 0, 16, SIZE, 16+SIZE, 2);
          }

        }
      }
    }
  }
  
  /**
   * @return Whether the tile is on the screen and not covered by another tile
   */
  private static boolean checkBorderTileNotInMap(int x, int y) {

    x += Camera.getX();
    y += Camera.getY();

   
    if(activeMap.getRectangle().contains(new Point(x, y))){
      return false;
    }
    //TODO fix border map borders
    for(Map map: borderMaps){
      if(map.getRectangle().contains(new Point(x, y))){
        return false;
      }
    }
    
    return true;
  }


  /**
   * Helps print out additional info about the border tile
   * Warning: Lags the game to death
   */
  @SuppressWarnings("unused")
  private static void printBorderStatus(int x, int y) {
    Rectangle r = activeMap.getRectangle();
  
    System.out.format("Rectangle: %d, %d, %d, %d", r.x,r.y,r.width,r.height);
    System.out.format(", \tPoint %d, %d", x,y);
    System.out.format(", \tCamera %d, %d\t", (int)Camera.getX(), (int)Camera.getY());
    System.out.println(!activeMap.getRectangle().contains(new Point(x, y)));
  }



  private static void renderHitboxes() {
    Draw.rectCam((int)play.getX(), (int)play.getY(), 16, 16, 18, 16, 18, 16, 2);
    
    //DEBUG Show Hitboxes
    for (int i = 0; i < collision.length; i++) {
      for (int j = 0; j < collision[0].length; j++) {
        if(!collision[i][j] && activeMap.checkTileInBounds(j*SIZE+lowX*SIZE, i*SIZE+lowY*SIZE)){
          Draw.rectCam(j*SIZE+lowX*SIZE, i*SIZE+lowY*SIZE, SIZE, SIZE, 
              16, 16, 16, 16, 2);
        }
      }
    }
  }
  
  
  //TODO Move to a Physics class
  /**
   * Checks the collisions between all four points of the character
   * 
   * @param x Player's X
   * @param y Player's Y
   * @param dx Delta x of Player
   * @param dy Delta y of Player
   * @return {Never, Eat, Slimy, Worms}
   */
  public static boolean[] checkCollisions(float x, float y, float dx, float dy){
    //Speed affected
    int x1 = (int)((x-16+dx)/SIZE)+1-lowX; //Left
    int y1 = (int)((y-16+dy)/SIZE)+1-lowY; //Bottom

    int bufX = Math.abs(x1*SIZE - (int) (x +dx));
    int bufY = Math.abs(y1*SIZE - (int) (y +dy));   

    int x2 = (int)((x-1+dx)/SIZE)+1-lowX;  //Right
    int y2 = (int)((y-1+dy)/SIZE)+1-lowY;  //Top

    //Speed Unaffected
    int x3 = (int)((x-16)/SIZE)+1-lowX; //Left
    int y3 = (int)((y-16)/SIZE)+1-lowY; //Bottom

    int x4 = (int)((x-1)/SIZE)+1-lowX;  //Right
    int y4 = (int)((y-1)/SIZE)+1-lowY;  //Top
    
    if(x < 0){
      x1--;x2--;x3--;x4--;
    }
    if(y < 0){
      y1--;y2--;y3--;y4--;
    }

    boolean[] ret = new boolean[4];
        
    ret[1] = x2 < collision[0].length-1; //East
    ret[3] = x1-1 >= 0; //West
    ret[0] = y2 < collision.length-1; //North
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
  
  public static void guidePlayer(float x, float y, float dx, float dy)
  {
    
    //Speed affected
    int x1 = (int)((x-16+dx)/SIZE)+1-lowX; //Left
    int y1 = (int)((y-16+dy)/SIZE)+1-lowY; //Bottom

    int bufX = Math.abs(x1*SIZE - (int) (x +dx));
    int bufY = Math.abs(y1*SIZE - (int) (y +dy));   

    int x2 = (int)((x-1+dx)/SIZE)+1-lowX;  //Right
    int y2 = (int)((y-1+dy)/SIZE)+1-lowY;  //Top

    //Speed Unaffected
    int x3 = (int)((x-16)/SIZE)+1-lowX; //Left
    int y3 = (int)((y-16)/SIZE)+1-lowY; //Bottom

    int x4 = (int)((x-1)/SIZE)+1-lowX;  //Right
    int y4 = (int)((y-1)/SIZE)+1-lowY;  //Top

    if(x < 0){
      x1--;x2--;x3--;x4--;
    }
    if(y < 0){
      y1--;y2--;y3--;y4--;
    }
    
    boolean[] ret = new boolean[4];

    if(!MapHandler.isOutside()){
      ret[1] = x2 < collision[0].length; //East
      ret[3] = x1-1 >= 0; //West
      ret[0] = y2 < collision.length; //North
      ret[2] = y1-1 >= 0; //South
    }else{
      for (int i = 0; i < ret.length; i++) {
        ret[i] = true;
      }
    }


    if (ret[0] && (collision[y2][x3] ^ collision[y2][x4]))
    {
      //Player moving up
      if (collision[y2][x3] && (16 - bufX) >= 10)
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
      } else if (collision[y1][x4] && bufX >= 10)
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

  
  public static boolean[][] getCollision() {
    return collision;
  }

  public static Player getPlayer() {
    return play;
  }

  public static Map getActiveMap() {
    return activeMap;
  }

  public static ArrayList<Enemy> getEnemies() {
    return enemies;
  }

  public static int getLowX() {
    return lowX;
  }

  public static int getLowY() {
    return lowY;
  }

  public static void cleanUp() {
    for (Iterator<Enemy> it = enemies.iterator(); it.hasNext();) {
      Enemy enemy = it.next();
      if (enemy.dead()) {
        it.remove();
      }
    }
  }

  public static void switchPlayer() {
    switch (play.getCharacter()) {
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

  public static Characters currentPlayer() {
    return play.getCharacter();
  }

  public static void addXPObject(XPObject xp) {
    xpObj.add(xp);
  }

  /**
   * Calculates the distance between the enemy and the player
   * 
   * @param x
   *          The x of the enemy
   * @param y
   *          The y of the enemy
   * @param tarX
   *          The target X (i.e., the x of the player)
   * @param tarY
   *          The target Y (i.e., the y of the player)
   * @return The distance, as a double
   */
  public static float distanceBetween(float x, float y, float tarX,
      float tarY) {
    return (float) Math.sqrt(Math.pow((x - tarX), 2) + Math.pow((y - tarY), 2));
  }

  public static float angleOf(float x, float y, float tarX, float tarY) {
    float triX = x - tarX, triY = y - tarY;
    double ret = Math.atan(triY / triX) * (180 / Math.PI);

    if (triX < 0 && triY > 0) {
      ret += 180;
    } else if (triX < 0 && triY < 0) {
      ret -= 180;
    }

    return (float) ret;
  }

  public static int quadrantOf(float x, float y, float tarX, float tarY) {
    double angle = angleOf(x, y, tarX, tarY);

    if (angle > 0.5 && angle < 89.5) {
      return 1;
    } else if (angle > 90.5 && angle < 179.5) {
      return 2;
    } else if (angle > -179.5 && angle < -90.5) {
      return 3;
    } else if (angle > -89.5 && angle < -0.5) {
      return 4;
    } else if (Math.abs(angle) < TOLERANCE || Math.abs(angle - 180) < TOLERANCE ||
        Math.abs(angle + 180) < TOLERANCE) {
      return 0;
    } else {
      return -1;
    }
  }

  public static void giveXP(Characters c, int xp) {
    switch (c) {
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
      default:
        break;
    }
  }

  public static float[] coordinatesAt(float cx, float cy, float dist,
      float ang) {
    float[] ret = new float[2];

    ret[0] = (float) (cx + Math.cos(ang * (Math.PI / 180)) * dist);
    ret[1] = (float) (cy + Math.sin(ang * (Math.PI / 180)) * dist);

    return ret;
  }

  public static boolean allPlayersDead() {
    return (caspian.getHealth() <= 0 && flannery.getHealth() <= 0 && 
        sierra.getHealth() <= 0 && bruno.getHealth() <= 0);
  }

  // TODO Unused
  public static boolean blockedByEnemy(Rectangle rectangle) {
    for (Enemy enemy : enemies) {
      if (enemy.getHitBox().intersects(rectangle)) {
        return true;
      }
    }

    return false;
  }
  
  public static boolean isOutside(){
    return isOutside;
  }
  
  public static void setCollision(boolean[][] collision){
    MapHandler.collision = collision;
  }
  
}
