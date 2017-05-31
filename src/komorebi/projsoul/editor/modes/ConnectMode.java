/**
 * ConnectMode.java    Oct 20, 2016, 8:56:15 AM
 */
package komorebi.projsoul.editor.modes;

import static komorebi.projsoul.editor.Buttons.BUTTON_SIZE;
import static komorebi.projsoul.engine.KeyHandler.keyClick;
import static komorebi.projsoul.engine.KeyHandler.keyDown;
import static komorebi.projsoul.engine.MainE.HEIGHT;
import static komorebi.projsoul.engine.MainE.WIDTH;

import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MainE;
import komorebi.projsoul.map.ConnectMap;
import komorebi.projsoul.map.ConnectMap.Side;
import komorebi.projsoul.map.EditorMap;

import org.lwjgl.input.Mouse;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Deals with Map connections, only to be used in the editor
 *
 * @author Aaron Roy
 */
public class ConnectMode extends Mode {

  private ArrayList<ConnectMap> maps;
  private int selected = -1;

  /**
   * Creates a new
   * 
   * @param map The list of maps
   */
  public ConnectMode(ArrayList<ConnectMap> map) {
    maps = map;
  }

  @Override
  public void update() {
    int index;
            
    if((index = getSelectedMap()) != -1 && 
        keyClick(Key.LBUTTON) || keyClick(Key.RBUTTON)){
      selected = index;
    }
    
    if(selected != -1){
      ConnectMap currMap = maps.get(selected);
      
      int offX = mx - pmx + currMap.getTileX();
      int offY = my - pmy + currMap.getTileY();
      
      Mode.status.write("Location: " + currMap.getTileX() + ", " + 
          currMap.getTileY() + ",     Current Map: " + currMap.getName(), 175,1);
      
      if(keyDown(Key.LBUTTON) && (mx != pmx || my != pmy)){
        currMap.setTileLocation(offX, offY);
        EditorMap.setUnsaved();
      }
    }
    
    if((index = getSelectedMap()) != -1 && keyClick(Key.RBUTTON)){
      ConnectMap currMap = maps.get(selected);

      Editor.loadMap(currMap.getName(), currMap.getX(), currMap.getY());
    }
    
    if(lButtonDoubleClicked && index != -1 && index == selected){
      EditSide dialog = new EditSide();
      dialog.pack();
      dialog.setVisible(true);
      KeyHandler.reloadKeyboard();
    }
    
    if(KeyHandler.keyClick(Key.LBUTTON) && checkButtonBounds()){
      switch(Mouse.getX()/(32*MainE.scale)){
        case WIDTH/BUTTON_SIZE-3:
          NewConnectDialog dialog = new NewConnectDialog();
          dialog.pack();
          dialog.setVisible(true);
          EditorMap.setUnsaved();
          KeyHandler.reloadKeyboard();
          break;
        case WIDTH/BUTTON_SIZE-2:
          if(selected != -1){
            maps.remove(selected);
            selected = -1;
            EditorMap.setUnsaved();
          }
          break;
        default:
          //DEBUG Button fail text
          System.out.println("Connect mode button failure pls");
      }
    }
  }

  @Override
  public void render() {
    for(ConnectMap map : maps){
      map.render();
    }

    if(selected != -1){
      ConnectMap map = maps.get(selected);
      //Draw transparent green pixel
      Draw.rect(map.getX(), map.getY(), map.getPxWidth(), map.getPxHeight(), 
          17, 16, 17, 16, 2);
    }

    EditorMap.renderGrid();

    Draw.rect(WIDTH-BUTTON_SIZE*3, HEIGHT-BUTTON_SIZE, 64, 32, 32, 16, 64, 32, 2);

    if(checkButtonBounds()){
      int x = Mouse.getX()/(BUTTON_SIZE*MainE.getScale())*BUTTON_SIZE;
      int y = HEIGHT - BUTTON_SIZE;

      Draw.rect(x, y, BUTTON_SIZE, BUTTON_SIZE, 64, 0, 2);


    }
  }

