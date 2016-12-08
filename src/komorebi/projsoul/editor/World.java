package komorebi.projsoul.editor;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
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

import komorebi.projsoul.map.ConnectMap;
import komorebi.projsoul.script.Lock;

public class World {
    
  private String name;
  private String reference = "";
    
  private ArrayList<ConnectMap> maps = new ArrayList<ConnectMap>();
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
  
  public static class FindWorldDialog extends JDialog 
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
    private Lock lock;
    private World selWorld;
    
    public FindWorldDialog(Lock lock)
    {
      this();
      this.lock = lock;
    }
   
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
           if (lock != null)
           {
             lock.resumeThread();
           }
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
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public World getSelectedWorld()
    {
      lock.pauseThread();
      return selWorld;
    }
    
    public void terminate()
    {
      this.setVisible(false);
    }
    
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
      // TODO Auto-generated catch block
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
      
      if (area.x + area.width == r.x && (((r.y >= area.y && r.y <
          area.y + area.height) || (r.y + r.height > area.y && r.y + r.height 
          < area.y + area.height)) || 
          ((area.y >= r.y && area.y <
          r.y + r.height) || (area.y + area.height > r.y && area.y + area.height 
          < r.y + r.height))))
      {
        touches = true;
      }
      
      if (r.x + r.width == area.x && (((r.y >= area.y && r.y <
          area.y + area.height) || (r.y + r.height > area.y && r.y + r.height 
          < area.y + area.height)) || 
          ((area.y >= r.y && area.y <
          r.y + r.height) || (area.y + area.height > r.y && area.y + area.height 
          < r.y + r.height))))
      {
        touches = true;
      }
      
      if (area.y + area.height == r.y && (((r.x >= area.x && r.x
          < area.x + area.width) || (r.x + r.width > area.x && r.x + r.width 
          < area.x + area.width)) || 
          ((area.x >= r.x && area.x <
          r.x + r.width) || (area.x + area.width > r.x && area.x + area.width 
          < r.x + r.width))))
      {
        touches = true;
      }
      
      if (r.y + r.height == area.y && (((r.x >= area.x && r.x
          < area.x + area.width) || (r.x + r.width > area.x && r.x + r.width 
          < area.x + area.width)) || 
          ((area.x >= r.x && area.x <
          r.x + r.width) || (area.x + area.width > r.x && area.x + area.width 
          < r.x + r.width))))
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
      }
      
      if (area.x + area.width == r.x && (((r.y >= area.y && r.y <
          area.y + area.height) || (r.y + r.height > area.y && r.y + r.height 
          < area.y + area.height)) || 
          ((area.y >= r.y && area.y <
          r.y + r.height) || (area.y + area.height > r.y && area.y + area.height 
          < r.y + r.height))))
      {
        touches = true;
      }
      
      if (r.x + r.width == area.x && (((r.y >= area.y && r.y <
          area.y + area.height) || (r.y + r.height > area.y && r.y + r.height 
          < area.y + area.height)) || 
          ((area.y >= r.y && area.y <
          r.y + r.height) || (area.y + area.height > r.y && area.y + area.height 
          < r.y + r.height))))
      {
        touches = true;
      }
      
      if (area.y + area.height == r.y && (((r.x >= area.x && r.x
          < area.x + area.width) || (r.x + r.width > area.x && r.x + r.width 
          < area.x + area.width)) || 
          ((area.x >= r.x && area.x <
          r.x + r.width) || (area.x + area.width > r.x && area.x + area.width 
          < r.x + r.width))))
      {
        touches = true;
      }
      
      if (r.y + r.height == area.y && (((r.x >= area.x && r.x
          < area.x + area.width) || (r.x + r.width > area.x && r.x + r.width 
          < area.x + area.width)) || 
          ((area.x >= r.x && area.x <
          r.x + r.width) || (area.x + area.width > r.x && area.x + area.width 
          < r.x + r.width))))
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
 
  

}