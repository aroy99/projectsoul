package komorebi.projsoul.editor.modes.connect;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import komorebi.projsoul.map.ConnectMap;
import komorebi.projsoul.map.ConnectMap.Side;

public class World {

  private String name;

  private ArrayList<ConnectMap> maps = new ArrayList<ConnectMap>();
  private ArrayList<ConnectMap> deadMaps = new ArrayList<ConnectMap>();
  private static ArrayList<WorldShell> worlds = new ArrayList<WorldShell>();
  private static boolean worldsLoaded = false;

  private static final String worldFile = "res/worlds/worlds.txt";

  public World(String name)
  {
    this.name = name;
    maps = new ArrayList<ConnectMap>();
  }

  public void addMap(ConnectMap c)
  {
    maps.add(c);
  }

  public String getName()
  {
    return name;
  }

  public boolean contains(ConnectMap c)
  {
    return maps.contains(c);
  }

  public void setMaps(ArrayList<ConnectMap> arr)
  {
    maps.clear();
    maps.addAll(arr);
  }
  
  public static boolean worldExists(String world)
  {
    for (WorldShell w: worlds)
    {     
      if (w.getWorldName().equals(world))
        return true;
    }
    
    return false;
  }

  public ConnectMap getMap(String str) throws NoSuchElementException
  {
    for (ConnectMap c: maps)
    {      
      if (c.getFilePath().equals(str))
      {
        return c;
      }
    }

    throw new NoSuchElementException(name + " does not contain " + str);
  }

  public static class TerminableActionListener implements ActionListener
  {

    JDialog parent;

    public TerminableActionListener(JDialog parent)
    {
      this.parent = parent;
    }


    @Override
    public void actionPerformed(ActionEvent arg0) {
      parent.setVisible(false);
    }

  }

  public abstract static class FindWorldDialog extends JDialog 
  {

    private static final Dimension DIALOG_SIZE = new Dimension(350,350);
    private static final Rectangle SCROLL_BOUNDS = new Rectangle(50, 75, 150, 200);

    private static final Rectangle TEXTBOX_BOUNDS = new Rectangle(50, 25, 150, 25);
    private static final Rectangle ADD_BUTTON = new Rectangle(200, 25, 75, 25);
    private static final Rectangle SEL_BUTTON = new Rectangle(200, 248, 75, 25);

    private JList<String> worldList;
    private DefaultListModel<String> worldModel;
    private JScrollPane worldPane;

    private JTextField text;
    private JButton add, sel;

    private JPanel panel;
    private WorldShell selWorld;