  private int getSelectedMap(){
    for(int i = 0; i < maps.size(); i++){
      ConnectMap map = maps.get(i);

      if(getMouseX() > (map.getX()-EditorMap.getX())/SIZE &&
          getMouseX() < (map.getX()+map.getPxWidth()-EditorMap.getX())/SIZE &&
          getMouseY() > (map.getY()-EditorMap.getY())/SIZE &&
          getMouseY() < (map.getY()+map.getPxHeight()-EditorMap.getY())/SIZE){
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
        Mouse.getX()/MainE.getScale() < WIDTH-BUTTON_SIZE &&
        Mouse.getY()/MainE.getScale() > HEIGHT-BUTTON_SIZE);
  }

  /**
   * Creates a dialog that allows you to add a map connection
   *
   * @author Aaron Roy
   */
  private class NewConnectDialog extends JDialog implements ActionListener,
                                                      PropertyChangeListener{

    /** Still don't know what this does... */
    private static final long serialVersionUID = 6541401764689709898L;


    private JOptionPane options;
    private String btnCreate = "Create";
    private String btnCancel = "Cancel";

    private Object[] buttons = {btnCreate, btnCancel};

    private JTextField map = new JTextField();
    private JButton open = new JButton("Open...");

    private Box mapBox = Box.createHorizontalBox();
    {
      mapBox.add(new JLabel("Map: "));
      mapBox.add(map);
      mapBox.add(open);
    }
    private JComboBox<Side> side = new JComboBox<Side>(Side.values());
    private Box sideBox = Box.createHorizontalBox();
    {
      sideBox.add(new JLabel("Side: "));
      sideBox.add(side);
    }

    Object[] fullContent = {mapBox, sideBox};

    public NewConnectDialog(){
      super((Frame)null, true);
      setLocationRelativeTo(null);
      setAlwaysOnTop(true);
      setResizable(false);

      setTitle("New Map Connection");

      options = new JOptionPane(fullContent, JOptionPane.PLAIN_MESSAGE, 
          JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);

      addComponentListener(new ComponentAdapter() {
        public void componentShown(ComponentEvent ce){
          map.requestFocusInWindow();
        }
      });

      setContentPane(options);
      options.addPropertyChangeListener(this);

      open.addActionListener(this);
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

        String name = map.getText();

        if(name.substring(name.length()-4, name.length()).equals(".map")){
          name = name.substring(0, name.length()-4);
        }

        String key = "res/maps/"+name+".map";

        if(checkMap(key)){

          float x, y;

          ConnectMap newMap = new ConnectMap(key, name, 
              (Side)side.getSelectedItem());

          switch(newMap.getSide()){
            case DOWN:
              x = EditorMap.getX();
              y = EditorMap.getY() - newMap.getPxHeight();
              break;
            case LEFT:
              x = EditorMap.getX() - newMap.getPxWidth();
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

          newMap.setLoc(x, y);

          maps.add(newMap);

          clearAndHide();
        }else{
          complainNotFound();
        }
      }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if(e.getSource() == open){
        JFileChooser chooser = new JFileChooser("res/maps/");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Map Files (.map)", "map");
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Enter the name of the map to load");
        int returnee = chooser.showOpenDialog(null);

        if(returnee == JFileChooser.APPROVE_OPTION){
          map.setText(chooser.getSelectedFile().getName());
        }

      }
    }


    /** This method clears the dialog and hides it. */
    private void clearAndHide() {
      setVisible(false);
      dispose();
    }

    /**
     * Checks the textbox for issues
     * 
     * @param text The textbox to check
     * @return Whether the textbox is valid or not
     */
    private boolean checkText(JTextField text){
      if(text.getText().contains(" ")){
        complainSpace();
        return false;
      }
      if(text.getText().equals("")){
        complainIncomplete();
        return false;
      }
      return true;
    }

    private boolean checkNum(JTextField text){
      return text.getText().matches("\\d{1,3}");
    }

    private void complainSpace(){
      JOptionPane.showMessageDialog(this, "Sorry, make sure there are no spaces " + 
          "in the names or script names", 
          "Please Try Again", JOptionPane.ERROR_MESSAGE);
    }

    private void complainIncomplete(){
      JOptionPane.showMessageDialog(this, "Sorry, make sure every field is filled in", 
          "Please Try Again", JOptionPane.ERROR_MESSAGE);
    }

    private void complainNotFound(){
      JOptionPane.showMessageDialog(this, "Sorry, the map couldn't be found", 
          "Please Try Again", JOptionPane.ERROR_MESSAGE);
    }


    private boolean checkMap(String text){      
      return new File(text).exists();
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
    
    ConnectMap map = maps.get(selected);

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

        map.setSide((Side)side.getSelectedItem());
        map.setLoc(x, y);

        clearAndHide();
      }
    }

    /** This method clears the dialog and hides it. */
    private void clearAndHide() {
      setVisible(false);
      dispose();
    }

  }


}