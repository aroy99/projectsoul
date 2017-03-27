package komorebi.projsoul.script.exceptions;

public class InvalidScriptSyntaxException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -7881015000718915134L;

  public InvalidScriptSyntaxException(String description)
  {
    super(description);
  }
}
