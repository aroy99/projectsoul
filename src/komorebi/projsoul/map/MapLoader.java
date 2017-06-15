/**
 * MapLoader.java     May 11, 2017, 9:08:22 AM
 */
package komorebi.projsoul.map;

import komorebi.projsoul.ai.Location;
import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.audio.Song;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.entities.SignPost;
import komorebi.projsoul.entities.enemy.Chaser;
import komorebi.projsoul.entities.enemy.Dummy;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.entities.enemy.EnemyType;
import komorebi.projsoul.entities.enemy.Shooter;
import komorebi.projsoul.entities.enemy.SmartEnemy;
import komorebi.projsoul.entities.enemy.Tackler;
import komorebi.projsoul.script.utils.AreaScript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Loads maps
 *
 * @author Aaron Roy
 */
public class MapLoader {

  private MapLoader(){}

  public static Map loadMap(String key, int offX, int offY) throws IllegalStateException{
    try {

      //DEBUG Printing maps as they load
      System.out.format("%s, %d, %d\n", key, offX, offY);

      int currOffX = 0; 
      int currOffY = 0;

      BufferedReader reader = new BufferedReader(new FileReader(
          new File("res/maps/" + key)));
      int rows = Integer.parseInt(reader.readLine());
      int cols = Integer.parseInt(reader.readLine());
      
      System.out.format("Rows: %d, Cols: %d\n", rows, cols);


      String title;                 //The in-game name of this map
      Song song;                    //The song this map uses

      //instantiates the necessary arrays and arraylists
      int[][][] tiles = new int[Map.LAYERS][rows][cols];
      boolean[][] collision = new boolean[rows][cols];
      ArrayList<NPC> npcs = new ArrayList<NPC>();
      ArrayList<AreaScript> scripts = new ArrayList<AreaScript>();
      ArrayList<SignPost> signs = new ArrayList<SignPost>();

      HashMap<String, Location> borderLocs = new HashMap<>();

      title = reader.readLine();
      song = Song.getSong(reader.readLine());
      if(song == null){
        song = Song.NONE;
      }
      MapHandler.isOutside = Integer.parseInt(reader.readLine()) == 1?true : false;

      //DEBUG Text
      //      System.out.println("Title: " + title);
      //      System.out.println("Song: " + song);
      //      System.out.println(isOutside);
      AudioHandler.play(song);

      int layer = -1;      
      String read;
      
      while ((read = reader.readLine()) != null)
      {
        if (read.startsWith("#")) {
          layer++;
          
          System.out.println("Incremented because: " + read);
        } else if(read.startsWith("--")){
          System.out.println("Moving to permissions");
          break;
        } else
        {
          String[] split = read.split(" ");
          int i = rows - 1, j = 0;
          
          System.out.println(read);

          for (String term: split)
          {
            if (term.isEmpty()) {
              continue;
            }

            int times;
            if (term.contains("^"))
            {
              times = Integer.valueOf(term.substring(term.indexOf("^") + 1));
              term = term.substring(0, term.indexOf("^"));
            } else {
              times = 1;
            }

            for (int repeat = 0; repeat < times; repeat++)
            {
              tiles[layer][i][j] = Integer.parseInt(term);
              j++;
              if (j >= cols)
              {
                j = 0;
                i--;
              }
            }
          }

        }
      }



      int i = rows - 1, j = 0;

      while((read = reader.readLine()) != null){
             
        if(!Pattern.matches("(\\d+\\^?\\d+ )+", read)){
          System.out.println(read + " didn't match");
          break;
        }

        String[] terms = read.split(" ");

        for (String term: terms)
        {
          int repetitions;

          if (term.contains("^"))
          {
            repetitions = Integer.parseInt(term.substring(
                term.indexOf("^")+1));
            term = term.substring(0, term.indexOf("^"));
          } else
          {
            repetitions = 1;
          }

          for (int rep = 0; rep < repetitions; rep++)
          {
            collision[i][j]= term.charAt(0) == '0'?true : false;

            j++;
            if (j >= cols)
            {
              j = 0;
              i--;
            }

          }
        }
      }

      do {
        if (read == null) {
          break;
        }
        if (read.startsWith("npc")) {
          read = read.replace("npc ", "");

          createNewNPC(npcs, read, currOffX + offX, currOffY + offY);

        } else if (read.startsWith("script")) {
          read = read.replace("script ", "");
          String[] split = read.split(" ");

          int arg0 = Integer.parseInt(split[2]) + currOffX + offX;
          int arg1 = Integer.parseInt(split[1]) + currOffY + offY;

          scripts.add(new AreaScript(split[0], arg0, arg1));
        } else if (read.startsWith("warp")) {
          read = read.replace("warp ", "");
          String[] split = read.split(" ");

          int arg0 = Integer.parseInt(split[2]) + currOffX + offX;
          int arg1 = Integer.parseInt(split[1]) + currOffY + offY;

//          scripts.add(new WarpScript(split[0], arg1, arg0, false));
        } else if (read.startsWith("enemy")) {
          read = read.replace("enemy ", "");

          createNewEnemy(read, currOffX + offX, currOffY + offY);

        } else if (read.startsWith("sign")) {
          read = read.replace("sign ", "");
          String[] split = read.split(" ", 3);

          int arg0 = Integer.parseInt(split[0]) + currOffX + offX;
          int arg1 = Integer.parseInt(split[1]) + currOffY + offY;

          signs.add(new SignPost(arg0 * 16, arg1 * 16, split[2]));

        } else if (read.startsWith("connect")) {
          read = read.replace("connect ", "");
          String[] split = read.split(" ");

          int offsetX = Integer.parseInt(split[2]);
          int offsetY = Integer.parseInt(split[3]);

          borderLocs.put(split[0] + ".map", new Location(offsetX, offsetY));
        }
      } 
      while ((read = reader.readLine()) != null);

      reader.close();

      Map returnee = new Map(tiles, collision, npcs, scripts, signs, title, song);
      returnee.setBorders(borderLocs);
      returnee.setOffset(offX, offY);
      returnee.setAddress(key);

      return returnee;

    } catch (IOException | NumberFormatException e) {
      e.printStackTrace();
    }

    throw new IllegalStateException();
  }

