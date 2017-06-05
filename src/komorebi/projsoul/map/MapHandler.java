/**
 * MapHandler.java    Mar 20, 2017, 9:12:43 AM
 */
package komorebi.projsoul.map;

import static komorebi.projsoul.map.Map.SIZE;

import komorebi.projsoul.ai.Location;
import komorebi.projsoul.engine.CollisionDetector;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Main;
import komorebi.projsoul.entities.XPObject;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.entities.enemy.SmartEnemy;
import komorebi.projsoul.entities.player.Bruno;
import komorebi.projsoul.entities.player.Caspian;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.entities.player.Flannery;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.entities.player.Sierra;
import komorebi.projsoul.entities.sprites.NPCLoader;
import komorebi.projsoul.gameplay.Camera;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.TextHandler;
import komorebi.projsoul.script.text.Word;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Handles Maps
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

  
  static boolean isOutside;
  
  public static final float TOLERANCE = 0.5f;
  
  private static int[][] border = {{226, 226},
                                   {226, 226}};
  
  public static boolean isHitBox;
    
  private MapHandler(){}
  
  public static void initialize(String firstMap){
    NPCLoader.initialize();
    
    activeMap = MapLoader.loadMap(firstMap, 0, 0);
    
    caspian = new Caspian(0,0);
    flannery = new Flannery(0,0);
    sierra = new Sierra(0,0);
    bruno = new Bruno(0,0);
    
    play = caspian;
    
    borderMaps = new ArrayList<Map>();
    
    for(Entry<String, Location> entry: activeMap.getBorders().entrySet()){
      borderMaps.add(MapLoader.loadMap(entry.getKey(), entry.getValue().x, 
                                                       entry.getValue().y));
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
    
    CollisionDetector.initialize(lowX, lowY, collision);
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
      switchMaps(currMapAddress);
    }
    
    //Removes all dead enemies from the computer's memory
    cleanUp();

  }

  private static void switchMaps(String currMapAddress) {
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
        borderMaps.add(MapLoader.loadMap(entry.getKey(),
                                         currOffX + entry.getValue().x, 
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
              Draw.getTexX(border[Math.abs(i%2)][Math.abs(j%2)]), 
              Draw.getTexY(border[Math.abs(i%2)][Math.abs(j%2)]), 1);

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
  
//  /**
//   * To only be used by the EditorMap
//   * 
//   * @param collision The new collision of the map
//   */
//  public static void setCollision(boolean[][] collision){
//    MapHandler.collision = collision;
//    CollisionDetector.initialize(lowX, lowY, collision);
//  }
  
  static void addEnemy(Enemy e){
    enemies.add(e);
  }
  
}
