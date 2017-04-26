package komorebi.projsoul.script.read;

import java.util.HashMap;

public class StringReader {

  private HashMap<String, CodeBlock> branches;
  private HashMap<String, CodeBlock[]> commands;
  private CommandReader read;

  private String[] lines;

  private int line = 0;

  public StringReader(String string)
  {
    branches = new HashMap<String, CodeBlock>();
    commands = new HashMap<String, CodeBlock[]>();

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

  public HashMap<String, CodeBlock[]> getCommands()
  {
    return commands;
  }

  private void nextBranch()
  {        
    String declaration = currentLine();
    String branchName = getBranchName(declaration);

    next();

    CodeBlock code = new CodeBlock(line + 1);

    boolean first = true;

    while (!declaresNewBranch(currentLine()))
    {       
      if (!first && !currentLine().trim().isEmpty())
        code.appendLine("");

      if (!currentLine().trim().isEmpty())
        code.append(currentLine());

      if (!hasNext())
        break;
      else
        next();

      first = false;
    }    

    branches.put(branchName, code);
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
