package komorebi.projsoul.script.exceptions;

public class UndefinedConstructorException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public UndefinedConstructorException(String className)
  {
    super("Class " + className + " must define a constructor with no "
        + "parameters, or define no constructor");
  }

}
