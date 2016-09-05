/**
 * InstructionList.java  Jun 12, 2016, 11:57:16 AM
 */
package komorebi.projsoul.script;

import java.util.ArrayList;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.NPCType;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class InstructionList {

  private String branchName;

  ArrayList<Task> instructions;


  private int instructionIndex;

  /**
   * Creates an instruction list object
   * @param s The branch name of the instruction list
   */
  public InstructionList(String s)
  {

    branchName = s;
    instructionIndex = 0;

    instructions = new ArrayList<Task>();

  }

  /**
   * Adds a task to the end of the NPC's queue
   * @param task The instruction for the NPC
   */
  public void add(Task task)
  {
    instructions.add(task);
  }


  public ArrayList<Task> getInstructions()
  {
    return instructions;
  }

 

  public void setBranchName(String s)
  {
    branchName = s;
  }

  public String getBranchName()
  {
    return branchName;
  }

  public void setInstructionIndex(int num)
  {
    instructionIndex = num;
  }

  public int getInstructionIndex()
  {
    return instructionIndex;
  }


}
