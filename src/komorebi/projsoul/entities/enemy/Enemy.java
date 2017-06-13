package komorebi.projsoul.entities.enemy;

import java.awt.Rectangle;

import komorebi.projsoul.attack.FireRingInstance;
import komorebi.projsoul.attack.RingOfFire;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.entities.Entity;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.XPObject;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.states.Game;
import komorebi.projsoul.attack.ElementalProperty;


public abstract class Enemy extends Entity {

  private int attack,defense;
  private int health;
  public Rectangle hitBox;

  public boolean invincible;
  private boolean dying;
  private boolean dead;
  private int hitCounter;
  

  public static ElementalProperty emyProperty = ElementalProperty.FIRE; 
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
    
    setAttack(baseAttack() + level);
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
      Game.getMap().addXPObject(new XPObject(getX(), y, xpPerLevel()*level, hitBy));
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
        if (ring.intersects(new Rectangle((int) (getX()+dx),(int) (y+dy),sx,sy)) && !invincible)
        {          
          float[] coords = ring.getCenter();
          inflictPain(ring.getDamage(), Map.angleOf(getX(), y, coords[0], coords[1]), 
              Map.getPlayer().getCharacter(), FireRingInstance.priAether);
        }
    }

    //Update the enemy
    setX(getX() + dx);
    y += dy;
    hitBox.x = (int) getX();
    hitBox.y = (int) y;

  }
  
  public void inflictPain(int attack, Face dir, Characters c, ElementalProperty filler)
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
    
    inflictPain(attack, chgx, chgy, c, filler);
  }
    
  //tells enemy how much damage to take in
  private void inflictPain(int attack, float dx, float dy, Characters c, ElementalProperty Aether)
  {
	  System.out.println(health);
	hitBy[c.getNumber()] = true; //Now, hit by which character
	attack=(int)(attack*emyProperty.findEffectiveness(Aether, emyProperty)+1);
	System.out.println("before stab dmge "+attack);
	
	attack = (int)(attack*Aether.calcStab(Map.getPlayer().charProperty, Aether));//calcs stab
	System.out.println("after stab dmge "+attack);
	
	if (attack-(defense/2) > 0){
		health -= attack - (defense/2);
	}
	else{
		health-=1;
	}
    System.out.println(health);
     //getNumber returns the character that's attacking
    
    //Kills the enemy
    if (health<=0)
    {
      deathAni.resume();
      dying = true;
    } 
    else
    {
      //Knocks back the enemy
      invincible = true;
      hitCounter = 50;
      hitAni.resume();
      
      //System.out.println(dx + " " + dy);
      
      this.dx = dx;
      this.dy = dy;
    }
  }
 

  /** /////////////////////////////////////////////////////////////////////////////////////
   * Updates whether the enemy is being hit 
   */ /////////////////////////////////////////////////////////////////////////////////////
  public void inflictPain(int attack, double ang, Characters c, ElementalProperty middleMan)
  {
    float chgx = (float) Math.cos(ang * (Math.PI/180)) * 5;
    float chgy = (float) Math.sin(ang * (Math.PI/180)) * 5;
    
    inflictPain(attack, chgx, chgy, c,middleMan);
    
  }

  @Override
  public void render() {
    if (invincible)
    {
      hitAni.playCam(getX(), y);
    } else if (dying)
    {
      deathAni.playCam(getX(), y);
    } else
    {
      Draw.rectCam(getX(), y, sx, sy, 0, 0, 16, 21, 0, 11);
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
    if (getX()+dx<0)
    {
      setX(0);
      dx = 0;
    } else if (getX()+dx>Game.getMap().getWidth()*16 - sx)
    {
      dx = 0;
      setX(Game.getMap().getHeight() * 16 - sx);
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

    Rectangle hypothetical = new Rectangle((int) (getX()+dx), (int) (y+dy),
        sx, sy);

    if (hypothetical.intersects(Map.getPlayer().getHitBox()))
    { 
      
      if (!Map.getPlayer().invincible())
      {
    	  if (health>0){
    		  Map.getPlayer().inflictPain(30, KNOCKBACK*Math.signum(dx)*(float)Math.sqrt(Math.abs(dx)), 
    				  KNOCKBACK*Math.signum(dy)*(float)Math.sqrt(Math.abs(dy)),emyProperty);
    	  }
      }

      dx = 0;
      dy = 0;
    }
    
    boolean[] col = Game.getMap().checkCollisions(getX(),y,dx,dy);

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
public int getAttack() {
	return attack;
}
public void setAttack(int attack) {
	this.attack = attack;
}
public Face getHitDirection() {
	return hitDirection;
}
public void setHitDirection(Face hitDirection) {
	this.hitDirection = hitDirection;
}

}
