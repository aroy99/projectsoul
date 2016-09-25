/**
 * NPCMode.java   Aug 16, 2016, 1:30:31 AM
 */

package komorebi.projsoul.editor.modes;

import static komorebi.projsoul.editor.Buttons.BUTTON_SIZE;
import static komorebi.projsoul.engine.KeyHandler.keyClick;
import static komorebi.projsoul.engine.KeyHandler.keyDown;
import static komorebi.projsoul.engine.MainE.HEIGHT;
import static komorebi.projsoul.engine.MainE.WIDTH;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.MainE;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.script.AreaScript;
import komorebi.projsoul.script.TalkingScript;
import komorebi.projsoul.script.WalkingScript;
import komorebi.projsoul.script.WarpScript;

import org.lwjgl.input.Mouse;

import java.awt.Button;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Area;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.function.ObjDoubleConsumer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * The NPC Editor
 * 
 * @author Aaron Roy
 */
public class NPCMode extends Mode{

  private ArrayList<NPC> npcs;
  private ArrayList<AreaScript> scripts;

  private int selected = -1;
  private boolean isNpc;

  private Animation selection;

  private float selX=-16, selY=-16;

  /**
   * The types of events that can be created
   *
   * @author Aaron Roy
   */
  enum EventTypes{
    NPC, SIGN, ENEMY, SCRIPT, WARP;

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
  public NPCMode(ArrayList<NPC> npcs, ArrayList<AreaScript> scripts) {
    this.npcs = npcs;
    this.scripts = scripts;

    selection = new Animation(8, 8, 16, 16, 2);
    for(int i=3; i >= 0; i--){
      selection.add(0 , 0 , i);
      selection.add(16, 0 , i);
    }
  }

  @Override
  public void update() {
    int index;
    //TODO Debug
    if(keyClick(Key.LBUTTON)){
      if((index = mouseInNPCBounds()) != -1){
        selX = npcs.get(index).getX();
        selY = npcs.get(index).getY();
        selected = index;
        isNpc = true;
      }

      else if((index = mouseInScriptBounds()) != -1){
        selX = scripts.get(index).getX();
        selY = scripts.get(index).getY();
        selected = index;
        isNpc = false;
      }
      System.out.println("X: " + getMouseX() + ", Y: " + getMouseY());
    }

    if(keyDown(Key.LBUTTON) && isNpc && mouseInSelectedNPCBounds(selected) &&
        (mx != pmx || my != pmy)){
      npcs.get(selected).setTileLocation(getMouseX(), getMouseY());
      EditorMap.setUnsaved();
    }

    if(keyDown(Key.LBUTTON) && !isNpc && mouseInSelectedScriptBounds(selected) &&
        (mx != pmx || my != pmy)){
      scripts.get(selected).setTileLocation(getMouseX(), getMouseY());
      EditorMap.setUnsaved();
    }

    if(lButtonDoubleClicked && isNpc && mouseInSelectedNPCBounds(selected)){
      //TODO Edit NPC Dialog
      EditEventDialog dialog = new EditEventDialog(EventTypes.NPC);
      dialog.pack();
      dialog.setVisible(true);
    }else if(lButtonDoubleClicked && !isNpc && mouseInSelectedScriptBounds(selected)){
      EditEventDialog dialog;
      if(scripts.get(selected) instanceof WarpScript){
        dialog = new EditEventDialog(EventTypes.WARP);
      }else{
        dialog = new EditEventDialog(EventTypes.SCRIPT);
      }     
      dialog.pack();
      dialog.setVisible(true);

    }

    if(KeyHandler.keyClick(Key.LBUTTON) && checkButtonBounds()){
      switch(Mouse.getX()/(32*MainE.scale)){
        case 22:
          NewEventDialog dialog = new NewEventDialog();
          dialog.pack();
          dialog.setVisible(true);
          //TODO Debug
          System.out.println("Created Dialog");
          break;
        case 23:
          //TODO Remove NPC
          break;
        default:
          //TODO Debug
          System.out.println("This shouldn't be happening m9");
      }
    }



    if(selected != -1){
      if(isNpc){
        selX = npcs.get(selected).getX();
        selY = npcs.get(selected).getY();
      }else{
        selX = scripts.get(selected).getX();
        selY = scripts.get(selected).getY();
      }
    }

  }


