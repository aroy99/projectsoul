package komorebi.projsoul.script.utils;

import java.io.File;
import java.util.ArrayList;

import komorebi.projsoul.script.execute.Execution;
import komorebi.projsoul.script.execute.LoopableExecution;

public class ScriptDatabase {

  private static final File SCRIPTS_FOLDER = 
      new File("res/scripts/");
  
  private static ArrayList<Script> scripts = 
      new ArrayList<Script>();
  
  public static void loadScripts()
  {
    ArrayList<File> files = ClassUtils.getAllFilesInFolder(
        SCRIPTS_FOLDER);
    
    for (File txtFile: files)
    {
      System.out.println("Loading " + txtFile.getName());
      
      Script script = new Script(txtFile);
      ScriptDatabase.addScript(script);
    }
  }
  
  private static void addScript(Script script)
  {
    scripts.add(script);
  }
  
  public static boolean scriptExists(String name)
  {
    for (Script script: scripts)
    {
      if (script.getName().equals(name))
        return true;
    }
    
    return false;
  }
  
  private static Script getScript(String name)
  {
    for (Script script: scripts)
    {
      if (script.getName().equals(name))
        return script;
    }
    
    throw new RuntimeException("HANDLE THIS: No such script as " 
        + name);
  }
  
  public static Execution newExecution(String name)
  {
    if (scriptExists(name)) {
      Script script = getScript(name);
      Execution execution = new Execution(script.main());
      return execution;
    } 
   
    throw new RuntimeException("HANDLE THIS: No such script as " + name);
  }
  
  public static LoopableExecution newLoopable(String name)
  {
    if (scriptExists(name)) {
      Script script = getScript(name);
      LoopableExecution execution = new LoopableExecution(script.main());
      return execution;
    } 
   
    throw new RuntimeException("HANDLE THIS: No such script as " + name);
  }
}
