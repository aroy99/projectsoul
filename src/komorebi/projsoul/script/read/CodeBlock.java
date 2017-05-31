package komorebi.projsoul.script.read;

public class CodeBlock {
  private int startingLine;
  private String code;
  
  public CodeBlock(int startingLine)
  {
    this.startingLine = startingLine;
    code = "";
  }
  
  public void append(String string)
  {
    code += string;
  }
  
  public void appendLine(String string)
  { 
    code += string + "\n";
  }
  
  public int getStartingLine()
  {
    return startingLine;
  }
  
  public String getCode()
  {
    return code;
  }
}
