/**
 * Entity.java    May 15, 2016, 11:47:59 PM
 */
package komorebi.projsoul.entities;

import java.awt.Rectangle;

import komorebi.projsoul.editor.Editable;
import komorebi.projsoul.engine.Renderable;
import komorebi.projsoul.map.EditorMap;

/**
 * Represents something that moves
 * 
 * @author Aaron Roy
 */
public abstract class Entity implements Renderable, Editable{
  protected float x;
  protected float y;
  
  protected int sx;
  protected int sy;
  
  protected int tx;           //Tile X and Y (accounting for Editor position)
  protected int ty;


  protected Rectangle area;
  
  protected Entities ent;

  /**
   * Represents the different varieties of entities that exist within the game
   * 
   * @author Aaron Roy
   * @version
   */
  public enum Entities{
    CLYDE, FILLER, NPC;
  }

  /**
   * Creates a new entity
   * 
   * @param x The x position
   * @param y The y position
   * @param sx The width
   * @param sy The height
   */
  public Entity(float x,float y,int sx,int sy){
    this.x = x;
    this.y = y;
    this.sx = sx;
    this.sy = sy;
    
    tx = (int)(x-EditorMap.getX())/16;
    ty = (int)(y-EditorMap.getY())/16;

    
  }

  public float getX()
  {
    return x;
  }

  public float getY()
  {
    return y;
  }
  
  /**
   * @return the original tile x of this NPC
   */
  public int getOrigTX(){
    return tx;
  }

  /**
   * @return the original tile y of this NPC
   */
  public int getOrigTY(){
    return ty;
  }

  
  /**
   * Moves the NPC to a new tile
   * 
   * @param tx The tile x location of the bottom left corner of the NPC
   * @param ty The tile y location of the bottom left corner of the NPC
   */
  public void setTileLocation(int tx, int ty){
    this.x=tx*16+EditorMap.getX();
    this.y=ty*16+EditorMap.getY();
    
    this.tx = tx;
    this.ty = ty;
  }
  
  /**
   * Relocates the Entity to a specific spot on the screen
   * @param x The x cooridnate of the new bottom left corner, in pixels, of the Entity
   * @param y The y coordinate of the new bottom left corner, in pixels, of the Entity
   */
  public void setPixLocation(int x, int y)
  {
    this.x = x;
    this.y = y;
  }



}
