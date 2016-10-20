/**
 * Song.java    Jul 22, 2016, 12:37:04 PM
 */
package komorebi.projsoul.audio;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author Andrew Faulkenberry
 */
public enum Song {
  NONE("silence"),
  TRAVELLING("A_Travelling_Theme"),
  BEACH("Beach_Town"),
  EARTH("Earth_Dungeon"),
  NIGHT("Night_in_Arial"),
  ROBBERS("Robbers"),
  SIERRA("Sierra's_Theme"),
  BEST_FRIENDS("The_Demonstration"),
  FOREST("The_Forest"),
  CHAOS("We_Live_in_Chaos"),
  FLOOD("Week_1_Music");
  
  private String path;
  private Audio music;
    
  private Song(String s)
  {
    this.path = s;
    try {
      music = AudioLoader.getStreamingAudio("OGG", 
          ResourceLoader.getResource("res/music/" + path + ".ogg"));
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
  
  public static void stop()
  { 
    NONE.music.playAsMusic(1.0f, 1.0f, false);
    NONE.music.stop();
  }
  
  public static Song get(String s)
  {
    for (Song song: Song.values())
    {
      if (song.getPath().equals(s)) return song;
    }
    
    return null;
  }
  
  /**
   * Returns the song that matches the inputted name
   * 
   * @param str The song name to search for
   * @return The Song if found, null if not
   */
  public static Song getSong(String str){
    for(Song s: values()){
      if(s.toString().equals(str)){
        return s;
      }
    }
    return null;
  }
  
  @Override
  public String toString(){
    switch(this){
      case NONE:           return "None";
      case BEACH:          return "The Beach";
      case BEST_FRIENDS:   return "Best Friends";
      case CHAOS:          return "We Live in Chaos";
      case EARTH:          return "Earth Dungeon";
      case FLOOD:          return "The Flood";
      case FOREST:         return "The Forest";
      case NIGHT:          return "A Night Somewhere";
      case ROBBERS:        return "Robbers!";
      case SIERRA:         return "Sierra's Theme";
      case TRAVELLING:     return "A Travelling Song";
      default:             return "Unknown Song";
      
    }
  }
}
