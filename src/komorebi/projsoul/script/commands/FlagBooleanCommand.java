package komorebi.projsoul.script.commands;

import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.decision.Flag;
import komorebi.projsoul.script.decision.Flags;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;

public class FlagBooleanCommand extends CommandNoSubject {

  private Flag flag;

  @Override
  public void interpret(String data, int line) throws 
    InvalidScriptSyntaxExceptionWithLine {
   
    if (data.contains(" "))
      throw new InvalidScriptSyntaxExceptionWithLine("The identifier "
          + "for a flag cannot contain any spaces", line);
    
    if (!Flags.flagExists(data))
      throw new InvalidScriptSyntaxExceptionWithLine("No such flag "
          + "as " + data + " is defined in res/flags/flags.txt", line);
    
    flag = Flags.getFlagWithIdentifier(data);
    
  }

  @Override
  public void execute() {
    flag.setValue(true);
  }
  
}
