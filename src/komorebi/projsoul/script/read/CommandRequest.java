package komorebi.projsoul.script.read;

public class CommandRequest extends Request {

  private Branch branch;
  private String branchName;
  
  private int line;
  
  public CommandRequest(Branch branch, int line)
  {    
    this.branch = branch;
    this.branchName = branch.getName();
    this.line = line;
  }
  
  public Branch getBranchReference()
  {
    return branch;
  }
  
  public int getLine()
  {
    return line;
  }
  
  public String getBranchName()
  {
    return branchName;
  }
  
  public static boolean isInstance(Request r)
  {
    return CommandRequest.class.isInstance(r);
  }
  
}
