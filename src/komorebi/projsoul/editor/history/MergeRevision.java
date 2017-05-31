package komorebi.projsoul.editor.history;

import komorebi.projsoul.editor.Layer;
import komorebi.projsoul.editor.Sublayer;
import komorebi.projsoul.engine.Draw;

public class MergeRevision extends Revision {

  private Sublayer[] preSubs;
  private Sublayer mergedSub;
  private Layer layerOf;
  
  public MergeRevision(Layer layerOf, Sublayer[] preSubs, Sublayer mergedSub)
  {
    this.layerOf = layerOf;
    this.preSubs = preSubs;
    this.mergedSub = mergedSub;
    
    description = "Merged ";
    
    for (Sublayer sub: preSubs)
    {
      description += sub.getTextField().getText() + ", ";
    }
    
    description += "into " + mergedSub.getTextField().getText();
  }

  @Override
  public void undo() {
    
    layerOf.getSubs().remove(mergedSub);
    
    for (Sublayer sublayer: preSubs)
    {
      layerOf.getSubs().add(sublayer);
    }
    
    layerOf.alignSublayers();
  }

  @Override
  public void redo() {
    for (Sublayer sublayer: preSubs)
    {
      layerOf.getSubs().remove(sublayer);
    }
    
    layerOf.getSubs().add(mergedSub);
    layerOf.alignSublayers();
    
  }

  @Override
  public void render() {
    Draw.rect(clickableArea.x, clickableArea.y, 13, 13, 
        13, 91, 26, 104, 2);
    
  }
}
