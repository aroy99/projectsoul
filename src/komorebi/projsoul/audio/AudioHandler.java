/**
 * AudioHandler.java    Apr 27, 2016, 8:29:27 PM
 */

package komorebi.projsoul.audio;

/**
 * Handles the playing, stopping, and looping of music and sfx
 * 
 * @author Aaron Roy 
 * @author Andrew Faulkenberry
 */
public class AudioHandler {

  private static Song currentSong;

  /**
   *  Initializes the audio
   */
  public static void init() {
    //play(Song.NORTH_FAITH);
  }
  
  public static void stop()
  {
    Song.stop();
  }
  
  /**
   * Plays a song, allowing the option to loop or not
   * 
   * @param song The song to play
   * @param loop Whether to loop or not
   */
  public static void play(Song song, boolean loop)
  {
    currentSong = song;
    song.play(loop);
  }
  
  /**
   * Plays a song that automatically loops
   * 
   * @param song The song to play
   */
  public static void play(Song song)
  {
    currentSong = song;
    song.play(true);
    
    //DEBUG Print song
    System.out.println("Play " + currentSong);
  }

}
