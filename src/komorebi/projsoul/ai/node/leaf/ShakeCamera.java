/**
 * ShakeCamera.java    Mar 1, 2017, 9:41:02 AM
 */
package komorebi.projsoul.ai.node.leaf;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.gameplay.Camera;

/**
 * Shakes dat camera
 *
 * @author Aaron Roy
 */
public class ShakeCamera extends Node {

  private int frames, step;
  private float intensity;

  public ShakeCamera(int frames, float intensity, int step) {
    this.frames = frames;
    this.intensity = intensity;
    this.step = step;
  }
  
  @Override
  public Status update() {
    Camera.shake(frames, intensity, step);
    return Status.SUCCESS;
  }

}
