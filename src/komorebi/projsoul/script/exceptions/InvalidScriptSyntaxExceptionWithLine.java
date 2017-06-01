package komorebi.projsoul.script.exceptions;

/**
 * 
 *
 * @author Andrew Faulkenberry
 */
public class InvalidScriptSyntaxExceptionWithLine extends InvalidScriptSyntaxException {

  private static final long serialVersionUID = -7881015000718915134L;
  
  public InvalidScriptSyntaxExceptionWithLine(String description, int line)
  {
    super("Line " + line + ": " + description);
  }
}
