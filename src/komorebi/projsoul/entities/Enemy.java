package komorebi.projsoul.entities;

import java.awt.Rectangle;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.states.Game;

public class Enemy extends Entity {

  private int health;
  public Rectangle hitBox;

  private boolean isHit, wasHit;
  public boolean invincible;
  private boolean dying;
  private boolean dead;
  private int hitCounter;
  
  private int attackersAttack;

  private Animation hitAni;
  private Animation deathAni;

  public float dx, dy;

  private Face hitDirection;

  /**
   * Creates a standard enemy
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param sx The horizontal size, in pixels, of the enemy
   * @param sy The vertical size, in pixels, of the enemy
   */
  public Enemy(float x, float y, int sx, int sy) {
    super(x, y, sx, sy);

    hitBox = new Rectangle((int)x,(int)y,sx,sy);
    health = 40;

    hitAni = new Animation(2,8,16,21,11);
    hitAni.add(0, 0);
    hitAni.add(0, 22);

    deathAni = new Animation(4,8,16,21,11,false);
    deathAni.add(0, 57);
    deathAni.add(0, 82);
    deathAni.add(0, 103);
    deathAni.add(0, 124);
  }

  /**
   * Updates the enemy's statuses and location
   */
  public void update() {
        
    if (dying && deathAni.lastFrame())
    {
      dead = true;
    } else if (dying)
    {
      dx = 0;
      dy = 0;
    }

    if (invincible)
    {
      hitCounter--;
      if (dx>0) dx--;
      if (dx<0) dx++;
      if (dy>0) dy--;
      if (dy<0) dy++;
    }

    if (hitCounter<=0)
    {
      hitAni.hStop();
      invincible = false;
    }

    if (isHit && !wasHit && !invincible)
    {
      isHit = false;
      health -= attackersAttack;

      //Kills the enemy
      if (health<=0)
      {
        deathAni.resume();
        dying = true;
      } else
      {
        //Knocks back the enemy
        invincible = true;
        hitCounter = 50;
        hitAni.resume();

        switch (hitDirection)
        {
          case DOWN:
            dx = 0;
            dy = -5;
            break;
          case LEFT:
            dx = -5;
            dy = 0;
            break;
          case RIGHT:
            dx = 5;
            dy = 0;
            break;
          case UP:
            dx = 0;
            dy = 5;
            break;
          default:
            break;

        }
      }
    }

    //Stops the enemy from moving places it shouldn't
    overrideImproperMovements();

    //Update the enemy
    x += dx;
    y += dy;
    hitBox.x = (int) x;
    hitBox.y = (int) y;

  }

  /*
   * Updates whether the enemy is being hit
   */
  public void updateHits(boolean isHit, int attack, Face dir)
  {
    wasHit = this.isHit;
    this.isHit = isHit;
    
    attackersAttack = attack;

    hitDirection = dir;
  }

  /*
   * (non-Javadoc)
   * @see komorebi.projsoul.engine.Renderable#render()
   */
  public void render() {
    if (invincible)
    {
      hitAni.playCam(x, y);
    } else if (dying)
    {
      deathAni.playCam(x, y);
    } else
    {
      Draw.rectCam(x, y, sx, sy, 0, 0, 16, 21, 0, 11);
    }
  }

  public Rectangle getHitBox()
  {
    return hitBox;
  }

  public int getHealth()
  {
    return health;
  }

  public boolean dead()
  {
    return dead;
  }

  /*
   * Stops the enemy from moving where it shouldn't 
   */
  public void overrideImproperMovements()
  {
    if (x+dx<0)
    {
      x = 0;
      dx = 0;
    } else if (x+dx>Game.getMap().getWidth()*16 - sx)
    {
      dx = 0;
      x = Game.getMap().getHeight() * 16 - sx;
    }

    if (y+dy<0)
    {
      dy = 0;
      y = 0;
    } else if (y+dy>Game.getMap().getHeight()*16 - sy)
    {
      dy = 0;
      y = Game.getMap().getHeight() * 16 - sy;
    }

    Rectangle hypothetical = new Rectangle((int) (x+dx), (int) (y+dy),
        sx, sy);

    if (hypothetical.intersects(Map.getPlayer().getHitBox()))
    { 
      
      if (!Map.getPlayer().invincible())
      {
        Map.getPlayer().inflictPain(10, 12*dx, 12*dy);
      }

      dx = 0;
      dy = 0;
    }
    
    boolean[] col = Game.getMap().checkCollisions(x,y,dx,dy);

    if(!col[0] || !col[2]){
      dy=0;
      dx*=.75f;
    }
    if(!col[1] || !col[3]){
      dx=0;
      dy*=.75f;
    }
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

}
