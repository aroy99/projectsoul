package komorebi.projsoul.editor;

import komorebi.projsoul.editor.controls.ExpandArrow;
import komorebi.projsoul.editor.controls.RadioButton;
import komorebi.projsoul.editor.history.AddSublayerRevision;
import komorebi.projsoul.editor.history.MergeRevision;
import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.editor.modes.TileMode;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.TextHandler;

import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Layer {
  
  private RadioButton visibilityRadioButton;
  private ExpandArrow expandArrow;
  private LayerType type;
  
  private Rectangle plusButton, mergeButton, flattenButton, cancelButton, icon;
  
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
    visibilityRadioButton = new RadioButton(164, header*16) {
      
      @Override
      public void click()
      {                
        for (Sublayer sub: sublayers)
        {
          sub.getRadioButton().setChecked(this.isChecked());
        }
      }
    };
    expandArrow = new ExpandArrow(135, header*16) {
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
            
            //layers[i].push(push*16);
          }
          
          if (this == layers[i].getExpandArrow())
            found = true;
        }

      }
    };
    text = new TextHandler(Draw.LAYER_MANAGER);
    sublayers = new ArrayList<Sublayer>();
    sublayers.add(new Sublayer(type));
    sublayers.add(new Sublayer(type));
    
    icon = new Rectangle(8, header*16, 16, 16);
    
    text.write(type.toString(), 32, icon.y, new EarthboundFont(2));
    plusButton = cancelButton = new Rectangle(105, header*16 - getSectionalHeight()*16-16, 
        26, 26);
    mergeButton = new Rectangle(131, header*16 - getSectionalHeight()*16-16, 
        26, 26);
    flattenButton = new Rectangle(157, header*16 - getSectionalHeight()*16-16, 
        26, 26);
  }
  
  public void render()
  {
    Draw.drawIfInBounds(Draw.LAYER_MANAGER, icon.x, icon.y, icon.width, icon.height, 
        type.getTexX(), type.getTexY(), type.getTexX()+16, type.getTexY()+16, 2);
    
    if (visibilityRadioButton.isVisible())
    {
      visibilityRadioButton.render();
    }
    if (expandArrow.isVisible())
    {
      expandArrow.render();
    }
    
    text.render();
    
    if (!expandArrow.pointsDown())
    {
      if (!merging)
      {
        Draw.drawIfInBounds(Draw.LAYER_MANAGER, plusButton.x, plusButton.y, plusButton.width + mergeButton.width + flattenButton.width, 
            plusButton.height,
            0, 92, 39, 105, 2);
      } else
      {
        Draw.drawIfInBounds(Draw.LAYER_MANAGER, plusButton.x, plusButton.y, 
            plusButton.width, plusButton.height, 66, 104, 79, 117, 2);
        Draw.drawIfInBounds(Draw.LAYER_MANAGER, flattenButton.x, flattenButton.y, 
            flattenButton.width, flattenButton.height, 26, 92, 39, 105, 2);
        if (checkBoxesChecked() <= 1)
        {
          Draw.drawIfInBounds(Draw.LAYER_MANAGER, mergeButton.x, mergeButton.y, 
              mergeButton.width, mergeButton.height, 81, 105, 94, 118, 2);
        } else
        {
          Draw.drawIfInBounds(Draw.LAYER_MANAGER, mergeButton.x, mergeButton.y, 
              mergeButton.width, mergeButton.height, 13, 92, 26, 105, 2);
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
      if (!expandArrow.pointsDown() && sub.draggableArea(Mode.getFloatMouseX(), 
          Mode.getFloatMouseY()) && KeyHandler.doubleClick(Key.LBUTTON))
      {
        Editor.getMap().setCurrentSublayer(sub);
        TileMode.updateCurrentSublayer();
      } else if (!expandArrow.pointsDown() && sub.draggableArea(Mode.getFloatMouseX(), 
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
        
        //drag.push(-32*dist);
        //sub.push(32*dist);
        
        subs.set(subs.indexOf(sub), drag);
        subs.set(index, sub);
      }
    }
    
    if (visibilityRadioButton.isVisible())
    {
      visibilityRadioButton.update();
    }
    
    if (expandArrow.isVisible())
    {
      expandArrow.update();
    }
    
    if (!expandArrow.pointsDown())
    {
      if (KeyHandler.bufferedKeyClick(Key.LBUTTON))
      {
        if (plusButton.contains(Mode.getFloatMouseX(), Mode.getFloatMouseY()))
        {
          if (merging)
          {
            merging = false;
            for (Sublayer s: sublayers) {
              s.setMerging(false);
              s.getCheckBox().setChecked(false);
            }
            
          } else
          {
            Sublayer newSub;
            
            sublayers.add((newSub = new Sublayer(type)));
            
            Editor.getMap().addRevision(new AddSublayerRevision(newSub, sublayers));
                        
            KeyHandler.tempDisable(Key.LBUTTON);
          }
        } else if (mergeButton.contains(Mode.getFloatMouseX(), Mode.getFloatMouseY()))
        {
          
          if (merging && checkBoxesChecked() > 1 && confirmMerge())
          {
            
            Sublayer merge = Sublayer.merge(selectedSublayers());
            
            Sublayer[] removees = selectedSublayers();
            
            for (Sublayer remove: removees)
            {
              sublayers.remove(remove);
              remove.setMerging(false);
            }
            
            Editor.getMap().addRevision(new MergeRevision(this, 
                removees, merge));
            
            sublayers.add(merge);
            merge.getTextField().setText("Merged");
            
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
          
        } else if (flattenButton.contains(Mode.getFloatMouseX(), Mode.getFloatMouseY())
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
            }

            if (!layPush)
            {
              //l.push(-32*(arrSize-1));
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
    return expandArrow;
  }
  
  public int getSectionalHeight()
  {
    return sublayers.size()*2+2;
  }
  
  public int getVisibleHeight()
  {
    if (expandArrow.pointsDown())
    {
      return 32;
    } else
    {
      return sublayers.size()*32+2*32;
    }
  }
  
  /*
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
  }*/
  
  
  
  public void pushButtons(int num)
  {
    plusButton.y-=num;
    mergeButton.y-=num;
    flattenButton.y-=num;
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
    
    plusButton.y = mergeButton.y = flattenButton.y = icon.y - 32*(sublayers.size()+1) - 16;
    
  }
  
  public boolean hasSublayers()
  {
    return !sublayers.isEmpty();
  }
  
  public void setY(int y)
  {
    icon.y = y;
    expandArrow.setLocation(135, y);
    visibilityRadioButton.setLocation(164, y);
    
    text.clear();
    text.write(type.toString(), 32, icon.y, new EarthboundFont(2));
    
    alignSublayers();
  }
  
}
