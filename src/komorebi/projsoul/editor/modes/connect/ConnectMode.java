/**
 * ConnectMode.java    Oct 20, 2016, 8:56:15 AM
 */
package komorebi.projsoul.editor.modes.connect;

import static komorebi.projsoul.editor.Buttons.BUTTON_SIZE;
import static komorebi.projsoul.engine.KeyHandler.keyClick;
import static komorebi.projsoul.engine.KeyHandler.keyDown;
import static komorebi.projsoul.engine.MainE.HEIGHT;
import static komorebi.projsoul.engine.MainE.WIDTH;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lwjgl.input.Mouse;

import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.editor.modes.Mode;
import komorebi.projsoul.editor.modes.connect.World.FindWorldDialog;
import komorebi.projsoul.editor.modes.connect.World.TerminableActionListener;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MainE;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.map.ConnectMap;
import komorebi.projsoul.map.ConnectMap.Side;
import komorebi.projsoul.map.EditorMap;

/**
 * Deals with Map connections, only to be used in the editor
 *
 * @author Aaron Roy
 */
public class ConnectMode extends Mode {

  private static World world;
  private static ConnectMap addMap;
  private Rectangle addMapRect = new Rectangle();
  
  private static int selected = -1;

  private static float offX, offY;
  

  @Override
  public void update() {
    int index = getSelectedMap(); //represents the map the mouse is hovering over
    
    if(KeyHandler.keyClick(Key.LBUTTON) && checkButtonBounds()){
      switch(Mouse.getX()/(32*MainE.scale)){
        case WIDTH/BUTTON_SIZE-3:
          NewConnectDialog dialog = new NewConnectDialog();
          EditorMap.setUnsaved();
          KeyHandler.reloadKeyboard();
          break;
        case WIDTH/BUTTON_SIZE-2:
          if(selected != -1){
            world.tryRemove(world.getMaps().get(selected));
            index = selected = -1;
            EditorMap.setUnsaved();
          }
          break;
        case WIDTH/BUTTON_SIZE-1:
          //FindWorldDialog find = new FindWorldDialog();
          break;
        default:
          //DEBUG Button fail text
          System.out.println("Connect mode button failure pls");
      }
    }
    
    
    if (addMap!=null)
    {
      addMapRect.setLocation(getAdjustedMouseX()-addMap.getWidth()/2, 
          getAdjustedMouseY()-addMap.getHeight()/2);
      addMapRect.setSize(addMap.getWidth(), addMap.getHeight());
    }

    if((keyClick(Key.LBUTTON) || keyClick(Key.RBUTTON))) {
      if (addMap == null)
      {
        selected = index;
      } else if (world.mapAtValidLocation(addMapRect))
      {
        addMap.setTileLocation(addMapRect.x, addMapRect.y);        
        world.addMap(addMap);
        world.updateConnections(addMap);
        
        addMap = null;
        addMapRect = null;
      }
    }

    if(selected != -1){
      ConnectMap currMap = world.getMaps().get(selected);

      int offX = mx - pmx + currMap.getTileX();
      int offY = my - pmy + currMap.getTileY();

      Mode.status.write("Location: " + currMap.getTileX() + ", " + 
          currMap.getTileY() + ",     Current Map: " + currMap.getName(), 175,1);

      int presX = currMap.getTileX();
      int presY = currMap.getTileY();
            
      if(keyDown(Key.LBUTTON) && (mx != pmx || my != pmy)) {
        currMap.setTileLocation(offX, offY);
        world.updateConnections(currMap);
        if (!world.allMapsConnected() || !world.mapAtValidLocationIgnoreSelf(
            currMap.getArea(), currMap))
        {
          currMap.setTileLocation(presX, presY);
          world.updateConnections(currMap);
        } else
        {
          EditorMap.setUnsaved();
        }
      }   
    }

    if((index = getSelectedMap()) != -1 && keyClick(Key.RBUTTON)){
      ConnectMap currMap = world.getMaps().get(selected);

      Editor.loadMap(currMap.getName(), currMap.getX(), currMap.getY());
    }

    if(lButtonDoubleClicked && index != -1 && index == selected){
      //EditSide dialog = new EditSide();
      //dialog.pack();
      //dialog.setVisible(true);
      //KeyHandler.reloadKeyboard();
    }
  }

  @Override
  public void render() {    

    if (addMap!=null)
    {
      if (world.mapAtValidLocation(addMapRect))
      {
        Draw.rect((getAdjustedMouseX()-addMap.getWidth()/2)*16+ConnectMode.getX(), 
            (getAdjustedMouseY()-addMap.getHeight()/2)*16+ConnectMode.getY(), addMap.getPxWidth(), 
            addMap.getPxHeight(), 17, 16, 17, 16, 2);
      
      } else
      {
        Draw.rect((getAdjustedMouseX()-addMap.getWidth()/2)*16+ConnectMode.getX(), 
            (getAdjustedMouseY()-addMap.getHeight()/2)*16+ConnectMode.getY(), addMap.getPxWidth(), 
            addMap.getPxHeight(), 32, 0, 33, 1, 2);
      }
    }
    
    if (world != null)
    {
      for(ConnectMap map : world.getMaps()){
        map.render();
      }
    }
    
    if(selected != -1){
      ConnectMap map = world.getMaps().get(selected);

      //Draw transparent green pixel
      Draw.rect(map.getX()+offX, map.getY()+offY, map.getPxWidth(), map.getPxHeight(), 
          17, 16, 17, 16, 2);
    }

    EditorMap.renderGrid();

    Draw.rect(WIDTH-BUTTON_SIZE*3, HEIGHT-BUTTON_SIZE, 64, 32, 32, 16, 64, 32, 2);
    Draw.rect(WIDTH-BUTTON_SIZE, HEIGHT-BUTTON_SIZE, 32, 32, 112, 0, 128, 16, 2);

    if(checkButtonBounds()){
      int x = Mouse.getX()/(BUTTON_SIZE*MainE.getScale())*BUTTON_SIZE;
      int y = HEIGHT - BUTTON_SIZE;

      Draw.rect(x, y, BUTTON_SIZE, BUTTON_SIZE, 64, 0, 2);


    }
  }

