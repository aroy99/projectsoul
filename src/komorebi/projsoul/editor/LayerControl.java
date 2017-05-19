package komorebi.projsoul.editor;

import java.util.Iterator;

import komorebi.projsoul.editor.controls.TabControl.Tab;
import komorebi.projsoul.editor.history.RemoveSublayerRevision;
import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Renderable;
import komorebi.projsoul.map.EditorMap;

public class LayerControl extends Tab implements Renderable {
 
  private static final int MIN_HT = 34*16;

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

  /**
   * @return An array of the four layers
   */
  public Layer[] getLayers()
  {
    return layers;
  }

  @Override
  public void update() {
    updateIndividualLayers();
    checkScroll();
    refresh();
    disableMapIfTextBoxFocused();
    killQueuedSublayers();
  }

  
  /**
   * Renders the layer tab's GUI
   */
  @Override
  public void render() {        
    for (int i = 0; i < layers.length; i++)
    {
      layers[i].render();
    }
  }

  
  /**
   * Finds the layer in which the given sublayer is located, and removes
   * it from that layer.  If the given sublayer is not found, nothing is
   * done.
   * @param sub The sublayer to be removed
   */
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

  /**
   * @return The first sublayer of the first layer
   */
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

  /**
   * Finds the layer which contains a given sublayer
   * @param s The given sublayer
   * @return The layer in which the given sublayer is found, or null
   * if no such layer is found
   */
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

  /**
   * Checks whether the user's mouse is within the bounds of the layer
   * tab
   * @return True if the mouse is in bounds
   */
  public boolean mouseInBounds()
  {
    return Draw.LAYER_MANAGER.contains(Mode.getFloatMouseX(), 
        Mode.getFloatMouseY());
  }

  /**
   * Merges all sublayers within each layer into one sublayer, and places
   * each flattened sublayer into an array
   * @return The array of four flattened sublayers
   */
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

  /**
   * Clears all sublayers within each layer
   */
  public void clear()
  {
    for (Layer lay: layers)
    {
      lay.getSubs().clear();
    }
  }

  /**
   * Refreshes the spacing of all controls within the Layer GUI
   */
  private void refresh()
  {
    int layerY = MIN_HT - 32 + scroll;

    for (int layer = layers.length - 1; layer >= 0; layer--)
    {
      layers[layer].setY(layerY);
      layerY -= layers[layer].getVisibleHeight();
    }
  }

  /**
   * Checks to see if the user has scrolled, and makes adjustments as
   * necessary
   */
  private void checkScroll()
  {
    scrollMax = Math.max(MIN_HT, getTotalHeightOfLayers());

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
  }
  
  /**
   * Calls each individual layer's update method
   */
  private void updateIndividualLayers()
  {
    for (Layer layer: layers)
      layer.update();
  }
  
  /**
   * Calculates the total height (in pixels) which all four layers,
   * and each of their sublayers, would occupy
   * @return The total height of the layers
   */
  private int getTotalHeightOfLayers()
  {
    int layersSize = 16;

    for (int i = 0; i < layers.length; i++)
    {
      layersSize+=layers[i].getVisibleHeight();
    }
    
    return layersSize;
  }
  
  /**
   * Checks to see if any textbox is focused. If so, using the keyboard
   * to edit the map is disabled.  If not, using the keyboard to edit the
   * map is enabled.
   */
  private void disableMapIfTextBoxFocused()
  {
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
    
    EditorMap.canUpdate(!any);

  }
  
  
  /**
   * Searches for sublayers queued to be removed, and kills them
   */
  private void killQueuedSublayers()
  {
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
  }

}
