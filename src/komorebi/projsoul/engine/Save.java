package komorebi.projsoul.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.IOException;


import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import komorebi.projsoul.entities.player.Bruno;
import komorebi.projsoul.entities.player.Caspian;
import komorebi.projsoul.entities.player.Flannery;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.entities.player.Sierra;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.map.MapHandler;
import komorebi.projsoul.states.Game;
import komorebi.projsoul.states.SaveList;
import komorebi.projsoul.engine.Main;
import komorebi.projsoul.entities.Entity;
import komorebi.projsoul.gameplay.Camera;

public class Save {
  //public static int scale;
  private BufferedReader saveReader;
  private static String gamePath;  //Path to save the map by default
  private static String gameName;  //Name to save the map by default
  static boolean saved = true;
  public String striX = ""; //tells Map starting X coordinates
  public String striY = ""; //tells Map starting Y coordinates
  public String chara = ""; //tells Map which character to load
  boolean cat = true; //used it ensure things are only running once

  //public static int numberOfSaves; Used to calculate which save is most recent

  public boolean isNumeric(String s) {  
    return s.matches("[-+]?\\d*\\.?\\d+");  
  }  
  //builds the game's save file
  public static boolean saveGame(){
    PrintWriter gameWriter;

    try {
      if(gamePath.substring(gamePath.length()-4).equals(".kom")){
        gameWriter = new PrintWriter(gamePath, "UTF-8");
      }else{
        gameWriter = new PrintWriter(gamePath + ".kom", "UTF-8");
      }

      //Scales Game
      gameWriter.println("#The scale of the game, as an integer, resolution is 224x256, "
          + "so you can scale it by changing the number below.");
      gameWriter.println(Main.scale);

      //Current Map of game
      gameWriter.println("#The map the game is on, no quotes");
      gameWriter.println(Game.testLoc);

      //Current Coordinates of character
      gameWriter.println("#Curr Coor:");
      gameWriter.println(Map.getPlayer().getX() + "," + Map.getPlayer().getY());

      //Current Character being shown
      gameWriter.println("#Curr Char:");
      gameWriter.println(MapHandler.getPlayer().getCharacter());

      /* 		Dead/Unplayable Characters
		  	gameWriter.println("#Available Characters:");
		  	gameWriter.println(Caspian.dead + Flannery.dead + Sierra.dead + Bruno.dead);
       */

      //Character Data/Stats
      gameWriter.println("#Char Data:");
      gameWriter.println("CSPN-Level:" + Caspian.level + " XP:" + Caspian.xp +" NextLevelUp:" + Caspian.nextLevelUp);
      gameWriter.println("FLNY-Level:" + Flannery.level + " XP:" + Flannery.xp +" NextLevelUp:" + Flannery.nextLevelUp);
      gameWriter.println("SIRA-Level:" + Sierra.level + " XP:" + Sierra.xp +" NextLevelUp:" + Sierra.nextLevelUp);
      gameWriter.println("BRNO-Level:" + Bruno.level + " XP:" + Bruno.xp +" NextLevelUp:" + Bruno.nextLevelUp);

      saved = true;	
      gameWriter.close();

      return true;
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      e.printStackTrace();
      return false;
    }

  }
  /*
	  public static void overWriteSettingsS(){
		  PrintWriter settingsSWriter;

		  try{

			  settingsSWriter.println("The amount of times this game has been saved.");
			  settingsSWriter.println(numberOfSaves);
		  }

	  }*/

  public static boolean newSaveGame(){
    JFileChooser gameChooser = new JFileChooser("res/saves/"){

      /**
       * I don't know what this does, but it does something.
       */
      private static final long serialVersionUID = 3881189161552826430L;

      @SuppressWarnings("unused")
      public void approveGameSelections(){
        File f = getSelectedFile();
        if (f.exists() && getDialogType() == SAVE_DIALOG){
          int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?",
              "Existing file",JOptionPane.YES_NO_CANCEL_OPTION);

          switch(result){
            case JOptionPane.YES_OPTION:
              super.approveSelection();
              return;
            case JOptionPane.NO_OPTION:
              return;
            case JOptionPane.CLOSED_OPTION:
              return;
            case JOptionPane.CANCEL_OPTION:
              cancelSelection();
              return;
            default:
              return;
          }
        }
        super.approveSelection();
      }

    };
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Game Files (.kom)", "kom");
    gameChooser.setFileFilter(filter);
    gameChooser.setFileSelectionMode(JFileChooser.FILES_ONLY );
    gameChooser.setDialogTitle("Enter a new name or overwrite existing files");
    int returnee = gameChooser.showSaveDialog(null);

