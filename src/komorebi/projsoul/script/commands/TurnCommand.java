package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.Person;
import komorebi.projsoul.script.commands.abstracts.CommandOnAnyPerson;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;

public class TurnCommand extends CommandOnAnyPerson {

  private Face[] directions;
  
  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine {
    
    String[] instructions = data.split(" ");
    directions = new Face[instructions.length];
    
    for (int i = 0; i < instructions.length; i++)
    {
      if (Face.valueOf(instructions[i].toUpperCase()) != null)
      {
        directions[i] = Face.valueOf(instructions[i].toUpperCase());
      } else
        throw new InvalidScriptSyntaxExceptionWithLine(instructions[i] + " is "
            + "not a valid direction", line);
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
