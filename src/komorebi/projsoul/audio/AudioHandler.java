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
  
  /**
   * Stops the current song from playing
   */
  public static void stop()
  {
    Song.stop();
  }
  
  /**
   * Plays a song, indicating whether it is a loopable song

   * @param song The song to be played
   * @param loop Whether the song should be played on loop
   */
  public static void play(Song song, boolean loop)
  {
    currentSong = song;
    song.play(loop);
  }
  
  /**
   * Plays a song, loops it to the beginning at the end

   * @param song The song to be played
   */
  public static void play(Song song)
  {
    currentSong = song;
    song.play(true);
    
  }

}