    KeyHandler.reloadKeyboard();

    if (returnee == JFileChooser.APPROVE_OPTION ){
      gamePath = gameChooser.getSelectedFile().getAbsolutePath();
      setGameName(gameChooser.getSelectedFile().getAbsolutePath());  
      return saveGame();
    }
    return false;
  }

  public static String getGameName() {
    return gameName;
  }

  public static void setGameName(String gameName) {
    Save.gameName = gameName;
  }

  //functions similar to Map @komorebi.projsoul.map.Map
  // Tasked to decipher the save File's actual data
  public Save(String key){
    try {
      saveReader = new BufferedReader(new FileReader
          (new File("res/saves/"+key)));
      String stri;

      while ((stri = saveReader.readLine()) != null) 
      {
        if(stri.equals("") || stri.charAt(0) == '#')
        {
          continue;
        }
        //	Gets Scale 
        if(Main.scale==1 && isNumeric(stri)){
          Main.scale=Integer.parseInt(stri); 
        }
        //	Gets Map
        else if(Game.testLoc == null && stri.contains(".")){
          Game.testLoc = stri;
        }
        //	Gets Start Coordinates
        else if(stri.contains(",")){
          findCoordinates(stri);
        }
        //	Gets Starting Character
        else if(chara == "" && !isNumeric(stri) && !stri.contains(".")){
          chara = stri;
          System.out.println(chara);
        }
        //	Gets char data
        else if(stri.startsWith("CSPN")){
          splitCharData(stri);
        }
        else if(stri.startsWith("FLNY")){
          splitCharData(stri);
        }
        else if(stri.startsWith("SIRA")){
          splitCharData(stri);
        }
        else if(stri.startsWith("BRNO")){
          splitCharData(stri);
        }
      }
    }catch(IOException | NumberFormatException e) {
      System.out.println("your Savereader failed");
      e.printStackTrace();
      Main.scale = 1;
    }
  }

  public void splitCharData(String str){
    String[] parts = str.split(" "); 

    String[] segments = parts[0].split(":");
    String[] segmentss = parts[1].split(":");
    String[] segmentsss = parts[2].split(":");
    if (str.startsWith("CSPN")){
      Caspian.level= Integer.parseInt(segments[1]);
      Caspian.xp= Integer.parseInt(segmentss[1]);
      Caspian.nextLevelUp= Integer.parseInt(segmentsss[1]);
    }
    else if (str.startsWith("FLNY")){
      Flannery.level= Integer.parseInt(segments[1]);
      Flannery.xp= Integer.parseInt(segmentss[1]);
      Flannery.nextLevelUp= Integer.parseInt(segmentsss[1]);
    }
    else if (str.startsWith("SIRA")){
      Sierra.level= Integer.parseInt(segments[1]);
      Sierra.xp= Integer.parseInt(segmentss[1]);
      Sierra.nextLevelUp= Integer.parseInt(segmentsss[1]);
    }
    else if (str.startsWith("BRNO")){
      Bruno.level= Integer.parseInt(segments[1]);
      Bruno.xp= Integer.parseInt(segmentss[1]);
      Bruno.nextLevelUp= Integer.parseInt(segmentsss[1]);
    }
  }

  public BufferedReader getsaveReader() {
    return saveReader;
  }

  public void setsaveReader(BufferedReader saveReader) {
    this.saveReader = saveReader;
  }

  public void findCoordinates(String s){
    int counter=0;
    while (counter<s.indexOf(',')){
      striX = striX +""+ s.charAt(counter);
      counter++;}
    counter=s.indexOf(',')+1;
    while (counter<s.length()){
      striY = striY +""+ s.charAt(counter);
      counter++;}
  }

}

