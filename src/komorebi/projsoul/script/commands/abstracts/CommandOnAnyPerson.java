package komorebi.projsoul.script.commands.abstracts;

import komorebi.projsoul.entities.Person;

public abstract class CommandOnAnyPerson extends Command {

  public abstract void execute(Person person);
  
}
