package komorebi.projsoul.editor;

import java.util.Iterator;

import komorebi.projsoul.editor.Layer.LayerType;
import komorebi.projsoul.editor.controls.TabControl.Tab;
import komorebi.projsoul.editor.history.RemoveSublayerRevision;
import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Renderable;
import komorebi.projsoul.map.EditorMap;

public class LayerControl extends Tab implements Renderable {
  
  private static final int MIN_HT = 34*16;
  private static final int WIDTH = 12*16;
  
  int bottom;
  private int scroll;
  private int scrollMax;
  
  private Layer[] layers;
  
  public LayerControl()
  {
    layers = new Layer[4];
    
    for (int i = 0; i < layers.length; i++)
    {
      layers[i] = new Layer(LayerType.layerNumber(i), 34-(8-2*i));
    }
    
    bottom = MIN_HT - 32*4;
    tabTitle = "Layers";
    
  }
  
  public Layer[] getLayers()
  {
    return layers;
  }

  @Override
  public void update() {
    
    int layersSize = 16;
    
    for (int i = 0; i < layers.length; i++)
    {
      layers[i].update();
      layersSize+=layers[i].getVisibleHeight();
    }
    
    scrollMax = Math.max(MIN_HT, layersSize);
        
    double dMouse = 0.1*Editor.dWheel;
        
    if (dMouse > 0 && scroll > 0)
    {
      scroll -= dMouse; 
      if (scroll < 0)
      {
        scroll = 0; 
      }
    } else if (dMouse < 0 && scroll + MIN_HT < scrollMax)
    {
      scroll -= dMouse;
      if (scroll + MIN_HT > scrollMax) 
      {
        scroll = scrollMax - MIN_HT;
      }
    }
   
    refresh();
    
    
   
    boolean any = false;
    
    for (Layer lay: layers)
    {
      for (Sublayer sub: lay.getSubs())
      {
        if (sub.getTextField().isFocused())
        {
          any = true;
          break;
        }
      }
      
      if (any)
      {
        break;
      }
    }
    
    for (Layer layer: layers)
    {
      for (Iterator<Sublayer> it = layer.getSubs().iterator(); it.hasNext();)
      {
        Sublayer sub = it.next();
        if (sub.isQueuedForRemoval())
        {
          it.remove();
          sub.kill();
          
          Editor.getMap().addRevision(new RemoveSublayerRevision(sub,
              layer.getSubs()));
        }
      }
    }
    
    EditorMap.canUpdate(!any);
    
    
    
  }

  @Override
  public void render() {        
    for (int i = 0; i < layers.length; i++)
    {
      layers[i].render();
    }
  }
  
  public void remove(Sublayer sub)
  {
    for (Layer layer: layers)
    {
      if (layer.getSubs().contains(sub))
      {
        layer.getSubs().remove(sub);
      }
    }
  }
  
  public Sublayer getFirst()
  {
    for (int i = 0; i < layers.length; i++)
    {
      if (!layers[i].getSubs().isEmpty())
      {
        return layers[i].getSubs().get(0);
      }
    }
    
    return null;
  }
  
  public Layer layerContaining(Sublayer s)
  {
    for (Layer l: layers)
    {
      if (l.getSubs().contains(s))
      {
        return l;
      }
    }
    
    return null;
  }
  
  public boolean clickInBounds()
  {
    return Draw.LAYER_MANAGER.contains(Mode.getFloatMouseX(), 
        Mode.getFloatMouseY());
  }
  
  public Sublayer[] getFlattenedLayers()
  {
    Sublayer[] flat = new Sublayer[4];
    
    for (int i = 0; i < layers.length; i++)
    {
      if (layers[i].hasSublayers())
      {
        flat[i] = Sublayer.merge(layers[i].getSubs().toArray(
            new Sublayer[layers[i].getSubs().size()]));
      } else
      {
        flat[i] = null;
      }
      
    }
    
    return flat;
  }
  
  public void clear()
  {
    for (Layer lay: layers)
    {
      lay.getSubs().clear();
    }
  }
  
  public void refresh()
  {
    int layerY = MIN_HT - 32 + scroll;
    
    for (int layer = layers.length - 1; layer >= 0; layer--)
    {
      layers[layer].setY(layerY);
      layerY -= layers[layer].getVisibleHeight();
    }
  }
  
}
