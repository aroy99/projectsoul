/**
 * Editable.java     Sep 28, 2016, 9:11:36 AM
 */
package komorebi.projsoul.editor;

/**
 * Things that can be edited in the editor
 *
 * @author Aaron Roy
 */
public interface Editable {
  public int getOrigTX();
  public int getOrigTY();
  public void setTileLocation(int tx, int ty);
  public void setPixLocation(int x, int y);
  public float getX();
  public float getY();
}
