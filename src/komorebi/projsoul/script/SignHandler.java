package komorebi.projsoul.script;

import komorebi.projsoul.entities.SignPost;

public class SignHandler extends SpeechHandler {

  
  private SignPost sign;
  
  public SignHandler(boolean b, SignPost sign) {
    super(b);
    this.sign = sign;
  }
  
  public void disengage()
  {
    sign.disengage();
  }

}
