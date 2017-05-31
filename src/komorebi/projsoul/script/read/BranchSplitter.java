package komorebi.projsoul.script.read;

public class BranchSplitter {
  
  private String[] lines;
  private CodeBlock[] commands;
  
  private int startLine;
    
  public BranchSplitter(CodeBlock code)
  {
    lines = code.getCode().split("\n");
    startLine = code.getStartingLine();
  }
  
  public void read()
  { 
    CodeBlock[] temp = new CodeBlock[lines.length];
    
    int commandNo = 0;
    int lineNo = startLine;
    
    CodeBlock prevCommand = new CodeBlock(lineNo);
    
    boolean isNewCommand = false;
    
    for (String line: lines)
    {       
      isNewCommand = !line.startsWith("else");
      
      if (isNewCommand && prevCommand.getCode() != "")
      {
        temp[commandNo] = prevCommand;
        commandNo++;
        prevCommand = new CodeBlock(lineNo);
      } else if (!isNewCommand)
      {
        prevCommand.append("\n");
      }
      
      prevCommand.append(line);
      lineNo++;
    }
    
    if (prevCommand.getCode() != "")
        temp[commandNo] = prevCommand;
    
    copyToCommands(temp);
  }
  
  public CodeBlock[] getCommands()
  {
    return commands;
  }
  
  private void copyToCommands(CodeBlock[] temp)
  {
    commands = new CodeBlock[numberOfCommandsIn(temp)];
       
    for (int i = 0; i < commands.length; i++)
    {
      commands[i] = temp[i];
    }

  }
  
  private int numberOfCommandsIn(CodeBlock[] temp)
  {
    
    for (int i = 0; i < temp.length; i++)
    {
      if (temp[i] == null || temp[i].getCode() == "")
        return i;
    }
    
    return temp.length;
  }
  
}
