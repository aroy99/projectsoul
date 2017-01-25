package komorebi.projsoul.entities.enemy;

import komorebi.projsoul.attack.FireRingInstance;
import komorebi.projsoul.attack.RingOfFire;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.entities.Entity;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.XPObject;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.states.Game;

import java.awt.Rectangle;

/**
 * Represents an Enemy in the game
 * 
 * @author Andrew Faulkenberry
 * @author Aaron Roy
 */
public abstract class Enemy extends Entity {

  protected int attack, defense;
  protected int health;
  protected int level;
  
  protected boolean hitWall, hitPlayer;
  
  public Rectangle hitBox;

  public boolean hurt;
  public boolean invincible;
  protected boolean dying;
  protected boolean dead;
  protected int hitCounter;

  //Animations
  protected final Animation regAni;
  protected final Animation hitAni;
  protected final Animation deathAni;

  public static final int DEFAULT_KNOCK = 6;
  public static final int INVINCIBILITY = 15;

  public float dx, dy;

  protected boolean[] hitBy;
  
  //Does something
  private Face hitDirection;

  //Sprite of this enemy
  private EnemyType type;


  /**
   * @return The amount of exp this enemy gives per level
   */
  public abstract int xpPerLevel();
  
  /**
   * @return The base attack of this enemy
   */
  public abstract int baseAttack();
  
  /**
   * @return The base defense of this enemy
   */
  public abstract int baseDefense();
  
  /**
   * @return The base health of this enemy
   */
  public abstract int baseHealth();

  /**
   * Creates a standard enemy
   * 
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param type The sprite of this enemy
   */
  public Enemy(float x, float y, EnemyType type) {
    this(x,y,type, 1);
  }

  /**
   * Creates a standard enemy
   * 
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param type The sprite of this enemy
   * @param level The level of this enemy
   */
  public Enemy(float x, float y, EnemyType type, int level) {
    super(x, y, type.getSX(), type.getSY());

    this.type = type;

    this.level = level;

    hitBox = new Rectangle((int)x,(int)y,sx,sy);
    hitBy = new boolean[4];

    attack = baseAttack() + level;
    health = baseHealth() + level;
    defense = baseDefense() + level;

    regAni = EnemyType.getAni(type);
    
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
      Game.getMap().addXPObject(new XPObject(x, y, xpPerLevel()*level, hitBy));
    } else if (dying)
    {
      dx = 0;
      dy = 0;
    }

    if (hurt)
    {
      hitCounter--;
      dx *= 0.9;
      dy *= 0.9;
    }

    if (hurt && hitCounter <= 0)
    {
      hitAni.hStop();
      hurt = false;
      invincible = false;
    }

    hitWall = false;
    hitPlayer = false;
    
    //Stops the enemy from moving places it shouldn't
    overrideImproperMovements();

    for (FireRingInstance ring: RingOfFire.allInstances())
    {
      if (ring.intersects(new Rectangle((int) (x+dx),(int) (y+dy),sx,sy)) && !hurt)
      {          
        float[] coords = ring.getCenter();
        inflictPain(ring.getDamage(), Map.angleOf(x, y, coords[0], coords[1]), 
            Characters.FLANNERY);
      }
    }

