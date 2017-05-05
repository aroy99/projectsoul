/**
 * Map.java    May 30, 2016, 11:32:19 AM
 */

package komorebi.projsoul.map;

import static komorebi.projsoul.engine.Main.HEIGHT;
import static komorebi.projsoul.engine.Main.WIDTH;
import static komorebi.projsoul.map.Map.SIZE;

import komorebi.projsoul.ai.Location;
import komorebi.projsoul.audio.Song;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.SignPost;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.gameplay.Camera;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.script.AreaScript;
import komorebi.projsoul.script.Script;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Represents a map of tiles
 * 
 * @author Aaron Roy
 */
public class Map implements Playable{

  private TileList[][] tiles;                //The Map itself
  private boolean[][] collision;

  public static final int SIZE = 16;  //Width and height of a tile
  
  private float drawOffsetX, drawOffsetY;
  
  private ArrayList<NPC> npcs;
  private ArrayList<AreaScript> scripts;
  private ArrayList<SignPost> signs;
    
  private String title;                 //The in-game name of this map
  private Song song;                    //The song this map uses

  private static Player play;
  
  private Rectangle rectangle;
  
  private HashMap<String, Location> borders;
  private String address;
  
  //DEBUG Map Debug variables
  public static boolean isGrid;
  
  public Map(TileList[][] tiles, boolean[][] collision, ArrayList<NPC> npcs, 
      ArrayList<AreaScript> scripts, ArrayList<SignPost> signs, String title,
      Song song){
    this.tiles = tiles;
    this.collision = collision;
    this.npcs = npcs;
    this.scripts = scripts;
    this.signs = signs;
    this.title = title;
    this.song = song;
    
    rectangle = new Rectangle((int)drawOffsetX, (int)drawOffsetY, 
                                tiles[0].length*SIZE, tiles.length*SIZE);
  }
  
  @Override
  public void getInput() {
    if(KeyHandler.keyClick(Key.G)){
      isGrid = !isGrid;
    }
  }

  @Override
  public void update() {
    play = MapHandler.getPlayer();

    updateNPCs();
    updateAreaScripts();
    updateSigns();
  }

  private void updateNPCs() {
    for (NPC npc : npcs) {
      if (npc != null) {
        npc.update();

        if (npc.isApproached(play.getArea(), play.getDirection()) && 
            KeyHandler.keyClick(Key.C)) {
          // DEBUG NPC turning
          npc.turn(play.getDirection().opposite());
          npc.approach();
        }

        if (!npc.started()) {
          npc.runWalkingScript();
        }

      }
    }
  }

  private void updateAreaScripts() {
    for (AreaScript script : scripts) {
      if (script.isLocationIntersected(play.getTileX(), play.getTileY()) && 
          !script.hasRun()) {
        script.run();
      }
    }
  }

  private void updateSigns() {
    for (SignPost sign : signs) {
      if (sign.isApproached(play.getArea(), play.getDirection()) && 
          KeyHandler.keyClick(Key.C)) {
        sign.show();
      }
    }
  }

  @Override
  public void render() {
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles[0].length; j++) {
        if (checkTileInBounds(drawOffsetX + j * SIZE, drawOffsetY + i * SIZE)) {
          Draw.rectCam(drawOffsetX + j * SIZE, drawOffsetY + i * SIZE, SIZE,
              SIZE, tiles[i][j].getX(), tiles[i][j].getY(), 1);

          
          // DEBUG Grid
          if (isGrid) {
            Draw.rectCam(drawOffsetX + j * SIZE, drawOffsetY + i * SIZE, SIZE,
                SIZE, 0, 16, SIZE, 16 + SIZE, 2);
          }

        }
      }
    }

    for (NPC npc : npcs) {
      if (npc != null) {
        npc.render();
      }
    }

    for (SignPost sign : signs) {
      sign.render();
    }
    
    //DEBUG Render Scripts
    for(AreaScript script: scripts){
      script.render();
    }

  }

  public AreaScript getScript(String s) {
    for (AreaScript scr : scripts) {
      if (scr != null) {
        if (scr.getName().equals(s)) {
          return scr;
        }
      }
    }

    return null;
  }

  /**
   * Finds the npc with the specified name
   * 
   * @param npcs
   *          The list to search
   * @param s
   *          The name of the NPC
   * @return The NPC if found, null if not
   */
  public NPC findNPC(String s) {

    for (NPC npc : npcs) {
      if (npc != null) {
        if (npc.getName().equals(s)) {
          return npc;
        }
      }
    }

    return null;
  }

  /**
   * @return Whether the tile is on the screen
   */
  boolean checkTileInBounds(float x, float y) {
    x -= Camera.getX();
    y -= Camera.getY();

    return x+32 > 0 && x < WIDTH && y+32 > 0 && y < HEIGHT;
  }
  
  /**
   * Checks the collisions between all four points of the character
   * 
   * @param x Player's X
   * @param y Player's Y
   * @param dx Delta x of Player
   * @param dy Delta y of Player
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
    if(MapHandler.isOutside()){
      return new boolean[]{true, true, true};
    }
    
    //TODO Figure out what this code means and document it better
    
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

  public int getTileWidth(){
    return tiles[0].length;
  }

  public int getTileHeight(){
    return tiles.length;
  }
  
  public int getPxWidth(){
    return tiles[0].length*SIZE;
  }

  public int getPxHeight(){
    return tiles.length*SIZE;
  }

  public void setTile(TileList tile, int x, int y)
  {
    tiles[x][y] = tile;
  }

  public ArrayList<NPC> getNPCs()
  {
    return npcs;
  }
  
  public Song getSong(){
    return song;
  }
  
  public void setSong(Song newSong){
    song = newSong;
  }
  
  public HashMap<String, Location> getBorders(){
    return borders;
  }
  
  
    
  public void setBorders(HashMap<String, Location> borders) {
    this.borders = borders;
  }

  public String getTitle(){
    return title;
  }
  
  public boolean[][] getCollision(){
    return collision;
  }
  
  public Rectangle getRectangle(){
    return rectangle;
  }
  
  public float getDrawOffsetX() {
    return drawOffsetX;
  }

  public float getDrawOffsetY() {
    return drawOffsetY;
  }
  
  public int getTileOffsetX() {
    return (int)drawOffsetX/SIZE;
  }

  public int getTileOffsetY() {
    return (int)drawOffsetY/SIZE;
  }

  public void setOffset(int tx, int ty){
    drawOffsetX = tx*SIZE;
    drawOffsetY = ty*SIZE;
    
    rectangle = new Rectangle((int)drawOffsetX, (int)drawOffsetY, 
        tiles[0].length*SIZE, tiles.length*SIZE);
  }

  /**
   * @return The filename of this map
   */
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Cleans up all outstanding threadas
   */
  public void cleanUp() {
    for(NPC npc: npcs){
      npc.cleanUp();
    }
    for(AreaScript script: scripts){
      script.close();
    }
  }

}

