/**
 * WaterBarrier.java    Jan 20, 2017, 9:41:22 AM
 */
package komorebi.projsoul.attack;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.map.MapHandler;

import java.awt.Rectangle;

/**
 * Caspian's Water Barrier, also a singleton
 *
 * @author Aaron Roy
 */
public class WaterBarrier implements SingleInstance {
  public float radius;
  private float x, y;
  
  private static final int MAX_TIME = 300;
  private int counter = MAX_TIME;
  
  //Center of the circle
  private float ox, oy;

  private int attack;
  
  public static final int KNOCKBACK = 5;
  
  private Animation ani;
  
  //TODO Make this variable relevant
  private boolean destroyMe;
  
  private WaterBarrier(float x, float y, float radius){
    ox = x;
    oy = y;

    this.x = ox-21;
    this.y = oy-21;

    this.radius = radius;
    
    attack = 0;
    
    ani = new Animation(2, 16, 42, 42, 12);
    
    ani.add(806, 119);
    ani.add(848, 118);
  }
  
  public WaterBarrier(){}
  
  @Override
  public boolean playing() {
    return ani.playing();
  }

  @Override
  public AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack) {
    return new WaterBarrier(x, y, dx);
  }

  @Override
  public void update() {      
    for (Enemy enemy: MapHandler.getEnemies()){
      Rectangle rect = enemy.getHitBox();
      float[] xs = {rect.x, rect.x+rect.width};
      float[] ys = {rect.y, rect.y+rect.height};

      if (!enemy.invincible()){
        outer:
          for(int i = 0; i < 2; i++){
            for (int j = 0; j < 2; j++){

              float distance = MapHandler.distanceBetween(ox, oy, xs[j], ys[i]);
              
              if(distance < radius){
                double ang = MapHandler.angleOf(xs[j], ys[i], ox, oy);
                enemy.inflictPain(attack, ang,
                    Characters.CASPIAN, KNOCKBACK);
                break outer;
              }
            }
          }
      }

    }
    
    if(counter <= 0){
      ani.hStop();
    }
    counter--;
    

  }
  
  public boolean intersectsCirc(float distance){
    return distance < radius;
  }
  
  public void play(){
    ani.playCam(x, y);
    //DEBUG Hitbox visual
    if(MapHandler.isHitBox){
      Draw.circCam(ox, oy, radius, 0, 255, 255, 128);
    }
  }
  
  public float getX() {
    return ox;
  }

  public float getY() {
    return oy;
  }


}
