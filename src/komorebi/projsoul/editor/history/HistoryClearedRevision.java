package komorebi.projsoul.editor.history;

import komorebi.projsoul.engine.Draw;

public class HistoryClearedRevision extends Revision {

  public HistoryClearedRevision() {
    super();
    description = "History cleared";
  }

  @Override
  public void undo() {}

  @Override
  public void redo() {}

  @Override
  public void render() {
    Draw.rect(clickableArea.x, clickableArea.y, 16, 16, 
        192, 0, 208, 16, 2);

  }

}
