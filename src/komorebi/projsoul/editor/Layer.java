package komorebi.projsoul.editor;

import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import komorebi.projsoul.editor.controls.ExpandArrow;
import komorebi.projsoul.editor.controls.RadioButton;
import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.script.EarthboundFont;
import komorebi.projsoul.script.TextHandler;

public class Layer {
  
  public static enum LayerType {
    COVERAGE("Coverage", 89, 82),
    L3("Layer 3", 73, 82),
    STRUCTURES("Structures", 57, 82),
    TERRAIN("Terrain", 41, 82);
    
    int texx, texy;
    String str;
    
    static int[] subNums = new int[4];
    
    public static String generateName(LayerType t)
    {
      String ret = t.toString().substring(0, 3) + subNums[numOf(t)];
      subNums[numOf(t)]++;
      return ret;
    }
    
    public String toString()
    {
      return str;
    }
    
    public int getTexX()
    {
      return texx;
    }
    
    public int getTexY()
    {
      return texy;
    }
    
    private LayerType(String str, int texx, int texy)
    {
      this.str = str;
      this.texx = texx;
      this.texy = texy;
    }
    
    public static int numOf(LayerType t)
    {
      switch (t)
      {
        case TERRAIN:
          return 0;
        case STRUCTURES:
          return 1;
        case L3:
          return 2;
        case COVERAGE:
          return 3;
        default:
          return -1;
      }
    }
    
    public static LayerType layerNumber(int num)
    {
      switch (num)
      {
        case 0:
          return TERRAIN;
        case 1:
          return STRUCTURES;
        case 2:
          return L3;
        case 3:
          return COVERAGE;
        default:
          return null;
      }
    }
  }
  
  private RadioButton visible;
  private ExpandArrow expand;
  private LayerType type;
  
  private Rectangle plus, merge, flatten, icon;
  
  private TextHandler text;
  private ArrayList<Sublayer> sublayers;
  
  private static Sublayer drag;
  private static boolean dragging;
  private boolean merging;
  
  public ArrayList<Sublayer> getSubs()
  {
    return sublayers;
  }
  
  
  public Layer(LayerType type, int header)
  {    
    this.type = type;
    visible = new RadioButton(164, header*16) {
      
      @Override
      public void click()
      {                
        for (Sublayer sub: sublayers)
        {
          sub.getRadioButton().setChecked(this.isChecked());
        }
      }
    };
    expand = new ExpandArrow(135, header*16) {
      @Override
      public void click()
      {
        
        Layer[] layers = Editor.getMap().getLayerControl().getLayers();
        boolean found = false;
        
        for (int i = layers.length - 1; i >= 0; i--)
        {
          if (found)
          {
            int push = getSectionalHeight();
            if (this.pointsDown())
            {
              push = -push;
            }
            
            layers[i].push(push*16);
          }
          
          if (this == layers[i].getExpandArrow())
            found = true;
        }

      }
    };
    text = new TextHandler(Draw.LAYER_MANAGER);
    sublayers = new ArrayList<Sublayer>();
    sublayers.add(new Sublayer((header-(sublayers.size()+1)*2)*16, type));
    sublayers.add(new Sublayer((header-(sublayers.size()+1)*2)*16, type));
    
    text.write(type.toString(), 32, header*16, new EarthboundFont(2));
    plus = new Rectangle(105, header*16 - getSectionalHeight()*16-16, 
        26, 26);
    merge = new Rectangle(131, header*16 - getSectionalHeight()*16-16, 
        26, 26);
    flatten = new Rectangle(157, header*16 - getSectionalHeight()*16-16, 
        26, 26);
    icon = new Rectangle(8, header*16, 16, 16);
  }
  
