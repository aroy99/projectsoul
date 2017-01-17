package komorebi.projsoul.attack;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.states.Game;

import java.awt.Rectangle;

/**
 * Caspian's Water Sword attack
 * 
 * @author Andrew Faulkenberry
 */
public class WaterSword extends Melee {

  private WaterSword(float x, float y, Face dir, int attack)
  {
    super(x,y,dir,attack);
    
    character = Characters.CASPIAN;
    
    downAttack = new Animation(5, 4, 11, false);
    downAttack.add(24, 0, 16, 46, 0, 0);
    downAttack.add(49, 14, 19, 41, 0, -8);
    downAttack.add(74, 14, 19, 40, 0, -8);
    downAttack.add(100, 13, 26, 33, 0, 0);
    downAttack.add(127,6,35,40,-16,0);

    upAttack = new Animation(5,4,11,false);
    upAttack.add(26,55,16,47);
    upAttack.add(49,71,20,31,-4,0);
    upAttack.add(74,73,20,29,-4,0);
    upAttack.add(105,73,23,29,-7,0);
    upAttack.add(140,67,32,35);

    rightAttack = new Animation(5,4,11,false);
    rightAttack.add(27,110,16,47);
    rightAttack.add(51,127,29,30);
    rightAttack.add(88,127,29,30);
    rightAttack.add(126,125,28,32,-11,0);
    rightAttack.add(163,120,22,37);

    leftAttack = new Animation(5,4,11,false);
    leftAttack.add(27,110,16,47,0,true);
    leftAttack.add(51,127,29,30,-11,0,true);
    leftAttack.add(88,127,29,30,-11,0,true);
    leftAttack.add(126,125,28,32,0,true);
    leftAttack.add(163,120,22,37,0,true);
    
    hitBox = new Rectangle(0,0,16,16);
  }
  
  public WaterSword()
  {
    super();
  }
  
  @Override
  public AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack) {
    return new WaterSword(x,y,dir,attack);
  }
  
  public void update(int x, int y)
  {    
    switch (currentDir)
    {
      case DOWN:
        hitBox.x = (int) x;
        hitBox.y = (int) (y-16);
        break;
      case LEFT:
        hitBox.x = (int) (x-16);
        hitBox.y = (int) y;
        break;
      case RIGHT:
        hitBox.x = (int) x+16;
        hitBox.y = (int) y;
        break;
      case UP:
        hitBox.x = (int) x;
        hitBox.y = (int) y+16;
        break;
      default:
        hitBox.x = (int) x;
        hitBox.y = (int) y;
        break;
      
    }
    
    for (Enemy enemy: Game.getMap().getEnemies())
    {
      if (hitBox.intersects(enemy.getHitBox()) && !enemy.invincible())
      {
        enemy.inflictPain((int) (Player.getAttack(Characters.CASPIAN)), currentDir,
            Characters.CASPIAN);
      }

    }

  }

  @Override
  public void update() {
    // TODO Auto-generated method stub
    
  }
}
