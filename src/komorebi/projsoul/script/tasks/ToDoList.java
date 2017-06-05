package komorebi.projsoul.script.tasks;

import java.util.ArrayList;

public class ToDoList {

  private ArrayList<Task> tasks;

  public ToDoList()
  {
    tasks = new ArrayList<Task>();
  }
  
  public void add(TimedTask task)
  {
    tasks.add(task);
  }
  
  public Task next()
  {
    return tasks.get(0);
  }
  
  public boolean hasTasks()
  {
    return !tasks.isEmpty();
  }
  
  public void clean()
  {    
    sort();
    collectGarbage();
        
  }
  
  private void collectGarbage()
  {
    for (int i = 0; i < tasks.size(); i++)
    {
      Task task = tasks.get(0);
      if (task != null && task.isFinished())
      {
        tasks.remove(0);
      }
    }
  }
  
  private void sort()
  {
    Task highestPrecedence;
    
    for (int i = 0; i < tasks.size() - 1; i++)
    {
      highestPrecedence = tasks.get(i);
      
      for (int j = i + 1; j < tasks.size(); j++)
      {
        if (tasks.get(j).takesPrecedenceOver(highestPrecedence))
        {
          highestPrecedence = tasks.get(j);
        }
      }
      
      int loc = tasks.indexOf(highestPrecedence);
      
      if (i != loc)
        swap(i, loc);
    }
  }
  
  private void swap(int elem1, int elem2)
  {
    Task temp = tasks.get(elem1);
    tasks.set(elem1, tasks.get(elem2));
    tasks.set(elem2, temp);
  }

}
