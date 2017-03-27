package komorebi.projsoul.script.read;

import java.util.HashMap;

public class StringReader {

  private HashMap<String, String> branches;
  private HashMap<String, String[]> commands;
  private CommandReader read;

  private String[] lines;

  private int line = 0;

  public StringReader(String string)
  {
    branches = new HashMap<String, String>();
    commands = new HashMap<String, String[]>();
    
    lines = string.split("\n");
  }

  public void read()
  {
    while (hasNext())
    {      
      nextBranch();
    }

    readBranches();
  }
  
  private void readBranches()
  {
    for (String branchName: branches.keySet())
    {
      read = new CommandReader(branches.get(branchName));
      read.read();
      
      commands.put(branchName, read.getCommands());
    }
  }
  
  public HashMap<String, String[]> getCommands()
  {
    return commands;
  }

  private void nextBranch()
  {    
    String declaration = currentLine();
    String branchName = getBranchName(declaration);
    String commands = "";
    
    next();
        
    while (!declaresNewBranch(currentLine()))
    {
      commands += currentLine() + "\n";
                 
      if (!hasNext())
        break;
      else
        next();
    }
    
    branches.put(branchName, commands);
  }

  private void next()
  {
    line++;
  }

  private String currentLine()
  {
    return lines[line];
  }

  private boolean hasNext()
  {
    return line < lines.length - 1;
  }

  private String getBranchName(String str)
  {
    return str.replace("branch", "").trim();
  }

  private boolean declaresNewBranch(String str)
  {
    return str.startsWith("branch ");
  }

}
