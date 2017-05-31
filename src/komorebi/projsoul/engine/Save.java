package komorebi.projsoul.engine;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;




import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import komorebi.projsoul.entities.player.Bruno;
import komorebi.projsoul.entities.player.Caspian;
import komorebi.projsoul.entities.player.Flannery;
import komorebi.projsoul.entities.player.Sierra;
import komorebi.projsoul.map.Map;

public class Save {

	private static String gamePath;  //Path to save the map by default
	  private static String gameName;  //Name to save the map by default
	  static boolean saved = true;
	  

	  public static boolean saveGame(){
		  PrintWriter gameWriter;
	  
		  try {
		  	if(gamePath.substring(gamePath.length()-4).equals(".kom")){
		  		gameWriter = new PrintWriter(gamePath, "UTF-8");
		  	}else{
		  		gameWriter = new PrintWriter(gamePath + ".kom", "UTF-8");
		  	}
		  	
		  	String lastUsedPlayer = Map.getPlayer().getCharacter().toString();
		  	gameWriter.println("Current Character: "+ lastUsedPlayer);
		  	//gameWriter.println("Available Characters: " + Capsian.dead + Flannery.dead + Sierra.dead + Bruno.dead);
		  	gameWriter.println("Current Coordinates: " + Map.getPlayer().getX() + "," + Map.getPlayer().getY());
		  	
		  	gameWriter.println("Caspian:" + " Level: " + Caspian.level + " XP: " + Caspian.xp +" NextLevelUp: " + Caspian.nextLevelUp);
		  	gameWriter.println("Flannery:" + " Level: " + Flannery.level + " XP: " + Flannery.xp +" NextLevelUp: " + Flannery.nextLevelUp);
		  	gameWriter.println("Sierra:" + " Level: " + Sierra.level + " XP: " + Sierra.xp +" NextLevelUp: " + Sierra.nextLevelUp);
		  	gameWriter.println("Bruno:" + " Level: " + Bruno.level + " XP: " + Bruno.xp +" NextLevelUp: " + Bruno.nextLevelUp);
		  	
		  	
		  saved = true;	
		  gameWriter.close();
		  
		  return true;
	    } catch (FileNotFoundException | UnsupportedEncodingException e) {
	      e.printStackTrace();
	      return false;
	    }

	  }

public static boolean newSaveGame(){
	JFileChooser gameChooser = new JFileChooser("res/koms/"){
	
		/**
	       * I don't know what this does, but Aaron says it does something.
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
	      FileNameExtensionFilter filter = new FileNameExtensionFilter("Game Files", "game");
	      gameChooser.setFileFilter(filter);
	      gameChooser.setFileSelectionMode(JFileChooser.FILES_ONLY );
	      gameChooser.setDialogTitle("Enter the name of your save file");
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
}

