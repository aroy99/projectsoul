/**
 * InstructionList.java  Jun 12, 2016, 11:57:16 AM
 */
package komorebi.projsoul.script.read;

import java.util.ArrayList;
import java.util.Iterator;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.commands.abstracts.Command;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class Branch implements Iterable<Command> {

  private String name;
  private ArrayList<Command> tasks;
  
  public Branch(String name)
  {
    this.name = name;
    tasks = new ArrayList<Command>();
  }
  
  public void addTask(Command t)
  {
    tasks.add(t);
  }
  
  public String getName()
  {
    return name;
  }

  @Override
  public Iterator<Command> iterator() {
    return tasks.iterator();
  }
  
  public int size()
  {
    return tasks.size();
  }
}