  public void render()
  {
    Draw.drawIfInBounds(Draw.LAYER_MANAGER, icon.x, icon.y, icon.width, icon.height, type.getTexX(), type.getTexY(), 
        type.getTexX()+16, type.getTexY()+16, 2);
    
    if (visible.isVisible())
    {
      visible.render();
    }
    if (expand.isVisible())
    {
      expand.render();
    }
    
    text.render();
    
    if (!expand.pointsDown())
    {
      if (!merging)
      {
        Draw.drawIfInBounds(Draw.LAYER_MANAGER, plus.x, plus.y, plus.width + merge.width + flatten.width, 
            plus.height,
            0, 92, 39, 105, 2);
      } else
      {
        Draw.drawIfInBounds(Draw.LAYER_MANAGER, plus.x, plus.y, 
            plus.width, plus.height, 66, 104, 79, 117, 2);
        Draw.drawIfInBounds(Draw.LAYER_MANAGER, flatten.x, flatten.y, 
            flatten.width, flatten.height, 26, 92, 39, 105, 2);
        if (checkBoxesChecked() <= 1)
        {
          Draw.drawIfInBounds(Draw.LAYER_MANAGER, merge.x, merge.y, 
              merge.width, merge.height, 81, 105, 94, 118, 2);
        } else
        {
          Draw.drawIfInBounds(Draw.LAYER_MANAGER, merge.x, merge.y, 
              merge.width, merge.height, 13, 92, 26, 105, 2);
        }
      }
      
      for (int i = sublayers.size()-1; i >= 0; i--)
      {
        sublayers.get(i).render();
      }
    }
  }
  
  public void update()
  {
    
    for (Sublayer sub: sublayers)
    {
      if (!expand.pointsDown() && sub.draggableArea(Mode.getFloatMouseX(), 
          Mode.getFloatMouseY()) && KeyHandler.doubleClick(Key.LBUTTON))
      {
        Editor.getMap().setCurrentSublayer(sub);
      } else if (!expand.pointsDown() && sub.draggableArea(Mode.getFloatMouseX(), 
          Mode.getFloatMouseY()) && KeyHandler.keyClick(Key.LBUTTON))
      {
        dragging = true;
        drag = sub;
      } else if (KeyHandler.keyRelease(Key.LBUTTON))
      {
        dragging = false;
      }
      
      if (dragging && drag!=sub && sub.draggableArea(Mode.getFloatMouseX(), 
          Mode.getFloatMouseY()) && 
          Editor.getMap().getLayerControl().layerContaining(drag) == 
          Editor.getMap().getLayerControl().layerContaining(sub))
      {
        ArrayList<Sublayer> subs = 
            Editor.getMap().getLayerControl().layerContaining(sub).getSubs();
        
        int index = subs.indexOf(drag);
        
        int dist = subs.indexOf(drag) - subs.indexOf(sub);
        
        drag.push(-32*dist);
        sub.push(32*dist);
        
        subs.set(subs.indexOf(sub), drag);
        subs.set(index, sub);
      }
    }
    
    if (visible.isVisible())
    {
      visible.update();
    }
    
    if (expand.isVisible())
    {
      expand.update();
    }
    
    if (!expand.pointsDown())
    {
      if (KeyHandler.bufferedKeyClick(Key.LBUTTON))
      {
        if (plus.contains(Mode.getFloatMouseX(), Mode.getFloatMouseY()))
        {
          if (merging)
          {
            merging = false;
            for (Sublayer s: sublayers) {
              s.setMerging(false);
            }
          } else
          {
            sublayers.add(new Sublayer(plus.y + 16, type));
            
            boolean push = false;
            
            for (int i = Editor.getMap().getLayerControl().getLayers().length - 1; i >=0; i--)
            {
              if (push)
              {
                Editor.getMap().getLayerControl().getLayers()[i].push(32);
              }
              
              if (this == Editor.getMap().getLayerControl().getLayers()[i])
              {
                push = true;
                plus.y-=32;
                merge.y-=32;
                flatten.y-=32;

              }
            }
            
            KeyHandler.tempDisable(Key.LBUTTON);
          }
        } else if (merge.contains(Mode.getFloatMouseX(), Mode.getFloatMouseY()))
        {
          
          if (merging && checkBoxesChecked() > 1 && confirmMerge())
          {
            
            Sublayer merge = Sublayer.merge(selectedSublayers());
            
            Sublayer[] removees = selectedSublayers();
            
            for (Sublayer remove: removees)
            {
              sublayers.remove(remove);
            }
            
            sublayers.add(merge);
            merge.getTextField().setText("Merged");
            
            boolean layPush = false;

            for (Layer l: Editor.getMap().getLayerControl().getLayers())
            {
              if (l.getSubs().contains(merge))
              {
                l.alignSublayers();
                
                layPush = true;
                l.pushButtons(-32*(removees.length-1));
              }

              if (!layPush)
              {
                l.push(-32*(removees.length-1));
              }
            } 
            
            merging = false;
            for (Sublayer s: sublayers) {
              s.setMerging(false);
              s.getCheckBox().setChecked(false);
            }
          } else
          {
            merging = true;
            for (Sublayer s: sublayers) {
              s.setMerging(true);
            }
          }
          
        } else if (flatten.contains(Mode.getFloatMouseX(), Mode.getFloatMouseY())
            && confirmMerge())
        {
          int arrSize = sublayers.size();
          
          Sublayer merge = Sublayer.merge(sublayers.toArray(
              new Sublayer[sublayers.size()]));
          
          sublayers.clear();
          
          sublayers.add(merge);
          merge.getTextField().setText("Flattened");
          
          boolean layPush = false;

          for (Layer l: Editor.getMap().getLayerControl().getLayers())
          {
            if (l.getSubs().contains(merge))
            {
              l.alignSublayers();
              
              layPush = true;
              l.pushButtons(-32*(arrSize-1));
            }

            if (!layPush)
            {
              l.push(-32*(arrSize-1));
            }
          }
        }
      }
      
      for (Sublayer sub: sublayers)
      {
        sub.update();
      }
    }
  }
  
