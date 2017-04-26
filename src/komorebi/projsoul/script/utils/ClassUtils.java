package komorebi.projsoul.script.utils;

import java.io.File;
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

}
