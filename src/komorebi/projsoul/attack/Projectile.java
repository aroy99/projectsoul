package komorebi.projsoul.attack;

import java.awt.Rectangle;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.states.Game;

public abstract class Projectile implements AttackInstance {
  
  public float x, y, dx, dy;

  public Animation leftAttack;
  public Animation rightAttack;
  public Animation upAttack;
  public Animation downAttack;

  public Face currentDir;
  public Characters character;
  
  public Rectangle area;
  
  public int attack;
  public boolean destroyMe;
  
  public Projectile(float x, float y, float dx, float dy, Face dir, int attack) {
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
    
    this.currentDir = dir;  
    this.attack = attack;
  
  }
  
  public Projectile()
  {
    
  }

  public void update() {
    switch (currentDir)
    {
      case DOWN:
        area.setSize((int) downAttack.getCurrentFrameSX(),
            (int) downAttack.getCurrentFrameSY()); 
        break;
      case LEFT:
        area.setSize((int) leftAttack.getCurrentFrameSX(),
            (int) downAttack.getCurrentFrameSY());
        break;
      case RIGHT:
        area.setSize((int) rightAttack.getCurrentFrameSX(),
            (int) downAttack.getCurrentFrameSY()); 
        break;
      case UP:
        area.setSize((int) upAttack.getCurrentFrameSX(),
            (int) downAttack.getCurrentFrameSY()); 
        break;
      default:
        break;
    } 
    
    for (Enemy enemy: Game.getMap().getEnemies())
    {
        if (enemy.getHitBox().intersects(new Rectangle((int) (x+dx), 
            (int) (y+dy), (int) area.getWidth(), 
            (int) area.getHeight())))
        {
          destroyMe = true;
          if (!enemy.invincible())
          {
            enemy.inflictPain(attack, currentDir, 
                character);
          }
        }
        
    }
    
    overrideImproperMovements();
    
    x += dx;
    y += dy;
    
    area.setLocation((int) x, (int) y);     
  }

  public void render() {
    // TODO Auto-generated method stub
    
  }
  
  public boolean destroyed()
  {
    return destroyMe;
  }
  
  public void overrideImproperMovements()
  {
    if (x+dx<0 || 
        x+dx>Game.getMap().getWidth()*16 - area.getWidth())
    {
      dx = 0;
      destroyMe = true;
    }

    if (y+dy<0 || 
        y+dy>Game.getMap().getHeight()*16 - area.getHeight())
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
    switch (currentDir)
    {
      case DOWN:
        downAttack.playCam(x, y);
        break;
      case LEFT:
        leftAttack.playCam(x, y);
        break;
      case RIGHT:
        rightAttack.playCam(x, y);
        break;
      case UP:
        upAttack.playCam(x, y);
        break;
      default:
        break;
    } 
  
  }
  
  public void setAttack(int attack)
  {
    this.attack = attack;
  }

  public abstract AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack);


  

}
