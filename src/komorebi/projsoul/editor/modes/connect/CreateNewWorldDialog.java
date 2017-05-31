package komorebi.projsoul.editor.modes.connect;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CreateNewWorldDialog extends JDialog {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private Lock lock;
  
  private String map;
  
  private WorldShell selWorld;
  private boolean cancelled;

  private static final Dimension DIALOG_SIZE = new Dimension(350,350);

  private static final Rectangle TEXTBOX_BOUNDS = new Rectangle(50, 25, 150, 25);
  private static final Rectangle ADD_BUTTON = new Rectangle(200, 25, 75, 25);

  private JTextField text;
  private JButton add;

  private JPanel panel;
  
  public CreateNewWorldDialog(String map, Lock lock)
  {
    this.map = map;
    cancelled = true;
    
    panel = new JPanel(null);
    panel.setSize(DIALOG_SIZE);
    
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
        } else if (text.getText().startsWith("#ref"))
        {
          JOptionPane.showMessageDialog(null, "Worlds cannot start with the " +
              "the reserved keyword #ref.");
          text.selectAll();
        } else
        {
          WorldShell newWorld = new WorldShell(text.getText());
          newWorld.addMap(getMap());
          World.addWorld(newWorld);
          
          selWorld = newWorld;
          cancelled = false;
          unlock();
          text.setText("");
        }
      }

    });
    
    panel.add(text);
    panel.add(add);

    this.add(panel);
    this.setTitle("Create World");
    this.setSize(DIALOG_SIZE);
    this.setResizable(false);
    this.setVisible(true);
    
    this.lock = lock;
  }
  
  private void unlock()
  {
    lock.unlock();
  }
  
  private String getMap()
  {
    return map;
  }
  
  public boolean wasCancelled()
  {
    return cancelled;
  }
  
  public WorldShell getSelectedWorld()
  {
    return selWorld;
  }
  
}
