package komorebi.projsoul.editor.controls;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.script.EarthboundFont;
import komorebi.projsoul.script.TextHandler;

public class TabControl {
  
  private TextHandler text;
  
  public static final int MIN_HT = 34*16;
  protected static final int WIDTH = 12*16;
  
  private ArrayList<Tab> tabs = new ArrayList<Tab>();
  private int currTab;
  
  public TabControl()
  {
    text = new TextHandler();
  }
  
  public static abstract class Tab
  {
    
    protected boolean tabbed;
    protected String tabTitle;
    protected int index;
    
    private Rectangle area;
    
    public abstract void update();
    public abstract void render();
    
    public String getTabTitle()
    {
      return tabTitle;
    }
    
    public int getIndex()
    {
      return index;
    }
    
    public boolean isTabbed()
    {
      return tabbed;
    }
    
    public void setIndex(int index)
    {
      this.index = index;
    }
    
    public void setTabbed(boolean tabbed)
    {
      this.tabbed = tabbed;
    }
    
    public void showTab()
    {
      if (tabbed)
      {
        Draw.rect(index*64, 34*16, 64, 24, 172, 82, 204, 94, 2);
      } else
      {
        Draw.rect(index*64, 34*16, 64, 24, 204, 82, 236, 94, 2);
      }
    }
    
    public void setRect(Rectangle rect)
    {
      area = rect;
    }
    
    public boolean containsMouse()
    {
      return area.contains(new Point((int) Mode.getFloatMouseX(), 
          (int) Mode.getFloatMouseY()));
    }
  }
  
  public void addTab(Tab tab)
  {
    tabs.add(tab);
    tab.setIndex(tabs.size()-1);
    tab.setRect(new Rectangle(tab.getIndex()*64, 34*16, 64, 24));
    
    text.write(tab.getTabTitle(), tab.getIndex()*64 + 6, 34*16+4, 
        new EarthboundFont(2));
  }
  
  public void update()
  {
    if (KeyHandler.keyClick(Key.LBUTTON))
    {
      for (Tab t: tabs)
      {
        if (t.containsMouse())
        {
          setCurrTab(t.getIndex());
        }
      }
    }
    
    
    tabs.get(currTab).update();
  }
  
  public void render()
  {    
    Draw.rect(0, 34*16, WIDTH, 24, 238, 82, 239, 83, 2);
    
    for (Tab t: tabs)
    {
      if (!t.isTabbed())
      {
        t.showTab();
        text.render(text.getWord(t.getTabTitle()));
      }
    }
    
    Draw.rect(0, 0, WIDTH, MIN_HT, 0, 66, 1, 67, 2);
    
    for (Tab t: tabs)
    {
      if (t.isTabbed())
      {
        t.showTab();
        text.render(text.getWord(t.getTabTitle()));
      }
    }
    
    
    tabs.get(currTab).render();

  }
  
  public void setCurrTab(int i)
  {
    tabs.get(currTab).setTabbed(false);
    currTab = i;
    tabs.get(currTab).setTabbed(true);
  }
  
}
