package komorebi.projsoul.script.exceptions;

public class UndefinedKeywordException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public UndefinedKeywordException(String className)
  {
    super("Class " + className + " must define the static method \n" + 
        "\tpublic static String keyword()");
  }
}
