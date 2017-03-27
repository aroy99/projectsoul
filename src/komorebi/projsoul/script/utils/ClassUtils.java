package komorebi.projsoul.script.utils;

import java.io.File;
import java.util.ArrayList;

public class ClassUtils {

  public static ArrayList<Class<?>> getAllClassesDirectlyInPackage(
      File packageName)
  {
    ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
   
    for (File file: packageName.listFiles())
    {
      if (!file.isDirectory())
      {
        try {
          classes.add(Class.forName(getQualifiedPackageNameOf(
              file)));
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
      }
    }
    
    return classes;
  }
 
  
  public static ArrayList<File> getAllFilesInFolder(File folder)
  {
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
  
  private static void addClassesWithinPackageTo(File packageName, 
      ArrayList<Class<?>> classes)
  {
    if (packageName.isDirectory())
    {
      for (File file: packageName.listFiles())
      {
        addClassesWithinPackageTo(file, classes);
      }
    } else
    {
      try {
        classes.add(Class.forName(getQualifiedPackageNameOf(
            packageName)));
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
  
  private static String getQualifiedPackageNameOf(File packageName)
  {
    String path = packageName.getAbsolutePath();
    return path.substring(path.indexOf("src") + 4).replace("\\", ".")
        .replace(".java", "");
  }
  
}
