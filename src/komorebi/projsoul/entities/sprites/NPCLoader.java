/**
 * NPCLoader.java   Jun 1, 2017, 9:25:01 AM
 */
package komorebi.projsoul.entities.sprites;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Loads NPCs from a compressed spritesheet
 *
 * @author Aaron Roy
 */
public class NPCLoader {
  public static final HashMap<String, Sprite> SPRITES = new HashMap<>();
  
  public static void initialize(){
    try {
      BufferedReader reader = new BufferedReader(
          new FileReader(new File("res/compressed_spritesheet.txt")));
      
      String str;

      while ((str = reader.readLine()) != null) {
        String[] split = str.split(" ");
        
        int tx = Integer.parseInt(split[1]);
        int ty = Integer.parseInt(split[2]);
        int sx = Integer.parseInt(split[3]);
        int sy = Integer.parseInt(split[4]);
        
        SPRITES.put(split[0], new Sprite(tx, ty, sx, sy));
        
//        System.out.println("Loaded " + split[0]);
      }
      
      reader.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
}
