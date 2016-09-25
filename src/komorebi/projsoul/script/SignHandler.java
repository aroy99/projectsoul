package komorebi.projsoul.script;

import komorebi.projsoul.entities.SignPost;

public class SignHandler extends SpeechHandler {

  
  private SignPost sign;
  
  public SignHandler(SignPost sign) {
    super();
    this.sign = sign;
  }
  
  public void disengage()
  {
    sign.disengage();
  }

}
