package komorebi.projsoul.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Tileset {
  
  private static final int HEIGHT = 16, WIDTH = 8;
  
  private int[][] tiles;
  private String file;
  
  public Tileset(String file)
  {
    this.file = file;
    
    tiles = new int[HEIGHT][WIDTH];
    
    try {
      BufferedReader read = new BufferedReader(
          new FileReader(new File("res/tilesets/" + file)));
      String str;
      int row = 0;

      while ((str = read.readLine()) != null) {
        String[] arr = str.split(" ");
        
        for (int i = 0; i < WIDTH; i++)
        {
          tiles[HEIGHT-row-1][i] = Integer.parseInt(arr[i]);
        }
        
        row++;
      }
      
      read.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  
  public String toString()
  {
    String ret = "";
    
    for (int[] row: tiles)
    {
      for (int i: row)
      {
        ret+=(i + " ");
      }
      
      ret+="\n";
    }
    
    return ret;
  }
  
  public int[][] tiles()
  {
    return tiles;
  }
  
  public String getFilePath()
  {
    return file;
  }
}
