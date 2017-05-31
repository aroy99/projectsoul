package komorebi.projsoul.editor.script;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import komorebi.projsoul.script.commands.keywords.Keywords;
import komorebi.projsoul.script.decision.Flags;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;
import komorebi.projsoul.script.exceptions.UndefinedKeywordException;
import komorebi.projsoul.script.utils.Script;

public class ScriptEditor extends JFrame {

  private static final long serialVersionUID = 1L;

  private JPanel canvas;
  private static final Dimension FRAME_DIMENSION = 
      new Dimension(450, 500);

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

  private JButton testButton;
  private static final Rectangle TEST_BUTTON_AREA = 
      new Rectangle(150, 330, 100, 25);

  private JTextArea output;
  private static final Rectangle OUTPUT_AREA = 
      new Rectangle(0, 0, 400, 100);

  private JMenuBar menu;
  private static final Rectangle MENU_AREA =
      new Rectangle(0, 0, 450, 25);
  private JMenu fileMenu;

  private static final ScriptChooser fileChooser = new
      ScriptChooser(new File("res/scripts"));

  private boolean needsToBeSaved;
  private File file;

  protected static class ScriptChooser extends JFileChooser {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ScriptChooser(File file)
    {
      super(file);
      
      this.setFileFilter(new FileNameExtensionFilter("Project Soul Scripts",
          "txt"));
      this.setAcceptAllFileFilterUsed(false);
    }

  }
  
  public static void main(String[] args)
  {
    try {
      Keywords.loadKeywords();
    } catch (UndefinedKeywordException | UndefinedConstructorException e) {
      e.printStackTrace();
    }

    Flags.loadFlags();

    @SuppressWarnings("unused")
    ScriptEditor edit = new ScriptEditor(null);
  }

