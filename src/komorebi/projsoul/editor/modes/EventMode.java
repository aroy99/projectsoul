/**
 * EventMode.java   Aug 16, 2016, 1:30:31 AM
 */

package komorebi.projsoul.editor.modes;

import static komorebi.projsoul.editor.Buttons.BUTTON_SIZE;
import static komorebi.projsoul.engine.KeyHandler.keyClick;
import static komorebi.projsoul.engine.KeyHandler.keyDown;
import static komorebi.projsoul.engine.MainE.HEIGHT;
import static komorebi.projsoul.engine.MainE.WIDTH;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.lwjgl.input.Mouse;

import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.editor.history.AddEventRevision;
import komorebi.projsoul.editor.history.EventMovedRevision;
import komorebi.projsoul.editor.history.EventRevision;
import komorebi.projsoul.editor.history.RemoveEventRevision;
import komorebi.projsoul.editor.modes.event.AreaScriptEvent;
import komorebi.projsoul.editor.modes.event.EnemyEvent;
import komorebi.projsoul.editor.modes.event.Event;
import komorebi.projsoul.editor.modes.event.NPCEvent;
import komorebi.projsoul.editor.modes.event.SignPostEvent;
import komorebi.projsoul.editor.modes.event.WarpScriptEvent;
import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MainE;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.entities.enemy.EnemyAI;
import komorebi.projsoul.entities.enemy.EnemyType;
import komorebi.projsoul.gameplay.Key;
import komorebi.projsoul.map.EditorMap;

/**
 * The Event Editor
 * 
 * @author Aaron Roy
 */
public class EventMode extends Mode{

  private static ArrayList<NPCEvent> npcs;
  private static ArrayList<AreaScriptEvent> scripts;
  private static ArrayList<WarpScriptEvent> warps;
  private static ArrayList<EnemyEvent> enemies;
  private static ArrayList<SignPostEvent> signs;

  private static Event pre, current;

  private static int selected = -1;
  private EventTypes selectedType;

  private Animation selection;

  private int selX=-16, selY=-16;
  private boolean anEventMoved = false;

  /**
   * The types of events that can be created
   *
   * @author Aaron Roy
   */
  public enum EventTypes{
    NPC, SIGN, ENEMY, SCRIPT, WARP;

    ArrayList<? extends Event> currEvents;

    public ArrayList<? extends Event> getEvents(){
      return currEvents;
    }

    public void setEvents(ArrayList<? extends Event> arr){
      currEvents = arr;
    }
    
    @SuppressWarnings("unchecked")
    public <T> void addEvent(Class<T> classOfEvent, Event event) throws 
      ClassCastException
    {
      
      try {
        ArrayList<T> events = (ArrayList<T>) currEvents;
        events.add(classOfEvent.cast(event));
      } catch (Exception e)
      {
        throw new ClassCastException("Incompatible type");
      }
      
    }
    
    public void removeEvent(Event event)
    {
      currEvents.remove(event);
      while (selected >= currEvents.size())
      {
        selected--;
      }
    }

    public String toString(){
      switch (this) {
        case NPC:    return "NPC";
        case ENEMY:  return "Enemy";
        case SCRIPT: return "Script";
        case SIGN:   return "Sign";
        case WARP:   return "Warp";
        default:     return "bleh";
      }
    }
  }

  /**
   * Creates an NPC Mode
   * 
   * @param npcs The list of NPCs
   * @param scripts The list of Scripts
   */
  public EventMode(ArrayList<NPCEvent> npcs, ArrayList<AreaScriptEvent> scripts, 
      ArrayList<WarpScriptEvent> warps, ArrayList<EnemyEvent> enemies,
      ArrayList<SignPostEvent> signs) {
    EventMode.npcs = npcs;
    EventMode.scripts = scripts;
    EventMode.warps = warps;
    EventMode.enemies = enemies;
    EventMode.signs = signs;

    EventTypes.NPC.setEvents(npcs);
    EventTypes.SCRIPT.setEvents(scripts);
    EventTypes.WARP.setEvents(scripts);
    EventTypes.ENEMY.setEvents(enemies);
    EventTypes.SIGN.setEvents(signs);

    selection = new Animation(8, 8, 16, 16, 2);
    for(int i=3; i >= 0; i--){
      selection.add(0 , 0 , i);
      selection.add(16, 0 , i);
    }
  }

