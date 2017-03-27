package komorebi.projsoul.script.read;

public class Request {

  private static final class NullRequest extends Request {
  }
  
  private static NullRequest nullRequest = new NullRequest();
  
  public static NullRequest nullRequest()
  {
    return nullRequest;
  }
  

}