  @Override
  public void render() {
    if(EditorMap.checkTileInBounds(selX, selY)){
      selection.play(selX, selY);
    }

    Draw.rect(WIDTH-SIZE*6, HEIGHT-SIZE*2, 64, 32, 32, 16, 64, 32, 2);

    if(checkButtonBounds()){
      int x = Mouse.getX()/(BUTTON_SIZE*MainE.getScale())*BUTTON_SIZE;
      int y = HEIGHT - BUTTON_SIZE;

      Draw.rect(x, y, BUTTON_SIZE, BUTTON_SIZE, 64, 0, 2);


    }

  }

  /**
   * Checks if the Mouse is in bounds of the buttons
   * @return Mouse is on a button
   */
  private boolean checkButtonBounds() {
    return (Mouse.getX()/MainE.getScale() > WIDTH-BUTTON_SIZE*3 &&
        Mouse.getX()/MainE.getScale() < WIDTH-BUTTON_SIZE &&
        Mouse.getY()/MainE.getScale() > HEIGHT-BUTTON_SIZE);
  }


  /**
   * Checks if the mouse is in bounds of an NPC
   * 
   * @return the NPC id if found, -1 if not
   */
  public int mouseInNPCBounds(){
    for(int i = 0; i < npcs.size(); i++){
      NPC npc = npcs.get(i);

      if(getMouseX() == npc.getOrigTX() && 
          getMouseY() == npc.getOrigTY()){
        return i;
      }
    }

    return -1;
  }

  /**
   * Checks if the mouse is in bounds of an NPC
   * 
   * @return The mouse is in bounds
   */
  public boolean mouseInSelectedNPCBounds(int index){
    if(selected == -1){
      return false;
    }

    if(pmx == npcs.get(selected).getOrigTX() &&
        pmy == npcs.get(selected).getOrigTY()){
      return true;
    }

    return false;
  }

  /**
   * Checks if the mouse is in bounds of an Script
   * 
   * @return The mouse is in bounds
   */
  public boolean mouseInSelectedScriptBounds(int index){
    //TODO Debug
    if(selected == -1){
      return false;
    }

    if(pmx == scripts.get(selected).getOrigTX() &&
        pmy == scripts.get(selected).getOrigTY()){
      return true;
    }    
    return false;
  }



  /**
   * Checks if the mouse is in bounds of an AreaScript
   * 
   * @return the AreaScript id if found, -1 if not
   */
  public int mouseInScriptBounds(){
    for(int i = 0; i < scripts.size(); i++){
      AreaScript scr = scripts.get(i);

      if(getMouseX() == scr.getOrigTX() && 
          getMouseY() == scr.getOrigTY()){
        return i;
      }
    }

    return -1;
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
    protected String[] npcPicArr = {"Ness", "Ash",};
    protected JComboBox<String> npcPics = new JComboBox<String>(npcPicArr);
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
    protected Component[] signContents = {new JLabel("Script: "), signID};


    //Enemy Items
    protected String[] enemyPicArr = {"Mr. Saturn"};
    protected JComboBox<String> enemyPics = new JComboBox<String>(enemyPicArr);
    protected String[] aiTypesArr = {"Radius"};
    protected JComboBox<String> aiType = new JComboBox<String>(aiTypesArr);

    protected Component[] enemyContents = {new JLabel("Picture: "), enemyPics, 
        Box.createHorizontalStrut(15),
        new JLabel("AI Type: "), aiType
    };


    //Script Items
    protected JTextField scriptID = new JTextField(15);
    protected JButton editScriptText = new JButton("...");
    protected JCheckBox repeatable = new JCheckBox("Repeatable: ");
    protected ArrayList<NPC> npcArr = new ArrayList<NPC>();
    protected JComboBox<NPC> npcsCombo;

    protected Box textBox;

    {
      npcArr.add(null);
      npcArr.addAll(npcs);
      npcsCombo = new JComboBox<NPC>(npcArr.toArray(new NPC[] {}));

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
     */
    protected void hideEverything(EventTypes e){
      //TODO Debug
      System.out.println(e);

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
          //TODO Debug
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


      for(Component c: signContents){
        c.setVisible(false);
      }

      for(Component c: enemyContents){
        c.setVisible(false);
      }
      for(Component c: (Component[])scriptContents){
        c.setVisible(false);
      }
      for(Component c: (Component[])warpContents){
        c.setVisible(false);
      }

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
          float x = EditorMap.getX();
          float y = EditorMap.getY();

          switch((EventTypes)types.getSelectedItem()){
            case NPC:
              if(checkText(name) && checkText(walking) && checkText(talking)){
                NPC newNPC = new NPC(name.getText(), x, y, 
                    NPCType.toEnum((String)npcPics.getSelectedItem()));
                newNPC.setWalkingScript(new WalkingScript(walking.getText(), newNPC));
                newNPC.setTalkingScript(new TalkingScript(talking.getText(), newNPC));
                npcs.add(newNPC);
                clearAndHide();
              }
              break;
            case SIGN:
              if(checkText(signID)){
                //TODO Implement signs
              }
              break;
            case ENEMY:
              //TODO Implement Enemies
              break;
            case SCRIPT:
              if(checkText(scriptID)){
                AreaScript newScript;
                if(npcsCombo.getSelectedItem() == null){
                  newScript = new AreaScript(scriptID.getText(), x, y, false);
                }else{
                  newScript = new AreaScript(scriptID.getText(), x, y, false, 
                      npcs.get(npcsCombo.getSelectedIndex()));
                }
                newScript.setRepeatable(repeatable.isSelected());

                scripts.add(newScript);
                clearAndHide();
              }
              break;
            case WARP:
              if(checkText(newMap)){
                //TODO Add warping to a specific location

                WarpScript newWarp = new WarpScript(newMap.getName(), x, y, true);
                scripts.add(newWarp);
                clearAndHide();
              }
              break;

            default:
              //TODO Debug
              System.out.println("How does this even happen??");

          }
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

        //TODO Debug
        System.out.println("For the top combobox");

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
            //TODO Debug
            System.out.println("How does this even happen??");
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
          options = new JOptionPane(enemyContents, JOptionPane.PLAIN_MESSAGE,
              JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);
          addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
              enemyPics.requestFocusInWindow();
            }
          });

