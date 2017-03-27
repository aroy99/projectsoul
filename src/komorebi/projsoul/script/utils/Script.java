package komorebi.projsoul.script.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.commands.abstracts.Command;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;
import komorebi.projsoul.script.exceptions.UndefinedKeywordException;
import komorebi.projsoul.script.read.Branch;
import komorebi.projsoul.script.read.CommandRequest;
import komorebi.projsoul.script.read.Request;
import komorebi.projsoul.script.read.StringReader;

public class Script {
  private Branch main;
  private String name;

  private HashMap<String, String[]> commands;
  
  protected Script(File scriptLocation)
  {
    main = new Branch("main");
    
    try {
      BufferedReader read = new BufferedReader(
          new FileReader(scriptLocation));
      
      String wholeScript = readEntireFile(read);
      
      if (!wholeScript.startsWith("branch"))
        throw new InvalidScriptSyntaxException("Every command "
            + "must be contained within a branch");
            
      commands = splitIntoBranches(wholeScript);
      
      if (!definesMain())
        throw new InvalidScriptSyntaxException("Script " + 
            scriptLocation.getName() + " must define a main branch");
      
      String[] mainBranch = commands.get("main");
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
  
  private HashMap<String, String[]> splitIntoBranches(String script)
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

  public void readInto(Branch branch, String[] commands)
  {    
    for (String command: commands)
    {
      readCommand(branch, command);
    }
  }

  private void readCommand(Branch branch, String command)
  {
    boolean instructionAppliesToPlayer = appliesToPlayer(command);
    if (instructionAppliesToPlayer)
      command = removeFirstCharOf(command);

    if (ScriptUtils.keywordExists(firstWordOf(command))) {
      
      String keyword = firstWordOf(command);
      
      Command task = 
          ScriptUtils.createNewInstanceWithKeyword(keyword);
      
      command = removeKeyword(command);
      
      task.interpret(command);
      task.setApplicableToPlayer(instructionAppliesToPlayer);
      
      handleRequests(task.askForRequests());
      branch.addTask(task);
      
    } else {
      throw new InvalidScriptSyntaxException("No such keyword as " + 
          firstWordOf(command));
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
        handleCommandRequests((CommandRequest) request);
    }
  }
  
  private void handleCommandRequests(CommandRequest request)
  {
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
}