  private static void createNewNPC(ArrayList<NPC> npcs, String s, int offX, int offY) {
    String[] split = s.split(" ");

    int arg0 = Integer.parseInt(split[1]) + offX;
    int arg1 = Integer.parseInt(split[2]) + offY;
    NPC n;
    
    npcs.add(n = new NPC(split[0], arg0 * 16, arg1 * 16,
        NPCType.toEnum(split[3])));

    n.setWalkingScript(split[4].replace("?", " "));
    n.setTalkingScript(split[5].replace("?", " "));

    n.runWalkingScript();
  }




  private static void createNewEnemy(String s, int offX, int offY) {
    String[] split = s.split(" ");

    int arg0 = Integer.parseInt(split[0]) + offX;
    int arg1 = Integer.parseInt(split[1]) + offY;

    Enemy enemy = null;

    switch(split[3]){
      case "none":
        enemy = new Dummy(arg0*16, arg1*16, EnemyType.toEnum(split[2]), 1);
        break;
      case "chaser":
        enemy = new Chaser(arg0*16, arg1*16, EnemyType.toEnum(split[2]),
            Integer.parseInt(split[4]));
        break;
      case "smart":
        enemy = new SmartEnemy(arg0*16, arg1*16, EnemyType.toEnum(split[2]),
            Integer.parseInt(split[4]));
        break;
      case "shooter":
        enemy = new Shooter(arg0*16, arg1*16, EnemyType.toEnum(split[2]),
            Integer.parseInt(split[4]));
        break;
      case "tackler":
        enemy = new Tackler(arg0*16, arg1*16, EnemyType.toEnum(split[2]),
            Integer.parseInt(split[4]));
        break;

      default:
        System.out.format("Enemy %s is invalid\n", s);
        break;
    }

    if(enemy != null){
      MapHandler.addEnemy(enemy);
    }
  }

  /**
   * Finds the npc with the specified name
   * 
   * @param npcs The list to search
   * @param s The name of the NPC
   * @return The NPC if found, null if not
   */
  private static NPC findNPC(ArrayList<NPC> npcs, String s) {

    for (NPC npc : npcs) {
      if (npc != null) {
        if (npc.getName().equals(s)) {
          return npc;
        }
      }
    }

    return null;
  }

}
