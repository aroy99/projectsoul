package komorebi.projsoul.script.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ClassUtils {

  public static ArrayList<File> getAllFilesInFolder(File folder) {
    ArrayList<File> files = new ArrayList<File>();
    addFile(folder, files);

    return files;
  }

  private static void addFile(File file, ArrayList<File> list)
  {
    if (file.isDirectory())
      for (File subfile: file.listFiles())
        addFile(subfile, list);
    else
      list.add(file);
  }

  public static boolean directoryContainsFile(File directory, String fileName)
  {
    if (!directory.isDirectory())
      throw new RuntimeException(directory.getPath() + " is not a directory");

    File[] inFolder = directory.listFiles(new FileFilter()
    {
      public boolean accept(File arg0) {
        return arg0.isFile();
      }
    });
    
    for (File file: inFolder)
    {
      if (file.getName().equals(fileName))
        return true;
    }
    
    return false;
  }
  
  public static File findFile(File directory, String fileName) 
      throws FileNotFoundException
  {
    if (!directory.isDirectory())
      throw new RuntimeException(directory.getPath() + " is not a directory");

    File[] inFolder = directory.listFiles(new FileFilter()
    {
      public boolean accept(File arg0) {
        return arg0.isFile();
      }
    });
    
    for (File file: inFolder)
    {
      if (file.getName().equals(fileName))
        return file;
    }
    
    throw new FileNotFoundException(fileName + " cannot be found within the directory "
        + directory.getPath());
  }

}
