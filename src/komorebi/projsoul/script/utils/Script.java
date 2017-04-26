package komorebi.projsoul.script.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import komorebi.projsoul.script.commands.abstracts.Command;
import komorebi.projsoul.script.commands.keywords.Keywords;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.read.Branch;
import komorebi.projsoul.script.read.CodeBlock;
import komorebi.projsoul.script.read.CommandRequest;
import komorebi.projsoul.script.read.ErrorLog;
import komorebi.projsoul.script.read.Request;
import komorebi.projsoul.script.read.StringReader;

public class Script {
  private Branch main;
  private String name;

  private HashMap<String, CodeBlock[]> commands;
  private ErrorLog errorLog;
  
  public Script(File scriptLocation)
  {
    main = new Branch("main");
    errorLog = new ErrorLog();
    
    try {
      BufferedReader read = new BufferedReader(
          new FileReader(scriptLocation));
      
      String wholeScript = readEntireFile(read);
      
      if (!wholeScript.startsWith("branch"))
        errorLog.addError(new InvalidScriptSyntaxExceptionWithLine("Every command "
            + "must be contained within a branch", 1));
            
      commands = splitIntoBranches(wholeScript);
      
      if (!definesMain())
        errorLog.addError(new InvalidScriptSyntaxExceptionWithLine("Script " + 
            scriptLocation.getName() + " must define a main branch", 1));
      
      CodeBlock[] mainBranch = commands.get("main");
      readInto(main, mainBranch);
      
      read.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    this.name = condenseToName(scriptLocation);
  }
  
  private String condenseToName(File file)
  {
    String name = file.getName().replace(".txt", "");
    return name;
  }
  
  private HashMap<String, CodeBlock[]> splitIntoBranches(String script)
  {    
    StringReader reader = new StringReader(script);
    reader.read();

    return reader.getCommands();
  }
  
  private boolean definesMain()
  {
    return commands.containsKey("main");
  }
  
  
  private String readEntireFile(BufferedReader read) throws IOException
  {
    String entireFile = "", nextLine;
    
    try {
      while ((nextLine = read.readLine()) != null)
      {
        entireFile += nextLine + "\n";
      }
      
      return entireFile;
    } catch (IOException e) {
      throw e;
    }
  }

  public void readInto(Branch branch, CodeBlock[] commands)
  {        
    for (CodeBlock command: commands)
    {
      try {
        readCommand(branch, command);
      } catch (InvalidScriptSyntaxExceptionWithLine e) {
        errorLog.addError(e);
      }
    }
  }

  private void readCommand(Branch branch, CodeBlock block) throws InvalidScriptSyntaxExceptionWithLine
  {
    String command = block.getCode();
    
    boolean instructionAppliesToPlayer = appliesToPlayer(command);
    if (instructionAppliesToPlayer)
      command = removeFirstCharOf(command);

    if (Keywords.keywordExists(firstWordOf(command))) {
      
      String keyword = firstWordOf(command);
      
      Command task = 
          Keywords.createNewInstanceWithKeyword(keyword);
      
      command = removeKeyword(command);
      
      try {        
        task.interpret(command, block.getStartingLine());
        task.setApplicableToPlayer(instructionAppliesToPlayer);
        
        handleRequests(task.askForRequests());
        branch.addTask(task);
      } catch (InvalidScriptSyntaxExceptionWithLine e) {
        errorLog.addError(e);
      }
      
    } else {
      throw new InvalidScriptSyntaxExceptionWithLine("No such keyword as " + 
          firstWordOf(command), block.getStartingLine());
    }
  }

  private String firstWordOf(String string)
  {
    if (string.contains(" "))
      return string.substring(0, string.indexOf(" "));
    else
      return string;
  }
  
  private String removeFirstCharOf(String string)
  {
    return string.substring(1);
  }
  
  private String removeKeyword(String string)
  {
    String ret;
    
    if (string.contains(" "))
      ret = string.substring(string.indexOf(" ") + 1);
    else
      ret = "";
    
    return ret.trim();
  }

  private boolean appliesToPlayer(String command)
  {
    return command.startsWith("@");
  }
  
  private void handleRequests(Request[] requests)
  {
    for (Request request: requests)
    {
      if (CommandRequest.isInstance(request))
        try {
          handleCommandRequests((CommandRequest) request);
        } catch (InvalidScriptSyntaxExceptionWithLine e) {
          errorLog.addError(e);
        }
    }
  }
  
  private void handleCommandRequests(CommandRequest request)
    throws InvalidScriptSyntaxExceptionWithLine
  {
    if (!commands.containsKey(request.getBranchName()))
      throw new InvalidScriptSyntaxExceptionWithLine("The ask option " +  
          request.getBranchName() + " does not define a corresponding "
              + "branch.", request.getLine());
    
    readInto(request.getBranchReference(), 
        commands.get(request.getBranchName()));  
  }
  
  public String getName()
  {
    return name;
  }
  
  public Branch main()
  {
    return main;
  }
  
  public String getErrors()
  {
    return errorLog.getErrors();
  }
}
