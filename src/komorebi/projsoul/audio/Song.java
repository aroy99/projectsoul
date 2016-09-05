/**
 * Song.java		Jul 22, 2016, 12:37:04 PM
 */
package komorebi.projsoul.audio;

import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public enum Song {
  CLYDE_THEME("clyde_theme.ogg"),
  NORTH_FAITH("north_faith.ogg"),
  MS_MUGELWORTH("ms_mugelworth.ogg"),
  GOOD_EATS("good_eats.ogg"),
  POLICE_STATION("police_station.ogg");
  
  
  private String path;
  private Audio music;
  
  private Song(String s)
  {
    this.path = s;
    try {
      music = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource("res/music/" + path));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public String getPath()
  {
    return path;
  }
  
  public static ArrayList<Song> getAllSongs()
  {
    ArrayList<Song> songs = new ArrayList<Song>();
    for (Song s: Song.values())
    {
      songs.add(s);
    }
    
    return songs;
  }
  
  public void play(boolean loop)
  {
    music.playAsMusic(1.0f, 1.0f, loop);
  }
  
  public void stop()
  {
    music.stop();
  }
  
  public static Song get(String s)
  {
    for (Song song: Song.values())
    {
      if (song.getPath().equals(s)) return song;
    }
    
    return null;
  }
}
