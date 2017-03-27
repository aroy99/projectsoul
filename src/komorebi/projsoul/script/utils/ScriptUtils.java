package komorebi.projsoul.script.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import komorebi.projsoul.script.commands.abstracts.Command;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;
import komorebi.projsoul.script.exceptions.UndefinedKeywordException;

public class ScriptUtils {

  private static HashMap<String, Constructor<? extends Command>> keywords;
  private static final File TASK_PACKAGE = 
      new File("src/komorebi/projsoul/script/commands");

  
  public static boolean keywordExists(String keyword)
  {
    return keywords.containsKey(keyword);
  }
  
  public static Command createNewInstanceWithKeyword(String keyword)
  {
    Constructor<?> constructor = keywords.get(keyword);
    try {
      return (Command) constructor.newInstance();
    } catch (InstantiationException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    }
    
    throw new RuntimeException("Invalid keyword");
  }

  
  public static void loadTaskConstructors() throws 
    UndefinedKeywordException,
    UndefinedConstructorException
  {
    keywords = new HashMap<String, Constructor<? extends Command>>();
    
    for (Class<?> genericClassName: 
      ClassUtils.getAllClassesDirectlyInPackage(TASK_PACKAGE))
    {      
      @SuppressWarnings("unchecked")
      Class<? extends Command> className = (Class<? extends Command>) 
          genericClassName;
      
      if (!classDefinesKeyword(className))
      {
        throw new UndefinedKeywordException(className.getSimpleName());
      } else if (!classProvidesConstructorWithoutParameters(className))
      {
        throw new 
          UndefinedConstructorException(className.getSimpleName());
      } else
      {
        addKeywordToHashMap(className);
      }
    }
  }
  
  private static void addKeywordToHashMap(Class<? extends Command> className)
  {    
    try {
      String keyword = (String) className.getMethod("keyword").invoke(null);
      Constructor<? extends Command> constructor = className.getConstructor();
      
      keywords.put(keyword, constructor);
    } catch (IllegalAccessException | IllegalArgumentException
        | InvocationTargetException | NoSuchMethodException
        | SecurityException e) {
      e.printStackTrace();
    }
  }
 
  private static boolean classDefinesKeyword(Class<?> className)
  {
    try
    {
      className.getMethod("keyword");
      return true;
    } catch (NoSuchMethodException e)
    {
      return false;
    }
  }
  
  private static boolean 
    classProvidesConstructorWithoutParameters(Class<?> className)
  {
    try
    {
      className.getConstructor();
      return true;
    } catch (NoSuchMethodException e)
    {
      return false;
    }
  }
  
  public static String getAllKeywords()
  {
    String ret = "";
    
    for (String keyword: keywords.keySet())
    {
      ret += keyword + "\t" + keywords.get(keyword) + "\n";
    }
    
    return ret;
  }
}
