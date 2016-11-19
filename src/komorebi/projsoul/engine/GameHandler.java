/*
 * GameHandler.java     Apr 27, 2016, 8:28:43 PM
 */

package komorebi.projsoul.engine;

import komorebi.projsoul.states.Game;
import komorebi.projsoul.states.Menu;
import komorebi.projsoul.states.Pause;
import komorebi.projsoul.states.State.States;

/**
 * Updates, renders and gets input depending on the current state
 * 
 * @author Aaron Roy
 * @version 0.0.1.0
 * 
 */
public class GameHandler implements Playable{

  public static States state;

  public static Game game;
  private static Menu menu;
  private static Pause pause;

  /**
   * Creates the GameHandler
   */
  public GameHandler(){
    state = States.GAME;
    game = new Game();
    menu = new Menu();
    pause = new Pause();
  }

  /**
   * @see komorebi.projsoul.engine.Playable#getInput()
   */
  public void getInput() {
    KeyHandler.getInput();

    switch(state){
      case GAME:
        game.getInput();
        break;
      case MENU:
        menu.getInput();
        break;
      case PAUSE:
        pause.getInput();
        break;
      default:
        break;
    }
  }

  /**
   * @see komorebi.projsoul.engine.Playable#update()
   */
  public void update() {
    switch(state){
      case GAME:
        game.update();
        break;
      case MENU:
        menu.update();
        break;
      case PAUSE:
        pause.update();
        break;
      default:
        break;
    }
  }

  /**
   * @see komorebi.projsoul.engine.Playable#render()
   */
  public void render() {
    switch (state) {
      case GAME:
        game.render();
        break;
      case MENU:
        menu.render();
        break;
      case PAUSE:
        game.render();
        pause.render();
        break;
      default:
        break;
    }
  }

  /**
   * Switches to a new state
   * 
   * @param nstate The state to switch to
   */
  public static void switchState(States nstate){
    if (nstate == States.PAUSE)
    {
      pause.reload();
    }
    state = nstate;
  }

}
