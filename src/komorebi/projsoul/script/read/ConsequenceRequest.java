package komorebi.projsoul.script.read;

import komorebi.projsoul.script.commands.abstracts.Command;
import komorebi.projsoul.script.decision.DecisionStatement;

public class ConsequenceRequest extends Request {

  private DecisionStatement dec;
  
  private String consequenceText;
  private int line;
  
  public ConsequenceRequest(DecisionStatement dec, String consequenceText,
      int line)
  {
    this.dec = dec;
    this.consequenceText = consequenceText;
    this.line = line;
  }
  
  public static boolean isInstance(Request r)
  {
    return ConsequenceRequest.class.isInstance(r);
  }
  
  public void setConsequence(Command consequence)
  {
    dec.setConsequence(consequence);
  }
  
  public String getConsequenceText()
  {
    return consequenceText;
  }
  
  public int getLine()
  {
    return line;
  }
  
  
}
