/**
 * Map.java    May 30, 2016, 11:32:19 AM
 */

package komorebi.projsoul.map;

import static komorebi.projsoul.engine.Main.HEIGHT;
import static komorebi.projsoul.engine.Main.WIDTH;

import komorebi.projsoul.ai.Location;
import komorebi.projsoul.audio.Song;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Playable;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.SignPost;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.gameplay.Camera;
import komorebi.projsoul.script.utils.AreaScript;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a map of tiles
 * 
 * @author Aaron Roy
 * @version 
 */
public class Map implements Playable{

  private int[][][] tiles;                //The Map itself
  private boolean[][] collision;

  public static final int SIZE = 16;  //Width and height of a tile
  public static final int LAYERS = 4;
  
  private float drawOffsetX, drawOffsetY;
  
  private ArrayList<NPC> npcs;
  private ArrayList<AreaScript> scripts;
  private ArrayList<SignPost> signs;
    
  private String title;                 //The in-game name of this map
  private Song song;                    //The song this map uses

  private static Player play;

  private Rectangle rectangle;

  private HashMap<String, Location> borders = new HashMap<String, Location>();
  private String address;

  //DEBUG Map Debug variables
  public static boolean isHitBox;
  public static boolean isGrid;



  /**
   * Creates a map from a map file, used for the game
   * 
   * @param key The location of the map
   */  
  public Map(int[][][] tiles, boolean[][] collision, ArrayList<NPC> npcs, 
      ArrayList<AreaScript> scripts, ArrayList<SignPost> signs, String title,
      Song song){
    this.tiles = tiles;
    this.collision = collision;
    this.npcs = npcs;
    this.scripts = scripts;
    this.signs = signs;
    this.title = title;
    this.song = song;
  }
  
  @Override
  public void getInput() {
    if(KeyHandler.firstKeyClick(Key.G)){
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
            KeyHandler.firstKeyClick(Key.C)) {
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
      if (script.isLocationIntersected(play.getTileX(), play.getTileY())) {
//        script.run();
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


  /**
   * Renders the map on-screen
   */
  @Override
  public void render() {

    for(int l = 0; l < LAYERS; l++){
      for (int i = 0; i < tiles[0].length; i++) {
        for (int j = 0; j < tiles[0][0].length; j++) {
          if (checkTileInBounds(drawOffsetX + j * SIZE, drawOffsetY + i * SIZE)) {
            
            if(tiles[l][i][j] != 0){
              Draw.tileCam(drawOffsetX + j * SIZE, drawOffsetY + i * SIZE, 
                  Draw.getTexX(tiles[l][i][j]), Draw.getTexY(tiles[l][i][j]), 
                  Draw.getTexture(tiles[l][i][j]));
            }

            // DEBUG Grid
            if (isGrid && l == LAYERS - 1) {
              Draw.rectCam(drawOffsetX + j * SIZE, drawOffsetY + i * SIZE, SIZE,
                  SIZE, 0, 16, SIZE, 16 + SIZE, 2);
            }

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
//      script.render();
    }

  }
  
  /**
   * Renders the bottom two layers of the map
   */
  public void renderFirstHalf() {
    for(int l = 0; l < LAYERS/2; l++){
      for (int i = 0; i < tiles[0].length; i++) {
        for (int j = 0; j < tiles[0][0].length; j++) {
          if (tiles[l][i][j] != 0 && 
              checkTileInBounds(drawOffsetX + j * SIZE, drawOffsetY + i * SIZE)
              ) {
            
            Draw.tileCam(drawOffsetX + j * SIZE, drawOffsetY + i * SIZE, 
                Draw.getTexX(tiles[l][i][j]), Draw.getTexY(tiles[l][i][j]), 
                Draw.getTexture(tiles[l][i][j]));
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
//      script.render();
    }

  }
  
  /**
   * Renders the top two layers of the map
   */
  public void renderSecondHalf() {
    for(int l = LAYERS/2; l < LAYERS; l++){
      for (int i = 0; i < tiles[0].length; i++) {
        for (int j = 0; j < tiles[0][0].length; j++) {
          if (checkTileInBounds(drawOffsetX + j * SIZE, drawOffsetY + i * SIZE)) {
            
            if(tiles[l][i][j] != 0){
              Draw.tileCam(drawOffsetX + j * SIZE, drawOffsetY + i * SIZE, 
                  Draw.getTexX(tiles[l][i][j]), Draw.getTexY(tiles[l][i][j]), 
                  Draw.getTexture(tiles[l][i][j]));
            }

            // DEBUG Grid
            if (isGrid && l == LAYERS - 1) {
              Draw.rectCam(drawOffsetX + j * SIZE, drawOffsetY + i * SIZE, SIZE,
                  SIZE, 0, 16, SIZE, 16 + SIZE, 2);
            }

          }
        }
      }
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
  public boolean checkTileInBounds(float x, float y) {
    x -= Camera.getX();
    y -= Camera.getY();
    
    return x+32 > 0 && x < WIDTH && y+32 > 0 && y < HEIGHT;
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

    ret[0] = tiles[0][0].length*16 > WIDTH && x+dx >= 0 && x+dx+WIDTH < tiles[0][0].length*16;

    //Left
    if(dx < 0){
      ret[0] = ret[0] && play.getX()-x < WIDTH/2-8 || x + WIDTH > tiles[0][0].length*16;

    }else if(dx > 0){ //Right
      ret[0] = ret[0] && play.getX()-x > WIDTH/2-8 || x < 0;
    }

    ret[1] = y+dy >= 0 && y+dy+HEIGHT < tiles[0].length*16 && 
        !(tiles[0].length*16 < HEIGHT);

    //Bottom
    if(dy < 0){
      ret[1] = ret[1] && play.getY()-y < HEIGHT/2-12 || y + HEIGHT > tiles[0].length*16;

    }else if(dy > 0){ //Top
      ret[1] = ret[1] && play.getY()-y > HEIGHT/2-12 || y < 0;
    }

    return ret;
  }

  public int getTileWidth(){
    return tiles[0][0].length;
  }

  public int getTileHeight(){
    return tiles[0].length;
  }
  
  public int getPxWidth(){
    return tiles[0][0].length*SIZE;
  }

  public int getPxHeight(){
    return tiles[0].length*SIZE;
  }

  public void setTile(int tile, int x, int y, int layer)
  {
    tiles[layer][x][y] = tile;
  }

  public static Player getPlayer()
  {
    return play;
  }

  public ArrayList<NPC> getNPCs()
  {
    return npcs;
  }
  
  public void move(float dx, float dy)
  {
    for (NPC npc: npcs)
    {
      npc.move(dx, dy);
    }
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


  /**
   * @return The filename of this map
   */
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
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


  /**
   * Cleans up all outstanding threadas
   */
  public void cleanUp() {
    for(NPC npc: npcs){
      npc.cleanUp();
    }
    for(AreaScript script: scripts){
//      script.close();
    }
  }

  public void setOffset(int tx, int ty){
    drawOffsetX = tx*SIZE;
    drawOffsetY = ty*SIZE;
    
    rectangle = new Rectangle((int)drawOffsetX, (int)drawOffsetY, 
        tiles[0][0].length*SIZE, tiles[0].length*SIZE);
  }

}

