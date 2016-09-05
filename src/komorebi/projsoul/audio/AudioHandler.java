/**
 * AudioHandler.java    Apr 27, 2016, 8:29:27 PM
 */

package komorebi.projsoul.audio;

/**
 * Handles the playing, stopping, and looping of music and sfx
 * 
 * @author Aaron Roy 
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
    currentSong.stop();
  }
  
  public static void play(Song song, boolean loop)
  {
    currentSong = song;
    song.play(loop);
  }
  
  public static void play(Song song)
  {
    currentSong = song;
    song.play(true);
    
    //TODO Debug
    System.out.println("Play " + currentSong);
  }

}
