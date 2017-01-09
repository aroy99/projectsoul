/**
 * ConnectMode.java    Oct 20, 2016, 8:56:15 AM
 */
package komorebi.projsoul.editor.modes;

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
import komorebi.projsoul.editor.World;
import komorebi.projsoul.editor.World.FindWorldDialog;
import komorebi.projsoul.editor.World.TerminableActionListener;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MainE;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.map.ConnectMap;
import komorebi.projsoul.map.ConnectMap.Side;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.script.Lock;

/**
 * Deals with Map connections, only to be used in the editor
 *
 * @author Aaron Roy
 */
public class ConnectMode extends Mode {

  private World world;
  private static ConnectMap addMap;
  private Rectangle addMapRect = new Rectangle();
  
  private static int selected = -1;
  private boolean hasNoCurrentWorld;

  private static float offX, offY;

  /**
   * Creates a new
   * 
   * @param map The list of maps
   */
  public ConnectMode(World world) {
    this.world = world;
    hasNoCurrentWorld = false;
  }
  
  public ConnectMode() {
    this.world = null;
    hasNoCurrentWorld = true;
  }

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
          FindWorldDialog find = new FindWorldDialog();
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
    
    if (hasNoCurrentWorld)
    {
      FindWorldDialog find = new FindWorldDialog(new Lock());
      world = find.getSelectedWorld();
      find.terminate();
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
    
    if (hasNoCurrentWorld)
    {
      FindWorldDialog find = new FindWorldDialog(new Lock());
      world = find.getSelectedWorld();
      find.terminate();
    }

    for(ConnectMap map : world.getMaps()){
      map.render();
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
          addMap = new ConnectMap("res/maps/" + text.getText());
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
          return arg1.endsWith(".map") && !World.hasWorld("res/maps/"+arg1);
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

  /**
   * Allows the user to edit which side a ConnectMap is on
   *
   * @author Aaron Roy
   */
  private class EditSide extends JDialog implements PropertyChangeListener{

    /** It's a mystery what this does.... */
    private static final long serialVersionUID = 1695317195087331220L;

    private JOptionPane options;
    private String btnCreate = "Create";
    private String btnCancel = "Cancel";

    private Object[] buttons = {btnCreate, btnCancel};

    private JComboBox<Side> side = new JComboBox<Side>(Side.values());
    private Box sideBox = Box.createHorizontalBox();
    {
      sideBox.add(new JLabel("Side: "));
      sideBox.add(side);
    }

    ConnectMap map = world.getMaps().get(selected);

    public EditSide(){
      super((Frame)null, true);
      setLocationRelativeTo(null);
      setAlwaysOnTop(true);
      setResizable(false);

      setTitle("Edit Side");

      side.setSelectedItem(map.getSide());

      options = new JOptionPane(sideBox, JOptionPane.PLAIN_MESSAGE, 
          JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);

      addComponentListener(new ComponentAdapter() {
        public void componentShown(ComponentEvent ce){
          side.requestFocusInWindow();
        }
      });

      setContentPane(options);
      options.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
      String prop = e.getPropertyName();

      if(e.getSource() == options && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
          JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))){
        Object value = options.getValue();

        if(value == JOptionPane.UNINITIALIZED_VALUE){
          //ignore reset
          return;
        }

        //Reset the JOptionPane's value.
        //If you don't do this, then if the user
        //presses the same button next time, no
        //property change event will be fired.
        options.setValue(JOptionPane.UNINITIALIZED_VALUE);

        //DEBUG Affirm Create button works
        System.out.println("Create works");

        float x, y;

        switch((Side)side.getSelectedItem()){
          case DOWN:
            x = EditorMap.getX();
            y = EditorMap.getY() - map.getPxHeight();
            break;
          case LEFT:
            x = EditorMap.getX() - map.getPxWidth();
            y = EditorMap.getY();
            break;
          case RIGHT:
            x = EditorMap.getX() + EditorMap.getPxWidth();
            y = EditorMap.getY();
            break;
          case UP:
            x = EditorMap.getX();
            y = EditorMap.getY() + EditorMap.getPxHeight();
            break;
          default:
            x = 0;
            y = 0;
            break;
        }

        //map.setSide((Side)side.getSelectedItem());
        //map.setLoc(x, y);

        clearAndHide();
      }
    }

    /** This method clears the dialog and hides it. */
    private void clearAndHide() {
      setVisible(false);
      dispose();
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

}