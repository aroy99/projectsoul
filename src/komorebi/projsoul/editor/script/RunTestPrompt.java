package komorebi.projsoul.editor.script;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import komorebi.projsoul.script.utils.ClassUtils;

public class RunTestPrompt extends JDialog {

  private static final long serialVersionUID = 1L;
  private static final Dimension FRAME_SIZE =
      new Dimension(325, 175);
  
  private static final Rectangle PANEL_BOUNDS = 
      new Rectangle(0, 0, 325, 150);

  private static final Rectangle WALK_LABEL_BOUNDS =
      new Rectangle(25, 25, 125, 25);
  
  private static final Rectangle TALK_LABEL_BOUNDS = 
      new Rectangle(175, 25, 125, 25);
  
  private static final Rectangle WALK_COMBO_BOUNDS = 
      new Rectangle(25, 50, 125, 25);

  private static final Rectangle TALK_COMBO_BOUNDS = 
      new Rectangle(175, 50, 125, 25);

  private static final Rectangle RUN_BUTTON_BOUNDS =
      new Rectangle(125, 100, 75, 25);

  private static final Rectangle CANCEL_BUTTON_BOUNDS =
      new Rectangle(225, 100, 75, 25);
  
  private JComboBox<String> walkCombo, talkCombo;


  public RunTestPrompt(JFrame owner, String testThisScript)
  {
    super(owner);
    
    this.setSize(FRAME_SIZE);
    this.setTitle("Test Scripts");
    this.setResizable(false);

    JPanel panel = new JPanel(null);
    panel.setBounds(PANEL_BOUNDS);

    String[] allScripts = getAllScripts();
    
    JLabel walkLabel = new JLabel("Walk Script:");
    walkLabel.setBounds(WALK_LABEL_BOUNDS);

    JLabel talkLabel = new JLabel("Talk Script:");
    talkLabel.setBounds(TALK_LABEL_BOUNDS);

    walkCombo = new JComboBox<String>(allScripts);
    walkCombo.setBounds(WALK_COMBO_BOUNDS);

    talkCombo = new JComboBox<String>(allScripts);
    talkCombo.setBounds(TALK_COMBO_BOUNDS);
    talkCombo.setSelectedItem(testThisScript);

    JButton runButton = new JButton("Run");
    JButton cancelButton = new JButton("Cancel");

    runButton.setBounds(RUN_BUTTON_BOUNDS);
    cancelButton.setBounds(CANCEL_BUTTON_BOUNDS);

    runButton.addActionListener(new ActionListener()
    {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        runTest();
        closeDialog();
      }

    });

    cancelButton.addActionListener(new ActionListener()
    {

      @Override
      public void actionPerformed(ActionEvent e) {
        closeDialog();
      }

    });

    panel.add(walkLabel);
    panel.add(talkLabel);
    panel.add(walkCombo);
    panel.add(talkCombo);
    panel.add(runButton);
    panel.add(cancelButton);

    this.add(panel);

  }

  private void closeDialog()
  {
    this.dispose();
  }

  private String[] getAllScripts()
  {
    ArrayList<File> files = 
        ClassUtils.getAllFilesInFolder(new File("res/scripts/"));

    String[] scripts = new String[files.size()];

    for (int i = 0; i < files.size(); i++)
    {
      scripts[i] = simpleNameOf(files.get(i));
    }

    return scripts;
  }

  private String simpleNameOf(File script)
  {
    return script.getName().replace(".txt", "");
  }
  
  private void runTest()
  {
    runTestWithScripts((String) walkCombo.getSelectedItem(), 
        (String) talkCombo.getSelectedItem());
  }

  private void runTestWithScripts(String walkScript, String talkScript)
  {
    String cmdLine = "java -Djava.library.path=\"native/windows/\" "
        + "-jar script-tester-eclipse.jar " + walkScript + " " + 
        talkScript;

    Runtime runTime = Runtime.getRuntime();
    try {
      runTime.exec(cmdLine);
    } catch (IOException e) {
      e.printStackTrace();
    }    

  }

}
