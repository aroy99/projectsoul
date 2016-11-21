package komorebi.projsoul.entities;

import java.awt.Rectangle;

import komorebi.projsoul.attack.FireRingInstance;
import komorebi.projsoul.attack.RingOfFire;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.states.Game;

public abstract class Enemy extends Entity {

  private int attack, defense;
  private int health;
  public Rectangle hitBox;

  public boolean invincible;
  private boolean dying;
  private boolean dead;
  private int hitCounter;
  

  private Animation hitAni;
  private Animation deathAni;
  
  public static final int KNOCKBACK = 6;

  public float dx, dy;
  
  private boolean[] hitBy;
  
  private int level;
  
  public abstract int xpPerLevel();
  public abstract int baseAttack();
  public abstract int baseDefense();
  public abstract int baseHealth();


  private Face hitDirection;
  
  private EnemyType type;

  /**
   * Creates a standard enemy
   * @param x The x location (in the map) of the enemy
   * @param y The y location (in the map) of the enemy
   * @param type The sprite of this enemy
   */
  public Enemy(float x, float y, EnemyType type) {
   this(x,y,type, 1);
  }
  
  public Enemy(float x, float y, EnemyType type, int level) {
    super(x, y, type.getSX(), type.getSY());
    
    this.type = type;
    
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

    if (invincible)
    {
      hitCounter--;
      if (dx > 0){
        dx--;
      }
      if (dx < 0){
        dx++;
      }
      if (dy > 0){
        dy--;
      }
      if (dy < 0){
        dy++;
      }
    }

    if (hitCounter<=0)
    {
      hitAni.hStop();
      invincible = false;
    }

    //Stops the enemy from moving places it shouldn't
    overrideImproperMovements();
    
    for (FireRingInstance ring: RingOfFire.allInstances())
    {
        if (ring.intersects(new Rectangle((int) (x+dx),(int) (y+dy),sx,sy)) && !invincible)
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
    
  
    
  
  private void inflictPain(int attack, float dx, float dy, Characters c)
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
      
      System.out.println(dx + " " + dy);
      
      this.dx = dx;
      this.dy = dy;
    }
  }
      

  /**
   * Updates whether the enemy is being hit
   */
  public void inflictPain(int attack, double ang, Characters c)
  {
    float chgx = (float) Math.cos(ang * (Math.PI/180)) * 5;
    float chgy = (float) Math.sin(ang * (Math.PI/180)) * 5;
    
    inflictPain(attack, chgx, chgy, c);
  }

 



  @Override
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
        Map.getPlayer().inflictPain(30, KNOCKBACK*Math.signum(dx)*(float)Math.sqrt(Math.abs(dx)), 
            KNOCKBACK*Math.signum(dy)*(float)Math.sqrt(Math.abs(dy)));

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
