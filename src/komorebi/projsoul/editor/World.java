package komorebi.projsoul.editor;

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

import komorebi.projsoul.editor.modes.ConnectMode;
import komorebi.projsoul.map.ConnectMap;
import komorebi.projsoul.map.ConnectMap.Side;

public class World {

  private String name;
  private String reference = "";

  private ArrayList<ConnectMap> maps = new ArrayList<ConnectMap>();
  private ArrayList<ConnectMap> deadMaps = new ArrayList<ConnectMap>();
  private static ArrayList<World> worlds;
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

  public static World getWorld(String str)
  {
    for (World world: worlds)
    {
      if (world.getName().equalsIgnoreCase(str))
      {
        return world;
      }
    }

    return null;
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

  public void setReference(String ref)
  {
    reference = ref;
  }

  public boolean emptyReference()
  {
    return reference.isEmpty();
  }

  public ConnectMap getReference()
  {
    for (ConnectMap c: maps)
    {
      if (c.matches(reference))
      {
        return c;
      }
    }

    return null;
  }

  public static void loadWorlds()
  {
    System.out.println("Worlds ever loaded?");

    worlds = new ArrayList<World>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(
          new File(worldFile)));

      String str;

      while ((str = reader.readLine())!=null)
      {
        if (!str.startsWith("#refs"))
        {
          worlds.add(new World(str));
        } else
        {
          String loc = str.substring(str.indexOf("{")+1, str.indexOf("}"));
          str = str.replace("#refs", "");
          str = str.replace("{" + loc + "}", "");
          str = str.trim();

          World.getWorld(loc).setReference("res/maps/" + str);

          ConnectMap c = new ConnectMap(World.getWorld(loc), "res/maps/" + str,
              0,0);
          //World.getWorld(loc).setMaps(c.map());
        }
      }

      reader.close();

      worldsLoaded = true;
    } catch (IOException e)
    {
      e.printStackTrace();
    }
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
    private World selWorld;

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

      for (World world: worlds)
      {
        worldModel.addElement(world.getName());
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
          if (World.contains(text.getText()))
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

          selWorld = World.findWorld(worldList.getSelectedValue());
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

    public World getSelectedWorld()
    {
      return selWorld;
    }

    public abstract void terminateSuccess();
    public abstract void terminateFailure();

  }

  public static boolean contains(String s)
  {
    for (World world: worlds)
    {
      if (world.getName().equalsIgnoreCase(s))
      {
        return true;
      }
    }

    return false;
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

  public static World findWorld(ConnectMap c)
  {
    for (World world: worlds)
    {
      for (ConnectMap map: world.getMaps())
      {        
        if (map.equals(c))
        {
          return world;
        }
      }

    }

    return null;
  }

  public static World findWorld(String s)
  {

    for (World world: worlds)
    {      
      if (world.getName().equals(s))
      {
        return world;
      }
    }

    return null;
  }

  public ArrayList<ConnectMap> getMaps()
  {
    return maps;
  }

  public static boolean hasWorld(String map)
  {
    for (World world: worlds)
    {
      for (ConnectMap c: world.getMaps())
      {
        if (c.getFilePath().equals(map))
          return true;
      }
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

  public static World findWorldContainingMap(String key) 
      throws NoSuchElementException {

    for (World world: worlds)
    {
      try 
      {
        ConnectMap c = world.getMap(key);
        return world;
      } catch (NoSuchElementException e)
      {
        continue;
      }
    }

    throw new NoSuchElementException();
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

    } else if (c.getFilePath().equals(reference))
    {
      if (maps.isEmpty())
      {
        reference = "";
      } else
      {
        reference = maps.get(0).getFilePath();
      }

      saveReference();
    }

    ConnectMode.clearSelection();
  }

  public void saveReference()
  {
    try {
      File temp = File.createTempFile("tmp", "");

      BufferedReader reader = new BufferedReader(new FileReader(
          new File(worldFile)));
      BufferedWriter writer = new BufferedWriter(new FileWriter(temp));

      String str;

      while ((str = reader.readLine())!=null)
      {        
        if (str.startsWith("#refs {" + name + "}"))
        {
          writer.write("#refs {" + name + "} " + reference.replace("res/maps/", 
              "") + "\n");
        } else
        {
          writer.write(str + "\n");
        }
      }

      reader.close();
      writer.close();

      File oldFile = new File(worldFile);
      if (oldFile.delete())
      {
        temp.renameTo(oldFile);
      }

    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

  }



  public ArrayList<ConnectMap> getDeadMaps()
  {
    return deadMaps;
  }
}