/**
 * Script.java  Jul 8, 2016, 12:11:04 PM
 */
package komorebi.projsoul.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.script.Task.TaskWithBoolean;
import komorebi.projsoul.script.Task.TaskWithBranch;
import komorebi.projsoul.script.Task.TaskWithBranches;
import komorebi.projsoul.script.Task.TaskWithLocation;
import komorebi.projsoul.script.Task.TaskWithNumber;
import komorebi.projsoul.script.Task.TaskWithNumberAndLocation;
import komorebi.projsoul.script.Task.TaskWithString;
import komorebi.projsoul.script.Task.TaskWithStringArray;
import komorebi.projsoul.script.Task.TaskWithTask;
import komorebi.projsoul.states.Game;

/**
 * 
 * 
 * @author Andrew Faulkenberry
 */
public abstract class Script {

  public int abortionIndex;
  private boolean syntaxError;

  public String script;
  public Execution execution;

  private boolean isRunning = false;
  public boolean isInterrupted;

  private BufferedReader read;

  private InstructionList currentBranch;
  private ArrayList<TaskWithBranch> branches;
  public NPC npc;

  public abstract void abort();
  /**
   * Interprets a given script and has a given NPC execute them
   */
  public void read()
  {

    InstructionList ex = new InstructionList("Main");
    branches = new ArrayList<TaskWithBranch>();

    setCurrentBranch(ex);

    try {
      read = new BufferedReader(
          new FileReader(new File("res/scripts/"+script+".txt")));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }

    String s;
    Task task;
    int line = 1;

    try {
      while ((s = read.readLine()) != null) {
        s=s.trim();
        if ((task=interpret(s, line)) != null)
        {
          currentBranch.add(task);
        }
        line++;
      }

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    execution = new Execution(npc, ex);
  }

  /**
   * Converts one line of a script into a Task
   * @param s The line of script to be interpreted
   * @param line The line number
   * @return The appropriate task, or null if an error is thrown
   */
  public Task interpret(String s, int line)
  {
    if (s.startsWith("walk"))
    {
      s=s.replace("walk ", "");

      String[] dir = s.split(" ");
      for (int i=0; i < dir.length; i++)
      {
        if (i+1 == dir.length)
        {
          if (dir[i].equals("left"))
          {
            return new Task(Instructions.WALK_LEFT);
          } else if (dir[i].equals("right"))
          {
            return new Task(Instructions.WALK_RIGHT);
          } else if (dir[i].equals("down"))
          {
            return new Task(Instructions.WALK_DOWN);
          } else if (dir[i].equals("up"))
          {
            return new Task(Instructions.WALK_UP);
          } else
          {
            return throwError(line, "Direction \'" + s + "\' not recognized");
          }
        } else
        {
          if (dir[i].equals("left"))
          {
            currentBranch.add(new Task(Instructions.WALK_LEFT));
          } else if (dir[i].equals("right"))
          {
            currentBranch.add(new Task(Instructions.WALK_RIGHT));
          } else if (dir[i].equals("down"))
          {
            currentBranch.add(new Task(Instructions.WALK_DOWN));
          } else if (dir[i].equals("up"))
          {
            currentBranch.add(new Task(Instructions.WALK_UP));
          } else
          {
            return throwError(line, "Direction \'" + s + "\' not recognized");
          }
        }

      }
    } else if (s.startsWith("jog"))
    {
      s=s.replace("jog ", "");

      String[] dir = s.split(" ");
      for (int i=0; i < dir.length; i++)
      {
        if (i+1 == dir.length)
        {
          if (dir[i].equals("left"))
          {
            return new Task(Instructions.JOG_LEFT);
          } else if (dir[i].equals("right"))
          {
            return new Task(Instructions.JOG_RIGHT);
          } else if (dir[i].equals("down"))
          {
            return new Task(Instructions.JOG_DOWN);
          } else if (dir[i].equals("up"))
          {
            return new Task(Instructions.JOG_UP);
          } else
          {
            return throwError(line, "Direction \'" + s + "\' not recognized");
          }
        } else
        {
          if (dir[i].equals("left"))
          {
            currentBranch.add(new Task(Instructions.JOG_LEFT));
          } else if (dir[i].equals("right"))
          {
            currentBranch.add(new Task(Instructions.JOG_RIGHT));
          } else if (dir[i].equals("down"))
          {
            currentBranch.add(new Task(Instructions.JOG_DOWN));
          } else if (dir[i].equals("up"))
          {
            currentBranch.add(new Task(Instructions.JOG_UP));
          } else
          {
            return throwError(line, "Direction \'" + s + "\' not recognized");
          }
        }
      }

    } else if (s.startsWith("change"))
    {
      s =s.replace("change ", "");

      for (String type: NPCType.allStrings())
      {
        if (s.equalsIgnoreCase(type))
        {

          return new TaskWithString(Instructions.CHANGE_SPRITE, s);
        }
      }
      return throwError(line, "NPC name \'" + s + "\' not recognized");
    } else if (s.startsWith("pause"))
    {
      s=s.replace("pause ", "");
      try
      {
        int frames = Integer.parseInt(s);
        return new TaskWithNumber(Instructions.WAIT, frames);
      } catch (NumberFormatException e)
      {
        return throwError(line, s + " cannot be resolved to an integer");
      }
    } else if (s.startsWith("stop music")) {
      return new Task(Instructions.STOP_SONG);
    } else if (s.startsWith("lock"))
    {
      return new Task(Instructions.LOCK);
    } else if (s.startsWith("unlock"))
    {
      return new Task(Instructions.UNLOCK);
    } else if (s.startsWith("turn"))
    {
      s = s.replace("turn ", "");
      if (s.equalsIgnoreCase("left"))
      {
        return new Task(Instructions.TURN_LEFT);
      } else if (s.equalsIgnoreCase("right"))
      {
        return new Task(Instructions.TURN_RIGHT);
      } else if (s.equalsIgnoreCase("up"))
      {
        return new Task(Instructions.TURN_UP);
      } else if (s.equalsIgnoreCase("down"))
      {
        return new Task(Instructions.TURN_DOWN);
      }else
      {
        return throwError(line, "Direction \'" + s + "\' not recognized");
      }
    } else if (s.startsWith("say"))
    {
      s = s.replace("say ", "");
      return new TaskWithString(Instructions.SAY, s);
    } else if (s.startsWith("ask"))
    {
      s = s.replace("ask ", "");
      String[] words = s.split("\"");

      String[] newWords = new String[words.length/2];
      TaskWithBranch[] newBranches = new TaskWithBranch[newWords.length];

      for (int i=0; i < newWords.length; i++)
      {
        newWords[i]=words[(i*2)+1];
        if (i != 0) 
        {
          checkForBranch(newWords[i]);
        }
        newBranches[i] = getTaskWithBranch(newWords[i]);
      }

      return new TaskWithStringArray(Instructions.ASK, newWords,
          newBranches);

    } else if (s.startsWith("branch"))
    {
      s = s.replace("branch ", "");
      checkForBranch(s);

      currentBranch = getTaskWithBranch(s).getBranch();
      return null;
    } else if (s.startsWith("fadeout"))
    {
      return new Task(Instructions.FADE_OUT);
    } else if (s.startsWith("fadein"))
    {
      return new Task(Instructions.FADE_IN);
    } else if (s.startsWith("run "))
    {
      s = s.replace("run ","");

      return new TaskWithString(Instructions.RUN_SCRIPT, s);
    } else if (s.startsWith("npc"))
    {
      s = s.replace("npc ", "");

      npc = Game.getMap().findNPC(s);
      if (npc == null) 
      {
        throwError(line, "NPC \'" + s + "\' not recognized");
      }
    } else if (s.startsWith("sprite"))
    {
      s = s.replace("sprite ", "");
      npc.setAttributes(NPCType.toEnum(s));

    } else if (s.startsWith("at"))
    {
      s = s.replace("at ","");
      String[] args = s.split(",");


      npc.setTileLocation(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
      npc.setVisible(true);
    } else if (s.startsWith("load"))
    {
      s = s.replace("load ", "");

      return new TaskWithString(Instructions.LOAD_MAP, s);
    } else if (s.startsWith("retile"))
    {
      s = s.replace("retile ", "");
      String[] split = s.split(" ");

      return new TaskWithNumberAndLocation(Instructions.RETILE,
          Integer.parseInt(split[0]), Integer.parseInt(split[1]), 
          Integer.parseInt(split[2]));
    } else if (s.startsWith("@walk"))
    {
      s = s.replace("@walk ", "");
      String[] dir = s.split(" ");
      for (int i=0; i < dir.length; i++)
      {
        if (i == dir.length-1)
        {
          if (dir[i].equals("left"))
          {
            return new Task(Instructions.CLYDE_WALK_LEFT);
          } else if (dir[i].equals("right"))
          {
            return new Task(Instructions.CLYDE_WALK_RIGHT);
          }else if (dir[i].equals("down"))
          {
            return new Task(Instructions.CLYDE_WALK_DOWN);
          } else if (dir[i].equals("up"))
          {
            return new Task(Instructions.CLYDE_WALK_UP);
          } else
          {
            throwError(line, "Direction '" + dir[i] + "' not recongized");
          }
        } else
        {
          if (dir[i].equals("left"))
          {
            currentBranch.add(new Task(Instructions.CLYDE_WALK_LEFT));
          } else if (dir[i].equals("right"))
          {
            currentBranch.add(new Task(Instructions.CLYDE_WALK_RIGHT));
          } else if (dir[i].equals("down"))
          {
            currentBranch.add(new Task(Instructions.CLYDE_WALK_DOWN));
          } else if (dir[i].equals("up"))
          {
            currentBranch.add(new Task(Instructions.CLYDE_WALK_UP));
          } else
          {
            throwError(line, "Direction '" + dir[i] + "' not recongized");
          }
        }

      }
    } else if (s.startsWith("call"))
    {

      s = s.replace("call", "");

      if (s.contains(","))
      {
        String[] args = s.split(",");

        TaskWithBranches task = new TaskWithBranches(Instructions.RUN_BRANCHES);

        for (String element: args)
        {
          element = element.trim();
          checkForBranch(element);
          task.addBranch(getTaskWithBranch(element));
        }

        task.finalize();
        return task;

      } else
      {
        s = s.replace("call", "");
        s = s.trim();
        checkForBranch(s);

        TaskWithBranch task = getTaskWithBranch(s);
        task.setInstruction(Instructions.RUN_BRANCH);

        return task;
      }

    } else if (s.startsWith("end"))
    {
      return new Task(Instructions.END);
    } else if (s.startsWith("@turn"))
    {
      s = s.replace("@turn ", "");
      if (s.equals("left"))
      {
        return new Task(Instructions.CLYDE_TURN_LEFT);
      } else if (s.equals("right")) {
        return new Task(Instructions.CLYDE_TURN_RIGHT);
      } else if (s.equals("up"))
      {
        return new Task(Instructions.CLYDE_TURN_UP);
      } else if (s.equals("down"))
      {
        return new Task(Instructions.CLYDE_TURN_DOWN);
      } else
      {
        return throwError(line, "Direction \'" + s + "\' not recognized");
      }
    } else if (s.startsWith("play"))
    {
      s = s.replace("play ", "");

      return new TaskWithString(Instructions.PLAY_SONG, s);
    } else if (s.startsWith("align"))
    {
      s = s.replace("align", "");
      s = s.trim();
      if (s.equals("left"))
      {
        return new Task(Instructions.ALIGN_LEFT);
      } else if (s.equals("right"))
      {
        return new Task(Instructions.ALIGN_RIGHT);
      } else if (s.equals("down"))
      {
        return new Task(Instructions.ALIGN_DOWN);
      } else if (s.equals("up"))
      {
        return new Task(Instructions.ALIGN_UP);
      } else if (s.equals("")) {
        return new Task(Instructions.ALIGN);
      } else
      {
        return throwError(line, "Direction \'" + s + "\' not recognized");
      }
    } else if (s.startsWith("goto")) {
      s = s.replace("goto ", "");
      String[] str = s.split(" ");

      return new TaskWithLocation(Instructions.GO_TO, 
          Integer.parseInt(str[0]), Integer.parseInt(str[1]));
    } else if (s.startsWith("@goto"))
    {
      s = s.replace("@goto ", "");
      String[] str = s.split(" ");

      return new TaskWithLocation(Instructions.CLYDE_GO_TO, 
          Integer.parseInt(str[0]), Integer.parseInt(str[1]));
    } else if (s.startsWith("give")){
      s = s.replace("give ", "");

      return new TaskWithString(Instructions.GIVE_ITEM, s);
    } else if (s.startsWith("if"))
    {
      String predicate = s.substring(0, s.indexOf(","));
      String toDo = s.substring(s.indexOf(",")+1, s.length());
      toDo = toDo.trim();
      predicate = predicate.replace("if", "");
      predicate = predicate.trim();

      boolean reverse = predicate.startsWith("!");
      if (reverse) 
      {
        predicate = predicate.replace("!", "");
      }

      try
      {
        int flag = Integer.parseInt(predicate);
        return new TaskWithTask(Instructions.IF_BOOLEAN, interpret(toDo, line), 
            flag, reverse);

      } catch (NumberFormatException nfe)
      {
        if (predicate.startsWith("$"))
        {
          predicate = predicate.replace("$", "");
          int compare = Integer.parseInt(predicate);

          return new TaskWithTask(Instructions.IF_MONEY, interpret(toDo, line), 
              compare, reverse);
        }
        else if (predicate.startsWith("#"))
        {
          predicate = predicate.replace("#", "");
          int compare = Integer.parseInt(predicate);

          return new TaskWithTask(Instructions.IF_CONFIDENCE, interpret(toDo, 
              line), compare, reverse);
        }
        else 
        {
          if (predicate.equalsIgnoreCase("caspian"))
          {
            return new TaskWithTask(Instructions.IF_CHAR, interpret(toDo, line),
                Characters.CASPIAN, reverse); 
          } else if (predicate.equalsIgnoreCase("flannery"))
          {
            return new TaskWithTask(Instructions.IF_CHAR, interpret(toDo, line),
                Characters.FLANNERY, reverse); 
          } else if (predicate.equalsIgnoreCase("sierra"))
          {
            return new TaskWithTask(Instructions.IF_CHAR, interpret(toDo, line),
                Characters.SIERRA, reverse); 
          } else if (predicate.equalsIgnoreCase("bruno"))
          {
            return new TaskWithTask(Instructions.IF_CHAR, interpret(toDo, line),
                Characters.BRUNO, reverse); 
          } else
          {
            return throwError(line, "Predicates must either correspond to a "
                + "boolean flag number, a monetary value, a confidence value,"
                + "or a playable character.");
          }
        }
      }

    } else if (s.startsWith("else"))
    {
      if (s.contains(","))
      {
        s = s.replaceFirst(",", "");
      } else
      {
        throwError(line, "'else' keyword must be proceeded by a comma (,)");
      }

      s = s.replace("else", "");
      s = s.trim();

      return new TaskWithBoolean(Instructions.ELSE, interpret(s, line));
    }
    else if (s.startsWith("pay"))
    {
      s = s.replace("pay ", "");
      if (s.startsWith("$"))
      {
        s = s.replace("$", "");

        return new TaskWithNumber(Instructions.PAY_MONEY, Integer.parseInt(s));
      } else if (s.startsWith("#"))
      {
        s = s.replace("#", "");

        return new TaskWithNumber(Instructions.PAY_CONFIDENCE, 
            Integer.parseInt(s));
      } else 
      {
        return throwError(line, "Cannot use pay keyword without either $ or # token");
      }
    } else if (s.startsWith("flag"))
    {
      s = s.replace("flag ", "");
      try
      {
        int bool = Integer.parseInt(s);

        if (bool > 255 || bool < 0)
        {
          throwError(line, "Flag values must be between 0 and 255 (inclusive)");
        } else
        {
          return new TaskWithNumber(Instructions.FLAG_BOOLEAN, bool);
        }
      } catch (NumberFormatException nfe)
      {
        throwError(line, "Flag values must be integers from 0 to 255 (inclusive)");
      }
    } else if (s.startsWith("block"))
    {
      s = s.replace("block ", "");
      String[] split = s.split(" ");

      try 
      {
        int argx = Integer.parseInt(split[0]);
        int argy = Integer.parseInt(split[1]);

        return new TaskWithLocation(Instructions.BLOCK, argx, argy);
      } catch (NumberFormatException nfe)
      {
        return throwError(line, s + " cannot be resolved to two integers");
      }

    } else if (s.startsWith("unblock"))
    {
      s = s.replace("unblock ", "");
      String[] split = s.split(" ");

      try 
      {
        int argx = Integer.parseInt(split[0]);
        int argy = Integer.parseInt(split[1]);

        return new TaskWithLocation(Instructions.UNBLOCK, argx, argy);
      } catch (NumberFormatException nfe)
      {
        return throwError(line, s + " cannot be resolved to two integers");
      }
    } else if (s.startsWith("sync"))
    {
      s = s.replace("sync", "");
      s = s.trim();

      return new Task(Instructions.SYNC);
    } else if (s.startsWith("freeze"))
    {
      s = s.replace("freeze", "");
      s = s.trim();
      
      try
      {
        int input = Integer.parseInt(s);
        return new TaskWithNumber(Instructions.FREEZE, input);
      } catch (NumberFormatException e)
      {
        return throwError(line, "freeze only takes integer arguments");
      }
      
    }
    else if (!(s.startsWith("//") || s.startsWith(" ") || s.isEmpty()))
    {
      return throwError(line, "Keyword \'" + s + "\' not recognized");
    }

    return null;
  }

  /**
   * Runs the script
   */
  public void run()
  { 
    
    if (!syntaxError && !isRunning) 
    {
      ThreadHandler.newThread(this);
      isRunning = true;
    }
  }

  /**
   * Prints an error message to the console, indicating a syntax error in the script
   * @param line The line number of the error
   * @param message The error message
   * @return null, beacuse no Task can be created from incorrect syntax
   */
  public Task throwError(int line, String message)
  {
    System.out.println("Syntax error in script " + script + ", line " 
        + line + ":");
    System.out.println(message);
    syntaxError = true;

    return null;
  }

  public void setExecution(Execution newEx)
  {
    execution = newEx;
  }

  public Execution getExecution()
  {
    return execution;
  }

  public String getScript()
  {
    return script;
  }

  public boolean isRunning()
  {
    return isRunning;
  }

  public boolean isInterrupted()
  {
    return isInterrupted;
  }

  public void setIsRunning(boolean b)
  {
    isRunning = b;
  }

  public void setIsInterrupted(boolean b)
  {
    isInterrupted = b;
  }

  public void setAbortionIndex(int i)
  {
    abortionIndex = i;
  }

  private void setCurrentBranch(InstructionList list)
  {
    currentBranch = list;
  }

  public void pause()
  {
    ThreadHandler.interrupt(this);
  }

  public void resume()
  {
    ThreadHandler.unlock(this);
  }

  public void checkForBranch(String s)
  {
    boolean there = false;

    for (TaskWithBranch task: branches)
    {
      if (task.getBranch().getBranchName().equals(s)) 
      {
        there = true;
        break;
      }
    }

    if (!there)
    {
      branches.add(new TaskWithBranch(null, new InstructionList(s)));
    }
  }

  public TaskWithBranch getTaskWithBranch(String s)
  {
    for (TaskWithBranch task: branches)
    {
      if (task.getBranch().getBranchName().equals(s)) 
      {
        return task;
      }
    }

    return null;
  }

}
