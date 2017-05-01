package komorebi.projsoul.editor.script;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import komorebi.projsoul.script.commands.keywords.Keywords;
import komorebi.projsoul.script.decision.Flags;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;
import komorebi.projsoul.script.exceptions.UndefinedKeywordException;
import komorebi.projsoul.script.utils.Script;

public class ScriptEditor extends JFrame {

  private static final long serialVersionUID = 1L;

  private JPanel canvas;
  private static final Dimension FRAME_DIMENSION = new Dimension(450, 500);

  private JScrollPane inputScroll;
  private static final Rectangle SCROLLPANE_AREA = 
      new Rectangle(25, 25, 400, 300);
  
  private JScrollPane outputScroll;
  private static final Rectangle OUTPUT_SCROLLPANE_AREA = 
      new Rectangle(25, 360, 400, 100);
  
  private NumberedTextArea input;
  private static final Dimension INPUT_DIMENSION = 
      new Dimension(400, 300);

  private JButton compileButton;
  private static final Rectangle COMPILE_BUTTON_AREA =
      new Rectangle(25, 330, 100, 25);
  
  private JButton saveButton;
  private static final Rectangle SAVE_BUTTON_AREA = 
      new Rectangle(150, 330, 100, 25);
  
  private JTextArea output;
  private static final Rectangle OUTPUT_AREA = 
      new Rectangle(0, 0, 400, 100);


  private File file;

  public static void main(String[] args)
  {
    try {
      Keywords.loadKeywords();
    } catch (UndefinedKeywordException | UndefinedConstructorException e) {
      e.printStackTrace();
    }
    
    Flags.loadFlags();
    
    File file = new File("res/scripts/debug_script");
    ScriptEditor edit = new ScriptEditor(file);
  }

  public ScriptEditor(File file)
  {
    this.file = file;
    this.setSize(FRAME_DIMENSION);

    canvas = new JPanel(null);
    canvas.setSize(FRAME_DIMENSION);
    
    input = new NumberedTextArea(new Point(), INPUT_DIMENSION);    
    inputScroll = new JScrollPane(input);
    inputScroll.setBounds(SCROLLPANE_AREA);
    
    readIntoInputTextBox(file);

    output = new JTextArea();
    outputScroll = new JScrollPane(output);
    output.setBounds(OUTPUT_AREA);
    outputScroll.setBounds(OUTPUT_SCROLLPANE_AREA);
    output.setEditable(false);

    compileButton = new JButton("Compile");
    compileButton.setBounds(COMPILE_BUTTON_AREA);
    
    compileButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        save();
        compile();
      }
      
    });
    
    saveButton = new JButton("Save");
    saveButton.setBounds(SAVE_BUTTON_AREA);
    
    saveButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        save(); 
      }
    });

    canvas.add(inputScroll);
    canvas.add(outputScroll);
    canvas.add(compileButton);
    canvas.add(saveButton);

    this.add(canvas);

    this.setResizable(false);
    this.setTitle("Edit Script - " + file.getName());
    this.setVisible(true);
  }

  private void readIntoInputTextBox(File file)
  {
    try {
      BufferedReader read = new BufferedReader(new FileReader(file));
      
      String line;
      
      while ((line = read.readLine()) != null)
      {
        input.append(line + "\n");
      }
      
      read.close();
      
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private void save()
  {
    try {
      BufferedWriter write = new BufferedWriter(new FileWriter(
          file, false));
      
      String[] lines = input.getText().split("\n");
      
      for (String line: lines)
      {
        write.append(line + "\n");
      }
      
      write.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private void compile()
  {
    Script script = new Script(file);
    
    String message;
    if (script.getErrors().isEmpty())
      message = "No errors.";
    else
      message = script.getErrors();
    
    output.setText(message);
  }

}