  @Override
  public void update() {
    int index;

    if(keyClick(Key.LBUTTON)){
      if((index = getSelectedEvent(npcs)) != -1){
        selectedType = EventTypes.NPC;
      }

      else if((index = getSelectedEvent(scripts)) != -1){
        selectedType = EventTypes.SCRIPT;
      }

      else if((index = getSelectedEvent(warps)) != -1){
        selectedType = EventTypes.WARP;
      }

      else if((index = getSelectedEvent(enemies)) != -1){
        selectedType = EventTypes.ENEMY;
      }

      else if((index = getSelectedEvent(signs)) != -1){
        selectedType = EventTypes.SIGN;
      }

      if(selectedType != null && index != -1){
        selX = selectedType.getEvents().get(index).getTileX();
        selY = selectedType.getEvents().get(index).getTileY();
        selected = index;

        current = selectedType.getEvents().get(index);
      }
    }

    if(keyDown(Key.LBUTTON) && !mouseSame){
      if(selectedType != null && mouseInEventBounds(selectedType.getEvents())){

        if (getMouseX() != selectedType.getEvents().get(selected).getTileX()
            || getMouseY() != selectedType.getEvents().get(selected).getTileY())
        {
          anEventMoved = true;
          selectedType.getEvents().get(selected).setTileLocation(getMouseX(), getMouseY());
        }

        EditorMap.setUnsaved();
      }
    }

    if (KeyHandler.keyRelease(Key.LBUTTON) && anEventMoved)
    {
      Editor.getMap().addRevision(new EventMovedRevision(current, pre.getTileX(),
          pre.getTileY(), current.getTileX(), current.getTileY()));
      pre = current.duplicate();

      anEventMoved = false;
    }


    if(lButtonDoubleClicked && selectedType != null && 
        mouseInEventBounds(selectedType.getEvents())){

      EditEventDialog dialog = new EditEventDialog(selectedType);
      dialog.pack();
      dialog.setVisible(true);
      EditorMap.setUnsaved();
    }

    if(KeyHandler.keyClick(Key.LBUTTON) && checkButtonBounds()){

      switch(Mouse.getX()/(32*MainE.scale)){
        case WIDTH/BUTTON_SIZE-3:
          NewEventDialog dialog = new NewEventDialog();
          dialog.pack();
          dialog.setVisible(true);
          EditorMap.setUnsaved();
          break;
        case WIDTH/BUTTON_SIZE-2:
          deleteSelectedEvent();
          break;
        default:
          //DEBUG Button fail text
          System.out.println("Event mode button failure pls");
      }
    }
    
    if(selected != -1){
      selX = selectedType.getEvents().get(selected).getTileX();
      selY = selectedType.getEvents().get(selected).getTileY();
    }

  }

  private void deleteSelectedEvent()
  {    
    if(selected != -1){
      Editor.getMap().addRevision(new RemoveEventRevision(
          selectedType.getEvents().get(selected)));
      selectedType.getEvents().remove(selected);
      selectedType = null;
      selected = -1;
      selX = -16;
      selY = -16;
      EditorMap.setUnsaved();
    }
  }

