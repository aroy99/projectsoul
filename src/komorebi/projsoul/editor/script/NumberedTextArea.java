package komorebi.projsoul.editor.script;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class NumberedTextArea extends JPanel {

  private static final long serialVersionUID = 1L;
  
  private JLabel numbers;
  private JTextArea text;
  
  private Dimension minimum;
  
  private static final String frontHTMLTag = "<html>";
  private static final String backHTMLTag = "</html>";
  private static final String newLine = "<br>";
  
  private int prevNumLines = 0;
  
  private static final int NUMBER_LABEL_WIDTH = 25;
  public NumberedTextArea(Point location, Dimension min)
  { 
    super(null);
    
    this.minimum = min;
    this.setLocation(location);
    
    numbers = new JLabel(frontHTMLTag + backHTMLTag);
        
    numbers.setBounds(0, 0, NUMBER_LABEL_WIDTH, min.height);
    numbers.setVerticalAlignment(SwingConstants.TOP);
    
    text = new JTextArea();
    text.setLocation(NUMBER_LABEL_WIDTH, 0);
    text.setMinimumSize(min);
    text.setSize(min);
    
    text.addKeyListener(new KeyListener() {

      @Override
      public void keyPressed(KeyEvent arg0) {
        adjustPanelSize();
        adjustTextAreaSize();        
      }

      @Override
      public void keyReleased(KeyEvent arg0) {
        
      }

      @Override
      public void keyTyped(KeyEvent arg0) {
        updateNumbers();
      }
      
    });
    
    this.add(text);
    this.add(numbers);
    
    updateNumbers();
    
  }
  
  public void append(String str)
  {
    text.append(str);
    updateNumbers();
  }
  
  public String getText()
  {
    return text.getText();
  }
  
  private void adjustPanelSize()
  {
    Dimension newDimension = new Dimension(this.getSize().width, 
        text.getPreferredSize().height);
    this.setPreferredSize(newDimension);
  }
  
  private void adjustTextAreaSize()
  {
    if (greaterThanMinimumHeight(text.getPreferredSize().height))
    {
      text.setSize(text.getSize().width, text.getPreferredSize().height);
      numbers.setSize(numbers.getSize().width, text.getPreferredSize().height);
    }
      
  }
  
  private boolean greaterThanMinimumHeight(int height)
  {
    return height > minimum.height;
  }
  
  private void updateNumbers()
  {
    if (text.getLineCount() != prevNumLines)
    {
      writeToLabel(1, text.getLineCount());
    }
     
    prevNumLines = text.getLineCount();
  }
  
  private void writeToLabel(int from, int to)
  {
    String newText = frontHTMLTag;
    
    for (int i = from; i <= to; i++)
    {
      newText += i;
      if (i != to)
        newText += newLine;
    }
    
    newText += backHTMLTag;
    
    numbers.setText(newText);
  }
  

}