  private int getSelectedMap(){    
    for(int i = 0; i < world.getMaps().size(); i++){
      ConnectMap map = world.getMaps().get(i);

      if(getFloatMouseX() > (map.getX()+ConnectMode.getX())&&
          getFloatMouseX() < (map.getX()+map.getPxWidth()+ConnectMode.getX())
          &&
          getFloatMouseY() > (map.getY()+ConnectMode.getY()) &&
          getFloatMouseY() < (map.getY()+map.getPxHeight()+ ConnectMode.getY())){
        return i;
      }
    }
    return -1;
  }

  /**
   * Checks if the Mouse is in bounds of the buttons
   * 
   * @return Mouse is on a button
   */
  private boolean checkButtonBounds() {
    return (Mouse.getX()/MainE.getScale() > WIDTH-BUTTON_SIZE*3 &&
        Mouse.getX()/MainE.getScale() < WIDTH &&
        Mouse.getY()/MainE.getScale() > HEIGHT-BUTTON_SIZE);
  }

  public void move(float dx, float dy)
  {   
    offX += dx;
    offY += dy;
    
    if ((dx!=0 || dy!=0) && selected != -1 && KeyHandler.keyDown(Key.LBUTTON))
    {
      ConnectMap curr = world.getMaps().get(selected);
      Rectangle attempt = new Rectangle(getAdjustedMouseX()-curr.getWidth()/2,
          getAdjustedMouseY()-curr.getHeight()/2, curr.getWidth(), curr.getHeight());
      
      if (world.mapAtValidLocationIgnoreSelf(attempt, curr))
      {        
        curr.setTileLocation(getAdjustedMouseX()-curr.getWidth()/2,
            getAdjustedMouseY()-curr.getHeight()/2);
      }
    }
  }

  /**
   * Creates a dialog that allows you to add a map connection
   *
   * @author Aaron Roy
   */
  private static class NewConnectDialog extends JDialog {

    private static final Dimension FRAME = new Dimension(500, 400);
    private static final Rectangle SEL_BUTTON = new Rectangle(400, 300, 75, 25);
    private static final Rectangle TEXTBOX = new Rectangle(25, 300, 370, 25);
    private static final Rectangle SCROLLER = new Rectangle(25, 25, 450, 250);

    /** Still don't know what this does... */
    private static final long serialVersionUID = 6541401764689709898L;

    private JPanel pane;
    private JButton add;
    private JTextField text;

    private JList<String> fileList;
    private DefaultListModel<String> fileModel;
    private JScrollPane filePane;

    public NewConnectDialog()
    {
      pane = new JPanel(null);
      pane.setSize(FRAME);
      this.setSize(FRAME);

      add = new JButton("Add");
      add.setBounds(SEL_BUTTON);
      add.addActionListener(new TerminableActionListener(this) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
          selected = -1;
          super.actionPerformed(e);
        }
      });

      text = new JTextField();
      text.setBounds(TEXTBOX);

      fileModel = new DefaultListModel<String>();
      fileList = new JList<String>(fileModel);
      filePane = new JScrollPane(fileList);

      fileList.setLayoutOrientation(JList.VERTICAL_WRAP);
      fileList.setVisibleRowCount(-1);
      fileList.setFixedCellWidth(200);
      fileList.addListSelectionListener(new ListSelectionListener() {

        @Override
        public void valueChanged(ListSelectionEvent e) {
          text.setText(fileList.getSelectedValue());
        }
        
      });


      filePane.setBounds(SCROLLER);
      filePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
      filePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

      pane.add(filePane);
      pane.add(text);
      pane.add(add);

      File folder = new File("res/maps/");
      FilenameFilter filter = new FilenameFilter() {

        @Override
        public boolean accept(File arg0, String arg1) {
          return arg1.endsWith(".map") && !World.hasWorldContainingMap("res/maps/"+arg1);
        }
        
      };
          
      
      File[] files = folder.listFiles(filter);

      if (files != null)
      {
        for (File file: files)
        {
          fileModel.addElement(file.getPath().replace("res\\maps\\", ""));
        }
      }

      this.setResizable(false);
      this.setTitle("Add new map");
      this.add(pane);
      this.setVisible(true);
    }

  }

  public static float getX()
  {
    return offX;
  }

  public static float getY()
  {
    return offY;
  }

  private static int getAdjustedMouseX()
  {
    return (int) ((getFloatMouseX() - ConnectMode.getX())/16);
  }
  
  private static int getAdjustedMouseY()
  {
    return (int) ((getFloatMouseY() - ConnectMode.getY())/16);
  }
  
  public boolean saveMyMaps()
  {
    for (ConnectMap c: world.getMaps())
    {
      c.saveConnectionsToFile();
    }
    
    for (ConnectMap c: world.getDeadMaps())
    {
      c.saveConnectionsToFile();
    }
    
    world.getDeadMaps().clear();
    
    return true;
  }
  
  public static void clearSelection()
  {
    selected = -1;
  }
  
  public static void setWorld(World world)
  {
    ConnectMode.world = world;
  }
  
  public boolean hasWorld()
  {
    return world != null;
  }

}