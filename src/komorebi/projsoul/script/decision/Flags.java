package komorebi.projsoul.script.decision;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Flags {

  private static final File FLAG_DECLARATION_FILE = 
      new File("res/flags/flags.txt");
  
  public static ArrayList<Flag> flags;
  
  public static void loadFlags()
  {
    flags = new ArrayList<Flag>();
    readFromFile();
  }
  
  private static void readFromFile()
  {
    try {
      BufferedReader read = new BufferedReader(new FileReader(
          FLAG_DECLARATION_FILE));
      
      String flag;
      
      while ((flag = read.readLine()) != null)
      {
        flags.add(new Flag(flag));
      }
      
      read.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static boolean flagExists(String identifier)
  {    
    for (Flag flag: flags)
    {
      if (flag.getIdentifier().equals(identifier))
        return true;
    }
    
    return false;
  }
  
  public static Flag getFlagWithIdentifier(String identifier)
  {

    for (Flag flag: flags)
    {
      if (flag.getIdentifier().equals(identifier))
        return flag;
    }
    
    throw new RuntimeException("Pre-condition unmet: no such flag as "
        + identifier);
  }
}