          //TODO Implement Enemy init

          break;
        case NPC:
          options = new JOptionPane(npcContents, JOptionPane.PLAIN_MESSAGE,
              JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);
          addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
              npcPics.requestFocusInWindow();
            }
          });

          NPC editNPC = npcs.get(selected);

          switch(editNPC.getType().toString()){
            case "NESS": npcPics.setSelectedItem(npcPicArr[0]);break;
            case "POKEMON" : npcPics.setSelectedItem(npcPicArr[1]);break;
            default: System.out.println("This shouldn't be happening!");
          }

          name.setText(editNPC.getName());
          walking.setText(editNPC.getWalkingScript().getScript());
          talking.setText(editNPC.getTalkingScript().getScript());

          break;
        case SCRIPT:
          options = new JOptionPane(scriptContents, JOptionPane.PLAIN_MESSAGE,
              JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);
          addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
              scriptID.requestFocusInWindow();
            }
          });

          AreaScript editScript = scripts.get(selected);
          scriptID.setText(editScript.getName());
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

          //TODO Implement Sign init

          break;
        case WARP:
          options = new JOptionPane(warpContents, JOptionPane.PLAIN_MESSAGE,
              JOptionPane.YES_NO_OPTION, null, buttons, buttons[0]);
          addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
              newMap.requestFocusInWindow();
            }
          });

          WarpScript editWarp = (WarpScript)scripts.get(selected);
          newMap.setText(editWarp.getMap());

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

          switch(type){
            case NPC:
              if(checkText(name) && checkText(walking) && checkText(talking)){
                NPC editNPC = npcs.get(selected);
                editNPC.setName(name.getText());
                editNPC.setNPCType(NPCType.toEnum((String)npcPics.getSelectedItem()));
                editNPC.setWalkingScript(new WalkingScript(walking.getText(), editNPC));
                editNPC.setTalkingScript(new TalkingScript(talking.getText(), editNPC));
                clearAndHide();
              }
              break;
            case SIGN:
              if(checkText(signID)){
                //TODO Implement signs
              }
              break;
            case ENEMY:
              //TODO Implement Enemies
              break;
            case SCRIPT:
              if(checkText(scriptID)){
                AreaScript editScript = scripts.get(selected);
                if(npcsCombo.getSelectedItem() != null){
                  editScript.setNPC((NPC)npcsCombo.getSelectedItem());
                }
                editScript.setScript(scriptID.getText());
                editScript.setRepeatable(repeatable.isSelected());

                clearAndHide();
              }
              break;
            case WARP:
              if(checkText(newMap)){
                //TODO Add warping to a specific location

                WarpScript editWarp = (WarpScript)scripts.get(selected);
                editWarp.setMap(newMap.getText());
                clearAndHide();
              }
              break;

            default:
              //TODO Debug
              System.out.println("How does this even happen??");

          }
        }else{
          clearAndHide();
        }
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
