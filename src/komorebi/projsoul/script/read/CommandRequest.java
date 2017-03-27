package komorebi.projsoul.script.read;

public class CommandRequest extends Request {

  private Branch branch;
  private String branchName;
  
  public CommandRequest(Branch branch)
  {    
    this.branch = branch;
    this.branchName = branch.getName();
  }
  
  public Branch getBranchReference()
  {
    return branch;
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
