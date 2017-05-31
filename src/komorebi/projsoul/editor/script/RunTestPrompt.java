package komorebi.projsoul.editor.script;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import komorebi.projsoul.editor.script.ScriptEditor.ScriptChooser;

public class RunTestPrompt extends JDialog {

  private static final long serialVersionUID = 1L;
  private static final Dimension FRAME_SIZE =
      new Dimension(380, 175);

  private static final Rectangle PANEL_BOUNDS = 
      new Rectangle(0, 0, 325, 150);

  private static final Rectangle WALK_LABEL_BOUNDS =
      new Rectangle(25, 25, 125, 25);

  private static final Rectangle WALK_SCRIPT_BOUNDS = 
      new Rectangle(25, 50, 125, 25);

  private static final Rectangle WALK_BUTTON_BOUNDS = 
      new Rectangle(155, 50, 25, 25);

  private static final Rectangle TALK_LABEL_BOUNDS = 
      new Rectangle(200, 25, 125, 25);

  private static final Rectangle TALK_SCRIPT_BOUNDS = 
      new Rectangle(200, 50, 125, 25);

  private static final Rectangle TALK_BUTTON_BOUNDS = 
      new Rectangle(330, 50, 25, 25);

  private static final Rectangle RUN_BUTTON_BOUNDS =
      new Rectangle(180, 100, 75, 25);

  private static final Rectangle CANCEL_BUTTON_BOUNDS =
      new Rectangle(280, 100, 75, 25);

  private JLabel walkScript, talkScript;
  
  private static final ScriptChooser fileChooser = new
      ScriptChooser(new File("res/scripts"));
  
  private static final File SCRIPT_ARGS = new File("scriptTest/scripts.args");

  public RunTestPrompt(JFrame owner, String testThisScript)
  {
    super(owner);

    this.setSize(FRAME_SIZE);
    this.setTitle("Test Scripts");
    this.setResizable(false);

    JPanel panel = new JPanel(null);
    panel.setBounds(PANEL_BOUNDS);

    JLabel walkLabel = new JLabel("Walk Script:");
    walkLabel.setBounds(WALK_LABEL_BOUNDS);

    JLabel talkLabel = new JLabel("Talk Script:");
    talkLabel.setBounds(TALK_LABEL_BOUNDS);

    walkScript = new JLabel();
    talkScript = new JLabel(testThisScript);

    walkScript.setBounds(WALK_SCRIPT_BOUNDS);
    talkScript.setBounds(TALK_SCRIPT_BOUNDS);

    walkScript.setBorder(BorderFactory.createLineBorder(Color.black));
    talkScript.setBorder(BorderFactory.createLineBorder(Color.black));

    JButton walkButton = new JButton("...");
    JButton talkButton = new JButton("...");

    walkButton.setBounds(WALK_BUTTON_BOUNDS);
    talkButton.setBounds(TALK_BUTTON_BOUNDS);

    walkButton.addActionListener(new ActionListener()
    {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        findScriptFor(walkScript);
        
      }

    });
    
    talkButton.addActionListener(new ActionListener()
    {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        findScriptFor(talkScript);
        
      }

    });

    JButton runButton = new JButton("Run");
    JButton cancelButton = new JButton("Cancel");

    runButton.setBounds(RUN_BUTTON_BOUNDS);
    cancelButton.setBounds(CANCEL_BUTTON_BOUNDS);

    runButton.addActionListener(new ActionListener()
    {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        if (bothLabelsHaveText())
        {
          runTest();
          closeDialog();
        } else
        {
          notifyNeedsToFillBothLabels();
        }
        
        
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
    panel.add(walkScript);
    panel.add(talkScript);

    panel.add(walkButton);
    panel.add(talkButton);
    panel.add(runButton);
    panel.add(cancelButton);

    this.add(panel);

  }
  
  private boolean bothLabelsHaveText()
  {
    return 
        !walkScript.getText().equals("") && !talkScript.getText().equals("");
  }
  
  private void notifyNeedsToFillBothLabels()
  {
    JOptionPane.showConfirmDialog(this, "A walk script and talk script"
        + " must be defined before running a test");
  }
  
  private void findScriptFor(JLabel label)
  {
    File selected = searchForFile();
    
    if (selected != null)
    {
      label.setText(selected.getAbsolutePath());
    }
  }
  
  private File searchForFile()
  {
    int buttonPressed = fileChooser.showOpenDialog(this);
    
    if (buttonPressed == JFileChooser.APPROVE_OPTION)
    {
      return fileChooser.getSelectedFile();
    }
    
    return null;
    
  }

  private void closeDialog()
  {
    this.dispose();
  }

  private void runTest()
  {
    try {
      PrintWriter write = new PrintWriter(SCRIPT_ARGS);
     
      write.println(walkScript.getText().replace(" ", "?"));
      write.println(talkScript.getText().replace(" ", "?"));
      
      write.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    
    runTestWithScripts(walkScript.getText(), 
        talkScript.getText());
  }

  private void runTestWithScripts(String walk, String talk)
  {

    String cmdLine = "java -jar \"Script Tester.jar\"";
    
    Runtime runTime = Runtime.getRuntime();
    try {
      runTime.exec(cmdLine);
    } catch (IOException e) {
      e.printStackTrace();
    }    

  }

}