  @Override
  public void render() {
    for (NPCEvent npc: npcs) {
      if(EditorMap.checkTileInBounds(npc.getX(), npc.getY())){
        npc.renderE();
      }
    }

    for (AreaScriptEvent script: scripts) {
      if(EditorMap.checkTileInBounds(script.getX(), script.getY())){
        script.renderE();
      }
    }

    for(EnemyEvent enemy: enemies){
      if(EditorMap.checkTileInBounds(enemy.getX(), enemy.getY())){
        enemy.renderE();
      }
    }

    for(SignPostEvent sign: signs){
      if(EditorMap.checkTileInBounds(sign.getX(), sign.getY())){
        sign.renderE();
      }
    }

    EditorMap.renderGrid();

    if(EditorMap.checkTileInBounds(selX, selY) && selected != -1){
      selection.playZoom(selX*16+EditorMap.getX(), selY*16+EditorMap.getY());
    }


    Draw.rect(WIDTH-BUTTON_SIZE*3, HEIGHT-BUTTON_SIZE, 64, 32, 32, 16, 64, 32, 2);

    if(checkButtonBounds()){
      int x = Mouse.getX()/(BUTTON_SIZE*MainE.getScale())*BUTTON_SIZE;
      int y = HEIGHT - BUTTON_SIZE;

      Draw.rect(x, y, BUTTON_SIZE, BUTTON_SIZE, 64, 0, 2);
    }


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
   * Looks for an event in the specified array that is at the 
   * same position as the mouse
   * 
   * @param events The event array to check
   * @return the Event id if found, -1 if not
   */
  public int getSelectedEvent(ArrayList<? extends Event> events){
    for(int i = 0; i < events.size(); i++){
      Event edit = events.get(i);

      if(getMouseX() == edit.getTileX() && 
          getMouseY() == edit.getTileY()){
        return i;
      }
    }

    return -1;
  }

  /**
   * Checks if the mouse is in bounds of an Event
   * 
   * @return The mouse is in bounds
   */
  public boolean mouseInEventBounds(ArrayList<? extends Event> events){
    if(selected == -1){
      return false;
    }

    if(pmx == events.get(selected).getTileX() &&
        pmy == events.get(selected).getTileY()){
      return true;
    }

    return false;
  }


  /**
   * An abstract class to contain all of the shared variables between the New 
   * and Edit dialogs
   *
   * @author Aaron Roy
   */
  private abstract class EventDialog extends JDialog implements ActionListener,
  PropertyChangeListener{

    /** Still don't know what this does... */
    private static final long serialVersionUID = 6541401764689709898L;

    /** The choices someone has with the editor */
    protected EventTypes type = EventTypes.NPC;

    protected JOptionPane options;
    protected String btnCreate = "Create";
    protected String btnCancel = "Cancel";

    protected Object[] buttons = {btnCreate, btnCancel};

    //NPC Items
    protected NPCType[] npcPicArr = NPCType.values();
    protected JComboBox<NPCType> npcPics = new JComboBox<NPCType>(npcPicArr);
    protected JTextField name = new JTextField(8);
    protected JTextField walking = new JTextField(15);
    protected JButton editWalkScript = new JButton("...");
    protected JTextField talking = new JTextField(15);
    protected JButton editTalkScript = new JButton("...");

    protected Box talkBox, walkBox;

    {
      walkBox = Box.createHorizontalBox();
      walkBox.add(walking);
      walkBox.add(editWalkScript);

      talkBox = Box.createHorizontalBox();
      talkBox.add(talking);               
      talkBox.add(editTalkScript);        

    }

    protected Component[] npcContents = {new JLabel("Picture: "), npcPics, 
        Box.createHorizontalStrut(15),
        new JLabel("Name: "), name,
        new JLabel("Walking Script: "), walkBox,
        new JLabel("Talking Script: "), talkBox
    };

    //Sign Items
    protected JTextField signID = new JTextField(15);
    protected Component[] signContents = {new JLabel("Message: "), signID};


    //Enemy Items
    protected String[] enemyPicArr = EnemyType.valuesAsString();
    protected JComboBox<String> enemyPics = new JComboBox<String>(enemyPicArr);
    protected String[] aiTypesArr = EnemyAI.valuesAsString();
    protected JComboBox<String> aiType = new JComboBox<String>(aiTypesArr);
    protected JTextField radius = new JTextField();

    protected Component[] enemyContents = {new JLabel("Picture: "), enemyPics, 
        Box.createHorizontalStrut(15),
        new JLabel("AI Type: "), aiType, new JLabel("Radius"), radius
    };


    //Script Items
    protected JTextField scriptID = new JTextField(15);
    protected JButton editScriptText = new JButton("...");
    protected JCheckBox repeatable = new JCheckBox("Repeatable: ");
    protected ArrayList<NPCEvent> npcArr = new ArrayList<NPCEvent>();
    protected JComboBox<NPCEvent> npcsCombo;

    protected Box textBox;

    {
      npcArr.add(null);
      npcArr.addAll(npcs);
      npcsCombo = new JComboBox<NPCEvent>(npcArr.toArray(new NPCEvent[] {}));

      textBox = Box.createHorizontalBox();
      textBox.add(scriptID);
      textBox.add(editScriptText);
    }
    protected Component[] scriptContents = {new JLabel("Script: "), textBox,
        repeatable,
        new JLabel("NPC (if applicable)"), npcsCombo};

    //Warp contents
    protected JTextField newMap = new JTextField(10);
    protected JTextField warpX = new JTextField(3);
    protected JTextField warpY = new JTextField(3);
    protected Component[] warpContents = {new JLabel("New Map"), newMap,
        new JLabel("New X: "), warpX, new JLabel("New Y: "), warpY};

    public EventDialog(){
      super((Frame)null, true);
      setLocationRelativeTo(null);
      setAlwaysOnTop(true);
      setResizable(false);
    }

    /**
     * Hides all of the other components
     * 
     * @param e The EventType to keep
     */
    protected void hideEverything(EventTypes e){
      if(e != EventTypes.NPC){
        for(Component c: npcContents){
          c.setVisible(false);
        }
      }
      if(e != EventTypes.SIGN){
        for(Component c: signContents){
          c.setVisible(false);
        }
      }
      if(e != EventTypes.ENEMY){
        for(Component c: enemyContents){
          c.setVisible(false);
        }
      }
      if(e != EventTypes.SCRIPT){
        for(Component c: (Component[])scriptContents){
          c.setVisible(false);
        }
      }
      if(e != EventTypes.WARP){
        for(Component c: (Component[])warpContents){
          c.setVisible(false);
        }
      }
    }

    /** This method clears the dialog and hides it. */
    protected void clearAndHide() {
      name.setText(null);
      walking.setText(null);
      talking.setText(null);

      scriptID.setText(null);

      newMap.setText(null);

      setVisible(false);
      dispose();
    }

    /**
     * Checks the textbox for issues
     * 
     * @param text The textbox to check
     * @return Whether the textbox is valid or not
     */
    protected boolean checkText(JTextField text){
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

    protected boolean checkNum(JTextField text){
      return text.getText().matches("\\d{1,3}");
    }

    protected void complainSpace(){
      JOptionPane.showMessageDialog(this, "Sorry, make sure there are no spaces " + 
          "in the names or script names", 
          "Please Try Again", JOptionPane.ERROR_MESSAGE);
    }

    protected void complainIncomplete(){
      JOptionPane.showMessageDialog(this, "Sorry, make sure every field is filled in", 
          "Please Try Again", JOptionPane.ERROR_MESSAGE);
    }

    protected void editScript(JTextField script){
      if(Desktop.isDesktopSupported()){
        File file = new File("res/scripts/"+script.getText()+".txt");
        try{
          //DEBUG Editing scripts
          System.out.println("Trying to launch " + script.getText() + "...");
          Desktop.getDesktop().edit(file);
        }catch (IllegalArgumentException ex) {
          ex.printStackTrace();
          System.out.println("bleh");
          int answer = JOptionPane.showConfirmDialog(this, "File Not Found, would you like to" + 
              "create a new file with this name?",
              "Error", JOptionPane.ERROR_MESSAGE);

          //Create new file
          if(answer == JOptionPane.OK_OPTION){
            file.getParentFile().mkdirs();

            try {
              file.createNewFile();
              Desktop.getDesktop().edit(file);
            } catch (IOException e) {
              e.printStackTrace();
              JOptionPane.showMessageDialog(this, "File creation failed...", 
                  "RIP", JOptionPane.ERROR_MESSAGE);
            }
          }
        }catch(IOException e){
          JOptionPane.showMessageDialog(this, "The file couldn't be opened", 
              "RIP", JOptionPane.ERROR_MESSAGE);
        }
      }else{
        JOptionPane.showMessageDialog(this, "Your OS doesn't support this", 
            "RIP", JOptionPane.ERROR_MESSAGE);

      }

    }

  }

  /**
   * Creates a new event dialog and lets the user create whatever they want
   *
   * @author Aaron Roy
   */
  private class NewEventDialog extends EventDialog{

    /** Still don't know what this does... */
    private static final long serialVersionUID = -2767171698655162628L;

    JComboBox<EventTypes> types = new JComboBox<EventTypes>();
    {
      for(EventTypes e: EventTypes.values()){
        types.addItem(e);
      }
    }

    Object[] fullContent = {types, Box.createHorizontalStrut(15),
        npcContents,signContents, enemyContents, scriptContents, warpContents};

    /**
     * Creates a dialog to create a new event
     */
    public NewEventDialog() {
      super();
      setTitle("New Event");

      //Special scripting init

      options = new JOptionPane(fullContent, JOptionPane.PLAIN_MESSAGE,
          JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);

      addComponentListener(new ComponentAdapter() {
        public void componentShown(ComponentEvent ce) {
          types.requestFocusInWindow();
        }
      });


      setContentPane(options);

      options.addPropertyChangeListener(this);

      types.addActionListener(this);

      editTalkScript.addActionListener(this);
      editWalkScript.addActionListener(this);

      editScriptText.addActionListener(this);

      hideEverything(EventTypes.NPC);
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

        if(btnCreate.equals(value)){
          Event event = null;
          
          float x = EditorMap.getX();
          float y = EditorMap.getY();

          switch((EventTypes)types.getSelectedItem()){
            case NPC:
              if(checkText(name) && checkText(walking) && checkText(talking)){
                NPCEvent newNPC = new NPCEvent((NPCType) 
                    npcPics.getSelectedItem(), name.getText(), walking.getText(),
                    talking.getText());
                newNPC.setTileLocation(0,0);
                npcs.add(newNPC);
                selX = 0;
                selY = 0;
                
                event = newNPC;

                pre = newNPC.duplicate();
                clearAndHide();
              }
              break;
            case SIGN:
              SignPostEvent newSign = new SignPostEvent(signID.getText());
              newSign.setTileLocation(0, 0);
              signs.add(newSign);
              selX = 0;
              selY = 0;
              
              event = newSign;

              pre = newSign.duplicate();
              clearAndHide();
              break;
            case ENEMY:
              EnemyEvent newEnemy;

              if(checkNum(radius)){
                newEnemy = new EnemyEvent(EnemyType.toEnum(
                    (String) enemyPics.getSelectedItem()), EnemyAI.toEnum(
                        (String) aiType.getSelectedItem()), Integer.parseInt(
                            radius.getText()));
                enemies.add(newEnemy);
                newEnemy.setTileLocation(0, 0);
                selX = 0;
                selY = 0;
                
                event = newEnemy;

                pre = newEnemy.duplicate();
                clearAndHide();
              }

              break;
            case SCRIPT:
              if(checkText(scriptID)){
                AreaScriptEvent newScript;
                if(npcsCombo.getSelectedItem() == null){
                  newScript = new AreaScriptEvent(scriptID.getText(),
                      repeatable.isSelected(), "");
                }else{
                  newScript = new AreaScriptEvent(scriptID.getText(),
                      repeatable.isSelected(), 
                      ((NPCEvent) npcsCombo.getSelectedItem()).getName());
                }
                newScript.setTileLocation(0, 0);
                scripts.add(newScript);
                selX = 0;
                selY = 0;
                
                event = newScript;

                pre = newScript.duplicate();
                clearAndHide();
              }
              break;
            case WARP:
              if(checkText(newMap) && checkNum(warpX) && checkNum(warpY)){
                WarpScriptEvent newWarp = new WarpScriptEvent(
                    newMap.getName(), Integer.parseInt(warpX.getText()),
                    Integer.parseInt(warpY.getText()));
                newWarp.setTileLocation(0, 0);
                warps.add(newWarp);
                selX = 0;
                selY = 0;
                
                event = newWarp;

                pre = newWarp.duplicate();
                clearAndHide();
              }
              break;

            default:
              //DEBUG New event case fail
              System.out.println("How does this even happen?? (New Event fail)");

          }
        
          Editor.getMap().addRevision(new AddEventRevision(event));
          
        }else{
          clearAndHide();
        }
      }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if(e.getSource() == types){

        EventTypes type = (EventTypes)types.getSelectedItem();

        hideEverything(type);

        switch(type){
          case NPC:
            for(Component c: npcContents){
              c.setVisible(true);
            }
            break;
          case SIGN:
            for(Component c: signContents){
              c.setVisible(true);
            }
            break;
          case ENEMY:
            for(Component c: enemyContents){
              c.setVisible(true);
            }
            break;
          case SCRIPT:
            for(Component c: scriptContents){
              c.setVisible(true);
            }
            break;
          case WARP:
            for(Component c: warpContents){
              c.setVisible(true);
            }
            break;

          default:
            //DEBUG Case fail New event
            System.out.println("How??? (New Event Case Fail2)");
        }
      }
      if(e.getSource() == options){
        options.setValue(btnCreate);
      }

      if(e.getSource() == editTalkScript && checkText(talking)){
        editScript(talking);
      }

      if(e.getSource() == editWalkScript && checkText(walking)){
        editScript(walking);
      }

      if(e.getSource() == editScriptText && checkText(scriptID)){
        editScript(scriptID);
      }

    }
  }

  /**
   * Lets the user edit any event
   *
   * @author Aaron Roy
   */
  private class EditEventDialog extends EventDialog{


    /** Still don't know what this does... */
    private static final long serialVersionUID = -8499985542863770713L;

    /**
     * Creates a new dialog to edit events
     * 
     * @param e the type of event being edited
     */
    public EditEventDialog(EventTypes e) {
      super();
      setTitle("Edit Event");

      type = e;


      switch(type){
        case ENEMY:

          EnemyEvent editEnemy = enemies.get(selected);

          enemyPics.setSelectedItem(editEnemy.getSpriteType());
          aiType.setSelectedItem(editEnemy.getAIType());
          radius.setText(""+editEnemy.getRadius());


          options = new JOptionPane(enemyContents, JOptionPane.PLAIN_MESSAGE,
              JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);
          addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
              enemyPics.requestFocusInWindow();
            }
          });

          break;
        case NPC:
          options = new JOptionPane(npcContents, JOptionPane.PLAIN_MESSAGE,
              JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);
          addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
              npcPics.requestFocusInWindow();
            }
          });

          NPCEvent editNPC = npcs.get(selected);

          npcPics.setSelectedItem(editNPC.getSpriteType());

          name.setText(editNPC.getName());
          walking.setText(editNPC.getWalkScript());
          talking.setText(editNPC.getTalkScript());

          break;
        case SCRIPT:
          options = new JOptionPane(scriptContents, JOptionPane.PLAIN_MESSAGE,
              JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);
          addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
              scriptID.requestFocusInWindow();
            }
          });

          AreaScriptEvent editScript = scripts.get(selected);
          scriptID.setText(editScript.getScript());
          repeatable.setSelected(editScript.isRepeatable());
          npcsCombo.setSelectedItem(editScript.getNPC());

          break;
        case SIGN:
          options = new JOptionPane(signContents, JOptionPane.PLAIN_MESSAGE,
              JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);
          addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
              signID.requestFocusInWindow();
            }
          });

          SignPostEvent editSign = signs.get(selected);
          signID.setText(editSign.getText());

          break;
        case WARP:
          options = new JOptionPane(warpContents, JOptionPane.PLAIN_MESSAGE,
              JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);
          addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
              newMap.requestFocusInWindow();
            }
          });

          WarpScriptEvent editWarp = warps.get(selected);
          newMap.setText(editWarp.getMap());
          warpX.setText(""+editWarp.getWarpToX());
          warpY.setText(""+editWarp.getWarpToY());

          break;
        default:
          break;

      }

      setContentPane(options);

      options.addPropertyChangeListener(this);

      editTalkScript.addActionListener(this);
      editWalkScript.addActionListener(this);

      editScriptText.addActionListener(this);


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

        System.out.println("Create works");

        if(btnCreate.equals(value)){

          Event beforeOK;

          switch(type){
            case NPC:

              if(checkText(name) && checkText(walking) && checkText(talking)){
                NPCEvent editNPC = npcs.get(selected);
                beforeOK = editNPC.duplicate();

                editNPC.setName(name.getText());
                editNPC.setSpriteType((NPCType) npcPics.getSelectedItem());
                editNPC.setWalkScript(walking.getText());
                editNPC.setTalkScript(talking.getText());

                Editor.getMap().addRevision(new EventRevision(editNPC, (NPCEvent) beforeOK, 
                    editNPC));

                clearAndHide();
              }
              break;
            case SIGN:
              SignPostEvent editSign = signs.get(selected);
              beforeOK = editSign.duplicate();

              editSign.setText(signID.getText());

              Editor.getMap().addRevision(new EventRevision(editSign, (SignPostEvent) beforeOK, 
                  editSign));
              clearAndHide();

              break;
            case ENEMY:

              if(checkNum(radius)){

                EnemyEvent editEnemy = enemies.get(selected);
                beforeOK = editEnemy.duplicate();

                editEnemy.setAIType(
                    EnemyAI.toEnum((String) aiType.getSelectedItem()));
                editEnemy.setSpriteType(
                    EnemyType.toEnum((String) enemyPics.getSelectedItem()));
                editEnemy.setRadius(Integer.parseInt(radius.getText()));

                Editor.getMap().addRevision(new EventRevision(enemies.get(selected),
                    (EnemyEvent) beforeOK, enemies.get(selected)));
              }

              break;
            case SCRIPT:
              if(checkText(scriptID)){
                AreaScriptEvent editScript = scripts.get(selected);
                beforeOK = editScript.duplicate();

                if(npcsCombo.getSelectedItem() != null){
                  editScript.setNPC(((NPCEvent) 
                      (npcsCombo.getSelectedItem())).getName());
                } else
                {
                  editScript.setNPC("");
                }
                editScript.setScript(scriptID.getText());
                editScript.setIsRepeatable(repeatable.isSelected());

                Editor.getMap().addRevision(new EventRevision(editScript, (AreaScriptEvent) 
                    beforeOK, editScript));

                clearAndHide();
              }
              break;
            case WARP:
              if(checkText(newMap) && checkNum(warpX) && checkNum(warpY)){
                WarpScriptEvent editWarp = warps.get(selected);
                beforeOK = editWarp.duplicate();

                editWarp.setMap(newMap.getText());
                editWarp.setWarpToX(Integer.parseInt(warpX.getText()));
                editWarp.setWarpToY(Integer.parseInt(warpY.getText()));

                Editor.getMap().addRevision(new EventRevision(editWarp, (WarpScriptEvent) 
                    beforeOK, editWarp));

                clearAndHide();
              }
              break;

            default:
              //DEBUG Edit event case fail
              System.out.println("How does this even happen?? (Edit event)");

          }
        }else{
          clearAndHide();
        }

        clearAndHide();
      }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if(e.getSource() == options){
        options.setValue(btnCreate);
      }

      if(e.getSource() == editTalkScript && checkText(talking)){
        editScript(talking);
      }

      if(e.getSource() == editWalkScript && checkText(walking)){
        editScript(walking);
      }

      if(e.getSource() == editScriptText && checkText(scriptID)){
        editScript(scriptID);
      }


    }

  }


}