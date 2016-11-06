package komorebi.projsoul.entities;

import java.awt.Rectangle;

import komorebi.projsoul.engine.Main;
import komorebi.projsoul.script.EarthboundFont;
import komorebi.projsoul.script.SignHandler;

public class SignPost extends Entity {

  SignHandler text;
  public boolean shown;
  String message;
  
  private Rectangle[] surround = new Rectangle[4];
  
  public SignPost(float x, float y, String message) {
    super(x*16, y*16, 16, 16);
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
    //text.write(message, 20, 58, new EarthboundFont(1));
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
  
  @Override
  public void render() {
    if (shown) text.render();
  }
}