    public FindWorldDialog()
    {
      this.setSize(DIALOG_SIZE);

      panel = new JPanel(null);
      panel.setSize(DIALOG_SIZE);

      worldModel = new DefaultListModel<String>();

      if (!worldsLoaded)
      {
        loadWorlds();
      }

      this.addWindowListener(new WindowListener()
      {

        public void windowActivated(WindowEvent e) {}

        public void windowClosed(WindowEvent e) {}

        public void windowClosing(WindowEvent e) {
          terminateFailure();
        }

        public void windowDeactivated(WindowEvent e) {}

        public void windowDeiconified(WindowEvent e) {}

        public void windowIconified(WindowEvent e) {}

        public void windowOpened(WindowEvent e) {}

      });

      for (WorldShell world: worlds)
      {
        worldModel.addElement(world.getWorldName());
      }

      worldList = new JList<String>(worldModel);
      worldPane = new JScrollPane(worldList);

      worldPane.setBounds(SCROLL_BOUNDS);

      text = new JTextField();
      text.setBounds(TEXTBOX_BOUNDS);
      text.getDocument().addDocumentListener(new DocumentListener() {

        private void checkLength()
        {
          if (text.getText().length() > 0)
          {
            add.setEnabled(true);
          } else
          {
            add.setEnabled(false);
          }
        }


        @Override
        public void changedUpdate(DocumentEvent arg0) {
          checkLength();
        }

        @Override
        public void insertUpdate(DocumentEvent arg0) {
          checkLength();          
        }

        @Override
        public void removeUpdate(DocumentEvent arg0) {
          checkLength();          
        }
      });

      add = new JButton("Add");
      add.setBounds(ADD_BUTTON);
      add.setEnabled(false);
      add.addActionListener(new ActionListener() {

        //When the button is clicked, adds the contents of the textfield to
        //the listbox
        public void actionPerformed(ActionEvent arg0) {
          if (World.worldExists(text.getText()))
          {
            JOptionPane.showMessageDialog(null, "A world already exists with " +
                "the name " + text.getText() + ".");
            text.selectAll();
          } else if (text.getText().startsWith("#refs"))
          {
            JOptionPane.showMessageDialog(null, "Worlds cannot start with the " +
                "the reserved keyword #refs.");
            text.selectAll();
          } else
          {
            World.writeToWorldFile(text.getText());
            worldModel.addElement(text.getText());
            text.setText("");
          }
        }

      });

      sel = new JButton("Select");
      sel.setBounds(SEL_BUTTON);
      sel.setEnabled(worldList.getSelectedIndex()!=-1);
      sel.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent arg0) {

          selWorld = World.findWorldContainingMap(worldList.getSelectedValue());
          terminate();
          terminateSuccess();
        }


      });

      worldList.addListSelectionListener(new ListSelectionListener() {

        @Override
        public void valueChanged(ListSelectionEvent arg0) {
          sel.setEnabled(worldList.getSelectedIndex()!=-1);
        }

      });

      panel.add(worldPane);
      panel.add(text);
      panel.add(add);
      panel.add(sel);

      this.add(panel);
      this.setTitle("Add/Select Worlds");
      this.setResizable(false);
      this.setVisible(true);
    }

    private void terminate()
    {
      this.setVisible(true);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public WorldShell getSelectedWorld()
    {
      return selWorld;
    }

    public abstract void terminateSuccess();
    public abstract void terminateFailure();

  }
  
  public static String[] getAllWorldNames()
  {
    String[] worldNames = new String[worlds.size()];
    
    for (int i = 0; i < worldNames.length; i++)
    {
      worldNames[i] = worlds.get(i).getWorldName();
    }
    
    return worldNames;
  }

  public static void writeToWorldFile(String add)
  {
    try {
      FileWriter fw = new FileWriter(worldFile,true);
      fw.write("\n"+ add);
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static WorldShell findWorldWithName(String name)
  {
    for (WorldShell world: worlds)
    {
      if (world.getWorldName().equals(name))
        return world;
    }
    
    throw new NoSuchElementException(name + " does not exist as a world");
  }

  public static WorldShell findWorldContainingMap(String map)
  {
    for (WorldShell world: worlds)
    {
      if (world.contains(map))
        return world;
    }
    
    throw new NoSuchElementException(map + " is not contained in a world");
  }

  public ArrayList<ConnectMap> getMaps()
  {
    return maps;
  }

  public static boolean hasWorldContainingMap(String map)
  {    
    for (WorldShell world: worlds)
    {
      if (world.contains(map))
        return true;
    }

    return false;
  }

  public boolean mapAtValidLocation(Rectangle r) {
    //TODO Re-add the code you already added but lost bc reasons

    boolean touches = false;

    for (ConnectMap c: maps)
    {
      Rectangle area = c.getArea();

      if (area.intersects(r))
      {
        return false;
      }

      if (adjacent(r, area))
      {
        touches = true;
      }
    }

    return touches;
  }

  public boolean mapAtValidLocationIgnoreSelf(Rectangle r, ConnectMap someMap) {

    boolean touches = false;

    for (ConnectMap c: maps)
    {
      Rectangle area = c.getArea();

      if (area.intersects(r) && someMap != c)
      {
        return false;
      } else if (someMap == c)
      {
        continue;
      }

      if (adjacent(r, area))
      {
        touches = true;
      }
    }
    return touches;
  }

  public static boolean adjacent(Rectangle r1, Rectangle r2)
  {
    boolean touches = false;

    if (r1.x + r1.width == r2.x && (((r2.y >= r1.y && r2.y <
        r1.y + r1.height) || (r2.y + r2.height > r1.y && r2.y + r2.height 
            < r1.y + r1.height)) || 
        ((r1.y >= r2.y && r1.y <
        r2.y + r2.height) || (r1.y + r1.height > r2.y && r1.y + r1.height 
            < r2.y + r2.height))))
    {
      touches = true;
    }

    if (r2.x + r2.width == r1.x && (((r2.y >= r1.y && r2.y <
        r1.y + r1.height) || (r2.y + r2.height > r1.y && r2.y + r2.height 
            < r1.y + r1.height)) || 
        ((r1.y >= r2.y && r1.y <
        r2.y + r2.height) || (r1.y + r1.height > r2.y && r1.y + r1.height 
            < r2.y + r2.height))))
    {
      touches = true;
    }

    if (r1.y + r1.height == r2.y && (((r2.x >= r1.x && r2.x
        < r1.x + r1.width) || (r2.x + r2.width > r1.x && r2.x + r2.width 
            < r1.x + r1.width)) || 
        ((r1.x >= r2.x && r1.x <
        r2.x + r2.width) || (r1.x + r1.width > r2.x && r1.x + r1.width 
            < r2.x + r2.width))))
    {
      touches = true;
    }

    if (r2.y + r2.height == r1.y && (((r2.x >= r1.x && r2.x
        < r1.x + r1.width) || (r2.x + r2.width > r1.x && r2.x + r2.width 
            < r1.x + r1.width)) || 
        ((r1.x >= r2.x && r1.x <
        r2.x + r2.width) || (r1.x + r1.width > r2.x && r1.x + r1.width 
            < r2.x + r2.width))))
    {
      touches = true;
    }

    return touches;
  }


  /**
   * Returns what side of r1 r2 is located
   * @param r1 The first rectangle
   * @param r2 The second rectangle
   * @return The side of r1 on which r2 lies
   */
  public static Side connectSide(Rectangle r1, Rectangle r2)
  {
    if (r1.x + r1.width == r2.x && (((r2.y >= r1.y && r2.y <
        r1.y + r1.height) || (r2.y + r2.height > r1.y && r2.y + r2.height 
            < r1.y + r1.height)) || 
        ((r1.y >= r2.y && r1.y <
        r2.y + r2.height) || (r1.y + r1.height > r2.y && r1.y + r1.height 
            < r2.y + r2.height))))
    {
      return Side.RIGHT;
    }

    if (r2.x + r2.width == r1.x && (((r2.y >= r1.y && r2.y <
        r1.y + r1.height) || (r2.y + r2.height > r1.y && r2.y + r2.height 
            < r1.y + r1.height)) || 
        ((r1.y >= r2.y && r1.y <
        r2.y + r2.height) || (r1.y + r1.height > r2.y && r1.y + r1.height 
            < r2.y + r2.height))))
    {
      return Side.LEFT;
    }

    if (r1.y + r1.height == r2.y && (((r2.x >= r1.x && r2.x
        < r1.x + r1.width) || (r2.x + r2.width > r1.x && r2.x + r2.width 
            < r1.x + r1.width)) || 
        ((r1.x >= r2.x && r1.x <
        r2.x + r2.width) || (r1.x + r1.width > r2.x && r1.x + r1.width 
            < r2.x + r2.width))))
    {
      return Side.UP;
    }

    if (r2.y + r2.height == r1.y && (((r2.x >= r1.x && r2.x
        < r1.x + r1.width) || (r2.x + r2.width > r1.x && r2.x + r2.width 
            < r1.x + r1.width)) || 
        ((r1.x >= r2.x && r1.x <
        r2.x + r2.width) || (r1.x + r1.width > r2.x && r1.x + r1.width 
            < r2.x + r2.width))))
    {
      return Side.DOWN;
    }

    return null;
  }

  public void updateConnections(ConnectMap compare)
  {
    for (ConnectMap c: maps)
    {
      if (c==compare)
        continue;

      if (c.isConnectedTo(compare) && 
          !World.adjacent(c.getArea(), compare.getArea()))
      {
        c.breakConnection(compare);
        compare.breakConnection(c);

      } else if (!c.isConnectedTo(compare) && 
          World.adjacent(c.getArea(), compare.getArea()))
      {
        c.addConnection(compare);
        compare.addConnection(c);
      }

    }

    for (ConnectMap c: maps)
    {
      System.out.println(c);
    }
  }

  private void removeAllReferencesTo(ConnectMap c)
  {
    for (ConnectMap con: c.getConnections())
    {
      con.breakConnection(c);
    }

    c.clearConnections();
    maps.remove(c);
    deadMaps.add(c);
  }

  public boolean allMapsConnected()
  {
    if (!maps.isEmpty())
    {
      ArrayList<ConnectMap> test = maps.get(0).map();
      return test.size()==maps.size();
    }

    return true;
  }

  public void tryRemove(ConnectMap c)
  {
    ArrayList<ConnectMap> refs = new ArrayList<ConnectMap>();

    for (ConnectMap ref: c.getConnections())
    {
      refs.add(ref);
    }

    removeAllReferencesTo(c);

    if (!allMapsConnected())
    {
      for (ConnectMap con: refs)
      {
        con.addConnection(c);
        c.addConnection(con);
      }

      maps.add(c);
      deadMaps.remove(c);

      JOptionPane.showMessageDialog(null, 
          "You cannot remove this map; removing it would leave some maps " +
          "unconnected.");

    } 
    

    ConnectMode.clearSelection();
  }

  public ArrayList<ConnectMap> getDeadMaps()
  {
    return deadMaps;
  }
  
  public static void loadWorlds()
  {
    try {
      BufferedReader read = new BufferedReader(new FileReader(worldFile));
      
      String str;
      WorldShell currWorld = null;
      
      while ((str = read.readLine()) != null && !str.isEmpty())
      {
        if (str.startsWith("#ref"))
        {
          str = str.replace("#ref", "");
          
          String[] maps = str.split(",");
          
          for (String map: maps)
          {
            currWorld.addMap(map.trim());
          }
          
        } else
        {
          currWorld = new WorldShell(str);
          worlds.add(currWorld);
        }
      }
      
      read.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
  
  public static void addWorld(WorldShell world)
  {
    worlds.add(world);
    
    writeToWorldFile(world.toString());
  }
  
  public static void main(String[] args)
  {
    loadWorlds();
    
    for (WorldShell world: worlds)
    {
      System.out.println(world);
    }
    
    System.out.println();
    
    System.out.println(findWorldContainingMap("testA.map"));
  }
}