    //Update the enemy
    x += dx;
    y += dy;
    hitBox.x = (int) x;
    hitBox.y = (int) y;

  }

  /**
   * Hits the enemy in one direction
   * 
   * @param attack The damage done to the enemy
   * @param dir The direction to whack the enemy
   * @param c The character that hit the enemy (used for exp)
   */
  public void inflictPain(int attack, Face dir, Characters c)
  {
    int chgx = 0, chgy = 0;

    switch (dir)
    {
      case DOWN:
        chgx = 0;
        chgy = -5;
        break;
      case LEFT:
        chgx = -5;
        chgy = 0;
        break;
      case RIGHT:
        chgx = 5;
        chgy = 0;
        break;
      case UP:
        chgx = 0;
        chgy = 5;
        break;
      default:
        break;
    }

    inflictPain(attack, chgx, chgy, c);
  }


  /**
   * Updates whether the enemy is being hit with default knockback
   * 
   * @param attack The damage done to the enemy
   * @param ang The angle to knock the enemy back (in degrees)
   * @param c The character that hit the enemy (used for exp)
   */
  public void inflictPain(int attack, double ang, Characters c)
  {
    inflictPain(attack, ang, c, DEFAULT_KNOCK);
  }
  
  /**
   * Updates whether the enemy is being hit, and gives extra knockback
   * 
   * @param attack The damage done to the enemy
   * @param ang The angle to knock the enemy back (in degrees)
   * @param c The character that hit the enemy (used for exp)
   * @param knock The custom knockback
   */
  public void inflictPain(int attack, double ang, Characters c, int knock)
  {
    float chgx = (float) Math.cos(ang * (Math.PI/180)) * knock;
    float chgy = (float) Math.sin(ang * (Math.PI/180)) * knock;

    inflictPain(attack, chgx, chgy, c);
  }

  /**
   * Whacks the enemy and kills it if his health is low enough
   * 
   * @param attack The damage done to the enemy
   * @param dx The new x velocity of the target
   * @param dy The new y velocity of the target
   * @param c The character that hit the enemy (used for exp)
   */
  public void inflictPain(int attack, float dx, float dy, Characters c)
  {
    if(!invincible){
      health -= attack - (defense/2);
      hitBy[c.getNumber()] = true;

      //Kills the enemy
      if (health <= 0)
      {
        deathAni.resume();
        dying = true;
      } else
      {
        //Knocks back the enemy
        hurt = true;
        invincible = true;
        hitCounter = INVINCIBILITY;
        hitAni.resume();

        //DEBUG Enemy movement speed
        System.out.println("----ENEMY MOVEMENT----\n"+dx + " " + dy + "\nHealth: " + health);

        this.dx = dx;
        this.dy = dy;
      }
    }
  }





  @Override
  public void render() {
    if (hurt)
    {
      hitAni.playCam(x, y);
    } else if (dying)
    {
      deathAni.playCam(x, y);
    } else
    {
      regAni.playCam(x, y);
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

  public EnemyType getType(){
    return type;
  }

  public String getBehavior(){
    return "none";
  }

  public boolean dead()
  {
    return dead;
  }

  /**
   * Stops the enemy from moving where it shouldn't 
   */
  public void overrideImproperMovements()
  {
    if (x+dx < 0)
    {
      x = 0;
      dx = 0;
      hitWall = true;
    } else if (x+dx > Game.getMap().getWidth()*16 - sx)
    {
      dx = 0;
      x = Game.getMap().getHeight() * 16 - sx;
      hitWall = true;
    }

    if (y+dy < 0)
    {
      dy = 0;
      y = 0;
      hitWall = true;
    } else if (y+dy > Game.getMap().getHeight()*16 - sy)
    {
      dy = 0;
      y = Game.getMap().getHeight() * 16 - sy;
      hitWall = true;
    }

    Rectangle hypothetical = new Rectangle((int) (x+dx), (int) (y+dy),
        sx, sy);

    if (hypothetical.intersects(Map.getPlayer().getHitBox()))
    { 

      if (!Map.getPlayer().invincible())
      {
        //DEBUG Enemy movements Part II
        System.out.println(dx + ", " + dy);
        Map.getPlayer().inflictPain(attack, 
            DEFAULT_KNOCK*Math.signum(dx)*(float)Math.sqrt(Math.abs(dx)), 
            DEFAULT_KNOCK*Math.signum(dy)*(float)Math.sqrt(Math.abs(dy)));
        hitPlayer = true;
        
      }

      dx = 0;
      dy = 0;
      hitPlayer = true;
    }

    boolean[] col = Game.getMap().checkCollisions(x,y,dx,dy);

    if(!col[0] || !col[2]){
      dy=0;
      dx*=.75f;
      hitWall = true;
    }
    if(!col[1] || !col[3]){
      dx=0;
      dy*=.75f;
      hitWall = true;
    }
    
  }

  public boolean invincible()
  {    
    return hurt;
  }
}
