/**
 * BranchList.java  Jun 18, 2016, 3:12:01 PM
 */
package komorebi.projsoul.script;

import java.util.ArrayList;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class BranchList extends ArrayList<InstructionList> {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public BranchList()
  {
    super();
  }

  /**
   * Returns the branch with a specified name
   * @param s The string name of the branch to be retrieved
   * @return The branch with string name, s
   */
  public InstructionList getBranch(String s)
  {
    for (InstructionList i: this)
    {
      if (s.equalsIgnoreCase(i.getBranchName()))
      {
        return i;
      }
    }

    return null;
  }

}
