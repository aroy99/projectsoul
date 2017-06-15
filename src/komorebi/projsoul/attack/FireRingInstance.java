package komorebi.projsoul.attack;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Arithmetic;
import komorebi.projsoul.entities.Face;

import java.awt.Rectangle;

/**
 * Represents a part of a fire ring
 * 
 * @author Andrew Faulkenberry
 */
public class FireRingInstance implements AttackInstance {

  private static final float CTR_X = 6.5f, CTR_Y = 8f;
  private static final float RING_RADIUS = 36f;

  private float x, y;
  private int attack;
  private int timeLeft;

  private boolean destroyMe;

  private Animation ani;
  private Rectangle[] flames;
  
  public static ElementalProperty priAether = ElementalProperty.FIRE;//Base type = Fire
 

  private FireRingInstance(float x, float y, int attack) {
    this.x = x - CTR_X;
    this.y = y - CTR_Y;
    this.attack = attack;

    timeLeft = 250;

    ani = new Animation(1, 8, 12, true);
    ani.add(810, 61, 13, 16);

    flames = new Rectangle[12];

    for (int i = 0; i < flames.length; i++) {
      float[] coords = Arithmetic.coordinatesAt(x, y, RING_RADIUS, i * 30);
      flames[i] = new Rectangle((int) coords[0], (int) coords[1], 13, 16);
    }
  }

  public FireRingInstance() {
  }

  @Override
  public AttackInstance build(float x, float y, float dx, float dy, Face dir,
      int attack) {
    return new FireRingInstance(x, y, attack);
  }

  @Override
  public void update() {
    timeLeft--;

    if (timeLeft <= 0) {
      destroyMe = true;
    }

  }

  public void render() {
    for (int ang = 0; ang < 360; ang += 30) {
      float[] coords = Arithmetic.coordinatesAt(x, y, RING_RADIUS, ang);
      ani.playCam(coords[0], coords[1]);
    }
  }

  public boolean destroyed() {
    return destroyMe;
  }

  public boolean intersects(Rectangle r) {

    for (Rectangle flame : flames) {
      if (flame.intersects(r)){
        return true;
      }
    }

    return false;
  }

  //TODO Refactor awayyyyyyyyyyy http://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
  public boolean intersectsCirc(Rectangle r) {
    float[][] ptsToCheck = new float[2][2];
    ptsToCheck[0][0] = (float)(r.getY() + r.getHeight()); //North
    ptsToCheck[0][1] = (float) r.getY();                 //South
    
    ptsToCheck[1][0] = (float) r.getX();                 //West
    ptsToCheck[1][1] = (float)(r.getX() + r.getWidth()); //East

    double dist;
    
    //Loops though: west, north; west, south; east, north; east, south
    for(int i = 0; i < 2; i++){
      for(int j = 0; j < 2; j++){
        dist = Arithmetic.distanceBetween(ptsToCheck[1][i], ptsToCheck[0][j], x, y);
        if (dist > 36 && dist < 48) {
          return true;
        }
      }
    }
    
    return false;
  }

  public boolean inRing(Rectangle r) {
    double norY = r.getY() + r.getHeight();
    double sthY = r.getY();

    double wstX = r.getX();
    double eastX = r.getX() + r.getHeight();

    double dist = Arithmetic.distanceBetween((float) wstX, (float) sthY, x, y);
    if (dist < 36) {
      return true;
    }

    dist = Arithmetic.distanceBetween((float) wstX, (float) norY, x, y);

    if (dist < 36) {
      return true;
    }

    dist = Arithmetic.distanceBetween((float) eastX, (float) sthY, x, y);
    if (dist < 36) {
      return true;
    }

    dist = Arithmetic.distanceBetween((float) eastX, (float) norY, x, y);
    if (dist < 36) {
      return true;
    }

    return false;
  }

  public int getDamage() {
    return attack;
  }

  public float[] getCenter() {
    float[] ret = new float[2];
    ret[0] = x;
    ret[1] = y;
    return ret;
  }

}
