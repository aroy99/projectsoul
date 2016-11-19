package komorebi.projsoul.entities;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.Main;
import komorebi.projsoul.script.EarthboundFont;
import komorebi.projsoul.map.EditorMap;
import komorebi.projsoul.map.EditorMap.Modes;
import komorebi.projsoul.script.SignHandler;

import java.awt.Rectangle;

public class SignPost extends Entity {

  SignHandler text;
  public boolean shown;
  String message;

  private Rectangle[] surround = new Rectangle[4];

  public SignPost(float x, float y, String message) {
    super(x, y, 16, 16);
    text = new SignHandler(this);

    surround[0] = new Rectangle((int) this.x, (int) this.y+16, 16, 16);
    surround[1] = new Rectangle((int) this.x + 16, (int) this.y, 16, 16);
    surround[2] = new Rectangle((int) this.x, (int) this.y - 16, 16, 16);
    surround[3] = new Rectangle((int) this.x - 16, (int) this.y, 16, 16);

    this.message = message;

  }



  public void show()
  {
    shown = true;
    text.write(message, 20, 58, new EarthboundFont(1));
    Main.getGame().setSpeaker(text);
  }


  public boolean doneAsking()
  {
    return text.alreadyAsked();
  }

  public void disengage() {
    shown = false;
  }

  public boolean isApproached(Rectangle clyde, Face direction)
  {
    return ((surround[0].intersects(clyde) && direction == Face.DOWN) ||
        (surround[1].intersects(clyde) && direction == Face.LEFT) ||
        (surround[2].intersects(clyde) && direction == Face.UP) ||
        (surround[3].intersects(clyde) && direction == Face.RIGHT)) && !shown;
  }

  @Override
  public void update() {
    // TODO Auto-generated method stub

  }
  
  public void setText(String message){
    this.message = message;
  }
  
  public String getText(){
    return message;
  }
  
    @Override
  public void render() {
    if (shown){
      text.render();
    }
    
    if(EditorMap.getMode() == Modes.EVENT){
      Draw.rect(x, y, sx, sy, 96, 0, 2);
    }
  
  }
}