  public ScriptEditor(File file)
  {
    this.setSize(FRAME_DIMENSION);

    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    this.addWindowListener(new WindowListener() {

      public void windowActivated(WindowEvent arg0) {}
      public void windowClosing(WindowEvent e) {
        boolean close = true;

        if (needsToBeSaved)
        {
          close = promptSaveBeforeClosing();
        }

        if (close)
          System.exit(0);
      }
      public void windowDeactivated(WindowEvent e) {}
      public void windowDeiconified(WindowEvent e) {}
      public void windowIconified(WindowEvent e) {}
      public void windowOpened(WindowEvent e) {}
      public void windowClosed(WindowEvent e) {}
    });

    canvas = new JPanel(null);
    canvas.setSize(FRAME_DIMENSION);

    createMenu();

    input = new NumberedTextArea(new Point(), INPUT_DIMENSION);    
    inputScroll = new JScrollPane(input);
    inputScroll.setBounds(SCROLLPANE_AREA);

    input.addKeyTypedListener(new KeyListener()
    {
      @Override
      public void keyPressed(KeyEvent e) {

      }

      @Override
      public void keyReleased(KeyEvent e) {

      }

      @Override
      public void keyTyped(KeyEvent e) {
        if (!e.isControlDown())
        {
          needsToBeSaved = true;
          updateTitleBar();
        }
      }

    });



    if (file != null)
    {
      loadFile(file);
    }

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

    testButton = new JButton("Test");
    testButton.setBounds(TEST_BUTTON_AREA);

    testButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {
        
        if (!hasCompilerErrors())
        {
          promptTestDialog();
        } else
        {
          Object[] option = {"OK"};
          
          JOptionPane.showOptionDialog(rootFrame(), 
              "Compiler errors in the script must be resolved "
              + "before testing.", 
              "Warning", 
              JOptionPane.OK_OPTION,
              JOptionPane.WARNING_MESSAGE, null,
              option, option[0]);
        }
      }
        
    });


    canvas.add(inputScroll);
    canvas.add(outputScroll);
    canvas.add(compileButton);
    canvas.add(testButton);
    canvas.add(menu);

    this.add(canvas);

    this.setResizable(false);
    updateTitleBar();
    this.setVisible(true);
  }
  
  private JFrame rootFrame()
  {
    return this;
  }
  
  private boolean hasCompilerErrors()
  {
    compile();
    
    return !output.getText().equals("No errors.");
  }

  private void createMenu()
  {
    menu = new JMenuBar();
    menu.setBounds(MENU_AREA);
    fileMenu = new JMenu("File");

    JMenuItem newFile = new JMenuItem("New");
    newFile.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {
        newFile();
      }

    });
    newFile.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_N, Event.CTRL_MASK));

    JMenuItem openFile = new JMenuItem("Open");
    openFile.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {
        open();
      }
    });
    openFile.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_O, Event.CTRL_MASK));

    JMenuItem saveFile = new JMenuItem("Save");
    saveFile.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {
        if (needsToBeSaved)
          save();
      }
    });

    saveFile.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_S, Event.CTRL_MASK));

    JMenuItem saveFileAs = new JMenuItem("Save As");
    saveFileAs.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {
        saveAs();
      }

    });

    fileMenu.add(newFile);
    fileMenu.add(openFile);
    fileMenu.add(saveFile);
    fileMenu.add(saveFileAs);

    menu.add(fileMenu);
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

  private void newFile()
  {
    boolean close = true;
    
    if (needsToBeSaved)
    {
      close = promptSaveBeforeClosing();
    }
    
    if (close)
    {
      file = null;
      input.clearText();
      updateTitleBar();
    }
  }

  private void save()
  {    
    if (file == null)
    {
      saveAs();
      return;
    }

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

    needsToBeSaved = false;

    updateTitleBar();
  }

  private void saveAs()
  {
    fileChooser.setDialogTitle("Save");
    int response = fileChooser.showSaveDialog(this);

    if (response == JFileChooser.APPROVE_OPTION)
    {
      file = fileChooser.getSelectedFile();
      
      if (!isScript(file))
        file = appendExtension(file);
      
      save();
    }
  }
  
  private boolean isScript(File file)
  {
    return file.getName().endsWith(".txt");
  }
  
  private File appendExtension(File file){
    return new File(file.getPath() + ".txt");
  }

  private void open()
  {
    boolean cancel = false;

    if (needsToBeSaved)
    {
      cancel = !promptSaveBeforeClosing();
    }

    if (!cancel)
    {
      showOpenDialog();
    }

  }

  private void showOpenDialog()
  {
    int buttonPressed = fileChooser.showOpenDialog(this);

    if (buttonPressed == JFileChooser.APPROVE_OPTION)
    {
      File chosen = fileChooser.getSelectedFile();

      loadFile(chosen);

      needsToBeSaved = false;
      updateTitleBar();

    }
  }

  /**
   * Prompts the user to save before closing the current 
   * @return true if the user does not cancel
   */
  private boolean promptSaveBeforeClosing()
  {
    Object[] options = {"Yes", "No", "Cancel"};

    String fileName = file==null?"this file":file.getName();

    int response = JOptionPane.showOptionDialog(this, 
        "Would you like to save " + fileName + "?", 
        "Save?", 
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE, null,
        options, options[2]);

    if (response == JOptionPane.YES_OPTION)
      save();
    else if (response == JOptionPane.CANCEL_OPTION)
      return false;

    return true;
  }

  private void loadFile(File file)
  {
    input.clearText();

    this.file = file;
    readIntoInputTextBox(file);


  }

  private void compile()
  {
    Script script = Script.fromFile(file);

    String message;
    if (script.getErrors().isEmpty())
      message = "No errors.";
    else
      message = script.getErrors();

    output.setText(message);
  }

  private void updateTitleBar()
  {
    if (file == null)
      this.setTitle("Script Editor - New Script");
    else
      this.setTitle("Script Editor - " + file.getName());

    if (needsToBeSaved && !doesTitleBarHaveStar())
    {
      addStarToTitle();
    }

    if (!needsToBeSaved && doesTitleBarHaveStar())
    {
      removeStarFromTitle();
    }
  }

  private boolean doesTitleBarHaveStar()
  {
    String title = this.getTitle();
    return title.charAt(title.length()-1) == '*';
  }

  private void addStarToTitle()
  {
    this.setTitle(this.getTitle() + "*");
  }

  private void removeStarFromTitle()
  {
    String title = this.getTitle();
    this.setTitle(title.substring(0, title.length()-1));
  }
  
  private void promptTestDialog()
  {
    RunTestPrompt prompt = new RunTestPrompt(this, 
        file.getAbsolutePath());
    
    prompt.setVisible(true);
    
  }

}
