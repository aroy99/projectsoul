package komorebi.projsoul.editor.modes.connect;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FindExistingWorldDialog extends JDialog {

  private static final long serialVersionUID = 1L;
  
  private static final Dimension DIALOG_SIZE = new Dimension(350,350);
  private static final Rectangle SCROLL_BOUNDS = new Rectangle(50, 75, 150, 200);

  private static final Rectangle SEL_BUTTON = new Rectangle(200, 248, 75, 25);

  private JList<String> worldList;
  private DefaultListModel<String> worldModel;
  private JScrollPane worldPane;
  
  private JButton sel, cancel;
  
  private JPanel panel;
  
  private WorldShell selWorld;
  private boolean cancelled;
  
  private Lock lock;

  public FindExistingWorldDialog(Lock lock)
  {
    cancelled = true;
    
    panel = new JPanel(null);
    panel.setSize(DIALOG_SIZE);
    
    worldModel = new DefaultListModel<String>();

    for (String worldName: World.getAllWorldNames())
    {
      worldModel.addElement(worldName);
    }

    worldList = new JList<String>(worldModel);
    worldPane = new JScrollPane(worldList);

    worldPane.setBounds(SCROLL_BOUNDS);
    
    sel = new JButton("Select");
    sel.setBounds(SEL_BUTTON);
    sel.setEnabled(worldList.getSelectedIndex()!=-1);
    sel.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {

        selWorld = World.findWorldContainingMap(worldList.getSelectedValue());
        cancelled = false;
        
        lock.unlock();
      }


    });

    worldList.addListSelectionListener(new ListSelectionListener() {

      @Override
      public void valueChanged(ListSelectionEvent arg0) {
        sel.setEnabled(worldList.getSelectedIndex()!=-1);
      }

    });

    panel.add(worldPane);
    panel.add(sel);

    this.add(panel);
    this.setTitle("Add/Select Worlds");
    this.setSize(DIALOG_SIZE);
    this.setResizable(false);
    this.setVisible(true);
    
    this.lock = lock;
  }
  
  public WorldShell getSelectedWorld()
  {
    return selWorld;
  }
  
  public boolean wasCancelled()
  {
    return cancelled;
  }
}
