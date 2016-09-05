/**
 * Encryptor.java		Jul 19, 2016, 2:41:09 PM
 */
package komorebi.projsoul.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class Encryptor {
  
  protected static final BigInteger e = new BigInteger("65537");
  protected static final BigInteger d = new BigInteger("346737473");
  protected static final BigInteger n = new BigInteger("1376213381"); 

  protected static final BigInteger one = new BigInteger("1");

  public static void main(String[] args)
  {
    pack(randomArray(128,128), "C:\\Users\\Andrew\\workspace\\clydes\\res\\maps\\test1.mapx");
    unpack("C:\\Users\\Andrew\\workspace\\clydes\\res\\maps\\test1.mapx");
  }
  
  private static BigInteger encrypt(BigInteger message)
  {

    return message.modPow(e, n);

  }

  private static BigInteger decrypt(BigInteger code)
  {
    return code.modPow(d, n);   
  }

  public static void code(int[][] tiles, String key)
  {

    try {
      PrintWriter writer = new PrintWriter(key, "UTF-8");

      String s = "";
      
      if (tiles.length<10) s = s + " ";
      if (tiles.length<100)s = s + " ";
      s = s + tiles.length;
      
      if (tiles[0].length<10) s = s + " ";
      if (tiles[0].length<100) s = s + " ";
      s = s + tiles[0].length;
      
      writer.println(encrypt(new BigInteger(s)));
      s = "1";
      
      int counter = 0;
      
      for (int[] i: tiles)
      {
        for (int j: i)
        {
          if (j<10) s = s + "00";
          else if (j<100) s = s + "0";
          s = s + j;
          counter++;

          if (counter>=2)
          {
            writer.println(encrypt(new BigInteger(s)));
            s = "1";
            counter=0;
          }
        }
        
      }

      if (counter!=0) writer.println(encrypt(new BigInteger(s)));
      writer.close();
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static int[][] decode(String key)
  {
    try {
      BufferedReader in = new BufferedReader(
          new FileReader(new File(key)));
      
      String bounds = decrypt(new BigInteger(in.readLine())).toString();
      
      int[][] tiles = new int[Integer.parseInt(bounds.substring(0, 3))][Integer.parseInt(bounds.substring(3, 6))];
      
      String str;
      
      int i=0, j=0;
      
      while ((str=in.readLine())!=null)
      {
        BigInteger num = new BigInteger(str);
        num = decrypt(num);
        String m = num.toString();
        for (int x=1; x<=4; x+=3)
        {
          try
          {
            tiles[i][j] = Integer.parseInt(m.substring(x, x+3));
          //TODO Debug
            System.out.println(i + ", " + j+ ": "+tiles[i][j]);
            
            j++;
            if (j>=tiles[0].length)
            {
              j=0;
              i++;
            }
          } catch (StringIndexOutOfBoundsException e)
          {
            break;
          }
          
        }
      }
      
      in.close();
      
      return tiles;

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return null;
  }
  
  public static void zip(File file, String zip)
  {
    try {
      FileOutputStream fileOut = new FileOutputStream(zip);
      ZipOutputStream zipOut = new ZipOutputStream(fileOut);
      
      ZipEntry entry = new ZipEntry(file.getName());
      zipOut.putNextEntry(entry);
      
      FileInputStream fileIn = new FileInputStream(file);
      byte[] buf = new byte[1024];
      int bytesRead;
      
      while ((bytesRead=fileIn.read(buf)) > 0)
      {
        zipOut.write(buf, 0, bytesRead);
      }
      
      zipOut.closeEntry();
      zipOut.close();
      fileOut.close();
      fileIn.close();
      
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  
  public static void unzip(String zip, String newLocation)
  {
    
    byte[] buffer = new byte[1024];
    
    try {
      
      ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zip));
      ZipEntry entry = zipIn.getNextEntry();
      
      FileOutputStream fileOut = new FileOutputStream(newLocation);    
      
      while (entry!=null)
      {
        int len;
        while ((len = zipIn.read(buffer)) > 0) {
          fileOut.write(buffer, 0, len);
        }
        
        fileOut.close();   
        entry = zipIn.getNextEntry();
      }
      
      
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    
  }
  
  public static int[][] randomArray(int length, int width)
  {
    int[][] array = new int[length][width];
    Random random = new Random();
    
    for (int i=0; i<length; i++)
    {
      for (int j=0; j<width; j++)
      {
        array[i][j] = random.nextInt(1000);
        //System.out.println(i + ", " + j+ ": "+array[i][j]);
      }
    }
    
    return array;
  }
  
  private static void delete(String file)
  {
    try {
      Files.delete(Paths.get(file));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /**
   * Encrypts the data into a .txt file of the same name as the .mapx file,
   * compresses this into a .mapx file and deletes the .txt file
   * @param tiles The array of integers representing a full map of tiles
   * @param file The file path (with file extension .mapx)
   */
  public static void pack(int[][] tiles, String file)
  {
    String volFile = file.replace(".mapx", ".txt");
    
    code(tiles, volFile);
    zip(new File(volFile), file);
    delete(volFile);
    
  }
  
  /**
   * Creates a .txt file into which the data from the .mapx file is decompressed,
   * then decrypts the data and deletes the .txt file
   * @param file The file path of the .mapx file
   * @return An integer array representing the map tiles
   */
  public static int[][] unpack(String file)
  {
    String volFile = file.replace(".mapx", ".txt");
    
    unzip(file, volFile);
    int[][] array = decode(volFile);
    delete(volFile);
    
    return array;
  }
}
