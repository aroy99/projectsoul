/**
 * Projectile.java     Jan 2, 2017, 9:13:37 AM
 */
package komorebi.projsoul.attack.projectile;

import komorebi.projsoul.attack.AttackInstance;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.states.Game;

import java.awt.Rectangle;

/**
 * Represents a vanilla projectile
 *
 * @author Aaron Roy
 */
public abstract class Projectile implements AttackInstance {
  protected float x, y, dx, dy;

  protected Animation[] attacks = new Animation[4];

  protected Face currentDir;

  protected ProjectileType type;

  protected int index;

  protected Rectangle area;

  protected int attack;
  protected boolean destroyMe;

  /**
   * Creates a new projectile
   * 
   * @param x The x location (in the map) of the projectile
   * @param y The y location (in the map) of the projectile
   * @param dx The x velocity, in pixels per frame
   * @param dy The y velocity, in pixels per frame
   * @param dir Direction this projectile is facing
   * @param attack The damage this will do
   */
  public Projectile(float x, float y, float dx, float dy, Face dir, int attack,
      ProjectileType type) {
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;

    this.currentDir = dir;
    this.attack = attack;

    index = dir.getFaceNum();

    this.type = type;

    attacks = ProjectileType.getAni(type);

    area = new Rectangle((int) attacks[index].getCurrSX(),
        (int) attacks[index].getCurrSY());    
  }

  public Projectile(){}

  @Override
  public void update(){

    overrideImproperMovements();

    x += dx;
    y += dy;

    area.setLocation((int) x, (int) y);     


  }

  public boolean destroyed()
  {
    return destroyMe;
  }

  /**
   * Destroys this projectile once it hit a wall
   */
  public void overrideImproperMovements()
  {
    if (x+dx < 0 || x+dx > Game.getMap().getWidth()*16 - area.getWidth())
    {
      dx = 0;
      destroyMe = true;
    }

    if (y+dy < 0 || y+dy > Game.getMap().getHeight()*16 - area.getHeight())
    {
      dy = 0;
      destroyMe = true;
    }

    boolean[] col = Game.getMap().checkCollisions(x,y,dx,dy);

    if(!col[0] || !col[2]){
      dy=0;
      destroyMe = true;
    }
    if(!col[1] || !col[3]){
      dx=0;
      destroyMe = true;
    }
  }

  public void play()
  {    
    attacks[index].playCam(x, y);  
  }

  public void setAttack(int attack)
  {
    this.attack = attack;
  }

  public abstract AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack);


}
