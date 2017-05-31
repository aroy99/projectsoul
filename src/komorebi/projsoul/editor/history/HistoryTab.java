package komorebi.projsoul.editor.history;

import komorebi.projsoul.editor.controls.TabControl;
import komorebi.projsoul.editor.controls.TabControl.Tab;
import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.script.text.TextHandler;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

public class HistoryTab extends Tab {
  
  private static ArrayList<Revision> revisions = new ArrayList<Revision>();
  
  private static final int CLEAR_ALL = 0, CLEAR_ABOVE = 1, CLEAR_BELOW = 2;
  private static final Rectangle GARBAGE_ICONS =
      new Rectangle(40, 20, 96, 32);
  
  
  private static final int MAX_REVISIONS = 20;
  
  private int curr;
  protected TextHandler text;
  
  public HistoryTab()
  {
    tabTitle = "History";
    text = new TextHandler();
    
    curr = -1;
  }
  
  @Override
  public void update() {
    
    if (GARBAGE_ICONS.contains(
        Mode.getFloatMouseX(), Mode.getFloatMouseY()))
    {
      if (KeyHandler.keyClick(Key.LBUTTON))
      {
        int hoveringOver = (int) 
            (Mode.getFloatMouseX()-GARBAGE_ICONS.x)/32;
        switch (hoveringOver)
        {
          case CLEAR_ALL:
            clearAll();
            break;
          case CLEAR_ABOVE:
            clearAboveCurrent();
            break;
          case CLEAR_BELOW:
            clearBelowCurrent();
            break;
        }
      }
    }
    
    for (Revision revision: revisions)
    {
      if (revision.isDoubleClicked())
      {
        setCurrentRevision(revisions.indexOf(revision));
      }
    }
    
  }

  @Override
  public void render() {
    for (int i = 0; i < revisions.size(); i++)
    {
      revisions.get(i).render();
    }
    
    if (GARBAGE_ICONS.contains(
        Mode.getFloatMouseX(), Mode.getFloatMouseY()))
    {
      int hoveringOver = (int) (Mode.getFloatMouseX()-GARBAGE_ICONS.x)/32;
      
      Draw.rect(hoveringOver*32 + GARBAGE_ICONS.x, GARBAGE_ICONS.y,
          32, 32, 64, 0, 96, 32, 2); 
    }
    
    Draw.drawIfInBounds(Draw.LAYER_MANAGER, 5, 
        revisions.get(curr).getY(), 4, 7, 27, 108, 31, 115, 2);
    
    Draw.rect(GARBAGE_ICONS.x, GARBAGE_ICONS.y, GARBAGE_ICONS.width, 
        GARBAGE_ICONS.height, 192, 0, 240, 16, 2); //Garbage icons
    
    Revision.showText(); //renders the revisions' descriptions
  }
  
  public void addRevision(Revision r)
  {    
    if (revisions.size() >= MAX_REVISIONS)
    {
      revisions.remove(0);
    }
    
    revisions.add(r);
    curr = revisions.size() - 1;
    
    alignRevisions();
    alignText();
  }
  
  public void clearBelowCurrent()
  {        
    Iterator<Revision> iterator = revisions.iterator();
    
    for (int i = 0; i <= curr; i++)
    {
      iterator.next();
    }
    
    while (iterator.hasNext())
    {
      iterator.next();
      iterator.remove();
    }
    
    alignRevisions();
    alignText();
  }
  
  private void clearAboveCurrent()
  {        
    Iterator<Revision> iterator = revisions.iterator();
    
    for (int i = 0; i < curr; i++)
    {
      iterator.next();
      iterator.remove();
    }
    
    curr = 0;
    
    alignText();
    alignRevisions();
  }
 
  
  private void alignRevisions()
  {
    for (int i = 0; i < revisions.size(); i++)
    {
      revisions.get(i).setY(TabControl.MIN_HT-25-24*i);
    }
  }
  
  private void clearAll()
  {
    revisions.clear();
    addRevision(new HistoryClearedRevision());
    
    curr = 0;
  }
  
  private void alignText()
  {
    Revision.getTextHandler().clear();
    
    for (int i = 0; i < revisions.size(); i++)
    {
      Revision.getTextHandler().write(revisions.get(i).getDescription(), 
          35, TabControl.MIN_HT-21-24*i);
    }
  }
  
  public void updateBackground()
  {
    if (KeyHandler.controlDown() && KeyHandler.keyClick(Key.Z))
    {
      if (revisions.size()>0 && curr > 0)
      {
        revisions.get(curr).undo();
        curr--;
      }
    }
    
    if (KeyHandler.controlDown() && KeyHandler.keyClick(Key.Y))
    {
      if (curr < revisions.size()-1)
      {
        curr++;
        revisions.get(curr).redo();
      }
    }
  }
  
  private void setCurrentRevision(int index)
  {
    if (index > curr)
    {
      for (int i = curr + 1; i <= index; i++)
      {
        revisions.get(i).redo();
      }
    } else
    {
      for (int i = curr; i > index; i--)
      {
        revisions.get(i).undo();
      }
    }
    
    curr = index;
  }
}
