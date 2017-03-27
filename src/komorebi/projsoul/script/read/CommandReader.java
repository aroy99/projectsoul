package komorebi.projsoul.script.read;

public class CommandReader {
  
  private String[] lines;
  private String[] commands;
    
  public CommandReader(String string)
  {
    lines = string.split("\n");
  }
  
  public void read()
  { 
    String[] temp = new String[lines.length];
    
    int commandNo = 0;
    String prevCommand = "";
    
    boolean isNewCommand = false;
    
    for (String line: lines)
    {       
      isNewCommand = !line.startsWith("else");
      
      if (isNewCommand && prevCommand != "")
      {
        temp[commandNo] = prevCommand;
        commandNo++;
        prevCommand = "";
      } else if (!isNewCommand)
      {
        prevCommand += "\n";
      }
      
      prevCommand += line;
    }
    
    if (prevCommand != "")
        temp[commandNo] = prevCommand;
    
    copyToCommands(temp);
  }
  
  public String[] getCommands()
  {
    return commands;
  }
  
  private void copyToCommands(String[] temp)
  {
    commands = new String[numberOfCommandsIn(temp)];
       
    for (int i = 0; i < commands.length; i++)
    {
      commands[i] = temp[i];
    }

  }
  
  private int numberOfCommandsIn(String[] temp)
  {
    
    for (int i = 0; i < temp.length; i++)
    {
      if (temp[i] == null || temp[i] == "")
        return i;
    }
    
    return temp.length;
  }
  
}
