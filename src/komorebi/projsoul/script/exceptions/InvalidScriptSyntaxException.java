package komorebi.projsoul.script.exceptions;

/**
 * 
 *
 * @author Andrew Faulkenberry
 */
public class InvalidScriptSyntaxException extends Exception {

  private static final long serialVersionUID = 1L;
  
  public InvalidScriptSyntaxException(String message)
  {
    super(message);
  }
}
