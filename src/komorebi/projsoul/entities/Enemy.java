package komorebi.projsoul.entities;

import java.awt.Rectangle;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.HUD;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.states.Game;
import komorebi.projsoul.entities.Characters;


public abstract class Enemy extends Entity {

  private int attack, defense;
  protected int health;
  public Rectangle hitBox;

  public boolean invincible;
  protected boolean dying;
  public boolean dead;
  private int hitCounter;
  public Characters character;
  public HUD healths;
  

  protected Animation hitAni;
  protected Animation deathAni;

  public float dx, dy;
  
  private boolean[] hitBy;
  
  private int level;
  
  public abstract int xpPerLevel();
  public abstract int baseAttack();
  public abstract int baseDefense();
  public abstract int baseHealth();

  /**
   * Creates a standard enemy
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param sx The horizontal size, in pixels, of the enemy
   * @param sy The vertical size, in pixels, of the enemy
   */
  public Enemy(float x, float y, int sx, int sy) {
   this(x,y,sx,sy,1);
  }
  
  public Enemy(float x, float y, int sx, int sy, int level)
  {
    super(x, y, sx, sy);
    
    this.level = level;

    hitBox = new Rectangle((int)x,(int)y,sx,sy);
    hitBy = new boolean[4];
    
    attack = baseAttack() + level;
    health = baseHealth() + level;
    defense = baseDefense() + level;

    hitAni = new Animation(2,8,16,21,11);
    hitAni.add(0, 0);
    hitAni.add(0, 22);

    deathAni = new Animation(4,8,16,21,11,false);
    deathAni.add(0, 57);
    deathAni.add(0, 82);
    deathAni.add(0, 103);
    deathAni.add(0, 124);
    healths = new HUD(0,0);

    }

  /**
   * Updates the enemy's statuses and location
   */
  public void update() {
        
    if (dying && deathAni.lastFrame())
    {
      dead = true;
      //increment coin stuff
      //Use this for returning the amount of money a current player has, to check if they can deposit/withdraw money.
      character = Map.currentPlayer();
      healths = Map.getPlayer().getCharacterHUD(character);
      //^
      healths.giveMoney(10);
      Game.getMap().addXPObject(new XPObject(x, y, xpPerLevel()*level, hitBy));
    } else if (dying)
    {
      dx = 0;
      dy = 0;
    }

    if (invincible)
    {
      hitCounter--;
      if (dx>0) dx-=0.25;
      if (dx<0) dx+=0.25;
      if (dy>0) dy-=0.25;
      if (dy<0) dy+=0.25;
    }

    if (hitCounter<=0)
    {
      hitAni.hStop();
      invincible = false;
    }

    //Stops the enemy from moving places it shouldn't
    overrideImproperMovements();

    //Update the enemy
    x += dx;
    y += dy;
    hitBox.x = (int) x;
    hitBox.y = (int) y;

  }
  
  public void inflictPain(int attack, Face dir, Characters c)
  {
    health -= attack - (defense/2);
    hitBy[c.getNumber()] = true;
    
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

      switch (dir)
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
      

  public void knockBack(int attack, Characters c)
  {
    health -= attack - (defense/2);
    hitBy[c.getNumber()] = true;
    
    dx*=-5;
    dy*=-5;
    
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
    }
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
        Map.getPlayer().inflictPain(attack, 12*dx, 12*dy);
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

  public boolean invincible()
  {
    return invincible;
  }
}