  public ExpandArrow getExpandArrow()
  {
    return expand;
  }
  
  public int getSectionalHeight()
  {
    return sublayers.size()*2+2;
  }
  
  public int getVisibleHeight()
  {
    if (expand.pointsDown())
    {
      return 24;
    } else
    {
      return sublayers.size()*32+2*32;
    }
  }
  
  public void push(int num)
  {
    visible.push(num);
    expand.push(num);
    text.move(type.toString(), 0, -num);
    
    for (Sublayer sub: sublayers)
    {
      sub.push(num);
    }
    
    pushButtons(num);
    icon.y-=num;
  }
  
  
  
  public void pushButtons(int num)
  {
    plus.y-=num;
    merge.y-=num;
    flatten.y-=num;
  }
  
  public LayerType getLayerType()
  {
    return type;
  }
  
  public int checkBoxesChecked()
  {
    int num = 0;
    for (Sublayer s: sublayers)
    {
      if (s.getCheckBox().isChecked())
      {
        num++;
      }
    }
    
    return num;
  }
  
  public Sublayer[] selectedSublayers()
  {
    Sublayer[] subs = new Sublayer[checkBoxesChecked()];
    int i = 0;
    
    for (Sublayer s: sublayers)
    {
      if (s.getCheckBox().isChecked())
      {
        subs[i] = s;
        i++;
      } 
    }
    
    System.out.println(subs.length);
    return subs;
  }
  
  private static boolean confirmMerge()
  {
    int returnee = JOptionPane.showConfirmDialog(null, 
        "Some sublayers will be deleted in the merge. Continue?");
    
    return (returnee == JOptionPane.YES_OPTION);
  }
  
  public void alignSublayers()
  {
    for (int i = 0; i < sublayers.size(); i++)
    {
      sublayers.get(i).setLocation(icon.y - 32*(i+1));
    }
    
    plus.y = merge.y = flatten.y = icon.y - 32*(sublayers.size()+1) - 16;
    
  }
  
  public boolean hasSublayers()
  {
    return !sublayers.isEmpty();
  }
  
}
