/**
 * CircleStrike.java    Jan 6, 2017, 9:19:04 AM
 */
package komorebi.projsoul.attack;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Arithmetic;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.map.MapHandler;

import java.awt.Rectangle;

/**
 * Sierra's circle strike attack
 *
 * @author Aaron Roy
 */
public class CircleStrike implements SingleInstance {
  public float beginRad, endRad;
  private float currRadius;
  private float x, y;

  // Center of the circle
  private float ox, oy;
  private int attack;

  public static final float STEP = 1.25f / 2;
  public static final int KNOCKBACK = 5;

  private Animation ani;

  /**
   * Creates a circle strike attack for Sierra
   * 
   * @param x
   *          The x location (in the map) of the center of the circle
   * @param y
   *          The y location (in the map) of the center of the circle
   * @param beginR
   *          Start radius (curr ani uses 21.5)
   * @param endR
   *          End radius (curr ani uses 40)
   * @param attack
   *          Max damage this will do
   */
  private CircleStrike(float x, float y, float beginR, float endR, int attack) {
    ox = x;
    oy = y;

    this.x = ox - 40;
    this.y = oy - 40;

    beginRad = beginR;
    endRad = endR;
    this.attack = attack;
    currRadius = beginR;

    ani = new Animation(4, 8, 12, false);

    ani.add(740, 875, 43, 43, 18, 19);
    ani.add(783, 861, 57, 57, 11, 12);
    ani.add(840, 849, 69, 69, 5, 6);
    ani.add(909, 838, 80, 80);

    ani.setPausedFrame(909, 838, 80, 80);

  }

  public CircleStrike() {}

  @Override
  public boolean playing() {
    return ani.playing();
  }

  @Override
  public AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack) {
    return new CircleStrike(x, y, dx, dy, attack);
  }

  @Override
  public void update() {
    if (currRadius < endRad) {
      currRadius += STEP;

      for (Enemy enemy : MapHandler.getEnemies()) {
        Rectangle rect = enemy.getHitBox();
        float[] xs = { rect.x, rect.x + rect.width };
        float[] ys = { rect.y, rect.y + rect.height };

        if (!enemy.invincible()) {
          outer: for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {

              float distance = Arithmetic.distanceBetween(ox, oy, xs[j], ys[i]);
              if (distance < currRadius) {
                double ang = Arithmetic.angleOf(xs[j], ys[i], ox, oy);
                enemy.inflictPain((int) (endRad / distance * attack - attack),
                    ang, Characters.SIERRA, KNOCKBACK, ElementalProperty.WIND);
                break outer;
              }
            }
          }
        }

      }

    }
  }

  public void play() {
    ani.playCam(x, y);
    // DEBUG Hitbox visual
    if (MapHandler.isHitBox) {
      Draw.circCam(ox, oy, currRadius, 0, 255, 255, 128);
    }
  }
}
