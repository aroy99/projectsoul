package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.Person;
import komorebi.projsoul.script.commands.abstracts.CommandOnAnyPerson;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class TurnCommand extends CommandOnAnyPerson {

  private Face[] directions;
  
  public static String keyword()
  {
    return "turn";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    
    String[] instructions = data.split(" ");
    directions = new Face[instructions.length];
    
    for (int i = 0; i < instructions.length; i++)
    {
      if (Face.valueOf(instructions[i].toUpperCase()) != null)
      {
        directions[i] = Face.valueOf(instructions[i].toUpperCase());
      } else
        throw new InvalidScriptSyntaxException(instructions[i] + " is "
            + "not a valid direction");
    }
    
  }

  @Override
  public void execute(Person person) {
    for (Face direction: directions)
    {
      person.turn(direction); 
    }
       
  }
  
  
}
